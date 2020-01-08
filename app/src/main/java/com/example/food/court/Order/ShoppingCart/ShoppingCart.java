package com.example.food.court.Order.ShoppingCart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.design.widget.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.ApplicationMode;
import com.example.food.court.Menu.MenuItems.ItemAdapter;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Order.OrderItem.Order;
import com.example.food.court.Order.OrderItem.OrdersAdapter;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.R;
import com.example.food.court.Shop.Shop;
import com.example.food.court.ShopList;
import com.example.food.court.User.UserInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {
    public static final String TAG = "shoppingCart";


    private DatabaseReference readReference;
    private DatabaseReference writeReference1;
    private DatabaseReference writeReference2;
    private ChildEventListener mChildEventListener;
    private Button orderButton;
    private TextView totalPriceView;
    private View labelView;
    private ArrayList<ShoppingCartItem> allItems;
    private int totalPrice;
    private FloatingActionButton floatingButton;
public static Context context;
    private ItemAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<ShoppingCartItem> items = new ArrayList<>();
    Context applicationContext;
    Activity a;
    View emptyView;

    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";

    private FirebaseUser cuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("My Cart");
        context=getApplicationContext();
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        pref=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String ShopID=pref.getString("SHOPID","");
        // creates a pending reference to put the new Order in.
        writeReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(cuser.getUid()).child("/orders/pending");
        writeReference2 = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(ShopID).child("/orders/pending");

        readReference = FirebaseDatabase.getInstance().getReference("Users/" + UserInfo.userID + "/shoppingCart");
        orderButton = findViewById(R.id.finalOrderButton);
       // listView = findViewById(R.id.sItemsList);
        totalPriceView = findViewById(R.id.soTotalPrice);
        labelView = findViewById(R.id.sLabelView);
        floatingButton = findViewById(R.id.addButton);

        labelView.setVisibility(View.GONE);
        floatingButton.setVisibility(View.VISIBLE);
        emptyView = findViewById(R.id.sEmptyView);
        allItems = new ArrayList<ShoppingCartItem>();
     //   listView.setEmptyView(emptyView);
        orderButton.setVisibility(View.GONE);
        orderButton.setBackgroundColor(getResources().getColor(R.color.grey));

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opens menu
                ApplicationMode.currentMode = "customer";
                Intent i1 = new Intent(getApplicationContext(), ShopList.class);
                startActivity(i1);
                finish();
            }
        });
        applicationContext = OrdersTerminalActivity.getContextOfApplication();
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));

        //creating an empty list view to set up the adapter
        //later on the onChildAdded() method in attachDatabaseReadListener() keeps updating our adapter
        /*List<ShoppingCartItem> items = new ArrayList<>();
        adapter = new ShoppingCartAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);
*/
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationMode.checkConnectivity(ShoppingCart.this)) {
                    // only allow order if internet is available

                    final Order order = new Order(allItems, UserInfo.userID,ShopID, String.valueOf(totalPrice));
                    //writeReference2.push().setValue(order);
                    String key=writeReference1.push().getKey();
                    writeReference2.child(key).setValue(order);
                    writeReference1.child(key).setValue(order, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                readReference.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {

                                            // clears view
                                            items.clear();
                                            labelView.setVisibility(View.GONE);

                                        }
                                    }
                                });

                                Toast.makeText(getApplicationContext(), "Your Order Has been placed", Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void attachDatabaseReadListener() {
        readReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             /*   // if a new child is added UI is changed to reflect ordering
                labelView.setVisibility(View.VISIBLE);
                orderButton.setVisibility(View.VISIBLE);
                orderButton.setEnabled(true);
                Log.i(TAG, "onChildAdded: itemkey :"+dataSnapshot.getKey());
                orderButton.setBackgroundColor(getResources().getColor(R.color.button_pink));
                Log.i(TAG, "onChildAdded: itemprice :"+dataSnapshot.child("price").getValue());
                Log.i(TAG, "onChildAdded: itemquantity"+dataSnapshot.child("quantity").getValue());
                Log.i(TAG, "onChildAdded: s:"+s);
                ShoppingCartItem item = dataSnapshot.getValue(ShoppingCartItem.class);
                Log.i(TAG, "onChildAdded: itemquantity :"+item.getQuantity());
                Log.i(TAG, "onChildAdded: itemprice :"+item.getPrice());
                // keeps updating total price on each addition of item
                totalPrice += Integer.parseInt(item.getQuantity()) * Integer.parseInt(item.getPrice());
                totalPriceView.setText("Total= " + String.valueOf(totalPrice) + "Rs/-");
                // adds new item to ShoppingCartItem array
                allItems.add(item);
                item.itemDatabaseKey = dataSnapshot.getKey();
                adapter.add(item);*/
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // clears and refreshed the UI, and if Items still remain OnChildAdded is called for appropriate modification

                allItems.clear();
                totalPrice = 0;
                detachDatabaseReadListener();
                attachDatabaseReadListener();
                labelView.setVisibility(View.GONE);
                orderButton.setEnabled(false);
                if(allItems.size()==0)
                {
                    emptyView.setVisibility(View.VISIBLE);
                    floatingButton.setVisibility(View.VISIBLE);

                }

                orderButton.setBackgroundColor(getResources().getColor(R.color.grey));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        readReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readReference.keepSynced(true);

                allItems.clear();
                totalPrice=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.exists()) {
                        labelView.setVisibility(View.VISIBLE);
                        orderButton.setVisibility(View.VISIBLE);
                        orderButton.setEnabled(true);
                        Log.i(TAG, "onChildAdded: itemkey :" + dataSnapshot1.getKey());
                        orderButton.setBackgroundColor(getResources().getColor(R.color.button_pink));
                        Log.i(TAG, "onChildAdded: itemprice :" + dataSnapshot1.child("price").getValue());
                        Log.i(TAG, "onChildAdded: itemquantity" + dataSnapshot1.child("quantity").getValue());
                        // Log.i(TAG, "onChildAdded: s:"+s);
                        ShoppingCartItem item = dataSnapshot1.getValue(ShoppingCartItem.class);
                        Log.i(TAG, "onChildAdded: itemquantity :" + item.getQuantity());
                        Log.i(TAG, "onChildAdded: itemprice :" + item.getPrice());
                        // keeps updating total price on each addition of item
                        totalPrice += Integer.parseInt(item.getQuantity()) * Integer.parseInt(item.getPrice());
                        totalPriceView.setText("Total= " + String.valueOf(totalPrice) + "Rs/-");
                        // adds new item to ShoppingCartItem array
                        allItems.add(item);
                        if(allItems.size()==0)
                        {
                            emptyView.setVisibility(View.VISIBLE);
                            floatingButton.setVisibility(View.VISIBLE);

                        }else
                        {
                            emptyView.setVisibility(View.GONE);
                        }
                        item.itemDatabaseKey = dataSnapshot1.getKey();
                        ShoppingCartAdapter adapter=new ShoppingCartAdapter(allItems,ShoppingCart.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            readReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {

        allItems.clear();
        detachDatabaseReadListener();
        super.onPause();
    }

    @Override
    public void onStart() {
        attachDatabaseReadListener();
        super.onStart();
    }
    public static Context getContextOfApplication()
    {
        return context;
    }
}
