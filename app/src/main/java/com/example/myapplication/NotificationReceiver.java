package com.example.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String mangaId = intent.getStringExtra("manga_id");
        String commentId = intent.getStringExtra("comment_id");

        Intent activityIntent = new Intent(context, MangaCommentActivity.class);
        activityIntent.putExtra("manga_id", mangaId);
        activityIntent.putExtra("comment_id", commentId);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);

        // ...
    }
}
