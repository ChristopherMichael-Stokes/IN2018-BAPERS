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
import bapers.domain.UserType;
import bapers.domain.Tasks;
import bapers.domain.User;
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
        if (user.getTasksList() == null) {
            user.setTasksList(new ArrayList<Tasks>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserType fkType = user.getFkType();
            if (fkType != null) {
                fkType = em.getReference(fkType.getClass(), fkType.getType());
                user.setFkType(fkType);
            }
            List<Tasks> attachedTasksList = new ArrayList<Tasks>();
            for (Tasks tasksListTasksToAttach : user.getTasksList()) {
                tasksListTasksToAttach = em.getReference(tasksListTasksToAttach.getClass(), tasksListTasksToAttach.getTasksPK());
                attachedTasksList.add(tasksListTasksToAttach);
            }
            user.setTasksList(attachedTasksList);
            em.persist(user);
            if (fkType != null) {
                fkType.getUserList().add(user);
                fkType = em.merge(fkType);
            }
            for (Tasks tasksListTasks : user.getTasksList()) {
                User oldUserusernameOfTasksListTasks = tasksListTasks.getUserusername();
                tasksListTasks.setUserusername(user);
                tasksListTasks = em.merge(tasksListTasks);
                if (oldUserusernameOfTasksListTasks != null) {
                    oldUserusernameOfTasksListTasks.getTasksList().remove(tasksListTasks);
                    oldUserusernameOfTasksListTasks = em.merge(oldUserusernameOfTasksListTasks);
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

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUsername());
            UserType fkTypeOld = persistentUser.getFkType();
            UserType fkTypeNew = user.getFkType();
            List<Tasks> tasksListOld = persistentUser.getTasksList();
            List<Tasks> tasksListNew = user.getTasksList();
            List<String> illegalOrphanMessages = null;
            for (Tasks tasksListOldTasks : tasksListOld) {
                if (!tasksListNew.contains(tasksListOldTasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tasks " + tasksListOldTasks + " since its userusername field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkTypeNew != null) {
                fkTypeNew = em.getReference(fkTypeNew.getClass(), fkTypeNew.getType());
                user.setFkType(fkTypeNew);
            }
            List<Tasks> attachedTasksListNew = new ArrayList<Tasks>();
            for (Tasks tasksListNewTasksToAttach : tasksListNew) {
                tasksListNewTasksToAttach = em.getReference(tasksListNewTasksToAttach.getClass(), tasksListNewTasksToAttach.getTasksPK());
                attachedTasksListNew.add(tasksListNewTasksToAttach);
            }
            tasksListNew = attachedTasksListNew;
            user.setTasksList(tasksListNew);
            user = em.merge(user);
            if (fkTypeOld != null && !fkTypeOld.equals(fkTypeNew)) {
                fkTypeOld.getUserList().remove(user);
                fkTypeOld = em.merge(fkTypeOld);
            }
            if (fkTypeNew != null && !fkTypeNew.equals(fkTypeOld)) {
                fkTypeNew.getUserList().add(user);
                fkTypeNew = em.merge(fkTypeNew);
            }
            for (Tasks tasksListNewTasks : tasksListNew) {
                if (!tasksListOld.contains(tasksListNewTasks)) {
                    User oldUserusernameOfTasksListNewTasks = tasksListNewTasks.getUserusername();
                    tasksListNewTasks.setUserusername(user);
                    tasksListNewTasks = em.merge(tasksListNewTasks);
                    if (oldUserusernameOfTasksListNewTasks != null && !oldUserusernameOfTasksListNewTasks.equals(user)) {
                        oldUserusernameOfTasksListNewTasks.getTasksList().remove(tasksListNewTasks);
                        oldUserusernameOfTasksListNewTasks = em.merge(oldUserusernameOfTasksListNewTasks);
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Tasks> tasksListOrphanCheck = user.getTasksList();
            for (Tasks tasksListOrphanCheckTasks : tasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Tasks " + tasksListOrphanCheckTasks + " in its tasksList field has a non-nullable userusername field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserType fkType = user.getFkType();
            if (fkType != null) {
                fkType.getUserList().remove(user);
                fkType = em.merge(fkType);
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
