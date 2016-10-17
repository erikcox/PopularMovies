/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rocks.ecox.popularmovies.models.Movie;
import rocks.ecox.popularmovies.R;

/**
 * Custom adapter for the movie poster GridView
 */

public class MoviePosterAdapter extends ArrayAdapter<Movie> {
    public MoviePosterAdapter(Activity context, List<Movie> movieObject) {
        super(context, 0, movieObject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /** Set the movie poster in the ImageView */
        Picasso.with(getContext())
                .load(movie.getPoster())
                .error(holder.placeholder)
                .placeholder(holder.placeholder)
                .into(holder.poster);

        return convertView;
    }

    /** Create ViewHolder to speed things up */
    static class ViewHolder {
        @BindView(R.id.ivPoster) ImageView poster;
        @BindDrawable(R.drawable.poster_placeholder)
        Drawable placeholder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}