package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Initialize views
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
    }

    // Handle registration button click
    public void GoRegister(View view) {
        progressBar.setVisibility(View.VISIBLE);

        // Get email and password from input fields
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Registration successful
                            Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                            // Automatically log in the user and redirect to the main menu
                            loginUser(email, password);
                        } else {
                            // Registration failed
                            Toast.makeText(Register.this, "Authentication failed. Please check your information.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Handle "Login Now" button click to go to login page
    public void ToLoginPage(View view) {
        Intent myIntent = new Intent(Register.this, Login.class);
        startActivity(myIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    // Log in the user with email and password and redirect to the main menu
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(Register.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Login failed
                            Toast.makeText(Register.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
