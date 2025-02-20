package com.example.placementmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Signup extends Fragment {
    private EditText emailEditText, passwordEditText;
    private RadioGroup roleRadioGroup;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public Signup() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signup, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        roleRadioGroup = view.findViewById(R.id.roleRadioGroup);
        signupButton = view.findViewById(R.id.signupButton);

        // Set onClick listener for the signup button
        signupButton.setOnClickListener(v -> registerUser(view));

        return view;  // Return the inflated view
    }

    private void registerUser(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

        if (email.isEmpty() || password.isEmpty() || selectedRoleId == -1) {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected role (Student or Placement)
        RadioButton selectedRoleButton = view.findViewById(selectedRoleId);
        String role = selectedRoleButton.getText().toString().toLowerCase();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Store user data in Firebase Database
                            User newUser = new User(email, role);  // Storing user role as well
                            usersRef.child(userId).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Signup Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), Login.class));
                                            getActivity().finish();
                                        } else {
                                            Log.e("Firebase", "Failed to store user", dbTask.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e("FirebaseAuth", "Signup Failed", task.getException());
                    }
                });
    }
}
