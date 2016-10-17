/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import rocks.ecox.popularmovies.adapters.MoviePosterAdapter;
import rocks.ecox.popularmovies.models.Movie;

import static rocks.ecox.popularmovies.BuildConfig.TMDB_API_KEY;

/**
 * Fragment in MainActivity that contains the movie posters
 */
public class MainActivityFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Movie> mMovieList;
    MoviePosterAdapter mMovieAdapter;
    GridView gridView;

    AsyncHttpClient client = new AsyncHttpClient();
    final String api_key = TMDB_API_KEY;
    final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + api_key;
    final String PAGE = "1";
    final String NETWORK_FAIL = "Network connection unavailable.";

    /**
     * Check to see if we have saved data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mMovieList = new ArrayList<>();
        mMovieAdapter = new MoviePosterAdapter(getActivity(), mMovieList);
        gridView.setAdapter(mMovieAdapter);
        fetchMoviesAsync(0);

        /** Sets listener so that we can call the DetailActivity */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMovieAdapter.getItem(position);

                /** Create Parcelable of movie object and pass with Intent to the DetailActivity */
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void fetchMoviesAsync(int page) {
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray moviesJsonResults = null;

                try {
                    moviesJsonResults = response.getJSONArray("results");
                    mMovieAdapter.clear();
                    mMovieAdapter.addAll(Movie.fromJSONArray(moviesJsonResults));
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