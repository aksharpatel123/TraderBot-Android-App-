package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView u_fname, u_lname, u_username, u_email, greet_msg, cur_date;
    // String email;
    // private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database; // Instance of realtime DB root node (Trader-bot).
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);



        // Intent intent = getIntent();
        // email = intent.getStringExtra("email");

        User user = new User();
        String email = user.email;
        // String UID = user.UID;

        cur_date = findViewById(R.id.current_date);
        greet_msg = findViewById(R.id.greet);
        u_fname = findViewById(R.id.fname);
        u_lname = findViewById(R.id.lname);
        u_username = findViewById(R.id.userName);
        u_email = findViewById(R.id.userEmail);

        // Todays Date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        //DatabaseReference userRef = database.getReference(firebaseAuth.getUid());

        u_email.setText(email);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(email)) {

                        cur_date.setText(currentDate);

                        greet_msg.setText("Welcome, " + ds.child("firstName").getValue(String.class));

                        u_fname.setText(" First Name: " + ds.child("firstName").getValue(String.class));
                        u_lname.setText(" Last name: " + ds.child("lastName").getValue(String.class));
                        u_username.setText(" Username: " + ds.child("username").getValue(String.class));
                        u_email.setText(" Email: " + ds.child("email").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void ClickMenu(View view) {
        // Open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        // Open drawer Layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        // Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        // Close drawer layout
        // check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // When drawer is open
            // Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickHome(View view) {
        // Recreate activity
        recreate();
    }

    public void ClickMessages(View view) {
        // Redirect activity to dashboard
        redirectActivity(this, Messages.class);
    }

    public void ClickSearch(View view) {
        // Redirect activity to dashboard
        redirectActivity(this,Search.class);
    }

    public void ClickMyWatchList(View view) {
        // Redirect activity to my watchlist
        redirectActivity(this, MyWatchList.class);
    }

    public void ClickGetHelp(View view) {
        // Redirect activity to my watchlist
        redirectActivity(this, GetHelp.class);
    }

    public void ClickLogout (View view) {
        // Close app
        logout(this);
    }

    public static void logout(final Activity activity) {
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set title
        builder.setTitle("Logout");
        // Set message
        builder.setMessage("Are you sure you want to log out ?");
        // Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Finish activity
                activity.finishAffinity();
                // Exit app
                System.exit(0);

            }
        });
        // Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss dialog
                dialog.dismiss();
            }
        });
        // Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        // Initialize intent
        Intent intent = new Intent(activity,aClass);
        // Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Close drawer
        closeDrawer(drawerLayout);
    }
}