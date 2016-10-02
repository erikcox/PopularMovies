/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterHolder> {
    private List<Movie> mMovies;
    private Context mContext;

    public MoviePosterAdapter(Context context, List<Movie> movies) {
        this.mMovies = movies;
        this.mContext = context;
    }

    @Override
    public MoviePosterHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_poster, null);
        MoviePosterHolder mh = new MoviePosterHolder(view);

        return mh;
    }

    // Set the view attributes based on the data
    @Override
    public void onBindViewHolder(MoviePosterHolder moviePosterHolder, int position) {
        // Get the data model based on position
        Movie movie = mMovies.get(position);
        Picasso.with(mContext)
                .load(movie.getPoster())
                .error(R.drawable.poster_placeholder)
                .placeholder(R.drawable.poster_placeholder)
                .into(moviePosterHolder.ivPoster);
    }

    // Determine the number of items
    @Override
    public int getItemCount() {
        return (null != mMovies ? mMovies.size() : 0);
    }

}
