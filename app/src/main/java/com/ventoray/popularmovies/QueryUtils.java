package com.ventoray.popularmovies;

import android.net.Uri;
import android.support.annotation.StringDef;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nick on 10/15/2017.
 */



public class QueryUtils {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef ({
            POPULAR_ASC,
            POPULAR_DESC,
            RATING_ASC,
            RATING_DESC
    })
    public @interface SortMethod {}

    public static final String POPULAR_ASC = "popularity.asc";
    public static final String RATING_ASC = "vote_average.asc";
    public static final String POPULAR_DESC = "popularity.desc";
    public static final String RATING_DESC = "vote_average.desc";


    private static final String TAG = "QueryUtils";

    // TODO: 10/15/2017 Delete before push!!! 
    private static final String API_KEY = "";
    private static final String EN_US = "en-US";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_LANGUAGE = "language";

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final int HTTP_RESPONSE_OK = 200;


    public static URL buildUrl(@SortMethod String sortBy) {
        URL url = null;
        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_SORT_BY, sortBy)
                .appendQueryParameter(PARAM_LANGUAGE, EN_US)

                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        // TODO: 10/15/2017 check for null when method called to notify user there was an error
        return url;
    }

    /**
     * This method is called in the AsyncTask in the MainActivity
     * @param url - url built in buildUrl() to retrieve movies in specified order
     * @return - returns List of Movies to pass to adapter class
     * @throws IOException
     */
    public static List<Movie> makeHttpUrlRequest(URL url) {
        InputStream in = null;
        String jsonToParse = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HTTP_RESPONSE_OK) {
                in = urlConnection.getInputStream();
                jsonToParse = getResponseFromHTTPUrl(in);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                   Log.d(TAG, e.getMessage());
                }
            }
        }

        return parseJson(jsonToParse);
    }

    /**
     * This method uses a BufferedReader to read each line of the http response and append to a
     * StringBuilder.
     * @param in InputStream created by the HttpUrlConnection in makeHttpUrlRequest()
     * @return
     */
    private static String getResponseFromHTTPUrl(InputStream in) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        Log.i(TAG, stringBuilder.toString());
        return stringBuilder.toString();
    }


    private static List<Movie> parseJson(String jsonToParse) {
        // TODO: 10/15/2017 parse json from httpresponse string

        if (jsonToParse == null) {
            return null;
        }

        return null;
    }






}
