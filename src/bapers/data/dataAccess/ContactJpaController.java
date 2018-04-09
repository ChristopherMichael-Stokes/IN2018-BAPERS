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
import bapers.data.domain.Contact;
import bapers.data.domain.ContactPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class ContactJpaController implements Serializable {

    public ContactJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contact contact) throws PreexistingEntityException, Exception {
        if (contact.getContactPK() == null) {
            contact.setContactPK(new ContactPK());
        }
        if (contact.getJobList() == null) {
            contact.setJobList(new ArrayList<Job>());
        }
        contact.getContactPK().setFkAccountNumber(contact.getCustomerAccount().getAccountNumber());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount = contact.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount = em.getReference(customerAccount.getClass(), customerAccount.getAccountNumber());
                contact.setCustomerAccount(customerAccount);
            }
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : contact.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobId());
                attachedJobList.add(jobListJobToAttach);
            }
            contact.setJobList(attachedJobList);
            em.persist(contact);
            if (customerAccount != null) {
                customerAccount.getContactList().add(contact);
                customerAccount = em.merge(customerAccount);
            }
            for (Job jobListJob : contact.getJobList()) {
                Contact oldContactOfJobListJob = jobListJob.getContact();
                jobListJob.setContact(contact);
                jobListJob = em.merge(jobListJob);
                if (oldContactOfJobListJob != null) {
                    oldContactOfJobListJob.getJobList().remove(jobListJob);
                    oldContactOfJobListJob = em.merge(oldContactOfJobListJob);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContact(contact.getContactPK()) != null) {
                throw new PreexistingEntityException("Contact " + contact + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contact contact) throws IllegalOrphanException, NonexistentEntityException, Exception {
        contact.getContactPK().setFkAccountNumber(contact.getCustomerAccount().getAccountNumber());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contact persistentContact = em.find(Contact.class, contact.getContactPK());
            CustomerAccount customerAccountOld = persistentContact.getCustomerAccount();
            CustomerAccount customerAccountNew = contact.getCustomerAccount();
            List<Job> jobListOld = persistentContact.getJobList();
            List<Job> jobListNew = contact.getJobList();
            List<String> illegalOrphanMessages = null;
            for (Job jobListOldJob : jobListOld) {
                if (!jobListNew.contains(jobListOldJob)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Job " + jobListOldJob + " since its contact field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerAccountNew != null) {
                customerAccountNew = em.getReference(customerAccountNew.getClass(), customerAccountNew.getAccountNumber());
                contact.setCustomerAccount(customerAccountNew);
            }
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobId());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            contact.setJobList(jobListNew);
            contact = em.merge(contact);
            if (customerAccountOld != null && !customerAccountOld.equals(customerAccountNew)) {
                customerAccountOld.getContactList().remove(contact);
                customerAccountOld = em.merge(customerAccountOld);
            }
            if (customerAccountNew != null && !customerAccountNew.equals(customerAccountOld)) {
                customerAccountNew.getContactList().add(contact);
                customerAccountNew = em.merge(customerAccountNew);
            }
            for (Job jobListNewJob : jobListNew) {
                if (!jobListOld.contains(jobListNewJob)) {
                    Contact oldContactOfJobListNewJob = jobListNewJob.getContact();
                    jobListNewJob.setContact(contact);
                    jobListNewJob = em.merge(jobListNewJob);
                    if (oldContactOfJobListNewJob != null && !oldContactOfJobListNewJob.equals(contact)) {
                        oldContactOfJobListNewJob.getJobList().remove(jobListNewJob);
                        oldContactOfJobListNewJob = em.merge(oldContactOfJobListNewJob);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ContactPK id = contact.getContactPK();
                if (findContact(id) == null) {
                    throw new NonexistentEntityException("The contact with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ContactPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contact contact;
            try {
                contact = em.getReference(Contact.class, id);
                contact.getContactPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contact with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Job> jobListOrphanCheck = contact.getJobList();
            for (Job jobListOrphanCheckJob : jobListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contact (" + contact + ") cannot be destroyed since the Job " + jobListOrphanCheckJob + " in its jobList field has a non-nullable contact field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CustomerAccount customerAccount = contact.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount.getContactList().remove(contact);
                customerAccount = em.merge(customerAccount);
            }
            em.remove(contact);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contact> findContactEntities() {
        return findContactEntities(true, -1, -1);
    }

    public List<Contact> findContactEntities(int maxResults, int firstResult) {
        return findContactEntities(false, maxResults, firstResult);
    }

    private List<Contact> findContactEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contact.class));
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

    public Contact findContact(ContactPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contact.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contact> rt = cq.from(Contact.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
