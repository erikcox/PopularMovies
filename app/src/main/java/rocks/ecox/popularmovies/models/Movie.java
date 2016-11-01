/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Creates a movie object
 */

@Table(name = "Movies")
public class Movie extends Model implements Parcelable {
    @Column(name = "movie_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mId;
    @Column(name = "title")
    private String mTitle;
    @Column(name = "poster_path")
    private String mPosterPath;
    @Column(name = "backdrop_path")
    private String mBackdropPath;
    @Column(name = "release_date")
    private String mReleaseDate;
    @Column(name = "rating")
    private Double mUserRating;
    @Column(name = "synopsis")
    private String mSynopsis;

    public String getmId() { return mId; }
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

    // Make sure to have a default constructor for every ActiveAndroid model
    public Movie(){
        super();
    }

    public Movie(JSONObject jsonObject) throws JSONException, ParseException{
        super();
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
