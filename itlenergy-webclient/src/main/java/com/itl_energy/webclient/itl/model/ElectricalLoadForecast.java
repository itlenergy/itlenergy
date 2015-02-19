package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * An electrical load forecast conducted for the associated
 * community an arbitrary period into the future. 
 * 
 * @author Bruce Stephen
 * @date 7th October 2014
 * @date 16th January 2015
 */
public class ElectricalLoadForecast
    {
    private Integer electricalForecastId;
    private Date timeObserved;
    private Float forecastLoadkwh;
    private Date timeOfForecast;
    private Integer siteId;

    public ElectricalLoadForecast(Integer electricalForecastId, Date timeObserved, Float forecastLoadkwh, Date timeOfForecast, Integer siteId) {
        this.electricalForecastId = electricalForecastId;
        this.timeObserved = timeObserved;
        this.forecastLoadkwh = forecastLoadkwh;
        this.timeOfForecast = timeOfForecast;
        this.siteId = siteId;
    }

    public ElectricalLoadForecast() {
    }

    public Integer getElectricalForecastId() {
        return electricalForecastId;
    }

    public void setElectricalForecastId(Integer electricalForecastId) {
        this.electricalForecastId = electricalForecastId;
    }

    public Date getTimeObserved() {
        return timeObserved;
    }

    public void setTimeObserved(Date timeObserved) {
        this.timeObserved = timeObserved;
    }

    public Float getForecastLoadkwh() {
        return forecastLoadkwh;
    }

    public void setForecastLoadkwh(Float forecastLoadkwh) {
        this.forecastLoadkwh = forecastLoadkwh;
    }

    public Date getTimeOfForecast() {
        return timeOfForecast;
    }

    public void setTimeOfForecast(Date timeOfForecast) {
        this.timeOfForecast = timeOfForecast;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    
    
    }
