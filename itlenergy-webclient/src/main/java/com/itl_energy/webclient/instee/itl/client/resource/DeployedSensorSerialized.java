package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.DeployedSensor;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class DeployedSensorSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public DeployedSensorSerialized(DeployedSensor d) {
        this.object = new JSONObject();

        if (d.getTypeid() > 0) {
            this.object.put("typeId", d.getTypeid());
        }

        this.object.put("description", d.getDescription());
        this.object.put("measurementUnits", d.getMeasurementUnits());
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

    public static DeployedSensor fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return DeployedSensorSerialized.fromJSON(map);
    }

    public static DeployedSensor fromJSON(Map<?, ?> map) {
        DeployedSensor dsns = new DeployedSensor();

        dsns.setTypeid(((Long) map.get("typeId")).intValue());
        dsns.setDescription((String) map.get("description"));
        dsns.setMeasurementUnits((String) map.get("measurementUnits"));

        return dsns;
    }
}
