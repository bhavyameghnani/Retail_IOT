package com.example.lenovo.retail_iot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CrowdDetection extends AppCompatActivity {

    DatabaseReference myRef;
    TextView tx1 , tx2, tx3, tx4;
    String clothS,footwearS,groceryS,kidS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_detection);
        tx1 = (findViewById(R.id.textView));
        tx2 = (findViewById(R.id.textView2));
        tx3 = (findViewById(R.id.textView3));
        tx4 = (findViewById(R.id.textView4));

        myRef = FirebaseDatabase.getInstance().getReference("CrowdStatus");



        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    CrowdStatus crowdStatus = ds.getValue(CrowdStatus.class);

                    Long clothcount = crowdStatus.getClothing();
                    Long footwearcount = crowdStatus.getFootwear();
                    Long grocerycount = crowdStatus.getGrocery();
                    Long kidcount = crowdStatus.getKids();

                    if(clothcount>10)
                        clothS="Crowded";
                    else
                        clothS="Less Crowded";

                    if(footwearcount>10)
                        footwearS="Crowded";
                    else
                        footwearS="Less Crowded";

                    if(grocerycount>10)
                        groceryS="Crowded";
                    else
                        groceryS="Less Crowded";

                    if(kidcount>10)
                        kidS="Crowded";
                    else
                        kidS="Less Crowded";


                    /*clothS = clothcount.toString();
                    footwearS = footwearcount.toString();
                    groceryS = grocerycount.toString();
                    kidS = kidcount.toString();*/



                }

                tx1.setText(clothS);
                tx2.setText(footwearS);
                tx3.setText(kidS);
                tx4.setText(groceryS);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }
}
