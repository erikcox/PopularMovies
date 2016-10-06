/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Error here
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MoviePrefs", MODE_PRIVATE);
        Editor editor = pref.edit();

        if(item.getTitle() == getResources().getString(R.string.action_sort_popular) && getSupportActionBar() != null) {
            item.setTitle(getResources().getString(R.string.action_sort_rating));
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            editor.putString("SortBy", "popular");
        } else {
            item.setTitle(getResources().getString(R.string.action_sort_popular));
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name_rating));
            }
            editor.putString("SortBy", "top_rated");
        }

        editor.apply();
        return super.onOptionsItemSelected(item);

    }

}
