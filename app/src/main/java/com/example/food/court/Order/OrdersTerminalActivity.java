package com.example.food.court.Order;

//import android.support.design.widget.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.ViewPager;
import androidx.viewpager.widget.ViewPager;

//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;

import com.example.food.court.R;
import com.google.android.material.tabs.TabLayout;

public class OrdersTerminalActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_orders);
        setTitle("Orders");
        contextOfApplication = getApplicationContext();
        ViewPager viewPager = findViewById(R.id.soViewpager);

        // Create an adapter that knows which fragment should be shown on each page
        OrdersCategoryAdapter adapter = new OrdersCategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.soTabs);

        tabLayout.setupWithViewPager(viewPager);
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
