package com.example.myapplication;

import android.graphics.Bitmap;

public class Manga {
    private String title ;
    private String imageUrl;
    private String synopsis;
    private Bitmap image;


    public Manga(String title, String imageUrl, String synopsis) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.synopsis = synopsis;
    }

    public Manga() {

    }
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}

