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
import bapers.domain.CustomerAccount;
import bapers.domain.DiscountPlan;
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
        if (discountPlan.getCustomerAccountList() == null) {
            discountPlan.setCustomerAccountList(new ArrayList<CustomerAccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CustomerAccount> attachedCustomerAccountList = new ArrayList<CustomerAccount>();
            for (CustomerAccount customerAccountListCustomerAccountToAttach : discountPlan.getCustomerAccountList()) {
                customerAccountListCustomerAccountToAttach = em.getReference(customerAccountListCustomerAccountToAttach.getClass(), customerAccountListCustomerAccountToAttach.getCustomerAccountPK());
                attachedCustomerAccountList.add(customerAccountListCustomerAccountToAttach);
            }
            discountPlan.setCustomerAccountList(attachedCustomerAccountList);
            em.persist(discountPlan);
            for (CustomerAccount customerAccountListCustomerAccount : discountPlan.getCustomerAccountList()) {
                DiscountPlan oldDiscountPlantypeOfCustomerAccountListCustomerAccount = customerAccountListCustomerAccount.getDiscountPlantype();
                customerAccountListCustomerAccount.setDiscountPlantype(discountPlan);
                customerAccountListCustomerAccount = em.merge(customerAccountListCustomerAccount);
                if (oldDiscountPlantypeOfCustomerAccountListCustomerAccount != null) {
                    oldDiscountPlantypeOfCustomerAccountListCustomerAccount.getCustomerAccountList().remove(customerAccountListCustomerAccount);
                    oldDiscountPlantypeOfCustomerAccountListCustomerAccount = em.merge(oldDiscountPlantypeOfCustomerAccountListCustomerAccount);
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
            List<CustomerAccount> customerAccountListOld = persistentDiscountPlan.getCustomerAccountList();
            List<CustomerAccount> customerAccountListNew = discountPlan.getCustomerAccountList();
            List<String> illegalOrphanMessages = null;
            for (CustomerAccount customerAccountListOldCustomerAccount : customerAccountListOld) {
                if (!customerAccountListNew.contains(customerAccountListOldCustomerAccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CustomerAccount " + customerAccountListOldCustomerAccount + " since its discountPlantype field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<CustomerAccount> attachedCustomerAccountListNew = new ArrayList<CustomerAccount>();
            for (CustomerAccount customerAccountListNewCustomerAccountToAttach : customerAccountListNew) {
                customerAccountListNewCustomerAccountToAttach = em.getReference(customerAccountListNewCustomerAccountToAttach.getClass(), customerAccountListNewCustomerAccountToAttach.getCustomerAccountPK());
                attachedCustomerAccountListNew.add(customerAccountListNewCustomerAccountToAttach);
            }
            customerAccountListNew = attachedCustomerAccountListNew;
            discountPlan.setCustomerAccountList(customerAccountListNew);
            discountPlan = em.merge(discountPlan);
            for (CustomerAccount customerAccountListNewCustomerAccount : customerAccountListNew) {
                if (!customerAccountListOld.contains(customerAccountListNewCustomerAccount)) {
                    DiscountPlan oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount = customerAccountListNewCustomerAccount.getDiscountPlantype();
                    customerAccountListNewCustomerAccount.setDiscountPlantype(discountPlan);
                    customerAccountListNewCustomerAccount = em.merge(customerAccountListNewCustomerAccount);
                    if (oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount != null && !oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount.equals(discountPlan)) {
                        oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount.getCustomerAccountList().remove(customerAccountListNewCustomerAccount);
                        oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount = em.merge(oldDiscountPlantypeOfCustomerAccountListNewCustomerAccount);
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
            List<CustomerAccount> customerAccountListOrphanCheck = discountPlan.getCustomerAccountList();
            for (CustomerAccount customerAccountListOrphanCheckCustomerAccount : customerAccountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiscountPlan (" + discountPlan + ") cannot be destroyed since the CustomerAccount " + customerAccountListOrphanCheckCustomerAccount + " in its customerAccountList field has a non-nullable discountPlantype field.");
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
