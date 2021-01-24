package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class GetHelp extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);

        drawerLayout = findViewById(R.id.drawer_layout);

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
        // Redirect activity to search
        Home.redirectActivity(this,Messages.class);
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
        // Recreate activity
        recreate();
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