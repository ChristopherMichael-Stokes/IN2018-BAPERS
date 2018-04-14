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
import bapers.data.domain.ComponentTask;
import bapers.data.domain.ComponentTaskPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.User;
import bapers.data.domain.JobComponent;
import bapers.data.domain.Task;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class ComponentTaskJpaController implements Serializable {

    public ComponentTaskJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComponentTask componentTask) throws PreexistingEntityException, Exception {
        if (componentTask.getComponentTaskPK() == null) {
            componentTask.setComponentTaskPK(new ComponentTaskPK());
        }
        componentTask.getComponentTaskPK().setFkJobId(componentTask.getJobComponent().getJobComponentPK().getFkJobId());
        componentTask.getComponentTaskPK().setFkComponentId(componentTask.getJobComponent().getJobComponentPK().getComponentId());
        componentTask.getComponentTaskPK().setFkTaskId(componentTask.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User fkUsername = componentTask.getFkUsername();
            if (fkUsername != null) {
                fkUsername = em.getReference(fkUsername.getClass(), fkUsername.getUsername());
                componentTask.setFkUsername(fkUsername);
            }
            JobComponent jobComponent = componentTask.getJobComponent();
            if (jobComponent != null) {
                jobComponent = em.getReference(jobComponent.getClass(), jobComponent.getJobComponentPK());
                componentTask.setJobComponent(jobComponent);
            }
            Task task = componentTask.getTask();
            if (task != null) {
                task = em.getReference(task.getClass(), task.getTaskId());
                componentTask.setTask(task);
            }
            em.persist(componentTask);
            if (fkUsername != null) {
                fkUsername.getComponentTaskList().add(componentTask);
                fkUsername = em.merge(fkUsername);
            }
            if (jobComponent != null) {
                jobComponent.getComponentTaskList().add(componentTask);
                jobComponent = em.merge(jobComponent);
            }
            if (task != null) {
                task.getComponentTaskList().add(componentTask);
                task = em.merge(task);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComponentTask(componentTask.getComponentTaskPK()) != null) {
                throw new PreexistingEntityException("ComponentTask " + componentTask + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComponentTask componentTask) throws NonexistentEntityException, Exception {
        componentTask.getComponentTaskPK().setFkJobId(componentTask.getJobComponent().getJobComponentPK().getFkJobId());
        componentTask.getComponentTaskPK().setFkComponentId(componentTask.getJobComponent().getJobComponentPK().getComponentId());
        componentTask.getComponentTaskPK().setFkTaskId(componentTask.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComponentTask persistentComponentTask = em.find(ComponentTask.class, componentTask.getComponentTaskPK());
            User fkUsernameOld = persistentComponentTask.getFkUsername();
            User fkUsernameNew = componentTask.getFkUsername();
            JobComponent jobComponentOld = persistentComponentTask.getJobComponent();
            JobComponent jobComponentNew = componentTask.getJobComponent();
            Task taskOld = persistentComponentTask.getTask();
            Task taskNew = componentTask.getTask();
            if (fkUsernameNew != null) {
                fkUsernameNew = em.getReference(fkUsernameNew.getClass(), fkUsernameNew.getUsername());
                componentTask.setFkUsername(fkUsernameNew);
            }
            if (jobComponentNew != null) {
                jobComponentNew = em.getReference(jobComponentNew.getClass(), jobComponentNew.getJobComponentPK());
                componentTask.setJobComponent(jobComponentNew);
            }
            if (taskNew != null) {
                taskNew = em.getReference(taskNew.getClass(), taskNew.getTaskId());
                componentTask.setTask(taskNew);
            }
            componentTask = em.merge(componentTask);
            if (fkUsernameOld != null && !fkUsernameOld.equals(fkUsernameNew)) {
                fkUsernameOld.getComponentTaskList().remove(componentTask);
                fkUsernameOld = em.merge(fkUsernameOld);
            }
            if (fkUsernameNew != null && !fkUsernameNew.equals(fkUsernameOld)) {
                fkUsernameNew.getComponentTaskList().add(componentTask);
                fkUsernameNew = em.merge(fkUsernameNew);
            }
            if (jobComponentOld != null && !jobComponentOld.equals(jobComponentNew)) {
                jobComponentOld.getComponentTaskList().remove(componentTask);
                jobComponentOld = em.merge(jobComponentOld);
            }
            if (jobComponentNew != null && !jobComponentNew.equals(jobComponentOld)) {
                jobComponentNew.getComponentTaskList().add(componentTask);
                jobComponentNew = em.merge(jobComponentNew);
            }
            if (taskOld != null && !taskOld.equals(taskNew)) {
                taskOld.getComponentTaskList().remove(componentTask);
                taskOld = em.merge(taskOld);
            }
            if (taskNew != null && !taskNew.equals(taskOld)) {
                taskNew.getComponentTaskList().add(componentTask);
                taskNew = em.merge(taskNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ComponentTaskPK id = componentTask.getComponentTaskPK();
                if (findComponentTask(id) == null) {
                    throw new NonexistentEntityException("The componentTask with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ComponentTaskPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComponentTask componentTask;
            try {
                componentTask = em.getReference(ComponentTask.class, id);
                componentTask.getComponentTaskPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The componentTask with id " + id + " no longer exists.", enfe);
            }
            User fkUsername = componentTask.getFkUsername();
            if (fkUsername != null) {
                fkUsername.getComponentTaskList().remove(componentTask);
                fkUsername = em.merge(fkUsername);
            }
            JobComponent jobComponent = componentTask.getJobComponent();
            if (jobComponent != null) {
                jobComponent.getComponentTaskList().remove(componentTask);
                jobComponent = em.merge(jobComponent);
            }
            Task task = componentTask.getTask();
            if (task != null) {
                task.getComponentTaskList().remove(componentTask);
                task = em.merge(task);
            }
            em.remove(componentTask);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComponentTask> findComponentTaskEntities() {
        return findComponentTaskEntities(true, -1, -1);
    }

    public List<ComponentTask> findComponentTaskEntities(int maxResults, int firstResult) {
        return findComponentTaskEntities(false, maxResults, firstResult);
    }

    private List<ComponentTask> findComponentTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComponentTask.class));
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

    public ComponentTask findComponentTask(ComponentTaskPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComponentTask.class, id);
        } finally {
            em.close();
        }
    }

    public int getComponentTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComponentTask> rt = cq.from(ComponentTask.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
