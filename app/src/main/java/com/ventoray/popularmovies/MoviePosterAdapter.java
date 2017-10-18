package com.ventoray.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.ventoray.popularmovies.DBConstants.BASE_URL_IMAGE;
import static com.ventoray.popularmovies.DBConstants.ORIGINAL;
import static com.ventoray.popularmovies.DBConstants.W185;
import static com.ventoray.popularmovies.DBConstants.W342;
import static com.ventoray.popularmovies.DBConstants.W500;

/**
 * Created by Nick on 10/16/2017.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    interface PosterOnClickListener {
        void onClick(Movie movie);
    }

    private List<Movie> movies;
    private Context context;
    private PosterOnClickListener listener;

    public MoviePosterAdapter(List<Movie> movies, Context context, PosterOnClickListener listener) {
        this.movies = movies;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String posterPath = movie.getPosterPath();

        if (posterPath != null && !posterPath.isEmpty()) {
            Picasso.with(context).load(BASE_URL_IMAGE + W342 + posterPath)
                    .into(holder.moviePosterImageView);
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onClick(movies.get(adapterPosition));
        }
    }




}
