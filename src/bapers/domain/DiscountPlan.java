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
import javax.persistence.Entity;
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
@Table(name = "discount_plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscountPlan.findAll", query = "SELECT d FROM DiscountPlan d")
    , @NamedQuery(name = "DiscountPlan.findByType", query = "SELECT d FROM DiscountPlan d WHERE d.type = :type")})
public class DiscountPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPlantype")
    private List<CustomerAccount> customerAccountList;

    public DiscountPlan() {
    }

    public DiscountPlan(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        hash += (type != null ? type.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiscountPlan)) {
            return false;
        }
        DiscountPlan other = (DiscountPlan) object;
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bapers.domain.DiscountPlan[ type=" + type + " ]";
    }
    
}
