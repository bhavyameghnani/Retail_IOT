package com.example.lenovo.retail_iot;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
/*import com.example.android.wastemanagement.Models.Bandwidth;
import com.example.android.wastemanagement.Models.Industry;
import com.example.android.wastemanagement.Models.Ngo;
import com.example.android.wastemanagement.Models.Society;
import com.example.android.wastemanagement.Models.Tracker;
import com.example.android.wastemanagement.Models.User;
import com.example.android.wastemanagement.Models.Volunteer;
import com.example.android.wastemanagement.Models.Zone;*/
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Home1 extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    public boolean closeview = false;
    private FirebaseAuth auth;
    private FirebaseUser userF;
    private TextView userName, userEmail , userPoints, userCash, filterName;
    EditText bandwidth;
    TextView textView;
    DatabaseReference dbuser, dbtoken ,reff;
    ImageView userImg, info;
    long userDonationStatus;
    String userType, userCity, userCardinal, infoDetails = "";
    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    Button donate, sell, submitDonation, submitSell, donorQR, ngoQR, showRoute, volunteerAccept, volunteerReject, volunteerScanQR;
    LinearLayout donationView, volunteerView, volunteer_acc_rej, volunteer_route_scan, donate_sell, sellView, dropDown, points;
    ImageView aclothes,agrains,apacked,astationary,afurniture,aelectronic;
    ImageView mclothes,mgrains,mpacked,mstationary,mfurniture,melectronic;
    TextView qclothes,qgrains,qpacked,qstationary,qfurniture,qelectronic;
    TextView fromDate,fromTime, fromDateSell, fromTimeSell;
    ImageView cancel, cancelSell;
    DatePickerDialog.OnDateSetListener fromDatepicker, fromDatepickerSell;
    Calendar myCalendar = Calendar.getInstance();
    String lat, lng, category;
    LatLng dangerous_area[] = new LatLng[60];
    GeoQuery geoQuery;
    GeoFire geoFire;
    List<String> inZoneOf = new ArrayList<>();
    List<String> donorSpinnerList = new ArrayList<>();
    List<Long> donorSpinnerAcceptStatus = new ArrayList<>();
    List<String> donorLati = new ArrayList<>();
    List<String> donorLongi = new ArrayList<>();
    List<String> volunteerLati = new ArrayList<>();
    List<String> volunteerLongi = new ArrayList<>();
    List<String> donorAuthKey = new ArrayList<>();
    List<Bandwidth> bandwithList = new ArrayList<>();
    List<Boolean> isNGOItem = new ArrayList<>();
    String donorName, donorImg, donorLat, donorLong, volunteerName, volunteerImg, volunteerLat, volunteerLong;
    String date, time, ngoAuthKey, username, userImgUrl, donorAuth, type, passedUser;
    LatLng source, dest;
    //Bandwidth toCollect, cmpBandwidth, getBandwidthTracker;
    Spinner donorSpinner;
    Boolean alreadyZoned = false;
    AlertDialog.Builder AlertName;
    double latitude , longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        reff = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(reff);

        donate = findViewById(R.id.donate);
        sell = findViewById(R.id.sell);
        info = findViewById(R.id.info);
        donate_sell = findViewById(R.id.donate_sell);
        sellView = findViewById(R.id.sell_view);
        dropDown = findViewById(R.id.dropDown);
        filterName = findViewById(R.id.filterName);
        submitDonation = findViewById(R.id.submitDonation);
        submitSell = findViewById(R.id.submitSell);
        donationView = findViewById(R.id.donation_view);
        donorQR = findViewById(R.id.donorQRgenr);
        ngoQR = findViewById(R.id.ngoQRgenr);
        volunteerView = findViewById(R.id.volunteer_view);
        donorSpinner = findViewById(R.id.donorList_spinner);
        volunteer_acc_rej = findViewById(R.id.volunteer_acc_rej);
        volunteer_route_scan = findViewById(R.id.volunteer_route_scan);
        volunteerScanQR = findViewById(R.id.volunteer_scanQR);
        showRoute = findViewById(R.id.makeRoute);
        volunteerAccept = findViewById(R.id.volunteer_accept);
        volunteerReject = findViewById(R.id.volunteer_reject);
        aclothes = findViewById(R.id.add_clothes);agrains = findViewById(R.id.add_grains);
        apacked = findViewById(R.id.add_packed);astationary = findViewById(R.id.add_stationary);
        afurniture = findViewById(R.id.add_furniture);aelectronic = findViewById(R.id.add_electronic);
        mclothes = findViewById(R.id.minus_clothes);mgrains = findViewById(R.id.minus_grains);
        mpacked = findViewById(R.id.minus_packed);mstationary = findViewById(R.id.minus_stationary);
        mfurniture = findViewById(R.id.minus_furniture);melectronic = findViewById(R.id.minus_electronic);
        qclothes = findViewById(R.id.qclothes);qgrains = findViewById(R.id.qgrains);
        qpacked = findViewById(R.id.qpacked);qstationary = findViewById(R.id.qstationary);
        qfurniture = findViewById(R.id.qfurniture);qelectronic = findViewById(R.id.qelectronic);

        fromDate = findViewById(R.id.fromDate);
        fromTime = findViewById(R.id.fromTime);
        fromDateSell = findViewById(R.id.fromDateSell);
        fromTimeSell = findViewById(R.id.fromTimeSell);
        cancel = findViewById(R.id.create_post_cancel);
        cancelSell = findViewById(R.id.create_post_cancel_sell);

        auth = FirebaseAuth.getInstance();

        //adding quantity
        aclothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qclothes.getText().toString())+1;
                qclothes.setText(String.valueOf(v));
            }
        });
        agrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qgrains.getText().toString())+1;
                qgrains.setText(String.valueOf(v));
            }
        });
        apacked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qpacked.getText().toString())+1;
                qpacked.setText(String.valueOf(v));
            }
        });
        astationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qstationary.getText().toString())+1;
                qstationary.setText(String.valueOf(v));
            }
        });
        afurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qfurniture.getText().toString())+1;
                qfurniture.setText(String.valueOf(v));
            }
        });
        aelectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qelectronic.getText().toString())+1;
                qelectronic.setText(String.valueOf(v));
            }
        });

        //removing quantity
        mclothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qclothes.getText().toString())-1;
                qclothes.setText(String.valueOf(v));
            }
        });
        mgrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qgrains.getText().toString())-1;
                qgrains.setText(String.valueOf(v));
            }
        });
        mpacked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qpacked.getText().toString())-1;
                qpacked.setText(String.valueOf(v));
            }
        });
        mstationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qstationary.getText().toString())-1;
                qstationary.setText(String.valueOf(v));
            }
        });
        mfurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qfurniture.getText().toString())-1;
                qfurniture.setText(String.valueOf(v));
            }
        });
        melectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.parseInt(qelectronic.getText().toString())-1;
                qelectronic.setText(String.valueOf(v));
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(Home1.this,fromDatepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fromDatepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                fromDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        fromDateSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Home1.this,fromDatepickerSell, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fromDatepickerSell = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                fromDateSell.setText(sdf.format(myCalendar.getTime()));
            }

        };

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Home1.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        fromTimeSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Home1.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTimeSell.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donationView.setVisibility(View.GONE);
                donate_sell.setVisibility(View.VISIBLE);
            }
        });
        cancelSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellView.setVisibility(View.GONE);
                donate_sell.setVisibility(View.VISIBLE);
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donationView.setVisibility(View.VISIBLE);
                donate_sell.setVisibility(View.GONE);
                category = "ngo";
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellView.setVisibility(View.VISIBLE);
                donate_sell.setVisibility(View.GONE);
                category = "industry";
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertName = new AlertDialog.Builder(Home1.this);
                textView = new TextView(Home1.this);
                textView.setText(infoDetails);
                AlertName.setTitle("Collection Details");
                //alert.setMessage("Enter Your Title");
                AlertName.setView(textView);
                AlertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        infoDetails = "";
                        dialog.dismiss();
                    }
                });
                AlertName.show();
            }
        });
        submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB store code
                long vclothes = Long.valueOf(qclothes.getText().toString());
                long vgrains = Long.valueOf(qgrains.getText().toString());
                long vpacked = Long.valueOf(qpacked.getText().toString());
                long vstationary = Long.valueOf(qstationary.getText().toString());
                long vfurniture = Long.valueOf(qfurniture.getText().toString());
                long velectronic = Long.valueOf(qelectronic.getText().toString());
                if(vclothes < 0 || vgrains < 0 || vpacked < 0 || vstationary < 0 || vfurniture < 0 || velectronic < 0 ){
                    Toast.makeText(Home1.this, "Cannot accept negative quantity", Toast.LENGTH_SHORT).show();
                }else{
                    Zone1 zone = new Zone1(auth.getUid(), lat, lng, category, userCity, userCardinal,
                            fromDate.getText().toString(), fromTime.getText().toString(),
                            new Bandwidth(vclothes,vpacked,vgrains,vstationary,0,vfurniture,velectronic));
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Waste");
                    dbref.child(auth.getUid()).setValue(zone);
                    Toast.makeText(Home1.this, "Items added successfully", Toast.LENGTH_SHORT).show();
                    donationView.setVisibility(View.GONE);
                    donorQR.setVisibility(View.VISIBLE);
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("donor").child(auth.getUid()).child("donation_status");
                ref.setValue((long)1);
            }
        });

        submitSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filterName.getText().toString().equals("Select Type")){
                    Toast.makeText(Home1.this, "Select type first", Toast.LENGTH_SHORT).show();
                }else{
                    Zone1 zone = new Zone1(auth.getUid(), lat, lng, category, userCity, userCardinal,
                            fromDateSell.getText().toString(), fromTimeSell.getText().toString(),
                            filterName.getText().toString());
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Waste");
                    dbref.child(auth.getUid()).setValue(zone);
                    Toast.makeText(Home1.this, "Items added successfully", Toast.LENGTH_SHORT).show();
                    sellView.setVisibility(View.GONE);
                    donorQR.setVisibility(View.VISIBLE);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("donor").child(auth.getUid()).child("donation_status");
                    ref.setValue((long)1);
                }
            }
        });


        volunteerAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                        .child("tracker").child(auth.getUid()).child(donorAuth).child("accept_status");
                db.setValue((long)1);
                DatabaseReference dbZone = FirebaseDatabase.getInstance().getReference()
                        .child("Zones").child(donorAuth);
                dbZone.getRef().removeValue();
                DatabaseReference dbMyloc = FirebaseDatabase.getInstance().getReference()
                        .child("MyLocation").child(donorAuth);
                dbMyloc.getRef().removeValue();
                volunteer_acc_rej.setVisibility(View.GONE);
                volunteer_route_scan.setVisibility  (View.VISIBLE);
            }
        });

        donorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Home1.this, donorSpinnerList.get(i)+" : ", Toast.LENGTH_SHORT).show();
                if(donorSpinnerAcceptStatus.get(i)==0){
                    volunteer_acc_rej.setVisibility(View.VISIBLE);
                    volunteer_route_scan.setVisibility(View.GONE);
                }else{
                    volunteer_acc_rej.setVisibility(View.GONE);
                    volunteer_route_scan.setVisibility(View.VISIBLE);
                }
                if(isNGOItem.get(i)){
                    info.setVisibility(View.VISIBLE);
                }else{
                    info.setVisibility(View.GONE);
                }
                infoDetails = "";
                if(!donorLati.get(i).equals("no")){
                    passedUser = "donor";
                    dest = new LatLng(Double.valueOf(donorLati.get(i)), Double.valueOf(donorLongi.get(i)));
                }else{
                    passedUser = "ngo";
                }
                source = new LatLng(Double.valueOf(volunteerLati.get(i)), Double.valueOf(volunteerLongi.get(i)));
                donorAuth = donorAuthKey.get(i);
                if(bandwithList.get(i).getClothes()>0){
                    infoDetails += "\n        Clothes : "+String.valueOf(bandwithList.get(i).getClothes());
                }
                if(bandwithList.get(i).getPackedFood()>0){
                    infoDetails += "\n        Packed Food : "+String.valueOf(bandwithList.get(i).getPackedFood());
                }
                if(bandwithList.get(i).getGrains()>0){
                    infoDetails += "\n        Grains : "+String.valueOf(bandwithList.get(i).getGrains());
                }
                if(bandwithList.get(i).getStationary()>0){
                    infoDetails += "\n        Stationary : "+String.valueOf(bandwithList.get(i).getStationary());
                }
                if(bandwithList.get(i).getHouseholdProduct()>0){
                    infoDetails += "\n        Household Product : "+String.valueOf(bandwithList.get(i).getHouseholdProduct());
                }
                if(bandwithList.get(i).getFurniture()>0){
                    infoDetails += "\n        Furniture : "+String.valueOf(bandwithList.get(i).getFurniture());
                }
                if(bandwithList.get(i).getElectronics()>0){
                    infoDetails += "\n        Electronics : "+String.valueOf(bandwithList.get(i).getElectronics());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home1.this,"Showing the best route!",Toast.LENGTH_SHORT).show();
                requestDirection(new LatLng(19.0249641,72.8515969),dest,mMap);
            }
        });

        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Home1.this, dropDown);
                // populate menu with 7 options
                popupMenu.getMenu().add(1, 1, 1, "Paper");
                popupMenu.getMenu().add(1, 2, 2, "E-waste");
                // show the menu
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case 1 : filterName.setText("Paper");
                                break;
                            case 2 : filterName.setText("E-waste");
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }


    public void loadSpinner(){
        DatabaseReference dbSPinner = FirebaseDatabase.getInstance().getReference()
                .child("tracker").child(auth.getUid());
        dbSPinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("loadSpinner","inside");
                    Log.d("loadSpinner",snapshot.child("donorName").getValue(String.class));
                    if(!donorSpinnerList.contains(snapshot.child("donorName").getValue(String.class))){
                        donorAuthKey.add(snapshot.getKey());
                        donorSpinnerList.add(snapshot.child("donorName").getValue(String.class));
                        donorSpinnerAcceptStatus.add(snapshot.child("accept_status").getValue(Long.class));
                        donorLati.add(snapshot.child("donorLat").getValue(String.class));
                        donorLongi.add(snapshot.child("donorLong").getValue(String.class));
                        volunteerLati.add(snapshot.child("volunteerLat").getValue(String.class));
                        volunteerLongi.add(snapshot.child("volunteerLong").getValue(String.class));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        Home1.this, android.R.layout.simple_spinner_item, donorSpinnerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinner.setSelection(0,true);
                donorSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void requestDirection(LatLng source, LatLng des, final GoogleMap mMap)
    {


        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source.latitude+","+source.longitude+"" +
                "&destination="+des.latitude+","+des.longitude+"&key="+"AIzaSyBqrewZNjkvVx28YqwDe718zPxzwV9zBkE";
        System.out.println("The url is ="+url);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("The response is ="+response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                            int count=jsonArray.length();
                            String[] polyline_array=new String[count];
                            JSONObject jsonObject2;

                            for(int i=0;i<count;i++)
                            {
                                jsonObject2=jsonArray.getJSONObject(i);
                                String polygone=jsonObject2.getJSONObject("polyline").getString("points");
                                polyline_array[i]=polygone;
                            }
                            int count2=polyline_array.length;

                            for(int i=0;i<count2;i++)
                            {
                                PolylineOptions options2=new PolylineOptions();
                                options2.color(Color.BLUE);
                                options2.width(10);
                                options2.addAll(PolyUtil.decode(polyline_array[i]));
                                mMap.addPolyline(options2);
                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("The error is "+error);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}
