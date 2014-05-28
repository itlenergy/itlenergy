package com.itl_energy.webclient.alertme.client;

import com.itl_energy.webclient.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.itl.client.model.Measurement;
import com.itl_energy.webclient.itl.client.util.ApiClient;
import com.itl_energy.webclient.itl.client.util.ApiException;
import com.itl_energy.webclient.itl.client.util.ApiResponse;
import com.itl_energy.webclient.itl.client.util.ITLClientUtilities;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Refactored interface to the AlertMe web service
 *
 * @author Bruce Stephen
 * @date 25th October 2013
 */
public class AMSWSInterface {

    protected String token;
    protected String hubid;
    protected String username;
    protected String password;

    protected List<String> deviceIds;
    protected Map<String, String> ids2device;

    protected static final String urlbase = "https://api.alertme.com/v5";

    public AMSWSInterface() {
        this.deviceIds = new ArrayList<>();
        this.ids2device = new HashMap<>();
    }

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

    public List<String> getHubsForUser() throws ApiException {
        AlertMeDevice[] devices = getResponse(AlertMeDevice[].class, "/users/%s/hubs", username);
        List<String> hubs = new ArrayList<>();

        for (AlertMeDevice device : devices) {
            hubs.add(device.getID());
        }

        return hubs;
    }

    public Map<String, String> getDevicesForHub() throws ApiException {
        AlertMeDevice[] devices = getResponse(AlertMeDevice[].class,
                "%s/users/%s/hubs/%s/devices", username, this.hubid);

        Map<String, String> map = new HashMap<>();

        for (AlertMeDevice device : devices) {
            map.put(device.getID(), device.getType());
        }

        return map;
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

    public List<Measurement> getPowerFromMeter(String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel("MeterReader", deviceId, "power", "max+min", start, end, interval).toHalfHourly().getMaxMeasurements();
    }

    public List<Measurement> getEnergyAdvancesFromMeter(String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel("MeterReader", deviceId, "energy", "max+min", start, end, interval).getMaxMeasurements();
    }

    public List<Measurement> getAmbientTemperatureFromDevice(String deviceName, String deviceId, String start, String end, int interval) throws ApiException {
        return getChannel(deviceName, deviceId, "temperature", "max+min+average", start, end, interval).getMaxMeasurements();
    }

    public List<Measurement> getEnergyMeasurementForDeviceAtHalfHour(String deviceid, String dateStart, String dateEnd) throws ApiException {
        AlertMeChannel channel = getChannel("MeterReader", deviceid, "energy", "max", dateStart, dateEnd, 120)
                .toHalfHourly();

        long time = channel.getStart() * 1000;

        //get the half hourly results
        List<Measurement> measurements = new ArrayList<>();

        for (Double observation : channel.getMax()) {
            String strtime = ITLClientUtilities.millisecondsToDateString(time);
            measurements.add(new Measurement(strtime, observation));

            time += 1800000;
        }

        return measurements;
    }

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
}
