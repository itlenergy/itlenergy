package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the deployed_sensor database table.
 * 
 */
@Entity
@Table(name="deployed_sensor")
@NamedQuery(name="DeployedSensor.findAll", query="SELECT d FROM DeployedSensor d")
@XmlRootElement
public class DeployedSensor implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer typeId;
	private String description;
	private String measurementUnits;

	public DeployedSensor() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="type_id")
	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="measurement_units")
	public String getMeasurementUnits() {
		return this.measurementUnits;
	}

	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

}