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
@Table(name = "payment_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentInfo.findAll", query = "SELECT p FROM PaymentInfo p")
    , @NamedQuery(name = "PaymentInfo.findByPaymentId", query = "SELECT p FROM PaymentInfo p WHERE p.paymentId = :paymentId")
    , @NamedQuery(name = "PaymentInfo.findByAmountPaid", query = "SELECT p FROM PaymentInfo p WHERE p.amountPaid = :amountPaid")
    , @NamedQuery(name = "PaymentInfo.findByPaymentType", query = "SELECT p FROM PaymentInfo p WHERE p.paymentType = :paymentType")
    , @NamedQuery(name = "PaymentInfo.findByDatePaid", query = "SELECT p FROM PaymentInfo p WHERE p.datePaid = :datePaid")})
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "payment_id")
    private Integer paymentId;
    @Basic(optional = false)
    @Column(name = "amount_paid")
    private int amountPaid;
    @Basic(optional = false)
    @Column(name = "payment_type")
    private String paymentType;
    @Basic(optional = false)
    @Column(name = "date_paid")
    @Temporal(TemporalType.DATE)
    private Date datePaid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentInfo")
    private List<CardDetails> cardDetailsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentInfo")
    private List<Job> jobList;

    public PaymentInfo() {
    }

    public PaymentInfo(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentInfo(Integer paymentId, int amountPaid, String paymentType, Date datePaid) {
        this.paymentId = paymentId;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
        this.datePaid = datePaid;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
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

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    @XmlTransient
    public List<CardDetails> getCardDetailsList() {
        return cardDetailsList;
    }

    public void setCardDetailsList(List<CardDetails> cardDetailsList) {
        this.cardDetailsList = cardDetailsList;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentInfo)) {
            return false;
        }
        PaymentInfo other = (PaymentInfo) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.PaymentInfo[ paymentId=" + paymentId + " ]";
    }
    
}
