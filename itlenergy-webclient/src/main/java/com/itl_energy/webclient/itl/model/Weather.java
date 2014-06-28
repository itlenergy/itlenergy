package com.itl_energy.webclient.itl.model;

/**
 * Weather record container class.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 */
public class Weather {

    protected int weatherObservationId;
    protected String observationTime;
    protected int siteId;
    protected double windSpeed;
    protected double windDirection;
    protected double ambientTemperature;
    protected double humidity;
    protected double uv;
    protected double precipitation;

    public Weather(int weatherObservationId, String observationTime,
            int siteId, double windSpeed, double windDirection,
            double ambientTemperature, double humidity, double uv,
            double precipitation) {
        super();
        this.weatherObservationId = weatherObservationId;
        this.observationTime = observationTime;
        this.siteId = siteId;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.ambientTemperature = ambientTemperature;
        this.humidity = humidity;
        this.uv = uv;
        this.precipitation = precipitation;
    }

    public Weather() {
        this.weatherObservationId = 1;
    }

    public int getWeatherObservationId() {
        return weatherObservationId;
    }

    public void setWeatherObservationId(int weatherObservationId) {
        this.weatherObservationId = weatherObservationId;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getAmbientTemperature() {
        return ambientTemperature;
    }

    public void setAmbientTemperature(double ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }
}
