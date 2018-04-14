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
     * @param accountNumber
     * @return
     */
    public ObservableList<Job> getUnpaidJobs(int accountNumber);

    /**
     *
     * @param datePaid
     * @param amount
     * @param jobs
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void addPayment(Date datePaid, int amount, List<Job> jobs) 
            throws PreexistingEntityException, Exception;

    /**
     *
     * @param datePaid
     * @param amount
     * @param cardDigits
     * @param expiryDate
     * @param cardType
     * @param jobs
     * @throws PreexistingEntityException
     * @throws IllegalOrphanException
     * @throws Exception
     */
    public void addPayment(Date datePaid, int amount, String cardDigits, 
            String expiryDate, String cardType, List<Job> jobs)
            throws PreexistingEntityException, IllegalOrphanException, Exception;
    
    //TODO - needs other function to work out all unpaid jobs
}
