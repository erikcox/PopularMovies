/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Utility {

    public static String getSortKey(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        return pref.getString("SortBy", "popular");
    }

    public static void setSortKey(Activity activity, String choice) {
        SharedPreferences pref = activity.getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SortBy",choice);
        editor.apply();
    }
}
