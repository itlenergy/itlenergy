package com.itl_energy.webclient.instee.itl.client.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility functions for accessing the ITL metering web service.
 *
 * @author bstephen
 * @date 10th July 2013
 */
public class ITLClientUtilities {

    public static long timeToUTC(String timestamp) {
        return ITLClientUtilities.timeToUTC(timestamp, "yyyy-MM-dd HH:mm");
    }

    public static long timeToUTC(String timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = sdf.parse(timestamp);
            cal1.setTime(beg);

            return cal1.getTimeInMillis();
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static long nowAsUTC() {
        Calendar cal1 = Calendar.getInstance();

        return cal1.getTimeInMillis();
    }

    public static String utcToTime(long utc) {
        return ITLClientUtilities.utcToTime(utc, "yyyy-MM-dd HH:mm:SS");
    }

    public static String utcToTime(long utc, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        cal1.setTimeInMillis(utc);
        beg = cal1.getTime();

        return sdf.format(beg);
    }

    public static ITLHTTPResponse makeHTTPPutRequest(String urlstrng, String[][] properties, String content) {
        URL url = null;
        ITLHTTPResponse response = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            connection.setRequestProperty("Content-Language", "en-US");

            if (content != null) {
                connection.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            if (content != null) {
                wr.writeBytes(content);
            }

            wr.flush();
            wr.close();

            response = ITLHTTPResponse.getHTTPResponse(connection);
        }
        catch (Exception e) {
            e.printStackTrace();

            return response;
        }

        return response;
    }

    public static String makeHTTPSPutRequest(String urlstrng, String args) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(args.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(args);
            wr.flush();
            wr.close();

			//connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }
    /*
     public static String makeHTTPPutRequest(String urlstrng,String[][] properties,String content)
     {
     URL url = null;
     BufferedReader reader = null;
     StringBuilder stringBuilder = new StringBuilder();
     HttpURLConnection connection=null;
		
     try
     {
     url = new URL(urlstrng);
			
     connection = (HttpURLConnection) url.openConnection();
			
     connection.setRequestMethod("PUT");
			
     for(int i=0;i<properties.length;i++)
     connection.setRequestProperty(properties[i][0],properties[i][1]);
			
     connection.setRequestProperty("Content-Language","en-US");
		    
     if(content!=null)		
     connection.setRequestProperty("Content-Length",""+Integer.toString(content.getBytes().length));
		    
     connection.setUseCaches (false);
     connection.setDoInput(true);
     connection.setDoOutput(true);

     // give it 2 minutes to respond
     connection.setReadTimeout(120*1000);
		 			
     //Send request
     DataOutputStream wr=new DataOutputStream(connection.getOutputStream());
		    
     if(content!=null)
     wr.writeBytes(content);
		    
     wr.flush();
     wr.close();
		    
     //connection.connect();
									
     if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
     {
     System.out.println(urlstrng);
     System.out.println(connection.getResponseCode());
     System.out.println(connection.getResponseMessage());
				
     return "";
     }
			
     // read the output from the server
     reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
     String line = null;
			
     while ((line = reader.readLine()) != null)
     stringBuilder.append(line + "\n");
     }
     catch (Exception e)
     {
     e.printStackTrace();
			
     return "";
     }
     finally
     {
     if (reader != null)
     {
     try
     {
     reader.close();
     }
     catch (IOException ioe)
     {
     ioe.printStackTrace();
     }
     }
     }
		
     return stringBuilder.toString();
     }
     */

    public static String makeHTTPSPutRequest(String urlstrng, String[][] properties, String content) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            connection.setRequestProperty("Content-Language", "en-US");

            if (content != null) {
                connection.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            if (content != null) {
                wr.writeBytes(content);
            }

            wr.flush();
            wr.close();

			//connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }

    public static String makeHTTPSPostRequest(String urlstrng, String args) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(args.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(args);
            wr.flush();
            wr.close();

			//connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }

    public static String makeHTTPSPostRequest(String urlstrng, String[][] properties, String content) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            connection.setRequestProperty("Content-Language", "en-US");

            if (content != null) {
                connection.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            if (content != null) {
                wr.writeBytes(content);
            }

            wr.flush();
            wr.close();

			//connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }

    public static String makeHTTPPostRequest(String urlstrng, String args) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(args.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(args);
            wr.flush();
            wr.close();

			//connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }

    public static ITLHTTPResponse makeHTTPPostRequest(String urlstrng, String[][] properties, String content) {
        URL url = null;
        ITLHTTPResponse response = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            connection.setRequestProperty("Content-Language", "en-US");

            if (content != null) {
                connection.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            if (content != null) {
                wr.writeBytes(content);
            }

            wr.flush();
            wr.close();

            response = ITLHTTPResponse.getHTTPResponse(connection);
        }
        catch (Exception e) {
            e.printStackTrace();

            return response;
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return /*stringBuilder.toString()*/ response;
    }

    /**
     * Forms and sends an HTTP GET request to the given URL.
     *
     * @param urlstrng - the URL to post the request to
     * @param cookie - the authentication cookie
     * @return content resulting from response. TODO - this needs to change to a
     * data structure containing the reposne, the code, the content and the
     * headers.
     */
    public static String makeHTTPSGetRequest(String urlstrng, String cookie) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestMethod("GET");

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        /*try
         {
         Thread.sleep(4000);
         }
         catch(Exception e)
         {
         //do nothing...
         }*/
        return stringBuilder.toString();
    }

    public static String makeHTTPSGetRequest(String urlstrng, String[][] properties) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection connection = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlstrng);
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());

                return "";
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }
    /*
     public static String makeHTTPGetRequest(String urlstrng,String[][] properties)
     {
     URL url = null;
     BufferedReader reader = null;
     StringBuilder stringBuilder = new StringBuilder();
     HttpURLConnection connection=null;
		
     try
     {
     url = new URL(urlstrng);
			
     connection = (HttpURLConnection) url.openConnection();
			
     connection.setRequestMethod("GET");
			
     for(int i=0;i<properties.length;i++)
     connection.setRequestProperty(properties[i][0],properties[i][1]);
			
     // give it 2 minutes to respond
     connection.setReadTimeout(120*1000);
     connection.connect();
			
     if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
     {
     System.out.println(urlstrng);
     System.out.println(connection.getResponseCode());
     System.out.println(connection.getResponseMessage());
				
     return "";
     }
			
     // read the output from the server
     reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
     String line = null;
			
     while ((line = reader.readLine()) != null)
     stringBuilder.append(line + "\n");
     }
     catch (Exception e)
     {
     e.printStackTrace();
			
     return "";
     }
     finally
     {
     if (reader != null)
     {
     try
     {
     reader.close();
     }
     catch (IOException ioe)
     {
     ioe.printStackTrace();
     }
     }
     }
		
     return stringBuilder.toString();
     }
     */

    public static ITLHTTPResponse makeHTTPGetRequest(String urlstrng, String[][] properties) {
        URL url = null;
        HttpURLConnection connection = null;
        ITLHTTPResponse response = null;

        try {
            url = new URL(urlstrng);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            for (int i = 0; i < properties.length; i++) {
                connection.setRequestProperty(properties[i][0], properties[i][1]);
            }

            // give it 2 minutes to respond
            connection.setReadTimeout(120 * 1000);
            connection.connect();

            response = ITLHTTPResponse.getHTTPResponse(connection);
        }
        catch (Exception e) {
            e.printStackTrace();

            return response;
        }

        return response;
    }

    public static Map<?, ?> parseJSONResponseToMap(String jsonstrng) {
        JSONParser parser = new JSONParser();

        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };

        try {
            Map<?, ?> json = (Map<?, ?>) parser.parse(jsonstrng.toString(), containerFactory);

            return json;
        }
        catch (ParseException pe) {
            System.out.println(pe);
        }

        return null;
    }

    public static List<?> parseJSONResponseToList(String jsonstrng) {
        JSONParser parser = new JSONParser();

        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };

        try {
            LinkedList<?> json = (LinkedList<?>) parser.parse(jsonstrng.toString(), containerFactory);

            return json;
        }
        catch (ParseException pe) {
            System.out.println(pe);
        }

        return null;
    }
}
