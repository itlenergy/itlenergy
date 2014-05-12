package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.Sensor;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class SensorSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public SensorSerialized(Sensor s) {
        this.object = new JSONObject();

        if (s.getSensorId() > 0) {
            this.object.put("sensorId", s.getSensorId());
        }

        this.object.put("description", s.getDescription());
        this.object.put("hubId", s.getHubId());
        this.object.put("typeId", s.getTypeId());
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

    public static Sensor fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return SensorSerialized.fromJSON(map);
    }

    public static Sensor fromJSON(Map<?, ?> map) {
        Sensor sns = new Sensor();

        sns.setSensorId(((Long) map.get("sensorId")).intValue());

        if (map.get("hubId") instanceof LinkedHashMap) {
            sns.setHubId(((Long) ((LinkedHashMap) map.get("hubId")).get("hubId")).intValue());
        }
        else {
            sns.setHubId(((Long) map.get("hubId")).intValue());
        }

        if (map.get("typeId") instanceof LinkedHashMap) {
            sns.setTypeId(((Long) ((LinkedHashMap) map.get("typeId")).get("typeId")).intValue());
        }
        else {
            sns.setTypeId(((Long) map.get("typeId")).intValue());
        }

        sns.setDescription((String) map.get("description"));

        return sns;
    }
}
