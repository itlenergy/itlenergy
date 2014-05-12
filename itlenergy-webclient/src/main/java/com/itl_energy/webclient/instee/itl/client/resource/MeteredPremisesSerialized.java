package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.MeteredPremises;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class MeteredPremisesSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public MeteredPremisesSerialized(MeteredPremises mp) {
        this.object = new JSONObject();

        if (mp.getHouseId() > 0) {
            this.object.put("houseId", mp.getHouseId());
        }

        this.object.put("rooms", mp.getRooms());
        this.object.put("floors", mp.getFloors());
        this.object.put("occupants", mp.getOccupants());
        this.object.put("centralHeatingGas", mp.isCentralHeatingGas());
        this.object.put("cookingGas", mp.isCookingGas());
        this.object.put("siteId", mp.getSiteId());
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

    public static MeteredPremises fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return MeteredPremisesSerialized.fromJSON(map);
    }

    public static MeteredPremises fromJSON(Map<?, ?> map) {
        MeteredPremises house = new MeteredPremises();

        house.setHouseId(((Long) map.get("houseId")).intValue());
        house.setRooms(((Long) map.get("rooms")).intValue());
        house.setFloors(((Long) map.get("floors")).intValue());
        house.setOccupants(((Long) map.get("occupants")).intValue());
        house.setCentralHeatingGas((Boolean) map.get("centralHeatingGas"));
        house.setCookingGas((Boolean) map.get("cookingGas"));

        if (map.get("siteId") instanceof LinkedHashMap) {
            house.setSiteId(((Long) ((LinkedHashMap) map.get("siteId")).get("siteId")).intValue());
        }
        else {
            house.setSiteId(((Long) map.get("siteId")).intValue());
        }

        return house;
    }
}
