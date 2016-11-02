/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.utilities.Utility;

/**
 * MovieActivity
 */

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Toast.makeText(this, "KEY: " + sortBy, Toast.LENGTH_SHORT).show();

        // Titles
        String ratingSortTitle = getResources().getString(R.string.title_sort_rating);
        String popularSortTitle = getResources().getString(R.string.title_sort_popular);
        String favoriteSortTitle = getResources().getString(R.string.title_sort_favorite);

        // Sort keys
        String ratingSortKey = getResources().getString(R.string.action_sort_rating);
        String popularSortKey = getResources().getString(R.string.action_sort_popular);
        String favoriteSortKey = getResources().getString(R.string.action_sort_favorite);

        // ActionBar names
        String popularAppName = getResources().getString(R.string.app_name_rating);
        String ratingAppName = getResources().getString(R.string.app_name);
        String favoriteAppName = getResources().getString(R.string.app_name_favorite);

        if(sortBy.equals(popularSortKey)) {
            item.setTitle(ratingSortTitle);
            Utility.setSortKey(activity, ratingSortKey);
        } else if(sortBy.equals(ratingSortKey)) {
            item.setTitle(popularSortTitle);
            Utility.setSortKey(activity, popularSortKey);
        } else if(sortBy.equals(favoriteSortKey)) {
            item.setTitle(favoriteSortTitle);
            Utility.setSortKey(activity, favoriteSortKey);
        }

        return super.onOptionsItemSelected(item);
    }

}
