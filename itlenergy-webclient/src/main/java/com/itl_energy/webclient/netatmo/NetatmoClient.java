package com.itl_energy.webclient.netatmo;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.itl.model.DeployedSensor;
import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.util.ApiClient;
import com.itl_energy.webclient.itl.util.ApiException;
import com.itl_energy.webclient.itl.util.ApiResponse;
import com.itl_energy.webclient.itl.util.ITLClientUtilities;
import java.text.ParseException;

/**
 * Interface to NetAtmo Web Service.
 *
 * @author Bruce Stephen
 * @date 10th September 2013
 * @date 17th October 2013
 * @date 20th October 2013
 */
public class NetatmoClient {

    protected String authtoken;
    protected String refreshtoken;

    protected Map<String, String> station2device;
    protected Map<String, String> device2module;

    protected static final String urlbase = "http://api.netatmo.net";

    public static String INDOOR_TEMPERATURE_MEASUREMENT = "Temperature";
    public static String INDOOR_HUMIDITY_MEASUREMENT = "Humidity";
    public static String OUTDOOR_TEMPERATURE_MEASUREMENT = "Temperature";
    public static String OUTDOOR_HUMIDITY_MEASUREMENT = "Humidity";
    public static String CO2_MEASUREMENT = "Co2";
    public static String PRESSURE_MEASUREMENT = "Pressure";
    public static String NOISE_MEASUREMENT = "Noise";

    /*
     Also need to add Error Codes:
 	
     1  : No access token given to the API
     2  : The access token is not valid
     3  : The access token has expired
     4  : Internal error
     5  : The application has been deactivated
     9  : The device has not been found
     10 : A mandatory API parameter is missing
     11 : An unexpected error occured 
     13 : Operation not allowed 
     15 : Installation of the device has not been finalized
     21 : Invalid argument
     25 : Invalid date given 
     26 : Maximum usage of the API has been reached by application
     */
    public NetatmoClient() {
        this.station2device = new HashMap<>();
        this.device2module = new HashMap<>();
    }

    public boolean authenticateUser(String user, String password, String clientid, String secret) throws ApiException {
        ApiClient client = new ApiClient(urlbase + "/oauth2/token");
        client.data("grant_type", "password");
        client.data("client_secret", secret);
        client.data("username", user);
        client.data("password", password);

        ApiResponse response = client.post();

        if (response.success()) {
            JsonObject result = response.deserialise();
            authtoken = result.getAsJsonPrimitive("access_token").getAsString();
            refreshtoken = result.getAsJsonPrimitive("refresh_token").getAsString();
            return true;
        }
        else {
            return false;
        }
    }

    public List<String> getDevices() throws ApiException {
        ApiClient client = new ApiClient(urlbase + "/api/devicelist");
        client.data("access_token", authtoken);

        ApiResponse response = client.get();

        if (response.success()) {
            DeviceList deviceList = response.deserialise("body", DeviceList.class);
            List<String> list = new ArrayList<>();

            for (NetatmoDevice device : deviceList.getDevices()) {
                list.add(device.getStationName());
                station2device.put(device.getStationName(), device.getID());
            }

            for (NetatmoModule module : deviceList.getModules()) {
                device2module.put(module.getMainDevice(), module.getID());
            }

            return list;
        }
        else {
            throw new ApiException("Could not get device list.");
        }
    }

    public String getIDForDevice(String station) {
        return this.station2device.get(station);
    }

    public String getModuleIDForDeviceID(String device) {
        return this.device2module.get(device);
    }

    /**
     * Get a specified measurement from a specified device between 2 specified
     * dates at 30 minute intervals.
     */
    public List<Measurement> getIndoorTemperatureMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.INDOOR_TEMPERATURE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getOutdoorTemperatureMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.OUTDOOR_TEMPERATURE_MEASUREMENT, false, this.getModuleIDForDeviceID(device), device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorHumidityMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.INDOOR_HUMIDITY_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getOutdoorHumidityMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.OUTDOOR_HUMIDITY_MEASUREMENT, false, this.getModuleIDForDeviceID(device), device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorNoiseMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.NOISE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorCO2MeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.CO2_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorPressureMeasurementForDevice(String device, String dateStart, String dateEnd) throws ApiException {
        return this.getMeasurementForDevice(NetatmoClient.PRESSURE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getMeasurementForDevice(String type, boolean indoor, String module, String device, String dateStart, String dateEnd) throws ApiException {
        ApiClient client = new ApiClient("/api/getmeasure");

        try {
            client.data("date_begin", Long.toString(ITLClientUtilities.dateStringToSeconds(dateStart)));
            client.data("date_end", Long.toString(ITLClientUtilities.dateStringToSeconds(dateEnd)));
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("dateStart or dateEnd is an invalid date", ex);
        }
        
        client.data("access_token", authtoken);
        client.data("device_id", device);
        client.data("type", type);
        client.data("scale", "30min");
        
        if (!indoor) {
            client.data("module_id", module);
        }

        ApiResponse response = client.get();

        if (response.success()) {
            NetatmoMeasurement[] netatmoMeasurements = response.deserialise("body", NetatmoMeasurement[].class);
            List<Measurement> measurements = new ArrayList<>();

            for (NetatmoMeasurement measurement : netatmoMeasurements) {
                measurements.addAll(measurement.getMeasurements());
            }

            return measurements;
        }
        else {
            throw new ApiException("Could not get measurement.");
        }
    }

    public static List<DeployedSensor> getSensorsForPlatform() {
        List<DeployedSensor> allsense = new ArrayList<>();
        DeployedSensor itemp = new DeployedSensor();
        DeployedSensor otemp = new DeployedSensor();
        DeployedSensor ihum = new DeployedSensor();
        DeployedSensor ohum = new DeployedSensor();
        DeployedSensor co2 = new DeployedSensor();
        DeployedSensor pressure = new DeployedSensor();
        DeployedSensor noise = new DeployedSensor();

        itemp.setDescription("NetAtmo Indoor Temperature");
        otemp.setDescription("NetAtmo Outdoor Temperature");
        ihum.setDescription("NetAtmo Indoor Humidity");
        ohum.setDescription("NetAtmo Outdoor Humidity");
        co2.setDescription("NetAtmo Indoor CO2 Sensor");
        pressure.setDescription("NetAtmo Indoor Barometric Pressure Sensor");
        noise.setDescription("NetAtmo Indoor Ambient Noise Sensor");

        itemp.setMeasurementUnits("Degrees Celsius");
        otemp.setMeasurementUnits("Degrees Celsius");
        ihum.setMeasurementUnits("%");
        ohum.setMeasurementUnits("%");
        co2.setMeasurementUnits("ppm");
        pressure.setMeasurementUnits("mbar");
        noise.setMeasurementUnits("dB");

        allsense.add(itemp);
        allsense.add(otemp);
        allsense.add(ihum);
        allsense.add(ohum);
        allsense.add(co2);
        allsense.add(pressure);
        allsense.add(noise);

        return allsense;
    }
}
