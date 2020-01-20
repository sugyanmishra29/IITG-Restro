package com.example.food.court;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.ProfileWindows.ShopProfileActivity;
import com.example.food.court.ProfileWindows.UserProfileActivity;
import com.example.food.court.Shop.Shop;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Restaurent_homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    //for getting an intent to auto start setting for app

    /*private static final Intent[] AUTO_START_INTENTS = {
            new Intent().setComponent(new ComponentName("com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
                    Uri.parse("mobilemanager://function/entry/AutoStart"))
    };
    */

    String name,email;
    private    FirebaseUser cuser;
    private static final String TAG = "Restaurent_homepage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_homepage);

        //for battery optimisation setting for app

        /*Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "onCreate: isTgnore:"+pm.isIgnoringBatteryOptimizations(packageName));
            if (pm != null && pm.isIgnoringBatteryOptimizations(packageName))
            {
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
        }*/

        //for getting an intent to auto start setting for app

       /* for (Intent intent : AUTO_START_INTENTS)
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {

                androidx.appcompat.app.AlertDialog.Builder abuilder = new androidx.appcompat.app.AlertDialog.Builder(Restaurent_homepage.this);
                //abuilder.setTitle("Signing out");
                abuilder.setMessage("Please allow App to always run in the background,else our services can't be accessed when you are in distress");
                abuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            for (Intent intent : AUTO_START_INTENTS)
                                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                                    startActivity(intent);
                                    break;
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                abuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                abuilder.show();


                break;
            }

        */



        cuser=FirebaseAuth.getInstance().getCurrentUser();


        Log.i(TAG, "onCreate: shopid :"+cuser.getUid());
        DatabaseReference m=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("Info");
        m.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shop shop=dataSnapshot.getValue(Shop.class);
                Log.i(TAG, "onDataChange: shopname:"+shop.getShopName());

                name=shop.getShopName();
                email=shop.getShopEmail();
                TextView shopname=findViewById(R.id.name);
                TextView shopemail=findViewById(R.id.email);
                Log.i(TAG, "onDataChange: shopname: "+name);
                Log.i(TAG, "onDataChange: shopemail: "+email);
                if(shopname!=null)
                shopname.setText(name);
                if(shopemail!=null)
                shopemail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ApplicationMode.currentMode="owner";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RestOwnerHomePage()).commit();
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();*/
                drawer.closeDrawer(GravityCompat.START);
                Intent profileIntent = new Intent(Restaurent_homepage.this, ShopProfileActivity.class);
                startActivity(profileIntent);

                break;
            case R.id.nav_chat:
              //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //        new ChatFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                Intent i1 = new Intent(getApplicationContext(), ShopMenuActivity.class);
                ApplicationMode.currentMode="owner";
                startActivity(i1);
                break;
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      new ProfileFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                ApplicationMode.ordersViewer = "owner";
                Intent intent = new Intent(getApplicationContext(), OrdersTerminalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_notification:
                Intent intent2 = new Intent(getApplicationContext(),Noti_Settings.class);
                startActivity(intent2);
                break;
            case R.id.nav_share:
                drawer.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(getApplicationContext(),Customer_About_Us.class);
                startActivity(intent1);
                break;
            case R.id.nav_send:
                drawer.closeDrawer(GravityCompat.START);
                androidx.appcompat.app.AlertDialog.Builder abuilder = new AlertDialog.Builder(Restaurent_homepage.this);
                abuilder.setTitle("Signing out");
                abuilder.setMessage("Are you sure you want to sign out?");
                abuilder.setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("Token").setValue(null);

                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Toast.makeText(Restaurent_homepage.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                abuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                abuilder.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "onStart: isTgnore:"+pm.isIgnoringBatteryOptimizations(packageName));
            if (pm != null && pm.isIgnoringBatteryOptimizations(packageName))
            {
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
        }*/
    }
}
