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
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class PaymentServiceTest {
    
    public PaymentServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getJobs method, of class PaymentService.
     */
    @Test
    public void testGetJobs() {
        System.out.println("getJobs");
        int accountNumber = 0;
        PaymentService instance = new PaymentServiceImpl();
        ObservableList<Job> expResult = null;
        ObservableList<Job> result = instance.getJobs(accountNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPayment method, of class PaymentService.
     */
    @Test
    public void testAddPayment_PaymentInfo_List() throws Exception {
        System.out.println("addPayment");
        PaymentInfo payment = null;
        List<Job> jobs = null;
        PaymentService instance = new PaymentServiceImpl();
        instance.addPayment(payment, jobs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPayment method, of class PaymentService.
     */
    @Test
    public void testAddPayment_5args() throws Exception {
        System.out.println("addPayment");
        PaymentInfo payment = null;
        String cardDigits = "";
        Date expiryDate = null;
        String cardType = "";
        List<Job> jobs = null;
        PaymentService instance = new PaymentServiceImpl();
        instance.addPayment(payment, cardDigits, expiryDate, cardType, jobs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

        
}
