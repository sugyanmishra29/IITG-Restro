package com.example.food.court.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.food.court.R;
import com.example.food.court.Restaurent_login;

public class Restaurent_authpage extends AppCompatActivity {

    private static final String TAG = "Restaurent_authpage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_authpage);


        Button sign_up_button = (Button) findViewById(R.id.sign_up);
        Button sign_in_button = (Button) findViewById(R.id.sign_in);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sign_up_intent = new Intent(getApplicationContext(), Restaurent_login.class);
                sign_up_intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                sign_up_intent.putExtra("mode", "sign_up");
                Log.i(TAG, "onClick: before intent");
                startActivity(sign_up_intent);
                Log.i(TAG, "onClick: after intent");
                finish();
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in_intent = new Intent(getApplicationContext(), Restaurent_login.class);
                sign_in_intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                sign_in_intent.putExtra("mode", "log_in");
                startActivity(sign_in_intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED);
        finish();

    }
}
