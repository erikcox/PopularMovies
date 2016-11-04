/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.fragments.DetailFragment;
import rocks.ecox.popularmovies.models.Movie;


/**
 * Detail Activity to display movie details once clicked on in MovieFragment
 */

public class DetailActivity extends AppCompatActivity {
    public Movie movie;
    Fragment fragmentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();

        if (savedInstanceState == null) {
            /** Fetch the movie from the Parcelable */
            if (intent != null && intent.hasExtra("movie")) {
                movie = intent.getExtras().getParcelable("movie");
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);

            /** Replace the FrameLayout placeholder in the tablet layout with the movie object */
            fragmentDetail = DetailFragment.newInstance(movie);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                .replace(R.id.flDetailContainer, fragmentDetail)
                .commit();
        }
    }
}
