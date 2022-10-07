package com.example.complaintapp;

import static java.util.Objects.requireNonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_main);


        new Handler().postDelayed(() -> {
            Intent i=new Intent(MainActivity.this,
                    HomeActivity.class);
            //Intent is used to switch from one activity to another.
            startActivity(i);
            //invoke the SecondActivity.
            finish();
            //the current activity will get finished.
        }, 1000);
    }
}