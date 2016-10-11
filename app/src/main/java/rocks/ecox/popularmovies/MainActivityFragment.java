/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment in MainActivity that contains the movie posters
 */
public class MainActivityFragment extends Fragment implements FetchMovieTask.AsyncResponse {
    static List<Movie> mMovieList = new ArrayList<Movie>();
    static MoviePosterAdapter mMovieAdapter;

    /** Check to see if we have saved data */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            /** See if we have a network connection */
            if(Utility.isOnline(getActivity())) {
                fetchMovies(Utility.getSortKey(getActivity()), "1");
            } else {
                Toast.makeText(getActivity(), "Network connection unavailable.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mMovieAdapter.clear();
            ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter.addAll(savedMovies);
        }
    }

    public MainActivityFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MoviePosterAdapter(getActivity(), mMovieList);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mMovieAdapter);

        /** Sets listener so that we can call the DetailActivity */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMovieAdapter.getItem(position);

                /** Create an array of movie details to pass to the DetailActivity */
                String[] movieDetails = {movie.getTitle(), movie.getThumbnail(), movie.getReleaseDate(), movie.getUserRating(), movie.getSynopsis()};
                ArrayList<String> movieDetailsLst = new ArrayList<String>(Arrays.asList(movieDetails));
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putStringArrayListExtra(Intent.EXTRA_TEXT, movieDetailsLst);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /** Update the adapter when the AsyncTask is complete */
    @Override
    public void processFinish(ArrayList<Movie> output) {
        mMovieList = output;
        mMovieAdapter.clear();
        mMovieAdapter.addAll(mMovieList);
        mMovieAdapter.notifyDataSetChanged();
    }

    /** Create a new AsyncTask to get Movie objects from TMDB api */
    public void fetchMovies(String sortBy, String page) {
        new FetchMovieTask((FetchMovieTask.AsyncResponse) this).execute(sortBy, page);
    }

    /** Update adapter with new Movie objects when new sort order is chosen in the menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Utility.isOnline(getActivity())) {
            fetchMovies(Utility.getSortKey(getActivity()), "1");
        } else {
            Toast.makeText(getActivity(), "Network connection unavailable.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}