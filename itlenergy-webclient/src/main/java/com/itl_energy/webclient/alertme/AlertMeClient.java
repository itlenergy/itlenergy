package com.itl_energy.webclient.alertme;

import com.itl_energy.webclient.itl.model.DeployedSensor;
import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.util.ApiClient;
import com.itl_energy.webclient.itl.util.ApiException;
import com.itl_energy.webclient.itl.util.ApiResponse;
import com.itl_energy.webclient.itl.util.ITLClientUtilities;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps the AlertMe API.
 *
 * @author Bruce Stephen
 * @date 25th October 2013
 */
public class AlertMeClient {
    private String token;
    private String hubid;
    private String username;

    private static final String urlbase = "https://api.alertme.com/v5";
    
    /**
     * Authenticates the user with the API.  After a successful authentication,
     * a token is stored and used by subsequent method calls.  Note that this 
     * method should be called first.
     * 
     * @param username the username to authenticate with
     * @param password the password to authenticate with
     * @return true if the authentication succeeded; otherwise, false
     * @throws ApiException if there is a connection error
     */
    public boolean authenticateUser(String username, String password) throws ApiException {
        ApiClient client = new ApiClient("%s/login", urlbase);
        client.data("username", username);
        client.data("password", password);
        client.data("caller", "ITL");

        ApiResponse response = client.post();

        if (response.success()) {
            Map<String, Object> map = response.deserialise(Map.class);
            List<String> allhubs = (ArrayList<String>) map.get("hubIds");

            this.hubid = allhubs.get(0);
            this.token = "ApiSession=" + (String) map.get("ApiSession");
            this.username = username;
            return true;
        }
        else {
            return false;
        }
    }

    private <T> T getResponse(Class<T> cls, String url, Object... params) throws ApiException {
        ApiClient client = new ApiClient(urlbase + url, params);
        client.cookie(token);

        ApiResponse response = client.get();

        if (response.success()) {
            return response.deserialise(cls);
        }
        else {
            throw new ApiException(String.format("Error (status code %d).", response.getStatusCode()));
        }
    }

    /**
     * Gets the IDs of the hubs for the currently-authenticated user.
     * @return a list of hub IDs
     * @throws ApiException if there is a connection error
     */
    public List<String> getHubsForUser() throws ApiException {
        AlertMeDevice[] devices = getResponse(AlertMeDevice[].class, "/users/%s/hubs", username);
        List<String> hubs = new ArrayList<>();

        for (AlertMeDevice device : devices) {
            hubs.add(device.getID());
        }

        return hubs;
    }

    /**
     * Gets all the devices for the user's hub.
     * @return a map mapping device IDs to device types
     * @throws ApiException if there is a connection error
     */
    public Map<String, String> getDevicesForHub() throws ApiException {
        AlertMeDevice[] devices = getResponse(AlertMeDevice[].class,
                "%s/users/%s/hubs/%s/devices", username, this.hubid);

        Map<String, String> map = new HashMap<>();

        for (AlertMeDevice device : devices) {
            map.put(device.getID(), device.getType());
        }

        return map;
    }

    /**
     * Gets the maximum power measurements at half hour intervals between the
     * specified times for the MeterReader device with the specified ID.
     * @param deviceId the ID of the device to get measurements for
     * @param start the start time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param end the end time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param interval the number of seconds between measurements
     * @return a list of Measurement objects
     * @throws ApiException if there is a connection error
     */
    public List<Measurement> getPowerFromMeter(String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel("MeterReader", deviceId, "power", "max", start, end, interval).toHalfHourly().getMaxMeasurements();
    }

    /**
     * Gets the maximum energy measurements between the specified times for the
     * MeterReader device with the specified ID.
     * @param deviceId the ID of the device to get measurements for
     * @param start the start time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param end the end time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param interval the number of seconds between measurements
     * @return a list of Measurement objects
     * @throws ApiException if there is a connection error
     */
    public List<Measurement> getEnergyAdvancesFromMeter(String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel("MeterReader", deviceId, "energy", "max", start, end, interval).getMaxMeasurements();
    }

    /**
     * Gets the maximum temperature measurements between the specified times for
     * the specified device.
     * @param deviceName the name of the device to get measurements for
     * @param deviceId the ID of the device to get measurements for
     * @param start the start time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param end the end time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param interval the number of seconds between measurements
     * @return a list of Measurement objects
     * @throws ApiException if there is a connection error
     */
    public List<Measurement> getAmbientTemperatureFromDevice(String deviceName, String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel(deviceName, deviceId, "temperature", "max", start, end, interval).getMaxMeasurements();
    }

    /**
     * Gets the maximum energy measurements at half hour intervals, between the
     * specified times for the MeterReader device with the specified ID.
     * @param deviceId the ID of the device to get measurements for
     * @param start the start time as a string in the format 'yyyy-MM-dd HH:mm'
     * @param end the end time as a string in the format 'yyyy-MM-dd HH:mm'
     * @return a list of Measurement objects
     * @throws ApiException if there is a connection error
     */
    public List<Measurement> getEnergyMeasurementForDeviceAtHalfHour(String deviceId, String start, String end) throws ApiException {
        return getChannel("MeterReader", deviceId, "energy", "max", start, end, 120)
            .toHalfHourly().getMaxMeasurements();
    }

    /**
     * Gets a list of sensors.
     * @return 
     */
    public List<DeployedSensor> getSensorsForPlatform() {
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
        ohum.setDescription("NetAtmo Indoor Humidity");
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
    
    private AlertMeChannel getChannel(String deviceName, String deviceId, String channelName, String operation, String start, String end, int interval) throws ApiException {
        ApiClient client = new ApiClient("%s/users/%s/hubs/%s/devices/%s/%s/channels/%s",
                urlbase, username, hubid, deviceName, deviceId, channelName);

        try {
            client.data("start", Long.toString(ITLClientUtilities.dateStringToSeconds(start)));
            client.data("end", Long.toString(ITLClientUtilities.dateStringToSeconds(end)));
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("start or end is an invalid date", ex);
        }

        client.cookie(token);
        client.data("interval", Integer.toString(interval));
        client.data("operation", operation);

        ApiResponse response = client.get();
        return response.deserialise(AlertMeChannel.class);
    }
}
