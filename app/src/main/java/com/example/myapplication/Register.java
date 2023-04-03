package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    DatabaseReference UserRef;
     TextInputEditText editTextEmail, editTextPassword;
     FirebaseAuth mAuth;
     ProgressBar progressBar;
     TextView textView;
    public static String pseudo;
     EditText username;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        username = findViewById(R.id.User);


    }


    public void GoRegister(View view){

        progressBar.setVisibility(View.VISIBLE);
         String email, password;
         email = String.valueOf(editTextEmail.getText());
         password = String.valueOf(editTextPassword.getText());


         if (TextUtils.isEmpty(email)){
             Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
             return;
         }


         if ( TextUtils.isEmpty(password)){
             Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
             return;
         }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            User user1 = new User();
                            pseudo = username.getText().toString();
                            user1.setUsername(pseudo);

                            UserRef = FirebaseDatabase.getInstance("https://mangas-a1043-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Acccount");
                            UserRef.push().setValue(user1);

                            Toast.makeText(Register.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    public void ToLoginPage(View view){

        Intent myIntent = new Intent(Register.this, Login.class);
        startActivity(myIntent);

    }
}