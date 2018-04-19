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
import bapers.data.domain.Contact;
import bapers.data.domain.CustomerAccount;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public interface CustomerAccountService {
    
    public enum DiscountPlan {
        none, fixed, variable, flexible;
        public static DiscountPlan getPlan(int plan) {
            switch(plan){
                case 0: return none;
                case 1: return fixed;
                case 2: return variable;
                case 3: return flexible;
                default: return null;
            }                
        }
    };

    /**
     *
     * @return a list of all the customer accounts stored in the system
     */
    public ObservableList<CustomerAccount> getCustomerAccounts();

    /**
     *
     * @param account the new account to be added
     * @param address the address of the new customer
     * @throws bapers.data.dataAccess.exceptions.IllegalOrphanException
     * @throws bapers.data.dataAccess.exceptions.NonexistentEntityException
     */
    public CustomerAccount addCustomer(CustomerAccount account, String addressLine1, 
            String postcode, String city);
    
    public CustomerAccount addCustomer(CustomerAccount account);
    public void modifyAddress(CustomerAccount account, String addressLine2, String region);
    public List<CustomerAccount> findCustomers(String accountMatches);
    public Contact addContact(Contact c, CustomerAccount ca) throws PreexistingEntityException, Exception ;

    /**
     *
     * @param accountNumber of the customer to look up
     * @return true if the customer record is in the database
     */
    public boolean customerExists(String accountNumber);

    /**
     *
     * @param account the modified customer account that is to be edited
     * @param address the modified address
     * @throws IllegalOrphanException if there are any other entities which rely 
     * upon data that has now been changed
     * @throws NonexistentEntityException if the account does not already exist
     * @throws Exception if db connection fails
     */
    public void updateAccount(CustomerAccount account, Address address) 
            throws IllegalOrphanException, NonexistentEntityException, Exception;
    
    /**
     *
     * @param account the modified customer account that is to be edited
     * @throws IllegalOrphanException if there are any other entities which rely 
     * upon data that has now been changed
     * @throws NonexistentEntityException if the account does not already exist
     * @throws Exception if db connection fails
     */
    public void updateAccount(CustomerAccount account) 
            throws IllegalOrphanException, NonexistentEntityException, Exception;
    
    public CustomerAccount setAccountActive(CustomerAccount ca ,boolean active);
}
