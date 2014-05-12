package com.itl_energy.webclient.instee.itl.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.model.DeploymentSite;
import com.itl_energy.webclient.instee.itl.client.model.Hub;
import com.itl_energy.webclient.instee.itl.client.model.Log;
import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.model.MeteredPremises;
import com.itl_energy.webclient.instee.itl.client.model.Sensor;
import com.itl_energy.webclient.instee.itl.client.model.Status;
import com.itl_energy.webclient.instee.itl.client.model.Weather;
import com.itl_energy.webclient.instee.itl.client.resource.DeployedSensorSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.DeploymentSiteSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.HubSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.MeasurementSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.MeteredPremisesSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.SensorSerialized;
import com.itl_energy.webclient.instee.itl.client.resource.WeatherSerialized;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;
import com.itl_energy.webclient.instee.itl.client.util.ITLHTTPResponse;

/**
 * Client interface to the ITL Metering Web Service.
 *
 * @author bstephen
 * @date 10th July 2013
 * @date 29th July 2013
 */
public class ITLWSInterface {

    protected String token;
    protected String sessionExpiry;
    protected String urlbase;

    protected Map<Integer, DeploymentSite> cachedSites;
    protected Map<Integer, MeteredPremises> cachedMeteredPremises;
    protected Map<Integer, MeteredPremises> cachedSensors;
    protected List<DeployedSensor> deployedSensors;

    public ITLWSInterface(String urlbase) {
        //this.urlbase="http://localhost:8282/apatsche-web";
        this.urlbase = urlbase;
        this.token = null;
        this.sessionExpiry = null;
    }

    public boolean beginSession(String user, String password) {
        String path = "/api/auth/login";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};

