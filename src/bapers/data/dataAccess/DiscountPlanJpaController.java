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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.Discount;
import bapers.data.domain.DiscountPlan;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class DiscountPlanJpaController implements Serializable {

    public DiscountPlanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscountPlan discountPlan) throws PreexistingEntityException, Exception {
        if (discountPlan.getDiscountList() == null) {
            discountPlan.setDiscountList(new ArrayList<Discount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Discount> attachedDiscountList = new ArrayList<Discount>();
            for (Discount discountListDiscountToAttach : discountPlan.getDiscountList()) {
                discountListDiscountToAttach = em.getReference(discountListDiscountToAttach.getClass(), discountListDiscountToAttach.getFkAccountNumber());
                attachedDiscountList.add(discountListDiscountToAttach);
            }
            discountPlan.setDiscountList(attachedDiscountList);
            em.persist(discountPlan);
            for (Discount discountListDiscount : discountPlan.getDiscountList()) {
                DiscountPlan oldFkTypeOfDiscountListDiscount = discountListDiscount.getFkType();
                discountListDiscount.setFkType(discountPlan);
                discountListDiscount = em.merge(discountListDiscount);
                if (oldFkTypeOfDiscountListDiscount != null) {
                    oldFkTypeOfDiscountListDiscount.getDiscountList().remove(discountListDiscount);
                    oldFkTypeOfDiscountListDiscount = em.merge(oldFkTypeOfDiscountListDiscount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiscountPlan(discountPlan.getType()) != null) {
                throw new PreexistingEntityException("DiscountPlan " + discountPlan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiscountPlan discountPlan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscountPlan persistentDiscountPlan = em.find(DiscountPlan.class, discountPlan.getType());
            List<Discount> discountListOld = persistentDiscountPlan.getDiscountList();
            List<Discount> discountListNew = discountPlan.getDiscountList();
            List<String> illegalOrphanMessages = null;
            for (Discount discountListOldDiscount : discountListOld) {
                if (!discountListNew.contains(discountListOldDiscount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Discount " + discountListOldDiscount + " since its fkType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Discount> attachedDiscountListNew = new ArrayList<Discount>();
            for (Discount discountListNewDiscountToAttach : discountListNew) {
                discountListNewDiscountToAttach = em.getReference(discountListNewDiscountToAttach.getClass(), discountListNewDiscountToAttach.getFkAccountNumber());
                attachedDiscountListNew.add(discountListNewDiscountToAttach);
            }
            discountListNew = attachedDiscountListNew;
            discountPlan.setDiscountList(discountListNew);
            discountPlan = em.merge(discountPlan);
            for (Discount discountListNewDiscount : discountListNew) {
                if (!discountListOld.contains(discountListNewDiscount)) {
                    DiscountPlan oldFkTypeOfDiscountListNewDiscount = discountListNewDiscount.getFkType();
                    discountListNewDiscount.setFkType(discountPlan);
                    discountListNewDiscount = em.merge(discountListNewDiscount);
                    if (oldFkTypeOfDiscountListNewDiscount != null && !oldFkTypeOfDiscountListNewDiscount.equals(discountPlan)) {
                        oldFkTypeOfDiscountListNewDiscount.getDiscountList().remove(discountListNewDiscount);
                        oldFkTypeOfDiscountListNewDiscount = em.merge(oldFkTypeOfDiscountListNewDiscount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = discountPlan.getType();
                if (findDiscountPlan(id) == null) {
                    throw new NonexistentEntityException("The discountPlan with id " + id + " no longer exists.");
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
            DiscountPlan discountPlan;
            try {
                discountPlan = em.getReference(DiscountPlan.class, id);
                discountPlan.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discountPlan with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Discount> discountListOrphanCheck = discountPlan.getDiscountList();
            for (Discount discountListOrphanCheckDiscount : discountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiscountPlan (" + discountPlan + ") cannot be destroyed since the Discount " + discountListOrphanCheckDiscount + " in its discountList field has a non-nullable fkType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(discountPlan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiscountPlan> findDiscountPlanEntities() {
        return findDiscountPlanEntities(true, -1, -1);
    }

    public List<DiscountPlan> findDiscountPlanEntities(int maxResults, int firstResult) {
        return findDiscountPlanEntities(false, maxResults, firstResult);
    }

    private List<DiscountPlan> findDiscountPlanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscountPlan.class));
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

    public DiscountPlan findDiscountPlan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscountPlan.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscountPlanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscountPlan> rt = cq.from(DiscountPlan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
