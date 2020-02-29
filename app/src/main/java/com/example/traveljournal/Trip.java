package com.example.traveljournal;

import android.widget.RatingBar;

public class Trip {
    private String name;
    private String id;
    private int rating;
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Trip(String name) {
        this.name = name;
    }

    public Trip() {
    }

    public Trip(String name, String id, int rating, String url) {
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.url = url;
    }
}
