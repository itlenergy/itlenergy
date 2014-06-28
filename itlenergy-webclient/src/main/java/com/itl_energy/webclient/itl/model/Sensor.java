package com.itl_energy.webclient.itl.model;

/**
 * Sensor container class. All sensors deployed have an association here to
 * register their location, purpose and unit of measurement.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 10/07/2013
 */
public class Sensor {

    protected int sensorId;
    protected int hubId;
    protected int typeId;
    protected String description;

    public Sensor() {
        this.sensorId = -1;
        this.hubId = -1;
        this.typeId = -1;
        this.description = "";
    }

    public Sensor(int sensorId, int hubId, int typeId, String description) {
        super();
        this.sensorId = sensorId;
        this.hubId = hubId;
        this.typeId = typeId;
        this.description = description;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getHubId() {
        return hubId;
    }

    public void setHubId(int hubId) {
        this.hubId = hubId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
