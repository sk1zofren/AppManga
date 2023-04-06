package com.example.myapplication;
import java.util.ArrayList;
public class User  {


    String Name;
    String username;
    ArrayList<Manga> arrayList;

    public User(){

        arrayList = new ArrayList<Manga>();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setArrayList(ArrayList<Manga> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Manga> getArrayListe() {
        return arrayList;
    }



}
