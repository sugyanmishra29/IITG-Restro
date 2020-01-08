package com.example.food.court.Order.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.food.court.ApplicationMode;
import com.example.food.court.CustomDialogs.OrderInfoDialog;
import com.example.food.court.Menu.MenuItems.ItemAdapter;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Order.OrderItem.Order;
import com.example.food.court.Order.OrderItem.OrdersAdapter;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.R;
import com.example.food.court.User.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PendingFragment extends Fragment {
    private ListView listView;
    private OrdersAdapter ordersAdapter;
    private ChildEventListener mChildEventListener;
    private Query mReference;
    private static final String TAG = "PendingFragment";
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    ArrayList<Order> items = new ArrayList<>();
    Context applicationContext;
    Activity a;
    View emptyView;
    
    private FirebaseUser cuser;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";

    public PendingFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.list_with_empty_view, container, false);
        applicationContext = OrdersTerminalActivity.getContextOfApplication();
        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
        a=getActivity();
        emptyView=rootView.findViewById(R.id.empty_view);
        /*List<Order> items = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(getActivity(), items);
        listView.setAdapter(ordersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApplicationMode.orderStatus = "pending";
                TextView tv = view.findViewById(R.id.soOrderID);
                TextView tv1 = view.findViewById(R.id.soUserID);
                // gives orderId and userId to dialog to allow it to retrieve it from database
                OrderInfoDialog od = new OrderInfoDialog(getActivity(), tv.getText().toString(), tv1.getText().toString());
                od.show();
            }
        });
        
         */
        return rootView;
    }

    private void attachDatabaseReadListener() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                mReference.keepSynced(true);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange:item id :"+dataSnapshot1.getKey());
                    //items.add(dataSnapshot1.getKey());
                    Order item = dataSnapshot1.getValue(Order.class);
                    item.orderID=dataSnapshot1.getKey();
                    Log.i(TAG, "onDataChange: ooderid: "+item.orderID);
                    Log.i(TAG, "onDataChange:userid "+item.getUserId());
                    Log.i(TAG, "onDataChange: shopid" +item.getShopID());
                    items.add(item);
                    if(items.size()==0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);


                }

                Log.i(TAG, "onCreate: items size :"+items.size());
                OrdersAdapter adapter=new OrdersAdapter(items,applicationContext,a,"pending");
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // reattaching the listener updates our list in UI
                    items.clear();
                    detachDatabaseReadListener();
                    attachDatabaseReadListener();
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // reattaching the listener updates our list in UI
                    items.clear();
                    detachDatabaseReadListener();
                    attachDatabaseReadListener();
                    if(items.size()==0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        items.clear();
        detachDatabaseReadListener();
        super.onPause();
    }

    @Override
    public void onStart() {
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        Context applicationContext = OrdersTerminalActivity.getContextOfApplication();
        pref = applicationContext.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String typeing=pref.getString("Type","");

        if (typeing.equals("R")) {
            mReference = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("orders/pending");
        }
        else
        {
            mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(cuser.getUid()).child("orders/pending");
        }
        attachDatabaseReadListener();
        super.onStart();
    }

     
}
