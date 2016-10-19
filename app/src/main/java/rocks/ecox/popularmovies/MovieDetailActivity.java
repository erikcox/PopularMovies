/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import rocks.ecox.popularmovies.models.Movie;
import rocks.ecox.popularmovies.models.Trailer;

import static rocks.ecox.popularmovies.BuildConfig.YOUTUBE_API_KEY;
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
    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;
    AsyncHttpClient client = new AsyncHttpClient();
    public String youTubeTrailerKey = "5xVh-7ywKpE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        /** Change the ActionBar title */
        //getSupportActionBar().setTitle(R.string.title_movie_details);

        /** Populate the ImageView and TextViews from the parcelable */
        if (intent != null && intent.hasExtra("movie")) {
            Movie movie = intent.getExtras().getParcelable("movie");
            try {
                fetchMoviesAsync(movie.getId());
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

    public void fetchMoviesAsync(String movieId) throws MalformedURLException {
        String url = String.format(TRAILER_BASE_URL, movieId) + APPEND_API_KEY;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray trailersJsonResults = null;

                try {
                    ArrayList<Trailer> mTrailers = new ArrayList<Trailer>();
                    trailersJsonResults = response.getJSONArray("results");
                    mTrailers.addAll(Trailer.fromJSONArray(trailersJsonResults));
                    youTubeTrailerKey = mTrailers.get(0).getKey();
// TODO: clean this up
//                    ArrayList<String> youtubeUrls = new ArrayList<String>();
//                    for (Trailer t : mTrailers) {
//                        youtubeUrls.add(YOUTUBE_URL_BASE + t.getKey());
//                        Toast.makeText(MovieDetailActivity.this, YOUTUBE_URL_BASE + t.getKey(), Toast.LENGTH_SHORT).show();
//                    }
                    // Do for loop on mTrailers if > 0, build all video urls, grab only 1st one and make a VideoPlayer object.
                    // Later make a custom adapter and ensure the site  == YouTube. Get API key if necessary.
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Setup YouTube
                youTubePlayerView.initialize(YOUTUBE_API_KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {

                                youTubePlayer.cueVideo(youTubeTrailerKey);
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
