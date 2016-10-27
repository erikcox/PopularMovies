/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Creates a movie object
 */

public class Movie implements Parcelable {
    private String mId;
    private String mTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private String mReleaseDate;
    private Double mUserRating;
    private String mSynopsis;
    private List<String> mTrailerKeys;

    public String getId() { return mId; }
    public String getTitle() { return mTitle; }
    public String getPoster() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", mPosterPath);
    }
    public String getBackdrop() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", mBackdropPath);
    }
    public String getThumbnail() {
        return String.format("https://image.tmdb.org/t/p/w92/%s", mPosterPath);
    }
    public String getReleaseDate() { return mReleaseDate; }
    public String getSynopsis() {
        return mSynopsis;
    }
    public Double getUserRating() {
        if (this.mUserRating > 0) {
            return mUserRating;
        } else {
            return 0.0;
        }
    }
    public void setTrailerKeys(List<String> trailerKeys) { this.mTrailerKeys = trailerKeys;}
    public List<String> getTrailerKeys() { return mTrailerKeys;}

    /** Reformats TMDB release date from yyyy-MM-dd to MM-dd-yyyy */
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

    public Movie(JSONObject jsonObject) throws JSONException, ParseException{
        this.mId = jsonObject.getString("id");
        this.mTitle = jsonObject.getString("original_title");
        this.mPosterPath = jsonObject.getString("poster_path");
        this.mBackdropPath = jsonObject.getString("backdrop_path");
        this.mReleaseDate = formatReleaseDate(jsonObject.getString("release_date"));
        this.mUserRating = jsonObject.getDouble("vote_average");
        this.mSynopsis = jsonObject.getString("overview");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /** Creates a Movie object  from a parcel*/
    private Movie(Parcel parcel){
        mId = parcel.readString();
        mTitle = parcel.readString();
        mPosterPath = parcel.readString();
        mBackdropPath = parcel.readString();
        mReleaseDate = parcel.readString();
        mUserRating = parcel.readDouble();
        mSynopsis = parcel.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    /** Creates a parcel from a Movie object */
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mBackdropPath);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mUserRating);
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