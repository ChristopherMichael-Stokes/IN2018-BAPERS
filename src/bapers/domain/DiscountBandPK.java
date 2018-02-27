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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author chris
 */
@Embeddable
public class DiscountBandPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "fk_account_number")
    private String fkAccountNumber;
    @Basic(optional = false)
    @Column(name = "price")
    private int price;

    public DiscountBandPK() {
    }

    public DiscountBandPK(String fkAccountNumber, int price) {
        this.fkAccountNumber = fkAccountNumber;
        this.price = price;
    }

    public String getFkAccountNumber() {
        return fkAccountNumber;
    }

    public void setFkAccountNumber(String fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fkAccountNumber != null ? fkAccountNumber.hashCode() : 0);
        hash += (int) price;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiscountBandPK)) {
            return false;
        }
        DiscountBandPK other = (DiscountBandPK) object;
        if ((this.fkAccountNumber == null && other.fkAccountNumber != null) || (this.fkAccountNumber != null && !this.fkAccountNumber.equals(other.fkAccountNumber))) {
            return false;
        }
        if (this.price != other.price) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.DiscountBandPK[ fkAccountNumber=" + fkAccountNumber + ", price=" + price + " ]";
    }
    
}
