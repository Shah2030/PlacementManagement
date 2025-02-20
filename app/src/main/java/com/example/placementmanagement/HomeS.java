package com.example.placementmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeS extends Fragment {

    private RecyclerView recyclerView;
    private PlacementAdapter adapter;
    private List<PlacementUpd> placementList;
    private DatabaseReference studentRef, placementRef;
    private FirebaseAuth auth;

    public HomeS() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        placementList = new ArrayList<>();
        adapter = new PlacementAdapter(placementList);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        studentRef = FirebaseDatabase.getInstance().getReference("students");
        placementRef = FirebaseDatabase.getInstance().getReference("placement_updates");

        loadStudentDepartment();

        return view;
    }

    private void loadStudentDepartment() {
        String userId = auth.getCurrentUser().getUid();

        studentRef.child(userId).child("department").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String studentDepartment = snapshot.getValue(String.class);
                    loadPlacementUpdates(studentDepartment);
                } else {
                    Toast.makeText(getContext(), "Department not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch department", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlacementUpdates(String department) {
        placementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placementList.clear();
                for (DataSnapshot updateSnapshot : snapshot.getChildren()) {
                    PlacementUpd update = updateSnapshot.getValue(PlacementUpd.class);
                    if (update != null && update.departments.contains(department)) {
                        placementList.add(update);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load placement updates", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
