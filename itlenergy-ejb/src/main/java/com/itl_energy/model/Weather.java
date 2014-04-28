package com.itl_energy.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The persistent class for the weather database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Weather.findAll", query="SELECT w FROM Weather w"),
    @NamedQuery(name="Weather.findBySiteId", query="SELECT w FROM Weather w WHERE w.siteId = :siteId"),
    @NamedQuery(name="Weather.findByDateRange", query="SELECT w FROM Weather w WHERE w.observationTime >= :minTime AND w.observationTime <= :maxTime"),
    @NamedQuery(name="Weather.findByDateRangeAndSiteId", query="SELECT w FROM Weather w WHERE w.siteId = :siteId AND w.observationTime >= :minTime AND w.observationTime <= :maxTime")
})
@XmlRootElement
public class Weather implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer weatherObservationId;
	private float ambientTemperature;
	private float humidity;
	private Date observationTime;
	private float precipitation;
	private Integer siteId;
	private float uv;
	private float windDirection;
	private float windSpeed;

	public Weather() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="weather_observation_id")
	public Integer getWeatherObservationId() {
		return this.weatherObservationId;
	}

	public void setWeatherObservationId(Integer weatherObservationId) {
		this.weatherObservationId = weatherObservationId;
	}


	@Column(name="ambient_temperature")
	public float getAmbientTemperature() {
		return this.ambientTemperature;
	}

	public void setAmbientTemperature(float ambientTemperature) {
		this.ambientTemperature = ambientTemperature;
	}


	public float getHumidity() {
		return this.humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}


	@Column(name="observation_time")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(IsoDateAdapter.class)
	public Date getObservationTime() {
		return this.observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}


	public float getPrecipitation() {
		return this.precipitation;
	}

	public void setPrecipitation(float precipitation) {
		this.precipitation = precipitation;
	}


	@Column(name="site_id")
	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}


	public float getUv() {
		return this.uv;
	}

	public void setUv(float uv) {
		this.uv = uv;
	}


	@Column(name="wind_direction")
	public float getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(float windDirection) {
		this.windDirection = windDirection;
	}


	@Column(name="wind_speed")
	public float getWindSpeed() {
		return this.windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

}