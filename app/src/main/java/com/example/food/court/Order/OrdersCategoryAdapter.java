package com.example.food.court.Order;

import android.content.Context;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.food.court.Order.Fragments.DeliveredFragment;
import com.example.food.court.Order.Fragments.DeliveringFragment;
import com.example.food.court.Order.Fragments.PendingFragment;

public class OrdersCategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;


    public OrdersCategoryAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return new PendingFragment();
        else if (position == 1)
            return new DeliveringFragment();
        else
            return new DeliveredFragment();
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "PENDING";
        else if (position == 1)
            return "DELIVERING";
        else
            return "DELIVERED";
    }
}

