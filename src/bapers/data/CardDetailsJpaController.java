/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.data;

import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import bapers.domain.CardDetails;
import bapers.domain.CardDetailsPK;
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
public class CardDetailsJpaController implements Serializable {

    public CardDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CardDetails cardDetails) throws PreexistingEntityException, Exception {
        if (cardDetails.getCardDetailsPK() == null) {
            cardDetails.setCardDetailsPK(new CardDetailsPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cardDetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCardDetails(cardDetails.getCardDetailsPK()) != null) {
                throw new PreexistingEntityException("CardDetails " + cardDetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CardDetails cardDetails) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cardDetails = em.merge(cardDetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CardDetailsPK id = cardDetails.getCardDetailsPK();
                if (findCardDetails(id) == null) {
                    throw new NonexistentEntityException("The cardDetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CardDetailsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CardDetails cardDetails;
            try {
                cardDetails = em.getReference(CardDetails.class, id);
                cardDetails.getCardDetailsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cardDetails with id " + id + " no longer exists.", enfe);
            }
            em.remove(cardDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CardDetails> findCardDetailsEntities() {
        return findCardDetailsEntities(true, -1, -1);
    }

    public List<CardDetails> findCardDetailsEntities(int maxResults, int firstResult) {
        return findCardDetailsEntities(false, maxResults, firstResult);
    }

    private List<CardDetails> findCardDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CardDetails.class));
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

    public CardDetails findCardDetails(CardDetailsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CardDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getCardDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CardDetails> rt = cq.from(CardDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
