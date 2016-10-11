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

import java.util.ArrayList;

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

        /** Populate the ImageView and TextViews from the Intent array */
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            ArrayList<String> movieDetailLst = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);
            ImageView poster = (ImageView) findViewById(R.id.ivPosterThumbnail);

            ((TextView) findViewById(R.id.tvTitle))
                    .setText(movieDetailLst.get(0));

            Picasso.with(getApplicationContext())
                    .load(movieDetailLst.get(1))
                    .error(R.drawable.poster_placeholder)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(poster);

            ((TextView) findViewById(R.id.tvReleaseDate))
                    .setText(movieDetailLst.get(2));

            ((TextView) findViewById(R.id.tvRating))
                    .setText(movieDetailLst.get(3));

            ((TextView) findViewById(R.id.tvSynopsis))
                    .setText(movieDetailLst.get(4));
        }
    }
}
