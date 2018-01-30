package com.beam.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUserEmail;
    private Button btnSaveInfo;
    private Button btnLogout;

    private EditText editTextName;
    private EditText editTextPhoneNumber;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        tvUserEmail = findViewById(R.id.tvUserEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextName = findViewById(R.id.editTextName);

        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        btnLogout = findViewById(R.id.btnLogout);

        tvUserEmail.setText("Welcome " + user.getEmail());

        btnSaveInfo.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Could not save information, Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Could not save information, Phone number is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInformation userInformation = new UserInformation(name, phoneNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("User").child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (v == btnSaveInfo) {
            saveUserInformation();
        }
    }
}
