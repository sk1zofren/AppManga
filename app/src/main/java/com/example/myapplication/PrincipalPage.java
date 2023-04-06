package com.example.myapplication;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrincipalPage extends AppCompatActivity {

    private DatabaseReference mangasRef;
    private ArrayList<Manga> favMangaList;
    private String title;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Retrieve the HorizontalScrollView containers
        ScrollView col1ScrollView = findViewById(R.id.col1_container);
        ScrollView col2ScrollView = findViewById(R.id.col2_container);
        ScrollView col3ScrollView = findViewById(R.id.col3_container);

        // Retrieve the LinearLayouts inside the HorizontalScrollView containers
        LinearLayout col1Container = col1ScrollView.findViewById(R.id.col1_linear_layout);
        LinearLayout col2Container = col2ScrollView.findViewById(R.id.col2_linear_layout);
        LinearLayout col3Container = col3ScrollView.findViewById(R.id.col3_linear_layout);


        // Initialize the drag and drop listeners for each LinearLayout
        initializeDragAndDropListeners(col1Container);
        initializeDragAndDropListeners(col2Container);
        initializeDragAndDropListeners(col3Container);

        // Retrieve the database reference
        if (currentUser != null) {
            mangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                    .getReference("listFavManga")
                    .child(currentUser.getUid());
        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté, par exemple en affichant un message
        }

        // Create a new ArrayList to store favorite mangas
        ArrayList<Manga> favMangaList = new ArrayList<>();

        // Set up ChildEventListener for database reference
        mangasRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Ne rien faire si vous n'avez pas besoin de gérer cet événement
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //...
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                //...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //...
            }
            @SuppressLint("RestrictedApi")




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

                    ImageView imageView = new ImageView(PrincipalPage.this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setTag(imageUrl);
                    Glide.with(PrincipalPage.this)
                            .load(imageUrl)
                            .override(105, 200)
                            .into(imageView);
                    imageView.setOnTouchListener(new ImageTouchListener());

                    // Add the ImageView to the correct LinearLayout container
                    if (col1Container.getChildCount() <= col2Container.getChildCount() && col1Container.getChildCount() <= col3Container.getChildCount()) {
                        col1Container.addView(imageView);
                    } else if (col2Container.getChildCount() <= col3Container.getChildCount()) {
                        col2Container.addView(imageView);
                    } else {
                        col3Container.addView(imageView);
                    }
                } else {
                    // Log an error if the data is not stored as a HashMap
                    Log.e(TAG, "Unexpected data type: ");
                }
            }

            // ...
        });
    }
    private class ImageTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView imageView = (ImageView) view;
                String imageUrl = (String) imageView.getTag();

                ClipData data = ClipData.newPlainText("imageUrl", imageUrl);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private void initializeDragAndDropListeners(LinearLayout container) {
        container.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return event.getClipDescription().hasMimeType("text/plain");
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(Color.LTGRAY);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        return true;
                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        String imageUrl = item.getText().toString();

                        ImageView imageView = new ImageView(PrincipalPage.this);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setTag(imageUrl);
                        Glide.with(PrincipalPage.this)
                                .load(imageUrl).override(105, 200)
                                .into(imageView);
                        imageView.setOnTouchListener(new ImageTouchListener());

                        LinearLayout targetContainer = (LinearLayout) v;
                        // Ajout de la ligne suivante
                        targetContainer.setMinimumWidth(dpToPx(105));

                        View draggedView = (View) event.getLocalState();
                        ViewGroup sourceContainer = (ViewGroup) draggedView.getParent();
                        sourceContainer.removeView(draggedView);

                        // Modification ici : Ajout de l'image à côté de l'image déjà présente
                        boolean isImageAdded = false;
                        for (int i = 0; i < targetContainer.getChildCount(); i++) {
                            View child = targetContainer.getChildAt(i);
                            if (child instanceof ImageView) {
                                float targetX = event.getX();
                                if (child.getX() > targetX) {
                                    targetContainer.addView(imageView, i);
                                    isImageAdded = true;
                                    break;
                                }
                            }
                        }
                        if (!isImageAdded) {
                            targetContainer.addView(imageView);
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // Méthode pour convertir les dp en pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}

