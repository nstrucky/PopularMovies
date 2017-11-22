package com.ventoray.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ventoray.popularmovies.adapters.MoviePosterAdapter;
import com.ventoray.popularmovies.async.MovieDataAsyncTask;
import com.ventoray.popularmovies.async.OnMovieDataLoadedListener;
import com.ventoray.popularmovies.utils.WebQueryUtils;
import com.ventoray.popularmovies.web_data_object.Movie;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ADULT;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANGUAGE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_POPULARITY;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_TITLE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VIDEO;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VOTE_COUNT;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.CONTENT_URI;
import static com.ventoray.popularmovies.utils.WebQueryUtils.URL_TYPE_TMDB_POPULAR;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_MOVIE_POPULAR;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_MOVIE_TOP_RATED;
import static com.ventoray.popularmovies.web_data_object.Movie.MOVIE_PARCEL_KEY;
import static com.ventoray.popularmovies.utils.NetworkUtils.checkConnectivity;

public class MainActivity extends AppCompatActivity {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef ({
            POSTER_TYPE_POPULAR,
            POSTER_TYPE_RATING,
            POSTER_TYPE_FAVORITES
    })
    @interface PosterType {}


    private final String TAG = getClass().getSimpleName();
    private List<Movie> mMovies;
    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mPosterAdapter;

    private static final String PREF_FILE = "PreferenceFile";
    private static final String POSTER_TYPE_PREF = "posterTypePref";

    private final String KEY_INSTANCE_STATE_MOVIES = "moviesInstanceStateKey";
    private final String KEY_INSTANCE_STATE_TITLE = "activityTitleKey";

    private static final int POSTER_TYPE_POPULAR = 1000;
    private static final int POSTER_TYPE_RATING = 1001;
    private static final int POSTER_TYPE_FAVORITES = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();

        if (savedInstanceState == null) {
            initializePosters();
        }
    }

    private int getTypePreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return sharedPreferences.getInt(POSTER_TYPE_PREF, POSTER_TYPE_POPULAR);
    }

    private void setPosterTypePref(@PosterType int posterType) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit();
        editor.putInt(POSTER_TYPE_PREF, posterType);
        editor.apply();
    }

    private void initializePosters() {
        switch (getTypePreference()) {
            case POSTER_TYPE_POPULAR:
                getMoviesFromWeb(BASE_URL_MOVIE_POPULAR, null, 1);
                setTitle(R.string.popular);
                break;

            case POSTER_TYPE_RATING:
                getMoviesFromWeb(BASE_URL_MOVIE_TOP_RATED, null, 1);
                setTitle(R.string.rating);
                break;

            case POSTER_TYPE_FAVORITES:
                getFavorites();
                setTitle(R.string.favorites);
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(KEY_INSTANCE_STATE_MOVIES)) {
            List<Movie> movies =
                    savedInstanceState.getParcelableArrayList(KEY_INSTANCE_STATE_MOVIES);
            if (movies != null) {
                mMovies.addAll(movies);
                mPosterAdapter.notifyDataSetChanged();
            }
        }

        if (savedInstanceState.containsKey(KEY_INSTANCE_STATE_TITLE)) {
            String title = savedInstanceState.getString(KEY_INSTANCE_STATE_TITLE);
            setTitle(title);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Movie> movies = new ArrayList<>(mMovies);
        outState.putParcelableArrayList(KEY_INSTANCE_STATE_MOVIES, movies);
        String title = getTitle().toString();
        if (!title.isEmpty()) {
            outState.putString(KEY_INSTANCE_STATE_TITLE, title);
        }
        super.onSaveInstanceState(outState);
    }

    private void setUpRecyclerView() {
        mMovies = new ArrayList<>();
        GridLayoutManager glm = new GridLayoutManager(this, 3);

        mPosterAdapter = new MoviePosterAdapter(mMovies,
                getApplicationContext(), new MoviePosterAdapter.PosterOnClickListener() {
            @Override
            public void onClick(Movie movie) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra(MOVIE_PARCEL_KEY, movie);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mPosterAdapter);
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
            case R.id.action_popular:
                getMoviesFromWeb(BASE_URL_MOVIE_POPULAR, null, 1);
                setTitle(R.string.popular);
                setPosterTypePref(POSTER_TYPE_POPULAR);
                break;


            case R.id.action_rating:
                getMoviesFromWeb(BASE_URL_MOVIE_TOP_RATED, null, 1);
                setTitle(R.string.rating);
                setPosterTypePref(POSTER_TYPE_RATING);
                break;


            case R.id.action_favorites:
                getFavorites();
                setTitle(R.string.favorites);
                setPosterTypePref(POSTER_TYPE_FAVORITES);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param baseUrl -
     * @param type    - will be updated for future functionality
     * @param page
     */
    private void getMoviesFromWeb(String baseUrl, String type, int page) {
        URL url = WebQueryUtils.buildMoviesUrl(baseUrl, type, page);

        if (url != null && checkConnectivity(this)) {
            // passing URL_TYPE_TMDB_POPULAR here now as URI type, but really it could be DISCOVER and TOP_RATED too
            new MovieDataAsyncTask(URL_TYPE_TMDB_POPULAR, new OnMovieDataLoadedListener() {
                @Override
                public void onMovieDataLoaded(Object[] movies) {
                    if (movies != null) {
                        if (movies.length > 0) {
                            mMovies.clear();
                            mMovies.addAll(Arrays.asList((Movie[]) movies));
                            mPosterAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(MainActivity.this,
                                R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute(url);
        } else {
            Toast.makeText(this, R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavorites() {
        Cursor cursor = getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            Toast.makeText(this, R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
            return;
        }
        mMovies.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            if (!cursor.moveToPosition(i)) return;
            Movie movie = new Movie();

            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID));
            int voteCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VOTE_COUNT));
            int voteAverage = cursor.getInt(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW));
            String releaseDate = cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE));
            String posterPath = cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH));
            String originalLanguage = cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_LANGUAGE));
            String originalTitle = cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE));
            String backdropPath = cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH));
            boolean adult = cursor.getInt(cursor.getColumnIndex(COLUMN_ADULT)) > 0;
            boolean video = cursor.getInt(cursor.getColumnIndex(COLUMN_VIDEO)) > 0;
            double popularity = cursor.getDouble(cursor.getColumnIndex(COLUMN_POPULARITY));

            movie.setId(id);
            movie.setVoteCount(voteCount);
            movie.setVoteAverage(voteAverage);
            movie.setTitle(title);
            movie.setOverview(overview);
            movie.setReleaseDate(releaseDate);
            movie.setPosterPath(posterPath);
            movie.setOriginalLanguage(originalLanguage);
            movie.setOriginalTitle(originalTitle);
            movie.setBackdropPath(backdropPath);
            movie.setAdult(adult);
            movie.setVideo(video);
            movie.setPopularity(popularity);

            mMovies.add(movie);

        }
        mPosterAdapter.notifyDataSetChanged();
        cursor.close();

    }
}
