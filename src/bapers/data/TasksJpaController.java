/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import bapers.domain.Task;
import bapers.domain.Tasks;
import bapers.domain.TasksPK;
import bapers.domain.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class TasksJpaController implements Serializable {

    public TasksJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tasks tasks) throws PreexistingEntityException, Exception {
        if (tasks.getTasksPK() == null) {
            tasks.setTasksPK(new TasksPK());
        }
        tasks.getTasksPK().setJobfkcustomerid(tasks.getJob().getJobPK().getFkCustomerId());
        tasks.getTasksPK().setTasktaskid(tasks.getTask().getTaskId());
        tasks.getTasksPK().setJobfkpaymentid(tasks.getJob().getJobPK().getFkPaymentId());
        tasks.getTasksPK().setJobjobid(tasks.getJob().getJobPK().getJobId());
        tasks.getTasksPK().setJobfkemail(tasks.getJob().getJobPK().getFkEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job job = tasks.getJob();
            if (job != null) {
                job = em.getReference(job.getClass(), job.getJobPK());
                tasks.setJob(job);
            }
            Task task = tasks.getTask();
            if (task != null) {
                task = em.getReference(task.getClass(), task.getTaskId());
                tasks.setTask(task);
            }
            User userusername = tasks.getUserusername();
            if (userusername != null) {
                userusername = em.getReference(userusername.getClass(), userusername.getUsername());
                tasks.setUserusername(userusername);
            }
            em.persist(tasks);
            if (job != null) {
                job.getTasksList().add(tasks);
                job = em.merge(job);
            }
            if (task != null) {
                task.getTasksList().add(tasks);
                task = em.merge(task);
            }
            if (userusername != null) {
                userusername.getTasksList().add(tasks);
                userusername = em.merge(userusername);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTasks(tasks.getTasksPK()) != null) {
                throw new PreexistingEntityException("Tasks " + tasks + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tasks tasks) throws NonexistentEntityException, Exception {
        tasks.getTasksPK().setJobfkcustomerid(tasks.getJob().getJobPK().getFkCustomerId());
        tasks.getTasksPK().setTasktaskid(tasks.getTask().getTaskId());
        tasks.getTasksPK().setJobfkpaymentid(tasks.getJob().getJobPK().getFkPaymentId());
        tasks.getTasksPK().setJobjobid(tasks.getJob().getJobPK().getJobId());
        tasks.getTasksPK().setJobfkemail(tasks.getJob().getJobPK().getFkEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tasks persistentTasks = em.find(Tasks.class, tasks.getTasksPK());
            Job jobOld = persistentTasks.getJob();
            Job jobNew = tasks.getJob();
            Task taskOld = persistentTasks.getTask();
            Task taskNew = tasks.getTask();
            User userusernameOld = persistentTasks.getUserusername();
            User userusernameNew = tasks.getUserusername();
            if (jobNew != null) {
                jobNew = em.getReference(jobNew.getClass(), jobNew.getJobPK());
                tasks.setJob(jobNew);
            }
            if (taskNew != null) {
                taskNew = em.getReference(taskNew.getClass(), taskNew.getTaskId());
                tasks.setTask(taskNew);
            }
            if (userusernameNew != null) {
                userusernameNew = em.getReference(userusernameNew.getClass(), userusernameNew.getUsername());
                tasks.setUserusername(userusernameNew);
            }
            tasks = em.merge(tasks);
            if (jobOld != null && !jobOld.equals(jobNew)) {
                jobOld.getTasksList().remove(tasks);
                jobOld = em.merge(jobOld);
            }
            if (jobNew != null && !jobNew.equals(jobOld)) {
                jobNew.getTasksList().add(tasks);
                jobNew = em.merge(jobNew);
            }
            if (taskOld != null && !taskOld.equals(taskNew)) {
                taskOld.getTasksList().remove(tasks);
                taskOld = em.merge(taskOld);
            }
            if (taskNew != null && !taskNew.equals(taskOld)) {
                taskNew.getTasksList().add(tasks);
                taskNew = em.merge(taskNew);
            }
            if (userusernameOld != null && !userusernameOld.equals(userusernameNew)) {
                userusernameOld.getTasksList().remove(tasks);
                userusernameOld = em.merge(userusernameOld);
            }
            if (userusernameNew != null && !userusernameNew.equals(userusernameOld)) {
                userusernameNew.getTasksList().add(tasks);
                userusernameNew = em.merge(userusernameNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TasksPK id = tasks.getTasksPK();
                if (findTasks(id) == null) {
                    throw new NonexistentEntityException("The tasks with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TasksPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tasks tasks;
            try {
                tasks = em.getReference(Tasks.class, id);
                tasks.getTasksPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tasks with id " + id + " no longer exists.", enfe);
            }
            Job job = tasks.getJob();
            if (job != null) {
                job.getTasksList().remove(tasks);
                job = em.merge(job);
            }
            Task task = tasks.getTask();
            if (task != null) {
                task.getTasksList().remove(tasks);
                task = em.merge(task);
            }
            User userusername = tasks.getUserusername();
            if (userusername != null) {
                userusername.getTasksList().remove(tasks);
                userusername = em.merge(userusername);
            }
            em.remove(tasks);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tasks> findTasksEntities() {
        return findTasksEntities(true, -1, -1);
    }

    public List<Tasks> findTasksEntities(int maxResults, int firstResult) {
        return findTasksEntities(false, maxResults, firstResult);
    }

    private List<Tasks> findTasksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tasks.class));
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

    public Tasks findTasks(TasksPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tasks.class, id);
        } finally {
            em.close();
        }
    }

    public int getTasksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tasks> rt = cq.from(Tasks.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
