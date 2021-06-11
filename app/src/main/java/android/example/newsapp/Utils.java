package android.example.newsapp;

import android.example.newsapp.models.News;
import android.graphics.drawable.Drawable;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static Drawable thumbnailDrawable;

    public static List<News> fetchNews(String requestUrl) {
        String jsonString = "";
        URL url = convertUrl(requestUrl);

        try {
            jsonString = httpRequest(url);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Something went wrong with parsing the requested URL", e);
        }

        List<News> result = parseJson(jsonString);

        return result;
    }

    /**
     * Converting obtained data into the custom News obj
     */
    private static List<News> parseJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject response = root.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject article = resultsArray.getJSONObject(i);
                JSONObject fields = article.getJSONObject("fields");

                String title = article.getString("webTitle");
                String date = article.getString("webPublicationDate");
                String url = article.getString("webUrl");

                ArrayList<String> authorsArray = new ArrayList<>();
                JSONArray tagsArray = article.getJSONArray("tags");
                if (tagsArray.length() > 0) {
                    for (int j=0; j < tagsArray.length(); j++) {
                        JSONObject tagObj = tagsArray.getJSONObject(j);
                        authorsArray.add(tagObj.getString("webTitle"));
                    }
                }
                // Converting obtained image URL to drawable
                String imageUrl = fields.getString("thumbnail");
                try {
                    InputStream inputStream = (InputStream) new URL(imageUrl).getContent();
                    thumbnailDrawable = Drawable.createFromStream(inputStream, title);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Something went wrong with parsing image url", e);
                    return null;
                }

                newsList.add(new News(title, authorsArray, date, thumbnailDrawable, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Something went wrong with retrieving JSON data", e);
        }

        return newsList;
    }

    /**
     * Conducting Http request
     */
    private static String httpRequest(URL url) throws IOException {
        String jsonString = "";
        if (url == null) {
            return jsonString;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonString = readInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error on URL connection. Response status code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonString;
    }


    /**
     * Translating received inputstream
     */
    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            // Wrapping inputStreamReader with BufferReader to escalate the process
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Read line by line and append the result to string builder
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }

        // Convert the result to String at the end and return it
        return stringBuilder.toString();
    }


    /**
     * Converting requestUrl to URL obj
     */
    private static URL convertUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        return url;
    }
}
