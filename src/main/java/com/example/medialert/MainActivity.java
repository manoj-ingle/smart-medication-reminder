package com.example.medialert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Using your existing XML IDs
        etEmail = findViewById(R.id.editTextText);
        etPassword = findViewById(R.id.editTextText2);
        btnLogin = findViewById(R.id.button);
        tvRegister = findViewById(R.id.tvCreateAccount);  // if id is same keep it

        dbHelper = new DBHelper(this);

        // 🔹 LOGIN BUTTON
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(MainActivity.this,
                        "Enter Email and Password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValid = dbHelper.checkUser(email, pass);

            if (isValid) {
                Toast.makeText(MainActivity.this,
                        "Login Successful!",
                        Toast.LENGTH_SHORT).show();
                goToHome();
            } else {
                Toast.makeText(MainActivity.this,
                        "Invalid Email or Password!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 🔹 OPEN REGISTER PAGE
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void goToHome() {
        Intent intent = new Intent(MainActivity.this, ActivityAddMedicine.class);
        startActivity(intent);
        finish();
    }
}