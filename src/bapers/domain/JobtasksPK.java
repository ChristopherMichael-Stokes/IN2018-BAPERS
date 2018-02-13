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
public class JobtasksPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "Job_job_id")
    private int jobjobid;
    @Basic(optional = false)
    @Column(name = "Job_Customer_Account_customer_id")
    private String jobCustomerAccountcustomerid;
    @Basic(optional = false)
    @Column(name = "Job_fk_email")
    private String jobfkemail;
    @Basic(optional = false)
    @Column(name = "Task_task_id")
    private int tasktaskid;

    public JobtasksPK() {
    }

    public JobtasksPK(int jobjobid, String jobCustomerAccountcustomerid, String jobfkemail, int tasktaskid) {
        this.jobjobid = jobjobid;
        this.jobCustomerAccountcustomerid = jobCustomerAccountcustomerid;
        this.jobfkemail = jobfkemail;
        this.tasktaskid = tasktaskid;
    }

    public int getJobjobid() {
        return jobjobid;
    }

    public void setJobjobid(int jobjobid) {
        this.jobjobid = jobjobid;
    }

    public String getJobCustomerAccountcustomerid() {
        return jobCustomerAccountcustomerid;
    }

    public void setJobCustomerAccountcustomerid(String jobCustomerAccountcustomerid) {
        this.jobCustomerAccountcustomerid = jobCustomerAccountcustomerid;
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
        hash += (jobCustomerAccountcustomerid != null ? jobCustomerAccountcustomerid.hashCode() : 0);
        hash += (jobfkemail != null ? jobfkemail.hashCode() : 0);
        hash += (int) tasktaskid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobtasksPK)) {
            return false;
        }
        JobtasksPK other = (JobtasksPK) object;
        if (this.jobjobid != other.jobjobid) {
            return false;
        }
        if ((this.jobCustomerAccountcustomerid == null && other.jobCustomerAccountcustomerid != null) || (this.jobCustomerAccountcustomerid != null && !this.jobCustomerAccountcustomerid.equals(other.jobCustomerAccountcustomerid))) {
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
        return "bapers.domain.JobtasksPK[ jobjobid=" + jobjobid + ", jobCustomerAccountcustomerid=" + jobCustomerAccountcustomerid + ", jobfkemail=" + jobfkemail + ", tasktaskid=" + tasktaskid + " ]";
    }
    
}
