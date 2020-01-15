package com.example.food.court;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.ProfileWindows.UserProfileActivity;
import com.example.food.court.Restaurent.Restaurent;
import com.example.food.court.Shop.Shop;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopList extends AppCompatActivity {

    private static final String TAG = "ShopList";
    private RecyclerView recyclerView;
    public static Context contextOfApplication;

    private ArrayList<Shop> mshoplsit=new ArrayList<>();
    private ArrayList<String> mshopidlist=new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser cuser;
    ProgressDialog progressDialog;
    private boolean cartempty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        contextOfApplication = getApplicationContext();
        recyclerView = findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       /* progressDialog = new ProgressDialog(ShopList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();*/




        cuser= FirebaseAuth.getInstance().getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Restaurents");

        Log.i(TAG, "onCreate: shoplidt started");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mshoplsit.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange:shop id :"+dataSnapshot1.getKey());
                    mshopidlist.add(dataSnapshot1.getKey());
                    Shop user = dataSnapshot1.child("Info").getValue(Shop.class);
                    //if(user.getShopUpiId()!=null && !user.getShopUpiId().equals(""))
                    mshoplsit.add(user);
                    Log.i(TAG, "onDataChange: "+mshoplsit.get(mshoplsit.size()-1).getShopName());
                }

                Log.i(TAG, "onCreate: shoplist size :"+mshoplsit.size());
                ShopListAdapter adapter=new ShopListAdapter(mshoplsit,mshopidlist,ShopList.this);
                recyclerView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
  /*  private void attachlistrener()
    {
        cartempty=true;
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Users").child(cuser.getUid()).child("shoppingCart").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    cartempty=false;
                }
                else
                {
                    cartempty=true;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    cartempty=false;
                }
                else
                {
                    cartempty=true;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    cartempty=false;
                }
                else
                {
                    cartempty=true;
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    cartempty=false;
                }
                else
                {
                    cartempty=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //for(int i=0;i<1000;i++)
        progressDialog.dismiss();
    }


   */



/*
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Restaurents");

        FirebaseRecyclerOptions<Restaurent> options =
                new FirebaseRecyclerOptions.Builder<Restaurent>()
                        .setQuery(query, new SnapshotParser<Restaurent>() {
                            @NonNull
                            @Override
                            public Restaurent parseSnapshot(@NonNull DataSnapshot snapshot) {
                                //snapshot=snapshot.child("Info");
                                String sn,se,sp,snum,sadd,sdes,sabout;
                                sn=snapshot.child("Info").child("shopName").getValue().toString();
                                se= snapshot.child("Info").child("shopEmail").getValue().toString();
                                sp=snapshot.child("Info").child("shopPassword").getValue().toString();
                                snum=snapshot.child("Info").child("shopNumber").getValue().toString();
                                sadd= snapshot.child("Info").child("shopAddress").getValue().toString();
                                sdes=snapshot.child("Info").child("shopPassword").getValue().toString();
                                sabout=snapshot.child("Info").child("shopPassword").getValue().toString();
                                return new Restaurent(sn,se,sp,snum,sadd,sdes,sabout,null);

                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Restaurent, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_shopview, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Restaurent model) {
                holder.shopname.setText(model.getUserName());
                holder.shopemail.setText(model.getUserEmail());
                holder.shoplocation.setText(model.getUserAddress());
                holder.shopdescription.setText(model.getUserDescription());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ShopList.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }
*/

    @Override
    protected void onStart() {
        super.onStart();

        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
