package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Objects;

public class Manga {
    private String title ;
    private String imageUrl;
    private String synopsis;
    private Bitmap image;

    private ArrayList<String> arrayListe;
    public Manga(String title, String imageUrl, String synopsis, ArrayList<String> arrayListe) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.synopsis = synopsis;
        this.arrayListe = arrayListe;
    }

    public Manga() {

    }

    public Manga(String title) {
        this.title = title;
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

    public ArrayList<String> getArrayListe() {
        return arrayListe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manga manga = (Manga) o;
        return Objects.equals(title, manga.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

}

