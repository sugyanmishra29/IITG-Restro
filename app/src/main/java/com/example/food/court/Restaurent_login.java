package com.example.food.court;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.Login.LoginActivity;
import com.example.food.court.Restaurent.Restaurent;
//import com.example.saif.nustana.Restaurent.Restaurent;
import com.example.food.court.Shop.Shop;
import com.example.food.court.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;


public class Restaurent_login extends AppCompatActivity {
    private static final String TAG = "Restaurent_login";

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    private EditText email_edit_field;
    private EditText password_edit_field;
    private EditText phone_no_edit_field;
    private EditText address_edit_field;
    private EditText confirm_password_edit_field;
    private Button register_button;
    private TextView lost_password_view;
    private EditText name_view;
    private TextView sign;
    private CheckBox rememberMe;
    private TextInputLayout container;

    private ProgressDialog progressDialog;

    private String email;
    private String password;
    private String phone_no;
    private String address;
    private String name;

    private FirebaseAuth mAuthentication;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserDatabaseReference;

private int USER=2;
private  String TYPE="Type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_login);

        Log.i(TAG, "onCreate: in login acti");
        Intent intent = getIntent();
        email_edit_field = (EditText) findViewById(R.id.email);
        password_edit_field = (EditText) findViewById(R.id.password);
        confirm_password_edit_field = (EditText) findViewById(R.id.confirm_password);
        phone_no_edit_field = (EditText) findViewById(R.id.phone_number);
        address_edit_field = (EditText) findViewById(R.id.address);
        register_button = (Button) findViewById(R.id.register_button);
        lost_password_view = (TextView) findViewById(R.id.password_reset);
        name_view = (EditText) findViewById(R.id.name);
        sign = findViewById(R.id.sign);
        rememberMe = findViewById(R.id.rememberMe);
        container = findViewById(R.id.container);

        rememberMe.setChecked(true);
        mAuthentication = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mDatabase.getReference().child("Restaurents");


        if (intent.getStringExtra("mode").equals("sign_up")) {
            signUp();
        } else if (intent.getStringExtra("mode").equals("log_in")) {
            signIn();
        }

