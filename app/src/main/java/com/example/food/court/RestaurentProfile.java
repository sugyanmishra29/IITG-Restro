package com.example.food.court;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.Shop.Shop;
import com.example.food.court.Shop.ShopInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurentProfile extends Fragment {

    private static final String TAG = "ShopProfileWindow";
    private static final int nameUpdate = 1;
    private static final int phoneUpdate = 2;
    private static final int addressUpdate = 3;
    private static final int descriptionUpdate = 4;
    private static final int aboutUpdate = 5;

    private TextView shopName;
    private TextView shopNumber;
    private TextView shopAddress;
    private TextView shopDescription;
    private TextView shopAbout;

    private DatabaseReference mReference;

    private String UID;
    public RestaurentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_restaurent_profile, container, false);

        //setTitle("Shop Profile");

        shopName =rootview. findViewById(R.id.shop_name);
        shopNumber = rootview.findViewById(R.id.shop_number);
        shopAddress = rootview.findViewById(R.id.shop_address);
        shopAbout = rootview.findViewById(R.id.about_view);
        shopDescription = rootview.findViewById(R.id.description_view);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        UID=currentFirebaseUser.getUid();
        mReference = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(UID).child("Info");
        updateShopAndUI();
        setOnClickListeners();
        return rootview;
    }

    private void setOnClickListeners() {
        if (ApplicationMode.currentMode.equals("owner")) {
            shopName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Name", "Please enter a new name", shopName.getText().toString(), nameUpdate);
                }
            });
            shopNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Number", "Please enter a new number", shopNumber.getText().toString().trim(), phoneUpdate);
                }
            });
            shopAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Address", "Please enter a new address", shopAddress.getText().toString().trim(), addressUpdate);
                }
            });
            shopDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Description", "Please enter a new description", shopDescription.getText().toString().trim(), descriptionUpdate);
                }
            });
            shopAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change About", "Please enter a new about", shopAbout.getText().toString().trim(), aboutUpdate);
                }
            });
        } else {
            // disallow a visitor from updating data
            Toast.makeText(getContext(), "You cannot update Shop data", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogBuilder(String title, String message, String displayText, final int mode) {
        // builds a dialog to update various fields
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);
        dialog.setMessage(message);
        final EditText editText = new EditText(getContext());
        editText.setText(displayText);
        dialog.setView(editText);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String string = editText.getText().toString().trim();
                if (!string.isEmpty()) {
                    switch (mode) {
                        case nameUpdate:
                            updateShopInDatabase("shopName", string);
                            break;
                        case phoneUpdate:
                            updateShopInDatabase("shopNumber", string);
                            break;
                        case addressUpdate:
                            updateShopInDatabase("shopAddress", string);
                            break;
                        case descriptionUpdate:
                            updateShopInDatabase("shopDescription", string);
                            break;
                        case aboutUpdate:
                            updateShopInDatabase("shopAbout", string);
                            break;
                    }
                } else
                    Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();

    }

    private void updateShopInDatabase(String key, String value) {
        mReference.child(key).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    updateShopAndUI();
                    Log.i(TAG, "Shop Updated in Database");
                } else {
                    Toast.makeText(getContext(), "Error updating ", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Error updating shop" + databaseError.toString());
                }
            }
        });
    }

    private void updateShopAndUI() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Shop shop = dataSnapshot.getValue(Shop.class);
                    setCurrentShopProfile(shop);
                    Log.i(TAG, shop.getShopAbout());
                    updateUI();
                }
                else
                {
                    Shop shop=new Shop("name","number","address","description","about");
                    setCurrentShopProfile(shop);
                    updateUI();
                    Log.i(TAG, shop.getShopAbout());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading shop data");
            }
        });

    }

    private void setCurrentShopProfile(Shop shop) {
        ShopInfo.setShopName(shop.getShopName());
        ShopInfo.setShopNumber(shop.getShopNumber());
        ShopInfo.setShopAddress(shop.getShopAddress());
        ShopInfo.setShopDescription(shop.getShopDescription());
        ShopInfo.setShopAbout(shop.getShopAbout());

    }

    private void updateUI() {
        Log.i(TAG, "Shop Info" + ShopInfo.shopName);
        shopName.setText(ShopInfo.shopName);
        shopNumber.setText(ShopInfo.shopNumber);
        shopAddress.setText(ShopInfo.shopAddress);
        shopDescription.setText(ShopInfo.shopDescription);
        shopAbout.setText(ShopInfo.shopAbout);
    }

}
