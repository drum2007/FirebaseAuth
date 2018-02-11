package com.beam.firebaseauth;

import android.app.Application;
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
    //    private Button bt3;
    private ImageView im2;
    private TextView tv2name;
    private TextView tv2day;
    private TextView tv2place;

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

        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
//        bt3 = findViewById(R.id.bt3);
        menuSelectStore = findViewById(R.id.menuSelectStore);
        menuProfile = findViewById(R.id.menuProfile);
        im2 = findViewById(R.id.im2);
        tv2day = findViewById(R.id.tv2day);
        tv2name = findViewById(R.id.tv2name);
        tv2place = findViewById(R.id.tv2place);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
//        bt3.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
        setStore("zWbUSFxT8HYu4ClL0jAj2C2v4dC2", im2, tv2name, tv2day, tv2place);

    }

    //Set store//
    private void setStore(String storeID, ImageView image, final TextView name, final TextView day, TextView place) {
        Query storeQuery = databaseReference.child("Store").child(storeID).child("StoreInfo");
        storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("qqqqqqqqqqqqqqqq");
                    System.out.println(dataSnapshot.child("storeName").getValue(String.class));
                    name.setText(dataSnapshot.child("storeName").getValue(String.class));
                    day.setText(dataSnapshot.child("openTime").getValue(String.class) + " - " + dataSnapshot.child("closeTime").getValue(String.class));
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
            Query storeQuery = databaseReference.child("Store").orderByChild("closeTime");
            storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (v == bt2) {
            Intent intent = new Intent(this, ReserveActivity.class);
            intent.putExtra("id", "zWbUSFxT8HYu4ClL0jAj2C2v4dC2");
            startActivity(intent);
        }
        if (v == menuProfile){
            firebaseAuth = FirebaseAuth.getInstance();
            Query userQuery = databaseReference.child("User").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
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
