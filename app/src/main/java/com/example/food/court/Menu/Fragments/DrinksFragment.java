package com.example.food.court.Menu.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.food.court.Menu.MenuItems.Item;
import com.example.food.court.Menu.MenuItems.ItemAdapter;
import com.example.food.court.Menu.MenuItems.itemEditor;
import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.ProfileWindows.itemInfoWindow;
import com.example.food.court.R;
import com.example.food.court.ShopList;
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

import static android.content.Context.MODE_PRIVATE;

public class DrinksFragment extends Fragment {
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    ArrayList<Item> items = new ArrayList<>();
    Context applicationContext;

    private FirebaseUser cuser;
    View emptyView;
    SharedPreferences pref; //sp-the name of shared preferences has to be the same in both the files
    SharedPreferences.Editor editor;//editor-the name of the editor can be different in both the files
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "DrinksFragment";
    public DrinksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_with_empty_view, container, false);
        applicationContext = ShopMenuActivity.getContextOfApplication();
        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));


        emptyView = rootView.findViewById(R.id.empty_view);
        pref = applicationContext.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
      
        cuser= FirebaseAuth.getInstance().getCurrentUser();
        if (!ApplicationMode.checkConnectivity(getActivity())) {
            TextView emptyTitle = rootView.findViewById(R.id.empty_title_text);
            emptyTitle.setText("Please check your internet connection");

        }
      
        String Type=pref.getString("Type","");
        if(Type.equals("R"))
            mReference = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("/items/" + getString(R.string.drink));
        else
        {
            String shopID=pref.getString("SHOPID","");
            mReference = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(shopID).child("/items/" + getString(R.string.drink));
        }
        //creating an empty list view to set up the adapter
        //later on the onChildAdded() method in attachDatabaseReadListener() keeps updating our adapter
        /*List<Item> items = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView itemNameView = view.findViewById(R.id.item_name_view);
                TextView itemDescriptionView = view.findViewById(R.id.item_description_view);
                TextView itemPriceView = view.findViewById(R.id.item_price_view);
                TextView itemIdVIew = view.findViewById(R.id.invisible_item_id_view);
                TextView imageUrl = view.findViewById(R.id.invisible_item_image_url);
                if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
                    // allow owner to update Item
                    Intent intent = new Intent(getContext(), itemEditor.class);
                    intent.putExtra(getString(R.string.imageUrl), imageUrl.getText().toString());
                    intent.putExtra(getString(R.string.itemName), itemNameView.getText().toString());
                    intent.putExtra(getString(R.string.itemPrice), itemPriceView.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(getString(R.string.itemDescription), itemDescriptionView.getText().toString());
                    intent.putExtra(getString(R.string.itemCategory), getString(R.string.drink));
                    intent.putExtra(getString(R.string.itemId), itemIdVIew.getText().toString());
                    startActivity(intent);
                } else {
                    // allow user to view ItemInfo
                    Intent intent = new Intent(getContext(), itemInfoWindow.class);
                    intent.putExtra(getString(R.string.imageUrl), imageUrl.getText().toString());
                    intent.putExtra(getString(R.string.itemName), itemNameView.getText().toString());
                    intent.putExtra(getString(R.string.itemPrice), itemPriceView.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(getString(R.string.itemDescription), itemDescriptionView.getText().toString());
                    intent.putExtra(getString(R.string.itemCategory), getString(R.string.drink));
                    intent.putExtra(getString(R.string.itemId), itemIdVIew.getText().toString());
                    startActivity(intent);

                }
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange:item id :"+dataSnapshot1.getKey());
                    //items.add(dataSnapshot1.getKey());
                    Item item = dataSnapshot1.getValue(Item.class);
                    item.itemId = dataSnapshot1.getKey();
                    Log.i(TAG, "onDataChange:itemname "+item.getItemName());
                    Log.i(TAG, "onDataChange: itemid" +item.itemId);
                    items.add(item);
                    if(items.size()==0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);

                    Log.i(TAG, "onDataChange: "+items.get(items.size()-1).getItemName());
                }

                Log.i(TAG, "onCreate: items size :"+items.size());
                ItemAdapter adapter=new ItemAdapter(items,applicationContext,"Drink");
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
        attachDatabaseReadListener();
        super.onStart();
    }
 
}
