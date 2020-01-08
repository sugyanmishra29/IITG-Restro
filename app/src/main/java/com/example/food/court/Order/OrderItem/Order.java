package com.example.food.court.Order.OrderItem;

import com.example.food.court.Order.ShoppingCart.ShoppingCartItem;

import java.util.List;

public class Order {
    private List<ShoppingCartItem> allItems;
    private String userId;
    private String totalPrice;
    public String orderID;
    private String shopID;

    public Order() {
    }

    public Order(List<ShoppingCartItem> allItems, String userId,String shopID, String totalPrice) {
        this.allItems = allItems;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.shopID=shopID;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShoppingCartItem> getAllItems() {
        return allItems;
    }

    public String getUserId() {
        return userId;
    }

public String getShopID(){return shopID;}

    public void setShopID(String shopID){this.shopID=shopID;}

    public void setAllItems(List<ShoppingCartItem> allItems) {
        this.allItems = allItems;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
