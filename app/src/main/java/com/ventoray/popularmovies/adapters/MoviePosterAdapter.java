package com.ventoray.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ventoray.popularmovies.data_object.Movie;
import com.ventoray.popularmovies.R;

import java.util.List;

import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.BASE_URL_IMAGE;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.W185;
/**
 * Created by Nick on 10/16/2017.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    public interface PosterOnClickListener {
        void onClick(Movie movie);
    }

    private List<Movie> movies;
    private Context context;
    private PosterOnClickListener listener;
    private int screenWidth;

    public MoviePosterAdapter(List<Movie> movies, Context context, PosterOnClickListener listener) {
        this.movies = movies;
        this.context = context;
        this.listener = listener;

        screenWidth = context.getResources().getConfiguration().screenWidthDp;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String posterPath = movie.getPosterPath();

        if (posterPath != null && !posterPath.isEmpty()) {

            int width = screenWidth/3;

            // TODO: 10/19/2017 find out when recyclerview actually measures children
            Picasso.with(context).load(BASE_URL_IMAGE + W185 + posterPath)
                    .resize(width, 180)
                    .centerCrop()
                    .into(holder.moviePosterImageView);

        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    /**************************************************************************************
     *
     */
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
