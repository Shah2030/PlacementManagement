package com.example.placementmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentRegistration extends Fragment {

    private EditText etStudentName, etStudentUsn, etAddress, etMobile, etEmail, etPassword;
    private Spinner spinnerDepartment;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference studentsRef;

    public StudentRegistration() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.student_registration, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        // Initialize UI elements
        etStudentName = view.findViewById(R.id.etStudentName);
        etStudentUsn = view.findViewById(R.id.etStudentUsn);
        etAddress = view.findViewById(R.id.etAddress);
        spinnerDepartment = view.findViewById(R.id.spinnerDepartment);
        etMobile = view.findViewById(R.id.etMobile);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnRegister = view.findViewById(R.id.btnRegister);

        // Set up department spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.departments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);

        // Handle registration button click
        btnRegister.setOnClickListener(v -> registerStudent());

        return view;  // Return the inflated view
    }

    private void registerStudent() {
        // Get user input
        String name = etStudentName.getText().toString().trim();
        String usn = etStudentUsn.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();
        String mobile = etMobile.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(usn) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(mobile) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register student in Firebase Authentication (for email/password login)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Store user data in Firebase Database (email and role)
                        User newUser = new User(email, "student");
                        usersRef.child(userId).setValue(newUser);

                        // Store student additional data in Firebase Database
                        Student student = new Student(name, usn, address, department, mobile, email);
                        studentsRef.child(userId).setValue(student)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().popBackStack();  // Close the fragment
                                    } else {
                                        Toast.makeText(getActivity(), "Error saving student data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
