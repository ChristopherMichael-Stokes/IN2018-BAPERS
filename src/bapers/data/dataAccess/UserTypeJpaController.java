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
import bapers.data.domain.Staff;
import bapers.data.domain.UserType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class UserTypeJpaController implements Serializable {

    public UserTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserType userType) throws PreexistingEntityException, Exception {
        if (userType.getStaffList() == null) {
            userType.setStaffList(new ArrayList<Staff>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Staff> attachedStaffList = new ArrayList<Staff>();
            for (Staff staffListStaffToAttach : userType.getStaffList()) {
                staffListStaffToAttach = em.getReference(staffListStaffToAttach.getClass(), staffListStaffToAttach.getStaffId());
                attachedStaffList.add(staffListStaffToAttach);
            }
            userType.setStaffList(attachedStaffList);
            em.persist(userType);
            for (Staff staffListStaff : userType.getStaffList()) {
                UserType oldFkTypeOfStaffListStaff = staffListStaff.getFkType();
                staffListStaff.setFkType(userType);
                staffListStaff = em.merge(staffListStaff);
                if (oldFkTypeOfStaffListStaff != null) {
                    oldFkTypeOfStaffListStaff.getStaffList().remove(staffListStaff);
                    oldFkTypeOfStaffListStaff = em.merge(oldFkTypeOfStaffListStaff);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserType(userType.getType()) != null) {
                throw new PreexistingEntityException("UserType " + userType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserType userType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserType persistentUserType = em.find(UserType.class, userType.getType());
            List<Staff> staffListOld = persistentUserType.getStaffList();
            List<Staff> staffListNew = userType.getStaffList();
            List<String> illegalOrphanMessages = null;
            for (Staff staffListOldStaff : staffListOld) {
                if (!staffListNew.contains(staffListOldStaff)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Staff " + staffListOldStaff + " since its fkType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Staff> attachedStaffListNew = new ArrayList<Staff>();
            for (Staff staffListNewStaffToAttach : staffListNew) {
                staffListNewStaffToAttach = em.getReference(staffListNewStaffToAttach.getClass(), staffListNewStaffToAttach.getStaffId());
                attachedStaffListNew.add(staffListNewStaffToAttach);
            }
            staffListNew = attachedStaffListNew;
            userType.setStaffList(staffListNew);
            userType = em.merge(userType);
            for (Staff staffListNewStaff : staffListNew) {
                if (!staffListOld.contains(staffListNewStaff)) {
                    UserType oldFkTypeOfStaffListNewStaff = staffListNewStaff.getFkType();
                    staffListNewStaff.setFkType(userType);
                    staffListNewStaff = em.merge(staffListNewStaff);
                    if (oldFkTypeOfStaffListNewStaff != null && !oldFkTypeOfStaffListNewStaff.equals(userType)) {
                        oldFkTypeOfStaffListNewStaff.getStaffList().remove(staffListNewStaff);
                        oldFkTypeOfStaffListNewStaff = em.merge(oldFkTypeOfStaffListNewStaff);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = userType.getType();
                if (findUserType(id) == null) {
                    throw new NonexistentEntityException("The userType with id " + id + " no longer exists.");
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
            UserType userType;
            try {
                userType = em.getReference(UserType.class, id);
                userType.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Staff> staffListOrphanCheck = userType.getStaffList();
            for (Staff staffListOrphanCheckStaff : staffListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserType (" + userType + ") cannot be destroyed since the Staff " + staffListOrphanCheckStaff + " in its staffList field has a non-nullable fkType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(userType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserType> findUserTypeEntities() {
        return findUserTypeEntities(true, -1, -1);
    }

    public List<UserType> findUserTypeEntities(int maxResults, int firstResult) {
        return findUserTypeEntities(false, maxResults, firstResult);
    }

    private List<UserType> findUserTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserType.class));
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

    public UserType findUserType(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserType.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserType> rt = cq.from(UserType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
