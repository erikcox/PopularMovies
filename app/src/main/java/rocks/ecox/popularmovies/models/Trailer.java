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
 * Creates a trailer object
 */

public class Trailer implements Parcelable {
    private String mId;
    private String mKey;
    private String mTitle;
    private String mSite;
    private String mType;

    public String getId() { return mId; }
    public String getKey() { return mKey; }
    public String getTitle() { return mTitle; }
    public String getSite() { return mSite; }
    public String getType() { return mType; }

    public Trailer(JSONObject jsonObject) throws JSONException, ParseException{
        // Only add trailers from YouTube
        if (jsonObject.getString("site").equals("YouTube")) {
            this.mId = jsonObject.getString("id");
            this.mKey = jsonObject.getString("key");
            this.mTitle = jsonObject.getString("name");
            this.mSite = jsonObject.getString("site");
            this.mType = jsonObject.getString("type");
        }
    }

    public static ArrayList<Trailer> fromJSONArray(JSONArray array) {
        ArrayList<Trailer> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Trailer(array.getJSONObject(x)));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /** Creates a Trailer object  from a parcel*/
    private Trailer(Parcel parcel){
        mId = parcel.readString();
        mKey = parcel.readString();
        mTitle = parcel.readString();
        mSite = parcel.readString();
        mType = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /** Creates a parcel from a Trailer object */
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mTitle);
        parcel.writeString(mSite);
        parcel.writeString(mType);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

}
