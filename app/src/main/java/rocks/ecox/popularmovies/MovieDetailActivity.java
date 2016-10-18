/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rocks.ecox.popularmovies.models.Movie;

/**
 * Activity to display movie details once clicked on in MainActivityFragment
 */

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle) TextView title;
    @BindView(R.id.ivBackdrop) ImageView poster;
    @BindDrawable(R.drawable.poster_placeholder)
    Drawable placeholder;
    @BindView(R.id.tvReleaseDate) TextView releaseDate;
    @BindView(R.id.tvRating) TextView rating;
    @BindView(R.id.tvSynopsis) TextView synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        Intent intent = this.getIntent();
        /** Change the ActionBar title */
        getSupportActionBar().setTitle(R.string.title_movie_details);

        /** Populate the ImageView and TextViews from the parcelable */
        if (intent != null && intent.hasExtra("movie")) {
            Movie movie = intent.getExtras().getParcelable("movie");

            Picasso.with(getApplicationContext())
                    .load(movie.getBackdrop())
                    .error(placeholder)
                    .placeholder(placeholder)
                    .into(poster);

            title.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
            rating.setText(movie.getUserRating().toString());
            synopsis.setText(movie.getSynopsis());
        }
    }
}
