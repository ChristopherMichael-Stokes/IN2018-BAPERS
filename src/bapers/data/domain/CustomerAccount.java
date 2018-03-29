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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    , @NamedQuery(name = "CustomerAccount.findByTitle", query = "SELECT c FROM CustomerAccount c WHERE c.title = :title")
    , @NamedQuery(name = "CustomerAccount.findByFirstName", query = "SELECT c FROM CustomerAccount c WHERE c.firstName = :firstName")
    , @NamedQuery(name = "CustomerAccount.findBySurname", query = "SELECT c FROM CustomerAccount c WHERE c.surname = :surname")
    , @NamedQuery(name = "CustomerAccount.findByHousePhone", query = "SELECT c FROM CustomerAccount c WHERE c.housePhone = :housePhone")
    , @NamedQuery(name = "CustomerAccount.findByMobilePhone", query = "SELECT c FROM CustomerAccount c WHERE c.mobilePhone = :mobilePhone")})
public class CustomerAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_number")
    private Integer accountNumber;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "account_holder_name")
    private String accountHolderName;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Column(name = "house_phone")
    private String housePhone;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount")
    private List<Address> addressList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customerAccount")
    private Discount discount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAccountNumber")
    private List<Job> jobList;

    public CustomerAccount() {
    }

    public CustomerAccount(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CustomerAccount(Integer accountNumber, String accountHolderName, String title, String firstName, String surname, String housePhone) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.title = title;
        this.firstName = firstName;
        this.surname = surname;
        this.housePhone = housePhone;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getHousePhone() {
        return housePhone;
    }

    public void setHousePhone(String housePhone) {
        this.housePhone = housePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @XmlTransient
    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @XmlTransient
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
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
