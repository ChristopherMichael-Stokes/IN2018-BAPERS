/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    , @NamedQuery(name = "Task.findByLocation", query = "SELECT t FROM Task t WHERE t.location = :location")
    , @NamedQuery(name = "Task.findByShelfSlot", query = "SELECT t FROM Task t WHERE t.shelfSlot = :shelfSlot")
    , @NamedQuery(name = "Task.findByPrice", query = "SELECT t FROM Task t WHERE t.price = :price")
    , @NamedQuery(name = "Task.findByDuration", query = "SELECT t FROM Task t WHERE t.duration = :duration")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "task_id")
    private Integer taskId;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "location")
    private String location;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Jobtasks> jobtasksList;

    public Task() {
    }

    public Task(Integer taskId) {
        this.taskId = taskId;
    }

    public Task(Integer taskId, String description, String location, String shelfSlot, int price, Date duration) {
        this.taskId = taskId;
        this.description = description;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    public List<Jobtasks> getJobtasksList() {
        return jobtasksList;
    }

    public void setJobtasksList(List<Jobtasks> jobtasksList) {
        this.jobtasksList = jobtasksList;
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
        return "bapers.domain.Task[ taskId=" + taskId + " ]";
    }
    
}
