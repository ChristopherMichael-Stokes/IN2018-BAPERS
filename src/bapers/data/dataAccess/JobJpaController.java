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
import bapers.data.domain.CustomerAccount;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import bapers.data.domain.JobTask;
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

    public void create(Job job) throws PreexistingEntityException, Exception {
        if (job.getJobTaskList() == null) {
            job.setJobTaskList(new ArrayList<JobTask>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount fkAccountNumber = job.getFkAccountNumber();
            if (fkAccountNumber != null) {
                fkAccountNumber = em.getReference(fkAccountNumber.getClass(), fkAccountNumber.getAccountNumber());
                job.setFkAccountNumber(fkAccountNumber);
            }
            PaymentInfo fkTransactionId = job.getFkTransactionId();
            if (fkTransactionId != null) {
                fkTransactionId = em.getReference(fkTransactionId.getClass(), fkTransactionId.getTransactionId());
                job.setFkTransactionId(fkTransactionId);
            }
            List<JobTask> attachedJobTaskList = new ArrayList<JobTask>();
            for (JobTask jobTaskListJobTaskToAttach : job.getJobTaskList()) {
                jobTaskListJobTaskToAttach = em.getReference(jobTaskListJobTaskToAttach.getClass(), jobTaskListJobTaskToAttach.getJobTaskPK());
                attachedJobTaskList.add(jobTaskListJobTaskToAttach);
            }
            job.setJobTaskList(attachedJobTaskList);
            em.persist(job);
            if (fkAccountNumber != null) {
                fkAccountNumber.getJobList().add(job);
                fkAccountNumber = em.merge(fkAccountNumber);
            }
            if (fkTransactionId != null) {
                fkTransactionId.getJobList().add(job);
                fkTransactionId = em.merge(fkTransactionId);
            }
            for (JobTask jobTaskListJobTask : job.getJobTaskList()) {
                Job oldJobOfJobTaskListJobTask = jobTaskListJobTask.getJob();
                jobTaskListJobTask.setJob(job);
                jobTaskListJobTask = em.merge(jobTaskListJobTask);
                if (oldJobOfJobTaskListJobTask != null) {
                    oldJobOfJobTaskListJobTask.getJobTaskList().remove(jobTaskListJobTask);
                    oldJobOfJobTaskListJobTask = em.merge(oldJobOfJobTaskListJobTask);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJob(job.getJobId()) != null) {
                throw new PreexistingEntityException("Job " + job + " already exists.", ex);
            }
            throw ex;
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
            CustomerAccount fkAccountNumberOld = persistentJob.getFkAccountNumber();
            CustomerAccount fkAccountNumberNew = job.getFkAccountNumber();
            PaymentInfo fkTransactionIdOld = persistentJob.getFkTransactionId();
            PaymentInfo fkTransactionIdNew = job.getFkTransactionId();
            List<JobTask> jobTaskListOld = persistentJob.getJobTaskList();
            List<JobTask> jobTaskListNew = job.getJobTaskList();
            List<String> illegalOrphanMessages = null;
            for (JobTask jobTaskListOldJobTask : jobTaskListOld) {
                if (!jobTaskListNew.contains(jobTaskListOldJobTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JobTask " + jobTaskListOldJobTask + " since its job field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkAccountNumberNew != null) {
                fkAccountNumberNew = em.getReference(fkAccountNumberNew.getClass(), fkAccountNumberNew.getAccountNumber());
                job.setFkAccountNumber(fkAccountNumberNew);
            }
            if (fkTransactionIdNew != null) {
                fkTransactionIdNew = em.getReference(fkTransactionIdNew.getClass(), fkTransactionIdNew.getTransactionId());
                job.setFkTransactionId(fkTransactionIdNew);
            }
            List<JobTask> attachedJobTaskListNew = new ArrayList<JobTask>();
            for (JobTask jobTaskListNewJobTaskToAttach : jobTaskListNew) {
                jobTaskListNewJobTaskToAttach = em.getReference(jobTaskListNewJobTaskToAttach.getClass(), jobTaskListNewJobTaskToAttach.getJobTaskPK());
                attachedJobTaskListNew.add(jobTaskListNewJobTaskToAttach);
            }
            jobTaskListNew = attachedJobTaskListNew;
            job.setJobTaskList(jobTaskListNew);
            job = em.merge(job);
            if (fkAccountNumberOld != null && !fkAccountNumberOld.equals(fkAccountNumberNew)) {
                fkAccountNumberOld.getJobList().remove(job);
                fkAccountNumberOld = em.merge(fkAccountNumberOld);
            }
            if (fkAccountNumberNew != null && !fkAccountNumberNew.equals(fkAccountNumberOld)) {
                fkAccountNumberNew.getJobList().add(job);
                fkAccountNumberNew = em.merge(fkAccountNumberNew);
            }
            if (fkTransactionIdOld != null && !fkTransactionIdOld.equals(fkTransactionIdNew)) {
                fkTransactionIdOld.getJobList().remove(job);
                fkTransactionIdOld = em.merge(fkTransactionIdOld);
            }
            if (fkTransactionIdNew != null && !fkTransactionIdNew.equals(fkTransactionIdOld)) {
                fkTransactionIdNew.getJobList().add(job);
                fkTransactionIdNew = em.merge(fkTransactionIdNew);
            }
            for (JobTask jobTaskListNewJobTask : jobTaskListNew) {
                if (!jobTaskListOld.contains(jobTaskListNewJobTask)) {
                    Job oldJobOfJobTaskListNewJobTask = jobTaskListNewJobTask.getJob();
                    jobTaskListNewJobTask.setJob(job);
                    jobTaskListNewJobTask = em.merge(jobTaskListNewJobTask);
                    if (oldJobOfJobTaskListNewJobTask != null && !oldJobOfJobTaskListNewJobTask.equals(job)) {
                        oldJobOfJobTaskListNewJobTask.getJobTaskList().remove(jobTaskListNewJobTask);
                        oldJobOfJobTaskListNewJobTask = em.merge(oldJobOfJobTaskListNewJobTask);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = job.getJobId();
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<JobTask> jobTaskListOrphanCheck = job.getJobTaskList();
            for (JobTask jobTaskListOrphanCheckJobTask : jobTaskListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Job (" + job + ") cannot be destroyed since the JobTask " + jobTaskListOrphanCheckJobTask + " in its jobTaskList field has a non-nullable job field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CustomerAccount fkAccountNumber = job.getFkAccountNumber();
            if (fkAccountNumber != null) {
                fkAccountNumber.getJobList().remove(job);
                fkAccountNumber = em.merge(fkAccountNumber);
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

    public Job findJob(String id) {
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
