package com.example.android.newsapp;

import android.text.TextUtils;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ayoawotunde on 18/06/2018.
 */

public final class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private Utils(){

    }

    // Query the USGS dataset and return an Earthquake object to represent a single earthquake.

    public static List<Article> fetchArticles(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an Earthquake object
        List<Article> earthquake = extractArticles(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }


    //Returns new URL object from the given string URL.

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    //Make an HTTP request to the given URL and return a String as the response.

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }



    //Convert the inputSream into a String which contains the
    // JSON response from the server.

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    //Return a list of articles objects that have been derived from a JSON parsing
    private static List<Article> extractArticles(String JSONresponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Article> stories = new ArrayList<>();

        try {
            // Convert JSON_RESPONSE String into a JSONObject
            JSONObject baseJsonResponse = new JSONObject(JSONresponse);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray storyArray = response.getJSONArray("results");

            // Get all results by looping through
            for (int i = 0; i < storyArray.length(); i++) {
                //Get article
                JSONObject currentArticle = storyArray.getJSONObject(i);
                //Extract "webTitle" for headline
                String headline = currentArticle.getString("webTitle");
                //getting the tags array for further info
                JSONArray tags = currentArticle.getJSONArray("tags");
                //get the author
                String author;
                if(tags.length()>0){
                    JSONObject currentTag = tags.getJSONObject(0);
                    author = currentTag.getString("webTitle");
                } else author = "";
                //get picture
                JSONObject fields = null;
                try {fields = currentArticle.getJSONObject("fields");
                } catch (JSONException e){
                    Log.e(LOG_TAG, "Parsing problem, no value for Fields, default picture will be shown");
                }
                String pictureUrl;
                if (fields!=null){
                    pictureUrl = fields.getString("thumbnail");
                } else {
                    pictureUrl = null;
                }
                // Extract “time” for time
                String time = currentArticle.getString("webPublicationDate");


                //Launch website
                String web= currentArticle.getString("webUrl");

                //  Create Earthquake java object from magnitude, location, and time
                //String title, String author, long dateTime, String web, String picture
                Article story = new Article(headline, author, time, web, pictureUrl);
                stories.add(story);


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return stories;
    }


}