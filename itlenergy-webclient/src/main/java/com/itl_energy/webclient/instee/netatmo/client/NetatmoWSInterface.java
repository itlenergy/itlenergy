package com.itl_energy.webclient.instee.netatmo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;
import com.itl_energy.webclient.instee.itl.client.util.ITLHTTPResponse;

/**
 * Interface to NetAtmo Web Service.
 *
 * @author Bruce Stephen
 * @date 10th September 2013
 * @date 17th October 2013
 * @date 20th October 2013
 */
public class NetatmoWSInterface {

    protected String authtoken;
    protected String refreshtoken;
    protected String user;
    protected String password;
    protected String secret;
    protected String clientid;

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
    public NetatmoWSInterface() {
        this.station2device = new HashMap<String, String>();
        this.device2module = new HashMap<String, String>();
    }

    public boolean authenticateUser(String uname, String pwd, String cid, String secret) {
        this.user = uname;
        this.password = pwd;
        this.clientid = cid;
        this.secret = secret;

        String path = "/oauth2/token";
        String[][] hargs = {{"Content-Type", " application/x-www-form-urlencoded;charset=UTF-8"}, {"Accept", "application/json"}};

        String params = "grant_type=password&client_id=" + this.clientid + "&client_secret=" + this.secret + "&username=" + this.user + "&password=" + this.password;
        String response = ITLClientUtilities.makeHTTPPostRequest(NetatmoWSInterface.urlbase + path, hargs, params).getContent();
        Map<?, ?> auth = ITLClientUtilities.parseJSONResponseToMap(response);

        if (auth.get("access_token") != null) {
            this.authtoken = (String) auth.get("access_token");
            this.refreshtoken = (String) auth.get("refresh_token");

            return true;
        }

        return false;
    }

    public List<String> getDevices() {
        String[][] hargs = {{"Content-Type", " application/x-www-form-urlencoded;charset=UTF-8"}, {"Accept", "application/json"}};
        String path = "/api/devicelist";
        String params = "?access_token=";
        List<String> lst = new ArrayList<String>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params + this.authtoken, hargs);

        if (response.getResponseCode() > 400) {
            return lst;
        }

        LinkedList<?> modules = (LinkedList<?>) ((Map<?, ?>) ITLClientUtilities.parseJSONResponseToMap(response.getContent()).get("body")).get("modules");
        LinkedList<?> devices = (LinkedList<?>) ((Map<?, ?>) ITLClientUtilities.parseJSONResponseToMap(response.getContent()).get("body")).get("devices");

        for (Iterator<?> e = devices.iterator(); e.hasNext();) {
            Map<?, ?> device = (Map<?, ?>) e.next();

            lst.add((String) device.get("station_name"));

            this.station2device.put((String) device.get("station_name"), (String) device.get("_id"));
        }

        for (Iterator<?> e = modules.iterator(); e.hasNext();) {
            Map<?, ?> module = (Map<?, ?>) e.next();

            this.device2module.put((String) module.get("main_device"), (String) module.get("_id"));
        }

        return lst;
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
    public List<Measurement> getIndoorTemperatureMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.INDOOR_TEMPERATURE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getOutdoorTemperatureMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.OUTDOOR_TEMPERATURE_MEASUREMENT, false, this.getModuleIDForDeviceID(device), device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorHumidityMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.INDOOR_HUMIDITY_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getOutdoorHumidityMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.OUTDOOR_HUMIDITY_MEASUREMENT, false, this.getModuleIDForDeviceID(device), device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorNoiseMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.NOISE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorCO2MeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.CO2_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getIndoorPressureMeasurementForDevice(String device, String dateStart, String dateEnd) {
        return this.getMeasurementForDevice(NetatmoWSInterface.PRESSURE_MEASUREMENT, true, null, device, dateStart, dateEnd);
    }

    public List<Measurement> getMeasurementForDevice(String type, boolean indoor, String module, String device, String dateStart, String dateEnd) {
        List<Measurement> measures = new ArrayList<Measurement>();
        String[][] hargs = {{"Content-Type", " application/x-www-form-urlencoded;charset=UTF-8"}, {"Accept", "application/json"}};

        String path = "/api/getmeasure";
        String params = "?access_token=" + this.authtoken;

        params += "&device_id=" + device;

        if (!indoor) {
            params += "&module_id=" + module;
        }

        params += "&type=" + type;
        params += "&scale=" + "30min";
        params += "&date_begin=" + ITLClientUtilities.timeToUTC(dateStart) / 1000L;
        params += "&date_end=" + ITLClientUtilities.timeToUTC(dateEnd) / 1000L;

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(NetatmoWSInterface.urlbase + path + params, hargs);

        if (response.getResponseCode() > 400) {
            return null;
        }

        LinkedList<?> meas = (LinkedList<?>) ITLClientUtilities.parseJSONResponseToMap(response.getContent()).get("body");

        for (Iterator<?> e = meas.iterator(); e.hasNext();) {
            Map<?, ?> obs = (Map<?, ?>) e.next();
            long beg_time = ((Long) obs.get("beg_time")).longValue() * 1000L;
            long step_time = ((Long) obs.get("step_time")).longValue() * 1000L;
            LinkedList<?> values = (LinkedList<?>) obs.get("value");
            int i = 0;

            for (Iterator<?> e2 = values.iterator(); e2.hasNext();) {
                LinkedList<?> tuple = (LinkedList<?>) e2.next();
                Measurement measure = new Measurement();

                measure.setObservationTime(ITLClientUtilities.utcToTime(beg_time + step_time * (i++)));
                measure.setObservation(Float.parseFloat(tuple.get(0).toString()));

                measures.add(measure);
            }
        }

        return measures;
    }

    public static List<DeployedSensor> getSensorsForPlatform() {
        List<DeployedSensor> allsense = new ArrayList<DeployedSensor>();
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
