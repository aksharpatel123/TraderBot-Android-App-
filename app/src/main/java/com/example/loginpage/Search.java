package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    // Initialize variable
    DrawerLayout drawerLayout;

    // search bar variables
    EditText search_input;
    Button searchBtn;

    // Search list view variables
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        // Search code
        listView = findViewById(R.id.list_view);
        // add items in array list
        stringArrayList.add("TSLA");
        stringArrayList.add("AAPL");
        stringArrayList.add("MSFT");

        // Initialize adapter
        adapter = new ArrayAdapter<>(Search.this, android.R.layout.simple_list_item_1,stringArrayList);
        // set adapter on list view
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Display click item position in toast
                Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(getApplicationContext(), StockInfo.class));
            }
        });

        search_input = findViewById(R.id.searchstr);
        searchBtn = findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Holds the search string
                String search_stock_str = search_input.getText().toString().trim();

                // Clears the search field
                search_input.setText("");

                // Clear suggestions and load new searched stock
                stringArrayList.clear();
                stringArrayList.add(search_stock_str);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                // Set title
                builder.setTitle("Add as favourite");
                // Set message
                builder.setMessage("Are you sure you want to add this stock to your watchlist ?");
                // Positive yes button
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String stock = adapter.getItem(position);

                        // code to add in watchlist
                        User user = new User();
                        user.setStock(stock);

                        // Message for user
                        Toast.makeText(getApplicationContext(), adapter.getItem(position) + " has been added to your watchlist!", Toast.LENGTH_SHORT).show();

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


                return false;
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

    public void ClickSearch(View view) {
        // Recreate activity
        recreate();
    }

    public void ClickMessages(View view) {
        // Redirect activity to Dashboard
        Home.redirectActivity(this, Messages.class);
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