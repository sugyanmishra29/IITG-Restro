package com.example.food.court.Shop;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.food.court.Restaurent.Restaurent;
//import com.example.saif.nustana.Restaurent.ShopInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class Shop {
    private String shopName;
    private String shopEmail;
    private String shopPassword;
    private String shopNumber;
    private String shopAddress;
    private String shopDescription;
    private String shopAbout;
    private String shopUpiId;
    private static final String TAG = "Shop";
    public Shop() {
        //empty constructor for firebase to map data values into shop object
    }

    public Shop(String shopName,String shopNumber, String shopAddress, String shopDescription, String shopAbout) {
        this.shopName = shopName;
        this.shopEmail="";
        this.shopPassword="";
        this.shopNumber = shopNumber;
        this.shopAddress = shopAddress;
        this.shopDescription = shopDescription;
        this.shopAbout = shopAbout;
    }
    public Shop(String shopName, String shopEmail, String shopPassword, String shopNumber, String shopAddress, String shopDescription, String shopAbout, URL userPhotoUrl,String shopUpiId) {
        this.shopName = shopName;
        this.shopEmail=shopEmail;
        this.shopPassword=shopPassword;
        this.shopNumber = shopNumber;
        this.shopAddress = shopAddress;
        this.shopDescription = shopDescription;
        this.shopAbout = shopAbout;
        this.shopUpiId=shopUpiId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public String getShopAbout() {
        return shopAbout;
    }

    public String getShopEmail(){return shopEmail;}
    public String getShopPassword(){return shopPassword;}
    public String getShopUpiId(){return shopUpiId;}

    public void setShopUpiId(String shopUpiId){this.shopUpiId=shopUpiId;}
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }
    public void setShopPassword(String shopPassword) {
        this.shopPassword = shopPassword;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public void setShopAbout(String shopAbout) {
        this.shopAbout = shopAbout;
    }

    public static void loadCurrentUser(final String userId) {
        DatabaseReference mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Restaurents");
        mUserDatabaseReference.child(userId).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shop currentUser = dataSnapshot.getValue(Shop.class);
                currentUser.setCurrentValues(userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading user data");
            }
        });

    }

    private void setCurrentValues(String userId) {
        ShopInfo.setShopID(userId);
        ShopInfo.setShopName(shopName);
        ShopInfo.setShopAddress(shopAddress);
        ShopInfo.setShopEmail(shopEmail);
        ShopInfo.setShopNumber(shopNumber);
        ShopInfo.setShopPassword(shopPassword);
        ShopInfo.setShopDescription(shopDescription);
        ShopInfo.setShopAbout(shopAbout);
        ShopInfo.setShopUpiId(shopUpiId);
    }
}
