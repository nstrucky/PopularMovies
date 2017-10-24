package com.ventoray.popularmovies;

import android.net.Uri;
import android.support.annotation.StringDef;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.ventoray.popularmovies.DBConstants.ADULT;
import static com.ventoray.popularmovies.DBConstants.API_KEY;
import static com.ventoray.popularmovies.DBConstants.BACKDROP_PATH;
import static com.ventoray.popularmovies.DBConstants.BASE_DISCOVER_URL;
import static com.ventoray.popularmovies.DBConstants.BASE_URL_MOVIE_POPULAR;
import static com.ventoray.popularmovies.DBConstants.BASE_URL_MOVIE_TOP_RATED;
import static com.ventoray.popularmovies.DBConstants.EN_US;
import static com.ventoray.popularmovies.DBConstants.HTTP_RESPONSE_OK;
import static com.ventoray.popularmovies.DBConstants.ID;
import static com.ventoray.popularmovies.DBConstants.ORIGINAL_LANGUAGE;
import static com.ventoray.popularmovies.DBConstants.ORIGINAL_TITLE;
import static com.ventoray.popularmovies.DBConstants.OVERVIEW;
import static com.ventoray.popularmovies.DBConstants.PARAM_API_KEY;
import static com.ventoray.popularmovies.DBConstants.PARAM_LANGUAGE;
import static com.ventoray.popularmovies.DBConstants.PARAM_PAGE;
import static com.ventoray.popularmovies.DBConstants.PARAM_SORT_BY;
import static com.ventoray.popularmovies.DBConstants.POPULARITY;
import static com.ventoray.popularmovies.DBConstants.POPULAR_ASC;
import static com.ventoray.popularmovies.DBConstants.POPULAR_DESC;
import static com.ventoray.popularmovies.DBConstants.POSTER_PATH;
import static com.ventoray.popularmovies.DBConstants.RATING_ASC;
import static com.ventoray.popularmovies.DBConstants.RATING_DESC;
import static com.ventoray.popularmovies.DBConstants.RELEASE_DATE;
import static com.ventoray.popularmovies.DBConstants.RESULTS;
import static com.ventoray.popularmovies.DBConstants.TITLE;
import static com.ventoray.popularmovies.DBConstants.VIDEO;
import static com.ventoray.popularmovies.DBConstants.VOTE_AVERAGE;
import static com.ventoray.popularmovies.DBConstants.VOTE_COUNT;

/**
 * Created by Nick on 10/15/2017.
 */

public class QueryUtils {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef ({
            POPULAR_ASC,
            POPULAR_DESC,
            RATING_ASC,
            RATING_DESC,
    })
    public @interface SortMethod {}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef ({
            BASE_URL_MOVIE_POPULAR,
            BASE_URL_MOVIE_TOP_RATED,
            BASE_DISCOVER_URL
    })
    public @interface BaseUrl {}


    private static final String TAG = "QueryUtils";

    public static URL buildUrl(@BaseUrl String baseUrl, @SortMethod String sortBy, int pageNumber) {
        URL url;
        Uri uri;

        Uri.Builder builder = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, EN_US);

        if (baseUrl.equals(BASE_DISCOVER_URL)) {
            builder.appendQueryParameter(PARAM_SORT_BY, sortBy);
        }

        if (pageNumber > 0 && pageNumber <= 1000) {
            String pageNumberString = Integer.toString(pageNumber);
            builder.appendQueryParameter(PARAM_PAGE, pageNumberString);
        }

        uri = builder.build();

        Log.d(TAG, uri.toString());
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
        return url;
    }

    /**
     * This method is called in the AsyncTask in the MainActivity
     * @param url - url built in buildUrl() to retrieve movies in specified order
     * @return - returns List of Movies to pass to adapter class
     * @throws IOException
     */
    public static Movie[] makeHttpUrlRequest(URL url) {
        InputStream in = null;
        String jsonToParse = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.d(TAG, "response code " + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == HTTP_RESPONSE_OK) {
                in = urlConnection.getInputStream();
                jsonToParse = getResponseFromHTTPUrl(in);

            } else {
                return null;
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

        return stringBuilder.toString();
    }


    private static Movie[] parseJson(String jsonToParse) {
        JSONObject jsonResponse;
        Movie[] movies;

        if (jsonToParse == null || jsonToParse.isEmpty()) {
            return null;
        }

        try {
            jsonResponse = new JSONObject(jsonToParse);
            JSONArray resultsArray = jsonResponse.getJSONArray(RESULTS);
            movies = new Movie[resultsArray.length()];
            int id;
            int voteCount;
            int voteAverage;
            String title;
            String overview;
            String releaseDate;
            String posterPath;
            String originalLanguage;
            String originalTitle;
            String backdropPath;
            boolean adult;
            boolean video;
            double popularity;

            for (int i = 0; i < movies.length; i++) {
                Movie movie = new Movie();
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                id = jsonObject.getInt(ID);
                voteCount = jsonObject.getInt(VOTE_COUNT);
                voteAverage = jsonObject.getInt(VOTE_AVERAGE);
                title = jsonObject.getString(TITLE);
                overview = jsonObject.getString(OVERVIEW);
                releaseDate = jsonObject.getString(RELEASE_DATE);
                posterPath = jsonObject.getString(POSTER_PATH);
                originalLanguage = jsonObject.getString(ORIGINAL_LANGUAGE);
                originalTitle = jsonObject.getString(ORIGINAL_TITLE);
                backdropPath = jsonObject.getString(BACKDROP_PATH);
                adult = jsonObject.getBoolean(ADULT);
                video = jsonObject.getBoolean(VIDEO);
                popularity = jsonObject.getDouble(POPULARITY);

                movie.setId(id);
                movie.setVoteCount(voteCount);
                movie.setVoteAverage(voteAverage);
                movie.setTitle(title);
                movie.setOverview(overview);
                movie.setReleaseDate(releaseDate);
                movie.setPosterPath(posterPath);
                movie.setOriginalLanguage(originalLanguage);
                movie.setOriginalTitle(originalTitle);
                movie.setBackdropPath(backdropPath);
                movie.setAdult(adult);
                movie.setVideo(video);
                movie.setPopularity(popularity);

                movies[i] = movie;
            }

            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }






}
