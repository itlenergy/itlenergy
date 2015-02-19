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
@Table(name = "weather_forecast")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeatherForecast.findAll", query = "SELECT w FROM WeatherForecast w"),
    @NamedQuery(name = "WeatherForecast.findByWeatherForecastId", query = "SELECT w FROM WeatherForecast w WHERE w.weatherForecastId = :weatherForecastId"),
    @NamedQuery(name = "WeatherForecast.findByTimeObserved", query = "SELECT w FROM WeatherForecast w WHERE w.timeObserved = :timeObserved"),
    @NamedQuery(name = "WeatherForecast.findByWindSpeed", query = "SELECT w FROM WeatherForecast w WHERE w.windSpeed = :windSpeed"),
    @NamedQuery(name = "WeatherForecast.findByWindDirection", query = "SELECT w FROM WeatherForecast w WHERE w.windDirection = :windDirection"),
    @NamedQuery(name = "WeatherForecast.findByTemperature", query = "SELECT w FROM WeatherForecast w WHERE w.temperature = :temperature"),
    @NamedQuery(name = "WeatherForecast.findBySolarDirect", query = "SELECT w FROM WeatherForecast w WHERE w.solarDirect = :solarDirect"),
    @NamedQuery(name = "WeatherForecast.findBySolarDiffuse", query = "SELECT w FROM WeatherForecast w WHERE w.solarDiffuse = :solarDiffuse"),
    @NamedQuery(name = "WeatherForecast.findByTimeOfForecast", query = "SELECT w FROM WeatherForecast w WHERE w.timeOfForecast = :timeOfForecast")})
public class WeatherForecast implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "weather_forecast_id")
    private Integer weatherForecastId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_observed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeObserved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "wind_speed")
    private Float windSpeed;
    @Column(name = "wind_direction")
    private Float windDirection;
    @Column(name = "temperature")
    private Float temperature;
    @Column(name = "solar_direct")
    private Float solarDirect;
    @Column(name = "solar_diffuse")
    private Float solarDiffuse;
    @Column(name = "time_of_forecast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfForecast;
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    @ManyToOne(optional = false)
    private Site siteId;

    public WeatherForecast() {
    }

    public WeatherForecast(Integer weatherForecastId) {
        this.weatherForecastId = weatherForecastId;
    }

    public WeatherForecast(Integer weatherForecastId, Date timeObserved) {
        this.weatherForecastId = weatherForecastId;
        this.timeObserved = timeObserved;
    }

    public Integer getWeatherForecastId() {
        return weatherForecastId;
    }

    public void setWeatherForecastId(Integer weatherForecastId) {
        this.weatherForecastId = weatherForecastId;
    }

    public Date getTimeObserved() {
        return timeObserved;
    }

    public void setTimeObserved(Date timeObserved) {
        this.timeObserved = timeObserved;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Float windDirection) {
        this.windDirection = windDirection;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getSolarDirect() {
        return solarDirect;
    }

    public void setSolarDirect(Float solarDirect) {
        this.solarDirect = solarDirect;
    }

    public Float getSolarDiffuse() {
        return solarDiffuse;
    }

    public void setSolarDiffuse(Float solarDiffuse) {
        this.solarDiffuse = solarDiffuse;
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
        hash += (weatherForecastId != null ? weatherForecastId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeatherForecast)) {
            return false;
        }
        WeatherForecast other = (WeatherForecast) object;
        if ((this.weatherForecastId == null && other.weatherForecastId != null) || (this.weatherForecastId != null && !this.weatherForecastId.equals(other.weatherForecastId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.WeatherForecast[ weatherForecastId=" + weatherForecastId + " ]";
    }
    
}
