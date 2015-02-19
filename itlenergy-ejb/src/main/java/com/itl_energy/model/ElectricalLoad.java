/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ajp97161
 */
@Entity
@Table(name = "electrical_load")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ElectricalLoad.findAll", query = "SELECT e FROM ElectricalLoad e"),
    @NamedQuery(name = "ElectricalLoad.findById", query = "SELECT e FROM ElectricalLoad e WHERE e.id = :id"),
    @NamedQuery(name = "ElectricalLoad.findByObserved", query = "SELECT e FROM ElectricalLoad e WHERE e.observed = :observed"),
    @NamedQuery(name = "ElectricalLoad.findByLoadkwh", query = "SELECT e FROM ElectricalLoad e WHERE e.loadkwh = :loadkwh")})
public class ElectricalLoad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "observed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date observed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "loadkwh")
    private float loadkwh;
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    @ManyToOne(optional = false)
    private Site siteId;

    public ElectricalLoad() {
    }

    public ElectricalLoad(Integer id) {
        this.id = id;
    }

    public ElectricalLoad(Integer id, Date observed, float loadkwh) {
        this.id = id;
        this.observed = observed;
        this.loadkwh = loadkwh;
    }

    public Integer getElectricalLoadObservationId() {
        return id;
    }

    public void setElectricalLoadObservationId(Integer id) {
        this.id = id;
    }

    public Date getObserved() {
        return observed;
    }

    public void setObserved(Date observed) {
        this.observed = observed;
    }

    public float getLoadkwh() {
        return loadkwh;
    }

    public void setLoadkwh(float loadkwh) {
        this.loadkwh = loadkwh;
    }

    public Site getSiteId() {
        return siteId;
    }

    public void setSiteId(Site siteId) {
        this.siteId = siteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ElectricalLoad)) {
            return false;
        }
        ElectricalLoad other = (ElectricalLoad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.ElectricalLoad[ id=" + id + " ]";
    }
    
}
