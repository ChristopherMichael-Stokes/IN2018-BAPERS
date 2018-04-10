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
import bapers.data.domain.Contact;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import java.util.Date;
import java.util.List;
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
    public ObservableList<Job> getJobs(int accountNumber) {
        return contactController.findContactEntities().stream()
                .filter(c -> c.getContactPK().getFkAccountNumber() == accountNumber)
                .map(Contact::getJobList)
                .flatMap(j -> j.stream()
                        .filter(jb -> jb.getFkTransactionId() == null))
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));
    }
        

    /**
     *
     * @param payment
     * @param jobs
     * @throws PreexistingEntityException
     * @throws Exception
     */
    @Override
    public void addPayment(PaymentInfo payment, List<Job> jobs) 
            throws PreexistingEntityException, Exception {
        PaymentInfo pi = new PaymentInfo(0, new Date(), false);
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

    /**
     *
     * @param payment
     * @param cardDigits
     * @param expiryDate
     * @param cardType
     * @param jobs
     * @throws PreexistingEntityException
     * @throws IllegalOrphanException
     * @throws Exception
     */
    @Override
    public void addPayment(PaymentInfo payment, String cardDigits, 
            Date expiryDate, String cardType, List<Job> jobs)
            throws PreexistingEntityException, IllegalOrphanException, Exception {
        PaymentInfo pi = new PaymentInfo(0, new Date(), true);
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

}
