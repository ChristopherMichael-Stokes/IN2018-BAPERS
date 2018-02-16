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
import bapers.domain.User;
import bapers.domain.UserType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class UserTypeJpaController implements Serializable {

    public UserTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserType userType) throws PreexistingEntityException, Exception {
        if (userType.getUserList() == null) {
            userType.setUserList(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<User> attachedUserList = new ArrayList<User>();
            for (User userListUserToAttach : userType.getUserList()) {
                userListUserToAttach = em.getReference(userListUserToAttach.getClass(), userListUserToAttach.getUsername());
                attachedUserList.add(userListUserToAttach);
            }
            userType.setUserList(attachedUserList);
            em.persist(userType);
            for (User userListUser : userType.getUserList()) {
                UserType oldFkTypeOfUserListUser = userListUser.getFkType();
                userListUser.setFkType(userType);
                userListUser = em.merge(userListUser);
                if (oldFkTypeOfUserListUser != null) {
                    oldFkTypeOfUserListUser.getUserList().remove(userListUser);
                    oldFkTypeOfUserListUser = em.merge(oldFkTypeOfUserListUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserType(userType.getType()) != null) {
                throw new PreexistingEntityException("UserType " + userType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserType userType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserType persistentUserType = em.find(UserType.class, userType.getType());
            List<User> userListOld = persistentUserType.getUserList();
            List<User> userListNew = userType.getUserList();
            List<String> illegalOrphanMessages = null;
            for (User userListOldUser : userListOld) {
                if (!userListNew.contains(userListOldUser)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain User " + userListOldUser + " since its fkType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<User> attachedUserListNew = new ArrayList<User>();
            for (User userListNewUserToAttach : userListNew) {
                userListNewUserToAttach = em.getReference(userListNewUserToAttach.getClass(), userListNewUserToAttach.getUsername());
                attachedUserListNew.add(userListNewUserToAttach);
            }
            userListNew = attachedUserListNew;
            userType.setUserList(userListNew);
            userType = em.merge(userType);
            for (User userListNewUser : userListNew) {
                if (!userListOld.contains(userListNewUser)) {
                    UserType oldFkTypeOfUserListNewUser = userListNewUser.getFkType();
                    userListNewUser.setFkType(userType);
                    userListNewUser = em.merge(userListNewUser);
                    if (oldFkTypeOfUserListNewUser != null && !oldFkTypeOfUserListNewUser.equals(userType)) {
                        oldFkTypeOfUserListNewUser.getUserList().remove(userListNewUser);
                        oldFkTypeOfUserListNewUser = em.merge(oldFkTypeOfUserListNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = userType.getType();
                if (findUserType(id) == null) {
                    throw new NonexistentEntityException("The userType with id " + id + " no longer exists.");
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
            UserType userType;
            try {
                userType = em.getReference(UserType.class, id);
                userType.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<User> userListOrphanCheck = userType.getUserList();
            for (User userListOrphanCheckUser : userListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserType (" + userType + ") cannot be destroyed since the User " + userListOrphanCheckUser + " in its userList field has a non-nullable fkType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(userType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserType> findUserTypeEntities() {
        return findUserTypeEntities(true, -1, -1);
    }

    public List<UserType> findUserTypeEntities(int maxResults, int firstResult) {
        return findUserTypeEntities(false, maxResults, firstResult);
    }

    private List<UserType> findUserTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserType.class));
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

    public UserType findUserType(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserType.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserType> rt = cq.from(UserType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
