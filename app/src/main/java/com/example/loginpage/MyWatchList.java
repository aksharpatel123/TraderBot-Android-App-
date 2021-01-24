package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyWatchList extends AppCompatActivity {
    // Initialize variable
    DrawerLayout drawerLayout;

    TextView stock_symbol;
    TextView live_price, open, high, low, close, volume;
    Button monthBtn, dailyBtn;
    String stock_str = "";

    // Firebase var to store user fav stock to DB
    FirebaseDatabase rootNode; // Instance of root node (Trader-bot).
    DatabaseReference reference;
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watchlist);

        // Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);


        stock_symbol = findViewById(R.id.symbol);
        live_price = findViewById(R.id.stock_live);
        open = findViewById(R.id.stock_open);
        high = findViewById(R.id.stock_high);
        low = findViewById(R.id.stock_low);
        close = findViewById(R.id.stock_close);
        volume = findViewById(R.id.stock_volume);
        monthBtn = findViewById(R.id.monthly);
        dailyBtn = findViewById(R.id.daily);


        // Get stock symbol from user class
        User user = new User();
        stock_str = user.fav_stock;
        UID = user.UID;



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        // String[] yyyymmdd = dateTime.split("-");

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("apicall");
        PyObject obj = pyobj.callAttr("main", stock_str); // App wont work, if yesterday was holiday like sat/sun (no stock data available)
        String s = obj.toString();
        String[] stockArr = s.split(",");

        PyObject pylive_pr = py.getModule("livepr");
        PyObject obj_price = pylive_pr.callAttr("parsePrice", stock_str); // price for any stock
        

        // Set symbol and other values under watchlist menu
        stock_symbol.setText(stock_str);
        // stock_symbol.setText(s);
        live_price.setText("Live Price: " + obj_price.toString());
        open.setText("Open price: $ " + stockArr[0]);
        high.setText("High: $ " + stockArr[1]);
        low.setText("Low: $ " + stockArr[2]);
        close.setText("Close price: $ " + stockArr[3]);
        volume.setText("Volume: " + stockArr[4]);


        // Update fav stock in Database
        // NOTE: Only 1 stock can be added to watchlist
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child(UID).child("Stock").setValue(stock_str);

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StockInfoMonthly.class));
            }
        });

        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StockInfo.class));
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
        // Redirect activity to dashboard
        Home.redirectActivity(this, Messages.class);
    }

    public void ClickSearch(View view) {
        // Redirect activity to search
        Home.redirectActivity(this,Search.class);
    }

    public void ClickMyWatchList(View view) {
        // Recreate activity
        recreate();

    }

    public void ClickGetHelp(View view) {
        // Redirect activity to search
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