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
import bapers.domain.Job;
import java.util.ArrayList;
import java.util.List;
import bapers.domain.CardDetails;
import bapers.domain.PaymentInfo;
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
        if (paymentInfo.getJobList() == null) {
            paymentInfo.setJobList(new ArrayList<Job>());
        }
        if (paymentInfo.getCardDetailsList() == null) {
            paymentInfo.setCardDetailsList(new ArrayList<CardDetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : paymentInfo.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobPK());
                attachedJobList.add(jobListJobToAttach);
            }
            paymentInfo.setJobList(attachedJobList);
            List<CardDetails> attachedCardDetailsList = new ArrayList<CardDetails>();
            for (CardDetails cardDetailsListCardDetailsToAttach : paymentInfo.getCardDetailsList()) {
                cardDetailsListCardDetailsToAttach = em.getReference(cardDetailsListCardDetailsToAttach.getClass(), cardDetailsListCardDetailsToAttach.getCardDetailsPK());
                attachedCardDetailsList.add(cardDetailsListCardDetailsToAttach);
            }
            paymentInfo.setCardDetailsList(attachedCardDetailsList);
            em.persist(paymentInfo);
            for (Job jobListJob : paymentInfo.getJobList()) {
                jobListJob.getPaymentInfoList().add(paymentInfo);
                jobListJob = em.merge(jobListJob);
            }
            for (CardDetails cardDetailsListCardDetails : paymentInfo.getCardDetailsList()) {
                PaymentInfo oldPaymentInfoOfCardDetailsListCardDetails = cardDetailsListCardDetails.getPaymentInfo();
                cardDetailsListCardDetails.setPaymentInfo(paymentInfo);
                cardDetailsListCardDetails = em.merge(cardDetailsListCardDetails);
                if (oldPaymentInfoOfCardDetailsListCardDetails != null) {
                    oldPaymentInfoOfCardDetailsListCardDetails.getCardDetailsList().remove(cardDetailsListCardDetails);
                    oldPaymentInfoOfCardDetailsListCardDetails = em.merge(oldPaymentInfoOfCardDetailsListCardDetails);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaymentInfo(paymentInfo.getPaymentId()) != null) {
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
            PaymentInfo persistentPaymentInfo = em.find(PaymentInfo.class, paymentInfo.getPaymentId());
            List<Job> jobListOld = persistentPaymentInfo.getJobList();
            List<Job> jobListNew = paymentInfo.getJobList();
            List<CardDetails> cardDetailsListOld = persistentPaymentInfo.getCardDetailsList();
            List<CardDetails> cardDetailsListNew = paymentInfo.getCardDetailsList();
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
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobPK());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            paymentInfo.setJobList(jobListNew);
            List<CardDetails> attachedCardDetailsListNew = new ArrayList<CardDetails>();
            for (CardDetails cardDetailsListNewCardDetailsToAttach : cardDetailsListNew) {
                cardDetailsListNewCardDetailsToAttach = em.getReference(cardDetailsListNewCardDetailsToAttach.getClass(), cardDetailsListNewCardDetailsToAttach.getCardDetailsPK());
                attachedCardDetailsListNew.add(cardDetailsListNewCardDetailsToAttach);
            }
            cardDetailsListNew = attachedCardDetailsListNew;
            paymentInfo.setCardDetailsList(cardDetailsListNew);
            paymentInfo = em.merge(paymentInfo);
            for (Job jobListOldJob : jobListOld) {
                if (!jobListNew.contains(jobListOldJob)) {
                    jobListOldJob.getPaymentInfoList().remove(paymentInfo);
                    jobListOldJob = em.merge(jobListOldJob);
                }
            }
            for (Job jobListNewJob : jobListNew) {
                if (!jobListOld.contains(jobListNewJob)) {
                    jobListNewJob.getPaymentInfoList().add(paymentInfo);
                    jobListNewJob = em.merge(jobListNewJob);
                }
            }
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
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paymentInfo.getPaymentId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfo paymentInfo;
            try {
                paymentInfo = em.getReference(PaymentInfo.class, id);
                paymentInfo.getPaymentId();
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
                jobListJob.getPaymentInfoList().remove(paymentInfo);
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
