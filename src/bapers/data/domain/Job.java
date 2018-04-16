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
    , @NamedQuery(name = "Job.findByJobId", query = "SELECT j FROM Job j WHERE j.jobId = :jobId")
    , @NamedQuery(name = "Job.findByDescription", query = "SELECT j FROM Job j WHERE j.description = :description")
    , @NamedQuery(name = "Job.findByDateIssued", query = "SELECT j FROM Job j WHERE j.dateIssued = :dateIssued")
    , @NamedQuery(name = "Job.findByQuanity", query = "SELECT j FROM Job j WHERE j.quanity = :quanity")
    , @NamedQuery(name = "Job.findByDeadline", query = "SELECT j FROM Job j WHERE j.deadline = :deadline")
    , @NamedQuery(name = "Job.findByAddedPercentage", query = "SELECT j FROM Job j WHERE j.addedPercentage = :addedPercentage")})
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_id")
    private Integer jobId;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "date_issued")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssued;
    @Basic(optional = false)
    @Column(name = "quanity")
    private short quanity;
    @Column(name = "deadline")
    @Temporal(TemporalType.TIME)
    private Date deadline;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "added_percentage")
    private Float addedPercentage;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job", fetch = FetchType.EAGER)
    private List<JobComponent> jobComponentList;
    @JoinColumns({
        @JoinColumn(name = "fk_forename", referencedColumnName = "forename")
        , @JoinColumn(name = "fk_surname", referencedColumnName = "surname")
        , @JoinColumn(name = "fk_account_number", referencedColumnName = "fk_account_number")})
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Contact contact;
    @JoinColumn(name = "fk_transaction_id", referencedColumnName = "transaction_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PaymentInfo fkTransactionId;

    public Job() {
    }

    public Job(Integer jobId) {
        this.jobId = jobId;
    }

    public Job(Integer jobId, Date dateIssued, short quanity) {
        this.jobId = jobId;
        this.dateIssued = dateIssued;
        this.quanity = quanity;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public short getQuanity() {
        return quanity;
    }

    public void setQuanity(short quanity) {
        this.quanity = quanity;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Float getAddedPercentage() {
        return addedPercentage;
    }

    public void setAddedPercentage(Float addedPercentage) {
        this.addedPercentage = addedPercentage;
    }

    @XmlTransient
    public List<JobComponent> getJobComponentList() {
        return jobComponentList;
    }

    public void setJobComponentList(List<JobComponent> jobComponentList) {
        this.jobComponentList = jobComponentList;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public PaymentInfo getFkTransactionId() {
        return fkTransactionId;
    }

    public void setFkTransactionId(PaymentInfo fkTransactionId) {
        this.fkTransactionId = fkTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobId != null ? jobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Job)) {
            return false;
        }
        Job other = (Job) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.Job[ jobId=" + jobId + " ]";
    }
    
}
