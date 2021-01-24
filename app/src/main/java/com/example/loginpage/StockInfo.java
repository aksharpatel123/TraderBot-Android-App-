package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class StockInfo extends AppCompatActivity {

    ImageView iv_graph;
    EditText date;
    TextView tv_title, showDate;
    Button enter;

    String stock_str, date_str, time_x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);


        date = findViewById(R.id.date_in);
        iv_graph = findViewById(R.id.imageGraph);
        tv_title = findViewById(R.id.show_title);
        showDate = findViewById(R.id.show_date);
        enter = findViewById(R.id.mybutton);


        // Get stock symbol from user class
        User user = new User();
        stock_str = user.fav_stock;

        tv_title.setText(stock_str + " : Price vs. Time Chart");

        time_x = "8,9,10,11,12,13,14,15";

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();

        // date_str = "2020-11-23" ;
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date_str = date.getText().toString().trim();
                showDate.setText("on " + date_str); // display the date entered by user

                PyObject pyobj = py.getModule("apicall");
                PyObject obj = pyobj.callAttr("getPrice", date_str, stock_str);

                String temp_csv = obj.toString(); // '1.20,...,10.26,'
                String price_str = temp_csv.substring(0, temp_csv.length() - 1); // Remove extra ',' at end of str



                // Now graph the Price vs Time
                PyObject pyo = py.getModule("graph");
                // PyObject graph_obj = pyo.callAttr("main", time_x, price_str);
                PyObject graph_obj = pyo.callAttr("main", time_x, price_str);

                // convert obj to str
                String str = graph_obj.toString();
                // convert it to byte array
                byte data[] = android.util.Base64.decode(str, Base64.DEFAULT);
                // Convert to bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                // now set it to image view
                iv_graph.setImageBitmap(bmp);
                // Graph will be displayed in image view

            }
        });

    }
}