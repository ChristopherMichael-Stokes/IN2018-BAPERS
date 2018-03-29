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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "discount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Discount.findAll", query = "SELECT d FROM Discount d")
    , @NamedQuery(name = "Discount.findByFkAccountNumber", query = "SELECT d FROM Discount d WHERE d.fkAccountNumber = :fkAccountNumber")})
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "fk_account_number")
    private Integer fkAccountNumber;
    @JoinColumn(name = "fk_account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private CustomerAccount customerAccount;
    @JoinColumn(name = "fk_type", referencedColumnName = "type")
    @ManyToOne(optional = false)
    private DiscountPlan fkType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discount")
    private List<TaskDiscount> taskDiscountList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discount")
    private List<DiscountBand> discountBandList;

    public Discount() {
    }

    public Discount(Integer fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    public Integer getFkAccountNumber() {
        return fkAccountNumber;
    }

    public void setFkAccountNumber(Integer fkAccountNumber) {
        this.fkAccountNumber = fkAccountNumber;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public DiscountPlan getFkType() {
        return fkType;
    }

    public void setFkType(DiscountPlan fkType) {
        this.fkType = fkType;
    }

    @XmlTransient
    public List<TaskDiscount> getTaskDiscountList() {
        return taskDiscountList;
    }

    public void setTaskDiscountList(List<TaskDiscount> taskDiscountList) {
        this.taskDiscountList = taskDiscountList;
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
        hash += (fkAccountNumber != null ? fkAccountNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        if ((this.fkAccountNumber == null && other.fkAccountNumber != null) || (this.fkAccountNumber != null && !this.fkAccountNumber.equals(other.fkAccountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.data.domain.Discount[ fkAccountNumber=" + fkAccountNumber + " ]";
    }
    
}
