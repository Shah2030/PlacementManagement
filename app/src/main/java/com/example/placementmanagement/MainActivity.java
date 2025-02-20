package com.example.placementmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String Root_Frag = "root_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHome, btnLogin, btnAbt, btnCntUs, btnHom;

        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnAbt = findViewById(R.id.btnAbt);
        btnHom = findViewById(R.id.btnHom);
        btnCntUs = findViewById(R.id.btnCntUs);

        loadFrag(new Home(), 0);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Home(), 0);
            }
        });

        btnHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Home(), 0);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Login(), 1);
            }
        });

        btnAbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new Abt(), 1);
            }
        });

        btnCntUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadFrag(new CntUs(), 1);
            }
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
