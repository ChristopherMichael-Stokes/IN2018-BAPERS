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
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.Discount;
import bapers.data.domain.Address;
import bapers.data.domain.CustomerAccount;
import java.util.ArrayList;
import java.util.List;
import bapers.data.domain.Job;
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

    public void create(CustomerAccount customerAccount) throws PreexistingEntityException, Exception {
        if (customerAccount.getAddressList() == null) {
            customerAccount.setAddressList(new ArrayList<Address>());
        }
        if (customerAccount.getJobList() == null) {
            customerAccount.setJobList(new ArrayList<Job>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Discount discount = customerAccount.getDiscount();
            if (discount != null) {
                discount = em.getReference(discount.getClass(), discount.getFkAccountNumber());
                customerAccount.setDiscount(discount);
            }
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : customerAccount.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getAddressPK());
                attachedAddressList.add(addressListAddressToAttach);
            }
            customerAccount.setAddressList(attachedAddressList);
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : customerAccount.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobId());
                attachedJobList.add(jobListJobToAttach);
            }
            customerAccount.setJobList(attachedJobList);
            em.persist(customerAccount);
            if (discount != null) {
                CustomerAccount oldCustomerAccountOfDiscount = discount.getCustomerAccount();
                if (oldCustomerAccountOfDiscount != null) {
                    oldCustomerAccountOfDiscount.setDiscount(null);
                    oldCustomerAccountOfDiscount = em.merge(oldCustomerAccountOfDiscount);
                }
                discount.setCustomerAccount(customerAccount);
                discount = em.merge(discount);
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
            for (Job jobListJob : customerAccount.getJobList()) {
                CustomerAccount oldFkAccountNumberOfJobListJob = jobListJob.getFkAccountNumber();
                jobListJob.setFkAccountNumber(customerAccount);
                jobListJob = em.merge(jobListJob);
                if (oldFkAccountNumberOfJobListJob != null) {
                    oldFkAccountNumberOfJobListJob.getJobList().remove(jobListJob);
                    oldFkAccountNumberOfJobListJob = em.merge(oldFkAccountNumberOfJobListJob);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomerAccount(customerAccount.getAccountNumber()) != null) {
                throw new PreexistingEntityException("CustomerAccount " + customerAccount + " already exists.", ex);
            }
            throw ex;
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
            Discount discountOld = persistentCustomerAccount.getDiscount();
            Discount discountNew = customerAccount.getDiscount();
            List<Address> addressListOld = persistentCustomerAccount.getAddressList();
            List<Address> addressListNew = customerAccount.getAddressList();
            List<Job> jobListOld = persistentCustomerAccount.getJobList();
            List<Job> jobListNew = customerAccount.getJobList();
            List<String> illegalOrphanMessages = null;
            if (discountOld != null && !discountOld.equals(discountNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Discount " + discountOld + " since its customerAccount field is not nullable.");
            }
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressListOldAddress + " since its customerAccount field is not nullable.");
                }
            }
            for (Job jobListOldJob : jobListOld) {
                if (!jobListNew.contains(jobListOldJob)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Job " + jobListOldJob + " since its fkAccountNumber field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (discountNew != null) {
                discountNew = em.getReference(discountNew.getClass(), discountNew.getFkAccountNumber());
                customerAccount.setDiscount(discountNew);
            }
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getAddressPK());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            customerAccount.setAddressList(addressListNew);
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobId());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            customerAccount.setJobList(jobListNew);
            customerAccount = em.merge(customerAccount);
            if (discountNew != null && !discountNew.equals(discountOld)) {
                CustomerAccount oldCustomerAccountOfDiscount = discountNew.getCustomerAccount();
                if (oldCustomerAccountOfDiscount != null) {
                    oldCustomerAccountOfDiscount.setDiscount(null);
                    oldCustomerAccountOfDiscount = em.merge(oldCustomerAccountOfDiscount);
                }
                discountNew.setCustomerAccount(customerAccount);
                discountNew = em.merge(discountNew);
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
            for (Job jobListNewJob : jobListNew) {
                if (!jobListOld.contains(jobListNewJob)) {
                    CustomerAccount oldFkAccountNumberOfJobListNewJob = jobListNewJob.getFkAccountNumber();
                    jobListNewJob.setFkAccountNumber(customerAccount);
                    jobListNewJob = em.merge(jobListNewJob);
                    if (oldFkAccountNumberOfJobListNewJob != null && !oldFkAccountNumberOfJobListNewJob.equals(customerAccount)) {
                        oldFkAccountNumberOfJobListNewJob.getJobList().remove(jobListNewJob);
                        oldFkAccountNumberOfJobListNewJob = em.merge(oldFkAccountNumberOfJobListNewJob);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = customerAccount.getAccountNumber();
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            Discount discountOrphanCheck = customerAccount.getDiscount();
            if (discountOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Discount " + discountOrphanCheck + " in its discount field has a non-nullable customerAccount field.");
            }
            List<Address> addressListOrphanCheck = customerAccount.getAddressList();
            for (Address addressListOrphanCheckAddress : addressListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Address " + addressListOrphanCheckAddress + " in its addressList field has a non-nullable customerAccount field.");
            }
            List<Job> jobListOrphanCheck = customerAccount.getJobList();
            for (Job jobListOrphanCheckJob : jobListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Job " + jobListOrphanCheckJob + " in its jobList field has a non-nullable fkAccountNumber field.");
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

    public CustomerAccount findCustomerAccount(String id) {
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
