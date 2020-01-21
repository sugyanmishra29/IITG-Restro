package com.example.food.court.ProfileWindows;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.ApplicationMode;
import com.example.food.court.MainActivity;
import com.example.food.court.R;
import com.example.food.court.Shop.Shop;
import com.example.food.court.Shop.ShopInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopProfileActivity extends AppCompatActivity {

    private static final String TAG = "ShopProfileWindow";
    private static final int nameUpdate = 1;
    private static final int emailUpdate = 2;
    private static final int passwordUpdate = 3;
    private static final int phoneUpdate = 4;
    private static final int addressUpdate = 5;
    private static final int descriptionUpdate = 6;
    private static final int aboutUpdate = 7;
    private static final int upiIdUpdate=8;

    private TextView shopName;
    private TextView shopEmail;
    private TextView shopPassword;
    private TextView shopNumber;
    private TextView shopAddress;
    private TextView shopDescription;
    private TextView shopAbout;
    private TextView shopUpiId;

    private Button signOutButton;
    private Button deleteButton;
    private DatabaseReference mReference;
    private FirebaseUser currentFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        setTitle("Shop Profile");

        shopName = findViewById(R.id.shop_name);
        shopEmail=findViewById(R.id.shop_email);
        shopPassword=findViewById(R.id.shop_password);
        shopNumber = findViewById(R.id.shop_number);
        shopAddress = findViewById(R.id.shop_address);
        shopAbout = findViewById(R.id.about_view);
        shopDescription = findViewById(R.id.description_view);
        signOutButton = findViewById(R.id.sign_out);
        deleteButton = findViewById(R.id.delete);
        shopUpiId=findViewById(R.id.shop_upiid);
        Log.i(TAG, "onCreate: in profileactivity");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String UID=currentFirebaseUser.getUid();
        mReference = FirebaseDatabase.getInstance().getReference().child("Restaurents").child(UID).child("Info");
        updateShopAndUI();
        setOnClickListeners();

    }

    private void setOnClickListeners() {

            shopName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Name", "Please enter a new name", shopName.getText().toString(), nameUpdate);
                }
            });
            shopEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Email","Please enter a new email",shopEmail.getText().toString(),emailUpdate);
                }
            });
            shopPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder("Change Password","Please enter a new password",shopPassword.getText().toString(),passwordUpdate);
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
            shopUpiId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Change Upi ID", "Please enter a new Upi ID", shopUpiId.getText().toString(), upiIdUpdate);
            }
            });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFirebaseUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ShopProfileActivity.this, MainActivity.class);
// set the new task and clear flags
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Restaurents").child(currentFirebaseUser.getUid()).child("Token").setValue(null);

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ShopProfileActivity.this, MainActivity.class);
// set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                //finishing this activity opens the main activity and onAuthStateChanged will get called
            }
        });
    }

    private void dialogBuilder(String title, String message, String displayText, final int mode) {
        // builds a dialog to update various fields
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        final EditText editText = new EditText(this);
        editText.setText(displayText);
        dialog.setView(editText);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String string = editText.getText().toString().trim();
                if (!string.isEmpty()) {
                    switch (mode) {
                        case nameUpdate:
                            updateName(string);
                            break;
                        case emailUpdate:
                            updateEmail(string);
                            break;
                        case passwordUpdate:
                           updatePassword(string);
                            break;
                        case phoneUpdate:
                            updatePhone(string);
                            break;
                        case addressUpdate:
                           updateAddress(string);
                            break;
                        case descriptionUpdate:
                           updateDescription(string);
                            break;
                        case aboutUpdate:
                            updateAbout(string);
                            break;

                        case upiIdUpdate:
                            updateUpiId(string);
                            break;
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
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

    private void updateName(final String newName) {
        // user also need updating
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
        currentFirebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateShopInDatabase("shopName", newName, "Name Changed");
                        }
                    }
                });
    }

    private void updatePhone(String newPhone) {
        // only database need updating
        updateShopInDatabase("shopPhone", newPhone, "Phone Number Changed");
    }

    private void updateAddress(String newAddress) {
        // only database need updating
        updateShopInDatabase("shopAddress", newAddress, "Address Changed");
    }

    private void updateUpiId(String newUpiId) {
        // only database need updating
        updateShopInDatabase("shopUpiId", newUpiId, "Upi Id Changed");
    }
    private void updateAbout(String newAbout) {
        // only database need updating
        updateShopInDatabase("shopAbout", newAbout, "About Changed");
    }
    private void updateDescription(String newDescription) {
        // only database need updating
        updateShopInDatabase("shopDescription", newDescription, "Description Changed");
    }

    private void updateEmail(final String newEmail) {
        // user also need updating
        currentFirebaseUser.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Shop email address updated.");
                            currentFirebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateShopInDatabase("shopEmail", newEmail, "Email Change, Please verify your new email");
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    private void updatePassword(final String newPassword) {
        // user also need updating
        currentFirebaseUser.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateShopInDatabase("shopPassword", newPassword, "Password Changed");
                        }
                    }
                });
    }
    private void updateShopInDatabase(String key, String value, final String toastMessage) {
        mReference.child(key).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    updateShopAndUI();
                    Log.i(TAG, "Shop Updated in Database");
                } else {
                    Toast.makeText(getApplicationContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Error updating shop");
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
                    Shop shop=new Shop("name","email","password","number","address","description","about",null,"");

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
        ShopInfo.setShopEmail(shop.getShopEmail());
        ShopInfo.setShopPassword(shop.getShopPassword());
        ShopInfo.setShopNumber(shop.getShopNumber());
        ShopInfo.setShopAddress(shop.getShopAddress());
        ShopInfo.setShopDescription(shop.getShopDescription());
        ShopInfo.setShopAbout(shop.getShopAbout());
        ShopInfo.setShopUpiId(shop.getShopUpiId());

    }

    private void updateUI() {
        Log.i(TAG, "Shop Info" + ShopInfo.shopName);
        shopName.setText(ShopInfo.shopName);
        shopEmail.setText(ShopInfo.shopEmail);
        shopPassword.setText(ShopInfo.shopPassword);
        shopNumber.setText(ShopInfo.shopNumber);
        shopAddress.setText(ShopInfo.shopAddress);
        shopDescription.setText(ShopInfo.shopDescription);
        shopAbout.setText(ShopInfo.shopAbout);
        shopUpiId.setText(ShopInfo.shopUpiId);
    }
}
