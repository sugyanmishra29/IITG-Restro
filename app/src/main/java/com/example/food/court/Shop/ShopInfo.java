package com.example.food.court.Shop;

import com.example.food.court.Restaurent.RestaurentInfo;

import java.net.URL;

public final class ShopInfo {

    //constant class to retrieve shopInfo
    public static String shopID;
    public static String shopName;
    public static String shopEmail;
    public static String shopPassword;
    public static String shopNumber;
    public static String shopAddress;
    public static String shopDescription;
    public static String shopAbout;
    public static URL shopPhotoUrl;
    public static String shopUpiId;

    public static void setShopUpiId(String shopUpiId){ShopInfo.shopUpiId=shopUpiId;}
    public static void setShopID(String userID) {
        ShopInfo.shopID = userID;
    }
    public static void setShopName(String shopName) {
        ShopInfo.shopName = shopName;
    }

    public static void setShopEmail(String shopEmail) {
        ShopInfo.shopEmail = shopEmail;
    }

    public static void setShopPassword(String shopPassword) {
        ShopInfo.shopPassword = shopPassword;
    }

    public static void setShopNumber(String shopNumber) {
        ShopInfo.shopNumber = shopNumber;
    }

    public static void setShopAddress(String shopAddress) {
        ShopInfo.shopAddress = shopAddress;
    }

    public static void setShopDescription(String shopDescription) {
        ShopInfo.shopDescription = shopDescription;
    }

    public static void setShopAbout(String shopAbout) {
        ShopInfo.shopAbout = shopAbout;
    }
    public static void setShopPhotoUrl(URL userPhotoUrl) {
        ShopInfo.shopPhotoUrl = userPhotoUrl;
    }
}
