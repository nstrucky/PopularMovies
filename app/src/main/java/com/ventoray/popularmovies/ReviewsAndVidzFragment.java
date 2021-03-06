package com.ventoray.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.ventoray.popularmovies.adapters.OnVideoItemClickedListener;
import com.ventoray.popularmovies.adapters.ReviewsRecyclerAdapter;
import com.ventoray.popularmovies.adapters.VideosRecyclerAdapter;
import com.ventoray.popularmovies.async.MovieDataAsyncTask;
import com.ventoray.popularmovies.async.OnMovieDataLoadedListener;
import com.ventoray.popularmovies.utils.WebQueryUtils;
import com.ventoray.popularmovies.web_data_object.Movie;
import com.ventoray.popularmovies.web_data_object.Review;
import com.ventoray.popularmovies.web_data_object.VideoData;
import com.ventoray.popularmovies.utils.WebApiConstants;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ventoray.popularmovies.utils.WebQueryUtils.URL_TYPE_TMDB_REVIEWS;
import static com.ventoray.popularmovies.utils.WebQueryUtils.URL_TYPE_TMDB_VIDEOS;
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


    private final String KEY_VIDEOS_LIST = "videoListKey";
    private final String KEY_REVIEWS_LIST = "reviewListKey";

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

        View view = inflater.inflate(R.layout.fragment_reviews_and_vidz,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mMovie = ((MovieDetailsActivity) getActivity()).getMovie();
        mPageType =
                getArguments() != null ? getArguments().getInt(PAGE_TYPE_KEY) : PAGE_TYPE_REVIEWS;

        switch (mPageType) {
            case PAGE_TYPE_VIDEOS:
                setUpVideosView(savedInstanceState);
                break;

            case PAGE_TYPE_REVIEWS:
                setUpReviewsView(savedInstanceState);

                break;
            default:
                    Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<VideoData> videoData = new ArrayList<>(mVideoDataList);
        ArrayList<Review> reviews = new ArrayList<>(mReviews);
        outState.putParcelableArrayList(KEY_VIDEOS_LIST, videoData);
        outState.putParcelableArrayList(KEY_REVIEWS_LIST, reviews);

        super.onSaveInstanceState(outState);
    }

    private void setUpReviewsView(Bundle savedInstanceState) {
        mReviewAdapter = new ReviewsRecyclerAdapter(mReviews, mContext);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mReviewAdapter);

        if (savedInstanceState == null) {
            getReviews(String.valueOf(mMovie.getId()));
        } else if (savedInstanceState.containsKey(KEY_REVIEWS_LIST)) {
            ArrayList<Review> reviews = savedInstanceState.getParcelableArrayList(KEY_REVIEWS_LIST);
            if (reviews != null) {
                mReviews.addAll(reviews);
                mReviewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setUpVideosView(Bundle savedInstanceState) {
        mVideosAdapter = new VideosRecyclerAdapter(mContext, mVideoDataList,
                new OnVideoItemClickedListener() {
                    @Override
                    public void onVideoItemClicked(String videoId) {
                        Uri uri = Uri.parse(WebApiConstants.YouTube.BASE_WATCH_URL)
                                .buildUpon()
                                .appendQueryParameter(WebApiConstants.YouTube.PARAM_V, videoId)
                                .build();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mVideosAdapter);
        if (savedInstanceState == null) {
            getVideoDataList(String.valueOf(mMovie.getId()));
        } else if (savedInstanceState.containsKey(KEY_VIDEOS_LIST)) {
            List<VideoData> videoData = savedInstanceState.getParcelableArrayList(KEY_VIDEOS_LIST);
            if (videoData != null) {
                mVideoDataList.addAll(videoData);
                mVideosAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * Retrieves the comments (reviews) related to the Movie object associated with the MovieDetailsActivity
     * @param movieId ID of Object passed to MoviesDetailsActivity
     */
    private void getReviews(String movieId) {
        URL url = WebQueryUtils.buildMovieDataUrl(movieId, PATH_MOVIE_REVIEWS);
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
                                R.string.error_retrieve_data,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute(url);
        } else {
            Toast.makeText(mContext,
                    R.string.error_retrieve_data,
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Retrieves data for videos associated with the activity's Movie, stores them in an ArrayList
     * of VideoData objects.
     * @param movieId ID of Object passed to MoviesDetailsActivity
     */
    private void getVideoDataList(String movieId) {
        URL url = WebQueryUtils.buildMovieDataUrl(movieId, PATH_MOVIE_VIDEOS);
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
                                    R.string.error_retrieve_data,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute(url);

        } else {
            Toast.makeText(mContext,
                    R.string.error_retrieve_data,
                    Toast.LENGTH_SHORT).show();
        }
    }

}
