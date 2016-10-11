/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates a movie object
 */

public class Movie {
    private String mId;
    private String mTitle;
    private String mPosterPath;
    private String mPoster;
    private String mPosterThumbnail;
    private String mReleaseDate;
    private String mUserRating;
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
        } else {i
            return "Unknown";
        }
    }

}
