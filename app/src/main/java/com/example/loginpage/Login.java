package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class Login extends AppCompatActivity {

    EditText emailTxt, passwordTxt;
    Button loginbtn, registerbtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailTxt = findViewById(R.id.emailField);
        passwordTxt = findViewById(R.id.passwordField);
        loginbtn = findViewById(R.id.loginBtn);
        registerbtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.loginProgressBar);
        fAuth = FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Login.this, "Register Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = emailTxt.getText().toString().trim();
                String passwordStr = passwordTxt.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)) {
                    emailTxt.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(passwordStr)) {
                    passwordTxt.setError("Password is required.");
                    return;
                }

                if (passwordStr.length() < 6) {
                    passwordTxt.setError("Must be at least 6 characters long!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate the user

                fAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            //startActivity(new Intent(getApplicationContext(), HomePage.class));
                            // startActivity(new Intent(getApplicationContext(), Home.class));
                            // Get users uid
                            FirebaseUser currentUser = fAuth.getCurrentUser();
                            String email = currentUser.getEmail();
                            String userID = currentUser.getUid();

                            User user = new User(email, userID);
                            user.setUser(email, userID);


                            // Pass user info to Profile page
                            // Intent intent = new Intent(getApplicationContext(), Home.class);
                            // intent.putExtra("email", user.email);

                            // startActivity(intent);
                            startActivity(new Intent(getApplicationContext(), Home.class));

                        }
                        else {
                            Toast.makeText(Login.this, "Incorrect Email or Password!" + task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
    }
}