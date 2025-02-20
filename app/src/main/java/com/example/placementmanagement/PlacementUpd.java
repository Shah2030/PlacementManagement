package com.example.placementmanagement;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class PlacementUpd implements Parcelable {
    public String companyName;
    public String jobRole;
    public String lastDate;
    public String jobUrl;
    public String companyDetails;
    public String jobDescription;
    public List<String> departments;

    // Default constructor (required for Firebase)
    public PlacementUpd() {}

    public PlacementUpd(String companyName, String jobRole, String lastDate, String jobUrl, String companyDetails, String jobDescription, List<String> departments) {
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.lastDate = lastDate;
        this.jobUrl = jobUrl;
        this.companyDetails = companyDetails;
        this.jobDescription = jobDescription;
        this.departments = departments;
    }

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public String getLastDate() { return lastDate; }
    public String getDepartment() {
        String s="";
        for(String d: departments) {
            s+=d+", ";
        }
        s = s.substring(0, s.length() - 2);
        return s;
    }
    public String getJobUrl() { return jobUrl; }
    public String getCompanyDetails() { return companyDetails; }
    public String getJobDescription() { return jobDescription; }
    public List<String> getDepartments() { return departments; }

    // Parcelable implementation
    protected PlacementUpd(Parcel in) {
        companyName = in.readString();
        jobRole = in.readString();
        lastDate = in.readString();
        jobUrl = in.readString();
        companyDetails = in.readString();
        jobDescription = in.readString();
        departments = in.createStringArrayList();
    }

    public static final Creator<PlacementUpd> CREATOR = new Creator<PlacementUpd>() {
        @Override
        public PlacementUpd createFromParcel(Parcel in) {
            return new PlacementUpd(in);
        }

        @Override
        public PlacementUpd[] newArray(int size) {
            return new PlacementUpd[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyName);
        dest.writeString(jobRole);
        dest.writeString(lastDate);
        dest.writeString(jobUrl);
        dest.writeString(companyDetails);
        dest.writeString(jobDescription);
        dest.writeStringList(departments);
    }

}
