package com.ventoray.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ventoray.popularmovies.adapters.ReviewVideoPagerAdapter;
import com.ventoray.popularmovies.data_favorites.FavoritesContract;
import com.ventoray.popularmovies.web_data_object.Movie;
import com.ventoray.popularmovies.utils.DateUtil;

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
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_IMAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.W780;
import static com.ventoray.popularmovies.web_data_object.Movie.MOVIE_PARCEL_KEY;
import static com.ventoray.popularmovies.utils.NetworkUtils.checkConnectivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mMovieTextView;
    private TextView mSynopsisTextView;
    private TextView mReleaseTextView;
    private ImageView mPosterImageView;
    private Menu mMenu;
    private boolean mIsFavorite;

    private RatingBar mRatingBar;
    private Movie mMovie;

    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mToast = Toast.makeText(this, "placeholder", Toast.LENGTH_SHORT);
        setUpActionBar();
        bindViews();
        Intent passedIntent = getIntent();

        if (passedIntent.hasExtra(MOVIE_PARCEL_KEY)) {
            mMovie = (Movie) passedIntent.getParcelableExtra(MOVIE_PARCEL_KEY);

        }
        setUpViewPager();
        updateUI();
    }

    private void setUpViewPager() {
        ViewPager mPager = findViewById(R.id.pager_details);
        TabLayout mTabLayout = findViewById(R.id.tablayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(getColor(R.color.colorPrimary), getColor(R.color.yellow));
        mTabLayout.setupWithViewPager(mPager);
        mPager.setAdapter(new ReviewVideoPagerAdapter(getSupportFragmentManager(), this));
    }

    private void bindViews() {
        mMovieTextView = (TextView) findViewById(R.id.tv_movie_title);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        mReleaseTextView = (TextView) findViewById(R.id.tv_release_date);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    private void updateUI() {
        if (mMovie != null) {
            String posterPath = mMovie.getPosterPath();
            String releaseDate = DateUtil.formatTmdbDate(mMovie.getReleaseDate());

            Log.d("DETAILS", "Date: " + mMovie.getReleaseDate());

            if (posterPath != null && checkConnectivity(this)){
                if (!posterPath.isEmpty()) {
                    Picasso.with(this).load(BASE_URL_IMAGE + W780 + posterPath).into(mPosterImageView);
                }
            }

            mMovieTextView.setText(mMovie.getTitle());
            mSynopsisTextView.setText(mMovie.getOverview());
            mReleaseTextView.setText(releaseDate);
            mRatingBar.setRating((float) mMovie.getVoteAverage()/2);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        mMenu = menu;

        if (mIsFavorite = checkFavorite()) {
            DrawableCompat.setTint(menu.findItem(R.id.action_favorite).getIcon(),
                    ContextCompat.getColor(this, R.color.colorAccent));
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_favorite:
                if (!mIsFavorite) {
                    saveFavorite();
                } else {
                    removeFromFavorites();
                }

                break;
        }

        logItems();
        return false;
    }


    /**
     * Saves the Movie object associated with this activity in favorites table in favorites.db
     */
    private void saveFavorite() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_MOVIE_ID, mMovie.getId());
        values.put(COLUMN_VOTE_COUNT, mMovie.getVoteCount());
        values.put(COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
        values.put(COLUMN_TITLE, mMovie.getTitle());
        values.put(COLUMN_OVERVIEW, mMovie.getOverview());
        values.put(COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        values.put(COLUMN_POSTER_PATH, mMovie.getPosterPath());
        values.put(COLUMN_ORIGINAL_LANGUAGE, mMovie.getOriginalLanguage());
        values.put(COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
        values.put(COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
        values.put(COLUMN_ADULT, mMovie.isAdult());
        values.put(COLUMN_VIDEO, mMovie.isVideo());
        values.put(COLUMN_POPULARITY, mMovie.getPopularity());

        Uri returnedUri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, values);

        if (returnedUri == null) {
            mToast.setText(R.string.error_save_favs);
            mToast.show();
        }

        mToast.setText(R.string.added_to_favs);
        mToast.show();
        DrawableCompat.setTint(mMenu.findItem(R.id.action_favorite).getIcon(), ContextCompat.getColor(this, R.color.colorAccent));
        mIsFavorite = true;
    }

    /**
     * Removes the Movie object associated with this activity from the favorites table in
     * the favorites.db
     */
    private void removeFromFavorites() {
        String movieId = String.valueOf(mMovie.getId());

        int rowsRmoved =
                getContentResolver().delete(CONTENT_URI, COLUMN_MOVIE_ID, new String[] {movieId});

        if (rowsRmoved < 1) {
            mToast.setText(R.string.error_remove_favs);
            mToast.show();
            return;
        }

        mToast.setText(R.string.removed_from_favs);
        mToast.show();
        DrawableCompat.setTint(mMenu.findItem(R.id.action_favorite).getIcon(), ContextCompat.getColor(this, R.color.colorPrimaryDark));

    }


    /**
     * Determines whether the movie object associated with this Activity is a favorite
     * @return - boolean is a favorite or not
     */
    private boolean checkFavorite() {
        Cursor cursor = getContentResolver().query(
                CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID, COLUMN_TITLE},
                null,
                null,
                null
        );

        if (cursor == null) return false;

        for (int i = 0; i < cursor.getCount(); i++) {
            if (!cursor.moveToPosition(i)) return false;

            int movieId = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID));
            if (movieId == mMovie.getId()) {
                cursor.close();
                return true;
            }
        }
        return false;
    }


    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public Movie getMovie() {
        return mMovie;
    }



    /**
     * used only for debugging purposes
     */
    private void logItems() {
        Cursor cursor = getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );

        for (int i = 0; i < cursor.getCount(); i++) {
            if (!cursor.moveToPosition(i)) return;
            int movieID = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));

            Log.i("DETAILS Acitivty",
                    String.format("Movie ID: %d%nMovie Title: %s%n",
                            movieID, title));
        }
        cursor.close();
    }
}
