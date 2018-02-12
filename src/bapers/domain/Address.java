/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
    , @NamedQuery(name = "Address.findByAddressLine1", query = "SELECT a FROM Address a WHERE a.addressPK.addressLine1 = :addressLine1")
    , @NamedQuery(name = "Address.findByAddressLine2", query = "SELECT a FROM Address a WHERE a.addressLine2 = :addressLine2")
    , @NamedQuery(name = "Address.findByPostcode", query = "SELECT a FROM Address a WHERE a.addressPK.postcode = :postcode")
    , @NamedQuery(name = "Address.findByCity", query = "SELECT a FROM Address a WHERE a.addressPK.city = :city")
    , @NamedQuery(name = "Address.findByCountry", query = "SELECT a FROM Address a WHERE a.country = :country")})
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AddressPK addressPK;
    @Column(name = "address_line2")
    private String addressLine2;
    @Basic(optional = false)
    @Column(name = "country")
    private String country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "address")
    private List<CustomerAccount> customerAccountList;

    public Address() {
    }

    public Address(AddressPK addressPK) {
        this.addressPK = addressPK;
    }

    public Address(AddressPK addressPK, String country) {
        this.addressPK = addressPK;
        this.country = country;
    }

    public Address(String addressLine1, String postcode, String city) {
        this.addressPK = new AddressPK(addressLine1, postcode, city);
    }

    public AddressPK getAddressPK() {
        return addressPK;
    }

    public void setAddressPK(AddressPK addressPK) {
        this.addressPK = addressPK;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlTransient
    public List<CustomerAccount> getCustomerAccountList() {
        return customerAccountList;
    }

    public void setCustomerAccountList(List<CustomerAccount> customerAccountList) {
        this.customerAccountList = customerAccountList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressPK != null ? addressPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.addressPK == null && other.addressPK != null) || (this.addressPK != null && !this.addressPK.equals(other.addressPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.Address[ addressPK=" + addressPK + " ]";
    }
    
}
