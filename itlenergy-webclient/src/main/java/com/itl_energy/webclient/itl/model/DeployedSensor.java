package com.itl_energy.webclient.itl.model;

/**
 * Deployed sensor container class. Records the types of sensors deployed at a
 * given Site/Hub. Used for version control.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 10/07/2013
 */
public class DeployedSensor {

    protected int typeId;
    protected String description;
    protected String measurementUnits;

    public DeployedSensor() {
        super();
        this.typeId = -1;
    }

    public DeployedSensor(int typeid, String description, String measurementUnits) {
        super();
        this.typeId = typeid;
        this.description = description;
        this.measurementUnits = measurementUnits;
    }

    public int getTypeid() {
        return typeId;
    }

    public void setTypeid(int typeid) {
        this.typeId = typeid;
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
