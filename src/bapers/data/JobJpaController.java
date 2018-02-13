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
import bapers.domain.Job;
import bapers.domain.JobPK;
import bapers.domain.PaymentInfo;
import java.util.ArrayList;
import java.util.List;
import bapers.domain.Jobtasks;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class JobJpaController implements Serializable {

    public JobJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Job job) throws PreexistingEntityException, Exception {
        if (job.getJobPK() == null) {
            job.setJobPK(new JobPK());
        }
        if (job.getPaymentInfoList() == null) {
            job.setPaymentInfoList(new ArrayList<PaymentInfo>());
        }
        if (job.getJobtasksList() == null) {
            job.setJobtasksList(new ArrayList<Jobtasks>());
        }
        job.getJobPK().setCustomerAccountcustomerid(job.getCustomerAccount().getCustomerAccountPK().getCustomerId());
        job.getJobPK().setFkEmail(job.getCustomerAccount().getCustomerAccountPK().getEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount = job.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount = em.getReference(customerAccount.getClass(), customerAccount.getCustomerAccountPK());
                job.setCustomerAccount(customerAccount);
            }
            List<PaymentInfo> attachedPaymentInfoList = new ArrayList<PaymentInfo>();
            for (PaymentInfo paymentInfoListPaymentInfoToAttach : job.getPaymentInfoList()) {
                paymentInfoListPaymentInfoToAttach = em.getReference(paymentInfoListPaymentInfoToAttach.getClass(), paymentInfoListPaymentInfoToAttach.getPaymentId());
                attachedPaymentInfoList.add(paymentInfoListPaymentInfoToAttach);
            }
            job.setPaymentInfoList(attachedPaymentInfoList);
            List<Jobtasks> attachedJobtasksList = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListJobtasksToAttach : job.getJobtasksList()) {
                jobtasksListJobtasksToAttach = em.getReference(jobtasksListJobtasksToAttach.getClass(), jobtasksListJobtasksToAttach.getJobtasksPK());
                attachedJobtasksList.add(jobtasksListJobtasksToAttach);
            }
            job.setJobtasksList(attachedJobtasksList);
            em.persist(job);
            if (customerAccount != null) {
                customerAccount.getJobList().add(job);
                customerAccount = em.merge(customerAccount);
            }
            for (PaymentInfo paymentInfoListPaymentInfo : job.getPaymentInfoList()) {
                paymentInfoListPaymentInfo.getJobList().add(job);
                paymentInfoListPaymentInfo = em.merge(paymentInfoListPaymentInfo);
            }
            for (Jobtasks jobtasksListJobtasks : job.getJobtasksList()) {
                Job oldJobOfJobtasksListJobtasks = jobtasksListJobtasks.getJob();
                jobtasksListJobtasks.setJob(job);
                jobtasksListJobtasks = em.merge(jobtasksListJobtasks);
                if (oldJobOfJobtasksListJobtasks != null) {
                    oldJobOfJobtasksListJobtasks.getJobtasksList().remove(jobtasksListJobtasks);
                    oldJobOfJobtasksListJobtasks = em.merge(oldJobOfJobtasksListJobtasks);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJob(job.getJobPK()) != null) {
                throw new PreexistingEntityException("Job " + job + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Job job) throws IllegalOrphanException, NonexistentEntityException, Exception {
        job.getJobPK().setCustomerAccountcustomerid(job.getCustomerAccount().getCustomerAccountPK().getCustomerId());
        job.getJobPK().setFkEmail(job.getCustomerAccount().getCustomerAccountPK().getEmail());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job persistentJob = em.find(Job.class, job.getJobPK());
            CustomerAccount customerAccountOld = persistentJob.getCustomerAccount();
            CustomerAccount customerAccountNew = job.getCustomerAccount();
            List<PaymentInfo> paymentInfoListOld = persistentJob.getPaymentInfoList();
            List<PaymentInfo> paymentInfoListNew = job.getPaymentInfoList();
            List<Jobtasks> jobtasksListOld = persistentJob.getJobtasksList();
            List<Jobtasks> jobtasksListNew = job.getJobtasksList();
            List<String> illegalOrphanMessages = null;
            for (Jobtasks jobtasksListOldJobtasks : jobtasksListOld) {
                if (!jobtasksListNew.contains(jobtasksListOldJobtasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jobtasks " + jobtasksListOldJobtasks + " since its job field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerAccountNew != null) {
                customerAccountNew = em.getReference(customerAccountNew.getClass(), customerAccountNew.getCustomerAccountPK());
                job.setCustomerAccount(customerAccountNew);
            }
            List<PaymentInfo> attachedPaymentInfoListNew = new ArrayList<PaymentInfo>();
            for (PaymentInfo paymentInfoListNewPaymentInfoToAttach : paymentInfoListNew) {
                paymentInfoListNewPaymentInfoToAttach = em.getReference(paymentInfoListNewPaymentInfoToAttach.getClass(), paymentInfoListNewPaymentInfoToAttach.getPaymentId());
                attachedPaymentInfoListNew.add(paymentInfoListNewPaymentInfoToAttach);
            }
            paymentInfoListNew = attachedPaymentInfoListNew;
            job.setPaymentInfoList(paymentInfoListNew);
            List<Jobtasks> attachedJobtasksListNew = new ArrayList<Jobtasks>();
            for (Jobtasks jobtasksListNewJobtasksToAttach : jobtasksListNew) {
                jobtasksListNewJobtasksToAttach = em.getReference(jobtasksListNewJobtasksToAttach.getClass(), jobtasksListNewJobtasksToAttach.getJobtasksPK());
                attachedJobtasksListNew.add(jobtasksListNewJobtasksToAttach);
            }
            jobtasksListNew = attachedJobtasksListNew;
            job.setJobtasksList(jobtasksListNew);
            job = em.merge(job);
            if (customerAccountOld != null && !customerAccountOld.equals(customerAccountNew)) {
                customerAccountOld.getJobList().remove(job);
                customerAccountOld = em.merge(customerAccountOld);
            }
            if (customerAccountNew != null && !customerAccountNew.equals(customerAccountOld)) {
                customerAccountNew.getJobList().add(job);
                customerAccountNew = em.merge(customerAccountNew);
            }
            for (PaymentInfo paymentInfoListOldPaymentInfo : paymentInfoListOld) {
                if (!paymentInfoListNew.contains(paymentInfoListOldPaymentInfo)) {
                    paymentInfoListOldPaymentInfo.getJobList().remove(job);
                    paymentInfoListOldPaymentInfo = em.merge(paymentInfoListOldPaymentInfo);
                }
            }
            for (PaymentInfo paymentInfoListNewPaymentInfo : paymentInfoListNew) {
                if (!paymentInfoListOld.contains(paymentInfoListNewPaymentInfo)) {
                    paymentInfoListNewPaymentInfo.getJobList().add(job);
                    paymentInfoListNewPaymentInfo = em.merge(paymentInfoListNewPaymentInfo);
                }
            }
            for (Jobtasks jobtasksListNewJobtasks : jobtasksListNew) {
                if (!jobtasksListOld.contains(jobtasksListNewJobtasks)) {
                    Job oldJobOfJobtasksListNewJobtasks = jobtasksListNewJobtasks.getJob();
                    jobtasksListNewJobtasks.setJob(job);
                    jobtasksListNewJobtasks = em.merge(jobtasksListNewJobtasks);
                    if (oldJobOfJobtasksListNewJobtasks != null && !oldJobOfJobtasksListNewJobtasks.equals(job)) {
                        oldJobOfJobtasksListNewJobtasks.getJobtasksList().remove(jobtasksListNewJobtasks);
                        oldJobOfJobtasksListNewJobtasks = em.merge(oldJobOfJobtasksListNewJobtasks);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JobPK id = job.getJobPK();
                if (findJob(id) == null) {
                    throw new NonexistentEntityException("The job with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JobPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job job;
            try {
                job = em.getReference(Job.class, id);
                job.getJobPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The job with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jobtasks> jobtasksListOrphanCheck = job.getJobtasksList();
            for (Jobtasks jobtasksListOrphanCheckJobtasks : jobtasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Job (" + job + ") cannot be destroyed since the Jobtasks " + jobtasksListOrphanCheckJobtasks + " in its jobtasksList field has a non-nullable job field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CustomerAccount customerAccount = job.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount.getJobList().remove(job);
                customerAccount = em.merge(customerAccount);
            }
            List<PaymentInfo> paymentInfoList = job.getPaymentInfoList();
            for (PaymentInfo paymentInfoListPaymentInfo : paymentInfoList) {
                paymentInfoListPaymentInfo.getJobList().remove(job);
                paymentInfoListPaymentInfo = em.merge(paymentInfoListPaymentInfo);
            }
            em.remove(job);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Job> findJobEntities() {
        return findJobEntities(true, -1, -1);
    }

    public List<Job> findJobEntities(int maxResults, int firstResult) {
        return findJobEntities(false, maxResults, firstResult);
    }

    private List<Job> findJobEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Job.class));
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

    public Job findJob(JobPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Job.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Job> rt = cq.from(Job.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
