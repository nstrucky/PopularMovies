package com.ventoray.popularmovies;

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

        public static final int HTTP_RESPONSE_OK = 200;

        public static final String RESULTS = "results";
        public static final String VOTE_COUNT = "vote_count";
        public static final String ID = "id";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String GENRE_IDS = "genre_ids";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String ADULT = "adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";

        /**
         * This are constants representing key names in the movie/#/reviews json response
         */
        public static final String REVIEW_AUTHOR = "author";
        public static final String REVIEW_CONTENT = "content";
        public static final String REVIEW_URL = "url";
        public static final String REVIEW_ID = "id";

    }


    public static class YouTube {




    }

}