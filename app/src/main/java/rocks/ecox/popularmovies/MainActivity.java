/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        // Get data from the Movie DB API
        FetchMovieTask mMovieTask = new FetchMovieTask();
        mMovieTask.execute("popular"); // popular or top_rated for now
//        mMovieTask.execute("top_rated"); // top_rated isn't working, test url in browser
        getItems();

        // Initialize recycler view
        RecyclerView rvPosters = (RecyclerView) findViewById(R.id.rvPoster);

        rvPosters.setLayoutManager(new GridLayoutManager(this, 2));

        MoviePosterAdapter mMovieAdapter = new MoviePosterAdapter(MainActivity.this, mMovies);
        rvPosters.setAdapter(mMovieAdapter);
    }

    // Get items from DB
    private void getItems() {
        mMovies = (ArrayList) Movie.getAll();
    }

}
