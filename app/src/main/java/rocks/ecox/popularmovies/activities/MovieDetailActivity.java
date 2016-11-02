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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.adapters.ReviewAdapter;
import rocks.ecox.popularmovies.adapters.TrailerAdapter;
import rocks.ecox.popularmovies.models.Movie;
import rocks.ecox.popularmovies.models.Review;
import rocks.ecox.popularmovies.models.Trailer;

import static rocks.ecox.popularmovies.utilities.Constants.APPEND_API_KEY;
import static rocks.ecox.popularmovies.utilities.Constants.REVIEW_BASE_URL;
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
    @BindView(R.id.tvTrailerHeader) TextView trailerHeader;
    @BindView(R.id.tvReviewHeader) TextView reviewHeader;
    @BindView(R.id.favorited) ImageButton fav;
    @BindView(R.id.unfavorited) ImageButton unfav;
    @BindBool(R.bool.favoriteStatus) Boolean favStatus;
    TrailerAdapter tAdapter;
    ReviewAdapter rAdapter;
    RecyclerView rvTrailer;
    RecyclerView rvReview;
    AsyncHttpClient client = new AsyncHttpClient();
    public ArrayList<String> youTubeTrailerKeys = new ArrayList<>();
    public Movie movie;
    public  ArrayList<Trailer> mTrailers = new ArrayList<>();
    public  ArrayList<Review> mReviews= new ArrayList<>();

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
            movie.save();

            try {
                fetchMoviesAsync(movie.getmId(), this);
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
        if (movie.getFavorite() != null) {
            favStatus = true;
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.INVISIBLE);
        }

    }

    public void fetchMoviesAsync(final String movieId, final Activity activity) throws MalformedURLException {
        String url = String.format(TRAILER_BASE_URL, movieId) + APPEND_API_KEY;
        String urlReview = String.format(REVIEW_BASE_URL, movieId) + APPEND_API_KEY;

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
                        t.setmId(movieId);
                        t.save();
                    }

                    // Set up trailers in RecyclerView
                    if (mTrailers.size() > 0) {
                        Log.d("DEBUG", "Setup Trailers. Size: " + mTrailers.size());
                        tAdapter = new TrailerAdapter(MovieDetailActivity.this, mTrailers);
                        rvTrailer = (RecyclerView) findViewById(R.id.rvTrailer);
                        rvTrailer.setAdapter(tAdapter);
                        rvTrailer.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                    } else {
                        // Hide Trailer header if there are no trailers
                        trailerHeader.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        client.get(urlReview, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray reviewsJsonResults = null;
                Log.d("DEBUG", "GETTING REVIEWS!");
                try {
                    reviewsJsonResults = response.getJSONArray("results");
                    mReviews.addAll(Review.fromJSONArray(reviewsJsonResults));

                    for (Review r : mReviews) {
                        r.setmId(movieId);
                        r.save();
                    }

                    // Set up reviews in RecyclerView
                    if (mReviews.size() > 0) {
                        rAdapter = new ReviewAdapter(MovieDetailActivity.this, mReviews);
                        rvReview= (RecyclerView) findViewById(R.id.rvReview);
                        rvReview.setAdapter(rAdapter);
                        rvReview.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                    } else {
                        // Hide Review header if there are no reviews
                        reviewHeader.setVisibility(View.GONE);
                    }

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

    public void onToggleStar(View view) {
        if(favStatus) {
            unfav.setVisibility(View.VISIBLE);
            fav.setVisibility(View.INVISIBLE);
            favStatus=false;
            movie.setFavorite(null); // Set this to "N" or null?
            movie.save();
            Toast.makeText(this, "Unfavorited " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        else {
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.INVISIBLE);
            favStatus=true;
            movie.setFavorite("Y");
            movie.save();
            Toast.makeText(this, "Favorited " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
