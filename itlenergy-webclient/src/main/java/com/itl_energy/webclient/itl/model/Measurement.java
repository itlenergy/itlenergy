package com.itl_energy.webclient.itl.model;

/**
 * Measurement record container class. Units and sensor context are available
 * from the DeployedSensor table.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 10/07/2013
 */
public class Measurement {

    protected int measurementId;
    protected int sensorId;
    protected String observationTime;
    protected double observation;

    public Measurement(int measurementId, int sensorId, String observationTime, double observation) {
        super();
        this.measurementId = measurementId;
        this.sensorId = sensorId;
        this.observationTime = observationTime;
        this.observation = observation;
    }
    
    
    public Measurement(String observationTime, double observation) {
        this.observationTime = observationTime;
        this.observation = observation;
    }

    public Measurement() {
        this.measurementId = -1;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public double getObservation() {
        return observation;
    }

    public void setObservation(double observation) {
        this.observation = observation;
    }
}
