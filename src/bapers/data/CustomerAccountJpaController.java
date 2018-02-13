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
import bapers.domain.Address;
import bapers.domain.CustomerAccount;
import bapers.domain.CustomerAccountPK;
import bapers.domain.DiscountPlan;
import bapers.domain.Job;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class CustomerAccountJpaController implements Serializable {

    public CustomerAccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CustomerAccount customerAccount) throws PreexistingEntityException, Exception {
        if (customerAccount.getCustomerAccountPK() == null) {
            customerAccount.setCustomerAccountPK(new CustomerAccountPK());
        }
        if (customerAccount.getJobList() == null) {
            customerAccount.setJobList(new ArrayList<Job>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address = customerAccount.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getAddressPK());
                customerAccount.setAddress(address);
            }
            DiscountPlan discountPlantype = customerAccount.getDiscountPlantype();
            if (discountPlantype != null) {
                discountPlantype = em.getReference(discountPlantype.getClass(), discountPlantype.getType());
                customerAccount.setDiscountPlantype(discountPlantype);
            }
            List<Job> attachedJobList = new ArrayList<Job>();
            for (Job jobListJobToAttach : customerAccount.getJobList()) {
                jobListJobToAttach = em.getReference(jobListJobToAttach.getClass(), jobListJobToAttach.getJobPK());
                attachedJobList.add(jobListJobToAttach);
            }
            customerAccount.setJobList(attachedJobList);
            em.persist(customerAccount);
            if (address != null) {
                address.getCustomerAccountList().add(customerAccount);
                address = em.merge(address);
            }
            if (discountPlantype != null) {
                discountPlantype.getCustomerAccountList().add(customerAccount);
                discountPlantype = em.merge(discountPlantype);
            }
            for (Job jobListJob : customerAccount.getJobList()) {
                CustomerAccount oldCustomerAccountOfJobListJob = jobListJob.getCustomerAccount();
                jobListJob.setCustomerAccount(customerAccount);
                jobListJob = em.merge(jobListJob);
                if (oldCustomerAccountOfJobListJob != null) {
                    oldCustomerAccountOfJobListJob.getJobList().remove(jobListJob);
                    oldCustomerAccountOfJobListJob = em.merge(oldCustomerAccountOfJobListJob);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomerAccount(customerAccount.getCustomerAccountPK()) != null) {
                throw new PreexistingEntityException("CustomerAccount " + customerAccount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CustomerAccount customerAccount) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount persistentCustomerAccount = em.find(CustomerAccount.class, customerAccount.getCustomerAccountPK());
            Address addressOld = persistentCustomerAccount.getAddress();
            Address addressNew = customerAccount.getAddress();
            DiscountPlan discountPlantypeOld = persistentCustomerAccount.getDiscountPlantype();
            DiscountPlan discountPlantypeNew = customerAccount.getDiscountPlantype();
            List<Job> jobListOld = persistentCustomerAccount.getJobList();
            List<Job> jobListNew = customerAccount.getJobList();
            List<String> illegalOrphanMessages = null;
            for (Job jobListOldJob : jobListOld) {
                if (!jobListNew.contains(jobListOldJob)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Job " + jobListOldJob + " since its customerAccount field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (addressNew != null) {
                addressNew = em.getReference(addressNew.getClass(), addressNew.getAddressPK());
                customerAccount.setAddress(addressNew);
            }
            if (discountPlantypeNew != null) {
                discountPlantypeNew = em.getReference(discountPlantypeNew.getClass(), discountPlantypeNew.getType());
                customerAccount.setDiscountPlantype(discountPlantypeNew);
            }
            List<Job> attachedJobListNew = new ArrayList<Job>();
            for (Job jobListNewJobToAttach : jobListNew) {
                jobListNewJobToAttach = em.getReference(jobListNewJobToAttach.getClass(), jobListNewJobToAttach.getJobPK());
                attachedJobListNew.add(jobListNewJobToAttach);
            }
            jobListNew = attachedJobListNew;
            customerAccount.setJobList(jobListNew);
            customerAccount = em.merge(customerAccount);
            if (addressOld != null && !addressOld.equals(addressNew)) {
                addressOld.getCustomerAccountList().remove(customerAccount);
                addressOld = em.merge(addressOld);
            }
            if (addressNew != null && !addressNew.equals(addressOld)) {
                addressNew.getCustomerAccountList().add(customerAccount);
                addressNew = em.merge(addressNew);
            }
            if (discountPlantypeOld != null && !discountPlantypeOld.equals(discountPlantypeNew)) {
                discountPlantypeOld.getCustomerAccountList().remove(customerAccount);
                discountPlantypeOld = em.merge(discountPlantypeOld);
            }
            if (discountPlantypeNew != null && !discountPlantypeNew.equals(discountPlantypeOld)) {
                discountPlantypeNew.getCustomerAccountList().add(customerAccount);
                discountPlantypeNew = em.merge(discountPlantypeNew);
            }
            for (Job jobListNewJob : jobListNew) {
                if (!jobListOld.contains(jobListNewJob)) {
                    CustomerAccount oldCustomerAccountOfJobListNewJob = jobListNewJob.getCustomerAccount();
                    jobListNewJob.setCustomerAccount(customerAccount);
                    jobListNewJob = em.merge(jobListNewJob);
                    if (oldCustomerAccountOfJobListNewJob != null && !oldCustomerAccountOfJobListNewJob.equals(customerAccount)) {
                        oldCustomerAccountOfJobListNewJob.getJobList().remove(jobListNewJob);
                        oldCustomerAccountOfJobListNewJob = em.merge(oldCustomerAccountOfJobListNewJob);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CustomerAccountPK id = customerAccount.getCustomerAccountPK();
                if (findCustomerAccount(id) == null) {
                    throw new NonexistentEntityException("The customerAccount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CustomerAccountPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount customerAccount;
            try {
                customerAccount = em.getReference(CustomerAccount.class, id);
                customerAccount.getCustomerAccountPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customerAccount with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Job> jobListOrphanCheck = customerAccount.getJobList();
            for (Job jobListOrphanCheckJob : jobListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerAccount (" + customerAccount + ") cannot be destroyed since the Job " + jobListOrphanCheckJob + " in its jobList field has a non-nullable customerAccount field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Address address = customerAccount.getAddress();
            if (address != null) {
                address.getCustomerAccountList().remove(customerAccount);
                address = em.merge(address);
            }
            DiscountPlan discountPlantype = customerAccount.getDiscountPlantype();
            if (discountPlantype != null) {
                discountPlantype.getCustomerAccountList().remove(customerAccount);
                discountPlantype = em.merge(discountPlantype);
            }
            em.remove(customerAccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CustomerAccount> findCustomerAccountEntities() {
        return findCustomerAccountEntities(true, -1, -1);
    }

    public List<CustomerAccount> findCustomerAccountEntities(int maxResults, int firstResult) {
        return findCustomerAccountEntities(false, maxResults, firstResult);
    }

    private List<CustomerAccount> findCustomerAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CustomerAccount.class));
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

    public CustomerAccount findCustomerAccount(CustomerAccountPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CustomerAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CustomerAccount> rt = cq.from(CustomerAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
