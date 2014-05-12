/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.instee.itl.client.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for connecting to a JSON API.
 *
 * @author stewart
 */
public class ApiClient {

    private String url;
    private Map<String, String> parameters;
    private Map<String, String> headers;

    /**
     * Constructor.
     *
     * @param url The URL to connect to.
     */
    public ApiClient(String url) {
        this.url = url;
        parameters = new HashMap<>();
        headers = new HashMap<>();
    }

    /**
     * Constructor.
     *
     * @param urlTemplate The URL to connect to, containing sprintf style args.
     * @param args The arguments to fill in the template with.
     */
    public ApiClient(String urlTemplate, Object... args) {
        this(String.format(urlTemplate, args));
    }

    /**
     * Set data for the request.
     *
     * @param key
     * @param value
     */
    public void data(String key, String value) {
        parameters.put(key, value);
    }

    /**
     * Sets a request property (header) for the request.
     *
     * @param key
     * @param value
     */
    public void header(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Sets the Cookie header for the request.
     *
     * @param value
     */
    public void cookie(String value) {
        header("Cookie", value);
    }

    /**
     * Performs an HTTP GET request.
     *
     * @return
     * @throws IOException
     */
    public ApiResponse get() throws IOException {
        return getResponse("GET");
    }

    /**
     * Performs an HTTP POST request, with any data passed to the data method as
     * the request body.
     *
     * @return
     * @throws IOException
     */
    public ApiResponse post() throws IOException {
        return getResponse("POST");
    }

    private ApiResponse getResponse(String method) throws IOException {
        String data = serialiseData();
        URL url;

        //create the URL: if it's a GET request it should include the query params
        if (!data.isEmpty() && method.equals("GET")) {
            url = new URL(this.url + "?" + data);
        }
        else {
            url = new URL(this.url);
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        //write the headers
        for (Map.Entry<String, String> header: headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        
        connection.setRequestMethod(method);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        //if it's not a GET request, the data needs to be written to the request
        if (!data.isEmpty() && !method.equals("GET")) {
            byte[] bytes = data.getBytes();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(bytes.length));
            connection.setRequestProperty("Content-Language", "en-US");

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(bytes);
                wr.flush();
            }
        }

        // give it 2 minutes to respond
        connection.setReadTimeout(120 * 1000);

        return new ApiResponse(connection);
    }

    
    private String serialiseData() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }

        return sb.toString();
    }
}
