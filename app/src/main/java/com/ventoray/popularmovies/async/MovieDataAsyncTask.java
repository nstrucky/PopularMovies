package com.ventoray.popularmovies.async;

import android.os.AsyncTask;

import com.ventoray.popularmovies.utils.WebQueryUtils;

import java.net.URL;

/**
 * Created by nicks on 10/23/2017.
 */

public class MovieDataAsyncTask extends AsyncTask <URL, Void, Object[]> {

    private OnMovieDataLoadedListener listener;
    private int mTmdbUrlType;

    public MovieDataAsyncTask(int tmdbUriType, OnMovieDataLoadedListener listener) {
        this.listener = listener;
        mTmdbUrlType = tmdbUriType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object[] doInBackground(URL... url) {
        return WebQueryUtils.makeHttpUrlRequest(url[0], mTmdbUrlType);
    }

    @Override
    protected void onPostExecute(Object[] movieData) {

        listener.onMovieDataLoaded(movieData);

    }


}
