package com.beam.firebaseauth;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnUserRegister;

    private final String TAG = "Log";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnUserRegister = findViewById(R.id.btnUserRegister);

        btnLogin.setOnClickListener(this);
        btnUserRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

        }
        if (v == btnUserRegister) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User ...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    Toast.makeText(MainActivity.this,"Register successful", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Log.e(TAG, "onComplete: Failed =" + task.getException().getMessage());
                    Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
