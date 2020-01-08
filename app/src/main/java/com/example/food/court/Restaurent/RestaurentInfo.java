package com.example.food.court.Restaurent;

import com.example.food.court.Restaurent.Restaurent;

import java.net.URL;

public class RestaurentInfo {

    // constant class to retrieve userInfo
    public static String resID;

    public static String resName;
    public static String resEmail;
    public static String resPassword;
    public static String resPhone;
    public static String resAddress;
    public static URL resPhotoUrl;
    public static String resDescription;
    public static String resAbout;

    public static void setresID(String userID) {
        RestaurentInfo.resID = userID;
    }

    public static void setresEmail(String userEmail) {
        RestaurentInfo.resEmail = userEmail;
    }

    public static void setresName(String userName) {
        RestaurentInfo.resName = userName;
    }

    public static void setresPassword(String userPassword) {
        RestaurentInfo.resPassword = userPassword;
    }

    public static void setresPhone(String userPhone) {
        RestaurentInfo.resPhone = userPhone;
    }

    public static void setresAddress(String userAddress) {
        RestaurentInfo.resAddress = userAddress;
    }
    public static void setresDesc(String userDescription){RestaurentInfo.resDescription=userDescription;}

    public static void setresAbout(String userAbout){RestaurentInfo.resAbout=userAbout;}

    public static void setresPhotoUrl(URL userPhotoUrl) {
        RestaurentInfo.resPhotoUrl = userPhotoUrl;
    }

}
