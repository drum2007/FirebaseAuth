package com.beam.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StoreRegistActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ERROR";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnStoreRegister;
    private TextView tvSignIn;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_regist);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //go to profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), StoreProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnStoreRegister = findViewById(R.id.btnStoreRegister);
        tvSignIn = findViewById(R.id.tvSignIn);

        btnStoreRegister.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnStoreRegister){
            registerStore();
        }
        if (v == tvSignIn){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerStore() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering Store ...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), StoreProfileActivity.class));
                    Toast.makeText(StoreRegistActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Log.e(TAG, "onComplete: Failed =" + task.getException().getMessage());
                    Toast.makeText(StoreRegistActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
