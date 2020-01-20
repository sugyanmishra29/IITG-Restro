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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.ApplicationMode;
import com.example.food.court.MainActivity;
import com.example.food.court.Menu.MenuItems.ItemAdapter;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Notifications.Api;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private String Token;
    private FloatingActionButton floatingButton;
public static Context context;
    private ItemAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<ShoppingCartItem> items = new ArrayList<>();
    Context applicationContext;
    Activity a;
    View emptyView;

    String ShopID;
    String shopName,shopUpiId,note;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    final int UPI_PAYMENT = 0;
    private FirebaseUser cuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("My Cart");
        context=getApplicationContext();
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        pref=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        editor=pref.edit();
        ShopID=pref.getString("SHOPID","");
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
                    sendNotification(ShopID);

                    FirebaseDatabase.getInstance().getReference().child("Restaurents").child(ShopID).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                shopName=dataSnapshot.child("shopName").getValue(String.class);
                                shopUpiId=dataSnapshot.child("shopUpiId").getValue(String.class);
                                note="Ordering from this shop.";
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if(shopName!=null && shopUpiId!=null && note!=null)
                    {
                        payUsingUpi(shopName,shopUpiId,note,String.valueOf(totalPrice));
                        items=allItems;
                    }
                   /* final Order order = new Order(allItems, UserInfo.userID,ShopID, String.valueOf(totalPrice));
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

                    */

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private  void sendNotification(String shopid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(shopid).child("Token");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Token=dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(Token!=null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://foudserver.firebaseapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<ResponseBody> call = api.sendNotification(Token," Restaurents", "Order Received .");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Toast.makeText(ShoppingCart.this, response.body().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getApplicationContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getApplicationContext())) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Log.i(TAG, "upiPaymentDataOperation: allitems: "+allItems.size());
                Log.i(TAG, "upiPaymentDataOperation: items: "+items.size());
                final Order order = new Order(items, UserInfo.userID,ShopID, String.valueOf(totalPrice));
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
                                        editor.putBoolean("Cart",true).apply();
                                        labelView.setVisibility(View.GONE);

                                    }
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Your Order Has been placed", Toast.LENGTH_SHORT).show();
                        } else {
                        }
                    }
                });
                //sendNotification(ShopID);

               // Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getApplicationContext(), "You need to pay first.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(getApplicationContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(getApplicationContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
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
                    editor.putBoolean("Cart",true).apply();

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
                            editor.putBoolean("Cart",true).apply();

                        }else
                        {
                            emptyView.setVisibility(View.GONE);
                        }
                        item.itemDatabaseKey = dataSnapshot1.getKey();
                        ShoppingCartAdapter adapter=new ShoppingCartAdapter(allItems,ShoppingCart.this);
                        recyclerView.setAdapter(adapter);
                    }
                    Log.i(TAG, "upiPaymentDataOperation: allitems: "+allItems.size());
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
