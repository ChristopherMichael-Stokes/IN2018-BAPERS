/*
 * Copyright (c) 2018, EdgarLaw
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package bapers.service;

import static bapers.BAPERS.EMF;
import bapers.data.dataAccess.CardDetailsJpaController;
import bapers.data.dataAccess.ContactJpaController;
import bapers.data.dataAccess.JobJpaController;
import bapers.data.dataAccess.PaymentInfoJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.CardDetails;
import bapers.data.domain.CardDetailsPK;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.DiscountBand;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.PaymentInfo;
import bapers.data.domain.TaskDiscount;
import static bapers.service.CustomerAccountService.DiscountPlan.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author EdgarLaw
 */
public class PaymentServiceImpl implements PaymentService {

    private final PaymentInfoJpaController paymentController;
    private final CardDetailsJpaController cardController;
    private final JobJpaController jobController;
    private final ContactJpaController contactController;

    /**
     *
     */
    public PaymentServiceImpl() {
        paymentController = new PaymentInfoJpaController(EMF);
        cardController = new CardDetailsJpaController(EMF);
        jobController = new JobJpaController(EMF);
        contactController = new ContactJpaController(EMF);
    }

    /**
     *
     * @param accountNumber
     * @return
     */
    @Override
    public ObservableList<Job> getUnpaidJobs(int accountNumber) {
        return jobController.findJobEntities().stream()
                .filter(j -> j.getContact().getContactPK().getFkAccountNumber() == accountNumber
                    && j.getFkTransactionId() == null)
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));
                
//        return contactController.findContactEntities().stream()
//                .filter(c -> c.getContactPK().getFkAccountNumber() == accountNumber)
//                .map(Contact::getJobList)
//                .flatMap(j -> j.stream()
//                        .filter(jb -> jb.getFkTransactionId() == null))
//                .collect(Collectors
//                        .toCollection(FXCollections::observableArrayList));
    }
        

    /**
     *
     * @param datePaid
     * @param card
     * @param jobs
     * @throws PreexistingEntityException
     * @throws Exception
     */
    @Override
    public void addPayment(Date datePaid, int amount, List<Job> jobs) 
            throws PreexistingEntityException, Exception {
        PaymentInfo pi = new PaymentInfo(0, datePaid, false);
        pi.setAmount(amount);
        pi.setJobList(jobs);
        
        int count = paymentController.getPaymentInfoCount(); //Will break if there are multiple clients adding payments concurrently
        paymentController.create(pi);
        
        //transaction id uses auto increment, so need to use lookup to get the
        //id of the payment        
        pi = paymentController.findPaymentInfoEntities(1, count).get(0);
        jobs = pi.getJobList();
        for (Job j : jobs) {
            j.setFkTransactionId(pi);
            jobController.edit(j);
        } 
    }

    /**
     *
     * @param datePaid
     * @param cardDigits
     * @param expiryDate
     * @param cardType
     * @param jobs
     * @throws PreexistingEntityException
     * @throws IllegalOrphanException
     * @throws Exception
     */
    @Override
    public void addPayment(Date datePaid, int amount, String cardDigits, 
            String expiryDate, String cardType, List<Job> jobs)
            throws PreexistingEntityException, IllegalOrphanException, Exception {
        PaymentInfo pi = new PaymentInfo(0, datePaid, true);
        pi.setAmount(amount);
        CardDetailsPK cpk = new CardDetailsPK(cardDigits, expiryDate);
        CardDetails cd;
        if ((cd = cardController.findCardDetails(cpk)) == null) {
            cd = new CardDetails(cpk, cardType);
        }        
        cardController.create(cd);
        pi.setCardDetails(cd);
        pi.setJobList(jobs);
        paymentController.create(pi);
        //transaction id uses auto increment, so need to use lookup to get the
        //id of the payment
        int count = paymentController.getPaymentInfoCount();
        pi = paymentController.findPaymentInfoEntities(1, count).get(0);
        for (Job j : jobs) {
            j.setFkTransactionId(pi);
            jobController.edit(j);
        }      
    }

    private double getFlatCost(Job job) {
        double cost = 0d;        
        //find cost
        //should work but doesnt
        cost = job.getJobComponentList().stream()
                .map(JobComponent::getComponentTaskList)
                .flatMap(jc -> jc.stream())
                .map(ct -> (double)ct.getTask().getPrice())
                .reduce(cost, (a, b) -> a + b);
        
        //account for urgency
        /*  for some reason, retrieving the deadline from the db gives the 
            actual time minus 1 hr, so add 1  */
//        double time = (job.getDeadline().getTime() + 3.6e6) / 3.6e6d;
        if (job.getAddedPercentage() != null)
            cost += (cost*(job.getAddedPercentage()/100f));
        
        return Math.round(cost) / 100d;
    }
    
    @Override
    public double getJobCost(int jobId) {
        Job job = jobController.findJob(jobId);
        double cost = getFlatCost(job);
        //account for discount plan 
        CustomerAccount account = job.getContact().getCustomerAccount();
        switch(getPlan(account.getDiscountType())) {
            case none: 
                return cost;
            case fixed:
                //should only have one discount band
                float rate = account.getDiscountBandList()
                        .get(0).getPercentage()/100f;
                return cost -= cost*rate;
            case variable:
                cost = 0d;
                List<TaskDiscount> td = account.getTaskDiscountList();
                for (JobComponent j : job.getJobComponentList()) {
                    for (ComponentTask t : j.getComponentTaskList()) {
                        Optional<TaskDiscount> otDiscount = td.stream()
                                .filter(ct -> ct.getTask().equals(t.getTask()))
                                .findAny();
                        double taskCost = t.getTask().getPrice() / 100d;
                        if (otDiscount.isPresent())
                            cost += taskCost + (taskCost * (otDiscount.get().getPercentage()/100f));
                        else 
                            cost += t.getTask().getPrice();
                    }
                }
                return cost;
            case flexible:
                //find all transactions in past month                
                Calendar month = Calendar.getInstance();
                month.setTime(new Date());
                month.set(Calendar.DAY_OF_MONTH, month.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date monthStart = month.getTime();
                
                double moneySpent_ = paymentController.findPaymentInfoEntities()
                        .stream()
                        .filter(pi -> pi.getDatePaid().after(monthStart)) 
                        .map(PaymentInfo::getJobList)
                        .flatMap(x -> x.stream())
                        .filter(j -> j.getContact().getCustomerAccount().equals(account))
                        .map(this::getFlatCost)
                        .reduce(cost, (accumulator, payment) -> accumulator + payment);
                int moneySpent = (int) moneySpent_ * 100;
                
                //work out which band to apply
                DiscountBand band = account.getDiscountBandList().get(0);
                for (DiscountBand db : account.getDiscountBandList()) {
                    if (moneySpent > db.getDiscountBandPK().getPrice())
                        band = db;
                }
                return cost -= cost*(band.getPercentage()/100f);
            default: return cost;
        }
    }

}
