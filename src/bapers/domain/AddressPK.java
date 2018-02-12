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
public class AddressPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "address_line1")
    private String addressLine1;
    @Basic(optional = false)
    @Column(name = "postcode")
    private String postcode;
    @Basic(optional = false)
    @Column(name = "city")
    private String city;

    public AddressPK() {
    }

    public AddressPK(String addressLine1, String postcode, String city) {
        this.addressLine1 = addressLine1;
        this.postcode = postcode;
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressLine1 != null ? addressLine1.hashCode() : 0);
        hash += (postcode != null ? postcode.hashCode() : 0);
        hash += (city != null ? city.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AddressPK)) {
            return false;
        }
        AddressPK other = (AddressPK) object;
        if ((this.addressLine1 == null && other.addressLine1 != null) || (this.addressLine1 != null && !this.addressLine1.equals(other.addressLine1))) {
            return false;
        }
        if ((this.postcode == null && other.postcode != null) || (this.postcode != null && !this.postcode.equals(other.postcode))) {
            return false;
        }
        if ((this.city == null && other.city != null) || (this.city != null && !this.city.equals(other.city))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.AddressPK[ addressLine1=" + addressLine1 + ", postcode=" + postcode + ", city=" + city + " ]";
    }
    
}
