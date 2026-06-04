package com.example.medialert;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etMobile, etDob, etPassword, etConfirmPassword;
    Spinner spinnerGender, spinnerBlood;
    Button btnRegister;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etDob = findViewById(R.id.etDob);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerBlood = findViewById(R.id.spinnerBlood);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {


        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String blood = spinnerBlood.getSelectedItem().toString();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 🔥 Validation

        if (TextUtils.isEmpty(fullName) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(mobile) ||
                TextUtils.isEmpty(dob) ||
                TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)) {

            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mobile.length() != 10) {
            Toast.makeText(this, "Mobile must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gender.equals("Select Gender")) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (blood.equals("Select Blood Group")) {
            Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.insertUser(
                fullName, email, mobile, dob, gender, blood, password
        );

        if (inserted) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}