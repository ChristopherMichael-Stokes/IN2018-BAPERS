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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "job")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j")
    , @NamedQuery(name = "Job.findByJobId", query = "SELECT j FROM Job j WHERE j.jobPK.jobId = :jobId")
    , @NamedQuery(name = "Job.findByAmountDue", query = "SELECT j FROM Job j WHERE j.amountDue = :amountDue")
    , @NamedQuery(name = "Job.findByDeadline", query = "SELECT j FROM Job j WHERE j.deadline = :deadline")
    , @NamedQuery(name = "Job.findByStartTime", query = "SELECT j FROM Job j WHERE j.startTime = :startTime")
    , @NamedQuery(name = "Job.findByEndTime", query = "SELECT j FROM Job j WHERE j.endTime = :endTime")
    , @NamedQuery(name = "Job.findByUrgent", query = "SELECT j FROM Job j WHERE j.urgent = :urgent")
    , @NamedQuery(name = "Job.findByFkCustomerId", query = "SELECT j FROM Job j WHERE j.jobPK.fkCustomerId = :fkCustomerId")
    , @NamedQuery(name = "Job.findByFkEmail", query = "SELECT j FROM Job j WHERE j.jobPK.fkEmail = :fkEmail")
    , @NamedQuery(name = "Job.findByFkPaymentId", query = "SELECT j FROM Job j WHERE j.jobPK.fkPaymentId = :fkPaymentId")})
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JobPK jobPK;
    @Basic(optional = false)
    @Column(name = "amount_due")
    private int amountDue;
    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Basic(optional = false)
    @Column(name = "urgent")
    private boolean urgent;
    @JoinColumns({
        @JoinColumn(name = "fk_customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
        , @JoinColumn(name = "fk_email", referencedColumnName = "email", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private CustomerAccount customerAccount;
    @JoinColumn(name = "fk_payment_id", referencedColumnName = "payment_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PaymentInfo paymentInfo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private List<Tasks> tasksList;

    public Job() {
    }

    public Job(JobPK jobPK) {
        this.jobPK = jobPK;
    }

    public Job(JobPK jobPK, int amountDue, Date startTime, boolean urgent) {
        this.jobPK = jobPK;
        this.amountDue = amountDue;
        this.startTime = startTime;
        this.urgent = urgent;
    }

    public Job(int jobId, String fkCustomerId, String fkEmail, int fkPaymentId) {
        this.jobPK = new JobPK(jobId, fkCustomerId, fkEmail, fkPaymentId);
    }

    public JobPK getJobPK() {
        return jobPK;
    }

    public void setJobPK(JobPK jobPK) {
        this.jobPK = jobPK;
    }

    public int getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(int amountDue) {
        this.amountDue = amountDue;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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

    public boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @XmlTransient
    public List<Tasks> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Tasks> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobPK != null ? jobPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Job)) {
            return false;
        }
        Job other = (Job) object;
        if ((this.jobPK == null && other.jobPK != null) || (this.jobPK != null && !this.jobPK.equals(other.jobPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.Job[ jobPK=" + jobPK + " ]";
    }
    
}
