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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText first_name, last_name, reg_email, username, password;
    TextView backToLogin;
    Button reg_btn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    FirebaseDatabase rootNode; // Instance of realtime DB root node (Trader-bot).
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastName);
        reg_email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        reg_btn = findViewById(R.id.reg_button);
        backToLogin = findViewById(R.id.backToLogin);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calls Trader-bot root node.
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");


                // Get all values in String format
                String first_name_str = first_name.getText().toString().trim();
                String last_name_str = last_name.getText().toString().trim();
                String reg_email_str = reg_email.getText().toString().trim();
                String username_str = username.getText().toString().trim();
                String password_str = password.getText().toString().trim();


                if (TextUtils.isEmpty(first_name_str)) {
                    first_name.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(last_name_str)) {
                    last_name.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(reg_email_str)) {
                    reg_email.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(username_str)) {
                    username.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(password_str)) {
                    password.setError("Required Field");
                    return;
                }
                if (password_str.length() < 6) {
                    password.setError("Must be at least 6 chars long!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Now Register the user in Firebase

                fAuth.createUserWithEmailAndPassword(reg_email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String userID = fAuth.getUid();

                            RegistrationHelperClass regHelper = new RegistrationHelperClass(first_name_str, last_name_str, reg_email_str, username_str, password_str, userID);
                            reference.child(userID).setValue(regHelper);

                            Toast.makeText(Register.this, "New User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Incorrect Email or Password!" + task.getException(), Toast.LENGTH_SHORT).show();
                            // We could include error message, also display the msg short and meaningful.
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

                //

            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}