package com.example.food.court.Menu;

import android.content.Context;

//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.food.court.Menu.Fragments.BurgerFragment;
import com.example.food.court.Menu.Fragments.DessertsFragment;
import com.example.food.court.Menu.Fragments.DrinksFragment;
import com.example.food.court.Menu.Fragments.OthersFragment;
import com.example.food.court.Menu.Fragments.PizzaFragment;

import java.util.ArrayList;

public class CategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;
    private ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private ArrayList<String> fragmentsTilte=new ArrayList<>();

    public CategoryAdapter(FragmentManager fm)
    {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    public CategoryAdapter(Context context, FragmentManager fm) {

     super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
     mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new BurgerFragment();
        else if (position == 1)
            return new PizzaFragment();
        else if (position == 2)
            return new DessertsFragment();
        else if (position == 3)
            return new DrinksFragment();
        else
            return new OthersFragment();
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {

        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "BURGERS";
        else if (position == 1)
            return "PIZZA";
        else if (position == 2)
            return "DESSERTS";
        else if (position == 3)
            return "DRINKS";
        else
            return "Others";
    }
    public void addFragment(Fragment fragment,String title)
    {
        fragmentArrayList.add(fragment);
        fragmentsTilte.add(title);
    }
}
