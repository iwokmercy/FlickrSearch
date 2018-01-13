package com.example.mjexco.flickrsearch.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FlickrSearchResponse implements Serializable
{

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 5450279298772338226L;

    /**
     * No args constructor for use in serialization
     *
     */
    public FlickrSearchResponse() {
    }

    /**
     *
     * @param photos
     * @param stat
     */
    public FlickrSearchResponse(Photos photos, String stat, int code, String message) {
        super();
        this.photos = photos;
        this.stat = stat;
        this.code = code;
        this.message = message;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("photos", photos).append("stat", stat).
                append("code", code).append("message", message).toString();
    }

}
