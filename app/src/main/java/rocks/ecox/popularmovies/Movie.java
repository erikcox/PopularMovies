package rocks.ecox.popularmovies;

import java.util.ArrayList;
import java.util.Date;

public class Movie {
    private String mTitle;
    private String mPoster;
    private String mPosterThumbnail;
    private Date mReleaseDate;
    private String mUserRating;
    private String mSynopsis;

    public Movie(String title, String poster) {
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

    public void setPoster(String poster) {
        this.mPoster = poster;
    }

    public String getThumbnail() {
        return mPosterThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mPosterThumbnail = thumbnail;
    }

}
