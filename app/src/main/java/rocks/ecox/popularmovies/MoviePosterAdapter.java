/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom adapter for the movie poster GridView
 */

public class MoviePosterAdapter extends ArrayAdapter<Movie> {
    public MoviePosterAdapter(Activity context, List<Movie> movieObject) {
        super(context, 0, movieObject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
        }

        /** Set the movie poster in the ImageView */
        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
        ImageView poster = viewHolder.poster;
        Picasso.with(getContext())
                .load(movie.getPoster())
                .error(R.drawable.poster_placeholder)
                .placeholder(R.drawable.poster_placeholder)
                .into(poster);

        return convertView;
    }

    private static class ViewHolder {
        final ImageView poster;

        ViewHolder(View view) {
            poster = (ImageView) view.findViewById(R.id.ivPoster);
        }
    }
}