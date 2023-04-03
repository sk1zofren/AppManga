package com.example.myapplication;

import static com.example.myapplication.Register.pseudo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_comment);

        // Get manga information from the Intent
        Intent intent = getIntent();
        mangaId = intent.getStringExtra("manga_id");
        mangaTitle = intent.getStringExtra("manga_title");

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

        String userName = pseudo;

        Comment comment = new Comment( userName, commentText, System.currentTimeMillis());
        commentsRef.push().setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    commentEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });

    }
}
