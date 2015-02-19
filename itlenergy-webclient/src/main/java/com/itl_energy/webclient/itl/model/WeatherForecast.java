package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Weather forecast made for a given site at a given time.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 */
public class WeatherForecast
    {
    private Integer weatherForecastId;
    private Date timeObserved;
    private Float windSpeed;
    private Float windDirection;
    private Float temperature;
    private Float solarDirect;
    private Float solarDiffuse;
    private Date timeOfForecast;
    private Integer siteId;

    public WeatherForecast(Integer weatherForecastId, Date timeObserved, Float windSpeed, Float windDirection, Float temperature, Float solarDirect, Float solarDiffuse, Date timeOfForecast, Integer siteId) {
        this.weatherForecastId = weatherForecastId;
        this.timeObserved = timeObserved;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.temperature = temperature;
        this.solarDirect = solarDirect;
        this.solarDiffuse = solarDiffuse;
        this.timeOfForecast = timeOfForecast;
        this.siteId = siteId;
    }

    public WeatherForecast() {
    }

    public Integer getWeatherForecastId() {
        return weatherForecastId;
    }

    public void setWeatherForecastId(Integer weatherForecastId) {
        this.weatherForecastId = weatherForecastId;
    }

    public Date getTimeObserved() {
        return timeObserved;
    }

    public void setTimeObserved(Date timeObserved) {
        this.timeObserved = timeObserved;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Float windDirection) {
        this.windDirection = windDirection;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getSolarDirect() {
        return solarDirect;
    }

    public void setSolarDirect(Float solarDirect) {
        this.solarDirect = solarDirect;
    }

    public Float getSolarDiffuse() {
        return solarDiffuse;
    }

    public void setSolarDiffuse(Float solarDiffuse) {
        this.solarDiffuse = solarDiffuse;
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