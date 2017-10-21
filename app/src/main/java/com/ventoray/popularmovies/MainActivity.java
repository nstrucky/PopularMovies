package com.ventoray.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ventoray.popularmovies.DBConstants.POPULAR_DESC;
import static com.ventoray.popularmovies.NetworkUtils.checkConnectivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private List<Movie> mMovies;
    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mAdapter;
    private MoviePosterAdapter.PosterOnClickListener posterListener;

    public static final String SERIALIZABLE_MOVIE_KEY = "serializableMovieKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        getMovieData(POPULAR_DESC, 1);

    }


    private void getMovieData(String type, int page) {
        URL url = QueryUtils.buildUrl(type, page);
        if (url != null && checkConnectivity(this)) {
            new MovieLoaderAsyncTask().execute(url);
        } else {
            Toast.makeText(this, R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
        }
    }


    private void setUpRecyclerView() {
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        mMovies = new ArrayList<>();
        mAdapter = new MoviePosterAdapter(mMovies,
                getApplicationContext(), new MoviePosterAdapter.PosterOnClickListener() {
            @Override
            public void onClick(Movie movie) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra(SERIALIZABLE_MOVIE_KEY, movie);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
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
            if (movies != null) {
                if (movies.length > 0) {
                    mMovies.addAll(Arrays.asList(movies));
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(MainActivity.this,
                        R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_switch:
                mMovies.clear();
                String title = item.getTitle().toString();
                String popular = getString(R.string.popular);
                String rating = getString(R.string.rating);

                if (title.equals(popular)) {
                    getMovieData(DBConstants.POPULAR_DESC, 1);
                    item.setTitle(rating);
                } else {
                    getMovieData(DBConstants.RATING_DESC, 1);
                    item.setTitle(popular);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
