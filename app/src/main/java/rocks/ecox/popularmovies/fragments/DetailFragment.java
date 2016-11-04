/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Activity to display movie details once clicked on in MovieFragment
 */

public class DetailFragment extends Fragment {
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
    @BindBool(R.bool.favoriteStatus) boolean favStatus;
    TrailerAdapter tAdapter;
    ReviewAdapter rAdapter;
    RecyclerView rvTrailer;
    RecyclerView rvReview;
    AsyncHttpClient client = new AsyncHttpClient();
    public ArrayList<String> youTubeTrailerKeys = new ArrayList<>();
    public Movie movie;
    public  ArrayList<Trailer> mTrailers = new ArrayList<>();
    public  ArrayList<Review> mReviews= new ArrayList<>();

    /** Creates a new instance of this fragment with a movie object */
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragmentDetail = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        fragmentDetail.setArguments(args);
        return fragmentDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);

        ButterKnife.bind(this, view);

        /** Populate the ImageView and TextViews from the bundle */
        Bundle bundle = getArguments();
        if (bundle != null) {
            movie = getArguments().getParcelable("movie");
            movie.getTitle();
            movie.save();
        }

        try {
            fetchMoviesAsync(movie.getmId(), getActivity(), view);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /** Place a movie backdrop image in the ImageView */
        Picasso.with(getActivity())
                .load(movie.getBackdrop())
                .error(placeholder)
                .placeholder(placeholder)
                .into(poster);

        /** Populate text in the detail fields */
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        rating.setText(movie.getUserRating().toString());
        synopsis.setText(movie.getSynopsis());

        /** Set listeners for movies that are favorited / unfavorited */
        fav.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                onToggleStar(v);
            }
        });

        unfav.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                onToggleStar(v);
            }
        });

        /** Determine the visibility  of the yellow star and blank star images */
        if (movie.getFavorite() != null) {
            favStatus = true;
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void fetchMoviesAsync(final String movieId, final Activity activity, final View view) throws MalformedURLException {
        String url = String.format(TRAILER_BASE_URL, movieId) + APPEND_API_KEY;
        String urlReview = String.format(REVIEW_BASE_URL, movieId) + APPEND_API_KEY;

        client.get(url, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                        tAdapter = new TrailerAdapter(getActivity(), mTrailers);
                        rvTrailer = (RecyclerView) view.findViewById(R.id.rvTrailer);
                        rvTrailer.setAdapter(tAdapter);
                        rvTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                        rAdapter = new ReviewAdapter(getActivity(), mReviews);
                        rvReview= (RecyclerView) view.findViewById(R.id.rvReview);
                        rvReview.setAdapter(rAdapter);
                        rvReview.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    /** Toggle the star image visibility, set the value in the DB, and Toast to notify the user */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onToggleStar(View view) {
        if(favStatus) {
            unfav.setVisibility(View.VISIBLE);
            fav.setVisibility(View.INVISIBLE);
            favStatus=false;
            movie.setFavorite(null); // Set this to "N" or null?
            movie.save();
            Toast.makeText(getActivity(), getString(R.string.unfavorite) + movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        else {
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.INVISIBLE);
            favStatus=true;
            movie.setFavorite("Y");
            movie.save();
            Toast.makeText(getActivity(), getString(R.string.favorite) + movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
