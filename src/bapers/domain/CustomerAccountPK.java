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
public class CustomerAccountPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "customer_id")
    private String customerId;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "fk_address_line1")
    private String fkAddressLine1;
    @Basic(optional = false)
    @Column(name = "fk_city")
    private String fkCity;
    @Basic(optional = false)
    @Column(name = "fk_postcode")
    private String fkPostcode;

    public CustomerAccountPK() {
    }

    public CustomerAccountPK(String customerId, String email, String fkAddressLine1, String fkCity, String fkPostcode) {
        this.customerId = customerId;
        this.email = email;
        this.fkAddressLine1 = fkAddressLine1;
        this.fkCity = fkCity;
        this.fkPostcode = fkPostcode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFkAddressLine1() {
        return fkAddressLine1;
    }

    public void setFkAddressLine1(String fkAddressLine1) {
        this.fkAddressLine1 = fkAddressLine1;
    }

    public String getFkCity() {
        return fkCity;
    }

    public void setFkCity(String fkCity) {
        this.fkCity = fkCity;
    }

    public String getFkPostcode() {
        return fkPostcode;
    }

    public void setFkPostcode(String fkPostcode) {
        this.fkPostcode = fkPostcode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        hash += (email != null ? email.hashCode() : 0);
        hash += (fkAddressLine1 != null ? fkAddressLine1.hashCode() : 0);
        hash += (fkCity != null ? fkCity.hashCode() : 0);
        hash += (fkPostcode != null ? fkPostcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerAccountPK)) {
            return false;
        }
        CustomerAccountPK other = (CustomerAccountPK) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        if ((this.fkAddressLine1 == null && other.fkAddressLine1 != null) || (this.fkAddressLine1 != null && !this.fkAddressLine1.equals(other.fkAddressLine1))) {
            return false;
        }
        if ((this.fkCity == null && other.fkCity != null) || (this.fkCity != null && !this.fkCity.equals(other.fkCity))) {
            return false;
        }
        if ((this.fkPostcode == null && other.fkPostcode != null) || (this.fkPostcode != null && !this.fkPostcode.equals(other.fkPostcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.CustomerAccountPK[ customerId=" + customerId + ", email=" + email + ", fkAddressLine1=" + fkAddressLine1 + ", fkCity=" + fkCity + ", fkPostcode=" + fkPostcode + " ]";
    }
    
}
