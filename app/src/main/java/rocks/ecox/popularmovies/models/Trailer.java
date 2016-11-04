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
import java.util.ArrayList;

/**
 * Creates a Trailer object
 */

@Table(name = "Trailers")
public class Trailer extends Model implements Parcelable {
    @Column(name = "video_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mVideoId;
    @Column(name = "movie_id")
    private String mId;
    @Column(name = "youtube_key")
    private String mKey;
    @Column(name = "trailer_title")
    private String mTitle;
    @Column(name = "trailer_domain")
    private String mSite;
    @Column(name = "trailer_type")
    private String mType;

    public String getmId() { return mId; }
    public void setmId(String id) { this.mId = id; }
    public String getmVideoId() { return mVideoId; }
    public String getKey() { return mKey; }
    public String getTitle() { return mTitle; }
    public String getSite() { return mSite; }
    public String getType() { return mType; }

    public Trailer(){
        super();
    }

    public Trailer(JSONObject jsonObject) throws JSONException, ParseException{
        super();
        /** Only add trailers from YouTube */
        if (jsonObject.getString("site").equals("YouTube")) {
            this.mVideoId = jsonObject.getString("id");
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
        mVideoId = parcel.readString();
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
        parcel.writeString(mVideoId);
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
