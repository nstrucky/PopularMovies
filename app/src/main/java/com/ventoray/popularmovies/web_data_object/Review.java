package com.ventoray.popularmovies.web_data_object;

/**
 * Created by Nick on 11/13/2017.
 */

public class Review {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public Review() {}

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}

