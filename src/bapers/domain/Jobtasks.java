/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.domain;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "jobtasks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jobtasks.findAll", query = "SELECT j FROM Jobtasks j")
    , @NamedQuery(name = "Jobtasks.findByJobjobid", query = "SELECT j FROM Jobtasks j WHERE j.jobtasksPK.jobjobid = :jobjobid")
    , @NamedQuery(name = "Jobtasks.findByJobCustomerAccountcustomerid", query = "SELECT j FROM Jobtasks j WHERE j.jobtasksPK.jobCustomerAccountcustomerid = :jobCustomerAccountcustomerid")
    , @NamedQuery(name = "Jobtasks.findByJobfkemail", query = "SELECT j FROM Jobtasks j WHERE j.jobtasksPK.jobfkemail = :jobfkemail")
    , @NamedQuery(name = "Jobtasks.findByTasktaskid", query = "SELECT j FROM Jobtasks j WHERE j.jobtasksPK.tasktaskid = :tasktaskid")
    , @NamedQuery(name = "Jobtasks.findByStartTime", query = "SELECT j FROM Jobtasks j WHERE j.startTime = :startTime")
    , @NamedQuery(name = "Jobtasks.findByEndTime", query = "SELECT j FROM Jobtasks j WHERE j.endTime = :endTime")
    , @NamedQuery(name = "Jobtasks.findByDiscount", query = "SELECT j FROM Jobtasks j WHERE j.discount = :discount")})
public class Jobtasks implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JobtasksPK jobtasksPK;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private Double discount;
    @JoinColumn(name = "User_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User useruserid;
    @JoinColumns({
        @JoinColumn(name = "Job_job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
        , @JoinColumn(name = "Job_Customer_Account_customer_id", referencedColumnName = "Customer_Account_customer_id", insertable = false, updatable = false)
        , @JoinColumn(name = "Job_fk_email", referencedColumnName = "fk_email", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Job job;
    @JoinColumn(name = "Task_task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;

    public Jobtasks() {
    }

    public Jobtasks(JobtasksPK jobtasksPK) {
        this.jobtasksPK = jobtasksPK;
    }

    public Jobtasks(int jobjobid, String jobCustomerAccountcustomerid, String jobfkemail, int tasktaskid) {
        this.jobtasksPK = new JobtasksPK(jobjobid, jobCustomerAccountcustomerid, jobfkemail, tasktaskid);
    }

    public JobtasksPK getJobtasksPK() {
        return jobtasksPK;
    }

    public void setJobtasksPK(JobtasksPK jobtasksPK) {
        this.jobtasksPK = jobtasksPK;
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public User getUseruserid() {
        return useruserid;
    }

    public void setUseruserid(User useruserid) {
        this.useruserid = useruserid;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobtasksPK != null ? jobtasksPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jobtasks)) {
            return false;
        }
        Jobtasks other = (Jobtasks) object;
        if ((this.jobtasksPK == null && other.jobtasksPK != null) || (this.jobtasksPK != null && !this.jobtasksPK.equals(other.jobtasksPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.Jobtasks[ jobtasksPK=" + jobtasksPK + " ]";
    }
    
}
