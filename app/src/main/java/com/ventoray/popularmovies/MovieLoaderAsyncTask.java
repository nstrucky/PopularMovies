package com.ventoray.popularmovies;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by nicks on 10/23/2017.
 */

public class MovieLoaderAsyncTask extends AsyncTask <URL, Void, Movie[]> {

    private OnMoviesLoadedListener listener;

    public MovieLoaderAsyncTask(OnMoviesLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(URL... url) {
        return QueryUtils.makeHttpUrlRequest(url[0]);
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        listener.onMoviesLoaded(movies);

    }


}
