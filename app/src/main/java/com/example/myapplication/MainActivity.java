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
    EditText MyName;
    private FirebaseAuth auth;
    private TextView textView;
    private FirebaseUser user;
    DatabaseReference UserRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent myIntent = new Intent(MainActivity.this, Login.class);
            startActivity(myIntent);
        } else {
            textView.setText(" Welcome " + pseudo);
        }
        //ici on identifie l'edit Text et on cree le noeud dans la base de donnee


    }

    public void ToLogout(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

    public void ToPrincipalPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, PrincipalPage.class);
        startActivity(myIntent);
    }

    public void ToMangaPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, MangaList.class);
        startActivity(myIntent);
    }

    public void ToFavorisPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, Favoris.class);
        startActivity(myIntent);
    }

    public void ToMapPage(View view) {
        Intent myIntent = new Intent(MainActivity.this, Map.class);
        startActivity(myIntent);
    }

    // method who call the method insertUserData
    public void ToSend(View view) {
        InsertUsersData();
        getNotif();

        // Write a message to the database


    }

    // method who take the name varibale and put in in the parameter in User instance and push it in the DataBase
    private void InsertUsersData() {
/*
        MyName = findViewById(R.id.name);
        String name = MyName.getText().toString();
        User user1 = new User(name);
        UserRef = FirebaseDatabase.getInstance("https://mangas-a1043-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users");
        UserRef.push().setValue(user1);
        Toast.makeText(MainActivity.this, "Data Insert", Toast.LENGTH_SHORT).show();
*/
    }

    private void getNotif() {


        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(MainActivity.this, "My Notification")
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle("The Notification").setContentText("This is a notification for you");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

        managerCompat.notify(1, mbuilder.build());

    }
}
