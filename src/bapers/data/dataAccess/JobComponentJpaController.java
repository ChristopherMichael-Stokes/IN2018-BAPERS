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

import bapers.data.dataAccess.exceptions.NonexistentEntityException;
import bapers.data.dataAccess.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.Job;
import bapers.data.domain.JobComponent;
import bapers.data.domain.JobComponentPK;
import bapers.data.domain.User;
import bapers.data.domain.Task;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class JobComponentJpaController implements Serializable {

    public JobComponentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JobComponent jobComponent) throws PreexistingEntityException, Exception {
        if (jobComponent.getJobComponentPK() == null) {
            jobComponent.setJobComponentPK(new JobComponentPK());
        }
        if (jobComponent.getTaskList() == null) {
            jobComponent.setTaskList(new ArrayList<Task>());
        }
        jobComponent.getJobComponentPK().setFkJobId(jobComponent.getJob().getJobId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job job = jobComponent.getJob();
            if (job != null) {
                job = em.getReference(job.getClass(), job.getJobId());
                jobComponent.setJob(job);
            }
            User fkUsername = jobComponent.getFkUsername();
            if (fkUsername != null) {
                fkUsername = em.getReference(fkUsername.getClass(), fkUsername.getUsername());
                jobComponent.setFkUsername(fkUsername);
            }
            List<Task> attachedTaskList = new ArrayList<Task>();
            for (Task taskListTaskToAttach : jobComponent.getTaskList()) {
                taskListTaskToAttach = em.getReference(taskListTaskToAttach.getClass(), taskListTaskToAttach.getTaskId());
                attachedTaskList.add(taskListTaskToAttach);
            }
            jobComponent.setTaskList(attachedTaskList);
            em.persist(jobComponent);
            if (job != null) {
                job.getJobComponentList().add(jobComponent);
                job = em.merge(job);
            }
            if (fkUsername != null) {
                fkUsername.getJobComponentList().add(jobComponent);
                fkUsername = em.merge(fkUsername);
            }
            for (Task taskListTask : jobComponent.getTaskList()) {
                taskListTask.getJobComponentList().add(jobComponent);
                taskListTask = em.merge(taskListTask);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJobComponent(jobComponent.getJobComponentPK()) != null) {
                throw new PreexistingEntityException("JobComponent " + jobComponent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JobComponent jobComponent) throws NonexistentEntityException, Exception {
        jobComponent.getJobComponentPK().setFkJobId(jobComponent.getJob().getJobId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobComponent persistentJobComponent = em.find(JobComponent.class, jobComponent.getJobComponentPK());
            Job jobOld = persistentJobComponent.getJob();
            Job jobNew = jobComponent.getJob();
            User fkUsernameOld = persistentJobComponent.getFkUsername();
            User fkUsernameNew = jobComponent.getFkUsername();
            List<Task> taskListOld = persistentJobComponent.getTaskList();
            List<Task> taskListNew = jobComponent.getTaskList();
            if (jobNew != null) {
                jobNew = em.getReference(jobNew.getClass(), jobNew.getJobId());
                jobComponent.setJob(jobNew);
            }
            if (fkUsernameNew != null) {
                fkUsernameNew = em.getReference(fkUsernameNew.getClass(), fkUsernameNew.getUsername());
                jobComponent.setFkUsername(fkUsernameNew);
            }
            List<Task> attachedTaskListNew = new ArrayList<Task>();
            for (Task taskListNewTaskToAttach : taskListNew) {
                taskListNewTaskToAttach = em.getReference(taskListNewTaskToAttach.getClass(), taskListNewTaskToAttach.getTaskId());
                attachedTaskListNew.add(taskListNewTaskToAttach);
            }
            taskListNew = attachedTaskListNew;
            jobComponent.setTaskList(taskListNew);
            jobComponent = em.merge(jobComponent);
            if (jobOld != null && !jobOld.equals(jobNew)) {
                jobOld.getJobComponentList().remove(jobComponent);
                jobOld = em.merge(jobOld);
            }
            if (jobNew != null && !jobNew.equals(jobOld)) {
                jobNew.getJobComponentList().add(jobComponent);
                jobNew = em.merge(jobNew);
            }
            if (fkUsernameOld != null && !fkUsernameOld.equals(fkUsernameNew)) {
                fkUsernameOld.getJobComponentList().remove(jobComponent);
                fkUsernameOld = em.merge(fkUsernameOld);
            }
            if (fkUsernameNew != null && !fkUsernameNew.equals(fkUsernameOld)) {
                fkUsernameNew.getJobComponentList().add(jobComponent);
                fkUsernameNew = em.merge(fkUsernameNew);
            }
            for (Task taskListOldTask : taskListOld) {
                if (!taskListNew.contains(taskListOldTask)) {
                    taskListOldTask.getJobComponentList().remove(jobComponent);
                    taskListOldTask = em.merge(taskListOldTask);
                }
            }
            for (Task taskListNewTask : taskListNew) {
                if (!taskListOld.contains(taskListNewTask)) {
                    taskListNewTask.getJobComponentList().add(jobComponent);
                    taskListNewTask = em.merge(taskListNewTask);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JobComponentPK id = jobComponent.getJobComponentPK();
                if (findJobComponent(id) == null) {
                    throw new NonexistentEntityException("The jobComponent with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JobComponentPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobComponent jobComponent;
            try {
                jobComponent = em.getReference(JobComponent.class, id);
                jobComponent.getJobComponentPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jobComponent with id " + id + " no longer exists.", enfe);
            }
            Job job = jobComponent.getJob();
            if (job != null) {
                job.getJobComponentList().remove(jobComponent);
                job = em.merge(job);
            }
            User fkUsername = jobComponent.getFkUsername();
            if (fkUsername != null) {
                fkUsername.getJobComponentList().remove(jobComponent);
                fkUsername = em.merge(fkUsername);
            }
            List<Task> taskList = jobComponent.getTaskList();
            for (Task taskListTask : taskList) {
                taskListTask.getJobComponentList().remove(jobComponent);
                taskListTask = em.merge(taskListTask);
            }
            em.remove(jobComponent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JobComponent> findJobComponentEntities() {
        return findJobComponentEntities(true, -1, -1);
    }

    public List<JobComponent> findJobComponentEntities(int maxResults, int firstResult) {
        return findJobComponentEntities(false, maxResults, firstResult);
    }

    private List<JobComponent> findJobComponentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JobComponent.class));
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

    public JobComponent findJobComponent(JobComponentPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JobComponent.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobComponentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JobComponent> rt = cq.from(JobComponent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
