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
package bapers.data.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author chris
 */
@Entity
@Table(name = "job_component")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobComponent.findAll", query = "SELECT j FROM JobComponent j")
    , @NamedQuery(name = "JobComponent.findByFkJobId", query = "SELECT j FROM JobComponent j WHERE j.jobComponentPK.fkJobId = :fkJobId")
    , @NamedQuery(name = "JobComponent.findByComponentId", query = "SELECT j FROM JobComponent j WHERE j.jobComponentPK.componentId = :componentId")
    , @NamedQuery(name = "JobComponent.findByDescription", query = "SELECT j FROM JobComponent j WHERE j.description = :description")
    , @NamedQuery(name = "JobComponent.findByStartTime", query = "SELECT j FROM JobComponent j WHERE j.startTime = :startTime")
    , @NamedQuery(name = "JobComponent.findByEndTime", query = "SELECT j FROM JobComponent j WHERE j.endTime = :endTime")})
public class JobComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JobComponentPK jobComponentPK;
    @Column(name = "description")
    private String description;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinTable(name = "component_task", joinColumns = {
        @JoinColumn(name = "fk_job_id", referencedColumnName = "fk_job_id")
        , @JoinColumn(name = "fk_component_id", referencedColumnName = "component_id")}, inverseJoinColumns = {
        @JoinColumn(name = "fk_task_id", referencedColumnName = "task_id")})
    @ManyToMany
    private List<Task> taskList;
    @JoinColumn(name = "fk_job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Job job;
    @JoinColumn(name = "fk_username", referencedColumnName = "username")
    @ManyToOne
    private User fkUsername;

    public JobComponent() {
    }

    public JobComponent(JobComponentPK jobComponentPK) {
        this.jobComponentPK = jobComponentPK;
    }

    public JobComponent(int fkJobId, int componentId) {
        this.jobComponentPK = new JobComponentPK(fkJobId, componentId);
    }

    public JobComponentPK getJobComponentPK() {
        return jobComponentPK;
    }

    public void setJobComponentPK(JobComponentPK jobComponentPK) {
        this.jobComponentPK = jobComponentPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @XmlTransient
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getFkUsername() {
        return fkUsername;
    }

    public void setFkUsername(User fkUsername) {
        this.fkUsername = fkUsername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobComponentPK != null ? jobComponentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobComponent)) {
            return false;
        }
        JobComponent other = (JobComponent) object;
        if ((this.jobComponentPK == null && other.jobComponentPK != null) || (this.jobComponentPK != null && !this.jobComponentPK.equals(other.jobComponentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.JobComponent[ jobComponentPK=" + jobComponentPK + " ]";
    }
    
}
