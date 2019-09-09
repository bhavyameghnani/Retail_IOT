package com.example.lenovo.retail_iot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ProgressDialog loginProgress;
    private FirebaseAuth auth;
    private CardView commuter_card;
    private CardView traffic_officer_card;
    private CardView ambulance_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        loginProgress = ProgressDialog.show(this, null, "Please wait...", true);
        loginProgress.setCancelable(false);

        if (auth.getCurrentUser() != null) {
            loginProgress.dismiss();
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
            return;
        }
        loginProgress.dismiss();
        commuter_card = findViewById(R.id.commuter_card);
        traffic_officer_card = findViewById(R.id.traffic_officer_card);
        ambulance_card = findViewById(R.id.ambulance_card);

        commuter_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                intent.putExtra("type","commuters");
                startActivity(intent);
            }
        });

        traffic_officer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                intent.putExtra("type","officer");
                startActivity(intent);
            }
        });

        ambulance_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                intent.putExtra("type","ambulance");
                startActivity(intent);
            }
        });
    }
}