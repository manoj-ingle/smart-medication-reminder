package com.example.medialert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    private List<MedicineModel> medicineList;

    public MedicineAdapter(Context context, List<MedicineModel> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicineModel medicine = medicineList.get(position);

        holder.tvName.setText(medicine.getName());
        holder.tvDose.setText("Dose: " + medicine.getDose());
        holder.tvTime.setText("Time: " + medicine.getTime());

        // ⭐ NEW LINE (Repeat show)
        holder.tvRepeat.setText("Repeat: " + medicine.getRepeat());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDose, tvTime, tvRepeat;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textView5);
            tvDose = itemView.findViewById(R.id.textView6);
            tvTime = itemView.findViewById(R.id.textView7);

            // ⭐ NEW
            tvRepeat = itemView.findViewById(R.id.textViewRepeat);
        }
    }
}