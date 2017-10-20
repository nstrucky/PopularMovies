package com.ventoray.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.ventoray.popularmovies.DBConstants.BASE_URL_IMAGE;
import static com.ventoray.popularmovies.DBConstants.W342;
import static com.ventoray.popularmovies.DBConstants.W780;
import static com.ventoray.popularmovies.MainActivity.SERIALIZABLE_MOVIE_KEY;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mMovieTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private RatingBar mRatingBar;
    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setUpActionBar();
        bindViews();
        Intent passedIntent = getIntent();

        if (passedIntent.hasExtra(SERIALIZABLE_MOVIE_KEY)) {
            mMovie = (Movie) passedIntent.getSerializableExtra(SERIALIZABLE_MOVIE_KEY);

        }

        retrievePoster();
    }

    private void bindViews() {
        mMovieTextView = (TextView) findViewById(R.id.tv_movie_title);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    private void retrievePoster() {
        if (mMovie != null) {
            String posterPath = mMovie.getPosterPath();


            if (posterPath != null && !posterPath.isEmpty())
                Picasso.with(this).load(BASE_URL_IMAGE + W780 + posterPath).into(mPosterImageView);

            mMovieTextView.setText(mMovie.getTitle());
            mSynopsisTextView.setText(mMovie.getOverview());
            mRatingBar.setRating(mMovie.getVoteAverage()/2);



        }

    }


    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }
}
