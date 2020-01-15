package com.example.food.court.User;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class User {
    private static final String TAG = "User";

    private String userEmail;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userAddress;
    private String userUpiId;


    public User() {

    }

    public User(String userEmail, String userPassword, String userName, String userPhone, String userAddress, URL userPhotoUrl,String userUpiId) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userUpiId=userUpiId;
    }

    public String getUserName() {
        return userName;
    }


    public String getUserEmail() {
        return userEmail;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }
    public String getUserUpiId(){return userUpiId;}

    public void setUserUpiId(String userUpiId){this.userUpiId=userUpiId;}
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public static void loadCurrentUser(final String userId) {
        DatabaseReference mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabaseReference.child(userId).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                Log.i(TAG, "onDataChange: userid : "+userId);
            currentUser.setCurrentValues(userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading user data");
            }
        });

    }

    private void setCurrentValues(String userId) {
        UserInfo.setUserID(userId);
        UserInfo.setUserName(userName);
        UserInfo.setUserAddress(userAddress);
        UserInfo.setUserEmail(userEmail);
        UserInfo.setUserPhone(userPhone);
        UserInfo.setUserPassword(userPassword);
        UserInfo.setUserUpiId(userUpiId);
    }

}
