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

public class StudentDashboard extends AppCompatActivity {

    private Button logoutButton;
    String Root_Frag = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        logoutButton = findViewById(R.id.logout_button);
        Button btnHome, btnCntUs, btnHom;

        btnHome = findViewById(R.id.btnHome);
        btnHom = findViewById(R.id.btnHom);
        btnCntUs = findViewById(R.id.btnCntUs);

        loadFrag(new Home(), 0);

        btnHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new HomeS(), 0);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Home(), 0);
            }
        });


        btnCntUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new CntUs(), 1);
            }
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(StudentDashboard.this, MainActivity.class));
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
