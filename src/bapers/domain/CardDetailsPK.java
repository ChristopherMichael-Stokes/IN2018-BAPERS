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
