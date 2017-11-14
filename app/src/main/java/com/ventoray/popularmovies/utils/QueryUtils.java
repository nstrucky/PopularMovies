package com.ventoray.popularmovies.utils;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.util.Log;

import com.ventoray.popularmovies.Movie;
import com.ventoray.popularmovies.Review;

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

import static com.ventoray.popularmovies.WebApiConstants.TMDB.ADULT;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.API_KEY_TMDB;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.BACKDROP_PATH;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.BASE_DISCOVER_URL;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.BASE_URL_MOVIE_POPULAR;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.BASE_URL_MOVIE_TOP_RATED;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.EN_US;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.HTTP_RESPONSE_OK;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.ID;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.ORIGINAL_LANGUAGE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.ORIGINAL_TITLE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.OVERVIEW;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PARAM_API_KEY;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PARAM_LANGUAGE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PARAM_PAGE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PARAM_SORT_BY;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_3;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_DISCOVER_MOVIES;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_MOVIES;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_MOVIES_POPULAR;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_MOVIES_TOP_RATED;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_MOVIE_REVIEWS;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.PATH_MOVIE_VIDEOS;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.POPULARITY;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.POPULAR_ASC;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.POPULAR_DESC;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.POSTER_PATH;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.RATING_ASC;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.RATING_DESC;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.RELEASE_DATE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.RESULTS;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.REVIEW_AUTHOR;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.REVIEW_CONTENT;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.REVIEW_ID;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.REVIEW_URL;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.TITLE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.TMDB_AUTHORITY;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.VIDEO;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.VOTE_AVERAGE;
import static com.ventoray.popularmovies.WebApiConstants.TMDB.VOTE_COUNT;

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

    public static final int TMDB_REVIEWS = 1000;
    public static final int TMDB_VIDEOS = 1001;
    public static final int TMDB_POPULAR = 1002;
    public static final int TMDB_TOP_RATED = 1003;
    public static final int TMDB_DISCOVER = 1004;

    private static int sTmdbUriType = 0;


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TMDB_AUTHORITY, PATH_3 + "/" + PATH_MOVIES + "/#" + PATH_MOVIE_REVIEWS, TMDB_REVIEWS);
        uriMatcher.addURI(TMDB_AUTHORITY, PATH_3 + "/" + PATH_MOVIES + "/#" + PATH_MOVIE_VIDEOS, TMDB_VIDEOS);
        uriMatcher.addURI(TMDB_AUTHORITY, PATH_3 + "/" + PATH_MOVIES + "/" + PATH_MOVIES_POPULAR, TMDB_POPULAR);
        uriMatcher.addURI(TMDB_AUTHORITY, PATH_3 + "/" + PATH_MOVIES + "/" + PATH_MOVIES_TOP_RATED, TMDB_TOP_RATED);
        uriMatcher.addURI(TMDB_AUTHORITY, PATH_DISCOVER_MOVIES, TMDB_DISCOVER);

        return uriMatcher;
    }


    public static URL buildReviewsUrl(String baseUrl, String movieId) {

        Uri uri;
        URL url;

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon()
                .appendPath(PATH_3)
                .appendPath(PATH_MOVIES)
                .appendPath(movieId)
                .appendPath(PATH_MOVIE_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY_TMDB);

        uri = builder.build();
        sTmdbUriType = TMDB_REVIEWS;
        Log.d(TAG, "URI TYPE ------------------------ " + sTmdbUriType);

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


    public static URL buildTmdbUrl(@BaseUrl String baseUrl, @SortMethod String sortBy, int pageNumber) {
        Uri uri;
        URL url;

        Uri.Builder builder = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY_TMDB)
                .appendQueryParameter(PARAM_LANGUAGE, EN_US);

        if (baseUrl.equals(BASE_DISCOVER_URL)) {
            builder.appendQueryParameter(PARAM_SORT_BY, sortBy);
        }

        if (pageNumber > 0 && pageNumber <= 1000) {
            String pageNumberString = Integer.toString(pageNumber);
            builder.appendQueryParameter(PARAM_PAGE, pageNumberString);
        }

        uri = builder.build();
        sTmdbUriType = buildUriMatcher().match(uri);

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
     * @param url - url built in buildTmdbUrl() to retrieve movies in specified order
     * @return - returns List of Movies to pass to adapter class
     * @throws IOException
     */
    public static Object[] makeHttpUrlRequest(URL url) {
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

        switch (sTmdbUriType) {
            case TMDB_DISCOVER:
            case TMDB_POPULAR:
            case TMDB_TOP_RATED:
                return parseMoviesFromJson(jsonToParse);

            case TMDB_REVIEWS:
                return parseReviewsFromJson(jsonToParse);
                default:
                    return null;
        }
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


    private static Review[] parseReviewsFromJson(String jsonToParse) {
        JSONObject jsonResponse;
        Review[] reviews = null;
        if (jsonToParse == null || jsonToParse.isEmpty()) {
            return null;
        }

        try {
            jsonResponse = new JSONObject(jsonToParse);
            JSONArray resultsArray = jsonResponse.getJSONArray(RESULTS);
            reviews = new Review[resultsArray.length()];
            String id;
            String author;
            String content;
            String url;

            for (int i = 0; i < reviews.length; i++) {
                Review review = new Review();
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                id = jsonObject.getString(REVIEW_ID);
                author = jsonObject.getString(REVIEW_AUTHOR);
                content = jsonObject.getString(REVIEW_CONTENT);
                url = jsonObject.getString(REVIEW_URL);

                review.setmId(id);
                review.setmAuthor(author);
                review.setmContent(content);
                review.setmUrl(url);

                reviews[i] = review;
            }

            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }


    private static Movie[] parseMoviesFromJson(String jsonToParse) {
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

                Log.d(TAG, "Movie: " + movie.getTitle());
                Log.d(TAG, "ID: " + movie.getId());

                movies[i] = movie;
            }

            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }






}
