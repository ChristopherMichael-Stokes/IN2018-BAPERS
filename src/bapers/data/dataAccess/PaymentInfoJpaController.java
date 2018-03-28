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
import bapers.data.domain.CardDetails;
import java.util.ArrayList;
import java.util.List;
import bapers.data.domain.Job;
import bapers.data.domain.PaymentInfo;
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

    public void create(PaymentInfo paymentInfo) throws PreexistingEntityException, Exception {
        if (paymentInfo.getCardDetailsList() == null) {
            paymentInfo.setCardDetailsList(new ArrayList<CardDetails>());
        }
        if (paymentInfo.getJobList() == null) {
            paymentInfo.setJobList(new ArrayList<Job>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CardDetails> attachedCardDetailsList = new ArrayList<CardDetails>();
            for (CardDetails cardDetailsListCardDetailsToAttach : paymentInfo.getCardDetailsList()) {
                cardDetailsListCardDetailsToAttach = em.getReference(cardDetailsListCardDetailsToAttach.getClass(), cardDetailsListCardDetailsToAttach.getCardDetailsPK());
                attachedCardDetailsList.add(cardDetailsListCardDetailsToAttach);
            }
            paymentInfo.setCardDetailsList(attachedCardDetailsList);
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : paymentInfo.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobId());
                attachedJobList.add(jobListJobToAttach);
            }
            paymentInfo.setJobList(attachedJobList);
            em.persist(paymentInfo);
            for (CardDetails cardDetailsListCardDetails : paymentInfo.getCardDetailsList()) {
                PaymentInfo oldPaymentInfoOfCardDetailsListCardDetails = cardDetailsListCardDetails.getPaymentInfo();
                cardDetailsListCardDetails.setPaymentInfo(paymentInfo);
                cardDetailsListCardDetails = em.merge(cardDetailsListCardDetails);
                if (oldPaymentInfoOfCardDetailsListCardDetails != null) {
                    oldPaymentInfoOfCardDetailsListCardDetails.getCardDetailsList().remove(cardDetailsListCardDetails);
                    oldPaymentInfoOfCardDetailsListCardDetails = em.merge(oldPaymentInfoOfCardDetailsListCardDetails);
                }
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
        } catch (Exception ex) {
            if (findPaymentInfo(paymentInfo.getTransactionId()) != null) {
                throw new PreexistingEntityException("PaymentInfo " + paymentInfo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PaymentInfo paymentInfo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfo persistentPaymentInfo = em.find(PaymentInfo.class, paymentInfo.getTransactionId());
            List<CardDetails> cardDetailsListOld = persistentPaymentInfo.getCardDetailsList();
            List<CardDetails> cardDetailsListNew = paymentInfo.getCardDetailsList();
            List<Job> jobListOld = persistentPaymentInfo.getJobList();
            List<Job> jobListNew = paymentInfo.getJobList();
            List<String> illegalOrphanMessages = null;
            for (CardDetails cardDetailsListOldCardDetails : cardDetailsListOld) {
                if (!cardDetailsListNew.contains(cardDetailsListOldCardDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CardDetails " + cardDetailsListOldCardDetails + " since its paymentInfo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<CardDetails> attachedCardDetailsListNew = new ArrayList<CardDetails>();
            for (CardDetails cardDetailsListNewCardDetailsToAttach : cardDetailsListNew) {
                cardDetailsListNewCardDetailsToAttach = em.getReference(cardDetailsListNewCardDetailsToAttach.getClass(), cardDetailsListNewCardDetailsToAttach.getCardDetailsPK());
                attachedCardDetailsListNew.add(cardDetailsListNewCardDetailsToAttach);
            }
            cardDetailsListNew = attachedCardDetailsListNew;
            paymentInfo.setCardDetailsList(cardDetailsListNew);
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobId());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            paymentInfo.setJobList(jobListNew);
            paymentInfo = em.merge(paymentInfo);
            for (CardDetails cardDetailsListNewCardDetails : cardDetailsListNew) {
                if (!cardDetailsListOld.contains(cardDetailsListNewCardDetails)) {
                    PaymentInfo oldPaymentInfoOfCardDetailsListNewCardDetails = cardDetailsListNewCardDetails.getPaymentInfo();
                    cardDetailsListNewCardDetails.setPaymentInfo(paymentInfo);
                    cardDetailsListNewCardDetails = em.merge(cardDetailsListNewCardDetails);
                    if (oldPaymentInfoOfCardDetailsListNewCardDetails != null && !oldPaymentInfoOfCardDetailsListNewCardDetails.equals(paymentInfo)) {
                        oldPaymentInfoOfCardDetailsListNewCardDetails.getCardDetailsList().remove(cardDetailsListNewCardDetails);
                        oldPaymentInfoOfCardDetailsListNewCardDetails = em.merge(oldPaymentInfoOfCardDetailsListNewCardDetails);
                    }
                }
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
                String id = paymentInfo.getTransactionId();
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<CardDetails> cardDetailsListOrphanCheck = paymentInfo.getCardDetailsList();
            for (CardDetails cardDetailsListOrphanCheckCardDetails : cardDetailsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PaymentInfo (" + paymentInfo + ") cannot be destroyed since the CardDetails " + cardDetailsListOrphanCheckCardDetails + " in its cardDetailsList field has a non-nullable paymentInfo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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

    public PaymentInfo findPaymentInfo(String id) {
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
