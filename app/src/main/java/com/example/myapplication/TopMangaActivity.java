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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        top1ImageView = findViewById(R.id.top1_image_view);
        top2ImageView = findViewById(R.id.top2_image_view);
        top3ImageView = findViewById(R.id.top3_image_view);

        top1TextView = findViewById(R.id.top1_text_view);
        top2TextView = findViewById(R.id.top2_text_view);
        top3TextView = findViewById(R.id.top3_text_view);

        listFavMangaRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                .getReference("listFavManga")
                .child(user.getUid());

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
                            Manga manga = snapshot.getValue(Manga.class);
                            if (manga != null) {
                                manga.setMangaKey(snapshot.getKey());
                                topMangas.add(manga);
                            }
                        }

                        Collections.reverse(topMangas);

                        if (topMangas.size() > 0) {
                            top1TextView.setText(topMangas.get(0).getTitle());
                            Picasso.get().load(topMangas.get(0).getImageUrl()).into(top1ImageView);
                        }

                        if (topMangas.size() > 1) {
                            top2TextView.setText(topMangas.get(1).getTitle());
                            Picasso.get().load(topMangas.get(1).getImageUrl()).into(top2ImageView);
                        }

                        if (topMangas.size() > 2) {
                            top3TextView.setText(topMangas.get(2).getTitle());
                            Picasso.get().load(topMangas.get(2).getImageUrl()).into(top3ImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });
    }
}
