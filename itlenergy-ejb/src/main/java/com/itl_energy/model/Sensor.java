package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the sensor database table. Hub selection added.
 *
 * @author Bruce Stephen
 * @date 21st January 2014
 * @date 18th August 2014
 */
@Entity
@Table(name = "sensor")
@NamedQueries({
    @NamedQuery(name = "Sensor.findAll", query = "SELECT s FROM Sensor s"),
    @NamedQuery(name = "Sensor.findBySensorId", query = "SELECT s FROM Sensor s WHERE s.sensorId = :sensorId"),
    @NamedQuery(name = "Sensor.findByHubId", query = "SELECT s FROM Sensor s WHERE s.hubId=:hubId"),
    @NamedQuery(name = "Sensor.findByDescription", query = "SELECT s FROM Sensor s WHERE s.description = :description")})
@XmlRootElement
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer sensorId;
    @NotNull
    @Size(min = 1, max = 64)
    private String description;
    private Collection<Actuations> actuationsCollection;
    private Integer hubId;
    private Integer typeId;

    public Sensor() {
    }

    public Sensor(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public Sensor(Integer sensorId, String description) {
        this.sensorId = sensorId;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sensor_id")
    public Integer getSensorId() {
        return this.sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    @Basic(optional = false)
    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "hub_id")
    public Integer getHubId() {
        return this.hubId;
    }

    public void setHubId(Integer hubId) {
        this.hubId = hubId;
    }

    @Column(name = "type_id")
    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sensorId")
    @XmlTransient
    public Collection<Actuations> getActuationsCollection() {
        return actuationsCollection;
    }

    public void setActuationsCollection(Collection<Actuations> actuationsCollection) {
        this.actuationsCollection = actuationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sensorId != null ? sensorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sensor)) {
            return false;
        }
        Sensor other = (Sensor) object;
        if ((this.sensorId == null && other.sensorId != null) || (this.sensorId != null && !this.sensorId.equals(other.sensorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Sensor[ sensorId=" + sensorId + " ]";
    }
}