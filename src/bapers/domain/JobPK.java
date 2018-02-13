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
public class JobPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "job_id")
    private int jobId;
    @Basic(optional = false)
    @Column(name = "Customer_Account_customer_id")
    private String customerAccountcustomerid;
    @Basic(optional = false)
    @Column(name = "fk_email")
    private String fkEmail;

    public JobPK() {
    }

    public JobPK(int jobId, String customerAccountcustomerid, String fkEmail) {
        this.jobId = jobId;
        this.customerAccountcustomerid = customerAccountcustomerid;
        this.fkEmail = fkEmail;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getCustomerAccountcustomerid() {
        return customerAccountcustomerid;
    }

    public void setCustomerAccountcustomerid(String customerAccountcustomerid) {
        this.customerAccountcustomerid = customerAccountcustomerid;
    }

    public String getFkEmail() {
        return fkEmail;
    }

    public void setFkEmail(String fkEmail) {
        this.fkEmail = fkEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) jobId;
        hash += (customerAccountcustomerid != null ? customerAccountcustomerid.hashCode() : 0);
        hash += (fkEmail != null ? fkEmail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobPK)) {
            return false;
        }
        JobPK other = (JobPK) object;
        if (this.jobId != other.jobId) {
            return false;
        }
        if ((this.customerAccountcustomerid == null && other.customerAccountcustomerid != null) || (this.customerAccountcustomerid != null && !this.customerAccountcustomerid.equals(other.customerAccountcustomerid))) {
            return false;
        }
        if ((this.fkEmail == null && other.fkEmail != null) || (this.fkEmail != null && !this.fkEmail.equals(other.fkEmail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.JobPK[ jobId=" + jobId + ", customerAccountcustomerid=" + customerAccountcustomerid + ", fkEmail=" + fkEmail + " ]";
    }
    
}
