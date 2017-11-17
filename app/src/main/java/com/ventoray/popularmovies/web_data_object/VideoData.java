package com.ventoray.popularmovies.web_data_object;

/**
 * Created by nicks on 11/15/2017.
 */

public class VideoData {

    private String mId;
    private String mIso6391;
    private String mIso31661;
    private String mKey;
    private String mName;
    private String mWebSite;
    private int mSize;
    private String mType;


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getIso6391() {
        return mIso6391;
    }

    public void setIso6391(String mIso6391) {
        this.mIso6391 = mIso6391;
    }

    public String getIso31661() {
        return mIso31661;
    }

    public void setIso31661(String mIso31661) {
        this.mIso31661 = mIso31661;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getWebSite() {
        return mWebSite;
    }

    public void setWebSite(String mWebSite) {
        this.mWebSite = mWebSite;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
