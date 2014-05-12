package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.model.DeploymentSite;
import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public class DeploymentSiteSerialized {

    private JSONObject object;

    @SuppressWarnings("unchecked")
    public DeploymentSiteSerialized(DeploymentSite d) {
        this.object = new JSONObject();

        if (d.getSiteid() > 0) {
            this.object.put("siteId", d.getSiteid());
        }

        this.object.put("siteName", d.getName());
        this.object.put("altitude", d.getAltitude());
        this.object.put("latitude", d.getLatitude());
        this.object.put("longitude", d.getLongitude());
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

    public static DeploymentSite fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return DeploymentSiteSerialized.fromJSON(map);
    }

    public static DeploymentSite fromJSON(Map<?, ?> map) {
        DeploymentSite site = new DeploymentSite();

        site.setSiteid(((Long) map.get("siteId")).intValue());
        site.setName((String) map.get("siteName"));
        site.setAltitude((Double) map.get("altitude"));
        site.setLatitude((Double) map.get("latitude"));
        site.setLongitude((Double) map.get("longitude"));

        return site;
    }
}
