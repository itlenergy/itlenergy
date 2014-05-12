/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.instee.itl.client.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Encapsulates a response from a JSON API.
 * @author stewart
 */
public class ApiResponse {

    private final int responseCode;
    private final String response;

    /**
     * Creates a new response from the connection.
     * @param connection The connection to build the response from.
     * @throws IOException 
     */
    public ApiResponse(HttpURLConnection connection) throws IOException {
        responseCode = connection.getResponseCode();
        response = readInputStream(connection.getInputStream());
    }
    
    /**
     * Gets the HTTP status code.
     * @return 
     */
    public int getStatusCode() {
        return responseCode;
    }
    
    /**
     * Gets the response body.
     * @return 
     */
    public String getResponseBody() {
        return response;
    }
    
    /**
     * Deserialises the response from JSON into the specified class.
     * @param cls The class to deserialise into.
     * @return 
     */
    public <T> T deserialise(Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(response, cls);
    }
    
    
    private String readInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
