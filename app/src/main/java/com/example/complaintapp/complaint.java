package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.service.vr.VrListenerService;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.complaintapp.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class complaint extends AppCompatActivity implements OnMapReadyCallback {
    String[] items;
    String[] items1={"Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana",
    "Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram",
    "Nagaland","Odisha","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttarakhand","Uttar Pradesh","West Bengal",
    "Andaman and Nicobar Islands","Chandigarh","Dadra and Nagar Haveli and Daman & Diu","The Government of NCT of Delhi",
    "Jammu & Kashmir","Ladakh","Lakshadweep","Puducherry"};
    String Slocation,userNumber;
    TextInputEditText Sname,Sapp,Splace,Sdetails,etdate, ettime, map;
    AutoCompleteTextView autoCompleteTextView,Sstate;
    Button zoomIN,zoomOUT,Ubtn,submitBtn,Vbtn;
    ArrayAdapter<String> adapterItems;
    int tpHour, tpMinute;
    DatePickerDialog.OnDateSetListener setListener;
    GoogleMap gMap;
    RadioGroup RG;
    LinearLayout Uimg;
    ActivityMainBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    TextView Ufilename;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_complaint);
        etdate = findViewById(R.id.TIETsdate);
        Sname=findViewById(R.id.TIETsname);
        Sapp=findViewById(R.id.TIETsapp);
        Sstate=findViewById(R.id.autoCompleteTV1);
        Splace=findViewById(R.id.TIETsplace);
        Sdetails=findViewById(R.id.TIETsdetails);
        ettime = findViewById(R.id.TIETstime);
        RG=findViewById(R.id.RG);
        Uimg= findViewById(R.id.Uimg);
        Ubtn= findViewById(R.id.Ubtn);
        submitBtn=findViewById(R.id.submitBtn);


       // String no2 = getIntent().getExtras().getString("mobile1");
       // Log.i("numbeeer","numbeeer "+no2);
       //Toast.makeText(complaint.this, "Your UserLocatieeeeon class "+HomeActivity.getValue(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(complaint.this, "Your number class "+no2, Toast.LENGTH_SHORT).show();

        autoCompleteTextView = findViewById(R.id.autoCompleteTV);

        zoomIN=findViewById(R.id.zoomIN);
        zoomOUT=findViewById(R.id.zoomOUT);
        Ufilename=findViewById(R.id.Ufilename);

        items= getResources().getStringArray(R.array.items12);
        adapterItems = new ArrayAdapter<String>(this, R.layout.select_items,items);



        autoCompleteTextView.setAdapter(adapterItems);

        adapterItems = new ArrayAdapter<String>(this, R.layout.select_items, items1);
        Sstate.setAdapter(adapterItems);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePD = new DatePickerDialog(
                        complaint.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        etdate.setText(date);
                    }
                }, year, month, day);
                datePD.show();
            }
        });

        ettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpDialog = new TimePickerDialog(
                        complaint.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                tpHour = hourOfDay;
                                tpMinute = minute;
                                String time = tpHour + ":" + tpMinute;
                                SimpleDateFormat format24 = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = format24.parse(time);
                                    SimpleDateFormat format21 = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    ettime.setText(format21.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );
                tpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpDialog.updateTime(tpHour, tpMinute);
                tpDialog.show();
            }
        });
        Ubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String susName=Sname.getText().toString();
                String susApp=Sapp.getText().toString();
                String susCategory=autoCompleteTextView.getText().toString();
                String susPlace=Splace.getText().toString();
                String susState=Sstate.getText().toString();
                String susDate=etdate.getText().toString();
                String susTime=ettime.getText().toString();
                String susDetails=Sdetails.getText().toString();
                String userNumber= Verify.getValue().toString();
                Log.i("numbeeer","numbeeer "+userNumber);
                String userLocation=HomeActivity.getValue().toString();
                String susLocation=Slocation.toString();

                uploadFile();

                dbHelper dbhelper=new dbHelper(susName,susApp,susCategory,susPlace,susState,susDate,susTime,susDetails,userNumber,userLocation,susLocation);
                firebaseDatabase=FirebaseDatabase.getInstance("https://complaintapp-341514-default-rtdb.asia-southeast1.firebasedatabase.app");
                databaseReference=firebaseDatabase.getReference("Complaints").child("State: "+susState).child("District: "+susPlace).child("UserNumber: "+userNumber).push();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.setValue(dbhelper);
                        Toast.makeText(complaint.this, "Added to db successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(complaint.this,Completed.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(complaint.this,"Error in "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void selectImage() {
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip","image/*","audio/*","video/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"),100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            Ufilename.setText(displayName(imageUri));
        }
    }
    private String displayName(Uri uri) {

        Cursor mCursor =
                getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        mCursor.moveToFirst();
        String filename = mCursor.getString(indexedname);
        mCursor.close();
        return filename;
    }
    private void uploadFile() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(complaint.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(complaint.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        //gMap.setMapType();
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + ":" + latLng.longitude);
                Slocation=("https://maps.google.com/?q="+latLng.latitude+","+latLng.longitude);
                Log.i("Suspect location link","Suspect lat and long"+ String.valueOf(Slocation));
                gMap.clear();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                gMap.addMarker(markerOptions);
            }

        });
        zoomIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoomOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        boolean checked=((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.TIETsradio:
                if(checked){
                    Toast.makeText(complaint.this, "Open gal", Toast.LENGTH_SHORT).show();
                    Uimg.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.TIETsradio1:
                if(checked) {
                    Toast.makeText(complaint.this, "nothing", Toast.LENGTH_SHORT).show();
                    Uimg.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}