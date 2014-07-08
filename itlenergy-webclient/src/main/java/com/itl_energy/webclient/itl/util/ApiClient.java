/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.itl.util;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
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
     * @throws ApiException
     */
    public ApiResponse get() throws ApiException {
        return getResponse("GET", null, null);
    }

    /**
     * Performs an HTTP POST request, with any data passed to the data method as
     * the request body.
     *
     * @return
     * @throws ApiException
     */
    public ApiResponse post() throws ApiException {
        return post(null);
    }
    
    /**
     * Performs an HTTP POST request. Any data passed to the data method is put
     * in the query string and the supplied requestObject is serialised to JSON
     * and sent in the request body.
     *
     * @param requestObject
     * @return
     * @throws ApiException
     */
    public ApiResponse post(Object requestObject) throws ApiException {
        return post(requestObject, null);
    }

    /**
     * Performs an HTTP POST request. Any data passed to the data method is put
     * in the query string and the supplied requestObject is serialised to JSON
     * and sent in the request body.
     *
     * @param requestObject
     * @param type
     * @return
     * @throws ApiException
     */
    public ApiResponse post(Object requestObject, Type type) throws ApiException {
        return getResponse("POST", requestObject, type);
    }
    
    
    public ApiResponse put(Object requestObject) throws ApiException {
        return getResponse("PUT", requestObject, null);
    }


    private ApiResponse getResponse(String method, Object requestObject, Type type) throws ApiException {
        try {
            URL url;

            //create the URL: if it's a GET request it should include the query params
            if (parameters.size() > 0 && (method.equals("GET") || requestObject != null)) {
                url = new URL(this.url + "?" + serialiseData());
            }
            else {
                url = new URL(this.url);
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //write the headers
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");

            //if it's not a GET request, the data needs to be written to the request
            if (!method.equals("GET")) {
                connection.setDoOutput(true);
                byte[] bytes = null;

                //do we need to send a JSON or a urlencoded request?
                if (requestObject != null) {
                    Gson gson = new Gson();
                    String json;
                    
                    if (type == null)
                        json = gson.toJson(requestObject);
                    else
                        json = gson.toJson(requestObject, type);
                    
                    bytes = json.getBytes();
                }
                else if (parameters.size() > 0) {
                    bytes = serialiseData().getBytes();
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    connection.setRequestProperty("Content-Language", "en-US");
                }

                //if we have a request body now, send it
                if (bytes != null) {
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));

                    try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                        wr.write(bytes);
                        wr.flush();
                    }
                }
            }

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            return new ApiResponse(connection);
        }
        catch (IOException ex) {
            throw new ApiException(ex);
        }
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
