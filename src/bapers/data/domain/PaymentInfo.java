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
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "payment_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentInfo.findAll", query = "SELECT p FROM PaymentInfo p")
    , @NamedQuery(name = "PaymentInfo.findByTransactionId", query = "SELECT p FROM PaymentInfo p WHERE p.transactionId = :transactionId")
    , @NamedQuery(name = "PaymentInfo.findByDatePaid", query = "SELECT p FROM PaymentInfo p WHERE p.datePaid = :datePaid")
    , @NamedQuery(name = "PaymentInfo.findByAmountPaid", query = "SELECT p FROM PaymentInfo p WHERE p.amountPaid = :amountPaid")
    , @NamedQuery(name = "PaymentInfo.findByPaymentType", query = "SELECT p FROM PaymentInfo p WHERE p.paymentType = :paymentType")})
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "transaction_id")
    private String transactionId;
    @Basic(optional = false)
    @Column(name = "date_paid")
    @Temporal(TemporalType.DATE)
    private Date datePaid;
    @Basic(optional = false)
    @Column(name = "amount_paid")
    private int amountPaid;
    @Basic(optional = false)
    @Column(name = "payment_type")
    private String paymentType;
    @ManyToMany(mappedBy = "paymentInfoList")
    private List<Job> jobList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentInfo")
    private List<CardDetails> cardDetailsList;

    public PaymentInfo() {
    }

    public PaymentInfo(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentInfo(String transactionId, Date datePaid, int amountPaid, String paymentType) {
        this.transactionId = transactionId;
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @XmlTransient
    public List<CardDetails> getCardDetailsList() {
        return cardDetailsList;
    }

    public void setCardDetailsList(List<CardDetails> cardDetailsList) {
        this.cardDetailsList = cardDetailsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentInfo)) {
            return false;
        }
        PaymentInfo other = (PaymentInfo) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.PaymentInfo[ transactionId=" + transactionId + " ]";
    }
    
}
