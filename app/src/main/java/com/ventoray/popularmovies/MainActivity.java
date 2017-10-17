package com.ventoray.popularmovies;

import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ventoray.popularmovies.DBConstants.RATING_ASC;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private List<Movie> mMovies;
    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();

        URL url = QueryUtils.buildUrl(RATING_ASC, 1);

        if (url != null) {
            new MovieLoaderAsyncTask().execute(url);
        } else {
            Toast.makeText(this, R.string.error_url_creation, Toast.LENGTH_SHORT).show();
        }

    }


    private void setUpRecyclerView() {
        GridLayoutManager glm = new GridLayoutManager(this, 3);
//        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mMovies = new ArrayList<>();
        mAdapter = new MoviePosterAdapter(mMovies, getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);
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
            if (movies.length > 0) {
                mMovies.addAll(Arrays.asList(movies));
                mAdapter.notifyDataSetChanged();
            }
        }

    }

}
