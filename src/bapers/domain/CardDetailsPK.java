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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author chris
 */
@Embeddable
public class CardDetailsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "last_digits")
    private String lastDigits;
    @Basic(optional = false)
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    @Basic(optional = false)
    @Column(name = "fk_payment_id")
    private int fkPaymentId;

    public CardDetailsPK() {
    }

    public CardDetailsPK(String lastDigits, Date expiryDate, int fkPaymentId) {
        this.lastDigits = lastDigits;
        this.expiryDate = expiryDate;
        this.fkPaymentId = fkPaymentId;
    }

    public String getLastDigits() {
        return lastDigits;
    }

    public void setLastDigits(String lastDigits) {
        this.lastDigits = lastDigits;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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
        hash += (lastDigits != null ? lastDigits.hashCode() : 0);
        hash += (expiryDate != null ? expiryDate.hashCode() : 0);
        hash += (int) fkPaymentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardDetailsPK)) {
            return false;
        }
        CardDetailsPK other = (CardDetailsPK) object;
        if ((this.lastDigits == null && other.lastDigits != null) || (this.lastDigits != null && !this.lastDigits.equals(other.lastDigits))) {
            return false;
        }
        if ((this.expiryDate == null && other.expiryDate != null) || (this.expiryDate != null && !this.expiryDate.equals(other.expiryDate))) {
            return false;
        }
        if (this.fkPaymentId != other.fkPaymentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.CardDetailsPK[ lastDigits=" + lastDigits + ", expiryDate=" + expiryDate + ", fkPaymentId=" + fkPaymentId + " ]";
    }
    
}
