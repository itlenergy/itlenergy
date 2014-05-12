package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.Measurement;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class MeasurementSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public MeasurementSerialized(Measurement m) {
        this.object = new JSONObject();

        if (m.getMeasurementId() > 0) {
            this.object.put("measurementId", m.getMeasurementId());
        }

        this.object.put("observationTime", m.getObservationTime());
        this.object.put("observation", m.getObservation());
        this.object.put("sensorId", m.getSensorId());
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

    public static Measurement fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return MeasurementSerialized.fromJSON(map);
    }

    public static Measurement fromJSON(Map<?, ?> map) {
        Measurement m = new Measurement();

        m.setMeasurementId(((Long) map.get("measurementId")).intValue());
        m.setObservationTime(((String) map.get("observationTime")).toString());
        m.setObservation((Double) map.get("observation"));

        if (map.get("sensorId") instanceof LinkedHashMap) {
            m.setSensorId(((Long) ((LinkedHashMap) map.get("sensorId")).get("sensorId")).intValue());
        }
        else {
            m.setSensorId(((Long) map.get("sensorId")).intValue());
        }

        return m;
    }
}
