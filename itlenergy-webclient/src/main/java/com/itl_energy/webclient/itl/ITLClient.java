package com.itl_energy.webclient.itl;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

import com.itl_energy.webclient.itl.model.Actuations;
import com.itl_energy.webclient.itl.model.ElectricalLoad;
import com.itl_energy.webclient.itl.model.ElectricalLoadForecast;
import com.itl_energy.webclient.itl.model.ForecastStatus;
import com.itl_energy.webclient.itl.model.Generation;
import com.itl_energy.webclient.itl.model.Tariff;
import com.itl_energy.webclient.itl.model.TariffBlock;
import com.itl_energy.webclient.itl.model.WeatherForecast;
import com.itl_energy.webclient.itl.model.DeployedSensor;
import com.itl_energy.webclient.itl.model.DeploymentSite;
import com.itl_energy.webclient.itl.model.Hub;
import com.itl_energy.webclient.itl.model.Items;
import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.model.MeteredPremises;
import com.itl_energy.webclient.itl.model.Sensor;
import com.itl_energy.webclient.itl.model.Weather;
import com.itl_energy.webclient.itl.util.ApiClient;
import com.itl_energy.webclient.itl.util.ApiException;
import com.itl_energy.webclient.itl.util.ApiResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

/**
 * Client interface to the ITL Metering Web Service.
 *
 * @author bstephen
 * @date 10th July 2013
 * @date 29th July 2013
 */
public class ITLClient {

    protected String token;
    protected String sessionExpiry;
    protected String urlbase;

    protected Map<Integer, DeploymentSite> cachedSites;
    protected Map<Integer, MeteredPremises> cachedMeteredPremises;
    protected Map<Integer, MeteredPremises> cachedSensors;
    protected List<DeployedSensor> deployedSensors;

    public ITLClient(String urlbase) {
        this.urlbase = urlbase;
        this.token = null;
        this.sessionExpiry = null;
    }

