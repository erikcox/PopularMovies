/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class MoviePosterHolder extends RecyclerView.ViewHolder{
    protected ImageView ivPoster;

    public MoviePosterHolder(View view) {
        super(view);
        this.ivPoster = (ImageView) view.findViewById(R.id.ivPoster);
    }
}
