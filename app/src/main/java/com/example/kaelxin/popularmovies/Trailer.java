package com.example.kaelxin.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String id;
    private String key;
    private String name;
    private String youtubeLink;

    public Trailer(String id, String key, String name,String youtubeLink ) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.youtubeLink = youtubeLink;
    }


    protected Trailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        youtubeLink = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(youtubeLink);
    }


}
