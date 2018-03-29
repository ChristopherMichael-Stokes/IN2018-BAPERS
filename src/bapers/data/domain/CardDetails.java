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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author chris
 */
@Entity
@Table(name = "card_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CardDetails.findAll", query = "SELECT c FROM CardDetails c")
    , @NamedQuery(name = "CardDetails.findByLastDigits", query = "SELECT c FROM CardDetails c WHERE c.cardDetailsPK.lastDigits = :lastDigits")
    , @NamedQuery(name = "CardDetails.findByExpiryDate", query = "SELECT c FROM CardDetails c WHERE c.cardDetailsPK.expiryDate = :expiryDate")
    , @NamedQuery(name = "CardDetails.findByCardType", query = "SELECT c FROM CardDetails c WHERE c.cardType = :cardType")
    , @NamedQuery(name = "CardDetails.findByFkTransactionId", query = "SELECT c FROM CardDetails c WHERE c.cardDetailsPK.fkTransactionId = :fkTransactionId")})
public class CardDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CardDetailsPK cardDetailsPK;
    @Basic(optional = false)
    @Column(name = "card_type")
    private String cardType;
    @JoinColumn(name = "fk_transaction_id", referencedColumnName = "transaction_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PaymentInfo paymentInfo;

    public CardDetails() {
    }

    public CardDetails(CardDetailsPK cardDetailsPK) {
        this.cardDetailsPK = cardDetailsPK;
    }

    public CardDetails(CardDetailsPK cardDetailsPK, String cardType) {
        this.cardDetailsPK = cardDetailsPK;
        this.cardType = cardType;
    }

    public CardDetails(String lastDigits, Date expiryDate, String fkTransactionId) {
        this.cardDetailsPK = new CardDetailsPK(lastDigits, expiryDate, fkTransactionId);
    }

    public CardDetailsPK getCardDetailsPK() {
        return cardDetailsPK;
    }

    public void setCardDetailsPK(CardDetailsPK cardDetailsPK) {
        this.cardDetailsPK = cardDetailsPK;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardDetailsPK != null ? cardDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardDetails)) {
            return false;
        }
        CardDetails other = (CardDetails) object;
        if ((this.cardDetailsPK == null && other.cardDetailsPK != null) || (this.cardDetailsPK != null && !this.cardDetailsPK.equals(other.cardDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.CardDetails[ cardDetailsPK=" + cardDetailsPK + " ]";
    }
    
}
