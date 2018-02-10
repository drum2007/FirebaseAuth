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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StoreProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 123;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText openTime;
    private EditText closeTime;
    private EditText storeCapacity;
    private Button btnSaveInfo;
    private TextView tvStoreEmail;
    private Button btnLogout;
    private Button menuSelectStore;
    private Button btnChooseImage;
    private ImageView imageView;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    private Uri filepath;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);
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

        tvStoreEmail = findViewById(R.id.tvStoreEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        openTime = findViewById(R.id.openTime);
        closeTime = findViewById(R.id.closeTime);
        storeCapacity = findViewById(R.id.storeCapacity);
        menuSelectStore = findViewById(R.id.menuSelectStore);
        imageView = findViewById(R.id.imageView);

        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        btnLogout = findViewById(R.id.btnLogout);
        tvStoreEmail.setText(String.format("Welcome %s", user.getEmail()));

        btnSaveInfo.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        menuSelectStore.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
    }

    private void initInstance() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                StoreProfileActivity.this,
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
        final StorageReference storePicRef = FirebaseStorage.getInstance().getReference(user.getUid());

        storePicRef.child(user.getUid() + "/profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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

            StorageReference storePicRef = storageReference.child(user.getUid() + "/profile.jpg");

            storePicRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
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

        StoreInformation storeInformation = new StoreInformation(storeName, phoneNumber, open, close, capacity);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("Store").child(user.getUid()).child("StoreInfo").setValue(storeInformation);

        if (uploadFile()) {
            Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();
        }
        if (!uploadFile()) {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show();
        }

    }

}

