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
@Table(name = "electrical_load_forecast")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ElectricalLoadForecast.findAll", query = "SELECT e FROM ElectricalLoadForecast e"),
    @NamedQuery(name = "ElectricalLoadForecast.findByElectricalForecastId", query = "SELECT e FROM ElectricalLoadForecast e WHERE e.electricalForecastId = :electricalForecastId"),
    @NamedQuery(name = "ElectricalLoadForecast.findByTimeObserved", query = "SELECT e FROM ElectricalLoadForecast e WHERE e.timeObserved = :timeObserved"),
    @NamedQuery(name = "ElectricalLoadForecast.findByForecastLoadkwh", query = "SELECT e FROM ElectricalLoadForecast e WHERE e.forecastLoadkwh = :forecastLoadkwh"),
    @NamedQuery(name = "ElectricalLoadForecast.findByTimeOfForecast", query = "SELECT e FROM ElectricalLoadForecast e WHERE e.timeOfForecast = :timeOfForecast")})
public class ElectricalLoadForecast implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "electrical_forecast_id")
    private Integer electricalForecastId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_observed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeObserved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "forecast_loadkwh")
    private Float forecastLoadkwh;
    @Column(name = "time_of_forecast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfForecast;
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    @ManyToOne(optional = false)
    private Site siteId;

    public ElectricalLoadForecast() {
    }

    public ElectricalLoadForecast(Integer electricalForecastId) {
        this.electricalForecastId = electricalForecastId;
    }

    public ElectricalLoadForecast(Integer electricalForecastId, Date timeObserved) {
        this.electricalForecastId = electricalForecastId;
        this.timeObserved = timeObserved;
    }

    public Integer getElectricalLoadForecastId() {
        return electricalForecastId;
    }

    public void setElectricalLoadForecastId(Integer electricalForecastId) {
        this.electricalForecastId = electricalForecastId;
    }

    public Date getTimeObserved() {
        return timeObserved;
    }

    public void setTimeObserved(Date timeObserved) {
        this.timeObserved = timeObserved;
    }

    public Float getForecastLoadkwh() {
        return forecastLoadkwh;
    }

    public void setForecastLoadkwh(Float forecastLoadkwh) {
        this.forecastLoadkwh = forecastLoadkwh;
    }

    public Date getTimeOfForecast() {
        return timeOfForecast;
    }

    public void setTimeOfForecast(Date timeOfForecast) {
        this.timeOfForecast = timeOfForecast;
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
        hash += (electricalForecastId != null ? electricalForecastId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ElectricalLoadForecast)) {
            return false;
        }
        ElectricalLoadForecast other = (ElectricalLoadForecast) object;
        if ((this.electricalForecastId == null && other.electricalForecastId != null) || (this.electricalForecastId != null && !this.electricalForecastId.equals(other.electricalForecastId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.ElectricalLoadForecast[ electricalForecastId=" + electricalForecastId + " ]";
    }
    
}
