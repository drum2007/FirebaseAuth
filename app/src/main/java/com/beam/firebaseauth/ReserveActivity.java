package com.beam.firebaseauth;

import android.app.DatePickerDialog;
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
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class ReserveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText editTextNumber;
    private Button btnDate;
    private TextView tvDate;
    private Button btnConfirm;
    private Button menuSelectStore;
    private TextView tvStoreName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public String Year;
    public String Month;
    public String DayOfMonth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        setTitle("Reserve");

        initInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        editTextNumber = findViewById(R.id.editTextNumber);
        btnDate = findViewById(R.id.btnDate);
        tvDate = findViewById(R.id.tvDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        menuSelectStore = findViewById(R.id.menuSelectStore);
        tvStoreName = findViewById(R.id.tvStoreName);

        btnDate.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
    }
    //menubar
    private void initInstance() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                ReserveActivity.this,
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(c.getTime());

        tvDate.setText(currentDate);

        Year = Integer.toString(year);
        Month = Integer.toString(month);
        DayOfMonth = Integer.toString(dayOfMonth);
    }

    @Override
    public void onClick(View v) {
        if (v == btnDate) {
            DatePickerFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "Date");
        }
        if (v == btnConfirm) {
            saveConfirmation();
        }
        if (v == menuSelectStore) {
            startActivity(new Intent(this, SelectStoreActivity.class));
        }
    }

    //must get store uid from selectstoreactivity to reserve under it
    private void saveConfirmation() {
        String numberOfPeople = editTextNumber.getText().toString().trim();
        String year = Year;
        String dayOfMonth = DayOfMonth;

        if (TextUtils.isEmpty(numberOfPeople)) {
            Toast.makeText(this, "Please enter number of people", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(dayOfMonth)) {
            Toast.makeText(this, "Please enter Date", Toast.LENGTH_SHORT).show();
            return;
        }

        String month = calculateRealMonth(Month);

        if (TextUtils.isEmpty(month)) {
            Toast.makeText(this, "Please enter Date", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        Confirmation confirmation = new Confirmation(numberOfPeople, user.getUid());

        //must save under store uid
        databaseReference.child("Store").child("2XHfcqzJKOaXQ2yd5OuomM60ek33").child("ReserveInfo")
                .child("Year: " + year).child("Month: " + month).child("Date: " + dayOfMonth).setValue(confirmation);

        Toast.makeText(this, "Reservation Saved", Toast.LENGTH_SHORT).show();

    }

    private String calculateRealMonth(String month) {
        String realMonth = month;

        switch (realMonth) {
            case "0":
                realMonth = "January";
                break;
            case "1":
                realMonth = "February";
                break;
            case "2":
                realMonth = "March";
                break;
            case "3":
                realMonth = "April";
                break;
            case "4":
                realMonth = "May";
                break;
            case "5":
                realMonth = "June";
                break;
            case "6":
                realMonth = "July";
                break;
            case "7":
                realMonth = "August";
                break;
            case "8":
                realMonth = "September";
                break;
            case "9":
                realMonth = "October";
                break;
            case "10":
                realMonth = "November";
                break;
            case "11":
                realMonth = "December";
                break;
            default:
                realMonth = "Invalid month";
                break;
        }

        return realMonth;
    }
}
