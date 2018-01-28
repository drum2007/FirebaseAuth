package com.beam.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnSignIn;
    private Button btnUserSignUp;
    private Button btnStoreSignUp;
    private Button btnStoreSelect;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //now only login to user profile must separate user and store
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnUserSignUp = findViewById(R.id.btnUserSignUp);
        btnStoreSignUp = findViewById(R.id.btnStoreSignUp);

        btnStoreSelect = findViewById(R.id.btnStoreSelect);

        btnSignIn.setOnClickListener(this);
        btnUserSignUp.setOnClickListener(this);
        btnStoreSignUp.setOnClickListener(this);
        btnStoreSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignIn) {
            //now only login to user profile must separate user and store
            userLogin();
        }
        if (v == btnUserSignUp) {
            finish();
            startActivity(new Intent(this, UserRegistActivity.class));
        }
        if (v == btnStoreSignUp){
            finish();
            startActivity(new Intent(this, StoreRegistActivity.class));
        }
        if (v == btnStoreSelect){
            finish();
            startActivity(new Intent(this,SelectStoreActivity.class));
        }
    }

    //now only login to user profile must separate user and store
    private void userLogin() {
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

        progressDialog.setMessage("Logging in now, Please wait ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    //start profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                }
            }
        });
    }
}
