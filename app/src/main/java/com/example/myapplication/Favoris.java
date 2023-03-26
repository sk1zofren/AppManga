package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Favoris extends AppCompatActivity {

    MangaList mangaList = new MangaList();
    ArrayList<Manga> favMangaList = mangaList.favMangaList;
    DatabaseReference UserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
//TODO pour afficher les mangas, le soucis viens du fait que quand je mets un mangas en favoris, et
//TODO que je retourne en arriere, l'arraylist de fav n'enregistre pas les mangas, donc je dois faire une
// base de donne avec les mangas favoris
        ListView listView = findViewById(R.id.manga_list_view);
        ArrayAdapter<Manga> adapter = new ArrayAdapter<Manga>(this, R.layout.list_item_manga, new ArrayList<Manga>());
        listView.setAdapter(adapter);

    }

    public void Go_display(View view){
//System.out.println(favMangaList.size());
/*
        User user1 = new User();
        user1.addArray(favMangaList);
        UserRef = FirebaseDatabase.getInstance("https://mangas-a1043-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("UsersFavManga");
        UserRef.push().setValue(user1);

 */
    }
}
