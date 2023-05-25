package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the comment_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        // Return a new instance of CommentViewHolder
        return new CommentViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Get the comment at the specified position
        Comment comment = comments.get(position);
        // Set the username and comment text in the corresponding TextViews
        holder.userNameTextView.setText(comment.getUserName());
        holder.commentTextView.setText(comment.getCommentText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Provide a reference to the views for each comment item
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView commentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get references to the TextViews in the comment_item layout
            userNameTextView = itemView.findViewById(R.id.comment_user_name);
            commentTextView = itemView.findViewById(R.id.comment_text);
        }
    }
}