package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MangaList extends AppCompatActivity {
    FirebaseAuth mAuth;
    private ListView mangaListView;
    private ArrayAdapter<Manga> mangaListAdapter;
    private ArrayList<Manga> mangaList;
    ArrayList<Manga> favMangaList;

    DatabaseReference UserRef;
    private FirebaseUser user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);
        mangaListView = findViewById(R.id.manga_list_view);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mangaListAdapter = new ArrayAdapter<Manga>(this, R.layout.list_item_manga, new ArrayList<Manga>()) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item_manga, null);
                }

                ImageView imageView = convertView.findViewById(R.id.image_view);
                Manga manga = getItem(position);

                Picasso.get()
                        .load(manga.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .resize(300, 800)
                        .centerInside() // Vous pouvez utiliser centerCrop() à la place, selon vos besoins.
                        .into(imageView);

                return convertView;
            }
        };

        mangaListView.setAdapter(mangaListAdapter);

        mangaList = new ArrayList<Manga>();
        favMangaList = new ArrayList<Manga>();

        new RetrieveMangaListTask().execute();

        mangaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Manga selectedManga = mangaListAdapter.getItem(i);
                showMangaDetails(selectedManga);
            }
        });
    }

    //methode que je suis obliger d'implémenter sinon quand j'appuie sur le boutton retour, je quite l'a^p
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showMangaDetails(Manga manga) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(manga.getTitle());
        builder.setMessage(manga.getSynopsis());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton("Commenter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MangaList.this, MangaCommentActivity.class);
                intent.putExtra("manga_title", manga.getTitle());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Add Fav", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference listFavMangaRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                        .getReference()
                        .child("listFavManga")
                        .child(user.getUid());


                Query query = listFavMangaRef.orderByChild("title").equalTo(manga.getTitle());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                Integer currentFav = childSnapshot.child("fav").getValue(Integer.class);
                                if (currentFav == null) {
                                    currentFav = 0;
                                }
                                int newFavCount = currentFav + 1;
                                childSnapshot.getRef().child("fav").setValue(newFavCount);

                                // Update the Manga object's fav value
                                manga.setFav(newFavCount);

                            }
                        } else {
                            DatabaseReference newFavMangaRef = listFavMangaRef.push();
                            HashMap<String, Object> mangaData = new HashMap<>();
                            mangaData.put("title", manga.getTitle());
                            mangaData.put("imageUrl", manga.getImageUrl());
                            mangaData.put("fav", 1); // Initialize the fav count to 1

                            newFavMangaRef.setValue(mangaData);

                            // Update the Manga object's fav value
                            manga.setFav(1);

                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }

        });


        AlertDialog dialog = builder.create();
          dialog.show();


    }

        private class RetrieveMangaListTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://api.jikan.moe/v4/manga");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override

            protected void onPostExecute(String response) {
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray mangaListArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < mangaListArray.length(); i++) {
                            JSONObject mangaJson = mangaListArray.getJSONObject(i);
                            Manga manga = new Manga();
                            manga.setTitle(mangaJson.getString("title"));
                            manga.setSynopsis(mangaJson.getString("synopsis"));
                            JSONObject images = mangaJson.getJSONObject("images");
                            JSONObject jpgImages = images.getJSONObject("jpg");
                            String imageUrl = jpgImages.getString("large_image_url");
                            manga.setImageUrl(imageUrl);
                            mangaList.add(manga);
                            System.out.println(manga.getTitle());

                        }
                        mangaListAdapter.clear();
                        mangaListAdapter.addAll(mangaList);

                        // Charger l'image pour chaque élément dans la liste
                        for (int i = 0; i < mangaList.size(); i++) {
                            final int position = i;

                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                                    mangaList.get(position).setImage(bitmap);


                                    mangaListAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                            Picasso.get().load(mangaList.get(i).getImageUrl()).into(target);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MangaList.this);
                    builder.setTitle("Error");
                    builder.setMessage("Failed to retrieve manga list from server");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

        }

}