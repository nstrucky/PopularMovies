package com.ventoray.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();

        URL url = QueryUtils.buildUrl(QueryUtils.RATING_ASC, 1000);

        new MovieLoaderAsyncTask().execute(url);

    }


    private void setUpRecyclerView() {
        mMovies = new ArrayList<>();
    }


    class MovieLoaderAsyncTask extends AsyncTask<URL, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(URL... url) {
            Movie[] movies = QueryUtils.makeHttpUrlRequest(url[0]);

            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
        }

    }

}
