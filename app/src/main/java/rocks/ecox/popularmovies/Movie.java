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

@Table(name = "Movies")
public class Movie extends Model{
    @Column(name = "Title", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mTitle;
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

    public Movie(String title, String poster) {
        super();
        mTitle = title;
        mPoster = poster;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) { this.mPoster = poster; }

    public String getThumbnail() {
        return mPosterThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mPosterThumbnail = thumbnail;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) { this.mReleaseDate = releaseDate; }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String userRating) {
        this.mUserRating = userRating;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    // Get all items
    public static List<Movie> getAll() {
        return new Select()
                .from(Movie.class)
                .execute();
    }

}
