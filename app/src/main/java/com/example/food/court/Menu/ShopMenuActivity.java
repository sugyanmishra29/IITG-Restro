package com.example.food.court.Menu;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.food.court.ApplicationMode;
import com.example.food.court.Menu.Fragments.BurgerFragment;
import com.example.food.court.Menu.Fragments.DessertsFragment;
import com.example.food.court.Menu.Fragments.DrinksFragment;
import com.example.food.court.Menu.Fragments.OthersFragment;
import com.example.food.court.Menu.Fragments.PizzaFragment;
import com.example.food.court.Menu.MenuItems.itemEditor;
import com.example.food.court.Order.ShoppingCart.ShoppingCart;
import com.example.food.court.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;



public class ShopMenuActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu);
        setTitle("Menu");
        contextOfApplication = getApplicationContext();
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()

        // setUpViewPager(viewPager);
       tabLayout.setupWithViewPager(viewPager);
       /* viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager));*/

        FloatingActionButton button = findViewById(R.id.floating_button);

        if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
            // show newItem button to user
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), itemEditor.class);
                    startActivity(i);
                }
            });
        } else {
            // hide newItem button from user
            button = findViewById(R.id.floating_button);
            button.hide();
        }


    }

   /* public  void setUpViewPager(ViewPager viewPager)
    {
        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
      // add fragmrnts to list
        adapter.addFragment(new BurgerFragment(),"Burger");
        adapter.addFragment(new DessertsFragment(),"Desserts");
        adapter.addFragment(new DrinksFragment(),"Drinks");
        adapter.addFragment(new PizzaFragment(),"Pizza");

        adapter.addFragment(new OthersFragment(),"Others");
       // for(int i=1;i<=adapter.getCount();i++)
         //   tabLayout.addTab(tabLayout.newTab().setText(adapter.getPageTitle(i)));
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        MenuItem mi = menu.findItem(R.id.goToCart);
        if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
            mi.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goToCart:
                Intent profileIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(profileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
