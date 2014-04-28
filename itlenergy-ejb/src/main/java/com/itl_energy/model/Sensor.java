package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the sensor database table. Hub selection added.
 * 
 * @author Bruce Stephen
 * @date 21st January 2014
 */

@Entity
@NamedQueries({
@NamedQuery(name="Sensor.findAll", query="SELECT s FROM Sensor s"),
@NamedQuery(name="Sensor.findByHubId", query="SELECT s FROM Sensor s WHERE s.hubId=:hubId")})
@XmlRootElement

public class Sensor implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer sensorId;
	private String description;
	private Integer hubId;
	private Integer typeId;

	public Sensor() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sensor_id")
	public Integer getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="hub_id")
	public Integer getHubId() {
		return this.hubId;
	}

	public void setHubId(Integer hubId) {
		this.hubId = hubId;
	}


	@Column(name="type_id")
	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}