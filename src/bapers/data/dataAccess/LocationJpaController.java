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
import bapers.data.domain.Location;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.Task;
import java.util.ArrayList;
import java.util.List;
import bapers.data.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class LocationJpaController implements Serializable {

    public LocationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Location location) throws PreexistingEntityException, Exception {
        if (location.getTaskList() == null) {
            location.setTaskList(new ArrayList<Task>());
        }
        if (location.getUserList() == null) {
            location.setUserList(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Task> attachedTaskList = new ArrayList<Task>();
            for (Task taskListTaskToAttach : location.getTaskList()) {
                taskListTaskToAttach = em.getReference(taskListTaskToAttach.getClass(), taskListTaskToAttach.getTaskId());
                attachedTaskList.add(taskListTaskToAttach);
            }
            location.setTaskList(attachedTaskList);
            List<User> attachedUserList = new ArrayList<User>();
            for (User userListUserToAttach : location.getUserList()) {
                userListUserToAttach = em.getReference(userListUserToAttach.getClass(), userListUserToAttach.getUsername());
                attachedUserList.add(userListUserToAttach);
            }
            location.setUserList(attachedUserList);
            em.persist(location);
            for (Task taskListTask : location.getTaskList()) {
                Location oldFkLocationOfTaskListTask = taskListTask.getFkLocation();
                taskListTask.setFkLocation(location);
                taskListTask = em.merge(taskListTask);
                if (oldFkLocationOfTaskListTask != null) {
                    oldFkLocationOfTaskListTask.getTaskList().remove(taskListTask);
                    oldFkLocationOfTaskListTask = em.merge(oldFkLocationOfTaskListTask);
                }
            }
            for (User userListUser : location.getUserList()) {
                Location oldFkLocationOfUserListUser = userListUser.getFkLocation();
                userListUser.setFkLocation(location);
                userListUser = em.merge(userListUser);
                if (oldFkLocationOfUserListUser != null) {
                    oldFkLocationOfUserListUser.getUserList().remove(userListUser);
                    oldFkLocationOfUserListUser = em.merge(oldFkLocationOfUserListUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocation(location.getLocation()) != null) {
                throw new PreexistingEntityException("Location " + location + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Location location) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Location persistentLocation = em.find(Location.class, location.getLocation());
            List<Task> taskListOld = persistentLocation.getTaskList();
            List<Task> taskListNew = location.getTaskList();
            List<User> userListOld = persistentLocation.getUserList();
            List<User> userListNew = location.getUserList();
            List<String> illegalOrphanMessages = null;
            for (Task taskListOldTask : taskListOld) {
                if (!taskListNew.contains(taskListOldTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Task " + taskListOldTask + " since its fkLocation field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Task> attachedTaskListNew = new ArrayList<Task>();
            for (Task taskListNewTaskToAttach : taskListNew) {
                taskListNewTaskToAttach = em.getReference(taskListNewTaskToAttach.getClass(), taskListNewTaskToAttach.getTaskId());
                attachedTaskListNew.add(taskListNewTaskToAttach);
            }
            taskListNew = attachedTaskListNew;
            location.setTaskList(taskListNew);
            List<User> attachedUserListNew = new ArrayList<User>();
            for (User userListNewUserToAttach : userListNew) {
                userListNewUserToAttach = em.getReference(userListNewUserToAttach.getClass(), userListNewUserToAttach.getUsername());
                attachedUserListNew.add(userListNewUserToAttach);
            }
            userListNew = attachedUserListNew;
            location.setUserList(userListNew);
            location = em.merge(location);
            for (Task taskListNewTask : taskListNew) {
                if (!taskListOld.contains(taskListNewTask)) {
                    Location oldFkLocationOfTaskListNewTask = taskListNewTask.getFkLocation();
                    taskListNewTask.setFkLocation(location);
                    taskListNewTask = em.merge(taskListNewTask);
                    if (oldFkLocationOfTaskListNewTask != null && !oldFkLocationOfTaskListNewTask.equals(location)) {
                        oldFkLocationOfTaskListNewTask.getTaskList().remove(taskListNewTask);
                        oldFkLocationOfTaskListNewTask = em.merge(oldFkLocationOfTaskListNewTask);
                    }
                }
            }
            for (User userListOldUser : userListOld) {
                if (!userListNew.contains(userListOldUser)) {
                    userListOldUser.setFkLocation(null);
                    userListOldUser = em.merge(userListOldUser);
                }
            }
            for (User userListNewUser : userListNew) {
                if (!userListOld.contains(userListNewUser)) {
                    Location oldFkLocationOfUserListNewUser = userListNewUser.getFkLocation();
                    userListNewUser.setFkLocation(location);
                    userListNewUser = em.merge(userListNewUser);
                    if (oldFkLocationOfUserListNewUser != null && !oldFkLocationOfUserListNewUser.equals(location)) {
                        oldFkLocationOfUserListNewUser.getUserList().remove(userListNewUser);
                        oldFkLocationOfUserListNewUser = em.merge(oldFkLocationOfUserListNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = location.getLocation();
                if (findLocation(id) == null) {
                    throw new NonexistentEntityException("The location with id " + id + " no longer exists.");
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
            Location location;
            try {
                location = em.getReference(Location.class, id);
                location.getLocation();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The location with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Task> taskListOrphanCheck = location.getTaskList();
            for (Task taskListOrphanCheckTask : taskListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Location (" + location + ") cannot be destroyed since the Task " + taskListOrphanCheckTask + " in its taskList field has a non-nullable fkLocation field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<User> userList = location.getUserList();
            for (User userListUser : userList) {
                userListUser.setFkLocation(null);
                userListUser = em.merge(userListUser);
            }
            em.remove(location);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Location> findLocationEntities() {
        return findLocationEntities(true, -1, -1);
    }

    public List<Location> findLocationEntities(int maxResults, int firstResult) {
        return findLocationEntities(false, maxResults, firstResult);
    }

    private List<Location> findLocationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Location.class));
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

    public Location findLocation(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Location.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Location> rt = cq.from(Location.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
