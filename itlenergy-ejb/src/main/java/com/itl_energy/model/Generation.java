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
@Table(name = "generation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generation.findAll", query = "SELECT g FROM Generation g"),
    @NamedQuery(name = "Generation.findById", query = "SELECT g FROM Generation g WHERE g.id = :id"),
    @NamedQuery(name = "Generation.findByObserved", query = "SELECT g FROM Generation g WHERE g.observed = :observed"),
    @NamedQuery(name = "Generation.findByActive", query = "SELECT g FROM Generation g WHERE g.active = :active"),
    @NamedQuery(name = "Generation.findByReactive", query = "SELECT g FROM Generation g WHERE g.reactive = :reactive"),
    @NamedQuery(name = "Generation.findByPhase", query = "SELECT g FROM Generation g WHERE g.phase = :phase")})
public class Generation implements Serializable {
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
    @Column(name = "active")
    private float active;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reactive")
    private float reactive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "phase")
    private float phase;
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    @ManyToOne(optional = false)
    private Site siteId;

    public Generation() {
    }

    public Generation(Integer id) {
        this.id = id;
    }

    public Generation(Integer id, Date observed, float active, float reactive, float phase) {
        this.id = id;
        this.observed = observed;
        this.active = active;
        this.reactive = reactive;
        this.phase = phase;
    }

    public Integer getGenerationObservationId() {
        return id;
    }

    public void setGenerationObservationId(Integer id) {
        this.id = id;
    }

    public Date getObserved() {
        return observed;
    }

    public void setObserved(Date observed) {
        this.observed = observed;
    }

    public float getActive() {
        return active;
    }

    public void setActive(float active) {
        this.active = active;
    }

    public float getReactive() {
        return reactive;
    }

    public void setReactive(float reactive) {
        this.reactive = reactive;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
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
        if (!(object instanceof Generation)) {
            return false;
        }
        Generation other = (Generation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Generation[ id=" + id + " ]";
    }
    
}
