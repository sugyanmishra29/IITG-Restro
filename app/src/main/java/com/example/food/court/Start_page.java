package com.example.food.court;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food.court.Login.Restaurent_authpage;
import com.example.food.court.Login.WelcomeScreenActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;

public class Start_page extends AppCompatActivity {
    Button customerBtn;
    Button ownerBtn;
    private static final String TAG = "start_page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Log.i(TAG, "onCreate: In start_page");
        customerBtn=(Button)findViewById(R.id.Customer);
        ownerBtn=(Button)findViewById(R.id.Owner);

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Start_page.this, WelcomeScreenActivity.class);
                    startActivity(i);
                }
                catch(Exception e){
                    Toast.makeText(Start_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ownerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Start_page.this, Restaurent_authpage.class);
                    startActivity(i);
                }
                catch(Exception e){
                    Toast.makeText(Start_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "onCreate: In start_page after some time");
    }

}
