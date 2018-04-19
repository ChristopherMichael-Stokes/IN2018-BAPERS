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
import bapers.data.dataAccess.ContactJpaController;
import bapers.data.dataAccess.CustomerAccountJpaController;
import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import bapers.data.domain.Address;
import bapers.data.domain.Contact;
import bapers.data.domain.CustomerAccount;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author EdgarLaw
 */
public class CustomerAccountServiceImpl implements CustomerAccountService {

    private final CustomerAccountJpaController customerController;
    private final AddressJpaController addressController;
    private final ContactJpaController contactController;

    
    public CustomerAccountServiceImpl() {
        customerController = new CustomerAccountJpaController(EMF);
        addressController = new AddressJpaController(EMF);
        contactController = new ContactJpaController(EMF);
    }

   
    @Override
    public ObservableList<CustomerAccount> getCustomerAccounts() {
        return FXCollections
                .observableArrayList(
                        customerController.findCustomerAccountEntities()
                );
    }

    private CustomerAccount setAddress(CustomerAccount account, Address address) {
        List<Address> addressList = Collections.singletonList(address);
        account.setAddressList(addressList);
        return account;
    }

    private CustomerAccount addCustomer_(CustomerAccount account) {
        int accounts = customerController.getCustomerAccountCount();
        customerController.create(account);
        account = customerController.findCustomerAccountEntities(1, accounts).get(0);

        return account;
    }
    
    @Override
    public Contact addContact(Contact c, CustomerAccount ca)  throws PreexistingEntityException, Exception {
        Contact contact = contactController.findContact(c.getContactPK());
        if (contact == null) {
            c.setCustomerAccount(ca);
            contactController.create(contact);
        }
        return contact == null ? c : contact;
    }

    @Override
    public List<CustomerAccount> findCustomers(String accountMatches) {
        return customerController.findCustomerAccountEntities().stream()
                .filter(ca -> ca.getAccountHolderName().contains(accountMatches))
                .collect(Collectors.toList());
    }
   
    @Override
    public CustomerAccount addCustomer(CustomerAccount account, String addressLine1,
            String postcode, String city) {
        account = addCustomer_(account);
        Address address = new Address(addressLine1, postcode, city, account.getAccountNumber());

        try {
            customerController.edit(setAddress(account, address));
            return account;
        } catch (NonexistentEntityException | IllegalOrphanException ex) {
            System.err.println("adding address to customer\n" + ex.getMessage());
            System.exit(-1);
        } catch (Exception ex) {
            System.err.println("adding address to customer\n" + ex.getMessage());
            System.exit(-1);
        }
        return null;
    }

    @Override
    public boolean customerExists(String accountNumber) {

        return customerController
                .findCustomerAccount(Short.parseShort(accountNumber)) != null;
    }

    @Override
    public void updateAccount(CustomerAccount account, Address address)
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        customerController.edit(setAddress(account, address));
    }

    @Override
    public void updateAccount(CustomerAccount account)
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        customerController.edit(account);
    }

    @Override
    public CustomerAccount addCustomer(CustomerAccount account) {
        return addCustomer_(account);
    }

    @Override
    public void modifyAddress(CustomerAccount account, String addressLine2, String region) {
        Address address = account.getAddressList().get(0);
        address.setAddressLine2(addressLine2);
        address.setRegion(region);
        setAddress(account, address);
    }

    @Override
    public CustomerAccount setAccountActive(CustomerAccount ca, boolean active) {
        if (active) {
            ca.setLocked((short) 0);
        }
        if (!active) {
            ca.setLocked((short) 1);
        }
        return ca;
    }
}
