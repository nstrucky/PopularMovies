package com.ventoray.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by nicks on 10/21/2017.
 */

public class NetworkUtils {
    /**
     *  Checks for internet connectivity - will need to replace with a network state broadcast listener
     * @return
     */
    public static boolean checkConnectivity(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            }
        }

        return false;
    }

}