    /**
     * Begins a new session with the API, authenticating the specified user.
     * This method should be called first as it saves an authentication token
     * which is used by subsequent method calls.
     * @param user the user to authenticate with
     * @param password the password to authenticate with
     * @return true if the authentication succeeded; otherwise, false
     * @throws ApiException if there was a connection error
     */
    public boolean beginSession(String user, String password) throws ApiException {
        ApiClient client = new ApiClient(urlbase + "/auth/login");

        JsonObject login = new JsonObject();
        login.addProperty("username", user);
        login.addProperty("password", password);

        ApiResponse response = client.post(login);

        if (response.success()) {
            JsonObject result = response.deserialise();
            token = result.getAsJsonPrimitive("ticket").getAsString();
            sessionExpiry = result.getAsJsonPrimitive("expires").getAsString();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets the list of deployment sites.
     * @return
     * @throws ApiException if there was a connection error
     */
    public List<DeploymentSite> getDeploymentSites() throws ApiException {
        Type type = new TypeToken<Items<DeploymentSite>>() {}.getType();
        return this.<Items<DeploymentSite>>getResult(type, "/sites").getItems();
    }

    /**
     * Adds a deployment site.
     * @param d The deployment site to add.
     * @return the ID of the added site
     * @throws ApiException if there was a connection error or the site could 
     * not be added
     */
    public int addDeploymentSite(DeploymentSite d) throws ApiException {
        return addObject(d, null, "/sites");
    }

    /**
     * Gets a list of weather observations between the specified times for the 
     * specified site.
     * @param d the 
     * @param start
     * @param finish
     * @return
     * @throws ApiException 
     */
    public List<Weather> getWeatherForDeploymentSite(DeploymentSite d, String start, String finish) throws ApiException {
        try {
            start = URLEncoder.encode(start, "UTF-8");
            finish = URLEncoder.encode(finish, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }

        Type type = new TypeToken<Items<Weather>>() {}.getType();
        return this.<Items<Weather>>getResult(type, "/sites/%d/weather/%s/%s/", d.getSiteid(), start, finish).getItems();
    }

    public List<Weather> getWeatherForDeploymentSite(DeploymentSite d) throws ApiException {
        Type type = new TypeToken<Items<Weather>>() {}.getType();
        return this.<Items<Weather>>getResult(type, "/sites/%d/weather", d.getSiteid()).getItems();
    }

    public void insertWeatherForDeploymentSite(DeploymentSite d, List<Weather> weather) throws ApiException {
        Type type = new TypeToken<Items<Weather>>() {}.getType();
        Items<Weather> items = new Items<>(weather);
        
        ApiResponse response = getClient("/sites/%d/weather", d.getSiteid()).post(items, type);
        
        if (!response.success())
            throw new ApiException("Could not add weather.");
    }

    public int insertWeatherForDeploymentSite(DeploymentSite d, Weather weather) throws ApiException {
        return addObject(weather, null, "/sites/%d/weather", d.getSiteid());
    }

        /**
     * Gets a list of weather observations between the specified times for the 
     * specified site.
     * @param d the 
     * @param start
     * @param finish
     * @return
     * @throws ApiException 
     */
    public List<WeatherForecast> getWeatherForecastForDeploymentSite(DeploymentSite d, String start, String finish) throws ApiException {
        try {
            start = URLEncoder.encode(start, "UTF-8");
            finish = URLEncoder.encode(finish, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }

        Type type = new TypeToken<Items<Weather>>() {}.getType();
        return this.<Items<WeatherForecast>>getResult(type, "/sites/%d/weather_forecast/%s/%s/", d.getSiteid(), start, finish).getItems();
    }

    public List<WeatherForecast> getWeatherForecastForDeploymentSite(DeploymentSite d) throws ApiException {
        Type type = new TypeToken<Items<WeatherForecast>>() {}.getType();
        return this.<Items<WeatherForecast>>getResult(type, "/sites/%d/weather_forecast", d.getSiteid()).getItems();
    }

    public void insertWeatherForecastForDeploymentSite(DeploymentSite d, List<WeatherForecast> weatherf) throws ApiException {
        Type type = new TypeToken<Items<WeatherForecast>>() {}.getType();
        Items<WeatherForecast> items = new Items<>(weatherf);
        
        ApiResponse response = getClient("/sites/%d/weather_forecast", d.getSiteid()).post(items, type);
        
        if (!response.success())
            throw new ApiException("Could not add weather forecast.");
    }

    public int insertWeatherForecastForDeploymentSite(DeploymentSite d, WeatherForecast weatherf) throws ApiException {
        return addObject(weatherf, null, "/sites/%d/weather_forecast", d.getSiteid());
    }
    
    public List<MeteredPremises> getMeteredPremisesForSite(DeploymentSite d) throws ApiException {
        Type type = new TypeToken<Items<MeteredPremises>>() {}.getType();
        return this.<Items<MeteredPremises>>getResult(type, "/sites/%d/houses", d.getSiteid()).getItems();
    }

    public int addMeteredPremisesToSite(DeploymentSite d, MeteredPremises m) throws ApiException {
        m.setSiteId(d.getSiteid());
        return addObject(m, null, "/houses");
    }

    public List<DeployedSensor> getDeployedSensors() throws ApiException {
        Type type = new TypeToken<Items<DeployedSensor>>() {}.getType();
        return this.<Items<DeployedSensor>>getResult(type, "/deployedsensors").getItems();
    }

    public int addDeployedSensor(DeployedSensor ds) throws ApiException {
        return addObject(ds, null, "/deployedsensors");
    }

    public List<Hub> getHubsForMeteredPremises(MeteredPremises m) throws ApiException {
        Type type = new TypeToken<Items<Hub>>() {}.getType();
        return this.<Items<Hub>>getResult(type, "/houses/%d/hubs", m.getHouseId()).getItems();
    }

    public int addHubToMeteredPremises(MeteredPremises house, Hub hub) throws ApiException {
        hub.setHouseId(house.getHouseId());
        return addObject(hub, null, "/hubs");
    }

    public void updateHub(Hub hub) throws ApiException {
        ApiResponse response = getClient("/hubs").put(hub);
        
        if (!response.success())
            throw new ApiException("Could not update hub.");
    }

    public List<Sensor> getSensorsForHub(int hid) throws ApiException {
        Type type = new TypeToken<Items<Sensor>>() {}.getType();
        return this.<Items<Sensor>>getResult(type, "/hubs/%d/sensors", hid).getItems();
    }
    
    public int addSensorToHub(Sensor s, Hub hub) throws ApiException {
        s.setHubId(hub.getHubId());
        return addObject(s, null, "/sensors");
    }

    public List<Measurement> getMeasurementsForSensor(Sensor s, String start, String finish) throws ApiException {
        return this.getMeasurementsForSensor(s.getSensorId(), start, finish);
    }

    public List<Measurement> getMeasurementsForSensor(int s, String start, String finish) throws ApiException {
        try {
            start = URLEncoder.encode(start, "UTF-8");
            finish = URLEncoder.encode(finish, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }
        
        Type type = new TypeToken<Items<Measurement>>() {}.getType();
        return this.<Items<Measurement>>getResult(type, "/sensors/%d/measurements/%s/%s/", s, start, finish).getItems();
    }

    public int addMeasurementForSensor(Sensor s, Measurement m) throws ApiException {
        m.setSensorId(s.getSensorId());
        return addObject(m, null, "/measurements");
    }

    public void addMeasurementsForSensor(Sensor s, List<Measurement> m) throws ApiException {
        this.addMeasurementsForSensor(s.getSensorId(), m);
    }

    public void addMeasurementsForSensor(int sid, List<Measurement> m) throws ApiException {
        Type type = new TypeToken<Items<Measurement>>() {}.getType();
        Items<Measurement> items = new Items<>(m);
        
        ApiResponse response = getClient("/sensors/%d/measurements", sid).post(items, type);
        
        if (!response.success())
            throw new ApiException("Could not add measurements.");
    }
    
    public List<Actuations> getActuationsForSensor(Sensor s, String start, String finish) throws ApiException {
        return this.getActuationsForSensor(s.getSensorId(), start, finish);
    }

    public List<Actuations> getActuationsForSensor(int s, String start, String finish) throws ApiException {
        try {
            start = URLEncoder.encode(start, "UTF-8");
            finish = URLEncoder.encode(finish, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }
        
        Type type = new TypeToken<Items<Actuations>>() {}.getType();
        return this.<Items<Actuations>>getResult(type, "/sensors/%d/actuations/%s/%s/", s, start, finish).getItems();
    }

    public int addActuationsForSensor(Sensor s, Actuations m) throws ApiException {
        m.setSensorId(s.getSensorId());
        return addObject(m, null, "/actuations");
    }

    public void addActuationsForSensor(Sensor s, List<Actuations> m) throws ApiException {
        this.addActuationsForSensor(s.getSensorId(), m);
    }

    public void addActuationsForSensor(int sid, List<Actuations> m) throws ApiException {
        Type type = new TypeToken<Items<Actuations>>() {}.getType();
        Items<Actuations> items = new Items<>(m);
        
        ApiResponse response = getClient("/sensors/%d/actuations", sid).post(items, type);
        
        if (!response.success())
            throw new ApiException("Could not add actuations.");
    }
    
    private int addObject(Object object, Type type, String url, Object... params) throws ApiException {
        ApiResponse response = getClient(url, params).post(object, type);

        if (response.success()) {
            String location = response.getHeaderField("Location");

            if (location != null) {
                return Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));
            }
        }

        throw new ApiException("Could not add object.");
    }
    
    
    private <T> T getResult(Type type, String url, Object... params) throws ApiException {
        ApiResponse response = getClient(url, params).get();
        
        if (!response.success())
            throw new ApiException("Request error.");
        
        return response.deserialise(type);
    }
    
    
    private ApiClient getClient(String url, Object... params) {
        ApiClient client = new ApiClient(urlbase + url, params);
        client.data("sgauth", token);
        
        return client;

    }
}
