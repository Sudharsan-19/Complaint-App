package com.example.complaintapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {

    private static String number;
    public static String getValue(){

        return number;
    }
    EditText inputnumber1;
    EditText inputnumber2;
    EditText inputnumber3;
    EditText inputnumber4;
    EditText inputnumber5;
    EditText inputnumber6;
    protected Button verifyOTP;
    String getBackOTP,no,loc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_verify);

        inputnumber1=findViewById(R.id.input1);
        inputnumber2=findViewById(R.id.input2);
        inputnumber3=findViewById(R.id.input3);
        inputnumber4=findViewById(R.id.input4);
        inputnumber5 = findViewById(R.id.input5);
        inputnumber6=findViewById(R.id.input6);
        setupOTPInputs();

        TextView textMobile=findViewById(R.id.textmobile);
        textMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        //no=textMobile.getText().toString();
        //loc2=getIntent().getExtras().getString("UserLocation");
        //Toast.makeText(Verify.this, HomeActivity.getValue(), Toast.LENGTH_SHORT).show();

        number=getIntent().getExtras().getString("mobile");
        Log.i("numbeechecker","numbeeer "+number);
        //Toast.makeText(Verify.this, "Your number class "+no, Toast.LENGTH_SHORT).show();

        getBackOTP=getIntent().getStringExtra("backOTP");

        final ProgressBar progressbar=findViewById(R.id.VerifyProgressBar);

        verifyOTP= findViewById(R.id.verifyOtp);
        verifyOTP.setOnClickListener(view -> {
            if(!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty() && !inputnumber3.getText().toString().trim().isEmpty() && !inputnumber4.getText().toString().trim().isEmpty() && !inputnumber5.getText().toString().trim().isEmpty() && !inputnumber6.getText().toString().trim().isEmpty()){
                String enterOTP=inputnumber1.getText().toString()+
                        inputnumber2.getText().toString()+
                        inputnumber3.getText().toString()+
                        inputnumber4.getText().toString()+
                        inputnumber5.getText().toString()+
                        inputnumber6.getText().toString();

                if(getBackOTP!=null){
                    progressbar.setVisibility(View.VISIBLE);
                    verifyOTP.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneauthcredential= PhoneAuthProvider.getCredential(
                            getBackOTP, enterOTP
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneauthcredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressbar.setVisibility(View.GONE);
                                    verifyOTP.setVisibility(View.VISIBLE);

                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(getApplicationContext(),complaint.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile1",no);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(Verify.this, "Enter correct OTP",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else{
                    Toast.makeText(Verify.this, "Check your internet connection",Toast.LENGTH_SHORT).show();
                }




                //Toast.makeText(Verify.this, "OTP verified",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Verify.this, "OTP verified111",Toast.LENGTH_SHORT).show();
            }
        });
        setupOTPInputs();

        TextView resendlabel=findViewById(R.id.resendOTP);

        resendlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        Verify.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getBackOTP=newbackOTP;
                                Toast.makeText(Verify.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });
    }

    private void setupOTPInputs(){
        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputnumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}