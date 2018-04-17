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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author chris
 */
@Entity
@Table(name = "customer_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerAccount.findAll", query = "SELECT c FROM CustomerAccount c")
    , @NamedQuery(name = "CustomerAccount.findByAccountNumber", query = "SELECT c FROM CustomerAccount c WHERE c.accountNumber = :accountNumber")
    , @NamedQuery(name = "CustomerAccount.findByEmail", query = "SELECT c FROM CustomerAccount c WHERE c.email = :email")
    , @NamedQuery(name = "CustomerAccount.findByAccountHolderName", query = "SELECT c FROM CustomerAccount c WHERE c.accountHolderName = :accountHolderName")
    , @NamedQuery(name = "CustomerAccount.findByLandline", query = "SELECT c FROM CustomerAccount c WHERE c.landline = :landline")
    , @NamedQuery(name = "CustomerAccount.findByDiscountType", query = "SELECT c FROM CustomerAccount c WHERE c.discountType = :discountType")
    , @NamedQuery(name = "CustomerAccount.findByLocked", query = "SELECT c FROM CustomerAccount c WHERE c.locked = :locked")})
public class CustomerAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_number")
    private Short accountNumber;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "account_holder_name")
    private String accountHolderName;
    @Column(name = "landline")
    private String landline;
    @Column(name = "discount_type")
    private Short discountType;
    @Basic(optional = false)
    @Column(name = "locked")
    private short locked;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private List<Address> addressList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private List<TaskDiscount> taskDiscountList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private List<Contact> contactList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private List<DiscountBand> discountBandList;

    public CustomerAccount() {
    }

    public CustomerAccount(Short accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CustomerAccount(Short accountNumber, String accountHolderName, short locked) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.locked = locked;
    }

    public Short getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Short accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Short getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Short discountType) {
        this.discountType = discountType;
    }

    public short getLocked() {
        return locked;
    }

    public void setLocked(short locked) {
        this.locked = locked;
    }

    @XmlTransient
    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @XmlTransient
    public List<TaskDiscount> getTaskDiscountList() {
        return taskDiscountList;
    }

    public void setTaskDiscountList(List<TaskDiscount> taskDiscountList) {
        this.taskDiscountList = taskDiscountList;
    }

    @XmlTransient
    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @XmlTransient
    public List<DiscountBand> getDiscountBandList() {
        return discountBandList;
    }

    public void setDiscountBandList(List<DiscountBand> discountBandList) {
        this.discountBandList = discountBandList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountNumber != null ? accountNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerAccount)) {
            return false;
        }
        CustomerAccount other = (CustomerAccount) object;
        if ((this.accountNumber == null && other.accountNumber != null) || (this.accountNumber != null && !this.accountNumber.equals(other.accountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.CustomerAccount[ accountNumber=" + accountNumber + " ]";
    }
    
}
