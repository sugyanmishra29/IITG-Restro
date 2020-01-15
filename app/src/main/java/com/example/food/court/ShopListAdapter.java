package com.example.food.court;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Restaurent.Restaurent;
import com.example.food.court.Shop.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {


    private static final String TAG = "Recyclerviewadapter";
    /*private ArrayList<String> mshopnames=new ArrayList<>();
    private ArrayList<String> mshoplocations=new ArrayList<>();
    private ArrayList<String> mshoprating=new ArrayList<>();*/
    private List<Shop> mShopList=new ArrayList<>();
    private List<String> mShopIDList=new ArrayList<>();
    Context mcontext;
    boolean shoppingcartempty;
    private DatabaseReference mref;
    private FirebaseUser firebaseUser;

    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    public ShopListAdapter( ArrayList<Shop> mshoplist,ArrayList<String> mshopidlist ,Context mcontext) {


        this.mShopList = mshoplist;
        this.mShopIDList=mshopidlist;
        this.mcontext = mcontext;
        this.shoppingcartempty=true;
        this.firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        this.mref= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("shoppingCart");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.single_shopview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: SHop"+position+":"+mShopList.get(position).getShopName());
        holder.shopname.setText(mShopList.get(position).getShopName());
        holder.shoplocation.setText(mShopList.get(position).getShopAddress());
        holder.shopemail.setText(mShopList.get(position).getShopEmail());
        holder.shopdescription.setText(mShopList.get(position).getShopDescription());
        holder.shopID=mShopIDList.get(position);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext,""+holder.shopID,Toast.LENGTH_SHORT).show();
                final Context applicationContext = ShopList.contextOfApplication;
                pref= applicationContext.getSharedPreferences(PREFS_NAME,mcontext.MODE_PRIVATE);
                editor=pref.edit();
                String curShop=pref.getString("SHOPID",null);
                shoppingcartempty=pref.getBoolean("Cart",true);
                if(shoppingcartempty)
                {
                    mref.removeValue();
                }

             /*   mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //for (dataSnapshot1 in dataSnapshot
                        if(dataSnapshot.exists())
                            shoppingcartempty=false;
                        else
                            shoppingcartempty=true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                Log.i(TAG, "onClick: prevsgopid : "+curShop);
                Log.i(TAG, "onClick: shoppingcart: "+shoppingcartempty);
                Log.i(TAG, "onClick: holder.shopid: "+holder.shopID);
                if(curShop==null || curShop.equals(holder.shopID) || shoppingcartempty)
                {
                    if(curShop==null && !shoppingcartempty)
                        mref.removeValue();
                    //ApplicationMode.currentShop=holder.shopID;
                    editor.putString("SHOPID",holder.shopID).apply();
                    Intent i=new Intent(mcontext, ShopMenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(i);
                }
                else
                {
                    androidx.appcompat.app.AlertDialog.Builder abuilder = new AlertDialog.Builder(mcontext);
                    abuilder.setTitle("Order");
                    abuilder.setMessage("Are you sure you want to change shop? Earlier items would be deleted.");

                    abuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    abuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mref.removeValue();
                            editor.putString("SHOPID",holder.shopID).apply();
                            editor.putBoolean("Cart",true).apply();
                            Intent i=new Intent(mcontext, ShopMenuActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(i);
                           // ApplicationMode.currentShop=holder.shopID;
                            //  FirebaseAuth.getInstance().signOut();
                            //finish();
                            //Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    abuilder.show();
                }
                Toast.makeText(mcontext,""+holder.shopID,Toast.LENGTH_SHORT).show();

               // editor.putString("SHOPID",holder.shopID).apply();
                //Log.i(TAG, "onClick: shopid: "+pref.getString("SHOPID",""));

            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: "+mShopList.size());
        return mShopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView root;
        TextView shopname;
        TextView shoplocation;
        TextView shopemail;
        TextView shopdescription;
        String shopID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.root);
            shopname=itemView.findViewById(R.id.shop_name);
            shoplocation=itemView.findViewById(R.id.location);
            shopemail=itemView.findViewById(R.id.shop_email);
            shopdescription=itemView.findViewById(R.id.description);
        }
    }
}
