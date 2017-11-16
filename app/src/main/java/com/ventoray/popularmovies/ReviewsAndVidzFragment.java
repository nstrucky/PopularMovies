package com.ventoray.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ventoray.popularmovies.adapters.ReviewsRecyclerAdapter;
import com.ventoray.popularmovies.adapters.VideosRecyclerAdapter;
import com.ventoray.popularmovies.async.MovieDataAsyncTask;
import com.ventoray.popularmovies.async.OnMovieDataLoadedListener;
import com.ventoray.popularmovies.data_object.Movie;
import com.ventoray.popularmovies.data_object.Review;
import com.ventoray.popularmovies.data_object.VideoData;
import com.ventoray.popularmovies.utils.QueryUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ventoray.popularmovies.utils.QueryUtils.URL_TYPE_TMDB_REVIEWS;
import static com.ventoray.popularmovies.utils.QueryUtils.URL_TYPE_TMDB_VIDEOS;
import static com.ventoray.popularmovies.utils.NetworkUtils.checkConnectivity;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PATH_MOVIE_REVIEWS;
import static com.ventoray.popularmovies.utils.WebApiConstants.TMDB.PATH_MOVIE_VIDEOS;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsAndVidzFragment extends Fragment {

    private static final int PAGE_TYPE_VIDEOS = 1000;
    private static final int PAGE_TYPE_REVIEWS = 1001;
    private static final String PAGE_TYPE_KEY = "pageTypeKey";

    private List<Review> mReviews;
    private List<VideoData> mVideoDataList;
    private ReviewsRecyclerAdapter mReviewAdapter;
    private VideosRecyclerAdapter mVideosAdapter;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private Movie mMovie;
    private int mPageType = PAGE_TYPE_REVIEWS;



    public ReviewsAndVidzFragment() {
        // Required empty public constructor
    }

    public static ReviewsAndVidzFragment newInstance(int position) {
        ReviewsAndVidzFragment fragment = new ReviewsAndVidzFragment();
        Bundle args = new Bundle();
        int pageType;
        switch (position) {
            case 0:
                pageType = PAGE_TYPE_VIDEOS;
                break;

            case 1:
                pageType = PAGE_TYPE_REVIEWS;
                break;

            default:
                pageType = PAGE_TYPE_REVIEWS;
        }
        args.putInt(PAGE_TYPE_KEY, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        mReviews = new ArrayList<>();
        mVideoDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reviews_and_vidz, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mMovie = ((MovieDetailsActivity) getActivity()).getMovie();
        mPageType =
                getArguments() != null ? getArguments().getInt(PAGE_TYPE_KEY) : PAGE_TYPE_REVIEWS;


        switch (mPageType) {
            case PAGE_TYPE_VIDEOS:
                setUpVideosView();
                break;

            case PAGE_TYPE_REVIEWS:
                setUpReviewsView();

                break;
            default:
                    Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void setUpReviewsView() {
        mReviewAdapter = new ReviewsRecyclerAdapter(mReviews, mContext);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mReviewAdapter);
        getReviews(String.valueOf(mMovie.getId()));
    }


    private void setUpVideosView() {
        mVideosAdapter = new VideosRecyclerAdapter(mContext, mVideoDataList);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mVideosAdapter);
        getVideoDataList(String.valueOf(mMovie.getId()));

    }

    /**
     * Retrieves the comments (reviews) related to the Movie object associated with the MovieDetailsActivity
     * @param movieId ID of Object passed to MoviesDetailsActivity
     */
    private void getReviews(String movieId) {
        URL url = QueryUtils.buildMovieDataUrl(movieId, PATH_MOVIE_REVIEWS);
        if (url != null && checkConnectivity(mContext)) {
            new MovieDataAsyncTask(URL_TYPE_TMDB_REVIEWS, new OnMovieDataLoadedListener() {
                @Override
                public void onMovieDataLoaded(Object[] reviews) {
                    if (reviews != null) {
                        if (reviews.length > 0) {
                            mReviews.addAll(Arrays.asList((Review[])reviews));
                            mReviewAdapter.notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(mContext,
                                R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute(url);
        } else {
            Toast.makeText(mContext, R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Retrieves data for videos associated with the activity's Movie, stores them in an ArrayList
     * of VideoData objects.
     * @param movieId ID of Object passed to MoviesDetailsActivity
     */
    private void getVideoDataList(String movieId) {
        URL url = QueryUtils.buildMovieDataUrl(movieId, PATH_MOVIE_VIDEOS);
        if (url != null && checkConnectivity(mContext)) {
                new MovieDataAsyncTask(URL_TYPE_TMDB_VIDEOS, new OnMovieDataLoadedListener() {
                    @Override
                    public void onMovieDataLoaded(Object[] videoDataArray) {
                        if (videoDataArray.length > 0) {
                            mVideoDataList.addAll(Arrays.asList((VideoData[]) videoDataArray));
                            mVideosAdapter.notifyDataSetChanged();
                            for (VideoData videoData : mVideoDataList) {
                                Log.i("GET VIDEO DATA LIST", "Name: " + videoData.getName());
                            }
                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute(url);

        } else {
            Toast.makeText(mContext, R.string.error_retrieve_data, Toast.LENGTH_SHORT).show();
        }
    }

}
