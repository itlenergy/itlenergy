package com.itl_energy.webclient.instee.itl.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import org.json.simple.JSONObject;

import com.itl_energy.webclient.instee.itl.client.util.ITLClientUtilities;

public abstract class ResourceSerialized<T> {

    protected JSONObject object;

    @SuppressWarnings("unchecked")
    public ResourceSerialized(T m) {
        this.object = new JSONObject();
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

    public T fromJSON(String json) {
        Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);

        return fromJSON(map);
    }

    public abstract T fromJSON(Map<?, ?> map);
}
