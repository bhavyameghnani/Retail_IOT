package com.example.lenovo.retail_iot;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.ZipOutputStream;

public class Home extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    public boolean closeview = false;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userName, userEmail;
    DatabaseReference dbuser, dbtoken;
    RecyclerView recyclerView;
    ImageView userImg;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Zone, ZoneViewHolder> adapter;

    private static final int ITEM_TO_LOAD = 30;
    private int mCurrentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.feature_req_toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_bookList);
        mSwipeRefreshLayout = findViewById(R.id.swip);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();

                        if(menuItem.getItemId() == R.id.logout){
                            dbtoken = FirebaseDatabase.getInstance().getReference().child("user_details")
                                    .child(auth.getUid()).child("token");
                            dbtoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            auth.signOut();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user == null) {
                                // user auth state is changed - user is null
                                // launch login activity
                                Intent intent = new Intent(Home.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                            return true;
                        }
                        /*if(menuItem.getItemId() == R.id.show_maps){
                            Intent intent = new Intent(Home.this, QR.class);
                            startActivity(intent);
                        }*/

                        if(menuItem.getItemId() == R.id.accSetting){
                            Intent intent = new Intent(Home.this, AccountSetting.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.createambulance){
                            Intent intent = new Intent(Home.this, Home.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.show_maps) {
                            Intent intent = new Intent(Home.this, MapsActivity.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.ambulance){
                            Intent intent = new Intent(Home.this, CrowdDetection.class);
                            startActivity(intent);
                        }

                        if(menuItem.getItemId() == R.id.createZone){
                            Intent intent = new Intent(Home.this, CreateZone.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.fineOfficer){
                            Intent intent = new Intent(Home.this, Home1.class);
                            startActivity(intent);
                        }
                        if (menuItem.getItemId() == R.id.viewFine) {

                            Intent intent = new Intent(Home.this, Home1.class);

                            startActivity(intent);
                        }
                        if(menuItem.getItemId()==R.id.finerates){
                            Intent intent = new Intent(Home.this, Scan.class);
                            startActivity(intent);
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        navigationView.setItemIconTintList(null);
        userName = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.head_email);
        userImg = navigationView.getHeaderView(0).findViewById(R.id.user_image);

        Log.d("auth_id",auth.getUid());
        dbuser = FirebaseDatabase.getInstance().getReference().child("user_details").child(auth.getUid());
        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String tkey = snapshot.getKey();
                    Log.d("current",tkey);
                    if(tkey.equals("userName")){
                        userName.setText(snapshot.getValue(String.class));
                        Log.d("current",snapshot.getValue(String.class));
                    }
                    if(tkey.equals("userEmail")){
                        userEmail.setText(snapshot.getValue(String.class));
                        Log.d("current",snapshot.getValue(String.class));
                    }
                    if(tkey.equals("userType")){
                        Log.d("current",snapshot.getValue(String.class));
                        if(snapshot.getValue(String.class).equals("commuters")){
                            navigationView.getMenu().findItem(R.id.fineOfficer).setVisible(true);
                            navigationView.getMenu().findItem(R.id.createZone).setVisible(false);
                            navigationView.getMenu().findItem(R.id.finerates).setVisible(true);
                        } if(snapshot.getValue(String.class).equals("officer")){
                            navigationView.getMenu().findItem(R.id.fineOfficer).setVisible(true);
                            navigationView.getMenu().findItem(R.id.createZone).setVisible(true);
                            navigationView.getMenu().findItem(R.id.createZone).setVisible(true);
                            navigationView.getMenu().findItem(R.id.finerates).setVisible(false);
                        }
                        if(snapshot.getValue(String.class).equals("ambulance")){
                            navigationView.getMenu().findItem(R.id.createambulance).setVisible(true);
                            navigationView.getMenu().findItem(R.id.createZone).setVisible(true);
                        }
                    }
                    if(tkey.equals("userImgUrl")){
                        Glide.with(getApplicationContext()).load(snapshot.getValue(String.class)).into(userImg);
                    }

                }
                /*User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName().toString());
                userEmail.setText(user.getUserEmail().toString());
                if(user.getUserType().equals("commuters")){
                    navigationView.getMenu().findItem(R.id.viewFine).setVisible(true);
                }else if(user.getUserType().equals("officer")){
                    navigationView.getMenu().findItem(R.id.fineOfficer).setVisible(true);
                }
                if(!user.getUserImgUrl().equals("")){
                    Glide.with(getApplicationContext()).load(user.getUserImgUrl()).into(userImg);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loaddata();
        refreshPage();
    }

    public void loaddata(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Zones");
        dbref.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<Zone, ZoneViewHolder>
                (Zone.class, R.layout.zone_list_item,
                        ZoneViewHolder.class,
                        dbref.limitToLast(mCurrentPage * ITEM_TO_LOAD).orderByChild("average_rating")) {

            public void populateViewHolder(final ZoneViewHolder zoneViewHolder,
                                           final Zone zone, final int position) {
                String key = this.getRef(position).getKey().toString();
                Log.d("key",key);
                Log.d("Position", String.valueOf(position));
                zoneViewHolder.setCounter(zone.getZoneStatusUpVote(),zone.getZoneStatusDownVote(),zone.getSolvedCount());
                zoneViewHolder.setKey(key);
                zoneViewHolder.setContext(getApplicationContext(),zone.getZoneID());
                zoneViewHolder.setTitle(zone.getZoneTitle());
                zoneViewHolder.setUserAuthID(zone.getZoneID());
                zoneViewHolder.setInfo(zone.getZoneData());
                zoneViewHolder.setSolution(zone.getZoneSolution());
                zoneViewHolder.setImage(zone.getZoneImage());
            }
        };
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refreshPage(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage ++;

                loaddata();
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }, 200);


            }
        });
    }

    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (closeview) {
            closeview = false;
            finish();
            startActivity(getIntent());

        } else {
            super.onBackPressed();
        }
    }

    public void closeview(Boolean value) {
        closeview = value;
    }
}