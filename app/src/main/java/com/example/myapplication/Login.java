package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
    }

    public void GoLogin(View view){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Welcome " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ToRegisterPage(View view){
        Intent myIntent = new Intent(Login.this, Register.class);
        startActivity(myIntent);
        finish();
    }

    public void ToMangaPage(View view) {
        Intent myIntent = new Intent(Login.this, MangaList.class);
        startActivity(myIntent);
        finish();
    }
}