        String content = "{\"username\":\"" + user + "\",\"password\":\"" + password + "\"}";
        String response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path, args, content).getContent();
        Map<?, ?> session = ITLClientUtilities.parseJSONResponseToMap(response);

        if (session != null) {
            this.token = (String) session.get("ticket");
            this.sessionExpiry = (String) session.get("expires");

            return true;
        }

        return false;
    }

    public List<DeploymentSite> getDeploymentSites() {
        List<DeploymentSite> deploys = new ArrayList<DeploymentSite>();

        String path = "/api/sites";
        String params = "?sgauth=" + this.token;
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> siteList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) siteList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            DeploymentSite ds = DeploymentSiteSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            deploys.add(ds);
        }

        return deploys;
    }

    public int addDeploymentSite(DeploymentSite d) {
        String path = "/api/sites";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        DeploymentSiteSerialized dss = new DeploymentSiteSerialized(d);
        String content = dss.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public List<Weather> getWeatherForDeploymentSite(DeploymentSite d, String start, String finish) {
        List<Weather> weather = new ArrayList<Weather>();
        String begin = start.replace(" ", "%20").replace(":", "%3A");
        String end = finish.replace(" ", "%20").replace(":", "%3A");

        String path = "/api/weather/" + begin + "/" + end + "/";
        String params = "?sgauth=" + this.token;
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> weatherList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) weatherList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Weather w = WeatherSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            weather.add(w);
        }

        return weather;
    }

    public List<Weather> getWeatherForDeploymentSite(DeploymentSite d, String start) {
        List<Weather> weather = new ArrayList<Weather>();

        String path = "/api/weather";
        String params = "?sgauth=" + this.token;
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> weatherList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) weatherList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Weather w = WeatherSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            weather.add(w);
        }

        return weather;
    }

    public int insertWeatherForDeploymentSite(DeploymentSite d, List<Weather> weather) {
        String path = "/api/sites/" + d.getSiteid() + "/weather";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        String content = "{ \"items\": [";

        for (int i = 0; i < weather.size(); i++) {
            WeatherSerialized ws = new WeatherSerialized(weather.get(i));

            content += ws.toJSON();

            if (i < weather.size() - 1) {
                content += ",";
            }
        }

        content += "]}";

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public int insertWeatherForDeploymentSite(DeploymentSite d, Weather weather) {
        String path = "/api/sites/" + d.getSiteid() + "/weather";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        WeatherSerialized ws = new WeatherSerialized(weather);
        String content = ws.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public List<MeteredPremises> getMeteredPremisesForSite(DeploymentSite d) {
        String path = "/api/sites/" + d.getSiteid() + "/houses";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<MeteredPremises> houses = new ArrayList<MeteredPremises>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> houseList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) houseList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            MeteredPremises m = MeteredPremisesSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            houses.add(m);
        }

        return houses;
    }

    /**
     * Adds a metered premises to a given deployment site.
     *
     * @param d the deployment site to add the property to
     * @param m the property to add
     * @return the id of the newly added property
     */
    public int addMeteredPremisesToSite(DeploymentSite d, MeteredPremises m) {
        String path = "/api/houses";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        m.setSiteId(d.getSiteid());

        MeteredPremisesSerialized mps = new MeteredPremisesSerialized(m);
        String content = mps.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public List<DeployedSensor> getDeployedSensorsForSite(DeploymentSite d) {
        String path = "/api/deployedsensors";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<DeployedSensor> dsense = new ArrayList<DeployedSensor>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> sensorList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) sensorList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            DeployedSensor ds = DeployedSensorSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            dsense.add(ds);
        }

        return dsense;
    }

    public int addDeployedSensor(DeployedSensor ds) {
        String path = "/api/deployedsensors";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        DeployedSensorSerialized dss = new DeployedSensorSerialized(ds);
        String content = dss.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public List<Hub> getHubsForMeteredPremises(MeteredPremises m) {
        String path = "/api/houses/" + m.getHouseId() + "/hubs";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<Hub> hlist = new ArrayList<Hub>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> hubList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) hubList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Hub h = HubSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            //filter by m here?
            hlist.add(h);
        }

        return hlist;
    }

    public int addHubToMeteredPremises(MeteredPremises house, Hub hub) {
        String path = "/api/hubs";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        hub.setHouseId(house.getHouseId());

        HubSerialized hs = new HubSerialized(hub);
        String content = hs.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public boolean updateHub(Hub h) {
        String path = "/api/hubs";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        HubSerialized hs = new HubSerialized(h);
        String content = hs.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPutRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            return true;
        }

        return false;
    }

    public List<Log> getLogsForHub(Hub h, String start, String finish) {
        //this should not be here...

        return null;
    }

    public List<Log> getLogsForHub(Hub h, String start) {
        return null;
    }

    public List<Log> getLogsForHub(Hub h) {
        return null;
    }

    public boolean addLogToHub(Hub h, Log l) {
        return false;
    }

    public List<Sensor> getSensorsForHub(Hub h) {
        return this.getSensorsForHub(h.getHubId());
    }

    public List<Sensor> getSensorsForHub(int hid) {
        String path = "/api/hubs/" + hid + "/sensors";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<Sensor> slist = new ArrayList<Sensor>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> senseList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) senseList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Sensor s = SensorSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            //filter by h here?
            slist.add(s);
        }

        return slist;
    }

    public int addSensorToHub(Sensor s, Hub hub) {
        String path = "/api/sensors";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        s.setHubId(hub.getHubId());

        SensorSerialized ss = new SensorSerialized(s);
        String content = ss.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public List<Measurement> getMeasurementsForSensor(Sensor s, String start, String finish) {
        return this.getMeasurementsForSensor(s.getSensorId(), start, finish);
    }

    public List<Measurement> getMeasurementsForSensor(int s, String start, String finish) {
        String begin = start.replace(" ", "%20").replace(":", "%3A");
        String end = finish.replace(" ", "%20").replace(":", "%3A");

        String path = "/api/sensors/" + s + "/measurements/" + begin + "/" + end + "/";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<Measurement> mlist = new ArrayList<Measurement>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);

        if (response == null) {
            return mlist;
        }

        Map<?, ?> measList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) measList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Measurement m = MeasurementSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            //filter by h here?
            mlist.add(m);
        }

        return mlist;
    }

    public List<Measurement> getMeasurementsForSensor(Sensor s, String start) {
        String path = "/api/measurements";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<Measurement> mlist = new ArrayList<Measurement>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> measList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) measList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Measurement m = MeasurementSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            //filter by h here?
            mlist.add(m);
        }

        return mlist;
    }

    public List<Measurement> getMeasurementsForSensor(Sensor s) {
        String path = "/api/measurements";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        List<Measurement> mlist = new ArrayList<Measurement>();

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
        Map<?, ?> measList = ITLClientUtilities.parseJSONResponseToMap(response.getContent());
        LinkedList<?> list = (LinkedList<?>) measList.get("items");

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Measurement m = MeasurementSerialized.fromJSON((LinkedHashMap<?, ?>) object);

            //filter by h here?
            mlist.add(m);
        }

        return mlist;
    }

    public int addMeasurementForSensor(Sensor s, Measurement m) {
        String path = "/api/measurements";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;

        m.setSensorId(s.getSensorId());

        MeasurementSerialized ms = new MeasurementSerialized(m);
        String content = ms.toJSON();
        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPostRequest(this.urlbase + path + params, args, content);

        if (response.getResponseCode() >= 200) {
            String loc = response.getHeaders().get("Location").get(0);

            if (loc != null) {
                return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1));
            }

            return -1;
        }

        return -1;
    }

    public int addMeasurementsForSensor(Sensor s, List<Measurement> m) {
        return this.addMeasurementsForSensor(s.getSensorId(), m);
    }

    public int addMeasurementsForSensor(int sid, List<Measurement> m) {
        String path = "/api/sensors/" + sid + "/measurements";
        String[][] args = {{"Content-Type", "application/json"}, {"Accept", "application/json"}};
        String params = "?sgauth=" + this.token;
        String content = "{ \"items\": [";

        for (int i = 0; i < m.size(); i++) {
            m.get(i).setSensorId(sid);

            MeasurementSerialized ms = new MeasurementSerialized(m.get(i));

            content += ms.toJSON();

            if (i < m.size() - 1) {
                content += ",";
            }
        }

        content += "]}";

        ITLHTTPResponse response = ITLClientUtilities.makeHTTPPutRequest(this.urlbase + path + params, args, content);

        if (response != null) {
            if (response.getResponseCode() == 200) {
                /*String loc=response.getHeaders().get("Location").get(0);
				
                 if(loc!=null)
                 return Integer.parseInt(loc.substring(loc.lastIndexOf('/')+1));*/

                return 0;
            }
        }

        return -1;
    }

    public List<Status> getStatusMessages() {
        return null;
    }

    public boolean addStatusMessage(Status s) {
        return false;
    }
}
