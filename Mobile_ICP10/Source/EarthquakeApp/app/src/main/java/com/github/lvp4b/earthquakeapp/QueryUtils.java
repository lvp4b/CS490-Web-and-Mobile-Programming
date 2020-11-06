package com.github.lvp4b.earthquakeapp;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static List<Earthquake> fetchEarthquakeData2(String requestUrl) {
        // An empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();
        //  URL object to store the url for a given string
        URL url;
        try {
            url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new IOException("Http error code: " + connection.getResponseCode());
            }

            try (InputStream stream = connection.getInputStream();
                 InputStreamReader reader = new InputStreamReader(stream);
                 BufferedReader buffer = new BufferedReader(reader)) {
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = buffer.readLine()) != null){
                    builder.append(line).append(System.lineSeparator());
                }

                JSONArray array = new JSONObject(builder.toString()).getJSONArray("features");
                for (int i = 0; i != array.length(); i++) {
                    JSONObject object = array.getJSONObject(i).getJSONObject("properties");
                    earthquakes.add(new Earthquake(object.getDouble("mag"),
                            object.getString("place"),
                            object.getLong("time"),
                            object.getString("url")));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing JSON array: " + e);
            }

            connection.disconnect();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating Url: " + e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception:  ", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception:  ", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
