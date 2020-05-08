package com.example.traveljournal;


import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String name;
    private String id;
    private String url;
    private String downloadUrl;
    private ArrayList<String> photoUrls = new ArrayList<>();

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(ArrayList<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        this.url = url;
    }
}
