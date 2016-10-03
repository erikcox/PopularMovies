/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Creates a movie object and puts it's data into SQLite.
 */

@Table(name = "Movies", id = "id")
public class Movie extends Model{
    @Column(name = "Movie_ID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long mId;
    @Column(name = "Title")
    private String mTitle;
    @Column(name = "PosterPath")
    private String mPosterPath;
    @Column(name = "Poster")
    private String mPoster;
    @Column(name = "PosterThumbnail")
    private String mPosterThumbnail;
    @Column(name = "ReleaseDate")
    private Date mReleaseDate;
    @Column(name = "Rating")
    private String mUserRating;
    @Column(name = "Overview")
    private String mOverview;

    public Movie(){
        super();
    }

    public Movie(long id, String title, String posterPath, String poster, String thumbnail,
                 Date releaseDate, String rating, String overview) {
        super();
        this.mId = id;
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mPoster = poster;
        this.mPosterThumbnail = thumbnail;
        this.mReleaseDate = releaseDate;
        this.mUserRating = rating;
        this.mOverview = overview;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getPosterPath() {
        return mPosterPath;
    }
    public String getPoster() {
        return mPoster;
    }
    public String getThumbnail() {
        return mPosterThumbnail;
    }
    public Date getReleaseDate() {
        return mReleaseDate;
    }
    public String getUserRating() {
        return mUserRating;
    }
    public String getOverview() {
        return mOverview;
    }

    // Get all movies from DB
    public static List<Movie> getAll() {
        return new Select()
                .from(Movie.class)
                .execute();
    }

}
