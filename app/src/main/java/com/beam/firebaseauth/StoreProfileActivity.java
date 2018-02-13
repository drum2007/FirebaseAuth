package com.beam.firebaseauth;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StoreProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private TextView tvUserName, reserveDate, reserveTime, number;
    private TextView tvStoreEmail, tvStoreName, tvStorePhoneNumber, tvStoreCap, tvAboutStore, openTime, closeTime;
    private Button btnEditProfile, btnLogout;
    private Button menuSelectStore;

    private int[] arrayDay = {0, 0, 0, 0, 0, 0, 0};

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);
        setTitle("Store Profile");

        initInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        assert user != null;
        tvStoreEmail.setText(String.format("Welcome %s", user.getEmail()));

        Query userQuery = databaseReference.child("Store").child(user.getUid());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storeName = dataSnapshot.child("StoreInfo").child("storeName").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("StoreInfo").child("phoneNumber").getValue(String.class);
                    String capacity = dataSnapshot.child("StoreInfo").child("capacity").getValue(String.class);
                    String aboutStore = dataSnapshot.child("StoreInfo").child("aboutStore").getValue(String.class);
                    String open = dataSnapshot.child("StoreInfo").child("openTime").getValue(String.class);
                    String close = dataSnapshot.child("StoreInfo").child("closeTime").getValue(String.class);

                    tvStoreName.setText(storeName);
                    tvStorePhoneNumber.setText(phoneNumber);
                    tvStoreCap.setText(capacity);
                    tvAboutStore.setText(aboutStore);
                    openTime.setText(open);
                    closeTime.setText(close);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        reserveInfo();

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        btnEditProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void reserveInfo() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        assert user != null;
        Query reserveInfo = databaseReference.child("Store").child(user.getUid()).child("ReserveInfo")
                .child("Year: 2018").child("Month: February").child("Date: 14");

        reserveInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userID = dataSnapshot.child("ID").getValue(String.class);
                    String numberOfPeople = dataSnapshot.child("numberOfPeople").getValue(String.class);
                    String reservetime = dataSnapshot.child("reserveTime").getValue(String.class);

                    number.setText(numberOfPeople);
                    reserveTime.setText(reservetime);
                    reserveDate.setText("Date: 14");

                    //store = storeID;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });


//        Query checkStore = databaseReference.child("Store").orderByKey().equalTo(store);
//        checkStore.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String storeName = dataSnapshot.child("StoreInfo").child("storeName").getValue(String.class);
//                tvStoreName.setText(storeName);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (v == btnEditProfile) {
            startActivity(new Intent(this, StoreEditProfileActivity.class));
        }
        if (v == btnLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
//        if (v == menuSelectStore) {
//            startActivity(new Intent(this, SelectStoreActivity.class));
//        }
    }

    private void initInstance() {
//        drawerLayout = findViewById(R.id.drawerLayout);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(
//                StoreProfileActivity.this,
//                drawerLayout,
//                R.string.open_drawer,
//                R.string.close_drawer
//        );
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuSelectStore = findViewById(R.id.menuSelectStore);
        imageView = findViewById(R.id.imageView);
        tvStoreEmail = findViewById(R.id.tvStoreEmail);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStorePhoneNumber = findViewById(R.id.tvStorePhoneNumber);
        tvStoreCap = findViewById(R.id.tvStoreCap);
        tvAboutStore = findViewById(R.id.tvAboutStore);
        openTime = findViewById(R.id.openTime);
        closeTime = findViewById(R.id.closeTime);
        tvUserName = findViewById(R.id.tvUserName);
        reserveDate = findViewById(R.id.reserveDate);
        reserveTime = findViewById(R.id.reserveTime);
        number = findViewById(R.id.number);
    }

//    @Override
//    public void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        actionBarDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        actionBarDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (actionBarDrawerToggle.onOptionsItemSelected(item))
//            return true;
//        return super.onOptionsItemSelected(item);
//    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.sunday:
                if (checked) {
                    arrayDay[0] = 1;
                } else
                    arrayDay[0] = 0;
                break;

            case R.id.monday:
                if (checked) {
                    arrayDay[1] = 1;
                } else
                    arrayDay[1] = 0;
                break;

            case R.id.tuesday:
                if (checked) {
                    arrayDay[2] = 1;
                } else
                    arrayDay[2] = 0;
                break;

            case R.id.wednesday:
                if (checked) {
                    arrayDay[3] = 1;
                } else
                    arrayDay[3] = 0;
                break;

            case R.id.thursday:
                if (checked) {
                    arrayDay[4] = 1;
                } else
                    arrayDay[4] = 0;
                break;

            case R.id.friday:
                if (checked) {
                    arrayDay[5] = 1;
                } else
                    arrayDay[5] = 0;
                break;

            case R.id.saturday:
                if (checked) {
                    arrayDay[6] = 1;
                } else
                    arrayDay[6] = 0;
                break;
        }

    }
}
