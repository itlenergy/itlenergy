package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.Hub;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class HubSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public HubSerialized(Hub h) {
        this.object = new JSONObject();

        if (h.getHubId() > 0) {
            this.object.put("hubId", h.getHubId());
        }

        this.object.put("lastUpdate", h.getLastUpdate().getTime());
        this.object.put("freeStorage", h.getFreeStorage());
        this.object.put("houseId", h.getHouseId());
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

    public static Hub fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return HubSerialized.fromJSON(map);
    }

    public static Hub fromJSON(Map<?, ?> map) {
        Hub hb = new Hub();

        hb.setHubId(((Long) map.get("hubId")).intValue());
        hb.setLastUpdate(new Date((Long) map.get("lastUpdate")));
        hb.setFreeStorage(((Long) map.get("freeStorage")).intValue());

        if (map.get("houseId") instanceof LinkedHashMap) {
            hb.setHouseId(((Long) ((LinkedHashMap) map.get("houseId")).get("houseId")).intValue());
        }
        else {
            hb.setHouseId(((Long) map.get("houseId")).intValue());
        }

        return hb;
    }
}
