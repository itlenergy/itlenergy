package com.itl_energy.webclient.instee.alertme.client;

import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.util.ApiClient;
import com.itl_energy.webclient.instee.itl.client.util.ApiException;
import com.itl_energy.webclient.instee.itl.client.util.ApiResponse;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;
import java.io.IOException;
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
public class AMSWSInterface2 {

    protected String token;
    protected String hubid;
    protected String username;
    protected String password;
    protected List<String> deviceIds;
    protected Map<String, String> ids2device;

    protected static final String urlbase = "https://api.alertme.com/v5";

    public AMSWSInterface2() {
        this.deviceIds = new ArrayList<>();
        this.ids2device = new HashMap<>();
    }

    public boolean authenticateUser(String username, String password) throws ApiException {
        this.username = username;
        this.password = password;

        try {
            ApiClient client = new ApiClient("%s/login", urlbase);
            client.data("username", username);
            client.data("password", password);
            client.data("caller", "ITL");

            ApiResponse response = client.post();

            if (response.getStatusCode() == 200) {
                Map<String, Object> map = response.deserialise(Map.class);
                List<String> allhubs = (ArrayList<String>) map.get("hubIds");

                this.hubid = allhubs.get(0);
                this.token = "ApiSession=" + (String) map.get("ApiSession");
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException ex) {
            throw new ApiException(ex);
        }
    }

    public AlertMeDevice[] getHubsForUser() throws ApiException {
        try {
            ApiClient client = new ApiClient("%s/users/%s/hubs", urlbase, username);
            client.cookie(token);

            ApiResponse response = client.get();

            if (response.getStatusCode() == 200) {
                return response.deserialise(AlertMeDevice[].class);
            }
            else {
                throw new ApiException(String.format(
                        "Error getting hubs (status code %d).", response.getStatusCode()));
            }
        }
        catch (IOException ex) {
            throw new ApiException(ex);
        }
    }

    public AlertMeDevice[] getDevicesForHub() throws ApiException {
        try {
            ApiClient client = new ApiClient("%s/users/%s/hubs/%s/devices", urlbase, username, this.hubid);
            client.cookie(token);

            ApiResponse response = client.get();

            if (response.getStatusCode() == 200) {
                return response.deserialise(AlertMeDevice[].class);
            }
            else {
                throw new ApiException(String.format(
                        "Error getting hubs (status code %d).", response.getStatusCode()));
            }
        }
        catch (IOException ex) {
            throw new ApiException(ex);
        }
    }

    
    public AlertMeChannel getChannel(String deviceName, String deviceId, String channel, String operation, String start, String end, int interval) throws IOException {
        ApiClient client = new ApiClient("%s/users/%s/hubs/%s/devices/%s/%s/channels/%s",
                urlbase, username, hubid, deviceName, deviceId, channel);

            client.cookie(token);
            client.data("start", "" + (ITLClientUtilities.timeToUTC(start) / 1000));
            client.data("end", "" + (ITLClientUtilities.timeToUTC(end) / 1000));
            client.data("interval", Integer.toString(interval));
            client.data("operation", operation);

            ApiResponse response = client.get();
            return response.deserialise(AlertMeChannel.class);
    }
    
    
    public AlertMeChannel getPowerFromMeter(String deviceId, String start, String end, int interval) throws IOException {
        return getChannel("MeterReader", deviceId, "power", "max+min", start, end, interval).toHalfHourly();
    }
    
    
    public AlertMeChannel getEnergyAdvancesFromMeter(String deviceId, String start, String end, int interval) throws IOException {
        return getChannel("MeterReader", deviceId, "energy", "max+min", start, end, interval);
    }
    
    
    public AlertMeChannel getAllDailyEnergyAdvancesFromMeter(String deviceId) throws IOException {
        //TODO
        return null;
    }
    
    
    public AlertMeChannel getAmbientTemperatureFromDevice(String deviceName, String deviceId, String start, String end, int interval) throws IOException {
        return getChannel(deviceName, deviceId, "temperature", "max+min+average", start, end, interval);
    }
    

    public List<Measurement> getEnergyMeasurementForDeviceAtHalfHour(String deviceid, String dateStart, String dateEnd) throws ApiException {
        try {
            AlertMeChannel channel = getChannel("MeterReader", deviceid, "energy", "max", dateStart, dateEnd, 120)
                .toHalfHourly();
            
            long time = channel.getStart() * 1000;
            
            //get the half hourly results
            List<Measurement> measurements = new ArrayList<>();
            
            for (Double observation: channel.getMax()) {
                String strtime = ITLClientUtilities.utcToTime(time);
                measurements.add(new Measurement(strtime, observation));
                
                time += 1800000;
            }
            
            return measurements;
        }
        catch (IOException ex) {
            throw new ApiException(ex);
        }
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
