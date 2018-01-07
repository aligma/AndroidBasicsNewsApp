package com.example.david.androidbasicsnewsapp;

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

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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


    private static List<Article> extractFeatureFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray itemArray = response.getJSONArray("results");

            for (int i = 0; i < itemArray.length(); i++) {

                JSONObject currentArticle = itemArray.getJSONObject(i);

                // JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = currentArticle.getString("webTitle");
                String sectionName = currentArticle.getString("sectionName");
                String webUrl = currentArticle.getString("webUrl");
                String authorName = "";

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                Date webPublicationDate = new Date();

                try {
                    webPublicationDate = df.parse(
                            currentArticle.getString("webPublicationDate")
                    );
                } catch (ParseException e) {
                    Log.e("QueryUtils", "Problem parsing the date", e);
                }

                JSONArray tags = currentArticle.getJSONArray("tags");
                if (tags.length() > 0) {
                    JSONObject tag = tags.getJSONObject(0);
                    authorName = tag.getString("webTitle");
                }

                Article article = new Article(title, sectionName, authorName, webUrl, webPublicationDate);

                articles.add(article);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        return articles;
    }
}
