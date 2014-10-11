package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.teamhardwork.kipp.R;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Welcome to KIPP!", Toast.LENGTH_SHORT).show();

    }
}
