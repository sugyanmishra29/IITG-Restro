package com.example.food.court;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.food.court.Login.WelcomeScreenActivity;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Notifications.Token;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.Order.ShoppingCart.ShoppingCart;
import com.example.food.court.ProfileWindows.ShopProfileActivity;
import com.example.food.court.ProfileWindows.UserProfileActivity;
import com.example.food.court.Shop.Shop;
import com.example.food.court.Shop.ShopHomeActivity;
import com.example.food.court.Shop.ShopInfo;
import com.example.food.court.User.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.food.court.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //constants declaration
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Main Activity";
    static boolean calledAlready = false;
    static boolean refresh=false;
    //UI instances

    private ImageView gif;
    //instance declarations
    private FirebaseAuth mAuthentication;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser current_user;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private String contactapp;
    private NotificationManagerCompat notificationManager;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    private Activity mainactivity;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contactapp="+919458773422";
        super.onCreate(savedInstanceState);
        //////////////////////////////////
        setContentView(R.layout.activity_main_loading);

        //////////////////////////////////

        mainactivity=this;

        notificationManager = NotificationManagerCompat.from(this);

        Log.i(TAG, "onCreate: started");
        //enable local data storage
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        if(refresh)
            gotocorrectAvtivity();
        else
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotocorrectAvtivity();
            }
        }, 2500);
    }

    private void gotocorrectAvtivity()
    {
        refresh=true;
        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String typeing=pref.getString("Type","");
        Log.i(TAG, "onCreate: started2");
        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        mAuthentication = FirebaseAuth.getInstance();
        current_user = mAuthentication.getCurrentUser();
        Log.i(TAG, "onCreate: started3");
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        //progressDialog.show();
        // loadShop();
        Log.i(TAG, "onCreate: started4");



        //keeps user logged in
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    if(typeing.equals("R"))
                    {
                        Intent ni=new Intent(MainActivity.this,Restaurent_homepage.class);
                        ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ni.putExtra("U_ID",user.getUid());
                        startActivity(ni);
                    }
                    if(typeing.equals("U"))

                    {

                        setContentView(R.layout.activity_main);
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        drawer = findViewById(R.id.drawer_layout);
                        NavigationView navigationView = findViewById(R.id.nav_view);
                        navigationView.setNavigationItemSelectedListener(MainActivity.this);

                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainactivity, drawer, toolbar,
                                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.addDrawerListener(toggle);
                        toggle.syncState();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new userhomepage()).commit();

                        progressDialog.dismiss();

                        User.loadCurrentUser(user.getUid());
                        FirebaseUser cuser=FirebaseAuth.getInstance().getCurrentUser();


                        DatabaseReference m=FirebaseDatabase.getInstance().getReference().child("Users").child(cuser.getUid()).child("Info");
                        m.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                updateToken(FirebaseInstanceId.getInstance().getToken());
                                // sendOnChannel1();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        m.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String mname=dataSnapshot.child("userName").getValue().toString();
                                String memail=dataSnapshot.child("userEmail").getValue().toString();
                                TextView username=findViewById(R.id.name);
                                TextView useremail=findViewById(R.id.email);
                                username.setText(mname);
                                useremail.setText(memail);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                           /* Intent intent = new Intent();
                            String packageName = getPackageName();
                            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Log.i(TAG, "onCreate isTgnore:"+pm.isIgnoringBatteryOptimizations(packageName));
                                if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName))
                                {
                                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                                    startActivity(intent);
                                }
                            }*/
                    }
                    Log.i(TAG, "onAuthStateChanged: sdkmksf"+typeing+"9m");
                    Log.i(TAG, "onAuthStateChanged: "+user.getUid());

                }
                else {
                    progressDialog.hide();
                    progressDialog.dismiss();
                    Intent i2 = new Intent(MainActivity.this, Start_page.class);
                    //i2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(i2, RC_SIGN_IN);

                    //finish this activity it is nod needed now
                    finish();
                }
            }
        };

        mAuthentication.addAuthStateListener(mAuthStateListener);
    }


    private  void  updateToken(String token)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        reference.child(user.getUid()).setValue(token1);

    }

    public void sendOnChannel1() {


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.main_image)
                .setContentTitle("User")
                .setContentText("Profile Updated")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "result");

            if (resultCode == RESULT_OK) {

                if(data.getIntExtra("USER",0)==1)
                {

                }
                if(data.getIntExtra("USER",0)==2)
                {
                    Intent ni=new Intent(MainActivity.this,Restaurent_homepage.class);
                    ni.putExtra("U_ID",data.getStringExtra("USER_ID"));
                    startActivity(ni);
                }
            }

            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "result cancelled");
                finish();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //  viewMenuButton.setEnabled(true);
//        if(refresh)
//            gotocorrectAvtivity();
//        else
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gotocorrectAvtivity();
//            }
//        }, 4000);
        //mAuthentication.addAuthStateListener(mAuthStateListener);
        //loadShop();
        ApplicationMode.currentMode = "customer";
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mAuthentication.removeAuthStateListener(mAuthStateListener);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();*/
                drawer.closeDrawer(GravityCompat.START);
                Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileIntent);

                break;
            case R.id.nav_chat:
                //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //        new ChatFragment()).commit();
                ApplicationMode.currentMode = "customer";drawer.closeDrawer(GravityCompat.START);
                Intent i1 = new Intent(getApplicationContext(), ShopList.class);
                startActivity(i1);
                break;
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //      new ProfileFragment()).commit();
                ApplicationMode.ordersViewer = "customer";
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(getApplicationContext(), OrdersTerminalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                drawer.closeDrawer(GravityCompat.START);
                //Toast.makeText(this, "Sharing...", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(),Customer_About_Us.class);
                startActivity(intent1);
                break;
            case R.id.nav_notifications:
                Intent intent2 = new Intent(getApplicationContext(),Noti_Settings.class);
                startActivity(intent2);
                break;
            case R.id.nav_send:
                drawer.closeDrawer(GravityCompat.START);
                androidx.appcompat.app.AlertDialog.Builder abuilder = new AlertDialog.Builder(MainActivity.this);
                abuilder.setTitle("Signing out");
                abuilder.setMessage("Are you sure you want to sign out?");
                abuilder.setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Token").setValue(null);

                        FirebaseAuth.getInstance().signOut();

                        Intent i2 = new Intent(MainActivity.this, MainActivity.class);
                        Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                        startActivity(i2);
                        finish();
                    }
                });
                abuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                abuilder.show();
                break;
            case R.id.mCallButton:
                drawer.closeDrawer(GravityCompat.START);
                Intent intent3 = new Intent(Intent.ACTION_DIAL);
                intent3.setData(Uri.parse("tel:" + contactapp));
                startActivity(intent3);
                break;
            case R.id.shoppingCart:
                drawer.closeDrawer(GravityCompat.START);
                Intent shoppingCartIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(shoppingCartIntent);
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
        /*Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "onStart: isTgnore:"+pm.isIgnoringBatteryOptimizations(packageName));
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName))
            {

                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
        }*/
    }
}
