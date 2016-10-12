/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Activity to display movie details once clicked on in MainActivityFragment
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = this.getIntent();
        /** Change the ActionBar title */
        getSupportActionBar().setTitle(R.string.title_movie_details);

        /** Populate the ImageView and TextViews from the parcelable */
        if (intent != null && intent.hasExtra("movie")) {
            Movie movie = intent.getExtras().getParcelable("movie");
            ImageView poster = (ImageView) findViewById(R.id.ivPosterThumbnail);

            ((TextView) findViewById(R.id.tvTitle))
                    .setText(movie.getTitle());

            Picasso.with(getApplicationContext())
                    .load(movie.getPoster())
                    .error(R.drawable.poster_placeholder)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(poster);

            ((TextView) findViewById(R.id.tvReleaseDate))
                    .setText(movie.getReleaseDate());

            ((TextView) findViewById(R.id.tvRating))
                    .setText(movie.getUserRating());

            ((TextView) findViewById(R.id.tvSynopsis))
                    .setText(movie.getSynopsis());
        }
    }
}
