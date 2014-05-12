package com.itl_energy.webclient.instee.alertme.client;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

/**
 * Class for interfacing with AlertMe Web Service to extract metering data.
 *
 * @author Bruce Stephen
 * @version June 27th 2013
 * @version October 23rd 2013
 */
public class AMWSInterface {

    public static final String ARCHIVE_PATH = "D:\\";

    protected static class AlertMeSession {

        protected String hubid;
        protected String apisession;

        public AlertMeSession(String hub, String token) {
            this.hubid = hub;
            this.apisession = token;
        }

        public String getHubID() {
            return this.hubid;
        }

        public String getCookie() {
            return this.apisession;
        }
    }

    protected static abstract class AlertMeObject {

        public static final String ID_KEY = "id";
        public static final String NAME_KEY = "name";

        public abstract Object getId();
    }

    protected static class AlertMeHub extends AlertMeObject {

        protected String user;
        protected long id;

        public AlertMeHub() {
            this.id = -1;
            this.user = "";
        }

        public Object getId() {
            return new Long(this.id);
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUser() {
            return this.user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }

    protected static class AlertMeDevice extends AlertMeObject {

        public static final String NAME_KEY = "name";
        public static final String TYPE_KEY = "type";
        protected String name;
        protected String type;
        protected String id;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    protected static abstract class AlertMeChannel {

        public static String START_KEY = "start";
        public static String END_KEY = "end";
        public static String INTERVAL_KEY = "interval";
        public static String VALUE_KEY = "values";
        public static String AVG_KEY = "average";
        public static String MIN_KEY = "min";
        public static String MAX_KEY = "max";
        protected long start;
        protected long end;
        protected int interval;
        protected double[] average;
        protected double[] max;
        protected double[] min;

        protected AlertMeChannel() {
            this.start = -1;
            this.end = -1;
            this.interval = 0;

            this.average = null;
            this.max = null;
            this.min = null;
        }

        public abstract String getUnits();

        protected void allocateStorage() {
            if (this.start == -1) {
                return;
            }
            if (this.end == -1) {
                return;
            }
            if (this.interval == 0) {
                return;
            }

            int len = this.getLength();

            this.average = new double[len];
            this.max = new double[len];
            this.min = new double[len];
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;

            this.allocateStorage();
        }

        public long getEnd() {
            return this.end;
        }

        public void setEnd(long end) {
            this.end = end;

            this.allocateStorage();
        }

        public int getInterval() {
            return this.interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;

            this.allocateStorage();
        }

        public double[] getAverage() {
            return this.average;
        }

        public double[] getMax() {
            return this.max;
        }

        public double[] getMin() {
            return this.min;
        }

        public int getLength() {
            return (int) (1 + (this.end - this.start) / this.interval);
        }

        public long getUTCTimeAt(int idx) {
            return this.start + idx * this.interval;
        }

        public String getTimestampAt(int idx) {
            return ITLClientUtilities.utcToTime(this.getUTCTimeAt(idx) * 1000);
        }

        public void setMinAt(int idx, double val) {
            this.min[idx] = val;
        }

        public void setAverageAt(int idx, double val) {
            this.average[idx] = val;
        }

        public void setMaxAt(int idx, double val) {
            this.max[idx] = val;
        }

        public double getMinAt(int idx) {
            return this.min[idx];
        }

        public double getAverageAt(int idx) {
            return this.average[idx];
        }

        public double getMaxAt(int idx) {
            return this.max[idx];
        }

        public boolean writeOut(Writer w) throws IOException {
            for (int i = 0; i < this.max.length; i++) {
                w.write(this.getTimestampAt(i) + "," + this.min[i] + "," + this.average[i] + "," + this.max[i] + "\n");
            }

            w.flush();

            return true;
        }

        public String toString() {
            String s = "Channel of readings in " + this.getUnits() + ":";

            s += "\n";
            s += "========================================\n";

            for (int i = 0; i < this.getLength(); i++) {
                s += i + " : " + this.getTimestampAt(i) + " " + this.getMaxAt(i) + " " + this.getAverageAt(i) + " " + this.getMinAt(i) + "\n";
            }

            s += "\n";

            return s;
        }
    }

    protected static class AlertMeTemperatureChannel extends AlertMeChannel {

        public AlertMeTemperatureChannel() {
            super();
        }

        public String getUnits() {
            return "Celsius";
        }
    }

    protected static class AlertMeEnergyChannel extends AlertMeChannel {

        public AlertMeEnergyChannel() {
            super();
        }

        public String getUnits() {
            return "kWh";
        }
    }

    protected static class AlertMePowerChannel extends AlertMeChannel {

        public AlertMePowerChannel() {
            super();
        }

        public String getUnits() {
            return "kW";
        }
    }

    public static List<AlertMeHub> getHubsForUser(String user) {
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs";
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, "");

        if (!response.isEmpty()) {
            List<?> l = ITLClientUtilities.parseJSONResponseToList(response);
            ArrayList<AlertMeHub> hubs = new ArrayList<AlertMeHub>();

            for (int i = 0; i < l.size(); i++) {
                AlertMeHub hb = new AlertMeHub();

                hb.setId(Long.parseLong((String) ((Map<?, ?>) l.get(i)).get(AlertMeHub.ID_KEY)));
                hb.setUser((String) ((Map<?, ?>) l.get(i)).get(AlertMeHub.NAME_KEY));

                hubs.add(hb);
            }

            return hubs;
        }

        return null;
    }

    public static List<AlertMeDevice> getDevicesForHub(String user, String hub, String cookie) {
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices";
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            List<?> lst = ITLClientUtilities.parseJSONResponseToList(response);
            ArrayList<AlertMeDevice> devs = new ArrayList<AlertMeDevice>();

            for (int i = 0; i < lst.size(); i++) {
                AlertMeDevice dv = new AlertMeDevice();

                dv.setId((String) ((Map<?, ?>) lst.get(i)).get(AlertMeDevice.ID_KEY));
                dv.setType((String) ((Map<?, ?>) lst.get(i)).get(AlertMeDevice.TYPE_KEY));
                dv.setName((String) ((Map<?, ?>) lst.get(i)).get(AlertMeDevice.NAME_KEY));

                devs.add(dv);
            }

            return devs;
        }

        return null;
    }

