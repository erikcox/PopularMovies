/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.utilities.Utility;

/**
 * MovieActivity to display a grid of movie posters
 */

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        // Add Stetho for debugging
        Stetho.initializeWithDefaults(this);
    }

    /** Creates the sort menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    /** Saves sort order as a sharedPreference and updates the menu text and ActionBar title */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity activity = this;
        String sortBy = Utility.getSortKey(activity);

        // Titles
        String ratingSortTitle = getResources().getString(R.string.title_sort_rating);
        String popularSortTitle = getResources().getString(R.string.title_sort_popular);
        String favoriteSortTitle = getResources().getString(R.string.title_sort_favorite);

        // Sort keys
        String ratingSortKey = getResources().getString(R.string.action_sort_rating);
        final String popularSortKey = getResources().getString(R.string.action_sort_popular);
        String favoriteSortKey = getResources().getString(R.string.action_sort_favorite);

        // ActionBar names
        String popularAppName = getResources().getString(R.string.app_name_rating);
        String ratingAppName = getResources().getString(R.string.app_name);
        String favoriteAppName = getResources().getString(R.string.app_name_favorite);

        if(item.toString().equals(popularSortTitle)) {
            Utility.setSortKey(activity, popularSortKey);
        } else if(item.toString().equals(ratingSortTitle)) {
            Utility.setSortKey(activity, ratingSortKey);
        } else if(item.toString().equals(favoriteSortTitle)) {
            Utility.setSortKey(activity, favoriteSortKey);
        }

        return super.onOptionsItemSelected(item);
    }

}
