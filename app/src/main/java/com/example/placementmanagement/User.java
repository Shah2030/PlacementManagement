package com.example.placementmanagement;
public class User {
    private String email;
    private String role;

    // Empty constructor for Firebase
    public User() {}

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
