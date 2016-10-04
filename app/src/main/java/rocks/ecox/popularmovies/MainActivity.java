/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static List<Movie> mMovies;
    private static MoviePosterAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        // Get data from the Movie DB API
        // sortBy: popularity, vote_average, pageNum: 1, 2, 3...
        getMoviesFromTMDB("vote_average", 1);
        populateFromDb();

        // Initialize recycler view
        final RecyclerView rvPosters = (RecyclerView) findViewById(R.id.rvPoster);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvPosters.setLayoutManager(mLayoutManager);

        mMovieAdapter = new MoviePosterAdapter(MainActivity.this, mMovies);
        rvPosters.setAdapter(mMovieAdapter);
//        mMovieAdapter.notifyDataSetChanged();

        // Add endless scroll listener
        rvPosters.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("RECYCLERVIEW", "onLoadMore: " + page);
                getMoviesFromTMDB("vote_average", page);
            }
        });

    }

    // Get items from DB
    private void populateFromDb() {
        mMovies = (ArrayList<Movie>) Movie.getAll();
    }

    private void getMoviesFromTMDB(String sortedBy, int pageNum) {
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute(sortedBy, String.valueOf(pageNum));
        mMovies = fetchMovieTask.getFinalResult();
        if(mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
    }

}
