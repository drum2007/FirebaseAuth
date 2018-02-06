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
import android.widget.ImageView;

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
    private Button menuSelectStore;

    private DatabaseReference databaseReference;

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

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
//        bt3.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

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
            intent.putExtra("id","2XHfcqzJKOaXQ2yd5OuomM60ek33");
            startActivity(intent);
        }
        if (v == menuSelectStore) {
            startActivity(new Intent(this, SelectStoreActivity.class));
        }
    }

}
