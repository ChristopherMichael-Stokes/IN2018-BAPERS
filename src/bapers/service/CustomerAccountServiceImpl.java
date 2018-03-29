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
import bapers.data.dataAccess.AddressJpaController;
import bapers.data.dataAccess.CustomerAccountJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Address;
import bapers.data.domain.CustomerAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author EdgarLaw
 */
public class CustomerAccountServiceImpl implements CustomerAccountService{
    private final CustomerAccountJpaController customerController;
    private final AddressJpaController addressController;
    private final ObservableList<CustomerAccount> customers;
    
    CustomerAccountServiceImpl(){
        customerController = new CustomerAccountJpaController(EMF);
        addressController = new AddressJpaController(EMF);
        customers = FXCollections.observableArrayList(customerController.findCustomerAccountEntities());
    }
    public ObservableList<CustomerAccount> getCustomerAccounts(){
        return customers;
    }
    public void addCustomer(CustomerAccount account, Address address) throws PreexistingEntityException, Exception {
        account.getAddressList().add(address);
        customerController.create(account);
    }
    public boolean customerExists(String accountNumber){
        return customerController.findCustomerAccount(Integer.parseInt(accountNumber)) != null;
    }
    public void updateAccount(CustomerAccount account, Address address) throws IllegalOrphanException, NonexistentEntityException, Exception {
        account.getAddressList().clear();
        account.getAddressList().add(address);
        customerController.edit(account);
        
    }

}
