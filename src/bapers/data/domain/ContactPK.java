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
public class ContactPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "forename")
    private String forename;
    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Column(name = "fk_account_number")
    private short fkAccountNumber;

    public ContactPK() {
    }

    public ContactPK(String forename, String surname, short fkAccountNumber) {
        this.forename = forename;
        this.surname = surname;
        this.fkAccountNumber = fkAccountNumber;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public short getFkAccountNumber() {
        return fkAccountNumber;
    }

    public void setFkAccountNumber(short fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (forename != null ? forename.hashCode() : 0);
        hash += (surname != null ? surname.hashCode() : 0);
        hash += (int) fkAccountNumber;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactPK)) {
            return false;
        }
        ContactPK other = (ContactPK) object;
        if ((this.forename == null && other.forename != null) || (this.forename != null && !this.forename.equals(other.forename))) {
            return false;
        }
        if ((this.surname == null && other.surname != null) || (this.surname != null && !this.surname.equals(other.surname))) {
            return false;
        }
        if (this.fkAccountNumber != other.fkAccountNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.ContactPK[ forename=" + forename + ", surname=" + surname + ", fkAccountNumber=" + fkAccountNumber + " ]";
    }
    
}
