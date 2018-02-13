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
import bapers.domain.User;
import bapers.domain.Job;
import bapers.domain.Jobtasks;
import bapers.domain.JobtasksPK;
import bapers.domain.Task;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class JobtasksJpaController implements Serializable {

    public JobtasksJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jobtasks jobtasks) throws PreexistingEntityException, Exception {
        if (jobtasks.getJobtasksPK() == null) {
            jobtasks.setJobtasksPK(new JobtasksPK());
        }
        jobtasks.getJobtasksPK().setJobCustomerAccountcustomerid(jobtasks.getJob().getJobPK().getCustomerAccountcustomerid());
        jobtasks.getJobtasksPK().setTasktaskid(jobtasks.getTask().getTaskId());
        jobtasks.getJobtasksPK().setJobjobid(jobtasks.getJob().getJobPK().getJobId());
        jobtasks.getJobtasksPK().setJobfkemail(jobtasks.getJob().getJobPK().getFkEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User useruserid = jobtasks.getUseruserid();
            if (useruserid != null) {
                useruserid = em.getReference(useruserid.getClass(), useruserid.getUserId());
                jobtasks.setUseruserid(useruserid);
            }
            Job job = jobtasks.getJob();
            if (job != null) {
                job = em.getReference(job.getClass(), job.getJobPK());
                jobtasks.setJob(job);
            }
            Task task = jobtasks.getTask();
            if (task != null) {
                task = em.getReference(task.getClass(), task.getTaskId());
                jobtasks.setTask(task);
            }
            em.persist(jobtasks);
            if (useruserid != null) {
                useruserid.getJobtasksList().add(jobtasks);
                useruserid = em.merge(useruserid);
            }
            if (job != null) {
                job.getJobtasksList().add(jobtasks);
                job = em.merge(job);
            }
            if (task != null) {
                task.getJobtasksList().add(jobtasks);
                task = em.merge(task);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJobtasks(jobtasks.getJobtasksPK()) != null) {
                throw new PreexistingEntityException("Jobtasks " + jobtasks + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jobtasks jobtasks) throws NonexistentEntityException, Exception {
        jobtasks.getJobtasksPK().setJobCustomerAccountcustomerid(jobtasks.getJob().getJobPK().getCustomerAccountcustomerid());
        jobtasks.getJobtasksPK().setTasktaskid(jobtasks.getTask().getTaskId());
        jobtasks.getJobtasksPK().setJobjobid(jobtasks.getJob().getJobPK().getJobId());
        jobtasks.getJobtasksPK().setJobfkemail(jobtasks.getJob().getJobPK().getFkEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jobtasks persistentJobtasks = em.find(Jobtasks.class, jobtasks.getJobtasksPK());
            User useruseridOld = persistentJobtasks.getUseruserid();
            User useruseridNew = jobtasks.getUseruserid();
            Job jobOld = persistentJobtasks.getJob();
            Job jobNew = jobtasks.getJob();
            Task taskOld = persistentJobtasks.getTask();
            Task taskNew = jobtasks.getTask();
            if (useruseridNew != null) {
                useruseridNew = em.getReference(useruseridNew.getClass(), useruseridNew.getUserId());
                jobtasks.setUseruserid(useruseridNew);
            }
            if (jobNew != null) {
                jobNew = em.getReference(jobNew.getClass(), jobNew.getJobPK());
                jobtasks.setJob(jobNew);
            }
            if (taskNew != null) {
                taskNew = em.getReference(taskNew.getClass(), taskNew.getTaskId());
                jobtasks.setTask(taskNew);
            }
            jobtasks = em.merge(jobtasks);
            if (useruseridOld != null && !useruseridOld.equals(useruseridNew)) {
                useruseridOld.getJobtasksList().remove(jobtasks);
                useruseridOld = em.merge(useruseridOld);
            }
            if (useruseridNew != null && !useruseridNew.equals(useruseridOld)) {
                useruseridNew.getJobtasksList().add(jobtasks);
                useruseridNew = em.merge(useruseridNew);
            }
            if (jobOld != null && !jobOld.equals(jobNew)) {
                jobOld.getJobtasksList().remove(jobtasks);
                jobOld = em.merge(jobOld);
            }
            if (jobNew != null && !jobNew.equals(jobOld)) {
                jobNew.getJobtasksList().add(jobtasks);
                jobNew = em.merge(jobNew);
            }
            if (taskOld != null && !taskOld.equals(taskNew)) {
                taskOld.getJobtasksList().remove(jobtasks);
                taskOld = em.merge(taskOld);
            }
            if (taskNew != null && !taskNew.equals(taskOld)) {
                taskNew.getJobtasksList().add(jobtasks);
                taskNew = em.merge(taskNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JobtasksPK id = jobtasks.getJobtasksPK();
                if (findJobtasks(id) == null) {
                    throw new NonexistentEntityException("The jobtasks with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JobtasksPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jobtasks jobtasks;
            try {
                jobtasks = em.getReference(Jobtasks.class, id);
                jobtasks.getJobtasksPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jobtasks with id " + id + " no longer exists.", enfe);
            }
            User useruserid = jobtasks.getUseruserid();
            if (useruserid != null) {
                useruserid.getJobtasksList().remove(jobtasks);
                useruserid = em.merge(useruserid);
            }
            Job job = jobtasks.getJob();
            if (job != null) {
                job.getJobtasksList().remove(jobtasks);
                job = em.merge(job);
            }
            Task task = jobtasks.getTask();
            if (task != null) {
                task.getJobtasksList().remove(jobtasks);
                task = em.merge(task);
            }
            em.remove(jobtasks);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jobtasks> findJobtasksEntities() {
        return findJobtasksEntities(true, -1, -1);
    }

    public List<Jobtasks> findJobtasksEntities(int maxResults, int firstResult) {
        return findJobtasksEntities(false, maxResults, firstResult);
    }

    private List<Jobtasks> findJobtasksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jobtasks.class));
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

    public Jobtasks findJobtasks(JobtasksPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jobtasks.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobtasksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jobtasks> rt = cq.from(Jobtasks.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
