package com.example.food.court.Order.OrderItem;


import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.food.court.ApplicationMode;
import com.example.food.court.CustomDialogs.OrderInfoDialog;
import com.example.food.court.Menu.MenuItems.Item;
import com.example.food.court.Menu.MenuItems.itemEditor;
import com.example.food.court.Order.Fragments.DeliveredFragment;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.ProfileWindows.itemInfoWindow;
import com.example.food.court.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;
/*
public class OrdersAdapter extends ArrayAdapter<Order> {
    public static final String TAG = "orderAdapter";
    private DatabaseReference mReference;
    private TextView userNameView;
private FirebaseUser cuser;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    public OrdersAdapter(@NonNull Context context, @NonNull List<Order> objects) {
        super(context, 0, objects);
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        Context applicationContext = OrdersTerminalActivity.getContextOfApplication();
        pref = applicationContext.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final String typeing=pref.getString("Type","");
        if(typeing.equals("R"))
             mReference = FirebaseDatabase.getInstance().getReference("/Restaurents/"+cuser.getUid()+"/orders");
        else
            mReference = FirebaseDatabase.getInstance().getReference("/Users"+cuser.getUid()+"/orders");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View orderItemView = convertView;
        if (orderItemView == null) {
            orderItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_item_order_view, parent, false);
        }

        Order orderItem = getItem(position);

        TextView userIdVIew = orderItemView.findViewById(R.id.soUserID);
        userIdVIew.setText(orderItem.getUserId());
        TextView orderIdVIew = orderItemView.findViewById(R.id.soOrderID);
        orderIdVIew.setText(orderItem.orderID.substring(1));
        TextView totalPrice = orderItemView.findViewById(R.id.soTotalPrice);
        totalPrice.setText("Total Price\n=" + orderItem.getTotalPrice() + "RS/-");
        return orderItemView;

    }

}

 */



public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    String orderStatus;
    Activity activity;
    public static final String TAG = "orderAdapter";
    private DatabaseReference mReference;
    private TextView userNameView;
    private FirebaseUser cuser;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    /*private ArrayList<String> mshopnames=new ArrayList<>();
    private ArrayList<String> mshoplocations=new ArrayList<>();
    private ArrayList<String> mshoprating=new ArrayList<>();*/
    private List<Order> mItemList=new ArrayList<>();
    // private List<String> mShopIDList=new ArrayList<>();
    Context mcontext;

    public OrdersAdapter( ArrayList<Order> mitemlist,Context mcontext,Activity c,String orderStatus) {

        this.mItemList = mitemlist;
        // this.mShopIDList=mshopidlist;
        this.mcontext = mcontext;
        this.activity=c;
        this.orderStatus=orderStatus;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.single_item_order_view,parent,false);
        OrdersAdapter.ViewHolder holder=new OrdersAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: SHop"+position+":"+mItemList.get(position).getUserId());
        final Order orderItem = mItemList.get(position);
        Log.i(TAG, "onBindViewHolder: orderDI : "+orderItem.orderID);
        holder.userIdVIew.setText(orderItem.getUserId());
        holder.orderIdVIew.setText(orderItem.orderID);
        holder.totalPrice.setText("Total Price\n=" + orderItem.getTotalPrice()+ " Rs/-");


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationMode.orderStatus = orderStatus;
                TextView tv = view.findViewById(R.id.soOrderID);
                TextView tv1 = view.findViewById(R.id.soUserID);
                // gives orderId and userId to dialog to allow it to retrieve it from database
                OrderInfoDialog od = new OrderInfoDialog(activity, tv.getText().toString(), tv1.getText().toString(),orderItem.getShopID(),orderItem.getTotalPrice(),mcontext);
                od.show();
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
        TextView userIdVIew;
        TextView orderIdVIew;
        TextView totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userIdVIew=itemView.findViewById(R.id.soUserID);
            orderIdVIew=itemView.findViewById(R.id.soOrderID);
            totalPrice=itemView.findViewById(R.id.soTotalPrice);

            root=itemView.findViewById(R.id.root);

        }
    }
}



