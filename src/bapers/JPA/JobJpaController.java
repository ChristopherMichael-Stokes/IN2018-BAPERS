/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.JPA;

import bapers.JPA.exceptions.IllegalOrphanException;
import bapers.JPA.exceptions.NonexistentEntityException;
import bapers.JPA.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.CustomerAccount;
import bapers.domain.Job;
import bapers.domain.JobPK;
import bapers.domain.PaymentInfo;
import bapers.domain.Tasks;
import java.util.ArrayList;
import java.util.List;
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
        if (job.getTasksList() == null) {
            job.setTasksList(new ArrayList<Tasks>());
        }
        job.getJobPK().setFkEmail(job.getCustomerAccount().getCustomerAccountPK().getEmail());
        job.getJobPK().setFkPaymentId(job.getPaymentInfo().getPaymentId());
        job.getJobPK().setFkCustomerId(job.getCustomerAccount().getCustomerAccountPK().getCustomerId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount = job.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount = em.getReference(customerAccount.getClass(), customerAccount.getCustomerAccountPK());
                job.setCustomerAccount(customerAccount);
            }
            PaymentInfo paymentInfo = job.getPaymentInfo();
            if (paymentInfo != null) {
                paymentInfo = em.getReference(paymentInfo.getClass(), paymentInfo.getPaymentId());
                job.setPaymentInfo(paymentInfo);
            }
            List<Tasks> attachedTasksList = new ArrayList<Tasks>();
            for (Tasks tasksListTasksToAttach : job.getTasksList()) {
                tasksListTasksToAttach = em.getReference(tasksListTasksToAttach.getClass(), tasksListTasksToAttach.getTasksPK());
                attachedTasksList.add(tasksListTasksToAttach);
            }
            job.setTasksList(attachedTasksList);
            em.persist(job);
            if (customerAccount != null) {
                customerAccount.getJobList().add(job);
                customerAccount = em.merge(customerAccount);
            }
            if (paymentInfo != null) {
                paymentInfo.getJobList().add(job);
                paymentInfo = em.merge(paymentInfo);
            }
            for (Tasks tasksListTasks : job.getTasksList()) {
                Job oldJobOfTasksListTasks = tasksListTasks.getJob();
                tasksListTasks.setJob(job);
                tasksListTasks = em.merge(tasksListTasks);
                if (oldJobOfTasksListTasks != null) {
                    oldJobOfTasksListTasks.getTasksList().remove(tasksListTasks);
                    oldJobOfTasksListTasks = em.merge(oldJobOfTasksListTasks);
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
        job.getJobPK().setFkEmail(job.getCustomerAccount().getCustomerAccountPK().getEmail());
        job.getJobPK().setFkPaymentId(job.getPaymentInfo().getPaymentId());
        job.getJobPK().setFkCustomerId(job.getCustomerAccount().getCustomerAccountPK().getCustomerId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Job persistentJob = em.find(Job.class, job.getJobPK());
            CustomerAccount customerAccountOld = persistentJob.getCustomerAccount();
            CustomerAccount customerAccountNew = job.getCustomerAccount();
            PaymentInfo paymentInfoOld = persistentJob.getPaymentInfo();
            PaymentInfo paymentInfoNew = job.getPaymentInfo();
            List<Tasks> tasksListOld = persistentJob.getTasksList();
            List<Tasks> tasksListNew = job.getTasksList();
            List<String> illegalOrphanMessages = null;
            for (Tasks tasksListOldTasks : tasksListOld) {
                if (!tasksListNew.contains(tasksListOldTasks)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tasks " + tasksListOldTasks + " since its job field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerAccountNew != null) {
                customerAccountNew = em.getReference(customerAccountNew.getClass(), customerAccountNew.getCustomerAccountPK());
                job.setCustomerAccount(customerAccountNew);
            }
            if (paymentInfoNew != null) {
                paymentInfoNew = em.getReference(paymentInfoNew.getClass(), paymentInfoNew.getPaymentId());
                job.setPaymentInfo(paymentInfoNew);
            }
            List<Tasks> attachedTasksListNew = new ArrayList<Tasks>();
            for (Tasks tasksListNewTasksToAttach : tasksListNew) {
                tasksListNewTasksToAttach = em.getReference(tasksListNewTasksToAttach.getClass(), tasksListNewTasksToAttach.getTasksPK());
                attachedTasksListNew.add(tasksListNewTasksToAttach);
            }
            tasksListNew = attachedTasksListNew;
            job.setTasksList(tasksListNew);
            job = em.merge(job);
            if (customerAccountOld != null && !customerAccountOld.equals(customerAccountNew)) {
                customerAccountOld.getJobList().remove(job);
                customerAccountOld = em.merge(customerAccountOld);
            }
            if (customerAccountNew != null && !customerAccountNew.equals(customerAccountOld)) {
                customerAccountNew.getJobList().add(job);
                customerAccountNew = em.merge(customerAccountNew);
            }
            if (paymentInfoOld != null && !paymentInfoOld.equals(paymentInfoNew)) {
                paymentInfoOld.getJobList().remove(job);
                paymentInfoOld = em.merge(paymentInfoOld);
            }
            if (paymentInfoNew != null && !paymentInfoNew.equals(paymentInfoOld)) {
                paymentInfoNew.getJobList().add(job);
                paymentInfoNew = em.merge(paymentInfoNew);
            }
            for (Tasks tasksListNewTasks : tasksListNew) {
                if (!tasksListOld.contains(tasksListNewTasks)) {
                    Job oldJobOfTasksListNewTasks = tasksListNewTasks.getJob();
                    tasksListNewTasks.setJob(job);
                    tasksListNewTasks = em.merge(tasksListNewTasks);
                    if (oldJobOfTasksListNewTasks != null && !oldJobOfTasksListNewTasks.equals(job)) {
                        oldJobOfTasksListNewTasks.getTasksList().remove(tasksListNewTasks);
                        oldJobOfTasksListNewTasks = em.merge(oldJobOfTasksListNewTasks);
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
            List<Tasks> tasksListOrphanCheck = job.getTasksList();
            for (Tasks tasksListOrphanCheckTasks : tasksListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Job (" + job + ") cannot be destroyed since the Tasks " + tasksListOrphanCheckTasks + " in its tasksList field has a non-nullable job field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CustomerAccount customerAccount = job.getCustomerAccount();
            if (customerAccount != null) {
                customerAccount.getJobList().remove(job);
                customerAccount = em.merge(customerAccount);
            }
            PaymentInfo paymentInfo = job.getPaymentInfo();
            if (paymentInfo != null) {
                paymentInfo.getJobList().remove(job);
                paymentInfo = em.merge(paymentInfo);
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
