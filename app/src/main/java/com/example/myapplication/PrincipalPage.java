package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ClipData;
import android.content.ClipDescription;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import com.bumptech.glide.Glide;

public class PrincipalPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mangasRef;
    private ArrayList<Manga> favMangaList;

    private MangaAdapter adapter;

    public String title;

    public String imageUrl;

    public ArrayList<String> titleManga;

    public ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);




        // Récupérer la référence de la base de données
        mangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/").getReference("listFavManga");

        // Créer une nouvelle ArrayList pour stocker les mangas favoris
        favMangaList = new ArrayList<>();

        titleManga = new ArrayList<>();


        // Récupérer le ListView et le lier à l'adaptateur

ImageView imageView = findViewById(R.id.image_view);
        LinearLayout col1Container = findViewById(R.id.col1_container);
        // Définir les listeners de drag and drop pour chaque conteneur de colonne
        LinearLayout col1 = findViewById(R.id.col1);
        //col1.setOnDragListener(new ColumnDragListener(col1Container));

        LinearLayout col2Container = findViewById(R.id.col2);
        //col2Container.setOnDragListener(new ColumnDragListener(col2Container));

        LinearLayout col3Container = findViewById(R.id.col3);
        //col3Container.setOnDragListener(new ColumnDragListener(col3Container));

        LinearLayout col4Container = findViewById(R.id.col4);
        //col4Container.setOnDragListener(new ColumnDragListener(col4Container));

        mangasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                if (snapshot.getValue() instanceof HashMap) {


                    // Récupérer les données de l'enfant sous forme d'objet HashMap

// Récupérer les données de l'enfant sous forme d'objet HashMap
                    HashMap<String, Object> mangaData = (HashMap<String, Object>) snapshot.getValue();
                    // Créer un nouvel objet Manga à partir des données récupérées
                    int startIndex = mangaData.get("arrayListe").toString().indexOf("title="); // Trouver l'indice de début de la sous-chaîne
                    int endIndex = mangaData.get("arrayListe").toString().indexOf("}", startIndex); // Trouver l'indice de fin de la sous-chaîne
                    title = mangaData.get("arrayListe").toString().substring(startIndex + 6, endIndex);
                    // Extraire le titre du manga de la chaîne de caractères stockée dans la base de données

                    String target = "imageUrl";
                    int startIndex2 = mangaData.get("arrayListe").toString().indexOf(target) + target.length();
                    int endIndex2 = mangaData.get("arrayListe").toString().indexOf(",", startIndex2);
                    imageUrl = mangaData.get("arrayListe").toString().substring(startIndex2 + 1, endIndex2);


                    // Créer un nouvel objet Manga à partir des données récupérées
                    Manga manga = new Manga(title);

                    manga.setImageUrl(imageUrl); // Assigner l'URL de l'image à l'objet Manga


                    // Ajouter l'objet Manga à la liste
                    favMangaList.add(manga);

                    // Ajouter le titre du manga
                    titleManga.add(title);
                    System.out.println(String.valueOf(titleManga.size()));
                    Glide.with(PrincipalPage.this)
                            .load(imageUrl)
                            .into(imageView);
                   // col1Container.addView(createTextView(title));

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
                if (snapshot.getValue() instanceof HashMap) {
                    HashMap<String, Object> mangaData = (HashMap<String, Object>) snapshot.getValue();
                    // Extraire le titre du manga de la chaîne de caractères stockée dans la base de données


                    // Créer un nouvel objet Manga à partir des données récupérées
                    Manga mangaToRemove = new Manga(title);

                    // Supprimer l'objet Manga de la liste
                    favMangaList.remove(mangaToRemove);

                    // Notifier l'adaptateur que les données ont changé
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Favoris", "Les données ne sont pas stockées sous forme de HashMap");
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
