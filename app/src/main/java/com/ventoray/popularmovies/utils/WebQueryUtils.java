package com.ventoray.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.StringDef;
import android.util.Log;

import com.ventoray.popularmovies.web_data_object.Movie;
import com.ventoray.popularmovies.web_data_object.Review;
import com.ventoray.popularmovies.web_data_object.VideoData;

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

import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.ADULT;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.API_KEY_TMDB;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BACKDROP_PATH;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_DISCOVER_URL;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_TMBD_URI;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_MOVIE_POPULAR;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_MOVIE_TOP_RATED;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.EN_US;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.HTTP_RESPONSE_OK;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.ID;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.ORIGINAL_LANGUAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.ORIGINAL_TITLE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.OVERVIEW;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PARAM_API_KEY;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PARAM_LANGUAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PARAM_PAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PARAM_SORT_BY;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PATH_3;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PATH_MOVIES;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.POPULARITY;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.POPULAR_ASC;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.POPULAR_DESC;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.POSTER_PATH;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.RATING_ASC;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.RATING_DESC;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.RELEASE_DATE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.RESULTS;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.REVIEW_AUTHOR;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.REVIEW_CONTENT;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.REVIEW_ID;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.REVIEW_RESULTS;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.REVIEW_URL;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.TITLE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_ID;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_ISO_3166_1;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_ISO_639_1;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_KEY;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_NAME;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_RESULTS;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_SIZE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_TYPE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VIDEO_WEBSITE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VOTE_AVERAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.VOTE_COUNT;

/**
 * Created by Nick on 10/15/2017.
 */

public class WebQueryUtils {

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


    private static final String TAG = "WebQueryUtils";

    public static final int URL_TYPE_TMDB_REVIEWS = 1000;
    public static final int URL_TYPE_TMDB_VIDEOS = 1001;
    public static final int URL_TYPE_TMDB_POPULAR = 1002;
    public static final int URL_TYPE_TMDB_TOP_RATED = 1003;
    public static final int URL_TYPE_TMDB_DISCOVER = 1004;


    /**
     * Method is used to build a URL retrieving either videos or reviews assocated with a movie
     * @param movieId - Tmdb id associated with movie
     * @param path - either /reviews or /videos
     * @return - URL used to fetch data
     */
    public static URL buildMovieDataUrl(String movieId, String path) {

        Uri uri;
        URL url;

        Uri.Builder builder = Uri.parse(BASE_TMBD_URI).buildUpon()
                .appendPath(PATH_3)
                .appendPath(PATH_MOVIES)
                .appendPath(movieId)
                .appendPath(path)
                .appendQueryParameter(PARAM_API_KEY, API_KEY_TMDB);

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


    public static URL buildMoviesUrl(@BaseUrl String baseUrl, @SortMethod String sortBy, int pageNumber) {
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
     * @param url - url built in buildMoviesUrl() to retrieve movies in specified order
     * @return - returns List of Movies to pass to adapter class
     */
    public static Object[] makeHttpUrlRequest(URL url, int tmdbUrlType) {
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

        switch (tmdbUrlType) {
            case URL_TYPE_TMDB_DISCOVER:
            case URL_TYPE_TMDB_POPULAR:
            case URL_TYPE_TMDB_TOP_RATED:
                return parseMoviesFromJson(jsonToParse);

            case URL_TYPE_TMDB_REVIEWS:
                return parseReviewsFromJson(jsonToParse);

            case URL_TYPE_TMDB_VIDEOS:
                return parseVideoDataFromJson(jsonToParse);

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


    private static VideoData[] parseVideoDataFromJson(String jsonToParse) {
        VideoData[] videoDataArray = null;

        try {
            JSONObject jsonResponse = new JSONObject(jsonToParse);
            JSONArray resultsArray = jsonResponse.getJSONArray(VIDEO_RESULTS);
            videoDataArray = new VideoData[resultsArray.length()];

            for (int i = 0; i < resultsArray.length(); i++) {
                VideoData videoData = new VideoData();
                String id;
                String iso6391;
                String iso31661;
                String key;
                String name;
                String webSite;
                int size;
                String type;

                JSONObject jsonObject = resultsArray.getJSONObject(i);

                id = jsonObject.getString(VIDEO_ID);
                iso6391 = jsonObject.getString(VIDEO_ISO_639_1);
                iso31661 = jsonObject.getString(VIDEO_ISO_3166_1);
                key = jsonObject.getString(VIDEO_KEY);
                name = jsonObject.getString(VIDEO_NAME);
                webSite = jsonObject.getString(VIDEO_WEBSITE);
                size = jsonObject.getInt(VIDEO_SIZE);
                type = jsonObject.getString(VIDEO_TYPE);

                videoData.setId(id);
                videoData.setIso6391(iso6391);
                videoData.setIso31661(iso31661);
                videoData.setKey(key);
                videoData.setName(name);
                videoData.setWebSite(webSite);
                videoData.setSize(size);
                videoData.setType(type);

                videoDataArray[i] = videoData;
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseVideoDataFromJson --- " + e.getMessage());
        }

        return videoDataArray;
    }


    private static Review[] parseReviewsFromJson(String jsonToParse) {
        JSONObject jsonResponse;
        Review[] reviews = null;
        if (jsonToParse == null || jsonToParse.isEmpty()) {
            return null;
        }

        try {
            jsonResponse = new JSONObject(jsonToParse);
            JSONArray resultsArray = jsonResponse.getJSONArray(REVIEW_RESULTS);
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
            Log.e(TAG, "parseReviewsFromJson --- " + e.getMessage());
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

                movies[i] = movie;
            }

            return movies;

        } catch (JSONException e) {
            Log.e(TAG, "parseMoviesFromJson --- " + e.getMessage());
        }

        return null;
    }






}
