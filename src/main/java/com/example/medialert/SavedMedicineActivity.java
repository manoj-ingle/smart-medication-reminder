package com.example.medialert;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SavedMedicineActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MedicineAdapter adapter;
    List<MedicineModel> medicineList;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_medicine);

        recyclerView = findViewById(R.id.recyclerViewMedicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DBHelper(this);
        medicineList = new ArrayList<>();

        adapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        medicineList.clear();
        medicineList.addAll(db.getAllMedicines());
        adapter.notifyDataSetChanged();
    }

    // 🔥 BACK BUTTON FIX
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SavedMedicineActivity.this, ActivityAddMedicine.class);
        startActivity(i);
        finish();
    }
}
