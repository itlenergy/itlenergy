package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.Weather;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class WeatherSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public WeatherSerialized(Weather w) {
        this.object = new JSONObject();

        if (w.getWeatherObservationId() > 0) {
            this.object.put("weatherObservationId", w.getWeatherObservationId());
        }

        this.object.put("observationTime", w.getObservationTime());
        this.object.put("windSpeed", w.getWindSpeed());
        this.object.put("windDirection", w.getWindDirection());
        this.object.put("ambientTemperature", w.getAmbientTemperature());
        this.object.put("humidity", w.getHumidity());
        this.object.put("uv", w.getUv());
        this.object.put("precipitation", w.getPrecipitation());
        this.object.put("siteId", w.getSiteId());
    }

    public String toJSON() {
        StringWriter out = new StringWriter();

        try {
            this.object.writeJSONString(out);
        }
        catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return out.toString();
    }

    public static Weather fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return WeatherSerialized.fromJSON(map);
    }

    public static Weather fromJSON(Map<?, ?> map) {
        Weather weather = new Weather();

        weather.setObservationTime((String) map.get("observationTime"));
        weather.setWindSpeed((Double) map.get("windSpeed"));
        weather.setWindDirection((Double) map.get("windDirection"));
        weather.setAmbientTemperature((Double) map.get("ambientTemperature"));
        weather.setHumidity((Double) map.get("humidity"));
        weather.setUv((Double) map.get("uv"));
        weather.setPrecipitation((Double) map.get("precipitation"));
        weather.setSiteId((Integer) map.get("siteId"));
        weather.setWeatherObservationId((Integer) map.get("weatherObservationId"));

        return weather;
    }
}
