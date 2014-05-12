package com.itl_energy.webclient.instee.itl.client.model;

/**
 * Deployment site container class. Basic information included for now limited
 * to GPS fixes for matching to weather context data from 3rd parties.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 03/06/2013
 * @version 10/07/2013
 */
public class DeploymentSite {

    protected int siteid;
    protected double latitude;
    protected double longitude;
    protected double altitude;
    protected String name;

    public DeploymentSite(int siteid, double latitude, double longitude, double altitude, String name) {
        super();
        this.siteid = siteid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.name = name;
    }

    public DeploymentSite() {
        super();
        this.siteid = -1;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.name = "";
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
