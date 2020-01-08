package com.example.food.court;


import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Restaurent.Restaurent;
import com.example.food.court.Shop.Shop;

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

    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    public ShopListAdapter( ArrayList<Shop> mshoplist,ArrayList<String> mshopidlist ,Context mcontext) {

        this.mShopList = mshoplist;
        this.mShopIDList=mshopidlist;
        this.mcontext = mcontext;
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
                Context applicationContext = ShopList.contextOfApplication;
                pref= applicationContext.getSharedPreferences(PREFS_NAME,mcontext.MODE_PRIVATE);
                editor=pref.edit();
                editor.putString("SHOPID",holder.shopID).apply();
                Log.i(TAG, "onClick: shopid: "+pref.getString("SHOPID",""));
                Intent i=new Intent(mcontext, ShopMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(i);
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
