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
    @Column(name = "fk_customer_id")
    private String fkCustomerId;
    @Basic(optional = false)
    @Column(name = "fk_email")
    private String fkEmail;
    @Basic(optional = false)
    @Column(name = "fk_payment_id")
    private int fkPaymentId;

    public JobPK() {
    }

    public JobPK(int jobId, String fkCustomerId, String fkEmail, int fkPaymentId) {
        this.jobId = jobId;
        this.fkCustomerId = fkCustomerId;
        this.fkEmail = fkEmail;
        this.fkPaymentId = fkPaymentId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getFkCustomerId() {
        return fkCustomerId;
    }

    public void setFkCustomerId(String fkCustomerId) {
        this.fkCustomerId = fkCustomerId;
    }

    public String getFkEmail() {
        return fkEmail;
    }

    public void setFkEmail(String fkEmail) {
        this.fkEmail = fkEmail;
    }

    public int getFkPaymentId() {
        return fkPaymentId;
    }

    public void setFkPaymentId(int fkPaymentId) {
        this.fkPaymentId = fkPaymentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) jobId;
        hash += (fkCustomerId != null ? fkCustomerId.hashCode() : 0);
        hash += (fkEmail != null ? fkEmail.hashCode() : 0);
        hash += (int) fkPaymentId;
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
        if ((this.fkCustomerId == null && other.fkCustomerId != null) || (this.fkCustomerId != null && !this.fkCustomerId.equals(other.fkCustomerId))) {
            return false;
        }
        if ((this.fkEmail == null && other.fkEmail != null) || (this.fkEmail != null && !this.fkEmail.equals(other.fkEmail))) {
            return false;
        }
        if (this.fkPaymentId != other.fkPaymentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.JobPK[ jobId=" + jobId + ", fkCustomerId=" + fkCustomerId + ", fkEmail=" + fkEmail + ", fkPaymentId=" + fkPaymentId + " ]";
    }
    
}
