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
import bapers.data.domain.Contact;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import bapers.data.domain.JobComponent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class JobJpaController implements Serializable {

    public JobJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Job job) {
        if (job.getJobComponentList() == null) {
            job.setJobComponentList(new ArrayList<JobComponent>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contact contact = job.getContact();
            if (contact != null) {
                contact = em.getReference(contact.getClass(), contact.getContactPK());
                job.setContact(contact);
            }
            PaymentInfo fkTransactionId = job.getFkTransactionId();
            if (fkTransactionId != null) {
                fkTransactionId = em.getReference(fkTransactionId.getClass(), fkTransactionId.getTransactionId());
                job.setFkTransactionId(fkTransactionId);
            }
            List<JobComponent> attachedJobComponentList = new ArrayList<JobComponent>();
            for (JobComponent jobComponentListJobComponentToAttach : job.getJobComponentList()) {
                jobComponentListJobComponentToAttach = em.getReference(jobComponentListJobComponentToAttach.getClass(), jobComponentListJobComponentToAttach.getJobComponentPK());
                attachedJobComponentList.add(jobComponentListJobComponentToAttach);
            }
            job.setJobComponentList(attachedJobComponentList);
            em.persist(job);
            if (contact != null) {
                contact.getJobList().add(job);
                contact = em.merge(contact);
            }
            if (fkTransactionId != null) {
                fkTransactionId.getJobList().add(job);
                fkTransactionId = em.merge(fkTransactionId);
            }
            for (JobComponent jobComponentListJobComponent : job.getJobComponentList()) {
                Job oldJobOfJobComponentListJobComponent = jobComponentListJobComponent.getJob();
                jobComponentListJobComponent.setJob(job);
                jobComponentListJobComponent = em.merge(jobComponentListJobComponent);
                if (oldJobOfJobComponentListJobComponent != null) {
                    oldJobOfJobComponentListJobComponent.getJobComponentList().remove(jobComponentListJobComponent);
                    oldJobOfJobComponentListJobComponent = em.merge(oldJobOfJobComponentListJobComponent);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Job job) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job persistentJob = em.find(Job.class, job.getJobId());
            Contact contactOld = persistentJob.getContact();
            Contact contactNew = job.getContact();
            PaymentInfo fkTransactionIdOld = persistentJob.getFkTransactionId();
            PaymentInfo fkTransactionIdNew = job.getFkTransactionId();
            List<JobComponent> jobComponentListOld = persistentJob.getJobComponentList();
            List<JobComponent> jobComponentListNew = job.getJobComponentList();
            List<String> illegalOrphanMessages = null;
            for (JobComponent jobComponentListOldJobComponent : jobComponentListOld) {
                if (!jobComponentListNew.contains(jobComponentListOldJobComponent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JobComponent " + jobComponentListOldJobComponent + " since its job field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contactNew != null) {
                contactNew = em.getReference(contactNew.getClass(), contactNew.getContactPK());
                job.setContact(contactNew);
            }
            if (fkTransactionIdNew != null) {
                fkTransactionIdNew = em.getReference(fkTransactionIdNew.getClass(), fkTransactionIdNew.getTransactionId());
                job.setFkTransactionId(fkTransactionIdNew);
            }
            List<JobComponent> attachedJobComponentListNew = new ArrayList<JobComponent>();
            for (JobComponent jobComponentListNewJobComponentToAttach : jobComponentListNew) {
                jobComponentListNewJobComponentToAttach = em.getReference(jobComponentListNewJobComponentToAttach.getClass(), jobComponentListNewJobComponentToAttach.getJobComponentPK());
                attachedJobComponentListNew.add(jobComponentListNewJobComponentToAttach);
            }
            jobComponentListNew = attachedJobComponentListNew;
            job.setJobComponentList(jobComponentListNew);
            job = em.merge(job);
            if (contactOld != null && !contactOld.equals(contactNew)) {
                contactOld.getJobList().remove(job);
                contactOld = em.merge(contactOld);
            }
            if (contactNew != null && !contactNew.equals(contactOld)) {
                contactNew.getJobList().add(job);
                contactNew = em.merge(contactNew);
            }
            if (fkTransactionIdOld != null && !fkTransactionIdOld.equals(fkTransactionIdNew)) {
                fkTransactionIdOld.getJobList().remove(job);
                fkTransactionIdOld = em.merge(fkTransactionIdOld);
            }
            if (fkTransactionIdNew != null && !fkTransactionIdNew.equals(fkTransactionIdOld)) {
                fkTransactionIdNew.getJobList().add(job);
                fkTransactionIdNew = em.merge(fkTransactionIdNew);
            }
            for (JobComponent jobComponentListNewJobComponent : jobComponentListNew) {
                if (!jobComponentListOld.contains(jobComponentListNewJobComponent)) {
                    Job oldJobOfJobComponentListNewJobComponent = jobComponentListNewJobComponent.getJob();
                    jobComponentListNewJobComponent.setJob(job);
                    jobComponentListNewJobComponent = em.merge(jobComponentListNewJobComponent);
                    if (oldJobOfJobComponentListNewJobComponent != null && !oldJobOfJobComponentListNewJobComponent.equals(job)) {
                        oldJobOfJobComponentListNewJobComponent.getJobComponentList().remove(jobComponentListNewJobComponent);
                        oldJobOfJobComponentListNewJobComponent = em.merge(oldJobOfJobComponentListNewJobComponent);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = job.getJobId();
                if (findJob(id) == null) {
                    throw new NonexistentEntityException("The job with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job job;
            try {
                job = em.getReference(Job.class, id);
                job.getJobId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The job with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<JobComponent> jobComponentListOrphanCheck = job.getJobComponentList();
            for (JobComponent jobComponentListOrphanCheckJobComponent : jobComponentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Job (" + job + ") cannot be destroyed since the JobComponent " + jobComponentListOrphanCheckJobComponent + " in its jobComponentList field has a non-nullable job field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Contact contact = job.getContact();
            if (contact != null) {
                contact.getJobList().remove(job);
                contact = em.merge(contact);
            }
            PaymentInfo fkTransactionId = job.getFkTransactionId();
            if (fkTransactionId != null) {
                fkTransactionId.getJobList().remove(job);
                fkTransactionId = em.merge(fkTransactionId);
            }
            em.remove(job);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Job> findJobEntities() {
        return findJobEntities(true, -1, -1);
    }

    public List<Job> findJobEntities(int maxResults, int firstResult) {
        return findJobEntities(false, maxResults, firstResult);
    }

    private List<Job> findJobEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Job.class));
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

    public Job findJob(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Job.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Job> rt = cq.from(Job.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
