package com.ventoray.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ventoray.popularmovies.ReviewsAndVidzFragment;

/**
 * Created by Nick on 11/13/2017.
 */

public class ReviewVideoPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_PAGES = 2;

    public ReviewVideoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle;
        switch (position) {
            case 0:
                pageTitle = "Videos!";
                break;

            case 1:
                pageTitle = "Reviews~!";
                break;

            default:
                pageTitle = "Error";
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
