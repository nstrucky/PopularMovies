package com.ventoray.popularmovies.utils;

import com.ventoray.popularmovies.BuildConfig;

/**
 * Created by Nick on 11/13/2017.
 */

public class WebApiConstants {
    /**
     * Created by Nick on 10/16/2017.
     */

    public static class TMDB {

        public static final String API_KEY_TMDB = BuildConfig.TMDB_API_KEY;
        public static final String EN_US = "en-US";

        public static final String TMDB_AUTHORITY = "api.themoviedb.org";
        public static final String PATH_3 = "3";
        public static final String PATH_MOVIES = "movie";
        public static final String PATH_MOVIE_REVIEWS = "reviews";
        public static final String PATH_MOVIE_VIDEOS = "videos";
        public static final String PATH_MOVIES_POPULAR = "popular";
        public static final String PATH_MOVIES_TOP_RATED = "top_rated";
        public static final String PATH_DISCOVER_MOVIES = "/3/discover/movie";
        public static final String BASE_TMBD_URI = "https://" + TMDB_AUTHORITY;

        public static final String BASE_URL_MOVIE_POPULAR = "https://api.themoviedb.org/3/movie/popular";
        public static final String BASE_URL_MOVIE_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";

        public static final String PARAM_API_KEY = "api_key";
        public static final String PARAM_SORT_BY = "sort_by";
        public static final String PARAM_LANGUAGE = "language";
        public static final String PARAM_PAGE = "page";

        public static final String BASE_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie";
        public static final String POPULAR_ASC = "popularity.asc";
        public static final String RATING_ASC = "vote_average.asc";
        public static final String POPULAR_DESC = "popularity.desc";
        public static final String RATING_DESC = "vote_average.desc";


        /**
         *  Image URL constants
         */
        public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
        public static final String ORIGINAL = "original";

        static final int HTTP_RESPONSE_OK = 200;

        static final String RESULTS = "results";
        static final String VOTE_COUNT = "vote_count";
        static final String ID = "id";
        static final String VIDEO = "video";
        static final String VOTE_AVERAGE = "vote_average";
        static final String TITLE = "title";
        static final String POPULARITY = "popularity";
        static final String POSTER_PATH = "poster_path";
        static final String ORIGINAL_LANGUAGE = "original_language";
        static final String ORIGINAL_TITLE = "original_title";
        static final String GENRE_IDS = "genre_ids";
        static final String BACKDROP_PATH = "backdrop_path";
        static final String ADULT = "adult";
        static final String OVERVIEW = "overview";
        static final String RELEASE_DATE = "release_date";

        /**
         * This are constants representing key names in the movie/#/reviews json response
         */
        static final String REVIEW_RESULTS = "results";
        static final String REVIEW_AUTHOR = "author";
        static final String REVIEW_CONTENT = "content";
        static final String REVIEW_URL = "url";
        static final String REVIEW_ID = "id";


        /**
         * These are constants representing key names in the movie/#/videos json response
         */
        static final String VIDEO_RESULTS = "results";
        static final String VIDEO_ID = "id";
        static final String VIDEO_ISO_639_1 = "iso_639_1";
        static final String VIDEO_ISO_3166_1 = "iso_3166_1";
        static final String VIDEO_KEY = "key";
        static final String VIDEO_NAME = "name";
        static final String VIDEO_SIZE = "size";
        static final String VIDEO_WEBSITE = "site";
        static final String VIDEO_TYPE = "type";


    }


    public static class YouTube {




    }

}
