package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;

public class Messages extends AppCompatActivity {
    // Initialize variable
    DrawerLayout drawerLayout;

    Button bb;
    String stock_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        bb = findViewById(R.id.bt);
        // Get stock symbol from user class
        User user = new User();
        stock_string = user.fav_stock;


        // Python starter
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();

        // create channel after oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My noti", "My noti", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PyObject mod = py.getModule("livepr");
                PyObject getSug = mod.callAttr("myfunc", stock_string); // Can be diff for each function

                //tv_suggest.setText(getSug.toString());

                String notify_str = getSug.toString();

                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext(), "My noti");
                builder.setContentTitle("TraderBot Notification");
                builder.setContentText(notify_str);
                builder.setSmallIcon(R.drawable.ic_notify);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                managerCompat.notify(1, builder.build());

            }
        });





    }

    public void ClickMenu(View view) {
        // Open Drawer
        Home.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        // Close drawer
        Home.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        // Redirect activity to home
        Home.redirectActivity(this, Home.class);
    }

    public void ClickMessages(View view) {
        // Recreate activity
        recreate();
    }

    public void ClickSearch(View view) {
        // Redirect activity to search
        Home.redirectActivity(this,Search.class);
    }

    public void ClickMyWatchList(View view) {
        // Redirect activity to my watchlist
        Home.redirectActivity(this, MyWatchList.class);
    }

    public void ClickGetHelp(View view) {
        // Redirect activity to my watchlist
        Home.redirectActivity(this, GetHelp.class);
    }

    public void ClickLogout(View view) {
        // Close app
        Home.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Close drawer
        Home.closeDrawer(drawerLayout);
    }
}