    public static AlertMePowerChannel getPowerFromMeter(String user, String hub, String meterid, String start, String end, int interval, String cookie) {
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(start) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(end) / 1000 + "&interval=" + interval + "&operation=max+min";
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices/MeterReader/" + meterid + "/channels/power" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            AlertMePowerChannel chan = new AlertMePowerChannel();

            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");

            //need to offset this to align with the hour/half hour
            long resid = begin.longValue() % 1800L;
            int offset = 0;

            if (resid == 0) {
                chan.setStart(begin.longValue());
            }
            else {
                chan.setStart(begin.longValue() + 1800L - resid);
                offset = (int) ((1800L - resid) / ival.longValue());
            }

            chan.setEnd(nd.longValue());
            chan.setInterval((int) ival.longValue()/**
             * 15
             */
            );

            for (int i = offset; i < max.size(); i += 1/*5*/) {
                if (max.get(i) instanceof Double) {
                    chan.setMaxAt((i/*-offset*/)/*/15*/, ((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    chan.setMaxAt((i/*-offset*/)/*/15*/, ((Long) max.get(i)).longValue());
                }
            }

            return chan;
        }

        return null;
    }

    public static AlertMeEnergyChannel getEnergyAdvancesFromMeter(String user, String hub, String meterid, String start, String end, int interval, String cookie) {
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(start) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(end) / 1000 + "&interval=" + interval + "&operation=max+min";
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices/MeterReader/" + meterid + "/channels/energy" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            AlertMeEnergyChannel chan = new AlertMeEnergyChannel();

            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");

            //need to offset this to align with the hour/half hour
            long resid = begin.longValue() % 1800L;
            int offset = 0;

            if (resid == 0) {
                chan.setStart(begin.longValue());
            }
            else {
                chan.setStart(begin.longValue() + 1800L - resid);
                offset = (int) ((1800L - resid) / ival.longValue());
            }

            chan.setEnd(nd.longValue());
            chan.setInterval((int) ival.longValue() * 15);//not sure if this is needed now...

            for (int i = offset; i < max.size(); i += 15) {
                if (max.get(i) instanceof Double) {
                    chan.setMaxAt((i - offset) / 15, ((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    chan.setMaxAt((i - offset) / 15, ((Long) max.get(i)).longValue());
                }
            }

            return chan;
        }

        return null;
    }

    public static AlertMeEnergyChannel getHiResEnergyAdvancesFromMeter(String user, String hub, String meterid, String start, String end, String cookie) {
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(start) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(end) / 1000 + "&interval=" + 120 + "&operation=max+min";
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices/MeterReader/" + meterid + "/channels/energy" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            AlertMeEnergyChannel chan = new AlertMeEnergyChannel();

            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");

            chan.setStart(begin.longValue());

            chan.setEnd(nd.longValue());
            chan.setInterval((int) ival.longValue());//not sure if this is needed now...

            for (int i = 0; i < max.size(); i++) {
                if (max.get(i) instanceof Double) {
                    chan.setMaxAt(i, ((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    chan.setMaxAt(i, ((Long) max.get(i)).longValue());
                }
            }

            return chan;
        }

        return null;
    }

    public static AlertMeEnergyChannel getAllDailyEnergyAdvancesFromMeter(String user, String hub, String meterid, String cookie) {
        String alertMeArgs = "?start=0&end=" + ITLClientUtilities.nowAsUTC() / 1000 + "&interval=86400&operation=max";
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices/MeterReader/" + meterid + "/channels/energy" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            AlertMeEnergyChannel chan = new AlertMeEnergyChannel();

            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");

            //need to offset this to align with the hour/half hour
            long resid = begin.longValue() % 86400L;
            int offset = 0;

            chan.setStart(begin.longValue());

            /*
             if(resid==0)
             chan.setStart(begin.longValue());
             else
             {
             chan.setStart(begin.longValue()+86400L-resid);
             offset=(int)((86400L-resid)/ival.longValue());
             }
             */
            chan.setEnd(nd.longValue());
            chan.setInterval((int) ival.longValue());

            /*
             for(int i=offset;i<max.size();i+=15)
             {
             if(max.get(i) instanceof Double)
             chan.setMaxAt((i-offset)/15,((Double)max.get(i)).doubleValue());
             if(max.get(i) instanceof Long)
             chan.setMaxAt((i-offset)/15,((Long)max.get(i)).longValue());
             }
             */
            for (int i = offset; i < max.size(); i++) {
                if (max.get(i) instanceof Double) {
                    chan.setMaxAt(i, ((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    chan.setMaxAt(i, ((Long) max.get(i)).longValue());
                }
            }

            return chan;
        }

        return null;
    }

    public static AlertMeTemperatureChannel getAmbientTemperatureFromDevice(String user, String hub, String deviceid, String devicename, String start, String end, int interval, String cookie) {
        String alertMeArgs = "?start=" + ITLClientUtilities.timeToUTC(start) / 1000 + "&end=" + ITLClientUtilities.timeToUTC(end) / 1000 + "&interval=" + interval + "&operation=max+min+average";
        String alertMeURL = "https://api.alertme.com/v5/" + "users/" + user + "/hubs/" + hub + "/devices/" + devicename + "/" + deviceid + "/channels/temperature" + alertMeArgs;
        String response = ITLClientUtilities.makeHTTPSGetRequest(alertMeURL, cookie);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            AlertMeTemperatureChannel chan = new AlertMeTemperatureChannel();

            Long begin = (Long) mp.get("start");
            Long nd = (Long) mp.get("end");
            Long ival = (Long) mp.get("interval");
            Map<?, ?> vals = (Map<?, ?>) mp.get("values");
            LinkedList<?> max = (LinkedList<?>) vals.get("max");
            LinkedList<?> min = (LinkedList<?>) vals.get("min");
            LinkedList<?> avg = (LinkedList<?>) vals.get("average");

            long resid = begin.longValue() % 1800L;
            int offset = 0;

            if (resid == 0) {
                chan.setStart(begin.longValue());
            }
            else {
                chan.setStart(begin.longValue() + 1800L - resid);
                offset = (int) ((1800L - resid) / ival.longValue());
            }

            chan.setEnd(nd.longValue());
            chan.setInterval((int) ival.longValue() * 15);

            for (int i = offset; i < min.size(); i += 15) {
                if (min.get(i) instanceof Double) {
                    chan.setMinAt((i - offset) / 15, ((Double) min.get(i)).doubleValue());
                }
                if (min.get(i) instanceof Long) {
                    chan.setMinAt((i - offset) / 15, ((Long) min.get(i)).longValue());
                }
            }

            for (int i = offset; i < max.size(); i += 15) {
                if (max.get(i) instanceof Double) {
                    chan.setMaxAt((i - offset) / 15, ((Double) max.get(i)).doubleValue());
                }
                if (max.get(i) instanceof Long) {
                    chan.setMaxAt((i - offset) / 15, ((Long) max.get(i)).longValue());
                }
            }

            for (int i = offset; i < avg.size(); i += 15) {
                if (avg.get(i) instanceof Double) {
                    chan.setAverageAt((i - offset) / 15, ((Double) avg.get(i)).doubleValue());
                }
                if (avg.get(i) instanceof Long) {
                    chan.setAverageAt((i - offset) / 15, ((Long) avg.get(i)).longValue());
                }
            }

            return chan;
        }

        return null;
    }

    public static void listEquipmentForUser(String user) {
        List<AlertMeHub> hubList = AMWSInterface.getHubsForUser(user);
        String hubid = (String) hubList.get(0).getId().toString();
        List<AlertMeDevice> devList = AMWSInterface.getDevicesForHub(user, hubid, "");

        System.out.print(user + "," + hubid);

        for (int i = 0; i < devList.size(); i++) {
            System.out.print("," + devList.get(i).getType());
        }

        System.out.println();
    }

    public static AlertMeSession loginAndInitialiseSession(String user, String password) {
        AlertMeSession ams = null;
        String alertMeArgs = "username=" + user + "&password=" + password + "&caller=ITL";
        String alertMeURL = "https://api.alertme.com/v5" + "/login";
        String response = ITLClientUtilities.makeHTTPSPostRequest(alertMeURL, alertMeArgs);

        if (!response.isEmpty()) {
            Map<?, ?> mp = ITLClientUtilities.parseJSONResponseToMap(response);
            LinkedList<?> allhubs = (LinkedList<?>) mp.get("hubIds");
            String cookie = (String) mp.get("ApiSession");
            Object hubid = allhubs.getFirst();
            ams = new AlertMeSession(hubid.toString(), cookie);
        }

        return ams;
    }

    public static boolean logoutAndCloseSession(String cookie) {
        String alertMeURL = "https://api.alertme.com/v5/" + "logout";
        String response = ITLClientUtilities.makeHTTPSPostRequest(alertMeURL, cookie);

        System.err.println(response);

        return !response.contains("40");
    }

    public List<Measurement> getMeasurementForDevice(String type, boolean indoor, String module, String device, String dateStart, String dateEnd) {
        List<Measurement> measures = new ArrayList<Measurement>();
        /*
         String[][] hargs={{"Content-Type"," application/x-www-form-urlencoded;charset=UTF-8"},{"Accept","application/json"}};
		
         String path="/api/getmeasure";
         String params="?access_token="+this.authtoken;
		
         params+="&device_id="+device;
		
         if(!indoor)
         params+="&module_id="+module;
		
         params+="&type="+type;
         params+="&scale="+"30min";
         params+="&date_begin="+ITLClientUtilities.timeToUTC(dateStart)/1000L;
         params+="&date_end="+ITLClientUtilities.timeToUTC(dateEnd)/1000L;
		
         String response=ITLClientUtilities.makeHTTPGetRequest(NetatmoWSInterface.urlbase+path+params,hargs);
		
         LinkedList<?> meas=(LinkedList<?>)ITLClientUtilities.parseJSONResponseToMap(response).get("body");
		
         for(Iterator<?> e=meas.iterator();e.hasNext();)
         {
         Map<?,?> obs=(Map<?,?>)e.next();
         long beg_time=((Long)obs.get("beg_time")).longValue()*1000L;
         long step_time=((Long)obs.get("step_time")).longValue()*1000L;
         LinkedList<?> values=(LinkedList<?>)obs.get("value");
         int i=0;
			
         for(Iterator<?> e2=values.iterator();e2.hasNext();)
         {
         LinkedList<?> tuple=(LinkedList<?>)e2.next();
         Measurement measure=new Measurement();
				
         measure.setObservationTime(ITLClientUtilities.utcToTime(beg_time+step_time*(i++)));
         measure.setObservation(Float.parseFloat(tuple.get(0).toString()));
				
         measures.add(measure);
         }
         }
         */
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
