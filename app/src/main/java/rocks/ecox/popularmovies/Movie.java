/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Creates a movie object and puts it's data into SQLite.
 */

@Table(name = "Movies", id = "id")
public class Movie extends Model{
    @Column(name = "Movie_ID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mId;
    @Column(name = "Title")
    private String mTitle;
    @Column(name = "PosterPath")
    private String mPosterPath;
    @Column(name = "Poster")
    private String mPoster;
    @Column(name = "PosterThumbnail")
    private String mPosterThumbnail;
    @Column(name = "ReleaseDate")
    private String mReleaseDate;
    @Column(name = "Rating")
    private String mUserRating;
    @Column(name = "Synopsis")
    private String mSynopsis;

    public Movie(){
        super();
    }

    Movie(String id, String title, String posterPath, String poster, String thumbnail,
          String releaseDate, String rating, String synopsis) throws ParseException {
        super();
        this.mId = id;
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mPoster = poster;
        this.mPosterThumbnail = thumbnail;
        this.mReleaseDate = formatReleaseDate(releaseDate);
        this.mUserRating = rating;
        this.mSynopsis = synopsis;
    }

    public String getMId() {
        return mId;
    }
    public String getTitle() { return mTitle; }
    public String getPosterPath() {
        return mPosterPath;
    }
    public String getPoster() {
        return mPoster;
    }
    public String getThumbnail() {
        return mPosterThumbnail;
    }
    public String getReleaseDate() { return mReleaseDate; }
    public String getUserRating() {
        return mUserRating;
    }
    public String getSynopsis() {
        return mSynopsis;
    }

    private String formatReleaseDate(String releaseDate) throws ParseException {
        if(releaseDate != null && !releaseDate.equals("")) {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date d = inFormat.parse(releaseDate);
            return outFormat.format(d);
        } else {
            return "Unknown";
        }
    }

    // Get all movies from DB
    public static List<Movie> getAll() {
        return new Select()
                .from(Movie.class)
                .execute();
    }

}
