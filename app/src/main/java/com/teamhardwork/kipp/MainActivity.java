package com.teamhardwork.kipp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class MainActivity extends Activity {
    public static final String YOUR_APPLICATION_ID = "6fb5KhXW73bUQKwdAb807wiIt9tROQ2HtHAYmKOq";
    public static final String YOUR_CLIENT_KEY = "y703WGXSG0rY4qvuP1dhM3vn1Qo4efXMMXoxtj12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
