package com.ventoray.popularmovies;

import android.os.AsyncTask;

import com.ventoray.popularmovies.utils.QueryUtils;

import java.net.URL;

/**
 * Created by nicks on 10/23/2017.
 */

public class MovieDataAsyncTask extends AsyncTask <URL, Void, Object[]> {

    private OnMovieDataLoadedListener listener;

    public MovieDataAsyncTask(OnMovieDataLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object[] doInBackground(URL... url) {
        return QueryUtils.makeHttpUrlRequest(url[0]);
    }

    @Override
    protected void onPostExecute(Object[] movieData) {

        listener.onMoviesLoaded(movieData);

    }


}
