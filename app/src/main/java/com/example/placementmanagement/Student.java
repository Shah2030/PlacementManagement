package com.example.placementmanagement;

public class Student {

    private String name;
    private String usn;
    private String address;
    private String department;
    private String mobile;
    private String email;  // Added role field

    // Constructor to initialize all fields including role
    public Student(String name, String usn, String address, String department, String mobile, String email) {
        this.name = name;
        this.usn = usn;
        this.address = address;
        this.department = department;
        this.mobile = mobile;
        this.email = email; // Set role to 'student' by default
    }

    // Getters and setters for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
