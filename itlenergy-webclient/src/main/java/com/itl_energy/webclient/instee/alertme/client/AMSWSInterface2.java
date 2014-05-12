package com.itl_energy.webclient.instee.alertme.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.instee.alertme.client.AMWSInterface.AlertMeHub;
import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

/**
 * Refactored interface to the AlertMe web service
 *
 * @author Bruce Stephen
 * @date 25th October 2013
 */
public class AMSWSInterface2 {

    protected String token;
    protected String hubid;
    protected String user;
    protected String password;
    protected List<String> deviceIds;
    protected Map<String, String> ids2device;

    protected static final String urlbase = "https://api.alertme.com/v5";

    public AMSWSInterface2() {
        this.deviceIds = new ArrayList<String>();
        this.ids2device = new HashMap<String, String>();
    }

    public boolean authenticateUser(String uname, String pwd) {
        this.user = uname;
        this.password = pwd;

        String alertMeArgs = "username=" + this.user + "&password=" + this.password + "&caller=ITL";
        String alertMeURL = AMSWSInterface2.urlbase + "/login";
        String response = ITLClientUtilities.makeHTTPSPostRequest(alertMeURL, alertMeArgs);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            LinkedList<?> allhubs = (LinkedList<?>) mp.get("hubIds");//
            String cookie = (String) mp.get("ApiSession");

            this.hubid = allhubs.getFirst().toString();
            this.token = "ApiSession=" + cookie;

            return true;
        }

        return false;
    }

    public List<String> getHubsForUser() {
        String alertMeURL = AMSWSInterface2.urlbase + "/users/" + this.user + "/hubs";
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, this.token);
        List<String> hubs = new ArrayList<String>();

        if (!response.isEmpty()) {
            List<?> l = ITLClientUtilities.parseJSONResponseToList(response);

            for (int i = 0; i < l.size(); i++) {
                hubs.add((String) ((Map<?, ?>) l.get(i)).get(AlertMeHub.ID_KEY));
            }

            return hubs;
        }

        return null;
    }

    public Map<String, String> getDevicesForHub() {
        String alertMeURL = AMSWSInterface2.urlbase + "/users/" + this.user + "/hubs/" + this.hubid + "/devices";
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, this.token);
        Map<String, String> devs = new HashMap<String, String>();
        Map<String, String> desc = new HashMap<String, String>();

        if (!response.isEmpty()) {
            List<?> lst = ITLClientUtilities.parseJSONResponseToList(response);

            for (int i = 0; i < lst.size(); i++) {
                devs.put((String) ((Map<?, ?>) lst.get(i)).get("id"), (String) ((Map<?, ?>) lst.get(i)).get("type"));
                desc.put((String) ((Map<?, ?>) lst.get(i)).get("id"), (String) ((Map<?, ?>) lst.get(i)).get("name"));
            }

            return devs;
        }

        return null;
    }

    /**
     * Generic function for extracting measurements.
     *
     */
    public List<Measurement> getMeasurementForDevice(String channel, String deviceid, String devicename, int interval, String dateStart, String dateEnd) {
        List<Measurement> measures = new ArrayList<Measurement>();
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(dateStart) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(dateEnd) / 1000 + "&interval=" + interval + "&operation=max+min";
        String alertMeURL = AMSWSInterface2.urlbase + "/users/" + this.user + "/hubs/" + this.hubid + "/devices/" + devicename + "/" + deviceid + "/channels/" + channel + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, this.token);

        return measures;
    }

    public List<Measurement> getEnergyMeasurementForDeviceAtHalfHour(String deviceid, String dateStart, String dateEnd) {
        List<Measurement> measures = new ArrayList<Measurement>();
        int interval = 120;
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(dateStart) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(dateEnd) / 1000 + "&interval=" + interval + "&operation=max+min";
        String alertMeURL = AMSWSInterface2.urlbase + "/users/" + this.user + "/hubs/" + this.hubid + "/devices/MeterReader/" + deviceid + "/channels/energy" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, this.token);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");

            //need to offset this to align with the hour/half hour
            long resid = begin.longValue() % 1800L;
            int offset = 0;

            if (resid != 0) {
                offset = (int) ((1800L - resid) / ival.longValue());
            }

            for (int i = offset; i < max.size(); i += 15) {
                Measurement measure = new Measurement();

                measure.setObservationTime(ITLClientUtilities.utcToTime((begin.longValue() * 1000L) + (resid + interval * i) * 1000L));

                if (max.get(i) instanceof Double) {
                    measure.setObservation(((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    measure.setObservation(((Long) max.get(i)).longValue());
                }

                measures.add(measure);
            }

            return measures;
        }

        return null;
    }

    public List<DeployedSensor> getSensorsForPlatform() {
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
