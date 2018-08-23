package com.flickerapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anup Lal on 21-08-2018.
 */

public class Photo implements Parcelable {
    private String id;
    private String secret;
    private String server;
    private int farm;
    private String path;


    public Photo(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.secret = jsonObject.getString("secret");
            this.server = jsonObject.getString("server");
            this.farm = jsonObject.getInt("farm");
            this.path = getAbsolutePath();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Photo(String path) {
        this.path = path;
    }

    public String getImagePath() {
        return this.path;
    }

    protected Photo(Parcel in) {
        id = in.readString();
        secret = in.readString();
        server = in.readString();
        farm = in.readInt();
        path = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(secret);
        dest.writeString(server);
        dest.writeInt(farm);
        dest.writeString(path);
    }

     String getAbsolutePath() {
        return getId() + "_" + getSecret() + "_" + getFarm() + ".png";
    }
}
