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
@Table(name = "actuations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actuations.findAll", query = "SELECT a FROM Actuations a"),
    @NamedQuery(name = "Actuations.findByActuationId", query = "SELECT a FROM Actuations a WHERE a.actuationId = :actuationId"),
    @NamedQuery(name = "Actuations.findBySwitchOn", query = "SELECT a FROM Actuations a WHERE a.switchOn = :switchOn"),
    @NamedQuery(name = "Actuations.findByActuationTime", query = "SELECT a FROM Actuations a WHERE a.actuationTime = :actuationTime"),
    @NamedQuery(name = "Actuations.findByDeviceId", query = "SELECT a FROM Actuations a WHERE a.deviceId = :deviceId"),
    @NamedQuery(name = "Actuations.findByDeactivationTime", query = "SELECT a FROM Actuations a WHERE a.deactivationTime = :deactivationTime")})
public class Actuations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "actuation_id")
    private Integer actuationId;
    @Column(name = "switch_on")
    private Boolean switchOn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actuation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actuationTime;
    @Column(name = "device_id")
    private Integer deviceId;
    @Column(name = "deactivation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deactivationTime;
    @JoinColumn(name = "sensor_id", referencedColumnName = "sensor_id")
    @ManyToOne(optional = false)
    private Sensor sensorId;

    public Actuations() {
    }

    public Actuations(Integer actuationId) {
        this.actuationId = actuationId;
    }

    public Actuations(Integer actuationId, Date actuationTime) {
        this.actuationId = actuationId;
        this.actuationTime = actuationTime;
    }

    public Integer getActuationId() {
        return actuationId;
    }

    public void setActuationId(Integer actuationId) {
        this.actuationId = actuationId;
    }

    public Boolean getSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(Boolean switchOn) {
        this.switchOn = switchOn;
    }

    public Date getActuationTime() {
        return actuationTime;
    }

    public void setActuationTime(Date actuationTime) {
        this.actuationTime = actuationTime;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(Date deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    public Sensor getSensorId() {
        return sensorId;
    }

    public void setSensorId(Sensor sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actuationId != null ? actuationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actuations)) {
            return false;
        }
        Actuations other = (Actuations) object;
        if ((this.actuationId == null && other.actuationId != null) || (this.actuationId != null && !this.actuationId.equals(other.actuationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Actuations[ actuationId=" + actuationId + " ]";
    }
    
}
