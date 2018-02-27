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
package bapers.data;

import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.Discount;
import bapers.domain.DiscountBand;
import bapers.domain.DiscountBandPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class DiscountBandJpaController implements Serializable {

    public DiscountBandJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscountBand discountBand) throws PreexistingEntityException, Exception {
        if (discountBand.getDiscountBandPK() == null) {
            discountBand.setDiscountBandPK(new DiscountBandPK());
        }
        discountBand.getDiscountBandPK().setFkAccountNumber(discountBand.getDiscount().getFkAccountNumber());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Discount discount = discountBand.getDiscount();
            if (discount != null) {
                discount = em.getReference(discount.getClass(), discount.getFkAccountNumber());
                discountBand.setDiscount(discount);
            }
            em.persist(discountBand);
            if (discount != null) {
                discount.getDiscountBandList().add(discountBand);
                discount = em.merge(discount);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiscountBand(discountBand.getDiscountBandPK()) != null) {
                throw new PreexistingEntityException("DiscountBand " + discountBand + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiscountBand discountBand) throws NonexistentEntityException, Exception {
        discountBand.getDiscountBandPK().setFkAccountNumber(discountBand.getDiscount().getFkAccountNumber());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscountBand persistentDiscountBand = em.find(DiscountBand.class, discountBand.getDiscountBandPK());
            Discount discountOld = persistentDiscountBand.getDiscount();
            Discount discountNew = discountBand.getDiscount();
            if (discountNew != null) {
                discountNew = em.getReference(discountNew.getClass(), discountNew.getFkAccountNumber());
                discountBand.setDiscount(discountNew);
            }
            discountBand = em.merge(discountBand);
            if (discountOld != null && !discountOld.equals(discountNew)) {
                discountOld.getDiscountBandList().remove(discountBand);
                discountOld = em.merge(discountOld);
            }
            if (discountNew != null && !discountNew.equals(discountOld)) {
                discountNew.getDiscountBandList().add(discountBand);
                discountNew = em.merge(discountNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DiscountBandPK id = discountBand.getDiscountBandPK();
                if (findDiscountBand(id) == null) {
                    throw new NonexistentEntityException("The discountBand with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DiscountBandPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscountBand discountBand;
            try {
                discountBand = em.getReference(DiscountBand.class, id);
                discountBand.getDiscountBandPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discountBand with id " + id + " no longer exists.", enfe);
            }
            Discount discount = discountBand.getDiscount();
            if (discount != null) {
                discount.getDiscountBandList().remove(discountBand);
                discount = em.merge(discount);
            }
            em.remove(discountBand);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiscountBand> findDiscountBandEntities() {
        return findDiscountBandEntities(true, -1, -1);
    }

    public List<DiscountBand> findDiscountBandEntities(int maxResults, int firstResult) {
        return findDiscountBandEntities(false, maxResults, firstResult);
    }

    private List<DiscountBand> findDiscountBandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscountBand.class));
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

    public DiscountBand findDiscountBand(DiscountBandPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscountBand.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscountBandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscountBand> rt = cq.from(DiscountBand.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
