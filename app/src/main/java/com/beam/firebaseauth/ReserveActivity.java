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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

public class ReserveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {//, AdapterView.OnItemClickListener { //OnMapReadyCallback

    private static final String[] paths = {"item1", "item2", "item3"};
    private int checkState = 0;

    private EditText editTextNumber, reserveTime;
    private Button btnDate;
    private TextView tvDate;
    private Button btnConfirm;
    private Button menuSelectStore;
    private Button menuProfile;

    private ImageView imStore;
    private TextView tvStoreName;
    private TextView tvStoreDay;
    private TextView tvStoreTime;
    private TextView tvStoreAddress;
    private TextView tvStoreAbout;
    private TextView tvStorePhone;

    private GoogleMap mMap;

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
        menuProfile = findViewById(R.id.menuProfile);
        tvStoreName = findViewById(R.id.tvStoreName);
        reserveTime = findViewById(R.id.reserveTime);

        imStore = findViewById(R.id.imStore);
        tvStoreDay = findViewById(R.id.tvStoreDay);
        tvStoreTime = findViewById(R.id.tvStoreTime);
        tvStoreAddress = findViewById(R.id.tvStoreAddress);
        tvStoreAbout = findViewById(R.id.tvStoreAbout);
        tvStorePhone = findViewById(R.id.tvStorePhone);

        //Spinner spinner = findViewById(R.id.spinner1);
        //ArrayAdapter<String>adapter = new ArrayAdapter<String>(ReserveActivity.this, R.layout.support_simple_spinner_dropdown_item,paths);

        //adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);

        btnDate.setOnClickListener(this);
        //spinner.setOnItemClickListener(this);
        btnConfirm.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
        menuProfile.setOnClickListener(this);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        // Intent Data
        String storeID = getIntent().getStringExtra("id");
        Query storeQuery = databaseReference.child("Store").child(storeID).child("StoreInfo");
        storeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storeName = dataSnapshot.child("storeName").getValue(String.class);
                    String storeOpenTime = dataSnapshot.child("openTime").getValue(String.class);
                    String storeCloseTime = dataSnapshot.child("closeTime").getValue(String.class);
                    String storeAddress = dataSnapshot.child("address").getValue(String.class);
                    String storeAbout = dataSnapshot.child("aboutStore").getValue(String.class);
                    String storePhone = dataSnapshot.child("phoneNumber").getValue(String.class);
                    // String storeDay = dataSnapshot.child("day").getValue(String.class);
                    tvStoreName.setText(storeName);
                    tvStoreTime.setText("Time : " + storeOpenTime + " - " + storeCloseTime);
                    tvStoreAddress.setText(storeAddress);
                    tvStoreAbout.setText(storeAbout);
                    tvStorePhone.setText(storePhone);
                    // tvStoreDay.setText("Day : " + storeDay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
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
            if (checkState == 1) {
                startActivity(new Intent(this, UserProfileActivity.class));
            }

        }
        if (v == menuProfile) {
            Query userQuery = databaseReference.child("User").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        finish();
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
                        finish();
                        startActivity(new Intent(getApplicationContext(), StoreProfileActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Could not sign in, please check your username and password", Toast.LENGTH_LONG).show();
        }
        if (v == menuSelectStore) {
            startActivity(new Intent(this, SelectStoreActivity.class));
        }
    }


    private void saveConfirmation() {
        String numberOfPeople = editTextNumber.getText().toString().trim();
        String year = Year;
        String dayOfMonth = DayOfMonth;
        String reservationTime = reserveTime.getText().toString().trim();

        String storeID = getIntent().getStringExtra("id");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();

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

        if (TextUtils.isEmpty(reservationTime)) {
            Toast.makeText(this, "Please enter Reservation time", Toast.LENGTH_SHORT).show();
            return;
        }

        Confirmation confirmForStore = new Confirmation(numberOfPeople, userID, reservationTime);
        Confirmation confirmForUser = new Confirmation(numberOfPeople, storeID, reservationTime);

        //save info for store
        databaseReference.child("Store").child(storeID).child("ReserveInfo")
                .child("Year: " + year).child("Month: " + month).child("Date: " + dayOfMonth).setValue(confirmForStore);

        //save info for user
        databaseReference.child("User").child(userID).child("ReserveInfo")
                .child("Year: " + year).child("Month: " + month).child("Date: " + dayOfMonth).setValue(confirmForUser);

        Toast.makeText(this, "Reservation Saved", Toast.LENGTH_SHORT).show();

        checkState = 1;

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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        switch (position){
//            case 0:
//
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//        }
//    }
//    //Map //
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng CMU_Clock_Tower = new LatLng(18.796488, 98.953402);
//        mMap.addMarker(new MarkerOptions().position(CMU_Clock_Tower).title("Marker in CMU").snippet("Hello CMU"));
//
//            LatLng myDome = new LatLng(18.788302, 98.955049);
//            mMap.addMarker(new MarkerOptions().position(myDome).title("My Dome").snippet("Hello my Dome"));
//
//            LatLng center = new LatLng( 18.79, 98.954);
//
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//
//            @Override
//            // Return null here, so that getInfoContents() is called next.
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                // Inflate the layouts for the info window, title and snippet.
//                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
//
//
//                TextView title = ((TextView) infoWindow.findViewById(R.id.textViewName));
//                title.setText(marker.getTitle());
//
//
//                TextView snippet = ((TextView) infoWindow.findViewById(R.id.textViewSnippet));
//                snippet.setText(marker.getSnippet());
//
//                ImageView imageView = (ImageView) infoWindow.findViewById(R.id.imageView);
//                imageView.setImageResource(R.drawable.ic_city);
//                if ("My Home".equals(marker.getTitle())) {
//                    imageView.setImageResource(R.drawable.ic_home);
//                }
//
//                return infoWindow;
//            }
//        });
//    }
//    //Map//
}
