package com.example.medialert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import java.util.Calendar;

public class ActivityAddMedicine extends AppCompatActivity {

    EditText etName, etDose;
    TextView tvTime;
    Button btnSave;
    Spinner spinnerRepeat;

    String selectedTime = "";
    String repeatType = "Daily";

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        etName = findViewById(R.id.editTextText3);
        etDose = findViewById(R.id.editTextText4);
        tvTime = findViewById(R.id.textView4);
        btnSave = findViewById(R.id.button2);
        spinnerRepeat = findViewById(R.id.spinnerRepeat);

        db = new DBHelper(this);

        // Spinner options
        String[] options = {"Daily","Weekly","15 Days","Monthly"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        options);

        spinnerRepeat.setAdapter(adapter);

        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatType = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Android 13 notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

        // Time picker
        tvTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int h = c.get(Calendar.HOUR_OF_DAY);
            int m = c.get(Calendar.MINUTE);

            TimePickerDialog tp = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        tvTime.setText(selectedTime);
                    },
                    h, m, true
            );
            tp.show();
        });

        // SAVE BUTTON
        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String dose = etDose.getText().toString().trim();

            if (name.isEmpty() || dose.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // ⭐ UPDATED SAVE CALL (RepeatType save होते)
            long newId = db.addMedicine(name, dose, selectedTime, repeatType);

            if (newId > 0) {
                scheduleAlarm((int) newId, name, dose, selectedTime);
                Toast.makeText(this, "Medicine saved!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ActivityAddMedicine.this, SavedMedicineActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Error saving medicine", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Alarm scheduler
    private void scheduleAlarm(int id, String name, String dose, String time) {

        Calendar calendar = Calendar.getInstance();
        String[] parts = time.split(":");

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance()))
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medicineName", name);
        intent.putExtra("dose", dose);
        intent.putExtra("time", time);
        intent.putExtra("medicineId", id);

        PendingIntent pi = PendingIntent.getBroadcast(
                this,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        long interval = AlarmManager.INTERVAL_DAY;

        switch (repeatType) {
            case "Weekly":
                interval = AlarmManager.INTERVAL_DAY * 7;
                break;

            case "15 Days":
                interval = AlarmManager.INTERVAL_DAY * 15;
                break;

            case "Monthly":
                interval = AlarmManager.INTERVAL_DAY * 30;
                break;
        }

        if (am != null) {
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    interval,
                    pi
            );
        }

        Log.d("AlarmDebug", "Alarm set for ID: " + id + " Repeat: " + repeatType);
    }

    // Back button
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityAddMedicine.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}