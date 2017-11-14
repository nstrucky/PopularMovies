package com.ventoray.popularmovies;

/**
 * Created by nicks on 10/23/2017.
 * Interface used for communication between MovieDataAsyncTask and MainActivity
 */

public interface OnMovieDataLoadedListener {

    /**
     * Called in MovieDataAsyncTask opPostExecute() to notify listening activity and pass
     * the array of movie objects.
     * @param movies
     */
    public void onMoviesLoaded(Object[] movies);

}