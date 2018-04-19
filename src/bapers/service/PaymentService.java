/*
 * Copyright (c) 2018, chris
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

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import javafx.collections.ObservableList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chris
 */
public interface PaymentService {

    /**
     *
     * @param accountNumber to find unpaid jobs for
     * @return list of unpaid jobs
     */
    public ObservableList<Job> getUnpaidJobs(int accountNumber);
    
    /**
     *
     * @param jobId of job to get cost for
     * @return cost for job
     */
    public double getJobCost(int jobId);

    /**
     *
     * @param datePaid date of payment
     * @param amount of money paid
     * @param jobs to add payment for
     * @throws PreexistingEntityException if payment item already exists
     * @throws Exception if database connection fails
     */
    public void addPayment(Date datePaid, int amount, List<Job> jobs) 
            throws PreexistingEntityException, Exception;

    /**
     * used for card payments
     * @param datePaid date of payment
     * @param amount of money paid
     * @param cardDigits last four digits of card
     * @param expiryDate expiry date of card
     * @param cardType type of card
     * @param jobs to add payment for
     * @throws PreexistingEntityException if payment already exists
     * @throws IllegalOrphanException if foreign keys are invalidated
     * @throws Exception if database connection fails
     */
    public void addPayment(Date datePaid, int amount, String cardDigits, 
            String expiryDate, String cardType, List<Job> jobs)
            throws PreexistingEntityException, IllegalOrphanException, Exception;
    
}
