/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.itl.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a response from a JSON API.
 * @author stewart
 */
public class ApiResponse {

    private final int statusCode;
    private String response;
    private Map<String, List<String>> headers;
    private JsonObject responseObject;
    
    /**
     * Creates a new response from the connection.
     * @param connection The connection to build the response from.
     * @throws IOException 
     */
    public ApiResponse(HttpURLConnection connection) throws IOException {
        statusCode = connection.getResponseCode();
        
        if (success()) {
            response = readInputStream(connection.getInputStream());
            headers = connection.getHeaderFields();
        }
    }
    
    /**
     * Gets the HTTP status code.
     * @return 
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Determines if the response was successful.
     * @return True if the HTTP status code is in the range 200-299; otherwise,
     * false.
     */
    public boolean success() {
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * Gets the response body.
     * @return 
     */
    public String getResponseBody() {
        return response;
    }
    
    
    /**
     * Gets the value of the header with the specified name if it exists;
     * otherwise, null.
     * @param name
     * @return 
     */
    public String getHeaderField(String name) {
        List<String> values = headers.get(name);
        
        if (values == null)
            return null;
        else
            return values.get(0);
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
    
    
    /**
     * Deserialises the response from JSON into the specified class.
     * @param type The type to deserialise into.
     * @return 
     */
    public <T> T deserialise(Type type) {
        Gson gson = new Gson();
        return gson.fromJson(response, type);
    }
    
    /**
     * Deserialises the response from JSON to JsonObject, so that you can get
     * at the results without a POJO.
     * @return 
     */
    public JsonObject deserialise() {
        if (responseObject == null) {
            JsonParser parser = new JsonParser();
            responseObject = (JsonObject)parser.parse(response);
        }
        
        return responseObject;
    }
    
    /**
     * Deserialises the response from JSON and returns the object represented
     * by the given property.
     * @param <T>
     * @param property
     * @param cls
     * @return 
     */
    public <T> T deserialise(String property, Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(deserialise().getAsJsonObject(property), cls);
    }
    
    /**
     * Deserialises the response from JSON and returns the object represented
     * by the given property.
     * @param <T>
     * @param property
     * @param type
     * @return 
     */
    public <T> T deserialise(String property, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(deserialise().getAsJsonObject(property), type);
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
