/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.data;

import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import bapers.domain.UserType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(userType);
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

    public void edit(UserType userType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userType = em.merge(userType);
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

    public void destroy(String id) throws NonexistentEntityException {
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
