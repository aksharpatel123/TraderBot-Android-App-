package com.example.loginpage;

import android.content.Intent;

public class User {

    public static String email = "";
    public static String UID = "";
    public static String fav_stock = "";

    // Empty constructor to avoid errors in firebase
    public User(String email, String UID) {
        this.email = email;
        this.UID = UID;
    }

    public User() {}


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public static String getFav_stock() {
        return fav_stock;
    }

    public static void setFav_stock(String fav_stock) {
        User.fav_stock = fav_stock;
    }

    public void setUser(String email, String UID) {
        this.email = email;
        this.UID = UID;
    }

    public void setStock(String fav_stock) {
        this.fav_stock = fav_stock;
    }

}
