/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Representation of an actuatable device.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class Actuations {
    private Integer actuationId;
    private Boolean switchOn;
    private Date actuationTime;
    private Integer deviceId;
    private Date deactivationTime;
    private Integer sensorId;

    public Actuations() {
    }

    public Actuations(Integer actuationId, Boolean switchOn, Date actuationTime, Integer deviceId, Date deactivationTime, Integer sensorId) {
        this.actuationId = actuationId;
        this.switchOn = switchOn;
        this.actuationTime = actuationTime;
        this.deviceId = deviceId;
        this.deactivationTime = deactivationTime;
        this.sensorId = sensorId;
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

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }
}
