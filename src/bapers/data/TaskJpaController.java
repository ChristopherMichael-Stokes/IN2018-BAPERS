/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import bapers.domain.Jobtasks;
import bapers.domain.Task;
import java.util.ArrayList;
import java.util.List;
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
        if (task.getJobtasksList() == null) {
            task.setJobtasksList(new ArrayList<Jobtasks>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Jobtasks> attachedJobtasksList = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListJobtasksToAttach : task.getJobtasksList()) {
                jobtasksListJobtasksToAttach = em.getReference(jobtasksListJobtasksToAttach.getClass(), jobtasksListJobtasksToAttach.getJobtasksPK());
                attachedJobtasksList.add(jobtasksListJobtasksToAttach);
            }
            task.setJobtasksList(attachedJobtasksList);
            em.persist(task);
            for (Jobtasks jobtasksListJobtasks : task.getJobtasksList()) {
                Task oldTaskOfJobtasksListJobtasks = jobtasksListJobtasks.getTask();
                jobtasksListJobtasks.setTask(task);
                jobtasksListJobtasks = em.merge(jobtasksListJobtasks);
                if (oldTaskOfJobtasksListJobtasks != null) {
                    oldTaskOfJobtasksListJobtasks.getJobtasksList().remove(jobtasksListJobtasks);
                    oldTaskOfJobtasksListJobtasks = em.merge(oldTaskOfJobtasksListJobtasks);
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
            List<Jobtasks> jobtasksListOld = persistentTask.getJobtasksList();
            List<Jobtasks> jobtasksListNew = task.getJobtasksList();
            List<String> illegalOrphanMessages = null;
            for (Jobtasks jobtasksListOldJobtasks : jobtasksListOld) {
                if (!jobtasksListNew.contains(jobtasksListOldJobtasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jobtasks " + jobtasksListOldJobtasks + " since its task field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Jobtasks> attachedJobtasksListNew = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListNewJobtasksToAttach : jobtasksListNew) {
                jobtasksListNewJobtasksToAttach = em.getReference(jobtasksListNewJobtasksToAttach.getClass(), jobtasksListNewJobtasksToAttach.getJobtasksPK());
                attachedJobtasksListNew.add(jobtasksListNewJobtasksToAttach);
            }
            jobtasksListNew = attachedJobtasksListNew;
            task.setJobtasksList(jobtasksListNew);
            task = em.merge(task);
            for (Jobtasks jobtasksListNewJobtasks : jobtasksListNew) {
                if (!jobtasksListOld.contains(jobtasksListNewJobtasks)) {
                    Task oldTaskOfJobtasksListNewJobtasks = jobtasksListNewJobtasks.getTask();
                    jobtasksListNewJobtasks.setTask(task);
                    jobtasksListNewJobtasks = em.merge(jobtasksListNewJobtasks);
                    if (oldTaskOfJobtasksListNewJobtasks != null && !oldTaskOfJobtasksListNewJobtasks.equals(task)) {
                        oldTaskOfJobtasksListNewJobtasks.getJobtasksList().remove(jobtasksListNewJobtasks);
                        oldTaskOfJobtasksListNewJobtasks = em.merge(oldTaskOfJobtasksListNewJobtasks);
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
            List<Jobtasks> jobtasksListOrphanCheck = task.getJobtasksList();
            for (Jobtasks jobtasksListOrphanCheckJobtasks : jobtasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Task (" + task + ") cannot be destroyed since the Jobtasks " + jobtasksListOrphanCheckJobtasks + " in its jobtasksList field has a non-nullable task field.");
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