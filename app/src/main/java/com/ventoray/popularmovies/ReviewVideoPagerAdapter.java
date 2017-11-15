package com.ventoray.popularmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ventoray.popularmovies.ReviewsAndVidzFragment;

/**
 * Created by Nick on 11/13/2017.
 */

public class ReviewVideoPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_PAGES = 2;
    private Context mContext;

    public ReviewVideoPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle;
        switch (position) {
            case 0:
                pageTitle = mContext.getString(R.string.videos);
                break;

            case 1:
                pageTitle = mContext.getString(R.string.reviews);
                break;

            default:
                pageTitle = mContext.getString(R.string.error);
        }



        return pageTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return ReviewsAndVidzFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }



}
