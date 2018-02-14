/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.JPA;

import bapers.JPA.exceptions.IllegalOrphanException;
import bapers.JPA.exceptions.NonexistentEntityException;
import bapers.JPA.exceptions.PreexistingEntityException;
import bapers.domain.Task;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.Tasks;
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
        if (task.getTasksList() == null) {
            task.setTasksList(new ArrayList<Tasks>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tasks> attachedTasksList = new ArrayList<Tasks>();
            for (Tasks tasksListTasksToAttach : task.getTasksList()) {
                tasksListTasksToAttach = em.getReference(tasksListTasksToAttach.getClass(), tasksListTasksToAttach.getTasksPK());
                attachedTasksList.add(tasksListTasksToAttach);
            }
            task.setTasksList(attachedTasksList);
            em.persist(task);
            for (Tasks tasksListTasks : task.getTasksList()) {
                Task oldTaskOfTasksListTasks = tasksListTasks.getTask();
                tasksListTasks.setTask(task);
                tasksListTasks = em.merge(tasksListTasks);
                if (oldTaskOfTasksListTasks != null) {
                    oldTaskOfTasksListTasks.getTasksList().remove(tasksListTasks);
                    oldTaskOfTasksListTasks = em.merge(oldTaskOfTasksListTasks);
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
            List<Tasks> tasksListOld = persistentTask.getTasksList();
            List<Tasks> tasksListNew = task.getTasksList();
            List<String> illegalOrphanMessages = null;
            for (Tasks tasksListOldTasks : tasksListOld) {
                if (!tasksListNew.contains(tasksListOldTasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tasks " + tasksListOldTasks + " since its task field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tasks> attachedTasksListNew = new ArrayList<Tasks>();
            for (Tasks tasksListNewTasksToAttach : tasksListNew) {
                tasksListNewTasksToAttach = em.getReference(tasksListNewTasksToAttach.getClass(), tasksListNewTasksToAttach.getTasksPK());
                attachedTasksListNew.add(tasksListNewTasksToAttach);
            }
            tasksListNew = attachedTasksListNew;
            task.setTasksList(tasksListNew);
            task = em.merge(task);
            for (Tasks tasksListNewTasks : tasksListNew) {
                if (!tasksListOld.contains(tasksListNewTasks)) {
                    Task oldTaskOfTasksListNewTasks = tasksListNewTasks.getTask();
                    tasksListNewTasks.setTask(task);
                    tasksListNewTasks = em.merge(tasksListNewTasks);
                    if (oldTaskOfTasksListNewTasks != null && !oldTaskOfTasksListNewTasks.equals(task)) {
                        oldTaskOfTasksListNewTasks.getTasksList().remove(tasksListNewTasks);
                        oldTaskOfTasksListNewTasks = em.merge(oldTaskOfTasksListNewTasks);
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
            List<Tasks> tasksListOrphanCheck = task.getTasksList();
            for (Tasks tasksListOrphanCheckTasks : tasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Task (" + task + ") cannot be destroyed since the Tasks " + tasksListOrphanCheckTasks + " in its tasksList field has a non-nullable task field.");
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
