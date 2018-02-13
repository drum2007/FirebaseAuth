package com.beam.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL;

public class StoreEditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PICK_IMAGE = 123;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText openTime;
    private EditText closeTime;
    private EditText storeCapacity;
    private EditText aboutStore;
    private Button btnSaveInfo;
    private TextView tvStoreEmail;
    private TextView tvPlace;
    private TextView tvAddress;
    private TextView tvLatlng;
    private Button btnLogout;
    private Button menuSelectStore, menuProfile;
    private Button btnChooseImage;
    private Button btnChooseLocation;
    private ImageView imageView;
    private int[] arrayDay = {0, 0, 0, 0, 0, 0, 0};

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    private Uri filepath, downloadUrl;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit_profile);
        setTitle("Store Profile");

        initInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        assert user != null;
        tvStoreEmail.setText(String.format("Welcome %s", user.getEmail()));

//        String url = "https://firebasestorage.googleapis.com/v0/b/fir-auth-1d7cd.appspot.com/o/2XHfcqzJKOaXQ2yd5OuomM60ek33%2Fprofile.jpg?alt=media&token=8720c2b4-9b3d-4a90-9493-de3ee9ea39b6";
//
//        Glide.with(getApplicationContext()).load(url).into(imageView);

        StorageReference ref = storageReference.child(user.getUid() + "/profile.jpg");

        btnSaveInfo.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
        btnChooseLocation.setOnClickListener(this);
    }

    private void initInstance() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                StoreEditProfileActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAddress = findViewById(R.id.tvAddress);
        tvLatlng = findViewById(R.id.tvLatlng);
        tvPlace = findViewById(R.id.tvPlace);
        tvStoreEmail = findViewById(R.id.tvStoreEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        openTime = findViewById(R.id.openTime);
        closeTime = findViewById(R.id.closeTime);
        storeCapacity = findViewById(R.id.storeCapacity);
        aboutStore = findViewById(R.id.editTextAboutStore);
        menuSelectStore = findViewById(R.id.menuSelectStore);
        menuProfile = findViewById(R.id.menuProfile);

        imageView = findViewById(R.id.imageView);

        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        btnLogout = findViewById(R.id.btnLogout);

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
        if (v == btnChooseImage) {
            showFileChooser();
        }
        if (v == btnChooseLocation) {
            PlacePicker.IntentBuilder mapBuilder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(mapBuilder.build(this), PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
        if (v == menuProfile){
            startActivity(new Intent(this, StoreProfileActivity.class));
        }
    }

    //select image
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;

        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (requestCode == 1) { //Result_OK ?
                Place place = PlacePicker.getPlace(this, data);
                String placeName = String.format("Place: %s", place.getAddress());
                tvPlace.setText(place.getName());
                tvAddress.setText(place.getAddress());
                tvLatlng.setText(place.getLatLng().toString());

            }
        }
    }

    //upload selected image
    private boolean uploadFile() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (filepath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            assert user != null;
            StorageReference storePicRef = storageReference.child(user.getUid() + "/profile.jpg");

            storePicRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //get percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "% Uploaded...");
                        }
                    });
            return true;
        } else {
            //display error toast
            Toast.makeText(getApplicationContext(), "Please choose Store's profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveStoreInformation() {
        String storeName = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String open = openTime.getText().toString().trim();
        String close = closeTime.getText().toString().trim();
        String capacity = storeCapacity.getText().toString().trim();
        String AboutStore = aboutStore.getText().toString().trim();
        String place = tvPlace.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String latlng = tvLatlng.getText().toString().substring(10,tvLatlng.getText().length()-1);

        if (TextUtils.isEmpty(storeName)) {
            Toast.makeText(this, "Please enter store's name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please enter store's phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(open)) {
            Toast.makeText(this, "Please enter open time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(close)) {
            Toast.makeText(this, "Please enter close time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(capacity)) {
            Toast.makeText(this, "Please enter store's capacity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(place) || TextUtils.isEmpty(address) ||TextUtils.isEmpty(latlng)) {
            Toast.makeText(this, "Please select store's location", Toast.LENGTH_SHORT).show();
            return;
        }


        StoreInformation storeInformation = new StoreInformation(storeName, phoneNumber, open, close, capacity, AboutStore, place, address, latlng);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        assert user != null;
        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").setValue(storeInformation);

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Sunday: ").setValue(arrayDay[0] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Monday: ").setValue(arrayDay[1] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Tuesday: ").setValue(arrayDay[2] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Wednesday: ").setValue(arrayDay[3] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Thursday: ").setValue(arrayDay[4] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Friday: ").setValue(arrayDay[5] + "");

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").child("BusinessDay").child("Saturday: ").setValue(arrayDay[6] + "");

        if (uploadFile()) {
            Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();
        }
        if (!uploadFile()) {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show();
        }

    }

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

