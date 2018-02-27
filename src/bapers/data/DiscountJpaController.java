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

import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.CustomerAccount;
import bapers.domain.Discount;
import bapers.domain.DiscountPlan;
import bapers.domain.TaskDiscount;
import java.util.ArrayList;
import java.util.List;
import bapers.domain.DiscountBand;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class DiscountJpaController implements Serializable {

    public DiscountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Discount discount) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (discount.getTaskDiscountList() == null) {
            discount.setTaskDiscountList(new ArrayList<TaskDiscount>());
        }
        if (discount.getDiscountBandList() == null) {
            discount.setDiscountBandList(new ArrayList<DiscountBand>());
        }
        List<String> illegalOrphanMessages = null;
        CustomerAccount customerAccountOrphanCheck = discount.getCustomerAccount();
        if (customerAccountOrphanCheck != null) {
            Discount oldDiscountOfCustomerAccount = customerAccountOrphanCheck.getDiscount();
            if (oldDiscountOfCustomerAccount != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The CustomerAccount " + customerAccountOrphanCheck + " already has an item of type Discount whose customerAccount column cannot be null. Please make another selection for the customerAccount field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount = discount.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount = em.getReference(customerAccount.getClass(), customerAccount.getAccountNumber());
                discount.setCustomerAccount(customerAccount);
            }
            DiscountPlan fkType = discount.getFkType();
            if (fkType != null) {
                fkType = em.getReference(fkType.getClass(), fkType.getType());
                discount.setFkType(fkType);
            }
            List<TaskDiscount> attachedTaskDiscountList = new ArrayList<TaskDiscount>();
            for (TaskDiscount taskDiscountListTaskDiscountToAttach : discount.getTaskDiscountList()) {
                taskDiscountListTaskDiscountToAttach = em.getReference(taskDiscountListTaskDiscountToAttach.getClass(), taskDiscountListTaskDiscountToAttach.getTaskDiscountPK());
                attachedTaskDiscountList.add(taskDiscountListTaskDiscountToAttach);
            }
            discount.setTaskDiscountList(attachedTaskDiscountList);
            List<DiscountBand> attachedDiscountBandList = new ArrayList<DiscountBand>();
            for (DiscountBand discountBandListDiscountBandToAttach : discount.getDiscountBandList()) {
                discountBandListDiscountBandToAttach = em.getReference(discountBandListDiscountBandToAttach.getClass(), discountBandListDiscountBandToAttach.getDiscountBandPK());
                attachedDiscountBandList.add(discountBandListDiscountBandToAttach);
            }
            discount.setDiscountBandList(attachedDiscountBandList);
            em.persist(discount);
            if (customerAccount != null) {
                customerAccount.setDiscount(discount);
                customerAccount = em.merge(customerAccount);
            }
            if (fkType != null) {
                fkType.getDiscountList().add(discount);
                fkType = em.merge(fkType);
            }
            for (TaskDiscount taskDiscountListTaskDiscount : discount.getTaskDiscountList()) {
                Discount oldDiscountOfTaskDiscountListTaskDiscount = taskDiscountListTaskDiscount.getDiscount();
                taskDiscountListTaskDiscount.setDiscount(discount);
                taskDiscountListTaskDiscount = em.merge(taskDiscountListTaskDiscount);
                if (oldDiscountOfTaskDiscountListTaskDiscount != null) {
                    oldDiscountOfTaskDiscountListTaskDiscount.getTaskDiscountList().remove(taskDiscountListTaskDiscount);
                    oldDiscountOfTaskDiscountListTaskDiscount = em.merge(oldDiscountOfTaskDiscountListTaskDiscount);
                }
            }
            for (DiscountBand discountBandListDiscountBand : discount.getDiscountBandList()) {
                Discount oldDiscountOfDiscountBandListDiscountBand = discountBandListDiscountBand.getDiscount();
                discountBandListDiscountBand.setDiscount(discount);
                discountBandListDiscountBand = em.merge(discountBandListDiscountBand);
                if (oldDiscountOfDiscountBandListDiscountBand != null) {
                    oldDiscountOfDiscountBandListDiscountBand.getDiscountBandList().remove(discountBandListDiscountBand);
                    oldDiscountOfDiscountBandListDiscountBand = em.merge(oldDiscountOfDiscountBandListDiscountBand);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiscount(discount.getFkAccountNumber()) != null) {
                throw new PreexistingEntityException("Discount " + discount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Discount discount) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Discount persistentDiscount = em.find(Discount.class, discount.getFkAccountNumber());
            CustomerAccount customerAccountOld = persistentDiscount.getCustomerAccount();
            CustomerAccount customerAccountNew = discount.getCustomerAccount();
            DiscountPlan fkTypeOld = persistentDiscount.getFkType();
            DiscountPlan fkTypeNew = discount.getFkType();
            List<TaskDiscount> taskDiscountListOld = persistentDiscount.getTaskDiscountList();
            List<TaskDiscount> taskDiscountListNew = discount.getTaskDiscountList();
            List<DiscountBand> discountBandListOld = persistentDiscount.getDiscountBandList();
            List<DiscountBand> discountBandListNew = discount.getDiscountBandList();
            List<String> illegalOrphanMessages = null;
            if (customerAccountNew != null && !customerAccountNew.equals(customerAccountOld)) {
                Discount oldDiscountOfCustomerAccount = customerAccountNew.getDiscount();
                if (oldDiscountOfCustomerAccount != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The CustomerAccount " + customerAccountNew + " already has an item of type Discount whose customerAccount column cannot be null. Please make another selection for the customerAccount field.");
                }
            }
            for (TaskDiscount taskDiscountListOldTaskDiscount : taskDiscountListOld) {
                if (!taskDiscountListNew.contains(taskDiscountListOldTaskDiscount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TaskDiscount " + taskDiscountListOldTaskDiscount + " since its discount field is not nullable.");
                }
            }
            for (DiscountBand discountBandListOldDiscountBand : discountBandListOld) {
                if (!discountBandListNew.contains(discountBandListOldDiscountBand)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscountBand " + discountBandListOldDiscountBand + " since its discount field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerAccountNew != null) {
                customerAccountNew = em.getReference(customerAccountNew.getClass(), customerAccountNew.getAccountNumber());
                discount.setCustomerAccount(customerAccountNew);
            }
            if (fkTypeNew != null) {
                fkTypeNew = em.getReference(fkTypeNew.getClass(), fkTypeNew.getType());
                discount.setFkType(fkTypeNew);
            }
            List<TaskDiscount> attachedTaskDiscountListNew = new ArrayList<TaskDiscount>();
            for (TaskDiscount taskDiscountListNewTaskDiscountToAttach : taskDiscountListNew) {
                taskDiscountListNewTaskDiscountToAttach = em.getReference(taskDiscountListNewTaskDiscountToAttach.getClass(), taskDiscountListNewTaskDiscountToAttach.getTaskDiscountPK());
                attachedTaskDiscountListNew.add(taskDiscountListNewTaskDiscountToAttach);
            }
            taskDiscountListNew = attachedTaskDiscountListNew;
            discount.setTaskDiscountList(taskDiscountListNew);
            List<DiscountBand> attachedDiscountBandListNew = new ArrayList<DiscountBand>();
            for (DiscountBand discountBandListNewDiscountBandToAttach : discountBandListNew) {
                discountBandListNewDiscountBandToAttach = em.getReference(discountBandListNewDiscountBandToAttach.getClass(), discountBandListNewDiscountBandToAttach.getDiscountBandPK());
                attachedDiscountBandListNew.add(discountBandListNewDiscountBandToAttach);
            }
            discountBandListNew = attachedDiscountBandListNew;
            discount.setDiscountBandList(discountBandListNew);
            discount = em.merge(discount);
            if (customerAccountOld != null && !customerAccountOld.equals(customerAccountNew)) {
                customerAccountOld.setDiscount(null);
                customerAccountOld = em.merge(customerAccountOld);
            }
            if (customerAccountNew != null && !customerAccountNew.equals(customerAccountOld)) {
                customerAccountNew.setDiscount(discount);
                customerAccountNew = em.merge(customerAccountNew);
            }
            if (fkTypeOld != null && !fkTypeOld.equals(fkTypeNew)) {
                fkTypeOld.getDiscountList().remove(discount);
                fkTypeOld = em.merge(fkTypeOld);
            }
            if (fkTypeNew != null && !fkTypeNew.equals(fkTypeOld)) {
                fkTypeNew.getDiscountList().add(discount);
                fkTypeNew = em.merge(fkTypeNew);
            }
            for (TaskDiscount taskDiscountListNewTaskDiscount : taskDiscountListNew) {
                if (!taskDiscountListOld.contains(taskDiscountListNewTaskDiscount)) {
                    Discount oldDiscountOfTaskDiscountListNewTaskDiscount = taskDiscountListNewTaskDiscount.getDiscount();
                    taskDiscountListNewTaskDiscount.setDiscount(discount);
                    taskDiscountListNewTaskDiscount = em.merge(taskDiscountListNewTaskDiscount);
                    if (oldDiscountOfTaskDiscountListNewTaskDiscount != null && !oldDiscountOfTaskDiscountListNewTaskDiscount.equals(discount)) {
                        oldDiscountOfTaskDiscountListNewTaskDiscount.getTaskDiscountList().remove(taskDiscountListNewTaskDiscount);
                        oldDiscountOfTaskDiscountListNewTaskDiscount = em.merge(oldDiscountOfTaskDiscountListNewTaskDiscount);
                    }
                }
            }
            for (DiscountBand discountBandListNewDiscountBand : discountBandListNew) {
                if (!discountBandListOld.contains(discountBandListNewDiscountBand)) {
                    Discount oldDiscountOfDiscountBandListNewDiscountBand = discountBandListNewDiscountBand.getDiscount();
                    discountBandListNewDiscountBand.setDiscount(discount);
                    discountBandListNewDiscountBand = em.merge(discountBandListNewDiscountBand);
                    if (oldDiscountOfDiscountBandListNewDiscountBand != null && !oldDiscountOfDiscountBandListNewDiscountBand.equals(discount)) {
                        oldDiscountOfDiscountBandListNewDiscountBand.getDiscountBandList().remove(discountBandListNewDiscountBand);
                        oldDiscountOfDiscountBandListNewDiscountBand = em.merge(oldDiscountOfDiscountBandListNewDiscountBand);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = discount.getFkAccountNumber();
                if (findDiscount(id) == null) {
                    throw new NonexistentEntityException("The discount with id " + id + " no longer exists.");
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
            Discount discount;
            try {
                discount = em.getReference(Discount.class, id);
                discount.getFkAccountNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discount with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TaskDiscount> taskDiscountListOrphanCheck = discount.getTaskDiscountList();
            for (TaskDiscount taskDiscountListOrphanCheckTaskDiscount : taskDiscountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Discount (" + discount + ") cannot be destroyed since the TaskDiscount " + taskDiscountListOrphanCheckTaskDiscount + " in its taskDiscountList field has a non-nullable discount field.");
            }
            List<DiscountBand> discountBandListOrphanCheck = discount.getDiscountBandList();
            for (DiscountBand discountBandListOrphanCheckDiscountBand : discountBandListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Discount (" + discount + ") cannot be destroyed since the DiscountBand " + discountBandListOrphanCheckDiscountBand + " in its discountBandList field has a non-nullable discount field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CustomerAccount customerAccount = discount.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount.setDiscount(null);
                customerAccount = em.merge(customerAccount);
            }
            DiscountPlan fkType = discount.getFkType();
            if (fkType != null) {
                fkType.getDiscountList().remove(discount);
                fkType = em.merge(fkType);
            }
            em.remove(discount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Discount> findDiscountEntities() {
        return findDiscountEntities(true, -1, -1);
    }

    public List<Discount> findDiscountEntities(int maxResults, int firstResult) {
        return findDiscountEntities(false, maxResults, firstResult);
    }

    private List<Discount> findDiscountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Discount.class));
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

    public Discount findDiscount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Discount.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Discount> rt = cq.from(Discount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
