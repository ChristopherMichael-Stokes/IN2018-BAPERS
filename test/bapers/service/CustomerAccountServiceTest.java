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
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Address;
import bapers.data.domain.CustomerAccount;
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
public class CustomerAccountServiceTest {
    
    public CustomerAccountServiceTest() {
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
     * Test of getCustomerAccounts method, of class CustomerAccountService.
     */
    @Test
    public void testGetCustomerAccounts() {
        System.out.println("getCustomerAccounts");
        CustomerAccountService instance = new CustomerAccountServiceImpl();
        ObservableList<CustomerAccount> expResult = null;
        ObservableList<CustomerAccount> result = instance.getCustomerAccounts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCustomer method, of class CustomerAccountService.
     */
    @Test
    public void testAddCustomer() throws Exception {
        System.out.println("addCustomer");
        CustomerAccount account = null;
        Address address = null;
        CustomerAccountService instance = new CustomerAccountServiceImpl();
        instance.addCustomer(account, address);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of customerExists method, of class CustomerAccountService.
     */
    @Test
    public void testCustomerExists() {
        System.out.println("customerExists");
        String accountNumber = "";
        CustomerAccountService instance = new CustomerAccountServiceImpl();
        boolean expResult = false;
        boolean result = instance.customerExists(accountNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateAccount method, of class CustomerAccountService.
     */
    @Test
    public void testUpdateAccount() throws Exception {
        System.out.println("updateAccount");
        CustomerAccount account = null;
        Address address = null;
        CustomerAccountService instance = new CustomerAccountServiceImpl();
        instance.updateAccount(account, address);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class CustomerAccountServiceImpl implements CustomerAccountService {

        public ObservableList<CustomerAccount> getCustomerAccounts() {
            return null;
        }

        public void addCustomer(CustomerAccount account, Address address) throws PreexistingEntityException, Exception {
        }

        public boolean customerExists(String accountNumber) {
            return false;
        }

        public void updateAccount(CustomerAccount account, Address address) throws IllegalOrphanException, NonexistentEntityException, Exception {
        }
    }
    
}
