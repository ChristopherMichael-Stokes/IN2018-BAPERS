/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.data;

import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import bapers.domain.Address;
import bapers.domain.AddressPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.CustomerAccount;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) throws PreexistingEntityException, Exception {
        if (address.getAddressPK() == null) {
            address.setAddressPK(new AddressPK());
        }
        if (address.getCustomerAccountList() == null) {
            address.setCustomerAccountList(new ArrayList<CustomerAccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CustomerAccount> attachedCustomerAccountList = new ArrayList<CustomerAccount>();
            for (CustomerAccount customerAccountListCustomerAccountToAttach : address.getCustomerAccountList()) {
                customerAccountListCustomerAccountToAttach = em.getReference(customerAccountListCustomerAccountToAttach.getClass(), customerAccountListCustomerAccountToAttach.getCustomerAccountPK());
                attachedCustomerAccountList.add(customerAccountListCustomerAccountToAttach);
            }
            address.setCustomerAccountList(attachedCustomerAccountList);
            em.persist(address);
            for (CustomerAccount customerAccountListCustomerAccount : address.getCustomerAccountList()) {
                Address oldAddressOfCustomerAccountListCustomerAccount = customerAccountListCustomerAccount.getAddress();
                customerAccountListCustomerAccount.setAddress(address);
                customerAccountListCustomerAccount = em.merge(customerAccountListCustomerAccount);
                if (oldAddressOfCustomerAccountListCustomerAccount != null) {
                    oldAddressOfCustomerAccountListCustomerAccount.getCustomerAccountList().remove(customerAccountListCustomerAccount);
                    oldAddressOfCustomerAccountListCustomerAccount = em.merge(oldAddressOfCustomerAccountListCustomerAccount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAddress(address.getAddressPK()) != null) {
                throw new PreexistingEntityException("Address " + address + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getAddressPK());
            List<CustomerAccount> customerAccountListOld = persistentAddress.getCustomerAccountList();
            List<CustomerAccount> customerAccountListNew = address.getCustomerAccountList();
            List<String> illegalOrphanMessages = null;
            for (CustomerAccount customerAccountListOldCustomerAccount : customerAccountListOld) {
                if (!customerAccountListNew.contains(customerAccountListOldCustomerAccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CustomerAccount " + customerAccountListOldCustomerAccount + " since its address field is not nullable.");
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
            address.setCustomerAccountList(customerAccountListNew);
            address = em.merge(address);
            for (CustomerAccount customerAccountListNewCustomerAccount : customerAccountListNew) {
                if (!customerAccountListOld.contains(customerAccountListNewCustomerAccount)) {
                    Address oldAddressOfCustomerAccountListNewCustomerAccount = customerAccountListNewCustomerAccount.getAddress();
                    customerAccountListNewCustomerAccount.setAddress(address);
                    customerAccountListNewCustomerAccount = em.merge(customerAccountListNewCustomerAccount);
                    if (oldAddressOfCustomerAccountListNewCustomerAccount != null && !oldAddressOfCustomerAccountListNewCustomerAccount.equals(address)) {
                        oldAddressOfCustomerAccountListNewCustomerAccount.getCustomerAccountList().remove(customerAccountListNewCustomerAccount);
                        oldAddressOfCustomerAccountListNewCustomerAccount = em.merge(oldAddressOfCustomerAccountListNewCustomerAccount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AddressPK id = address.getAddressPK();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AddressPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getAddressPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CustomerAccount> customerAccountListOrphanCheck = address.getCustomerAccountList();
            for (CustomerAccount customerAccountListOrphanCheckCustomerAccount : customerAccountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Address (" + address + ") cannot be destroyed since the CustomerAccount " + customerAccountListOrphanCheckCustomerAccount + " in its customerAccountList field has a non-nullable address field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(AddressPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
