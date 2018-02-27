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

import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.TaskDiscount;
import java.util.ArrayList;
import java.util.List;
import bapers.domain.JobTask;
import bapers.domain.Task;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class TaskJpaController implements Serializable {

    public TaskJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Task task) throws PreexistingEntityException, Exception {
        if (task.getTaskDiscountList() == null) {
            task.setTaskDiscountList(new ArrayList<TaskDiscount>());
        }
        if (task.getJobTaskList() == null) {
            task.setJobTaskList(new ArrayList<JobTask>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TaskDiscount> attachedTaskDiscountList = new ArrayList<TaskDiscount>();
            for (TaskDiscount taskDiscountListTaskDiscountToAttach : task.getTaskDiscountList()) {
                taskDiscountListTaskDiscountToAttach = em.getReference(taskDiscountListTaskDiscountToAttach.getClass(), taskDiscountListTaskDiscountToAttach.getTaskDiscountPK());
                attachedTaskDiscountList.add(taskDiscountListTaskDiscountToAttach);
            }
            task.setTaskDiscountList(attachedTaskDiscountList);
            List<JobTask> attachedJobTaskList = new ArrayList<JobTask>();
            for (JobTask jobTaskListJobTaskToAttach : task.getJobTaskList()) {
                jobTaskListJobTaskToAttach = em.getReference(jobTaskListJobTaskToAttach.getClass(), jobTaskListJobTaskToAttach.getJobTaskPK());
                attachedJobTaskList.add(jobTaskListJobTaskToAttach);
            }
            task.setJobTaskList(attachedJobTaskList);
            em.persist(task);
            for (TaskDiscount taskDiscountListTaskDiscount : task.getTaskDiscountList()) {
                Task oldTaskOfTaskDiscountListTaskDiscount = taskDiscountListTaskDiscount.getTask();
                taskDiscountListTaskDiscount.setTask(task);
                taskDiscountListTaskDiscount = em.merge(taskDiscountListTaskDiscount);
                if (oldTaskOfTaskDiscountListTaskDiscount != null) {
                    oldTaskOfTaskDiscountListTaskDiscount.getTaskDiscountList().remove(taskDiscountListTaskDiscount);
                    oldTaskOfTaskDiscountListTaskDiscount = em.merge(oldTaskOfTaskDiscountListTaskDiscount);
                }
            }
            for (JobTask jobTaskListJobTask : task.getJobTaskList()) {
                Task oldTaskOfJobTaskListJobTask = jobTaskListJobTask.getTask();
                jobTaskListJobTask.setTask(task);
                jobTaskListJobTask = em.merge(jobTaskListJobTask);
                if (oldTaskOfJobTaskListJobTask != null) {
                    oldTaskOfJobTaskListJobTask.getJobTaskList().remove(jobTaskListJobTask);
                    oldTaskOfJobTaskListJobTask = em.merge(oldTaskOfJobTaskListJobTask);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTask(task.getTaskId()) != null) {
                throw new PreexistingEntityException("Task " + task + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Task task) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task persistentTask = em.find(Task.class, task.getTaskId());
            List<TaskDiscount> taskDiscountListOld = persistentTask.getTaskDiscountList();
            List<TaskDiscount> taskDiscountListNew = task.getTaskDiscountList();
            List<JobTask> jobTaskListOld = persistentTask.getJobTaskList();
            List<JobTask> jobTaskListNew = task.getJobTaskList();
            List<String> illegalOrphanMessages = null;
            for (TaskDiscount taskDiscountListOldTaskDiscount : taskDiscountListOld) {
                if (!taskDiscountListNew.contains(taskDiscountListOldTaskDiscount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TaskDiscount " + taskDiscountListOldTaskDiscount + " since its task field is not nullable.");
                }
            }
            for (JobTask jobTaskListOldJobTask : jobTaskListOld) {
                if (!jobTaskListNew.contains(jobTaskListOldJobTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JobTask " + jobTaskListOldJobTask + " since its task field is not nullable.");
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
            task.setTaskDiscountList(taskDiscountListNew);
            List<JobTask> attachedJobTaskListNew = new ArrayList<JobTask>();
            for (JobTask jobTaskListNewJobTaskToAttach : jobTaskListNew) {
                jobTaskListNewJobTaskToAttach = em.getReference(jobTaskListNewJobTaskToAttach.getClass(), jobTaskListNewJobTaskToAttach.getJobTaskPK());
                attachedJobTaskListNew.add(jobTaskListNewJobTaskToAttach);
            }
            jobTaskListNew = attachedJobTaskListNew;
            task.setJobTaskList(jobTaskListNew);
            task = em.merge(task);
            for (TaskDiscount taskDiscountListNewTaskDiscount : taskDiscountListNew) {
                if (!taskDiscountListOld.contains(taskDiscountListNewTaskDiscount)) {
                    Task oldTaskOfTaskDiscountListNewTaskDiscount = taskDiscountListNewTaskDiscount.getTask();
                    taskDiscountListNewTaskDiscount.setTask(task);
                    taskDiscountListNewTaskDiscount = em.merge(taskDiscountListNewTaskDiscount);
                    if (oldTaskOfTaskDiscountListNewTaskDiscount != null && !oldTaskOfTaskDiscountListNewTaskDiscount.equals(task)) {
                        oldTaskOfTaskDiscountListNewTaskDiscount.getTaskDiscountList().remove(taskDiscountListNewTaskDiscount);
                        oldTaskOfTaskDiscountListNewTaskDiscount = em.merge(oldTaskOfTaskDiscountListNewTaskDiscount);
                    }
                }
            }
            for (JobTask jobTaskListNewJobTask : jobTaskListNew) {
                if (!jobTaskListOld.contains(jobTaskListNewJobTask)) {
                    Task oldTaskOfJobTaskListNewJobTask = jobTaskListNewJobTask.getTask();
                    jobTaskListNewJobTask.setTask(task);
                    jobTaskListNewJobTask = em.merge(jobTaskListNewJobTask);
                    if (oldTaskOfJobTaskListNewJobTask != null && !oldTaskOfJobTaskListNewJobTask.equals(task)) {
                        oldTaskOfJobTaskListNewJobTask.getJobTaskList().remove(jobTaskListNewJobTask);
                        oldTaskOfJobTaskListNewJobTask = em.merge(oldTaskOfJobTaskListNewJobTask);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = task.getTaskId();
                if (findTask(id) == null) {
                    throw new NonexistentEntityException("The task with id " + id + " no longer exists.");
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
            Task task;
            try {
                task = em.getReference(Task.class, id);
                task.getTaskId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The task with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TaskDiscount> taskDiscountListOrphanCheck = task.getTaskDiscountList();
            for (TaskDiscount taskDiscountListOrphanCheckTaskDiscount : taskDiscountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Task (" + task + ") cannot be destroyed since the TaskDiscount " + taskDiscountListOrphanCheckTaskDiscount + " in its taskDiscountList field has a non-nullable task field.");
            }
            List<JobTask> jobTaskListOrphanCheck = task.getJobTaskList();
            for (JobTask jobTaskListOrphanCheckJobTask : jobTaskListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Task (" + task + ") cannot be destroyed since the JobTask " + jobTaskListOrphanCheckJobTask + " in its jobTaskList field has a non-nullable task field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(task);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Task> findTaskEntities() {
        return findTaskEntities(true, -1, -1);
    }

    public List<Task> findTaskEntities(int maxResults, int firstResult) {
        return findTaskEntities(false, maxResults, firstResult);
    }

    private List<Task> findTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Task.class));
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

    public Task findTask(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Task.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Task> rt = cq.from(Task.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
