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
package bapers.data;

import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.Job;
import bapers.domain.JobTask;
import bapers.domain.JobTaskPK;
import bapers.domain.Task;
import bapers.domain.Staff;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class JobTaskJpaController implements Serializable {

    public JobTaskJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JobTask jobTask) throws PreexistingEntityException, Exception {
        if (jobTask.getJobTaskPK() == null) {
            jobTask.setJobTaskPK(new JobTaskPK());
        }
        jobTask.getJobTaskPK().setFkJobId(jobTask.getJob().getJobId());
        jobTask.getJobTaskPK().setFkTaskId(jobTask.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job job = jobTask.getJob();
            if (job != null) {
                job = em.getReference(job.getClass(), job.getJobId());
                jobTask.setJob(job);
            }
            Task task = jobTask.getTask();
            if (task != null) {
                task = em.getReference(task.getClass(), task.getTaskId());
                jobTask.setTask(task);
            }
            Staff fkStaffId = jobTask.getFkStaffId();
            if (fkStaffId != null) {
                fkStaffId = em.getReference(fkStaffId.getClass(), fkStaffId.getStaffId());
                jobTask.setFkStaffId(fkStaffId);
            }
            em.persist(jobTask);
            if (job != null) {
                job.getJobTaskList().add(jobTask);
                job = em.merge(job);
            }
            if (task != null) {
                task.getJobTaskList().add(jobTask);
                task = em.merge(task);
            }
            if (fkStaffId != null) {
                fkStaffId.getJobTaskList().add(jobTask);
                fkStaffId = em.merge(fkStaffId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJobTask(jobTask.getJobTaskPK()) != null) {
                throw new PreexistingEntityException("JobTask " + jobTask + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JobTask jobTask) throws NonexistentEntityException, Exception {
        jobTask.getJobTaskPK().setFkJobId(jobTask.getJob().getJobId());
        jobTask.getJobTaskPK().setFkTaskId(jobTask.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobTask persistentJobTask = em.find(JobTask.class, jobTask.getJobTaskPK());
            Job jobOld = persistentJobTask.getJob();
            Job jobNew = jobTask.getJob();
            Task taskOld = persistentJobTask.getTask();
            Task taskNew = jobTask.getTask();
            Staff fkStaffIdOld = persistentJobTask.getFkStaffId();
            Staff fkStaffIdNew = jobTask.getFkStaffId();
            if (jobNew != null) {
                jobNew = em.getReference(jobNew.getClass(), jobNew.getJobId());
                jobTask.setJob(jobNew);
            }
            if (taskNew != null) {
                taskNew = em.getReference(taskNew.getClass(), taskNew.getTaskId());
                jobTask.setTask(taskNew);
            }
            if (fkStaffIdNew != null) {
                fkStaffIdNew = em.getReference(fkStaffIdNew.getClass(), fkStaffIdNew.getStaffId());
                jobTask.setFkStaffId(fkStaffIdNew);
            }
            jobTask = em.merge(jobTask);
            if (jobOld != null && !jobOld.equals(jobNew)) {
                jobOld.getJobTaskList().remove(jobTask);
                jobOld = em.merge(jobOld);
            }
            if (jobNew != null && !jobNew.equals(jobOld)) {
                jobNew.getJobTaskList().add(jobTask);
                jobNew = em.merge(jobNew);
            }
            if (taskOld != null && !taskOld.equals(taskNew)) {
                taskOld.getJobTaskList().remove(jobTask);
                taskOld = em.merge(taskOld);
            }
            if (taskNew != null && !taskNew.equals(taskOld)) {
                taskNew.getJobTaskList().add(jobTask);
                taskNew = em.merge(taskNew);
            }
            if (fkStaffIdOld != null && !fkStaffIdOld.equals(fkStaffIdNew)) {
                fkStaffIdOld.getJobTaskList().remove(jobTask);
                fkStaffIdOld = em.merge(fkStaffIdOld);
            }
            if (fkStaffIdNew != null && !fkStaffIdNew.equals(fkStaffIdOld)) {
                fkStaffIdNew.getJobTaskList().add(jobTask);
                fkStaffIdNew = em.merge(fkStaffIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JobTaskPK id = jobTask.getJobTaskPK();
                if (findJobTask(id) == null) {
                    throw new NonexistentEntityException("The jobTask with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JobTaskPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobTask jobTask;
            try {
                jobTask = em.getReference(JobTask.class, id);
                jobTask.getJobTaskPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jobTask with id " + id + " no longer exists.", enfe);
            }
            Job job = jobTask.getJob();
            if (job != null) {
                job.getJobTaskList().remove(jobTask);
                job = em.merge(job);
            }
            Task task = jobTask.getTask();
            if (task != null) {
                task.getJobTaskList().remove(jobTask);
                task = em.merge(task);
            }
            Staff fkStaffId = jobTask.getFkStaffId();
            if (fkStaffId != null) {
                fkStaffId.getJobTaskList().remove(jobTask);
                fkStaffId = em.merge(fkStaffId);
            }
            em.remove(jobTask);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JobTask> findJobTaskEntities() {
        return findJobTaskEntities(true, -1, -1);
    }

    public List<JobTask> findJobTaskEntities(int maxResults, int firstResult) {
        return findJobTaskEntities(false, maxResults, firstResult);
    }

    private List<JobTask> findJobTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JobTask.class));
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

    public JobTask findJobTask(JobTaskPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JobTask.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JobTask> rt = cq.from(JobTask.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
