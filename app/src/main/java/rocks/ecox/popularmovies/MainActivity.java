/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import rocks.ecox.popularmovies.utilities.Utility;

/**
 * MainActivity
 */

public class MainActivity extends AppCompatActivity {

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
        String ratingSortTitle = getResources().getString(R.string.title_sort_rating);
        String popularSortTitle = getResources().getString(R.string.title_sort_popular);
        String ratingSortKey = getResources().getString(R.string.action_sort_rating);
        String popularSortKey = getResources().getString(R.string.action_sort_popular);
        String popularAppName = getResources().getString(R.string.app_name_rating);
        String ratingAppName = getResources().getString(R.string.app_name);

        if(sortBy.equals(popularSortKey)) {
            item.setTitle(ratingSortTitle);
            getSupportActionBar().setTitle(ratingAppName);
            Utility.setSortKey(activity, ratingSortKey);
        } else {
            item.setTitle(popularSortTitle);
            getSupportActionBar().setTitle(popularAppName);
            Utility.setSortKey(activity, popularSortKey);

        }

        return super.onOptionsItemSelected(item);
    }

}
