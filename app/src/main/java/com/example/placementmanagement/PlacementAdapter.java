package com.example.placementmanagement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlacementAdapter extends RecyclerView.Adapter<PlacementAdapter.ViewHolder> {

    private List<PlacementUpd> placementList;

    public PlacementAdapter(List<PlacementUpd> placementList) {
        this.placementList = placementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placement_update, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacementUpd update = placementList.get(position);
        holder.companyName.setText(update.companyName);
        holder.jobRole.setText(update.jobRole);
        holder.jobDescription.setText(update.jobDescription);
        holder.companyDetails.setText(update.companyDetails);
        holder.lastDate.setText(update.lastDate);
        holder.jobUrl.setText(update.jobUrl);

        holder.btnApply.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(update.jobUrl));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, jobRole, jobDescription, lastDate, jobUrl, companyDetails;
        Button btnApply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.tvCompanyName);
            jobRole = itemView.findViewById(R.id.tvJobRole);
            jobDescription = itemView.findViewById(R.id.tvJobDescription);
            companyDetails = itemView.findViewById(R.id.tvCompanyDetails);
            lastDate = itemView.findViewById(R.id.tvLastDate);
            jobUrl = itemView.findViewById(R.id.tvJobUrl);
            btnApply = itemView.findViewById(R.id.btnApply);
        }
    }
}
