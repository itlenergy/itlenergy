package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Forecast of energy usage in a particular premises at a given time.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class ForecastStatus
    {
    private Integer forecastStatusId;
    private Date timeForecastFor;
    private float energyUsage;
    private Integer hubId;

    public ForecastStatus(Integer forecastStatusId, Date timeForecastFor, float energyUsage, Integer hubId) {
        this.forecastStatusId = forecastStatusId;
        this.timeForecastFor = timeForecastFor;
        this.energyUsage = energyUsage;
        this.hubId = hubId;
    }

    public ForecastStatus() {
    }

    public Integer getForecastStatusId() {
        return forecastStatusId;
    }

    public void setForecastStatusId(Integer forecastStatusId) {
        this.forecastStatusId = forecastStatusId;
    }

    public Date getTimeForecastFor() {
        return timeForecastFor;
    }

    public void setTimeForecastFor(Date timeForecastFor) {
        this.timeForecastFor = timeForecastFor;
    }

    public float getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(float energyUsage) {
        this.energyUsage = energyUsage;
    }

    public Integer getHubId() {
        return hubId;
    }

    public void setHubId(Integer hubId) {
        this.hubId = hubId;
    }
    
    
    }
