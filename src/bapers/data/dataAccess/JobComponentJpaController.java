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
import bapers.data.domain.Job;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.JobComponent;
import bapers.data.domain.JobComponentPK;
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
        if (jobComponent.getComponentTaskList() == null) {
            jobComponent.setComponentTaskList(new ArrayList<ComponentTask>());
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
            List<ComponentTask> attachedComponentTaskList = new ArrayList<ComponentTask>();
            for (ComponentTask componentTaskListComponentTaskToAttach : jobComponent.getComponentTaskList()) {
                componentTaskListComponentTaskToAttach = em.getReference(componentTaskListComponentTaskToAttach.getClass(), componentTaskListComponentTaskToAttach.getComponentTaskPK());
                attachedComponentTaskList.add(componentTaskListComponentTaskToAttach);
            }
            jobComponent.setComponentTaskList(attachedComponentTaskList);
            em.persist(jobComponent);
            if (job != null) {
                job.getJobComponentList().add(jobComponent);
                job = em.merge(job);
            }
            for (ComponentTask componentTaskListComponentTask : jobComponent.getComponentTaskList()) {
                JobComponent oldJobComponentOfComponentTaskListComponentTask = componentTaskListComponentTask.getJobComponent();
                componentTaskListComponentTask.setJobComponent(jobComponent);
                componentTaskListComponentTask = em.merge(componentTaskListComponentTask);
                if (oldJobComponentOfComponentTaskListComponentTask != null) {
                    oldJobComponentOfComponentTaskListComponentTask.getComponentTaskList().remove(componentTaskListComponentTask);
                    oldJobComponentOfComponentTaskListComponentTask = em.merge(oldJobComponentOfComponentTaskListComponentTask);
                }
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

    public void edit(JobComponent jobComponent) throws IllegalOrphanException, NonexistentEntityException, Exception {
        jobComponent.getJobComponentPK().setFkJobId(jobComponent.getJob().getJobId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobComponent persistentJobComponent = em.find(JobComponent.class, jobComponent.getJobComponentPK());
            Job jobOld = persistentJobComponent.getJob();
            Job jobNew = jobComponent.getJob();
            List<ComponentTask> componentTaskListOld = persistentJobComponent.getComponentTaskList();
            List<ComponentTask> componentTaskListNew = jobComponent.getComponentTaskList();
            List<String> illegalOrphanMessages = null;
            for (ComponentTask componentTaskListOldComponentTask : componentTaskListOld) {
                if (!componentTaskListNew.contains(componentTaskListOldComponentTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComponentTask " + componentTaskListOldComponentTask + " since its jobComponent field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (jobNew != null) {
                jobNew = em.getReference(jobNew.getClass(), jobNew.getJobId());
                jobComponent.setJob(jobNew);
            }
            List<ComponentTask> attachedComponentTaskListNew = new ArrayList<ComponentTask>();
            for (ComponentTask componentTaskListNewComponentTaskToAttach : componentTaskListNew) {
                componentTaskListNewComponentTaskToAttach = em.getReference(componentTaskListNewComponentTaskToAttach.getClass(), componentTaskListNewComponentTaskToAttach.getComponentTaskPK());
                attachedComponentTaskListNew.add(componentTaskListNewComponentTaskToAttach);
            }
            componentTaskListNew = attachedComponentTaskListNew;
            jobComponent.setComponentTaskList(componentTaskListNew);
            jobComponent = em.merge(jobComponent);
            if (jobOld != null && !jobOld.equals(jobNew)) {
                jobOld.getJobComponentList().remove(jobComponent);
                jobOld = em.merge(jobOld);
            }
            if (jobNew != null && !jobNew.equals(jobOld)) {
                jobNew.getJobComponentList().add(jobComponent);
                jobNew = em.merge(jobNew);
            }
            for (ComponentTask componentTaskListNewComponentTask : componentTaskListNew) {
                if (!componentTaskListOld.contains(componentTaskListNewComponentTask)) {
                    JobComponent oldJobComponentOfComponentTaskListNewComponentTask = componentTaskListNewComponentTask.getJobComponent();
                    componentTaskListNewComponentTask.setJobComponent(jobComponent);
                    componentTaskListNewComponentTask = em.merge(componentTaskListNewComponentTask);
                    if (oldJobComponentOfComponentTaskListNewComponentTask != null && !oldJobComponentOfComponentTaskListNewComponentTask.equals(jobComponent)) {
                        oldJobComponentOfComponentTaskListNewComponentTask.getComponentTaskList().remove(componentTaskListNewComponentTask);
                        oldJobComponentOfComponentTaskListNewComponentTask = em.merge(oldJobComponentOfComponentTaskListNewComponentTask);
                    }
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

    public void destroy(JobComponentPK id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<ComponentTask> componentTaskListOrphanCheck = jobComponent.getComponentTaskList();
            for (ComponentTask componentTaskListOrphanCheckComponentTask : componentTaskListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This JobComponent (" + jobComponent + ") cannot be destroyed since the ComponentTask " + componentTaskListOrphanCheckComponentTask + " in its componentTaskList field has a non-nullable jobComponent field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Job job = jobComponent.getJob();
            if (job != null) {
                job.getJobComponentList().remove(jobComponent);
                job = em.merge(job);
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
