package com.beam.firebaseauth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectStoreActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt1;
    private Button bt2;
    private ImageView imStore;
    private TextView tvStoreName;
    private TextView tvStoreDay;
    private TextView tvStoreTime;
    private TextView tvStorePhone;
    private TextView tvStoreAddress;
    private TextView tvStoreAbout;
    private ImageView imStore2;
    private TextView tvStoreName2;
    private TextView tvStoreDay2;
    private TextView tvStoreTime2;
    private TextView tvStorePhone2;
    private TextView tvStoreAddress2;
    private TextView tvStoreAbout2;

    private Button menuSelectStore;
    private Button menuProfile;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        setTitle("SelectStore");
        initInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bt2 = findViewById(R.id.bt2);
        bt1 = findViewById(R.id.bt1);
        imStore = findViewById(R.id.imStore);
        imStore2 = findViewById(R.id.imStore2);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreName2 = findViewById(R.id.tvStoreName2);
        tvStoreDay = findViewById(R.id.tvStoreDay);
        tvStoreDay2 = findViewById(R.id.tvStoreDay2);
        tvStoreTime = findViewById(R.id.tvStoreTime);
        tvStoreTime2 = findViewById(R.id.tvStoreTime2);
        tvStorePhone = findViewById(R.id.tvStorePhone);
        tvStorePhone2 = findViewById(R.id.tvStorePhone2);
        tvStoreAddress = findViewById(R.id.tvStoreAddress);
        tvStoreAddress2 = findViewById(R.id.tvStoreAddress2);
        tvStoreAbout = findViewById(R.id.tvStoreAbout);
        tvStoreAbout2 = findViewById(R.id.tvStoreAbout2);
        menuSelectStore = findViewById(R.id.menuSelectStore);
        menuProfile = findViewById(R.id.menuProfile);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
        setStore("ocyRIrdctWYl5sib2xNyHVt7cMK2", imStore, tvStoreName, tvStoreDay, tvStoreTime, tvStoreAddress, tvStoreAbout, tvStorePhone);
    }


    //Set store//
    private void setStore(final String storeID, ImageView image, final TextView name, final TextView day, final TextView time,
                          final TextView address, final TextView about, final TextView phone) {
        final Query storeQuery = databaseReference.child("Store").child(storeID).child("StoreInfo");
        storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name.setText("Name" + dataSnapshot.child("storeName").getValue(String.class));
                    time.setText("Time" + dataSnapshot.child("openTime").getValue(String.class) + " - " + dataSnapshot.child("closeTime").getValue(String.class));
                    address.setText("Address : " + dataSnapshot.child("address").getValue(String.class));
                    phone.setText("Phone : " + dataSnapshot.child("phoneNumber").getValue(String.class));
                    about.setText("About : " + dataSnapshot.child("aboutStore").getValue(String.class));

//                    Query busidayQuery = databaseReference.child("Store").child(storeID).child("StoreInfo").child("BusinessDay");
//                    busidayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                String fri = "";
//                                String mon = "";
//                                String Sat = "";
//                                String Sun = "";
//                                String Wed = "";
//                                String Thur = "";
//                                String Tues = "";
//                                if (dataSnapshot.child("Friday").getValue(String.class).equals("1")) {
//                                    fri = " Fr";
//                                }
//                                if (dataSnapshot.child("Monday").getValue(String.class).equals("1")) {
//                                    mon = " M";
//                                }
//                                if (dataSnapshot.child("Saturday").getValue(String.class).equals("1")) {
//                                    Sat = " Sa";
//                                }
//                                if (dataSnapshot.child("Sunday").getValue(String.class).equals("1")) {
//                                    Sun = " Su";
//                                }
//                                if (dataSnapshot.child("Thursday").getValue(String.class).equals("1")) {
//                                    Thur = " Th";
//                                }
//                                if (dataSnapshot.child("Tuesday").getValue(String.class).equals("1")) {
//                                    Tues = " Tu";
//                                }
//                                if (dataSnapshot.child("Wednesday").getValue(String.class).equals("1")) {
//                                    Wed = " We";
//                                }
//                                String sday = ("Day :" + Sun + mon + Tues + Wed + Thur + fri + Sat);
//                                day.setText(sday);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    //menubar

    private void initInstance() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                SelectStoreActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    //menubar

    @Override
    public void onClick(View v) {

        if (v == bt1) {
            Intent intent = new Intent(this, ReserveActivity.class);
            intent.putExtra("id", "ocyRIrdctWYl5sib2xNyHVt7cMK2");
            startActivity(intent);
        }
        if (v == bt2) {
            Intent intent = new Intent(this, ReserveActivity.class);
            intent.putExtra("id", "zWbUSFxT8HYu4ClL0jAj2C2v4dC2");
            startActivity(intent);
        }
        if (v == menuProfile) {
            firebaseAuth = FirebaseAuth.getInstance();

            //user
            Query userQuery = databaseReference.child("User").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });

            //store
            Query storeQuery = databaseReference.child("Store").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());
            storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        startActivity(new Intent(getApplicationContext(), StoreEditProfileActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
        }
        if (v == menuSelectStore) {
            finish();
            startActivity(new Intent(this, SelectStoreActivity.class));
        }
    }

}
