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
import java.util.ArrayList;

/**
 * Creates a review object
 */

public class Review implements Parcelable {
    private String mId;
    private String mAuthor;
    private String mContent;
//    private String mUrl; // Not using url, unless WebView implemented. Just know it's there.

    public String getId() { return mId; }
    public String getAuthor() { return mAuthor; }
    public String getContent() { return mContent; }

    public Review(JSONObject jsonObject) throws JSONException, ParseException{
        this.mId = jsonObject.getString("id");
        this.mAuthor = jsonObject.getString("author");
        this.mContent = jsonObject.getString("content");
    }

    public static ArrayList<Review> fromJSONArray(JSONArray array) {
        ArrayList<Review> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Review(array.getJSONObject(x)));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /** Creates a Trailer object  from a parcel*/
    private Review(Parcel parcel){
        mId = parcel.readString();
        mAuthor = parcel.readString();
        mContent = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /** Creates a parcel from a Trailer object */
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>(){
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

}
