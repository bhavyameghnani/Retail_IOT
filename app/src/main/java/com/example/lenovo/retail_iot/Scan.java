package com.example.lenovo.retail_iot;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Scan extends AppCompatActivity {

    public static TextView tvresult;
    private  Button btn;
    private IntentIntegrator qrScan;

    String userId, productTitle, userIdd;
    Long productId, cost;
    Long prodId;

    private FirebaseAuth auth;
    private FirebaseUser user;

    int flag=0;
    int totalCost=0;

    DatabaseReference myRef, myRef1, mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        totalCost=0;

        qrScan = new IntentIntegrator(this);
        tvresult = (TextView) findViewById(R.id.tvresult);
        btn = (Button) findViewById(R.id.btn);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference("ProductDetails");
        myRef1 = FirebaseDatabase.getInstance().getReference("MyCart");
        mm = FirebaseDatabase.getInstance().getReference("MyCart");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        mm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userId = auth.getUid().toString();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MyCart myCart = ds.getValue(MyCart.class);
                    userIdd = myCart.getUserId();
                    cost=myCart.getProductCost();
                    Log.d("COST",cost.toString());

                    totalCost = cost.intValue() + totalCost;
                    tvresult.setText(Integer.toString(totalCost));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    void sendData(){
        String MyCartKey = myRef1.push().getKey();
        MyCart myCart = new MyCart(userId,productTitle,productId,cost);
        myRef1.child(MyCartKey).setValue(myCart);
        userId = "";
        productId = 5000000000L;
        startActivity(new Intent(Scan.this, Home.class));
        finish();

    }

    void removeData(DataSnapshot dds){
        dds.getRef().removeValue();
        userId = "";
        productId = 5000000000L;
        Log.d("BALALE","BALALEee");
        startActivity(new Intent(Scan.this, Home.class));
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    tvresult.setText(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    Log.d("USER_ID",user.toString());
                    Log.d("AUTH_ID",auth.getUid().toString());


                    userId = auth.getUid().toString();
                    productId = Long.parseLong(result.getContents());

                    myRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                MyCart myCart = ds.getValue(MyCart.class);
                                prodId = myCart.getProductId();
                                userIdd = myCart.getUserId();

                                if(productId.equals(prodId) && userId.equals(userIdd)){
                                   removeData(ds);
                                }
                            }

                            if(flag==0) {
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // This method is called once with the initial value and again
                                        // whenever data at this location is updated.
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            ProductDetails prod = ds.getValue(ProductDetails.class);
                                            productTitle = prod.getProductTitle();
                                            cost = prod.getProductCost();
                                            prodId = prod.getProductId();
                                            Log.d("BALALE","BALALE");
                                            Log.d("PRODUCT_ID",productId.toString());
                                            Log.d("PROD_ID",prodId.toString());
                                            if (prodId.equals(productId)) {
                                                sendData();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w("Failed to read value.", error.toException());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}