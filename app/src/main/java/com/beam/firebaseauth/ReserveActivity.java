package com.beam.firebaseauth;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public int Year, Month, DayOfMonth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextNumber = findViewById(R.id.editTextNumber);
        btnDate = findViewById(R.id.btnDate);
        tvDate = findViewById(R.id.tvDate);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnDate.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(c.getTime());

        tvDate.setText(currentDate);

        Year = year;
        Month = month;
        DayOfMonth = dayOfMonth;
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
    }

    private void saveConfirmation() {
        String numberOfPeople = editTextNumber.getText().toString().trim();
        String year = String.valueOf(Year);
        String month = String.valueOf(Month);
        String dayOfMonth = String.valueOf(DayOfMonth);

        if (TextUtils.isEmpty(numberOfPeople)) {
            Toast.makeText(this, "Please enter number of people", Toast.LENGTH_SHORT).show();
        }

        Confirmation confirmation = new Confirmation(numberOfPeople, year, month, dayOfMonth);

        databaseReference.child("ReserveInfo").setValue(confirmation);

        Toast.makeText(this, "Reservation Saved", Toast.LENGTH_SHORT).show();
    }
}
