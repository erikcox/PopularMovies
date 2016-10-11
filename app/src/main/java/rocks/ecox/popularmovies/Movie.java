/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates a movie object
 */

public class Movie implements Parcelable {
    private String mId;
    private String mTitle;
    private String mPoster;
    private String mPosterThumbnail;
    private String mReleaseDate;
    private String mUserRating;
    private String mSynopsis;

    public Movie(){
        super();
    }

    Movie(String id, String title, String poster, String thumbnail,
          String releaseDate, String rating, String synopsis) throws ParseException {
        super();
        this.mId = id;
        this.mTitle = title;
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

    private Movie(Parcel parcel){
        mId = parcel.readString();
        mTitle = parcel.readString();
        mPoster = parcel.readString();
        mPosterThumbnail = parcel.readString();
        mReleaseDate = parcel.readString();
        mUserRating = parcel.readString();
        mSynopsis = parcel.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mPosterThumbnail);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mUserRating);
        parcel.writeString(mSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
