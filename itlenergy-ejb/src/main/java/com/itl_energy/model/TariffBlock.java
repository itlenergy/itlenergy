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
@Table(name = "tariff_block")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TariffBlock.findAll", query = "SELECT t FROM TariffBlock t"),
    @NamedQuery(name = "TariffBlock.findByTariffBlockId", query = "SELECT t FROM TariffBlock t WHERE t.tariffBlockId = :tariffBlockId"),
    @NamedQuery(name = "TariffBlock.findByStartTime", query = "SELECT t FROM TariffBlock t WHERE t.startTime = :startTime"),
    @NamedQuery(name = "TariffBlock.findByStopTime", query = "SELECT t FROM TariffBlock t WHERE t.stopTime = :stopTime"),
    @NamedQuery(name = "TariffBlock.findByUnitPrice", query = "SELECT t FROM TariffBlock t WHERE t.unitPrice = :unitPrice")})
public class TariffBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tariff_block_id")
    private Integer tariffBlockId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stop_time")
    @Temporal(TemporalType.TIME)
    private Date stopTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unit_price")
    private float unitPrice;
    @JoinColumn(name = "tariff_id", referencedColumnName = "tariff_id")
    @ManyToOne(optional = false)
    private Tariff tariffId;

    public TariffBlock() {
    }

    public TariffBlock(Integer tariffBlockId) {
        this.tariffBlockId = tariffBlockId;
    }

    public TariffBlock(Integer tariffBlockId, Date startTime, Date stopTime, float unitPrice) {
        this.tariffBlockId = tariffBlockId;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.unitPrice = unitPrice;
    }

    public Integer getTariffBlockId() {
        return tariffBlockId;
    }

    public void setTariffBlockId(Integer tariffBlockId) {
        this.tariffBlockId = tariffBlockId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Tariff getTariffId() {
        return tariffId;
    }

    public void setTariffId(Tariff tariffId) {
        this.tariffId = tariffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tariffBlockId != null ? tariffBlockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TariffBlock)) {
            return false;
        }
        TariffBlock other = (TariffBlock) object;
        if ((this.tariffBlockId == null && other.tariffBlockId != null) || (this.tariffBlockId != null && !this.tariffBlockId.equals(other.tariffBlockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.TariffBlock[ tariffBlockId=" + tariffBlockId + " ]";
    }
    
}