        lost_password_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthentication.sendPasswordResetEmail(email_edit_field.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "A password reset email has been sent", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean validateForm() {
        boolean return_code = true;
        if (email_edit_field.getText().toString().isEmpty()) {
            email_edit_field.setError("Field cannot be empty");
            return_code = false;
        }
        if (password_edit_field.getText().toString().isEmpty()) {
            password_edit_field.setError("Field cannot be empty");
            return_code = false;
        }
        if (confirm_password_edit_field.getVisibility() == View.VISIBLE) {

            if (confirm_password_edit_field.getText().toString().isEmpty()) {
                confirm_password_edit_field.setError("Field cannot be empty");
                return_code = false;
            }
            if (phone_no_edit_field.getText().toString().isEmpty()) {
                phone_no_edit_field.setError("Field cannot be empty");
                return_code = false;
            }
            if (address_edit_field.getText().toString().isEmpty()) {
                address_edit_field.setError("Field cannot be empty");
                return_code = false;
            }
            if (password_edit_field.getText().toString().trim().length() < 6) {
                password_edit_field.setError("Password Must be greater than 6 characters");
                return_code = false;
            }
            if (!password_edit_field.getText().toString().trim().equals(confirm_password_edit_field.getText().toString().trim())) {
                confirm_password_edit_field.setError("Passwords do not match");
                return_code = false;
            }
            if (phone_no_edit_field.getText().toString().trim().length() < 11) {
                phone_no_edit_field.setError("Phone No. incorrect");
                return_code = false;
            }
        }
        return return_code;
    }


    private void signUp() {
        setTitle("SIGN UP");
        sign.setText(R.string.log_in);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        Log.i(TAG, "signUp: isdfdf");
        container.setEnabled(true);
        container.setVisibility(View.VISIBLE);
        confirm_password_edit_field.setVisibility(View.VISIBLE);
        phone_no_edit_field.setVisibility(View.VISIBLE);
        address_edit_field.setVisibility(View.VISIBLE);
        name_view.setVisibility(View.VISIBLE);
        lost_password_view.setVisibility(View.GONE);
        rememberMe.setVisibility(View.GONE);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_edit_field.getText().toString().trim();
                password = password_edit_field.getText().toString().trim();
                name = name_view.getText().toString().trim();
                phone_no = phone_no_edit_field.getText().toString().trim();
                address = address_edit_field.getText().toString().trim();

                if (validateForm()) {
                    // only progress if data entered is valid
                    register_button.setEnabled(false);
                    progressDialog = new ProgressDialog(Restaurent_login.this);
                    progressDialog.setMessage("Signing Up...");
                    progressDialog.show();
                    String hashedPassword=sha256(password);
                    mAuthentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in restaurent's information
                                FirebaseUser user = mAuthentication.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            // associates name with user
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Log.i(TAG, "User profile updated.");
                                                }
                                            }
                                        });


                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //send a verification email
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.i(TAG, "Error sending email");
                                        }
                                    }
                                });
                                // writes user to database
                                Shop newUser = new Shop(name,email,password, phone_no, address,"description","about", null,"");
                                mUserDatabaseReference.child(user.getUid()).child("Info").setValue(newUser);
                                mUserDatabaseReference.child(user.getUid()).child("Type").setValue("R");

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Error signing Up", Toast.LENGTH_SHORT).show();

                            }
                            register_button.setEnabled(true);


                        }

                    });
                }

            }
        });

    }

    private void signIn() {
        setTitle("LOG IN");
        sign.setText(R.string.sign_up);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        container.setEnabled(false);
        container.setVisibility(View.GONE);
        rememberMe.setVisibility(View.VISIBLE);
        lost_password_view.setVisibility(View.VISIBLE);
        confirm_password_edit_field.setVisibility(View.GONE);
        phone_no_edit_field.setVisibility(View.GONE);
        address_edit_field.setVisibility(View.GONE);
        name_view.setVisibility(View.GONE);

        register_button.setText("LOG IN");


        // get userCredentials from sharedPreferences and auto fill views if found
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_EMAIL, null);
        String password = pref.getString(PREF_PASSWORD, null);


        if (!(username == null || password == null)) {
            Log.i(TAG, username);
            email_edit_field.setText(username);
            password_edit_field.setText(password);
        }


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    // only progress if data entered is valid
                    register_button.setEnabled(false);
                    final String email = email_edit_field.getText().toString().trim();
                    final String password = password_edit_field.getText().toString().trim();
                    progressDialog = new ProgressDialog(Restaurent_login.this);
                    progressDialog.setMessage("Logging In...");
                    progressDialog.show();

                    mAuthentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if (rememberMe.isChecked()) {

                                    //save off user credentials locally for later use
                                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                                            .putString(PREF_EMAIL, email)
                                            .putString(PREF_PASSWORD, password)
                                            .commit();
                                    Log.i(TAG, "prefs created");
                                }

                                final FirebaseUser user = mAuthentication.getCurrentUser();
                                if (user.isEmailVerified()) {


                                    FirebaseDatabase.getInstance().getReference().child("Restaurents").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(!dataSnapshot.exists())
                                            {
                                                FirebaseAuth.getInstance().signOut();
                                                progressDialog.dismiss();
                                                Toast.makeText(Restaurent_login.this,"You are not shop owner",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                progressDialog.dismiss();
                                                Shop.loadCurrentUser(user.getUid());
                                                getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit()
                                                        .putString(TYPE,"R").apply();

                                                FirebaseInstanceId.getInstance().getInstanceId()
                                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    String token = task.getResult().getToken();
                                                                    FirebaseDatabase.getInstance().getReference().child("Restaurents").child(user.getUid()).child("Token").setValue(token);
                                                                } else {
                                                                    Log.i(TAG, "onComplete: token error");
                                                                }
                                                            }
                                                        });
                                                Intent i = new Intent(Restaurent_login.this,MainActivity.class);
                                                i.putExtra("USER_ID", user.getUid());
                                                i.putExtra("USER",USER);
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Please verify email first", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Restaurent_login.this, "Error signing in", Toast.LENGTH_SHORT).show();
                            }
                            register_button.setEnabled(true);

                        }
                    });

                }
            }
        });
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
