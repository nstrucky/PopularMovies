package com.ventoray.popularmovies;

/**
 * Created by nicks on 10/23/2017.
 * Interface used for communication between MovieLoaderAsyncTask and MainActivity
 */

public interface OnMoviesLoadedListener {

    /**
     * Called in MovieLoaderAsyncTask opPostExecute() to notify listening activity and pass
     * the array of movie objects.
     * @param movies
     */
    public void onMoviesLoaded(Movie[] movies);

}
