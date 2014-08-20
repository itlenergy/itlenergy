package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the deployed_sensor database table.
 * 
 */
@Entity
@Table(name="deployed_sensor")
@NamedQuery(name="DeployedSensor.findAll", query="SELECT d FROM DeployedSensor d")
@XmlRootElement
public class DeployedSensor implements Serializable {
@Basic(optional = false)
@OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
@NotNull
@Size(min = 1, max = 128)
    private Collection<Sensor> sensorCollection;
    private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="type_id")
    private Integer typeId;
@Column(name = "description")
        private String description;
@Column(name="measurement_units")
    private String measurementUnits;

    public DeployedSensor() {
    }


        public Integer getTypeId() {
            return this.typeId;
    }

    public void setTypeId(Integer typeId) {
            this.typeId = typeId;
    }

        public String getMeasurementUnits() {
            return this.measurementUnits;
    }

    public void setMeasurementUnits(String measurementUnits) {
            this.measurementUnits = measurementUnits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Sensor> getSensorCollection() {
        return sensorCollection;
    }

    public void setSensorCollection(Collection<Sensor> sensorCollection) {
        this.sensorCollection = sensorCollection;
    }

}