package com.example.placementmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class PlacementDashboard extends AppCompatActivity {

    private Button logoutButton;
    String Root_Frag = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placement_dashboard);

        logoutButton = findViewById(R.id.logout_button);
        Button btnHome, btnCntUs, btnSreg, btnPlacementDetails;
        btnPlacementDetails = findViewById(R.id.btnPlacementDetails);
        btnHome = findViewById(R.id.btnHome);
        btnCntUs = findViewById(R.id.btnCntUs);
        btnSreg = findViewById(R.id.btnSreg);

        loadFrag(new Home(), 0);

        btnPlacementDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFrag(new PlacementDetails(), 1);
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Home(), 0);
            }
        });


        btnSreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFrag(new StudentRegistration(),1);
            }
        });

        btnCntUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new PlacementUpdates(), 1);
            }
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(PlacementDashboard.this, MainActivity.class));
            finish();
        });
    }

    public void loadFrag(Fragment fragment_name, int flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (flag == 0) {
            ft.add(R.id.FL, fragment_name);

            fm.popBackStack(Root_Frag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(Root_Frag);
        }
        else {
            ft.replace(R.id.FL, fragment_name);
            ft.addToBackStack(null);
        }

        ft.commit();
    }
}
