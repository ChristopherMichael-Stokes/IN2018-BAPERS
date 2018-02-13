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
import bapers.domain.Jobtasks;
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
        if (user.getJobtasksList() == null) {
            user.setJobtasksList(new ArrayList<Jobtasks>());
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
            List<Jobtasks> attachedJobtasksList = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListJobtasksToAttach : user.getJobtasksList()) {
                jobtasksListJobtasksToAttach = em.getReference(jobtasksListJobtasksToAttach.getClass(), jobtasksListJobtasksToAttach.getJobtasksPK());
                attachedJobtasksList.add(jobtasksListJobtasksToAttach);
            }
            user.setJobtasksList(attachedJobtasksList);
            em.persist(user);
            if (fkType != null) {
                fkType.getUserList().add(user);
                fkType = em.merge(fkType);
            }
            for (Jobtasks jobtasksListJobtasks : user.getJobtasksList()) {
                User oldUseruseridOfJobtasksListJobtasks = jobtasksListJobtasks.getUseruserid();
                jobtasksListJobtasks.setUseruserid(user);
                jobtasksListJobtasks = em.merge(jobtasksListJobtasks);
                if (oldUseruseridOfJobtasksListJobtasks != null) {
                    oldUseruseridOfJobtasksListJobtasks.getJobtasksList().remove(jobtasksListJobtasks);
                    oldUseruseridOfJobtasksListJobtasks = em.merge(oldUseruseridOfJobtasksListJobtasks);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getUserId()) != null) {
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
            User persistentUser = em.find(User.class, user.getUserId());
            UserType fkTypeOld = persistentUser.getFkType();
            UserType fkTypeNew = user.getFkType();
            List<Jobtasks> jobtasksListOld = persistentUser.getJobtasksList();
            List<Jobtasks> jobtasksListNew = user.getJobtasksList();
            List<String> illegalOrphanMessages = null;
            for (Jobtasks jobtasksListOldJobtasks : jobtasksListOld) {
                if (!jobtasksListNew.contains(jobtasksListOldJobtasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jobtasks " + jobtasksListOldJobtasks + " since its useruserid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkTypeNew != null) {
                fkTypeNew = em.getReference(fkTypeNew.getClass(), fkTypeNew.getType());
                user.setFkType(fkTypeNew);
            }
            List<Jobtasks> attachedJobtasksListNew = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListNewJobtasksToAttach : jobtasksListNew) {
                jobtasksListNewJobtasksToAttach = em.getReference(jobtasksListNewJobtasksToAttach.getClass(), jobtasksListNewJobtasksToAttach.getJobtasksPK());
                attachedJobtasksListNew.add(jobtasksListNewJobtasksToAttach);
            }
            jobtasksListNew = attachedJobtasksListNew;
            user.setJobtasksList(jobtasksListNew);
            user = em.merge(user);
            if (fkTypeOld != null && !fkTypeOld.equals(fkTypeNew)) {
                fkTypeOld.getUserList().remove(user);
                fkTypeOld = em.merge(fkTypeOld);
            }
            if (fkTypeNew != null && !fkTypeNew.equals(fkTypeOld)) {
                fkTypeNew.getUserList().add(user);
                fkTypeNew = em.merge(fkTypeNew);
            }
            for (Jobtasks jobtasksListNewJobtasks : jobtasksListNew) {
                if (!jobtasksListOld.contains(jobtasksListNewJobtasks)) {
                    User oldUseruseridOfJobtasksListNewJobtasks = jobtasksListNewJobtasks.getUseruserid();
                    jobtasksListNewJobtasks.setUseruserid(user);
                    jobtasksListNewJobtasks = em.merge(jobtasksListNewJobtasks);
                    if (oldUseruseridOfJobtasksListNewJobtasks != null && !oldUseruseridOfJobtasksListNewJobtasks.equals(user)) {
                        oldUseruseridOfJobtasksListNewJobtasks.getJobtasksList().remove(jobtasksListNewJobtasks);
                        oldUseruseridOfJobtasksListNewJobtasks = em.merge(oldUseruseridOfJobtasksListNewJobtasks);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getUserId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jobtasks> jobtasksListOrphanCheck = user.getJobtasksList();
            for (Jobtasks jobtasksListOrphanCheckJobtasks : jobtasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Jobtasks " + jobtasksListOrphanCheckJobtasks + " in its jobtasksList field has a non-nullable useruserid field.");
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

    public User findUser(Integer id) {
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
