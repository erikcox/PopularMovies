/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.activities.DetailActivity;
import rocks.ecox.popularmovies.adapters.MoviePosterAdapter;
import rocks.ecox.popularmovies.models.Movie;
import rocks.ecox.popularmovies.utilities.Constants;
import rocks.ecox.popularmovies.utilities.Utility;

/**
 * Fragment in MovieActivity that contains the movie posters
 */
public class MovieFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Movie> mMovieList;
    MoviePosterAdapter mMovieAdapter;
    GridView gridView;
    AsyncHttpClient client = new AsyncHttpClient();
    final int PAGE = 0;

    /**
     * Check to see if we have saved data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MovieFragment() {
    }

    /** Save the movies in the view on exit or changing views */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        /** Make an ArrayList of the Movie objects in the mMovieAdapter */
        ArrayList<Movie> arrayList = new ArrayList<>();
        for(int i = 0; i < mMovieAdapter.getCount(); i++){
            arrayList.add(mMovieAdapter.getItem(i));
        }

        outState.putParcelableArrayList("movies", arrayList);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        if (!Utility.getSortKey(getActivity()).equals("favorite")) {
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        fetchMoviesAsync(PAGE, Utility.getSortKey(getActivity()));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /** Configure the refreshing colors */
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mMovieList = new ArrayList<>();
        mMovieAdapter = new MoviePosterAdapter(getActivity(), mMovieList);
        gridView.setAdapter(mMovieAdapter);

        // Check if there's a saved instance, network connection, and sortBy isn't "favorite"
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")
                && !Utility.getSortKey(getActivity()).equals("favorite")) {
            /** See if we have a network connection */
            if (Utility.isOnline(getActivity())) {
                try {
                    fetchMoviesAsync(PAGE, Utility.getSortKey(getActivity()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), Constants.NETWORK_FAIL, Toast.LENGTH_SHORT).show();
            }
        }
        // TODO: test this on app load (check SavedInstanceState when fav saved - Load movies from DB if sortBy = "favorite"
        else if (Utility.getSortKey(getActivity()).equals("favorite")) {
            mMovieAdapter.clear();
            // Get all movies from DB marked as favorite
            List<Movie> favoriteMovies = Movie.getFavs();
            mMovieAdapter.addAll(favoriteMovies);
        } else {
            mMovieAdapter.clear();
            ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter.addAll(savedMovies);
        }

        try {
            fetchMoviesAsync(PAGE, Utility.getSortKey(getActivity()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /** Sets listener so that we can call the DetailActivity */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMovieAdapter.getItem(position);

                /** Create Parcelable of movie object and pass with Intent to the DetailActivity */
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(Utility.isOnline(getActivity()) && !Utility.getSortKey(getActivity()).equals("favorite")) {
            try {
                fetchMoviesAsync(PAGE, Utility.getSortKey(getActivity()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (Utility.getSortKey(getActivity()).equals("favorite")) {
            mMovieAdapter.clear();
            // Get all movies from DB marked as favorite
            List<Movie> favoriteMovies = Movie.getFavs();
            mMovieAdapter.addAll(favoriteMovies);
        }else{
            Toast.makeText(getActivity(), Constants.NETWORK_FAIL, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void fetchMoviesAsync(int page, String sortBy) throws MalformedURLException {
        String baseUrl = "https://api.themoviedb.org/3/movie/";
        Uri.Builder uriBuilder = Uri.parse(baseUrl + sortBy).buildUpon()
                .appendQueryParameter("api_key", Constants.API_KEY);

        Uri completeUri = uriBuilder.build();
        String url = completeUri.toString();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray moviesJsonResults = null;

                try {
                    moviesJsonResults = response.getJSONArray("results");
                    mMovieAdapter.clear();
                    mMovieAdapter.addAll(Movie.fromJSONArray(moviesJsonResults));
                    swipeContainer.setRefreshing(false);
                    mMovieAdapter.notifyDataSetChanged();
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
