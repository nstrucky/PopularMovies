package com.ventoray.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ventoray.popularmovies.R;
import com.ventoray.popularmovies.data_object.VideoData;

import java.util.List;

/**
 * Created by nicks on 11/15/2017.
 */

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.VideoViewHolder> {

    private Context mContext;
    private List<VideoData> mVideoDataList;
    private OnVideoItemClickedListener mListener;

    public VideosRecyclerAdapter(Context context, List<VideoData> videoDataList, OnVideoItemClickedListener listener) {
        super();
        mContext = context;
        mVideoDataList = videoDataList;
        mListener = listener;

    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoData videoData = mVideoDataList.get(position);
        String videoId = videoData.getKey();
        Picasso.with(mContext)
                .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                .into(holder.thumbnailImageView);

        holder.videoTitleTextView.setText(videoData.getName());

    }

    @Override
    public int getItemCount() {
        return mVideoDataList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnailImageView;
        TextView videoTitleTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);

            thumbnailImageView = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            videoTitleTextView = (TextView) itemView.findViewById(R.id.tv_video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            VideoData data = mVideoDataList.get(getAdapterPosition());
            String videoKey = data.getKey();
            mListener.onVideoItemClicked(videoKey);
        }
    }
}



//https://img.youtube.com/vi/<insert-youtube-video-id-here>/0.jpg