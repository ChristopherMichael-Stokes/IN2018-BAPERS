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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "component_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponentTask.findAll", query = "SELECT c FROM ComponentTask c")
    , @NamedQuery(name = "ComponentTask.findByFkJobId", query = "SELECT c FROM ComponentTask c WHERE c.componentTaskPK.fkJobId = :fkJobId")
    , @NamedQuery(name = "ComponentTask.findByFkComponentId", query = "SELECT c FROM ComponentTask c WHERE c.componentTaskPK.fkComponentId = :fkComponentId")
    , @NamedQuery(name = "ComponentTask.findByFkTaskId", query = "SELECT c FROM ComponentTask c WHERE c.componentTaskPK.fkTaskId = :fkTaskId")
    , @NamedQuery(name = "ComponentTask.findByStartTime", query = "SELECT c FROM ComponentTask c WHERE c.startTime = :startTime")
    , @NamedQuery(name = "ComponentTask.findByEndTime", query = "SELECT c FROM ComponentTask c WHERE c.endTime = :endTime")})
public class ComponentTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComponentTaskPK componentTaskPK;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinColumn(name = "fk_username", referencedColumnName = "username")
    @ManyToOne(fetch = FetchType.EAGER)
    private User fkUsername;
    @JoinColumns({
        @JoinColumn(name = "fk_job_id", referencedColumnName = "fk_job_id", insertable = false, updatable = false)
        , @JoinColumn(name = "fk_component_id", referencedColumnName = "component_id", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private JobComponent jobComponent;
    @JoinColumn(name = "fk_task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Task task;

    public ComponentTask() {
    }

    public ComponentTask(ComponentTaskPK componentTaskPK) {
        this.componentTaskPK = componentTaskPK;
    }

    public ComponentTask(int fkJobId, String fkComponentId, int fkTaskId) {
        this.componentTaskPK = new ComponentTaskPK(fkJobId, fkComponentId, fkTaskId);
    }

    public ComponentTaskPK getComponentTaskPK() {
        return componentTaskPK;
    }

    public void setComponentTaskPK(ComponentTaskPK componentTaskPK) {
        this.componentTaskPK = componentTaskPK;
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

    public User getFkUsername() {
        return fkUsername;
    }

    public void setFkUsername(User fkUsername) {
        this.fkUsername = fkUsername;
    }

    public JobComponent getJobComponent() {
        return jobComponent;
    }

    public void setJobComponent(JobComponent jobComponent) {
        this.jobComponent = jobComponent;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentTaskPK != null ? componentTaskPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentTask)) {
            return false;
        }
        ComponentTask other = (ComponentTask) object;
        if ((this.componentTaskPK == null && other.componentTaskPK != null) || (this.componentTaskPK != null && !this.componentTaskPK.equals(other.componentTaskPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.ComponentTask[ componentTaskPK=" + componentTaskPK + " ]";
    }
    
}
