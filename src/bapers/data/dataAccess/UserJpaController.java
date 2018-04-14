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
import bapers.data.domain.Location;
import bapers.data.domain.ComponentTask;
import bapers.data.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, Exception {
        if (user.getComponentTaskList() == null) {
            user.setComponentTaskList(new ArrayList<ComponentTask>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Location fkLocation = user.getFkLocation();
            if (fkLocation != null) {
                fkLocation = em.getReference(fkLocation.getClass(), fkLocation.getLocation());
                user.setFkLocation(fkLocation);
            }
            List<ComponentTask> attachedComponentTaskList = new ArrayList<ComponentTask>();
            for (ComponentTask componentTaskListComponentTaskToAttach : user.getComponentTaskList()) {
                componentTaskListComponentTaskToAttach = em.getReference(componentTaskListComponentTaskToAttach.getClass(), componentTaskListComponentTaskToAttach.getComponentTaskPK());
                attachedComponentTaskList.add(componentTaskListComponentTaskToAttach);
            }
            user.setComponentTaskList(attachedComponentTaskList);
            em.persist(user);
            if (fkLocation != null) {
                fkLocation.getUserList().add(user);
                fkLocation = em.merge(fkLocation);
            }
            for (ComponentTask componentTaskListComponentTask : user.getComponentTaskList()) {
                User oldFkUsernameOfComponentTaskListComponentTask = componentTaskListComponentTask.getFkUsername();
                componentTaskListComponentTask.setFkUsername(user);
                componentTaskListComponentTask = em.merge(componentTaskListComponentTask);
                if (oldFkUsernameOfComponentTaskListComponentTask != null) {
                    oldFkUsernameOfComponentTaskListComponentTask.getComponentTaskList().remove(componentTaskListComponentTask);
                    oldFkUsernameOfComponentTaskListComponentTask = em.merge(oldFkUsernameOfComponentTaskListComponentTask);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getUsername()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUsername());
            Location fkLocationOld = persistentUser.getFkLocation();
            Location fkLocationNew = user.getFkLocation();
            List<ComponentTask> componentTaskListOld = persistentUser.getComponentTaskList();
            List<ComponentTask> componentTaskListNew = user.getComponentTaskList();
            if (fkLocationNew != null) {
                fkLocationNew = em.getReference(fkLocationNew.getClass(), fkLocationNew.getLocation());
                user.setFkLocation(fkLocationNew);
            }
            List<ComponentTask> attachedComponentTaskListNew = new ArrayList<ComponentTask>();
            for (ComponentTask componentTaskListNewComponentTaskToAttach : componentTaskListNew) {
                componentTaskListNewComponentTaskToAttach = em.getReference(componentTaskListNewComponentTaskToAttach.getClass(), componentTaskListNewComponentTaskToAttach.getComponentTaskPK());
                attachedComponentTaskListNew.add(componentTaskListNewComponentTaskToAttach);
            }
            componentTaskListNew = attachedComponentTaskListNew;
            user.setComponentTaskList(componentTaskListNew);
            user = em.merge(user);
            if (fkLocationOld != null && !fkLocationOld.equals(fkLocationNew)) {
                fkLocationOld.getUserList().remove(user);
                fkLocationOld = em.merge(fkLocationOld);
            }
            if (fkLocationNew != null && !fkLocationNew.equals(fkLocationOld)) {
                fkLocationNew.getUserList().add(user);
                fkLocationNew = em.merge(fkLocationNew);
            }
            for (ComponentTask componentTaskListOldComponentTask : componentTaskListOld) {
                if (!componentTaskListNew.contains(componentTaskListOldComponentTask)) {
                    componentTaskListOldComponentTask.setFkUsername(null);
                    componentTaskListOldComponentTask = em.merge(componentTaskListOldComponentTask);
                }
            }
            for (ComponentTask componentTaskListNewComponentTask : componentTaskListNew) {
                if (!componentTaskListOld.contains(componentTaskListNewComponentTask)) {
                    User oldFkUsernameOfComponentTaskListNewComponentTask = componentTaskListNewComponentTask.getFkUsername();
                    componentTaskListNewComponentTask.setFkUsername(user);
                    componentTaskListNewComponentTask = em.merge(componentTaskListNewComponentTask);
                    if (oldFkUsernameOfComponentTaskListNewComponentTask != null && !oldFkUsernameOfComponentTaskListNewComponentTask.equals(user)) {
                        oldFkUsernameOfComponentTaskListNewComponentTask.getComponentTaskList().remove(componentTaskListNewComponentTask);
                        oldFkUsernameOfComponentTaskListNewComponentTask = em.merge(oldFkUsernameOfComponentTaskListNewComponentTask);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = user.getUsername();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Location fkLocation = user.getFkLocation();
            if (fkLocation != null) {
                fkLocation.getUserList().remove(user);
                fkLocation = em.merge(fkLocation);
            }
            List<ComponentTask> componentTaskList = user.getComponentTaskList();
            for (ComponentTask componentTaskListComponentTask : componentTaskList) {
                componentTaskListComponentTask.setFkUsername(null);
                componentTaskListComponentTask = em.merge(componentTaskListComponentTask);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
