package com.itl_energy.webclient.instee.itl.client.model;

/**
 * Deployed sensor container class. Records the types of sensors deployed at a
 * given Site/Hub. Used for version control.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 10/07/2013
 */
public class DeployedSensor {

    protected int typeid;
    protected String description;
    protected String measurementUnits;

    public DeployedSensor() {
        super();
        this.typeid = -1;
    }

    public DeployedSensor(int typeid, String description, String measurementUnits) {
        super();
        this.typeid = typeid;
        this.description = description;
        this.measurementUnits = measurementUnits;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasurementUnits() {
        return measurementUnits;
    }

    public void setMeasurementUnits(String measurementUnits) {
        this.measurementUnits = measurementUnits;
    }
}
