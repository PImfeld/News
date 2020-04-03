package com.example.news;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from the Guardian.
 */
public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Creates a private constructor, hold static variables and methods, which can be accessed directly from the class name QueryUtils
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Creates URL object
        URL url = createUrl(requestUrl);

        // Performs HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extracts relevant fields from the JSON response and create a list of {@link News}
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link news}
        return news;
    }

    /**
     * Returns the new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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

            // If the request was successful (response code 200), then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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

    /**
     * Converts the {@link InputStream} into a String which contains the whole JSON response from the server.
     */
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

    /**
     * Returns a list of {@link News} objects that has been built up from parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Tries to parse the JSON response string. If there's a problem with the way the JSON is formatted, a JSONException exception object will be thrown.
        // Catches the exception so the app doesn't crash, and prints the error message to the logs.
        try {
            // Creates a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extracts the JSONArray associated with the key called "results", which represents a list of articles.
            JSONArray newsArray = baseJsonResponse.getJSONArray("results");

            // For each article in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Gets a single article at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentNews.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = currentNews.getString("sectionName");

                // Extract the value for the key called "authorName"
                String author = currentNews.getString("authorName");

                // Extract the value for the key called "url"
                String url = currentNews.getString("webUrl");

                // Creates a new {@link News} object with the title, section, author, and url from the JSON response.
                News article = new News(title, section, url);

                // Adds the new {@link News} to the list of news.
                news.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of articles
        return news;
    }
        }
