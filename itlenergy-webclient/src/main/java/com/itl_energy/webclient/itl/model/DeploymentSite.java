package com.itl_energy.webclient.itl.model;

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

    protected int siteId;
    protected double latitude;
    protected double longitude;
    protected double altitude;
    protected String siteName;

    public DeploymentSite(int siteid, double latitude, double longitude, double altitude, String name) {
        super();
        this.siteId = siteid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.siteName = name;
    }

    public DeploymentSite() {
        super();
        this.siteId = -1;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.siteName = "";
    }

    public int getSiteid() {
        return siteId;
    }

    public void setSiteid(int siteid) {
        this.siteId = siteid;
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
        return siteName;
    }

    public void setName(String name) {
        this.siteName = name;
    }
}
