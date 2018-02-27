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
package bapers.data;

import bapers.data.exceptions.IllegalOrphanException;
import bapers.data.exceptions.NonexistentEntityException;
import bapers.data.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bapers.domain.UserType;
import bapers.domain.JobTask;
import bapers.domain.Staff;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author chris
 */
public class StaffJpaController implements Serializable {

    public StaffJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Staff staff) throws PreexistingEntityException, Exception {
        if (staff.getJobTaskList() == null) {
            staff.setJobTaskList(new ArrayList<JobTask>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserType fkType = staff.getFkType();
            if (fkType != null) {
                fkType = em.getReference(fkType.getClass(), fkType.getType());
                staff.setFkType(fkType);
            }
            List<JobTask> attachedJobTaskList = new ArrayList<JobTask>();
            for (JobTask jobTaskListJobTaskToAttach : staff.getJobTaskList()) {
                jobTaskListJobTaskToAttach = em.getReference(jobTaskListJobTaskToAttach.getClass(), jobTaskListJobTaskToAttach.getJobTaskPK());
                attachedJobTaskList.add(jobTaskListJobTaskToAttach);
            }
            staff.setJobTaskList(attachedJobTaskList);
            em.persist(staff);
            if (fkType != null) {
                fkType.getStaffList().add(staff);
                fkType = em.merge(fkType);
            }
            for (JobTask jobTaskListJobTask : staff.getJobTaskList()) {
                Staff oldFkStaffIdOfJobTaskListJobTask = jobTaskListJobTask.getFkStaffId();
                jobTaskListJobTask.setFkStaffId(staff);
                jobTaskListJobTask = em.merge(jobTaskListJobTask);
                if (oldFkStaffIdOfJobTaskListJobTask != null) {
                    oldFkStaffIdOfJobTaskListJobTask.getJobTaskList().remove(jobTaskListJobTask);
                    oldFkStaffIdOfJobTaskListJobTask = em.merge(oldFkStaffIdOfJobTaskListJobTask);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStaff(staff.getStaffId()) != null) {
                throw new PreexistingEntityException("Staff " + staff + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Staff persistentStaff = em.find(Staff.class, staff.getStaffId());
            UserType fkTypeOld = persistentStaff.getFkType();
            UserType fkTypeNew = staff.getFkType();
            List<JobTask> jobTaskListOld = persistentStaff.getJobTaskList();
            List<JobTask> jobTaskListNew = staff.getJobTaskList();
            List<String> illegalOrphanMessages = null;
            for (JobTask jobTaskListOldJobTask : jobTaskListOld) {
                if (!jobTaskListNew.contains(jobTaskListOldJobTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JobTask " + jobTaskListOldJobTask + " since its fkStaffId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkTypeNew != null) {
                fkTypeNew = em.getReference(fkTypeNew.getClass(), fkTypeNew.getType());
                staff.setFkType(fkTypeNew);
            }
            List<JobTask> attachedJobTaskListNew = new ArrayList<JobTask>();
            for (JobTask jobTaskListNewJobTaskToAttach : jobTaskListNew) {
                jobTaskListNewJobTaskToAttach = em.getReference(jobTaskListNewJobTaskToAttach.getClass(), jobTaskListNewJobTaskToAttach.getJobTaskPK());
                attachedJobTaskListNew.add(jobTaskListNewJobTaskToAttach);
            }
            jobTaskListNew = attachedJobTaskListNew;
            staff.setJobTaskList(jobTaskListNew);
            staff = em.merge(staff);
            if (fkTypeOld != null && !fkTypeOld.equals(fkTypeNew)) {
                fkTypeOld.getStaffList().remove(staff);
                fkTypeOld = em.merge(fkTypeOld);
            }
            if (fkTypeNew != null && !fkTypeNew.equals(fkTypeOld)) {
                fkTypeNew.getStaffList().add(staff);
                fkTypeNew = em.merge(fkTypeNew);
            }
            for (JobTask jobTaskListNewJobTask : jobTaskListNew) {
                if (!jobTaskListOld.contains(jobTaskListNewJobTask)) {
                    Staff oldFkStaffIdOfJobTaskListNewJobTask = jobTaskListNewJobTask.getFkStaffId();
                    jobTaskListNewJobTask.setFkStaffId(staff);
                    jobTaskListNewJobTask = em.merge(jobTaskListNewJobTask);
                    if (oldFkStaffIdOfJobTaskListNewJobTask != null && !oldFkStaffIdOfJobTaskListNewJobTask.equals(staff)) {
                        oldFkStaffIdOfJobTaskListNewJobTask.getJobTaskList().remove(jobTaskListNewJobTask);
                        oldFkStaffIdOfJobTaskListNewJobTask = em.merge(oldFkStaffIdOfJobTaskListNewJobTask);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = staff.getStaffId();
                if (findStaff(id) == null) {
                    throw new NonexistentEntityException("The staff with id " + id + " no longer exists.");
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
            Staff staff;
            try {
                staff = em.getReference(Staff.class, id);
                staff.getStaffId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The staff with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<JobTask> jobTaskListOrphanCheck = staff.getJobTaskList();
            for (JobTask jobTaskListOrphanCheckJobTask : jobTaskListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Staff (" + staff + ") cannot be destroyed since the JobTask " + jobTaskListOrphanCheckJobTask + " in its jobTaskList field has a non-nullable fkStaffId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserType fkType = staff.getFkType();
            if (fkType != null) {
                fkType.getStaffList().remove(staff);
                fkType = em.merge(fkType);
            }
            em.remove(staff);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Staff> findStaffEntities() {
        return findStaffEntities(true, -1, -1);
    }

    public List<Staff> findStaffEntities(int maxResults, int firstResult) {
        return findStaffEntities(false, maxResults, firstResult);
    }

    private List<Staff> findStaffEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Staff.class));
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

    public Staff findStaff(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Staff.class, id);
        } finally {
            em.close();
        }
    }

    public int getStaffCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Staff> rt = cq.from(Staff.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
