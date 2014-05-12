package com.itl_energy.webclient.instee.itl.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Utility class for holding parameters of an HTTP response.
 *
 * @author Bruce Stephen
 * @date 13th January 2014
 */
public class ITLHTTPResponse {

    protected int code;
    protected String content;
    protected String message;
    protected String type;
    protected byte[] raw;
    protected Map<String, List<String>> headers;

    protected ITLHTTPResponse() {
        this.code = -1;
        this.content = null;
        this.message = null;
        this.headers = null;
        this.raw = null;
    }

    public int getResponseCode() {
        return this.code;
    }

    public String getResponseMessage() {
        return this.message;
    }

    public String getContentType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }

    public byte[] getContentRaw() {
        return this.raw;
    }

    public int getContentLength() {
        return this.raw.length;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public static ITLHTTPResponse getHTTPResponse(HttpURLConnection connection) throws IOException {
        ITLHTTPResponse response = new ITLHTTPResponse();

        response.type = connection.getContentType();
        response.code = connection.getResponseCode();
        response.message = connection.getResponseMessage();
        response.headers = connection.getHeaderFields();

        if (response.code < 300) /*	{
         DataInputStream reader = new DataInputStream(connection.getInputStream());
			
         response.raw=new byte[connection.getContentLength()];
			
         reader.read(response.raw);
			
         response.content=new String(response.raw);
			
         reader.close();
         }
         else*/ {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            response.content = stringBuilder.toString();

            reader.close();
        }

        return response;
    }
}
