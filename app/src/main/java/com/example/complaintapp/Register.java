package com.example.complaintapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    EditText inputmobile;
    Button getOTP;
    String loc1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        inputmobile=findViewById(R.id.inputMobile);
        getOTP =(Button) findViewById(R.id.getOtp);

        final ProgressBar progressbar= findViewById(R.id.progressBar);



        getOTP.setOnClickListener(view -> {

            if(!inputmobile.getText().toString().trim().isEmpty()) {
                if((inputmobile.getText().toString().trim()).length() == 10) {

                    progressbar.setVisibility(View.VISIBLE);
                    getOTP.setVisibility(View.INVISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + inputmobile.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            Register.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressbar.setVisibility(View.GONE);
                                    getOTP.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressbar.setVisibility(View.GONE);
                                    getOTP.setVisibility(View.VISIBLE);
                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCodeSent(@NonNull String backOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressbar.setVisibility(View.GONE);
                                    getOTP.setVisibility(View.VISIBLE);
                                    Intent intent=new Intent(getApplicationContext(),Verify.class);
                                    intent.putExtra("mobile",inputmobile.getText().toString());
                                    intent.putExtra("backOTP",backOTP);
                                    startActivity(intent);

                                }
                            }
                    );
                    Intent intent=new Intent(getApplicationContext(),Verify.class);
                    intent.putExtra("mobile",inputmobile.getText().toString());
                    intent.putExtra("UserLocation",loc1);
                    startActivity(intent);

                }else{
                    Toast.makeText(Register.this, "Check your number", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Register.this, "Please verify your number", Toast.LENGTH_SHORT).show();
            }
        });

    }
}