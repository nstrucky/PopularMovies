package com.ventoray.popularmovies.data_object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nick on 10/15/2017.
 */

public class Movie implements Parcelable {

    public static final String MOVIE_PARCEL_KEY = "movieParcelKey";

    private int mId;
    private int mVoteCount;
    private double mVoteAverage;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mPosterPath;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private String mBackdropPath;
    private boolean mAdult;
    private boolean mVideo;
    private double mPopularity;

    public Movie() {
    }

    /**
     * Called by creator to initialize variables
     * @param in - Parcel containing class variables.  Note that they must be read in the same
     *           order that they were written to the Parcel.
     */
    private Movie(Parcel in) {
        mId = in.readInt();
        mVoteCount = in.readInt();
        mVoteAverage = in.readDouble();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mPosterPath = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mBackdropPath = in.readString();
        mAdult = in.readByte() != 0; // written out as out.writeByte((byte) (mAdult ? 1 : 0));
        mVideo = in.readByte() != 0;
        mPopularity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
        out.writeInt(mVoteCount);
        out.writeDouble(mVoteAverage);
        out.writeString(mTitle);
        out.writeString(mOverview);
        out.writeString(mReleaseDate);
        out.writeString(mPosterPath);
        out.writeString(mOriginalLanguage);
        out.writeString(mOriginalTitle);
        out.writeString(mBackdropPath);
        out.writeByte((byte) (mAdult ? 1 : 0));
        out.writeByte((byte) (mVideo ? 1 : 0));
        out.writeDouble(mPopularity);

    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };



    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String mOriginalLanguage) {
        this.mOriginalLanguage = mOriginalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double mPopularity) {
        this.mPopularity = mPopularity;
    }

}