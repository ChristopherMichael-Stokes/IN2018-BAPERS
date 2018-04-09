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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.data.domain.CardDetails;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class PaymentInfoJpaController implements Serializable {

    public PaymentInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PaymentInfo paymentInfo) {
        if (paymentInfo.getJobList() == null) {
            paymentInfo.setJobList(new ArrayList<Job>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CardDetails cardDetails = paymentInfo.getCardDetails();
            if (cardDetails != null) {
                cardDetails = em.getReference(cardDetails.getClass(), cardDetails.getCardDetailsPK());
                paymentInfo.setCardDetails(cardDetails);
            }
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : paymentInfo.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobId());
                attachedJobList.add(jobListJobToAttach);
            }
            paymentInfo.setJobList(attachedJobList);
            em.persist(paymentInfo);
            if (cardDetails != null) {
                cardDetails.getPaymentInfoList().add(paymentInfo);
                cardDetails = em.merge(cardDetails);
            }
            for (Job jobListJob : paymentInfo.getJobList()) {
                PaymentInfo oldFkTransactionIdOfJobListJob = jobListJob.getFkTransactionId();
                jobListJob.setFkTransactionId(paymentInfo);
                jobListJob = em.merge(jobListJob);
                if (oldFkTransactionIdOfJobListJob != null) {
                    oldFkTransactionIdOfJobListJob.getJobList().remove(jobListJob);
                    oldFkTransactionIdOfJobListJob = em.merge(oldFkTransactionIdOfJobListJob);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PaymentInfo paymentInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfo persistentPaymentInfo = em.find(PaymentInfo.class, paymentInfo.getTransactionId());
            CardDetails cardDetailsOld = persistentPaymentInfo.getCardDetails();
            CardDetails cardDetailsNew = paymentInfo.getCardDetails();
            List<Job> jobListOld = persistentPaymentInfo.getJobList();
            List<Job> jobListNew = paymentInfo.getJobList();
            if (cardDetailsNew != null) {
                cardDetailsNew = em.getReference(cardDetailsNew.getClass(), cardDetailsNew.getCardDetailsPK());
                paymentInfo.setCardDetails(cardDetailsNew);
            }
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobId());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            paymentInfo.setJobList(jobListNew);
            paymentInfo = em.merge(paymentInfo);
            if (cardDetailsOld != null && !cardDetailsOld.equals(cardDetailsNew)) {
                cardDetailsOld.getPaymentInfoList().remove(paymentInfo);
                cardDetailsOld = em.merge(cardDetailsOld);
            }
            if (cardDetailsNew != null && !cardDetailsNew.equals(cardDetailsOld)) {
                cardDetailsNew.getPaymentInfoList().add(paymentInfo);
                cardDetailsNew = em.merge(cardDetailsNew);
            }
            for (Job jobListOldJob : jobListOld) {
                if (!jobListNew.contains(jobListOldJob)) {
                    jobListOldJob.setFkTransactionId(null);
                    jobListOldJob = em.merge(jobListOldJob);
                }
            }
            for (Job jobListNewJob : jobListNew) {
                if (!jobListOld.contains(jobListNewJob)) {
                    PaymentInfo oldFkTransactionIdOfJobListNewJob = jobListNewJob.getFkTransactionId();
                    jobListNewJob.setFkTransactionId(paymentInfo);
                    jobListNewJob = em.merge(jobListNewJob);
                    if (oldFkTransactionIdOfJobListNewJob != null && !oldFkTransactionIdOfJobListNewJob.equals(paymentInfo)) {
                        oldFkTransactionIdOfJobListNewJob.getJobList().remove(jobListNewJob);
                        oldFkTransactionIdOfJobListNewJob = em.merge(oldFkTransactionIdOfJobListNewJob);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paymentInfo.getTransactionId();
                if (findPaymentInfo(id) == null) {
                    throw new NonexistentEntityException("The paymentInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfo paymentInfo;
            try {
                paymentInfo = em.getReference(PaymentInfo.class, id);
                paymentInfo.getTransactionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paymentInfo with id " + id + " no longer exists.", enfe);
            }
            CardDetails cardDetails = paymentInfo.getCardDetails();
            if (cardDetails != null) {
                cardDetails.getPaymentInfoList().remove(paymentInfo);
                cardDetails = em.merge(cardDetails);
            }
            List<Job> jobList = paymentInfo.getJobList();
            for (Job jobListJob : jobList) {
                jobListJob.setFkTransactionId(null);
                jobListJob = em.merge(jobListJob);
            }
            em.remove(paymentInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PaymentInfo> findPaymentInfoEntities() {
        return findPaymentInfoEntities(true, -1, -1);
    }

    public List<PaymentInfo> findPaymentInfoEntities(int maxResults, int firstResult) {
        return findPaymentInfoEntities(false, maxResults, firstResult);
    }

    private List<PaymentInfo> findPaymentInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PaymentInfo.class));
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

    public PaymentInfo findPaymentInfo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PaymentInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaymentInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PaymentInfo> rt = cq.from(PaymentInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
