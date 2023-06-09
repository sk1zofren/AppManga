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
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bumptech.glide.request.RequestOptions;



public class PrincipalPage extends AppCompatActivity {

    private DatabaseReference mangasRef;
    private ArrayList<Manga> favMangaList;
    private String title;
    private String imageUrl;

    private ScrollView col1ScrollView;
    private ScrollView col2ScrollView;
    private ScrollView col3ScrollView;
    private ScrollView col4ScrollView;
    private ScrollView col5ScrollView;
    private ScrollView col6ScrollView;

    private LinearLayout col1Container;
    private LinearLayout col2Container;
    private LinearLayout col3Container;
    private LinearLayout col4Container;
    private LinearLayout col5Container;
    private LinearLayout col6Container;

    private static final String TAG = "PrincipalPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);

        // Initialize Firebase authentication and retrieve the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Retrieve the ScrollView containers
        col1ScrollView = findViewById(R.id.col1_container);
        col2ScrollView = findViewById(R.id.col2_container);
        col3ScrollView = findViewById(R.id.col3_container);
        col4ScrollView = findViewById(R.id.col4_container);
        col5ScrollView = findViewById(R.id.col5_container);
        col6ScrollView = findViewById(R.id.col6_container);

        // Retrieve the LinearLayouts inside the ScrollView containers
        col1Container = col1ScrollView.findViewById(R.id.col1_linear_layout);
        col2Container = col2ScrollView.findViewById(R.id.col2_linear_layout);
        col3Container = col3ScrollView.findViewById(R.id.col3_linear_layout);
        col4Container = col4ScrollView.findViewById(R.id.col4_linear_layout);
        col5Container = col5ScrollView.findViewById(R.id.col5_linear_layout);
        col6Container = col6ScrollView.findViewById(R.id.col6_linear_layout);

        // Initialize the drag and drop listeners for each LinearLayout
        initializeDragAndDropListeners(col1Container);
        initializeDragAndDropListeners(col2Container);
        initializeDragAndDropListeners(col3Container);
        initializeDragAndDropListeners(col4Container);
        initializeDragAndDropListeners(col5Container);
        initializeDragAndDropListeners(col6Container);

        // Check if the user is logged in and retrieve the database reference
        if (currentUser != null) {
            mangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                    .getReference("listFavManga")
                    .child(currentUser.getUid());
        } else {
            // Handle the case when the user is not logged in, for example, by displaying a message
        }

        // Create a new ArrayList to store favorite mangas
        favMangaList = new ArrayList<>();

        // Set up ChildEventListener for database reference
        mangasRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Do nothing if you don't need to handle this event
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                if (snapshot.getValue() instanceof HashMap) {
                    // Retrieve data of the child as a HashMap object
                    HashMap<String, String> mangaData = (HashMap<String, String>) snapshot.getValue();

                    // Create a new Manga object from the retrieved data
                    String title = mangaData.get("title");
                    String imageUrl = mangaData.get("imageUrl");

                    Manga manga = new Manga(title);
                    manga.setImageUrl(imageUrl); // Assign the image URL to the Manga object

                    // Add the Manga object to the list
                    favMangaList.add(manga);

                    // Create an ImageView for the manga image
                    ImageView imageView = new ImageView(PrincipalPage.this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setTag(imageUrl);
                    Glide.with(PrincipalPage.this)
                            .load(imageUrl)
                            .override(140, 150)
                            .apply(RequestOptions.bitmapTransform(new Rotate(270)))
                            .into(imageView);
                    imageView.setOnTouchListener(new ImageTouchListener());

                    // Find the container with the fewest images and add the ImageView
                    LinearLayout smallestContainer = col1Container;
                    int smallestChildCount = col1Container.getChildCount();

                    for (LinearLayout container : new LinearLayout[]{col2Container, col3Container, col4Container, col5Container, col6Container}) {
                        if (container.getChildCount() < smallestChildCount) {
                            smallestChildCount = container.getChildCount();
                            smallestContainer = container;
                        }
                    }

                    smallestContainer.addView(imageView);
                } else {
                    // Log an error if the data is not stored as a HashMap
                    Log.e(TAG, "Unexpected data type: ");
                }
            }
        });
    }

    // Inner class for handling touch events on the manga images
    private class ImageTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView imageView = (ImageView) view;
                String imageUrl = (String) imageView.getTag();

                // Create a ClipData object with the image URL as plain text
                ClipData data = ClipData.newPlainText("imageUrl", imageUrl);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    // Method to initialize drag and drop listeners for the containers
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

                        // Create an ImageView for the dropped image
                        ImageView imageView = new ImageView(PrincipalPage.this);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setTag(imageUrl);
                        Glide.with(PrincipalPage.this)
                                .load(imageUrl)
                                .override(140, 150)
                                .apply(RequestOptions.bitmapTransform(new Rotate(270)))
                                .into(imageView);
                        imageView.setOnTouchListener(new ImageTouchListener());

                        LinearLayout targetContainer = (LinearLayout) v;
                        targetContainer.setMinimumWidth(dpToPx(105));

                        View draggedView = (View) event.getLocalState();
                        ViewGroup sourceContainer = (ViewGroup) draggedView.getParent();
                        sourceContainer.removeView(draggedView);

                        boolean isImageAdded = false;
                        for (int i = 0; i < targetContainer.getChildCount(); i++) {
                            View child = targetContainer.getChildAt(i);
                            if (child instanceof ImageView) {
                                float targetX = event.getX();
                                if (child.getX() + child.getWidth() / 2 > targetX) {
                                    targetContainer.addView(imageView, i);
                                    isImageAdded = true;
                                    break;
                                }
                            }
                        }
                        if (!isImageAdded) {
                            targetContainer.addView(imageView);
                        }
                        v.setBackgroundColor(Color.TRANSPARENT);
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

    // Method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}


