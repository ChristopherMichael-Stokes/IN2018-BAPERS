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
import bapers.data.domain.CardDetails;
import bapers.data.domain.CardDetailsPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.PaymentInfo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        cardDetails.getCardDetailsPK().setFkTransactionId(cardDetails.getPaymentInfo().getTransactionId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfo paymentInfo = cardDetails.getPaymentInfo();
            if (paymentInfo != null) {
                paymentInfo = em.getReference(paymentInfo.getClass(), paymentInfo.getTransactionId());
                cardDetails.setPaymentInfo(paymentInfo);
            }
            em.persist(cardDetails);
            if (paymentInfo != null) {
                paymentInfo.getCardDetailsList().add(cardDetails);
                paymentInfo = em.merge(paymentInfo);
            }
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
        cardDetails.getCardDetailsPK().setFkTransactionId(cardDetails.getPaymentInfo().getTransactionId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CardDetails persistentCardDetails = em.find(CardDetails.class, cardDetails.getCardDetailsPK());
            PaymentInfo paymentInfoOld = persistentCardDetails.getPaymentInfo();
            PaymentInfo paymentInfoNew = cardDetails.getPaymentInfo();
            if (paymentInfoNew != null) {
                paymentInfoNew = em.getReference(paymentInfoNew.getClass(), paymentInfoNew.getTransactionId());
                cardDetails.setPaymentInfo(paymentInfoNew);
            }
            cardDetails = em.merge(cardDetails);
            if (paymentInfoOld != null && !paymentInfoOld.equals(paymentInfoNew)) {
                paymentInfoOld.getCardDetailsList().remove(cardDetails);
                paymentInfoOld = em.merge(paymentInfoOld);
            }
            if (paymentInfoNew != null && !paymentInfoNew.equals(paymentInfoOld)) {
                paymentInfoNew.getCardDetailsList().add(cardDetails);
                paymentInfoNew = em.merge(paymentInfoNew);
            }
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
            PaymentInfo paymentInfo = cardDetails.getPaymentInfo();
            if (paymentInfo != null) {
                paymentInfo.getCardDetailsList().remove(cardDetails);
                paymentInfo = em.merge(paymentInfo);
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
