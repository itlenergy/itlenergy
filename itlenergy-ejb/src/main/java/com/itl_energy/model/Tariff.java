/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ajp97161
 */
@Entity
@Table(name = "tariff")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tariff.findAll", query = "SELECT t FROM Tariff t"),
    @NamedQuery(name = "Tariff.findByTariffId", query = "SELECT t FROM Tariff t WHERE t.tariffId = :tariffId"),
    @NamedQuery(name = "Tariff.findByTariffName", query = "SELECT t FROM Tariff t WHERE t.tariffName = :tariffName"),
    @NamedQuery(name = "Tariff.findByPriceUnit", query = "SELECT t FROM Tariff t WHERE t.priceUnit = :priceUnit"),
    @NamedQuery(name = "Tariff.findByIssueDate", query = "SELECT t FROM Tariff t WHERE t.issueDate = :issueDate")})
public class Tariff implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tariff_id")
    private Integer tariffId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "tariff_name")
    private String tariffName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "price_unit")
    private String priceUnit;
    @Column(name = "issue_date")
    @Temporal(TemporalType.DATE)
    private Date issueDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffId")
    private Collection<TariffBlock> tariffBlockCollection;

    public Tariff() {
    }

    public Tariff(Integer tariffId) {
        this.tariffId = tariffId;
    }

    public Tariff(Integer tariffId, String tariffName, String priceUnit) {
        this.tariffId = tariffId;
        this.tariffName = tariffName;
        this.priceUnit = priceUnit;
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    @XmlTransient
    public Collection<TariffBlock> getTariffBlockCollection() {
        return tariffBlockCollection;
    }

    public void setTariffBlockCollection(Collection<TariffBlock> tariffBlockCollection) {
        this.tariffBlockCollection = tariffBlockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tariffId != null ? tariffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tariff)) {
            return false;
        }
        Tariff other = (Tariff) object;
        if ((this.tariffId == null && other.tariffId != null) || (this.tariffId != null && !this.tariffId.equals(other.tariffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Tariff[ tariffId=" + tariffId + " ]";
    }
    
}
