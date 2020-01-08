package com.example.food.court.Restaurent;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.food.court.Restaurent.Restaurent;
import com.example.food.court.Restaurent.RestaurentInfo;
import com.example.food.court.User.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

import static com.example.food.court.User.UserInfo.userName;

public class Restaurent {

   // private static final String TAG = "User";
   private static final String TAG = "Restaurent";


    private String restaurentName;
    private String restaurentEmail;
    private String restaurentPassword;
    private String restaurentNumber;
    private String restaurentAddress;
    private String restaurentDescription;
    private String restaurentAbout;

    public Restaurent() {

    }

    public Restaurent( String shopName,String shopEmail, String shopPassword, String shopPhone, String shopAddress,String shopDescription,String shopAbout, URL userPhotoUrl) {
        this.restaurentEmail = shopEmail;
        this.restaurentName = shopName;
        this.restaurentPassword = shopPassword;
        this.restaurentNumber = shopPhone;
        this.restaurentAddress = shopAddress;
        this.restaurentDescription=shopDescription;
        this.restaurentAbout=shopAbout;
    }

    public String getShopName() {
        return restaurentName;
    }


    public String getShopEmail() {
        return restaurentEmail;
    }


    public String getShopPassword() {
        return restaurentPassword;
    }

    public String getShopNumber() {
        return restaurentNumber;
    }

    public String getShopAddress() {
        return restaurentAddress;
    }

    public String getShopDescription(){return  restaurentDescription;}

    public String getShopAbout(){return restaurentAbout;}

    public void setShopAddress(String shopAddress) {
        this.restaurentAddress = shopAddress;
    }

    public void setShopEmail(String shopEmail) {
        this.restaurentEmail = shopEmail;
    }

    public void setShopName(String shopName) {
        this.restaurentName = shopName;
    }

    public void setShopPassword(String shopPassword) {
        this.restaurentPassword = shopPassword;
    }

    public void setShopNumber(String shopPhone) {
        this.restaurentNumber = shopPhone;
    }

    public void setShopDescription(String shopDescription){
        this.restaurentDescription=shopDescription;
    }
    public  void setShopAbout(String userAbout){
        this.restaurentAbout=userAbout;
    }


   /* public static void loadCurrentUser(final String userId) {
        DatabaseReference mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Restaurents");
        mUserDatabaseReference.child(userId).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurent currentUser = dataSnapshot.getValue(Restaurent.class);
                currentUser.setCurrentValues(userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading user data");
            }
        });

    }

    private void setCurrentValues(String userId) {
        RestaurentInfo.setresID(userId);
        RestaurentInfo.setresName(restaurentName);
        RestaurentInfo.setresAddress(restaurentAddress);
        RestaurentInfo.setresEmail(restaurentEmail);
        RestaurentInfo.setresPhone(restaurentPhone);
        RestaurentInfo.setresPassword(restaurentPassword);
        RestaurentInfo.setresDesc(restaurentDescription);
        RestaurentInfo.setresAbout(restaurentAbout);
    }
*/

}
