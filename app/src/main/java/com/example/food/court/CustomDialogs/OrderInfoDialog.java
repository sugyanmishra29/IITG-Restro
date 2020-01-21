package com.example.food.court.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.ApplicationMode;
import com.example.food.court.Order.DeclineOrder;
import com.example.food.court.Notifications.Api;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.Order.ShoppingCart.ShoppingCartAdapter;
import com.example.food.court.Order.ShoppingCart.ShoppingCartItem;
import com.example.food.court.R;
import com.example.food.court.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderInfoDialog extends Dialog {
    public static final String TAG = "orderInfoDialog";

    private String positiveToastMessage;
    private String negativeToastMessage;
    private Activity c;
    private String orderId;
    private String userId;
    private DatabaseReference fromReference;
    private DatabaseReference fromReferenceuser;
    private DatabaseReference toReferenceuser;
    private DatabaseReference fromReferenceshop;
    private DatabaseReference toReferenceshop;
    private DatabaseReference mReference;
    private ValueEventListener mChildEventListener;
    private TextView userNameView, userPhoneView, userAddressView;
    private ListView listView;
    private Button accept, decline;
    private ShoppingCartAdapter adapter;
    private RecyclerView recyclerView;

    ArrayList<ShoppingCartItem> items = new ArrayList<>();
    Context applicationContext;
    Activity a;
    View emptyView;
    FirebaseUser cuser;

    String shopid;
    String title;
    String body;
    String Token;
    Context mcontext;
    String userName,userUpiId,note,totalprice;
    final int UPI_PAYMENT = 0;

    public OrderInfoDialog(Activity a, String orderId, String userId,String shopid,String totalprice,Context mcontext) {
        super(a);
        this.c = a;
        this.orderId = orderId;
        this.userId = userId;
        this.shopid=shopid;
        this.totalprice=totalprice;
        this.mcontext=mcontext;
        Log.i(TAG, "OrderInfoDialog: orderid: "+orderId);
        Log.i(TAG, "OrderInfoDialog: userid : "+userId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_info_window);
        title="User";
        userNameView = findViewById(R.id.oi_user_name);
        userAddressView = findViewById(R.id.oi_user_address);
        userPhoneView = findViewById(R.id.oi_user_number);
        //listView = findViewById(R.id.oiItems_list);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);


        if (!ApplicationMode.checkConnectivity(getContext())) {
            // no internet, disallow writes
            accept.setEnabled(false);
            decline.setEnabled(false);
            accept.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
            decline.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();

        }



        applicationContext =OrdersTerminalActivity.getContextOfApplication() ;
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        Log.i(TAG, ApplicationMode.orderStatus);
        Log.i(TAG, "onCreate: "+ApplicationMode.ordersViewer);
        switch (ApplicationMode.orderStatus) {
            case "pending":

                if (ApplicationMode.ordersViewer == "owner") {
                    accept.setText("ACCEPT ORDER");
                    decline.setText("DECLINE ORDER");

                    fromReference=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/orders/pending/"+orderId);
                    fromReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/pending/" + orderId);
                    toReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("/orders/delivering");
                    fromReferenceshop = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("orders/pending/" + orderId);
                    toReferenceshop = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/orders/delivering");
                    positiveToastMessage = "Order Accepted";
                    negativeToastMessage = "Order Declined";
                } else {
                    fromReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/pending/"+orderId);
                    fromReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/pending/" + orderId);
                    fromReferenceshop = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(shopid).child("orders/pending/" + orderId);
                    accept.setVisibility(View.GONE);
                    decline.setText("Cancel Order");
                    decline.setVisibility(View.GONE);
                    negativeToastMessage = "Order Cancelled";
                }

                break;
            case "delivering":

                if (ApplicationMode.ordersViewer == "owner") {
                    fromReference=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/orders/delivering/"+orderId);
                    fromReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/delivering/" + orderId);
                    toReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("/orders/delivered");
                    fromReferenceshop = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("orders/delivering/" + orderId);
                    toReferenceshop = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/orders/delivered");
                    accept.setText("DELIVER ORDER");
                    decline.setText("CANCEL ORDER");
                    positiveToastMessage = "Order delivered";
                    negativeToastMessage = "Order Cancelled";
                } else {
                    fromReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/delivering/"+orderId);
                    accept.setVisibility(View.GONE);
                    decline.setVisibility(View.GONE);
                }

                break;
            case "delivered":



                if(ApplicationMode.ordersViewer == "owner")
                {
                    fromReference=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/orders/delivered/"+orderId);
                    fromReferenceshop=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("orders/delivered/" + orderId);
                }
                else
                {
                    fromReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/delivered/"+orderId);
                    fromReferenceuser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders/delivered/" + orderId);
                }
                accept.setVisibility(View.GONE);
                decline.setText("CLEAR ITEM");
                negativeToastMessage = "Order Cleared";

                break;

        }
        mReference = FirebaseDatabase.getInstance().getReference("/Users/" + userId+"/Info");
        loadAndSetCurrentUser(userId);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("customer")) {
                    moveFirebaseRecord();

                } else {
                    Toast.makeText(getContext(), "You aren't authorized for this", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("customer")) {
                    if(ApplicationMode.orderStatus.equals("delivered"))
                    {
                        if (fromReferenceshop != null) {
                            fromReferenceshop.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(getContext(), negativeToastMessage, Toast.LENGTH_SHORT).show();
                                        dismiss();

                                    }
                                }
                            });
                        }
                        if (fromReferenceuser != null) {
                            fromReferenceuser.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(getContext(), negativeToastMessage, Toast.LENGTH_SHORT).show();
                                        dismiss();

                                    }
                                }
                            });
                        }
                    }
                    if(ApplicationMode.orderStatus.equals("pending") || ApplicationMode.orderStatus.equals("delivering"))
                    {


                            Intent i=new Intent(applicationContext, DeclineOrder.class);
                            i.putExtra("shopid",shopid);
                            i.putExtra("userid",userId);
                            i.putExtra("orderid",orderId);
                            i.putExtra("note",note);
                            i.putExtra("totalprice",totalprice);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(i);
                            dismiss();

                    }
                }
                else
                    {
                    Toast.makeText(getContext(), "You aren't authorized for this", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    public void loadAndSetCurrentUser(String userId) {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                Log.i(TAG, mReference.toString());
                userNameView.setText(currentUser.getUserName());
                userAddressView.setText(currentUser.getUserAddress());
                userPhoneView.setText(currentUser.getUserPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading user data");
            }
        });

    }


    public void moveFirebaseRecord() {
        Log.i(TAG, fromReferenceuser.toString() + "   " + toReferenceuser.toString());
        fromReferenceuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toReferenceuser.child( orderId).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        fromReferenceuser.removeValue();
                        Toast.makeText(getContext(), positiveToastMessage, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, databaseError.toString());
            }
        });
        Log.i(TAG, fromReferenceshop.toString() + "   " + toReferenceshop.toString());
        fromReferenceshop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toReferenceshop.child( orderId).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        fromReferenceshop.removeValue();
                       // Toast.makeText(getContext(), positiveToastMessage, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, databaseError.toString());
            }
        });
        if(ApplicationMode.orderStatus.equals("pending"))
            body="Your Order is Accepted.";
        else
            body="Your Order is Delivered.";
        sendNotification(userId,title,body);
    }
    private  void sendNotification(String userId,String title,String body){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Token");
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

            Call<ResponseBody> call = api.sendNotification(Token,title, body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Toast.makeText(mcontext, response.body().string(), Toast.LENGTH_LONG).show();
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


    private void attachDatabaseReadListener() {


        Log.i(TAG, "attachDatabaseReadListener: orderid :"+orderId);
        fromReference.child("allItems").addValueEventListener(mChildEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange:item id :"+dataSnapshot1.getKey());
                    //items.add(dataSnapshot1.getKey());
                    ShoppingCartItem item = dataSnapshot1.getValue(ShoppingCartItem.class);
                    items.add(item);
                }

                Log.i(TAG, "onCreate: items size :"+items.size());
                ShoppingCartAdapter adapter=new ShoppingCartAdapter(items,applicationContext);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            fromReference.child("allItems").removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    @Override
    protected void onStop() {

        detachDatabaseReadListener();
        super.onStop();
    }

    @Override
    public void onStart() {
        attachDatabaseReadListener();
        super.onStart();
    }
}
