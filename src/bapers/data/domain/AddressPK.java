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
    @Basic(optional = false)
    @Column(name = "fk_account_number")
    private int fkAccountNumber;

    public AddressPK() {
    }

    public AddressPK(String addressLine1, String postcode, String city, int fkAccountNumber) {
        this.addressLine1 = addressLine1;
        this.postcode = postcode;
        this.city = city;
        this.fkAccountNumber = fkAccountNumber;
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

    public int getFkAccountNumber() {
        return fkAccountNumber;
    }

    public void setFkAccountNumber(int fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressLine1 != null ? addressLine1.hashCode() : 0);
        hash += (postcode != null ? postcode.hashCode() : 0);
        hash += (city != null ? city.hashCode() : 0);
        hash += (int) fkAccountNumber;
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
        if (this.fkAccountNumber != other.fkAccountNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.AddressPK[ addressLine1=" + addressLine1 + ", postcode=" + postcode + ", city=" + city + ", fkAccountNumber=" + fkAccountNumber + " ]";
    }
    
}
