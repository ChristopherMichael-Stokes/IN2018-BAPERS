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

    /**
     *
     */
    public CustomerAccountServiceImpl() {
        customerController = new CustomerAccountJpaController(EMF);
        addressController = new AddressJpaController(EMF);
        contactController = new ContactJpaController(EMF);
    }

    /**
     *
     * @return a list of all the customer accounts stored in the system
     */
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

    /**
     *
     * @param c
     * @param ca
     * @return
     * @throws PreexistingEntityException
     * @throws Exception
     */
    @Override
    public Contact addContact(Contact c, CustomerAccount ca)  throws PreexistingEntityException, Exception {
        Contact contact = contactController.findContact(c.getContactPK());
        if (contact == null) {
            c.setCustomerAccount(ca);
            contactController.create(contact);
        }
        return contact == null ? c : contact;
    }

    /**
     *
     * @param accountMatches
     * @return
     */
    @Override
    public List<CustomerAccount> findCustomers(String accountMatches) {
        return customerController.findCustomerAccountEntities().stream()
                .filter(ca -> ca.getAccountHolderName().contains(accountMatches))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param account the new account to be added
     * @param addressLine1
     * @param postcode
     * @param city
     * @return 
     */
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

    /**
     *
     * @param accountNumber of the customer to look up
     * @return true if the customer record is in the database
     */
    @Override
    public boolean customerExists(String accountNumber) {

        return customerController
                .findCustomerAccount(Short.parseShort(accountNumber)) != null;
    }

    /**
     *
     * @param account the modified customer account that is to be edited
     * @param address the modified address
     * @throws IllegalOrphanException if there are any other entities which rely
     * upon data that has now been changed
     * @throws NonexistentEntityException if the account does not already exist
     * @throws Exception if db connection fails
     */
    @Override
    public void updateAccount(CustomerAccount account, Address address)
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        customerController.edit(setAddress(account, address));
    }

    /**
     *
     * @param account the modified customer account that is to be edited
     * @throws IllegalOrphanException if there are any other entities which rely
     * upon data that has now been changed
     * @throws NonexistentEntityException if the account does not already exist
     * @throws Exception if db connection fails
     */
    @Override
    public void updateAccount(CustomerAccount account)
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        customerController.edit(account);
    }

    /**
     *
     * @param account
     * @return
     */
    @Override
    public CustomerAccount addCustomer(CustomerAccount account) {
        return addCustomer_(account);
    }

    /**
     *
     * @param account
     * @param addressLine2
     * @param region
     */
    @Override
    public void modifyAddress(CustomerAccount account, String addressLine2, String region) {
        Address address = account.getAddressList().get(0);
        address.setAddressLine2(addressLine2);
        address.setRegion(region);
        setAddress(account, address);
    }

    /**
     *
     * @param ca
     * @param active
     * @return
     */
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
