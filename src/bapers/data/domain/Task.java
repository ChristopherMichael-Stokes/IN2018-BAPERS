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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
    , @NamedQuery(name = "Task.findByTaskId", query = "SELECT t FROM Task t WHERE t.taskId = :taskId")
    , @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description")
    , @NamedQuery(name = "Task.findByShelfSlot", query = "SELECT t FROM Task t WHERE t.shelfSlot = :shelfSlot")
    , @NamedQuery(name = "Task.findByPrice", query = "SELECT t FROM Task t WHERE t.price = :price")
    , @NamedQuery(name = "Task.findByDuration", query = "SELECT t FROM Task t WHERE t.duration = :duration")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "task_id")
    private Integer taskId;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "shelf_slot")
    private String shelfSlot;
    @Basic(optional = false)
    @Column(name = "price")
    private int price;
    @Basic(optional = false)
    @Column(name = "duration")
    @Temporal(TemporalType.TIME)
    private Date duration;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", fetch = FetchType.EAGER)
    private List<ComponentTask> componentTaskList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", fetch = FetchType.EAGER)
    private List<TaskDiscount> taskDiscountList;
    @JoinColumn(name = "fk_location", referencedColumnName = "location")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Location fkLocation;

    public Task() {
    }

    public Task(Integer taskId) {
        this.taskId = taskId;
    }

    public Task(Integer taskId, String description, String shelfSlot, int price, Date duration) {
        this.taskId = taskId;
        this.description = description;
        this.shelfSlot = shelfSlot;
        this.price = price;
        this.duration = duration;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShelfSlot() {
        return shelfSlot;
    }

    public void setShelfSlot(String shelfSlot) {
        this.shelfSlot = shelfSlot;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    @XmlTransient
    public List<ComponentTask> getComponentTaskList() {
        return componentTaskList;
    }

    public void setComponentTaskList(List<ComponentTask> componentTaskList) {
        this.componentTaskList = componentTaskList;
    }

    @XmlTransient
    public List<TaskDiscount> getTaskDiscountList() {
        return taskDiscountList;
    }

    public void setTaskDiscountList(List<TaskDiscount> taskDiscountList) {
        this.taskDiscountList = taskDiscountList;
    }

    public Location getFkLocation() {
        return fkLocation;
    }

    public void setFkLocation(Location fkLocation) {
        this.fkLocation = fkLocation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.Task[ taskId=" + taskId + " ]";
    }
    
}
