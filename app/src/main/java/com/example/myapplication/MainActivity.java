package com.example.myapplication;

import static com.example.myapplication.Register.pseudo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Check if the user is logged in
        if (user == null) {
            // If not logged in, redirect to Login activity
            Intent myIntent = new Intent(MainActivity.this, Login.class);
            startActivity(myIntent);
        } else {
            // If logged in, continue with the main activity
            // You can add code here to customize the behavior for a logged-in user
        }
    }

    // Logout button click event handler
    public void ToLogout(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

    // Button click event handlers for different activities
    public void ToPrincipalPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, PrincipalPage.class);
        startActivity(myIntent);
    }

    public void ToFavorisPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, Favoris.class);
        startActivity(myIntent);
    }

    public void ToClassement(View view) {
        Intent intent = new Intent(MainActivity.this, TopMangaActivity.class);
        startActivity(intent);
    }

    public void ToMapPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, Map.class);
        startActivity(myIntent);
    }

    public void ToMangaPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, MangaList.class);
        startActivity(myIntent);
        finish();
    }
}
