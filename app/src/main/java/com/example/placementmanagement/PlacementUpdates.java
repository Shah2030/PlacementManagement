package com.example.placementmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class PlacementUpdates extends Fragment {

    private EditText etCompanyName, etJobRole, etJobDescription, etLastDate, etJobUrl, etCompanyDetails;
    private MultiAutoCompleteTextView multiDepartment;
    private Button btnSendUpdate;
    private DatabaseReference placementRef;

    public PlacementUpdates() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.placement_updates, container, false);

        // Initialize Firebase Database reference
        placementRef = FirebaseDatabase.getInstance().getReference("placement_updates");

        // Initialize UI elements
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etJobRole = view.findViewById(R.id.etJobRole);
        etJobDescription = view.findViewById(R.id.etJobDescription);
        etLastDate = view.findViewById(R.id.etLastDate);
        etJobUrl = view.findViewById(R.id.etJobUrl);
        etCompanyDetails = view.findViewById(R.id.etCompanyDetails);
        multiDepartment = view.findViewById(R.id.multiDepartment);
        btnSendUpdate = view.findViewById(R.id.btnSendUpdate);

        // Set up multi-selection department list
        String[] departments = getResources().getStringArray(R.array.departments);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, departments);
        multiDepartment.setAdapter(adapter);
        multiDepartment.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        btnSendUpdate.setOnClickListener(v -> sendPlacementUpdate());

        return view;
    }

    private void sendPlacementUpdate() {
        String companyName = etCompanyName.getText().toString().trim();
        String jobRole = etJobRole.getText().toString().trim();
        String jobDescription = etJobDescription.getText().toString().trim();
        String lastDate = etLastDate.getText().toString().trim();
        String jobUrl = etJobUrl.getText().toString().trim();
        String companyDetails = etCompanyDetails.getText().toString().trim();
        String departments = multiDepartment.getText().toString().trim();

        if (TextUtils.isEmpty(companyName) || TextUtils.isEmpty(jobRole) ||
                TextUtils.isEmpty(jobDescription) || TextUtils.isEmpty(lastDate) ||
                TextUtils.isEmpty(jobUrl) || TextUtils.isEmpty(companyDetails) || TextUtils.isEmpty(departments)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> departmentList = Arrays.asList(departments.split(",\\s*"));

        String updateId = placementRef.push().getKey();
        PlacementUpdate update = new PlacementUpdate(updateId, companyName, jobRole, jobDescription, lastDate, jobUrl, companyDetails, departmentList);

        if (updateId != null) {
            placementRef.child(updateId).setValue(update)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Placement update sent successfully!", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send update", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
class PlacementUpdate {
    public String id, companyName, jobRole, jobDescription, lastDate, jobUrl, companyDetails;
    public List<String> departments;

    public PlacementUpdate() {}

    public PlacementUpdate(String id, String companyName, String jobRole, String jobDescription, String lastDate, String jobUrl, String companyDetails, List<String> departments) {
        this.id = id;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.lastDate = lastDate;
        this.jobUrl = jobUrl;
        this.companyDetails = companyDetails;
        this.departments = departments;
    }
}