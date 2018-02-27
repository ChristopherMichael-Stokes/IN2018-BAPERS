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
package bapers.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author chris
 */
@Entity
@Table(name = "job_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobTask.findAll", query = "SELECT j FROM JobTask j")
    , @NamedQuery(name = "JobTask.findByFkJobId", query = "SELECT j FROM JobTask j WHERE j.jobTaskPK.fkJobId = :fkJobId")
    , @NamedQuery(name = "JobTask.findByFkTaskId", query = "SELECT j FROM JobTask j WHERE j.jobTaskPK.fkTaskId = :fkTaskId")
    , @NamedQuery(name = "JobTask.findByStartTime", query = "SELECT j FROM JobTask j WHERE j.startTime = :startTime")
    , @NamedQuery(name = "JobTask.findByEndTime", query = "SELECT j FROM JobTask j WHERE j.endTime = :endTime")})
public class JobTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JobTaskPK jobTaskPK;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinColumn(name = "fk_job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Job job;
    @JoinColumn(name = "fk_task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;
    @JoinColumn(name = "fk_staff_id", referencedColumnName = "staff_id")
    @ManyToOne(optional = false)
    private Staff fkStaffId;

    public JobTask() {
    }

    public JobTask(JobTaskPK jobTaskPK) {
        this.jobTaskPK = jobTaskPK;
    }

    public JobTask(JobTaskPK jobTaskPK, Date startTime) {
        this.jobTaskPK = jobTaskPK;
        this.startTime = startTime;
    }

    public JobTask(String fkJobId, int fkTaskId) {
        this.jobTaskPK = new JobTaskPK(fkJobId, fkTaskId);
    }

    public JobTaskPK getJobTaskPK() {
        return jobTaskPK;
    }

    public void setJobTaskPK(JobTaskPK jobTaskPK) {
        this.jobTaskPK = jobTaskPK;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Staff getFkStaffId() {
        return fkStaffId;
    }

    public void setFkStaffId(Staff fkStaffId) {
        this.fkStaffId = fkStaffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobTaskPK != null ? jobTaskPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobTask)) {
            return false;
        }
        JobTask other = (JobTask) object;
        if ((this.jobTaskPK == null && other.jobTaskPK != null) || (this.jobTaskPK != null && !this.jobTaskPK.equals(other.jobTaskPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.JobTask[ jobTaskPK=" + jobTaskPK + " ]";
    }
    
}
