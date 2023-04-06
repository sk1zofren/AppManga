package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MangaCommentActivity extends AppCompatActivity {

    private String mangaId;
    private String mangaTitle;

    private TextView titleTextView;
    private EditText commentEditText;
    private RecyclerView commentsRecyclerView;

    private FirebaseDatabase database;
    private DatabaseReference commentsRef;
    private FirebaseUser user;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comments;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_comment);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        mangaId = intent.getStringExtra("manga_id");
        mangaTitle = intent.getStringExtra("manga_title");
        String commentId = getIntent().getStringExtra("comment_id");

        if (mangaId != null && commentId != null) {
            loadSpecificComment(mangaId, commentId);
        } else {
            // Gérer le cas où mangaId ou commentId est null
        }

        if (mangaTitle == null || mangaTitle.isEmpty()) {
            // Handle the error, for example, by showing an error message or finishing the activity
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




        titleTextView = findViewById(R.id.manga_title_text_view);
        commentEditText = findViewById(R.id.comment_edit_text);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        titleTextView.setText(mangaTitle);

        database = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/");
        commentsRef = database.getReference("manga_comments").child(mangaTitle).child("comments");

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        fetchComments();
    }

    private void sendNotification(String mangaId, String mangaTitle, String userName, String commentText, String commentId) {
        // 1. Create the intent to open MangaCommentActivity
        Intent intent = new Intent(this, MangaCommentActivity.class);
        intent.putExtra("manga_id", mangaId);
        intent.putExtra("comment_id", commentId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // 2. Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 3. Build the notification with the PendingIntent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MangaCommentActivity.this, "My Notification")
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle(mangaTitle) // Add the manga title
                .setContentText(userName + ": " + commentText) // Add the username and the comment
                .setContentIntent(pendingIntent) // Add the PendingIntent
                .setAutoCancel(true); // The notification closes automatically when clicked

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MangaCommentActivity.this);
        managerCompat.notify(1, builder.build());
    }


    private void loadSpecificComment(String mangaId, String commentId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/").getReference("manga_comments").child(mangaId).child("comments").child(commentId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null) {
                    // Mettez à jour l'interface utilisateur avec le commentaire spécifique
                    updateUIWithSpecificComment(comment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });
    }

    private void updateUIWithSpecificComment(Comment comment) {
        // Mettez à jour l'interface utilisateur avec le commentaire spécifique
        // Par exemple, vous pouvez utiliser un TextView pour afficher le texte du commentaire
        TextView commentTextView = findViewById(R.id.specific_comment_text_view);
        commentTextView.setText(comment.getCommentText());
    }




    private void fetchComments() {
        commentsRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    comments.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void addComment(View view) {
        String commentText = commentEditText.getText().toString().trim();
        if (commentText.isEmpty()) {
            // Show error message
            return;
        }

        // Get user information (Replace with actual user data)
        String userName = user.getEmail();

        Comment comment = new Comment(userName, commentText, System.currentTimeMillis());

        // Store the reference of the comment
        DatabaseReference commentRef = commentsRef.push();
        commentRef.setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    commentEditText.setText("");
                    // Use commentRef.getKey() to get the key of the comment
                    sendNotification(mangaId, mangaTitle, userName, commentText, commentRef.getKey());
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

}
