package com.example.placementmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlacementDeptAdapter extends RecyclerView.Adapter<PlacementDeptAdapter.ViewHolder> {
    private List<PlacementUpd> placementList;
    private List<PlacementUpd> selectedCompanies;

    public PlacementDeptAdapter(List<PlacementUpd> placementList, List<PlacementUpd> selectedCompanies) {
        this.placementList = placementList;
        this.selectedCompanies = selectedCompanies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placement_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacementUpd placement = placementList.get(position);
        holder.tvCompanyName.setText(placement.getCompanyName());
        holder.tvJobRole.setText(placement.getJobRole());
        holder.tvLastDate.setText(placement.getLastDate());

        holder.checkBox.setOnCheckedChangeListener(null); // Prevent unwanted triggers
        holder.checkBox.setChecked(selectedCompanies.contains(placement));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCompanies.add(placement);
            } else {
                selectedCompanies.remove(placement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompanyName, tvJobRole, tvLastDate;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvJobRole = itemView.findViewById(R.id.tvJobRole);
            tvLastDate = itemView.findViewById(R.id.tvLastDate);
            checkBox = itemView.findViewById(R.id.checkBoxSelect);
        }
    }
}
