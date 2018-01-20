package com.beam.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StoreProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvStoreEmail;
    private Button btnLogout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        tvStoreEmail = findViewById(R.id.tvStoreEmail);
        btnLogout = findViewById(R.id.btnLogout);

        tvStoreEmail.setText("Welcome " + user.getEmail());

        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
