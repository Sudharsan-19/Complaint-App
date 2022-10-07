package com.example.complaintapp;

import android.os.Parcel;
import android.os.Parcelable;

public class dbHelper implements Parcelable{

    private String susName;
    private String susApp;
    private String susCategory;
    private String susPlace;
    private String susState;
    private String susDate;
    private String susTime;
    private String susDetails;
    private String userNumber;
    private String userLocation;
    private String susLocation;


    protected dbHelper(Parcel in) {
        susName = in.readString();
        susApp = in.readString();
        susCategory = in.readString();
        susPlace = in.readString();
        susState = in.readString();
        susDate = in.readString();
        susTime = in.readString();
        susDetails = in.readString();
        userNumber=in.readString();
        userLocation = in.readString();
        susLocation = in.readString();
    }

    public static final Creator<dbHelper> CREATOR = new Creator<dbHelper>() {
        @Override
        public dbHelper createFromParcel(Parcel in) {
            return new dbHelper(in);
        }

        @Override
        public dbHelper[] newArray(int size) {
            return new dbHelper[size];
        }
    };

    public String getSusName() {

        return susName;
    }

    public void setSusName(String susName) {

        this.susName = susName;
    }

    public String getSusApp() {
        return susApp;
    }

    public void setSusApp(String susApp) {
        this.susApp = susApp;
    }

    public String getSusCategory() {
        return susCategory;
    }

    public void setSusCategory(String susCategory) {
        this.susCategory = susCategory;
    }

    public String getSusPlace() {
        return susPlace;
    }

    public void setSusPlace(String susPlace) {
        this.susPlace = susPlace;
    }

    public String getSusState() {
        return susState;
    }

    public void setSusState(String susState) {
        this.susState = susState;
    }

    public String getSusDate() {
        return susDate;
    }

    public void setSusDate(String susDate) {
        this.susDate = susDate;
    }

    public String getSusTime() {
        return susTime;
    }

    public void setSusTime(String susTime) {
        this.susTime = susTime;
    }

    public String getSusDetails() {
        return susDetails;
    }

    public void setSusDetails(String susDetails) {
        this.susDetails = susDetails;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getSusLocation() {
        return susLocation;
    }

    public void setSusLocation(String susLocation) {
        this.susLocation = susLocation;
    }

    public dbHelper(String susName, String susApp, String susCategory, String susPlace, String susState, String susDate, String susTime, String susDetails,String userNumber, String userLocation, String susLocation) {
        this.susName = susName;
        this.susApp = susApp;
        this.susCategory = susCategory;
        this.susPlace = susPlace;
        this.susState = susState;
        this.susDate = susDate;
        this.susTime = susTime;
        this.susDetails = susDetails;
        this.userNumber=userNumber;
        this.userLocation = userLocation;
        this.susLocation = susLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(susName);
        parcel.writeString(susApp);
        parcel.writeString(susCategory);
        parcel.writeString(susPlace);
        parcel.writeString(susState);
        parcel.writeString(susDate);
        parcel.writeString(susTime);
        parcel.writeString(susDetails);
        parcel.writeString(userNumber);
        parcel.writeString(userLocation);
        parcel.writeString(susLocation);
    }
}
