package com.example.food.court.Order.ShoppingCart;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food.court.ApplicationMode;
import com.example.food.court.Menu.MenuItems.Item;
import com.example.food.court.Menu.MenuItems.itemEditor;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.ProfileWindows.itemInfoWindow;
import com.example.food.court.R;
import com.example.food.court.User.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
/*
public class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartItem> {

    DatabaseReference mReference;


    public ShoppingCartAdapter(@NonNull Context context, @NonNull List<ShoppingCartItem> objects) {
        super(context, 0, objects);
        mReference = FirebaseDatabase.getInstance().getReference("Users/" + UserInfo.userID + "/shoppingCart");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_shopping_cart_item, parent, false);
        }

        final ShoppingCartItem item = getItem(position);
        TextView itemDatabaseKey = listItemView.findViewById(R.id.sInvisibleKey);
        itemDatabaseKey.setText(item.itemDatabaseKey);
        TextView itemName = listItemView.findViewById(R.id.sItemNameView);
        itemName.setText(item.getName());
        TextView itemQuantity = listItemView.findViewById(R.id.sItemQuantityView);
        itemQuantity.setText("Quantity:" + item.getQuantity());
        TextView itemPrice = listItemView.findViewById(R.id.sItemPriceView);
        itemPrice.setText(item.getPrice() + " Rs/-");

        // cancel button removes item from database
        final ImageView cancelView = listItemView.findViewById(R.id.sCancelItem);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingCartAdapter.this.remove(getItem(position));
                cancelView.setEnabled(false);
                mReference.child(item.itemDatabaseKey).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                        } else {
                            Toast.makeText(getContext(), "Unable to remove at the moment.Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        return listItemView;
    }


}

 */

public class ShoppingCartAdapter extends Adapter<ShoppingCartAdapter.ViewHolder> {


    private static final String TAG = "ShoppingCartAdapter";
    /*private ArrayList<String> mshopnames=new ArrayList<>();
    private ArrayList<String> mshoplocations=new ArrayList<>();
    private ArrayList<String> mshoprating=new ArrayList<>();*/
    private List<ShoppingCartItem> mItemList=new ArrayList<>();
    // private List<String> mShopIDList=new ArrayList<>();
    Context mcontext;
    DatabaseReference mReference;

    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    public ShoppingCartAdapter( ArrayList<ShoppingCartItem> mitemlist,Context mcontext) {

        this.mItemList = mitemlist;
        // this.mShopIDList=mshopidlist;
        this.mcontext = mcontext;
        mReference = FirebaseDatabase.getInstance().getReference("Users/" + UserInfo.userID + "/shoppingCart");
    }

    @NonNull
    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.single_shopping_cart_item,parent,false);
        ShoppingCartAdapter.ViewHolder holder=new ShoppingCartAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCartAdapter.ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: SHop"+position+":"+mItemList.get(position).getItemId());
        final ShoppingCartItem item = mItemList.get(position);


        holder.itemName.setText(item.getName());
        holder.itemDatabaseKey.setText(item.itemDatabaseKey);
        holder.itemPrice.setText(item.getPrice()+ " Rs/-");
        holder.itemQuantity.setText("Quantity:" +item.getQuantity());

        if(mcontext== OrdersTerminalActivity.getContextOfApplication())
        {
            holder.cancelView.setVisibility(View.GONE);
        }
        holder.cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemList.remove(position);
                holder.cancelView.setEnabled(false);
                mReference.child(item.itemDatabaseKey).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError==null)
                        {

                        }
                        else
                        {
                            Toast.makeText(mcontext, "Unable to remove at the moment.Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: "+mItemList.size());
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView root;
        TextView itemDatabaseKey;
        TextView itemName;
        TextView itemQuantity;
        TextView itemPrice;
      ImageView cancelView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            itemDatabaseKey=itemView.findViewById(R.id.sInvisibleKey);
            itemQuantity=itemView.findViewById(R.id.sItemQuantityView);
            itemName=itemView.findViewById(R.id.sItemNameView);
            cancelView=itemView.findViewById(R.id.sCancelItem);
            itemPrice=itemView.findViewById(R.id.sItemPriceView);

            root=itemView.findViewById(R.id.root);

        }
    }
}




