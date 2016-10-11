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

public class MainActivityFragment extends Fragment implements FetchMovieTask.AsyncResponse {
    static List<Movie> movieList = new ArrayList<Movie>();
    static MoviePosterAdapter movieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            if(Utility.isOnline(getActivity())) {
                fetchMovies(Utility.getSortKey(getActivity()), "1");
            } else {
                Toast.makeText(getActivity(), "Network connection unavailable.", Toast.LENGTH_SHORT).show();
            }
        } else {
            movieAdapter.clear();
            ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList("movies");
            movieAdapter.addAll(savedMovies);
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<Movie> arrayList = new ArrayList<>();
        for(int i=0;i < movieAdapter.getCount(); i++){
            arrayList.add(movieAdapter.getItem(i));
        }

        outState.putParcelableArrayList("movies", arrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MoviePosterAdapter(getActivity(), movieList);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = movieAdapter.getItem(position);

                String[] movieDetails = {movie.getTitle(), movie.getThumbnail(), movie.getReleaseDate(), movie.getUserRating(), movie.getSynopsis()};
                ArrayList<String> movieDetailsLst = new ArrayList<String>(Arrays.asList(movieDetails));
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putStringArrayListExtra(Intent.EXTRA_TEXT, movieDetailsLst);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void processFinish(ArrayList<Movie> output) {
        movieList = output;
        movieAdapter.clear();
        movieAdapter.addAll(movieList);
        movieAdapter.notifyDataSetChanged();
    }

    public void fetchMovies(String sortBy, String page) {
        new FetchMovieTask((FetchMovieTask.AsyncResponse) this).execute(sortBy, page);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(Utility.isOnline(getActivity())) {
            fetchMovies(Utility.getSortKey(getActivity()), "1");
        } else {
            Toast.makeText(getActivity(), "Network connection unavailable.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}