package com.beam.firebaseauth;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoreProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText openTime;
    private EditText closeTime;
    private EditText storeCapacity;
    private Button btnSaveInfo;
    private TextView tvStoreEmail;
    private Button btnLogout;
    private Button menuSelectStore;

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

        tvStoreEmail = findViewById(R.id.tvStoreEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        openTime = findViewById(R.id.openTime);
        closeTime = findViewById(R.id.closeTime);
        storeCapacity = findViewById(R.id.storeCapacity);
        menuSelectStore = findViewById(R.id.menuSelectStore);

        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        btnLogout = findViewById(R.id.btnLogout);
        tvStoreEmail.setText("Welcome " + user.getEmail());

        btnSaveInfo.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
    }

    //menubar
    private void initInstance() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                StoreProfileActivity.this,
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
        if (v == btnLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == btnSaveInfo) {
            saveStoreInformation();
        }
        if (v == menuSelectStore) {
            startActivity(new Intent(this, SelectStoreActivity.class));
        }
    }

    private void saveStoreInformation() {
        String storeName = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String open = openTime.getText().toString().trim();
        String close = closeTime.getText().toString().trim();
        String capacity = storeCapacity.getText().toString().trim();

        if (TextUtils.isEmpty(storeName)) {
            Toast.makeText(this, "Could not save information, Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Could not save information, Address is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(open)) {
            Toast.makeText(this, "Could not save information, Open time is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(close)) {
            Toast.makeText(this, "Could not save information, Close time is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(capacity)) {
            Toast.makeText(this, "Could not save information, Capacity is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StoreInformation storeInformation = new StoreInformation(storeName, phoneNumber, open, close, capacity);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").setValue(storeInformation);

        Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();
    }
}
