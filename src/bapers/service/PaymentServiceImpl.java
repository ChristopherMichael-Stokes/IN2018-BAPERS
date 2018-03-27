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
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author EdgarLaw
 */
public class PaymentServiceImpl implements PaymentService {

    private final PaymentInfoJpaController PaymentController;
    private final CardDetailsJpaController CardController;
    private final JobJpaController jobController;
    private final ObservableList<Job> job;

    public PaymentServiceImpl() {
        PaymentController = new PaymentInfoJpaController(EMF);
        CardController = new CardDetailsJpaController(EMF);
        jobController = new JobJpaController(EMF);
        job = FXCollections.observableArrayList(jobController.findJobEntities());
    }

    @Override
    public ObservableList<Job> getJobs(String accountNumber) {
        ObservableList<Job> specifyJob = FXCollections.observableArrayList();
        for(int n = 0; n < job.size(); ++n) {
            if (job.get(n).getFkAccountNumber().getAccountNumber() == accountNumber && job.get(n).getAmountDue() > 0) {
                specifyJob.add(job.get(n));

            }
        }
        return specifyJob;
    }

    @Override
    public void addPayment(String accountNumber, String... jobs
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPayment(String accountNumber, String cardDigits,
            DateTime expiryDate, String cardType, String... jobs
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
