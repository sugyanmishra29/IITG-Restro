package com.example.food.court.Menu.MenuItems;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.court.ApplicationMode;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.ProfileWindows.itemInfoWindow;
import com.example.food.court.R;
import com.example.food.court.Shop.Shop;
import com.example.food.court.ShopList;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/* this is the for listview ,but we have added recycler view adapter below



public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(@NonNull Context context, @NonNull List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_item_view, parent, false);
        }

        Item item = getItem(position);

        TextView imageUrl = listItemView.findViewById(R.id.invisible_item_image_url);
        imageUrl.setText(item.getImageUrl());
        TextView itemId = listItemView.findViewById(R.id.invisible_item_id_view);
        itemId.setText(item.itemId);
        TextView itemName = listItemView.findViewById(R.id.item_name_view);
        itemName.setText(item.getItemName());
        TextView itemDescription = listItemView.findViewById(R.id.item_description_view);
        itemDescription.setText(item.getItemDescription());
        TextView itemPrice = listItemView.findViewById(R.id.item_price_view);
        itemPrice.setText(item.getItemPrice() + " Rs/-");
        ImageView itemImage = listItemView.findViewById(R.id.item_image_view);
        if (item.getImageUrl() != null) {
            Glide.with(itemImage.getContext())
                    .load(item.getImageUrl())
                    .into(itemImage);
        } else {
            itemImage.setImageResource(R.drawable.no_image);
        }

        return listItemView;
    }
}
*/



public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private static final String TAG = "ItemAdapter";
    /*private ArrayList<String> mshopnames=new ArrayList<>();
    private ArrayList<String> mshoplocations=new ArrayList<>();
    private ArrayList<String> mshoprating=new ArrayList<>();*/
    private List<Item> mItemList=new ArrayList<>();
   // private List<String> mShopIDList=new ArrayList<>();
    Context mcontext;
    String category;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    public ItemAdapter( ArrayList<Item> mitemlist,Context mcontext,String category) {

        this.mItemList = mitemlist;
       // this.mShopIDList=mshopidlist;
        this.mcontext = mcontext;
        this.category=category;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.single_item_view,parent,false);
        ItemAdapter.ViewHolder holder=new ItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: SHop"+position+":"+mItemList.get(position).getItemName());
        final Item item = mItemList.get(position);


        String itid=(item.itemId);
        Log.i(TAG, "onBindViewHolder: item id : "+itid);

        holder.itemName.setText(item.getItemName());
        holder.itemDescription.setText(item.getItemDescription());
        holder.itemPrice.setText(item.getItemPrice()+ " Rs/-");
        holder.itemId.setText(itid);
        if (item.getImageUrl() != null && !(item.getImageUrl().equals(""))) {
            Glide.with(holder.itemImage.getContext())
                    .load(item.getImageUrl())
                    .into(holder.itemImage);
            holder.imageUrl.setText(item.getImageUrl());
        } else {
            holder.itemImage.setImageResource(R.drawable.no_image);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {

                    //allow owner to update item
                    Log.i(TAG, "onClick: itemurl: "+item.getImageUrl());
                    Log.i(TAG, "onClick: itemurl: "+holder.imageUrl.getText().toString());
                    Intent intent = new Intent(mcontext, itemEditor.class);
                    String imageurl;

                    intent.putExtra(mcontext.getString(R.string.imageUrl),holder.imageUrl.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemName), holder.itemName.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemPrice), holder.itemPrice.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(mcontext.getString(R.string.itemDescription), holder.itemDescription.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemCategory),category);
                    intent.putExtra(mcontext.getString(R.string.itemId), holder.itemId.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);


                } else {

                    // allow user to view item info
                    Intent intent = new Intent(mcontext, itemInfoWindow.class);
                    intent.putExtra(mcontext.getString(R.string.imageUrl), holder.imageUrl.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemName), holder.itemName.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemPrice), holder.itemPrice.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(mcontext.getString(R.string.itemDescription), holder.itemDescription.getText().toString());
                    intent.putExtra(mcontext.getString(R.string.itemCategory),category);
                    intent.putExtra(mcontext.getString(R.string.itemId), holder.itemId.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);

                }
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
        TextView imageUrl;
        TextView itemId;
        TextView itemName;
        TextView itemDescription;
        TextView itemPrice;
        ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUrl=itemView.findViewById(R.id.invisible_item_image_url);
            itemId=itemView.findViewById(R.id.invisible_item_id_view);
            itemName=itemView.findViewById(R.id.item_name_view);
            itemDescription=itemView.findViewById(R.id.item_description_view);
            itemPrice=itemView.findViewById(R.id.item_price_view);
            itemImage=itemView.findViewById(R.id.item_image_view);
            root=itemView.findViewById(R.id.root);

        }
    }
}


