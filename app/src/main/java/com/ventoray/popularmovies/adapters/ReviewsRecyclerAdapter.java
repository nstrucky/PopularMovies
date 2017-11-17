package com.ventoray.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ventoray.popularmovies.R;
import com.ventoray.popularmovies.web_data_object.Review;

import java.util.List;

/**
 * Created by Nick on 11/14/2017.
 */

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsViewHolder> {

    private List<Review> mReviews;
    private Context mContext;

    public ReviewsRecyclerAdapter(List<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

        String review = mReviews.get(position).getmContent();
        String author = mReviews.get(position).getmAuthor();

        holder.reviewTextView.setText(review);
        holder.authorTextView.setText(author);

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView authorTextView;
        TextView reviewTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            reviewTextView = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

}
