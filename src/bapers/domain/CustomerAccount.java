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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
    , @NamedQuery(name = "CustomerAccount.findByCustomerId", query = "SELECT c FROM CustomerAccount c WHERE c.customerAccountPK.customerId = :customerId")
    , @NamedQuery(name = "CustomerAccount.findByEmail", query = "SELECT c FROM CustomerAccount c WHERE c.customerAccountPK.email = :email")
    , @NamedQuery(name = "CustomerAccount.findByFirstName", query = "SELECT c FROM CustomerAccount c WHERE c.firstName = :firstName")
    , @NamedQuery(name = "CustomerAccount.findBySurname", query = "SELECT c FROM CustomerAccount c WHERE c.surname = :surname")
    , @NamedQuery(name = "CustomerAccount.findByHousePhone", query = "SELECT c FROM CustomerAccount c WHERE c.housePhone = :housePhone")
    , @NamedQuery(name = "CustomerAccount.findByMobilePhone", query = "SELECT c FROM CustomerAccount c WHERE c.mobilePhone = :mobilePhone")
    , @NamedQuery(name = "CustomerAccount.findByValued", query = "SELECT c FROM CustomerAccount c WHERE c.valued = :valued")
    , @NamedQuery(name = "CustomerAccount.findByCustomerDiscountDiscountPlantype", query = "SELECT c FROM CustomerAccount c WHERE c.customerDiscountDiscountPlantype = :customerDiscountDiscountPlantype")})
public class CustomerAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CustomerAccountPK customerAccountPK;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Column(name = "house_phone")
    private String housePhone;
    @Basic(optional = false)
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Basic(optional = false)
    @Column(name = "valued")
    private boolean valued;
    @Column(name = "Customer_Discount_Discount_Plan_type")
    private String customerDiscountDiscountPlantype;
    @JoinColumns({
        @JoinColumn(name = "Address_address_line1", referencedColumnName = "address_line1")
        , @JoinColumn(name = "Address_city", referencedColumnName = "city")
        , @JoinColumn(name = "Address_postcode", referencedColumnName = "postcode")})
    @ManyToOne(optional = false)
    private Address address;
    @JoinColumn(name = "Discount_Plan_type", referencedColumnName = "type")
    @ManyToOne(optional = false)
    private DiscountPlan discountPlantype;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerAccount")
    private List<Job> jobList;

    public CustomerAccount() {
    }

    public CustomerAccount(CustomerAccountPK customerAccountPK) {
        this.customerAccountPK = customerAccountPK;
    }

    public CustomerAccount(CustomerAccountPK customerAccountPK, String firstName, String surname, String housePhone, String mobilePhone, boolean valued) {
        this.customerAccountPK = customerAccountPK;
        this.firstName = firstName;
        this.surname = surname;
        this.housePhone = housePhone;
        this.mobilePhone = mobilePhone;
        this.valued = valued;
    }

    public CustomerAccount(String customerId, String email) {
        this.customerAccountPK = new CustomerAccountPK(customerId, email);
    }

    public CustomerAccountPK getCustomerAccountPK() {
        return customerAccountPK;
    }

    public void setCustomerAccountPK(CustomerAccountPK customerAccountPK) {
        this.customerAccountPK = customerAccountPK;
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

    public boolean getValued() {
        return valued;
    }

    public void setValued(boolean valued) {
        this.valued = valued;
    }

    public String getCustomerDiscountDiscountPlantype() {
        return customerDiscountDiscountPlantype;
    }

    public void setCustomerDiscountDiscountPlantype(String customerDiscountDiscountPlantype) {
        this.customerDiscountDiscountPlantype = customerDiscountDiscountPlantype;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DiscountPlan getDiscountPlantype() {
        return discountPlantype;
    }

    public void setDiscountPlantype(DiscountPlan discountPlantype) {
        this.discountPlantype = discountPlantype;
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
        hash += (customerAccountPK != null ? customerAccountPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerAccount)) {
            return false;
        }
        CustomerAccount other = (CustomerAccount) object;
        if ((this.customerAccountPK == null && other.customerAccountPK != null) || (this.customerAccountPK != null && !this.customerAccountPK.equals(other.customerAccountPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.CustomerAccount[ customerAccountPK=" + customerAccountPK + " ]";
    }
    
}
