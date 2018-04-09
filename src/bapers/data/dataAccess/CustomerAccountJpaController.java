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
package bapers.data.dataAccess;

import bapers.data.dataAccess.exceptions.IllegalOrphanException;
import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.TaskDiscount;
import java.util.ArrayList;
import java.util.List;
import bapers.data.domain.Address;
import bapers.data.domain.Contact;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.DiscountBand;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class CustomerAccountJpaController implements Serializable {

    public CustomerAccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CustomerAccount customerAccount) {
        if (customerAccount.getTaskDiscountList() == null) {
            customerAccount.setTaskDiscountList(new ArrayList<TaskDiscount>());
        }
        if (customerAccount.getAddressList() == null) {
            customerAccount.setAddressList(new ArrayList<Address>());
        }
        if (customerAccount.getContactList() == null) {
            customerAccount.setContactList(new ArrayList<Contact>());
        }
        if (customerAccount.getDiscountBandList() == null) {
            customerAccount.setDiscountBandList(new ArrayList<DiscountBand>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TaskDiscount> attachedTaskDiscountList = new ArrayList<TaskDiscount>();
            for (TaskDiscount taskDiscountListTaskDiscountToAttach : customerAccount.getTaskDiscountList()) {
                taskDiscountListTaskDiscountToAttach = em.getReference(taskDiscountListTaskDiscountToAttach.getClass(), taskDiscountListTaskDiscountToAttach.getTaskDiscountPK());
                attachedTaskDiscountList.add(taskDiscountListTaskDiscountToAttach);
            }
            customerAccount.setTaskDiscountList(attachedTaskDiscountList);
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : customerAccount.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getAddressPK());
                attachedAddressList.add(addressListAddressToAttach);
            }
            customerAccount.setAddressList(attachedAddressList);
            List<Contact> attachedContactList = new ArrayList<Contact>();
            for (Contact contactListContactToAttach : customerAccount.getContactList()) {
                contactListContactToAttach = em.getReference(contactListContactToAttach.getClass(), contactListContactToAttach.getContactPK());
                attachedContactList.add(contactListContactToAttach);
            }
            customerAccount.setContactList(attachedContactList);
            List<DiscountBand> attachedDiscountBandList = new ArrayList<DiscountBand>();
            for (DiscountBand discountBandListDiscountBandToAttach : customerAccount.getDiscountBandList()) {
                discountBandListDiscountBandToAttach = em.getReference(discountBandListDiscountBandToAttach.getClass(), discountBandListDiscountBandToAttach.getDiscountBandPK());
                attachedDiscountBandList.add(discountBandListDiscountBandToAttach);
            }
            customerAccount.setDiscountBandList(attachedDiscountBandList);
            em.persist(customerAccount);
            for (TaskDiscount taskDiscountListTaskDiscount : customerAccount.getTaskDiscountList()) {
                CustomerAccount oldCustomerAccountOfTaskDiscountListTaskDiscount = taskDiscountListTaskDiscount.getCustomerAccount();
                taskDiscountListTaskDiscount.setCustomerAccount(customerAccount);
                taskDiscountListTaskDiscount = em.merge(taskDiscountListTaskDiscount);
                if (oldCustomerAccountOfTaskDiscountListTaskDiscount != null) {
                    oldCustomerAccountOfTaskDiscountListTaskDiscount.getTaskDiscountList().remove(taskDiscountListTaskDiscount);
                    oldCustomerAccountOfTaskDiscountListTaskDiscount = em.merge(oldCustomerAccountOfTaskDiscountListTaskDiscount);
                }
            }
            for (Address addressListAddress : customerAccount.getAddressList()) {
                CustomerAccount oldCustomerAccountOfAddressListAddress = addressListAddress.getCustomerAccount();
                addressListAddress.setCustomerAccount(customerAccount);
                addressListAddress = em.merge(addressListAddress);
                if (oldCustomerAccountOfAddressListAddress != null) {
                    oldCustomerAccountOfAddressListAddress.getAddressList().remove(addressListAddress);
                    oldCustomerAccountOfAddressListAddress = em.merge(oldCustomerAccountOfAddressListAddress);
                }
            }
            for (Contact contactListContact : customerAccount.getContactList()) {
                CustomerAccount oldCustomerAccountOfContactListContact = contactListContact.getCustomerAccount();
                contactListContact.setCustomerAccount(customerAccount);
                contactListContact = em.merge(contactListContact);
                if (oldCustomerAccountOfContactListContact != null) {
                    oldCustomerAccountOfContactListContact.getContactList().remove(contactListContact);
                    oldCustomerAccountOfContactListContact = em.merge(oldCustomerAccountOfContactListContact);
                }
            }
            for (DiscountBand discountBandListDiscountBand : customerAccount.getDiscountBandList()) {
                CustomerAccount oldCustomerAccountOfDiscountBandListDiscountBand = discountBandListDiscountBand.getCustomerAccount();
                discountBandListDiscountBand.setCustomerAccount(customerAccount);
                discountBandListDiscountBand = em.merge(discountBandListDiscountBand);
                if (oldCustomerAccountOfDiscountBandListDiscountBand != null) {
                    oldCustomerAccountOfDiscountBandListDiscountBand.getDiscountBandList().remove(discountBandListDiscountBand);
                    oldCustomerAccountOfDiscountBandListDiscountBand = em.merge(oldCustomerAccountOfDiscountBandListDiscountBand);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CustomerAccount customerAccount) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount persistentCustomerAccount = em.find(CustomerAccount.class, customerAccount.getAccountNumber());
            List<TaskDiscount> taskDiscountListOld = persistentCustomerAccount.getTaskDiscountList();
            List<TaskDiscount> taskDiscountListNew = customerAccount.getTaskDiscountList();
            List<Address> addressListOld = persistentCustomerAccount.getAddressList();
            List<Address> addressListNew = customerAccount.getAddressList();
            List<Contact> contactListOld = persistentCustomerAccount.getContactList();
            List<Contact> contactListNew = customerAccount.getContactList();
            List<DiscountBand> discountBandListOld = persistentCustomerAccount.getDiscountBandList();
            List<DiscountBand> discountBandListNew = customerAccount.getDiscountBandList();
            List<String> illegalOrphanMessages = null;
            for (TaskDiscount taskDiscountListOldTaskDiscount : taskDiscountListOld) {
                if (!taskDiscountListNew.contains(taskDiscountListOldTaskDiscount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TaskDiscount " + taskDiscountListOldTaskDiscount + " since its customerAccount field is not nullable.");
                }
            }
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressListOldAddress + " since its customerAccount field is not nullable.");
                }
            }
            for (Contact contactListOldContact : contactListOld) {
                if (!contactListNew.contains(contactListOldContact)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contact " + contactListOldContact + " since its customerAccount field is not nullable.");
                }
            }
            for (DiscountBand discountBandListOldDiscountBand : discountBandListOld) {
                if (!discountBandListNew.contains(discountBandListOldDiscountBand)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscountBand " + discountBandListOldDiscountBand + " since its customerAccount field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TaskDiscount> attachedTaskDiscountListNew = new ArrayList<TaskDiscount>();
            for (TaskDiscount taskDiscountListNewTaskDiscountToAttach : taskDiscountListNew) {
                taskDiscountListNewTaskDiscountToAttach = em.getReference(taskDiscountListNewTaskDiscountToAttach.getClass(), taskDiscountListNewTaskDiscountToAttach.getTaskDiscountPK());
                attachedTaskDiscountListNew.add(taskDiscountListNewTaskDiscountToAttach);
            }
            taskDiscountListNew = attachedTaskDiscountListNew;
            customerAccount.setTaskDiscountList(taskDiscountListNew);
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getAddressPK());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            customerAccount.setAddressList(addressListNew);
            List<Contact> attachedContactListNew = new ArrayList<Contact>();
            for (Contact contactListNewContactToAttach : contactListNew) {
                contactListNewContactToAttach = em.getReference(contactListNewContactToAttach.getClass(), contactListNewContactToAttach.getContactPK());
                attachedContactListNew.add(contactListNewContactToAttach);
            }
            contactListNew = attachedContactListNew;
            customerAccount.setContactList(contactListNew);
            List<DiscountBand> attachedDiscountBandListNew = new ArrayList<DiscountBand>();
            for (DiscountBand discountBandListNewDiscountBandToAttach : discountBandListNew) {
                discountBandListNewDiscountBandToAttach = em.getReference(discountBandListNewDiscountBandToAttach.getClass(), discountBandListNewDiscountBandToAttach.getDiscountBandPK());
                attachedDiscountBandListNew.add(discountBandListNewDiscountBandToAttach);
            }
            discountBandListNew = attachedDiscountBandListNew;
            customerAccount.setDiscountBandList(discountBandListNew);
            customerAccount = em.merge(customerAccount);
            for (TaskDiscount taskDiscountListNewTaskDiscount : taskDiscountListNew) {
                if (!taskDiscountListOld.contains(taskDiscountListNewTaskDiscount)) {
                    CustomerAccount oldCustomerAccountOfTaskDiscountListNewTaskDiscount = taskDiscountListNewTaskDiscount.getCustomerAccount();
                    taskDiscountListNewTaskDiscount.setCustomerAccount(customerAccount);
                    taskDiscountListNewTaskDiscount = em.merge(taskDiscountListNewTaskDiscount);
                    if (oldCustomerAccountOfTaskDiscountListNewTaskDiscount != null && !oldCustomerAccountOfTaskDiscountListNewTaskDiscount.equals(customerAccount)) {
                        oldCustomerAccountOfTaskDiscountListNewTaskDiscount.getTaskDiscountList().remove(taskDiscountListNewTaskDiscount);
                        oldCustomerAccountOfTaskDiscountListNewTaskDiscount = em.merge(oldCustomerAccountOfTaskDiscountListNewTaskDiscount);
                    }
                }
            }
            for (Address addressListNewAddress : addressListNew) {
                if (!addressListOld.contains(addressListNewAddress)) {
                    CustomerAccount oldCustomerAccountOfAddressListNewAddress = addressListNewAddress.getCustomerAccount();
                    addressListNewAddress.setCustomerAccount(customerAccount);
                    addressListNewAddress = em.merge(addressListNewAddress);
                    if (oldCustomerAccountOfAddressListNewAddress != null && !oldCustomerAccountOfAddressListNewAddress.equals(customerAccount)) {
                        oldCustomerAccountOfAddressListNewAddress.getAddressList().remove(addressListNewAddress);
                        oldCustomerAccountOfAddressListNewAddress = em.merge(oldCustomerAccountOfAddressListNewAddress);
                    }
                }
            }
            for (Contact contactListNewContact : contactListNew) {
                if (!contactListOld.contains(contactListNewContact)) {
                    CustomerAccount oldCustomerAccountOfContactListNewContact = contactListNewContact.getCustomerAccount();
                    contactListNewContact.setCustomerAccount(customerAccount);
                    contactListNewContact = em.merge(contactListNewContact);
                    if (oldCustomerAccountOfContactListNewContact != null && !oldCustomerAccountOfContactListNewContact.equals(customerAccount)) {
                        oldCustomerAccountOfContactListNewContact.getContactList().remove(contactListNewContact);
                        oldCustomerAccountOfContactListNewContact = em.merge(oldCustomerAccountOfContactListNewContact);
                    }
                }
            }
            for (DiscountBand discountBandListNewDiscountBand : discountBandListNew) {
                if (!discountBandListOld.contains(discountBandListNewDiscountBand)) {
                    CustomerAccount oldCustomerAccountOfDiscountBandListNewDiscountBand = discountBandListNewDiscountBand.getCustomerAccount();
                    discountBandListNewDiscountBand.setCustomerAccount(customerAccount);
                    discountBandListNewDiscountBand = em.merge(discountBandListNewDiscountBand);
                    if (oldCustomerAccountOfDiscountBandListNewDiscountBand != null && !oldCustomerAccountOfDiscountBandListNewDiscountBand.equals(customerAccount)) {
                        oldCustomerAccountOfDiscountBandListNewDiscountBand.getDiscountBandList().remove(discountBandListNewDiscountBand);
                        oldCustomerAccountOfDiscountBandListNewDiscountBand = em.merge(oldCustomerAccountOfDiscountBandListNewDiscountBand);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = customerAccount.getAccountNumber();
                if (findCustomerAccount(id) == null) {
                    throw new NonexistentEntityException("The customerAccount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount;
            try {
                customerAccount = em.getReference(CustomerAccount.class, id);
                customerAccount.getAccountNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customerAccount with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TaskDiscount> taskDiscountListOrphanCheck = customerAccount.getTaskDiscountList();
            for (TaskDiscount taskDiscountListOrphanCheckTaskDiscount : taskDiscountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the TaskDiscount " + taskDiscountListOrphanCheckTaskDiscount + " in its taskDiscountList field has a non-nullable customerAccount field.");
            }
            List<Address> addressListOrphanCheck = customerAccount.getAddressList();
            for (Address addressListOrphanCheckAddress : addressListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Address " + addressListOrphanCheckAddress + " in its addressList field has a non-nullable customerAccount field.");
            }
            List<Contact> contactListOrphanCheck = customerAccount.getContactList();
            for (Contact contactListOrphanCheckContact : contactListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Contact " + contactListOrphanCheckContact + " in its contactList field has a non-nullable customerAccount field.");
            }
            List<DiscountBand> discountBandListOrphanCheck = customerAccount.getDiscountBandList();
            for (DiscountBand discountBandListOrphanCheckDiscountBand : discountBandListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the DiscountBand " + discountBandListOrphanCheckDiscountBand + " in its discountBandList field has a non-nullable customerAccount field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(customerAccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CustomerAccount> findCustomerAccountEntities() {
        return findCustomerAccountEntities(true, -1, -1);
    }

    public List<CustomerAccount> findCustomerAccountEntities(int maxResults, int firstResult) {
        return findCustomerAccountEntities(false, maxResults, firstResult);
    }

    private List<CustomerAccount> findCustomerAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CustomerAccount.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CustomerAccount findCustomerAccount(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CustomerAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CustomerAccount> rt = cq.from(CustomerAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
