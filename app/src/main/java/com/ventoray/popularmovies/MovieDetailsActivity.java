package com.ventoray.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.ventoray.popularmovies.MainActivity.SERIALIZABLE_MOVIE_KEY;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mMovieTextView;
    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMovieTextView = (TextView) findViewById(R.id.tv_movie_title);
        Intent passedIntent = getIntent();

        if (passedIntent.hasExtra(SERIALIZABLE_MOVIE_KEY)) {
            mMovie = (Movie) passedIntent.getSerializableExtra(SERIALIZABLE_MOVIE_KEY);

            mMovieTextView.setText(mMovie.getTitle());
        }
    }
}
