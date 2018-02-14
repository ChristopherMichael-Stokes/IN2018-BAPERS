/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tasks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tasks.findAll", query = "SELECT t FROM Tasks t")
    , @NamedQuery(name = "Tasks.findByJobjobid", query = "SELECT t FROM Tasks t WHERE t.tasksPK.jobjobid = :jobjobid")
    , @NamedQuery(name = "Tasks.findByJobfkpaymentid", query = "SELECT t FROM Tasks t WHERE t.tasksPK.jobfkpaymentid = :jobfkpaymentid")
    , @NamedQuery(name = "Tasks.findByJobfkcustomerid", query = "SELECT t FROM Tasks t WHERE t.tasksPK.jobfkcustomerid = :jobfkcustomerid")
    , @NamedQuery(name = "Tasks.findByJobfkemail", query = "SELECT t FROM Tasks t WHERE t.tasksPK.jobfkemail = :jobfkemail")
    , @NamedQuery(name = "Tasks.findByTasktaskid", query = "SELECT t FROM Tasks t WHERE t.tasksPK.tasktaskid = :tasktaskid")
    , @NamedQuery(name = "Tasks.findByStartTime", query = "SELECT t FROM Tasks t WHERE t.startTime = :startTime")
    , @NamedQuery(name = "Tasks.findByEndTime", query = "SELECT t FROM Tasks t WHERE t.endTime = :endTime")
    , @NamedQuery(name = "Tasks.findByDiscount", query = "SELECT t FROM Tasks t WHERE t.discount = :discount")})
public class Tasks implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TasksPK tasksPK;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Basic(optional = false)
    @Column(name = "discount")
    private double discount;
    @JoinColumns({
        @JoinColumn(name = "Job_job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
        , @JoinColumn(name = "Job_fk_payment_id", referencedColumnName = "fk_payment_id", insertable = false, updatable = false)
        , @JoinColumn(name = "Job_fk_customer_id", referencedColumnName = "fk_customer_id", insertable = false, updatable = false)
        , @JoinColumn(name = "Job_fk_email", referencedColumnName = "fk_email", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Job job;
    @JoinColumn(name = "Task_task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;
    @JoinColumn(name = "User_username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private User userusername;

    public Tasks() {
    }

    public Tasks(TasksPK tasksPK) {
        this.tasksPK = tasksPK;
    }

    public Tasks(TasksPK tasksPK, Date startTime, Date endTime, double discount) {
        this.tasksPK = tasksPK;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discount = discount;
    }

    public Tasks(int jobjobid, int jobfkpaymentid, String jobfkcustomerid, String jobfkemail, int tasktaskid) {
        this.tasksPK = new TasksPK(jobjobid, jobfkpaymentid, jobfkcustomerid, jobfkemail, tasktaskid);
    }

    public TasksPK getTasksPK() {
        return tasksPK;
    }

    public void setTasksPK(TasksPK tasksPK) {
        this.tasksPK = tasksPK;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public User getUserusername() {
        return userusername;
    }

    public void setUserusername(User userusername) {
        this.userusername = userusername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tasksPK != null ? tasksPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tasks)) {
            return false;
        }
        Tasks other = (Tasks) object;
        if ((this.tasksPK == null && other.tasksPK != null) || (this.tasksPK != null && !this.tasksPK.equals(other.tasksPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.Tasks[ tasksPK=" + tasksPK + " ]";
    }
    
}
