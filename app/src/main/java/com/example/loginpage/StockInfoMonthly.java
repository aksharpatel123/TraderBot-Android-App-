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

public class StockInfoMonthly extends AppCompatActivity {

    EditText getDate;
    TextView graph_title, graph_date;
    ImageView month_graph;
    Button enterClick;

    String stock_str, date_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info_monthly);

        getDate = findViewById(R.id.date_input);
        graph_title = findViewById(R.id.title_m);
        graph_date = findViewById(R.id.on_date);
        month_graph = findViewById(R.id.graph_img);
        enterClick = findViewById(R.id.enter_btn);


        // Get stock symbol from user class
        User user = new User();
        stock_str = user.fav_stock;
        graph_title.setText(stock_str + " : Price vs. Time Chart");

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();


        enterClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_str = getDate.getText().toString().trim();

                String[] arr = date_str.split("-");
                int year = Integer.parseInt(arr[0]);
                int month = Integer.parseInt(arr[1]);
                int day = Integer.parseInt(arr[2]);

                graph_date.setText("of " + year + "-" + month);


                PyObject pyobj = py.getModule("apicall");
                PyObject obj = pyobj.callAttr("getDaily", year, month, day, stock_str);


                String temp_csv = obj.toString(); // '1.20,...,10.26,'
                String price_str = temp_csv.substring(0, temp_csv.length() - 1); // Remove extra ',' at end of str
                String days = "";
                int d=0;
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    d=31;
                }
                else if (month==2) {
                    d=28;
                }
                else  {
                    d=30;
                }

                for (int i=1; i<=d; i++) {
                    days += Integer.toString(i) + ",";
                }
                String pass_days = days.substring(0, days.length()-1);


                // Now graph the Price vs Time
                PyObject pyo = py.getModule("graph");
                // PyObject graph_obj = pyo.callAttr("main", time_x, price_str);
                PyObject graph_obj = pyo.callAttr("main", pass_days, price_str);

                // convert obj to str
                String str = graph_obj.toString();
                // convert it to byte array
                byte data[] = android.util.Base64.decode(str, Base64.DEFAULT);
                // Convert to bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                // now set it to image view
                month_graph.setImageBitmap(bmp);
                // Graph will be displayed in image view
            }
        });

    }
}