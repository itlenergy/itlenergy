package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The persistent class for the measurement database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Measurement.findAll", query="SELECT m FROM Measurement m ORDER BY m.observationTime"),
    @NamedQuery(name="Measurement.findBySensorId", query="SELECT m FROM Measurement m WHERE m.sensorId = :sensorId ORDER BY m.observationTime"),
    @NamedQuery(name="Measurement.findByDateRange", query="SELECT m FROM Measurement m WHERE m.observationTime >= :minTime AND m.observationTime <= :maxTime ORDER BY m.observationTime"),
    @NamedQuery(name="Measurement.findByDateRangeAndSensorId", query="SELECT m FROM Measurement m WHERE m.sensorId = :sensorId AND m.observationTime >= :minTime AND m.observationTime <= :maxTime ORDER BY m.observationTime")
})
@XmlRootElement
public class Measurement implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer measurementId;
	private float observation;
	private Date observationTime;
	private Integer sensorId;

	public Measurement() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="measurement_id")
	public Integer getMeasurementId() {
		return this.measurementId;
	}

	public void setMeasurementId(Integer measurementId) {
		this.measurementId = measurementId;
	}


	public float getObservation() {
		return this.observation;
	}

	public void setObservation(float observation) {
		this.observation = observation;
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


	@Column(name="sensor_id")
	public Integer getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

}