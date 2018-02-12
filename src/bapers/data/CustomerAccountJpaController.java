/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.data;

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
        customerAccount.getCustomerAccountPK().setFkAddressLine1(customerAccount.getAddress().getAddressPK().getAddressLine1());
        customerAccount.getCustomerAccountPK().setFkPostcode(customerAccount.getAddress().getAddressPK().getPostcode());
        customerAccount.getCustomerAccountPK().setFkCity(customerAccount.getAddress().getAddressPK().getCity());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address = customerAccount.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getAddressPK());
                customerAccount.setAddress(address);
            }
            em.persist(customerAccount);
            if (address != null) {
                address.getCustomerAccountList().add(customerAccount);
                address = em.merge(address);
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

    public void edit(CustomerAccount customerAccount) throws NonexistentEntityException, Exception {
        customerAccount.getCustomerAccountPK().setFkAddressLine1(customerAccount.getAddress().getAddressPK().getAddressLine1());
        customerAccount.getCustomerAccountPK().setFkPostcode(customerAccount.getAddress().getAddressPK().getPostcode());
        customerAccount.getCustomerAccountPK().setFkCity(customerAccount.getAddress().getAddressPK().getCity());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CustomerAccount persistentCustomerAccount = em.find(CustomerAccount.class, customerAccount.getCustomerAccountPK());
            Address addressOld = persistentCustomerAccount.getAddress();
            Address addressNew = customerAccount.getAddress();
            if (addressNew != null) {
                addressNew = em.getReference(addressNew.getClass(), addressNew.getAddressPK());
                customerAccount.setAddress(addressNew);
            }
            customerAccount = em.merge(customerAccount);
            if (addressOld != null && !addressOld.equals(addressNew)) {
                addressOld.getCustomerAccountList().remove(customerAccount);
                addressOld = em.merge(addressOld);
            }
            if (addressNew != null && !addressNew.equals(addressOld)) {
                addressNew.getCustomerAccountList().add(customerAccount);
                addressNew = em.merge(addressNew);
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

    public void destroy(CustomerAccountPK id) throws NonexistentEntityException {
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
            Address address = customerAccount.getAddress();
            if (address != null) {
                address.getCustomerAccountList().remove(customerAccount);
                address = em.merge(address);
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
