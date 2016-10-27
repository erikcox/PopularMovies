/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.adapters.TrailerAdapter;
import rocks.ecox.popularmovies.models.Movie;
import rocks.ecox.popularmovies.models.Trailer;

import static rocks.ecox.popularmovies.utilities.Constants.APPEND_API_KEY;
import static rocks.ecox.popularmovies.utilities.Constants.TRAILER_BASE_URL;

/**
 * Activity to display movie details once clicked on in MovieActivityFragment
 */

public class MovieDetailActivity extends YouTubeBaseActivity {
    @BindView(R.id.tvTitle) TextView title;
    @BindView(R.id.ivBackdrop) ImageView poster;
    @BindDrawable(R.drawable.poster_placeholder_backdrop)
    Drawable placeholder;
    @BindView(R.id.tvReleaseDate) TextView releaseDate;
    @BindView(R.id.tvRating) TextView rating;
    @BindView(R.id.tvSynopsis) TextView synopsis;
    AsyncHttpClient client = new AsyncHttpClient();
    public List<String> youTubeTrailerKeys = new ArrayList<String>();
    public Movie movie;
    TrailerAdapter adapter;
    RecyclerView rvTrailer;
    public  ArrayList<Trailer> mTrailers = new ArrayList<Trailer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        /** Change the ActionBar title */
//        getActionBar().setTitle(R.string.title_movie_details);

        /** Populate the ImageView and TextViews from the parcelable */
        if (intent != null && intent.hasExtra("movie")) {
            movie = intent.getExtras().getParcelable("movie");
            try {
                fetchMoviesAsync(movie.getId(), this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

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

    public void fetchMoviesAsync(String movieId, final Activity activity) throws MalformedURLException {
        String url = String.format(TRAILER_BASE_URL, movieId) + APPEND_API_KEY;
        String urlReview = String.format(TRAILER_BASE_URL, movieId) + "reviews" + APPEND_API_KEY;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray trailersJsonResults = null;

                try {
                    trailersJsonResults = response.getJSONArray("results");
                    mTrailers.addAll(Trailer.fromJSONArray(trailersJsonResults));
                    for (Trailer t : mTrailers) {
                        youTubeTrailerKeys.add(t.getKey());
                    }

                    movie.setTrailerKeys(youTubeTrailerKeys);

                    // Set up trailers in RecyclerView
                    adapter = new TrailerAdapter(MovieDetailActivity.this, mTrailers);
                    rvTrailer = (RecyclerView) findViewById(R.id.rvTrailer);
                    rvTrailer.setAdapter(adapter);
                    rvTrailer.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
