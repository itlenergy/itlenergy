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
@Table(name = "forecast_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ForecastStatus.findAll", query = "SELECT f FROM ForecastStatus f"),
    @NamedQuery(name = "ForecastStatus.findByForecastStatusId", query = "SELECT f FROM ForecastStatus f WHERE f.forecastStatusId = :forecastStatusId"),
    @NamedQuery(name = "ForecastStatus.findByTimeForecastFor", query = "SELECT f FROM ForecastStatus f WHERE f.timeForecastFor = :timeForecastFor"),
    @NamedQuery(name = "ForecastStatus.findByEnergyUsage", query = "SELECT f FROM ForecastStatus f WHERE f.energyUsage = :energyUsage")})
public class ForecastStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "forecast_status_id")
    private Integer forecastStatusId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_forecast_for")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeForecastFor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "energy_usage")
    private float energyUsage;
    @JoinColumn(name = "hub_id", referencedColumnName = "hub_id")
    @ManyToOne(optional = false)
    private Hub hubId;

    public ForecastStatus() {
    }

    public ForecastStatus(Integer forecastStatusId) {
        this.forecastStatusId = forecastStatusId;
    }

    public ForecastStatus(Integer forecastStatusId, Date timeForecastFor, float energyUsage) {
        this.forecastStatusId = forecastStatusId;
        this.timeForecastFor = timeForecastFor;
        this.energyUsage = energyUsage;
    }

    public Integer getForecastStatusId() {
        return forecastStatusId;
    }

    public void setForecastStatusId(Integer forecastStatusId) {
        this.forecastStatusId = forecastStatusId;
    }

    public Date getTimeForecastFor() {
        return timeForecastFor;
    }

    public void setTimeForecastFor(Date timeForecastFor) {
        this.timeForecastFor = timeForecastFor;
    }

    public float getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(float energyUsage) {
        this.energyUsage = energyUsage;
    }

    public Hub getHubId() {
        return hubId;
    }

    public void setHubId(Hub hubId) {
        this.hubId = hubId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (forecastStatusId != null ? forecastStatusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ForecastStatus)) {
            return false;
        }
        ForecastStatus other = (ForecastStatus) object;
        if ((this.forecastStatusId == null && other.forecastStatusId != null) || (this.forecastStatusId != null && !this.forecastStatusId.equals(other.forecastStatusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.ForecastStatus[ forecastStatusId=" + forecastStatusId + " ]";
    }
    
}
