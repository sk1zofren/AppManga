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
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        System.out.println(user.getUid());

        // Get the reference to the database
        mangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                .getReference("listFavManga")
                .child(user.getUid());

        // Create a new ArrayList to store favorite mangas
        favMangaList = new ArrayList<>();
        // Create a new adapter for the ListView
        adapter = new MangaAdapter(this, R.layout.list_item_manga, favMangaList);

        // Get the ListView and bind it with the adapter
        ListView listView = findViewById(R.id.manga_list_view);
        listView.setAdapter(adapter);

        // Add a ChildEventListener to the database reference to retrieve data
        mangasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                if (snapshot.getValue() instanceof HashMap) {
                    // Retrieve the child data as a HashMap object
                    HashMap<String, String> mangaData = (HashMap<String, String>) snapshot.getValue();

                    // Create a new Manga object from the retrieved data
                    String title = mangaData.get("title");
                    String imageUrl = mangaData.get("imageUrl");

                    Manga manga = new Manga(title);
                    manga.setImageUrl(imageUrl); // Set the image URL for the Manga object

                    // Add the Manga object to the list
                    favMangaList.add(manga);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Favoris", "Data is not stored as a HashMap");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Nothing to do here
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Manga manga = snapshot.getValue(Manga.class);
                if (manga != null) {
                    // Remove the Manga object from the list
                    favMangaList.remove(manga);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Favoris", "Failed to retrieve manga data");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Nothing to do here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favoris", "Failed to read database: " + error.getMessage());
            }
        });
    }
}
