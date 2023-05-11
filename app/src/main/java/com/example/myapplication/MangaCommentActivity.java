package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

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
        DatabaseReference userFavMangasRef = FirebaseDatabase.getInstance("https://mangas-a1043.europe-west1.firebasedatabase.app/")
                .getReference("listFavManga")
                .child(user.getUid());

        userFavMangasRef.orderByChild("title").equalTo(mangaTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The manga is in the user's favorites list, proceed with sending the notification
                    Intent intent = new Intent(MangaCommentActivity.this, MangaCommentActivity.class);
                    intent.putExtra("manga_title", mangaTitle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MangaCommentActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);



                    RemoteInput remoteInput = new RemoteInput.Builder(CommentBroadcastReceiver.KEY_COMMENT_REPLY)
                            .setLabel("Add a comment")
                            .build();

                    Intent replyIntent = new Intent(MangaCommentActivity.this, CommentBroadcastReceiver.class);
                    replyIntent.putExtra("manga_title", mangaTitle);
                    PendingIntent replyPendingIntent = PendingIntent.getBroadcast(MangaCommentActivity.this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);


                    NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.a_j, "Reply", replyPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MangaCommentActivity.this, "My Notification")
                            .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_focused)
                            .setContentTitle(mangaTitle)
                            .setContentText(userName + ": " + commentText)
                            .setContentIntent(pendingIntent)
                            .addAction(replyAction)
                            .setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MangaCommentActivity.this);
                    managerCompat.notify(1, builder.build());
                } else {
                    // The manga is not in the user's favorites list, do not send the notification
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
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

    public void ToMenu(View view) {
        Intent myIntent = new Intent(MangaCommentActivity.this, MainActivity.class);
        startActivity(myIntent);
    }

}
