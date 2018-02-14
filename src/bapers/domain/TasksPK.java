/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author chris
 */
@Embeddable
public class TasksPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "Job_job_id")
    private int jobjobid;
    @Basic(optional = false)
    @Column(name = "Job_fk_payment_id")
    private int jobfkpaymentid;
    @Basic(optional = false)
    @Column(name = "Job_fk_customer_id")
    private String jobfkcustomerid;
    @Basic(optional = false)
    @Column(name = "Job_fk_email")
    private String jobfkemail;
    @Basic(optional = false)
    @Column(name = "Task_task_id")
    private int tasktaskid;

    public TasksPK() {
    }

    public TasksPK(int jobjobid, int jobfkpaymentid, String jobfkcustomerid, String jobfkemail, int tasktaskid) {
        this.jobjobid = jobjobid;
        this.jobfkpaymentid = jobfkpaymentid;
        this.jobfkcustomerid = jobfkcustomerid;
        this.jobfkemail = jobfkemail;
        this.tasktaskid = tasktaskid;
    }

    public int getJobjobid() {
        return jobjobid;
    }

    public void setJobjobid(int jobjobid) {
        this.jobjobid = jobjobid;
    }

    public int getJobfkpaymentid() {
        return jobfkpaymentid;
    }

    public void setJobfkpaymentid(int jobfkpaymentid) {
        this.jobfkpaymentid = jobfkpaymentid;
    }

    public String getJobfkcustomerid() {
        return jobfkcustomerid;
    }

    public void setJobfkcustomerid(String jobfkcustomerid) {
        this.jobfkcustomerid = jobfkcustomerid;
    }

    public String getJobfkemail() {
        return jobfkemail;
    }

    public void setJobfkemail(String jobfkemail) {
        this.jobfkemail = jobfkemail;
    }

    public int getTasktaskid() {
        return tasktaskid;
    }

    public void setTasktaskid(int tasktaskid) {
        this.tasktaskid = tasktaskid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) jobjobid;
        hash += (int) jobfkpaymentid;
        hash += (jobfkcustomerid != null ? jobfkcustomerid.hashCode() : 0);
        hash += (jobfkemail != null ? jobfkemail.hashCode() : 0);
        hash += (int) tasktaskid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TasksPK)) {
            return false;
        }
        TasksPK other = (TasksPK) object;
        if (this.jobjobid != other.jobjobid) {
            return false;
        }
        if (this.jobfkpaymentid != other.jobfkpaymentid) {
            return false;
        }
        if ((this.jobfkcustomerid == null && other.jobfkcustomerid != null) || (this.jobfkcustomerid != null && !this.jobfkcustomerid.equals(other.jobfkcustomerid))) {
            return false;
        }
        if ((this.jobfkemail == null && other.jobfkemail != null) || (this.jobfkemail != null && !this.jobfkemail.equals(other.jobfkemail))) {
            return false;
        }
        if (this.tasktaskid != other.tasktaskid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.TasksPK[ jobjobid=" + jobjobid + ", jobfkpaymentid=" + jobfkpaymentid + ", jobfkcustomerid=" + jobfkcustomerid + ", jobfkemail=" + jobfkemail + ", tasktaskid=" + tasktaskid + " ]";
    }
    
}
