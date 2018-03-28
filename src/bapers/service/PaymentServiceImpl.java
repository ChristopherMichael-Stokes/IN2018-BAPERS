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
import bapers.data.dataAccess.JobJpaController;
import bapers.data.dataAccess.PaymentInfoJpaController;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.CardDetails;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import bapers.utility.SimpleHash;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public PaymentServiceImpl() {
        paymentController = new PaymentInfoJpaController(EMF);
        cardController = new CardDetailsJpaController(EMF);
        jobController = new JobJpaController(EMF);
    }

    @Override
    public ObservableList<Job> getJobs(String accountNumber) {
        return jobController.findJobEntities().stream()
                .filter(j -> j.getFkAccountNumber().getAccountNumber()
                    .equals(accountNumber) && j.getAmountDue() > 0)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void addPayment(String accountNumber, int amountPaid, Date datePaid, 
            String... jobs) throws PreexistingEntityException, Exception {
        String transactionId = new SimpleDateFormat("dd/MM/yy:HH:mm:SS")
                .format(datePaid) + SimpleHash.getStringHash(jobs).substring(0,8);
        PaymentInfo pi = new PaymentInfo(transactionId, datePaid, 
                amountPaid, "cash");
        paymentController.create(pi);
    }

    @Override
    public void addPayment(String accountNumber, int amountPaid, Date datePaid, 
            String cardDigits, Date expiryDate, String cardType, String... jobs) 
            throws PreexistingEntityException, Exception {
        String transactionId = new SimpleDateFormat("dd/MM/yy:HH:mm:SS")
                .format(datePaid) + SimpleHash.getStringHash(jobs).substring(0,8);
        PaymentInfo pi = new PaymentInfo(transactionId, datePaid, 
                amountPaid, "card");
        paymentController.create(pi);
        CardDetails cd = new CardDetails(cardDigits, expiryDate, transactionId);
        cd.setCardType(cardType);
        cardController.create(cd);
        
    }

}
