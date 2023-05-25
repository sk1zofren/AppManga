package com.example.myapplication;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentBroadcastReceiver extends BroadcastReceiver {
    public static final String KEY_COMMENT_REPLY = "key_comment_reply";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference commentsRef;
    private String mangaTitle;
    private String mangaId;
    private FirebaseUser user;
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the comment reply from the remote input
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String replyText = remoteInput.getCharSequence(KEY_COMMENT_REPLY).toString();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            mangaId = intent.getStringExtra("manga_id");
            mangaTitle = intent.getStringExtra("manga_title");
            String userName = user.getEmail();

            // Initialize Firebase database
            database = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/");
            commentsRef = database.getReference("manga_comments").child(mangaTitle).child("comments");

            comments = new ArrayList<>();
            commentAdapter = new CommentAdapter(comments);

            fetchComments();

            // Add the new comment to the Firebase database
            Comment comment = new Comment(userName, replyText, System.currentTimeMillis());
            DatabaseReference commentRef = commentsRef.push();
            commentRef.setValue(comment)
                    .addOnSuccessListener(aVoid -> {
                        // Handle the success, if needed
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error, if needed
                    });

            // Cancel the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1);
        }
    }

    // Fetch comments from Firebase database
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
}
