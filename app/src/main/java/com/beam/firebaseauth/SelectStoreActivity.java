package com.beam.firebaseauth;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    ListView listview;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        setTitle("SelectStore");

        initInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        menuSelectStore = findViewById(R.id.menuSelectStore);
        menuProfile = findViewById(R.id.menuProfile);
        menuProfile.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);

        listview = findViewById(R.id.listview);
        DatabaseReference storeRef = databaseReference.child("Store");
        //ArrayAdapter mAdapter = new FirebaseList<StoreInformation>(this, StoreInformation.class, R.layout.listview_store, listview);


    }

    //Set store need image//
    private void setStore(String storeID, final TextView name, final TextView day, TextView place) {
        Query storeQuery = databaseReference.child("Store").child(storeID).child("StoreInfo");
        storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

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
