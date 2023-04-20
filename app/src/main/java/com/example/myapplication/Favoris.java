package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Favoris extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mangasRef;
    private ArrayList<Manga> favMangaList;
    private MangaAdapter adapter;
    public String title;
    public String imageUrl;
    public ListView titleManga;

    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        System.out.println(user.getUid());

        // Récupérer la référence de la base de données
        mangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                .getReference("listFavManga")
                .child(user.getUid());

        // Créer une nouvelle ArrayList pour stocker les mangas favoris
        favMangaList = new ArrayList<>();
        // Créer un nouvel adaptateur pour le ListView
        adapter = new MangaAdapter(this, R.layout.list_item_manga, favMangaList);

        // Récupérer le ListView et le lier à l'adaptateur
        ListView listView = findViewById(R.id.manga_list_view);
        listView.setAdapter(adapter);

        // Ajouter un ChildEventListener à la référence de la base de données pour récupérer les données
        mangasRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                if (snapshot.getValue() instanceof HashMap) {
                    // Récupérer les données de l'enfant sous forme d'objet HashMap
                    HashMap<String, String> mangaData = (HashMap<String, String>) snapshot.getValue();

                    // Créer un nouvel objet Manga à partir des données récupérées
                    String title = mangaData.get("title");
                    String imageUrl = mangaData.get("imageUrl");

                    Manga manga = new Manga(title);
                    manga.setImageUrl(imageUrl); // Assigner l'URL de l'image à l'objet Manga

                    // Ajouter l'objet Manga à la liste
                    favMangaList.add(manga);

                    // Notifier l'adaptateur que les données ont changé
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Favoris", "Les données ne sont pas stockées sous forme de HashMap");
                }
            }









            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Rien à faire ici
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Manga manga = snapshot.getValue(Manga.class);
                if (manga != null) {
                    // Supprimer l'objet Manga de la liste
                    favMangaList.remove(manga);

                    // Notifier l'adaptateur que les données ont changé
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Favoris", "Erreur lors de la récupération des données du manga");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Rien à faire ici
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favoris", "Erreur de lecture de la base de données: " + error.getMessage());
            }
        });
    }

}