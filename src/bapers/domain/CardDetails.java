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
    , @NamedQuery(name = "CardDetails.findByFkPaymentId", query = "SELECT c FROM CardDetails c WHERE c.cardDetailsPK.fkPaymentId = :fkPaymentId")
    , @NamedQuery(name = "CardDetails.findByCardType", query = "SELECT c FROM CardDetails c WHERE c.cardType = :cardType")})
public class CardDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CardDetailsPK cardDetailsPK;
    @Basic(optional = false)
    @Column(name = "card_type")
    private String cardType;
    @JoinColumn(name = "fk_payment_id", referencedColumnName = "payment_id", insertable = false, updatable = false)
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

    public CardDetails(String lastDigits, Date expiryDate, int fkPaymentId) {
        this.cardDetailsPK = new CardDetailsPK(lastDigits, expiryDate, fkPaymentId);
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
        return "bapers.domain.CardDetails[ cardDetailsPK=" + cardDetailsPK + " ]";
    }
    
}
