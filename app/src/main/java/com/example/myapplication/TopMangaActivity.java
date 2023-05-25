package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopMangaActivity extends AppCompatActivity {

    private ImageView top1ImageView, top2ImageView, top3ImageView;
    private TextView top1TextView, top2TextView, top3TextView;
    private DatabaseReference listFavMangaRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_manga);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Initialize views
        top1ImageView = findViewById(R.id.top1_image_view);
        top2ImageView = findViewById(R.id.top2_image_view);
        top3ImageView = findViewById(R.id.top3_image_view);
        top1TextView = findViewById(R.id.top1_text_view);
        top2TextView = findViewById(R.id.top2_text_view);
        top3TextView = findViewById(R.id.top3_text_view);

        // Get the database reference for favorite mangas of the current user
        listFavMangaRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                .getReference("listFavManga")
                .child(user.getUid());

        // Load and display the top mangas
        loadTopMangas();
    }

    private void loadTopMangas() {
        listFavMangaRef.orderByChild("fav")
                .limitToLast(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Manga> topMangas = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Retrieve the Manga object from the dataSnapshot
                            Manga manga = snapshot.getValue(Manga.class);
                            if (manga != null) {
                                manga.setMangaKey(snapshot.getKey());
                                topMangas.add(manga);
                            }
                        }

                        // Reverse the list to display the top mangas in descending order
                        Collections.reverse(topMangas);

                        // Display the top mangas in the ImageView and TextView
                        if (topMangas.size() > 0) {
                            Picasso.get().load(topMangas.get(1).getImageUrl()).into(top1ImageView);
                            top1TextView.setText(String.valueOf(topMangas.get(1).getFav()));
                        }

                        if (topMangas.size() > 1) {
                            Picasso.get().load(topMangas.get(0).getImageUrl()).into(top2ImageView);
                            top2TextView.setText(String.valueOf(topMangas.get(0).getFav()));
                        }

                        if (topMangas.size() > 2) {
                            Picasso.get().load(topMangas.get(2).getImageUrl()).into(top3ImageView);
                            top3TextView.setText(String.valueOf(topMangas.get(2).getFav()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });
    }

}
