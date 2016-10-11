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

public class Utility {

    static String getSortKey(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        return pref.getString("SortBy", "popular");
    }

    static void setSortKey(Activity activity, String choice) {
        SharedPreferences pref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SortBy",choice);
        editor.apply();
    }

    static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
