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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.Task;
import bapers.data.domain.Discount;
import bapers.data.domain.TaskDiscount;
import bapers.data.domain.TaskDiscountPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class TaskDiscountJpaController implements Serializable {

    public TaskDiscountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TaskDiscount taskDiscount) throws PreexistingEntityException, Exception {
        if (taskDiscount.getTaskDiscountPK() == null) {
            taskDiscount.setTaskDiscountPK(new TaskDiscountPK());
        }
        taskDiscount.getTaskDiscountPK().setFkAccountNumber(taskDiscount.getDiscount().getFkAccountNumber());
        taskDiscount.getTaskDiscountPK().setFkTaskId(taskDiscount.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task task = taskDiscount.getTask();
            if (task != null) {
                task = em.getReference(task.getClass(), task.getTaskId());
                taskDiscount.setTask(task);
            }
            Discount discount = taskDiscount.getDiscount();
            if (discount != null) {
                discount = em.getReference(discount.getClass(), discount.getFkAccountNumber());
                taskDiscount.setDiscount(discount);
            }
            em.persist(taskDiscount);
            if (task != null) {
                task.getTaskDiscountList().add(taskDiscount);
                task = em.merge(task);
            }
            if (discount != null) {
                discount.getTaskDiscountList().add(taskDiscount);
                discount = em.merge(discount);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTaskDiscount(taskDiscount.getTaskDiscountPK()) != null) {
                throw new PreexistingEntityException("TaskDiscount " + taskDiscount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TaskDiscount taskDiscount) throws NonexistentEntityException, Exception {
        taskDiscount.getTaskDiscountPK().setFkAccountNumber(taskDiscount.getDiscount().getFkAccountNumber());
        taskDiscount.getTaskDiscountPK().setFkTaskId(taskDiscount.getTask().getTaskId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TaskDiscount persistentTaskDiscount = em.find(TaskDiscount.class, taskDiscount.getTaskDiscountPK());
            Task taskOld = persistentTaskDiscount.getTask();
            Task taskNew = taskDiscount.getTask();
            Discount discountOld = persistentTaskDiscount.getDiscount();
            Discount discountNew = taskDiscount.getDiscount();
            if (taskNew != null) {
                taskNew = em.getReference(taskNew.getClass(), taskNew.getTaskId());
                taskDiscount.setTask(taskNew);
            }
            if (discountNew != null) {
                discountNew = em.getReference(discountNew.getClass(), discountNew.getFkAccountNumber());
                taskDiscount.setDiscount(discountNew);
            }
            taskDiscount = em.merge(taskDiscount);
            if (taskOld != null && !taskOld.equals(taskNew)) {
                taskOld.getTaskDiscountList().remove(taskDiscount);
                taskOld = em.merge(taskOld);
            }
            if (taskNew != null && !taskNew.equals(taskOld)) {
                taskNew.getTaskDiscountList().add(taskDiscount);
                taskNew = em.merge(taskNew);
            }
            if (discountOld != null && !discountOld.equals(discountNew)) {
                discountOld.getTaskDiscountList().remove(taskDiscount);
                discountOld = em.merge(discountOld);
            }
            if (discountNew != null && !discountNew.equals(discountOld)) {
                discountNew.getTaskDiscountList().add(taskDiscount);
                discountNew = em.merge(discountNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TaskDiscountPK id = taskDiscount.getTaskDiscountPK();
                if (findTaskDiscount(id) == null) {
                    throw new NonexistentEntityException("The taskDiscount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TaskDiscountPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TaskDiscount taskDiscount;
            try {
                taskDiscount = em.getReference(TaskDiscount.class, id);
                taskDiscount.getTaskDiscountPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taskDiscount with id " + id + " no longer exists.", enfe);
            }
            Task task = taskDiscount.getTask();
            if (task != null) {
                task.getTaskDiscountList().remove(taskDiscount);
                task = em.merge(task);
            }
            Discount discount = taskDiscount.getDiscount();
            if (discount != null) {
                discount.getTaskDiscountList().remove(taskDiscount);
                discount = em.merge(discount);
            }
            em.remove(taskDiscount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TaskDiscount> findTaskDiscountEntities() {
        return findTaskDiscountEntities(true, -1, -1);
    }

    public List<TaskDiscount> findTaskDiscountEntities(int maxResults, int firstResult) {
        return findTaskDiscountEntities(false, maxResults, firstResult);
    }

    private List<TaskDiscount> findTaskDiscountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TaskDiscount.class));
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

    public TaskDiscount findTaskDiscount(TaskDiscountPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TaskDiscount.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskDiscountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TaskDiscount> rt = cq.from(TaskDiscount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
