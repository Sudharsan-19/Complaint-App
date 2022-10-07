package com.example.complaintapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity {

    private static String Ulocation;

    public static String getValue() {

        return Ulocation;
    }

    final int[] checkedItem = {-1};
    int REQUEST_LOCATION = 88;
    Button button1,langbtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadLocale();
        setContentView(R.layout.activity_home);
        getLocation();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        button1 = (Button) findViewById(R.id.button);
        langbtn=(Button) findViewById(R.id.langbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(HomeActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23);
                }
            }
        });
        langbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] listItems={"English","தமிழ்","हिन्दी","తెలుగు","മലയാളം"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(HomeActivity.this);
        mBuilder.setTitle("Choose language...");
        mBuilder.setIcon(R.drawable.ic_baseline_language_24);
        mBuilder.setSingleChoiceItems(listItems,checkedItem[0],new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            if(i==0){
                setLocale("en");
                recreate();
            }
            else if(i==1){
                setLocale("ta");
                recreate();
            }
            else if(i==2){
                setLocale("hi");
                recreate();
            }
            else if(i==3){
                setLocale("te");
                recreate();
            }
            else if(i==4){
                setLocale("ml");
                recreate();
            }
            dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog= mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My languages",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My languages","");
        setLocale(language);

    }

    @SuppressLint("MissingPermission")
    private void Location() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                //play with location
                openRegister();
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                        break;
                    //doesnt havve location feature
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void getLocation() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(HomeActivity.this
                                        , Locale.getDefault());
                                List<Address> address = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1
                                );
                                Ulocation = ("https://maps.google.com/?q=" + address.get(0).getLatitude() + "," + address.get(0).getLongitude());
                                Log.i("User location link", "User lat and long " + String.valueOf(Ulocation));
                                //Toast.makeText(HomeActivity.this, Ulocation, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(HomeActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public void openRegister(){
        Intent intent=new Intent(this, Register.class);
        startActivity(intent);
    }
}