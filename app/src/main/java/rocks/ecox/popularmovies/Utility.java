/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * A class of common tasks
 */

public class Utility {

    /** Gets the value of the sortBy sharedPreference */
    static String getSortKey(Activity activity) {
        SharedPreferences mPref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        return mPref.getString("SortBy", "popular");
    }

    /** Saves the value of sortBy to a sharedPreference */
    static void setSortKey(Activity activity, String choice) {
        SharedPreferences mPref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPref.edit();
        mEditor.putString("SortBy",choice);
        mEditor.apply();
    }

    /** Checks to see if a network connection is available */
    static boolean isOnline(Context context) {
        ConnectivityManager mCm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetInfo = mCm.getActiveNetworkInfo();
        return mNetInfo != null && mNetInfo.isConnected();
    }
}
