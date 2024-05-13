package com.example.veloxstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.veloxstudy.fragment_explore;

public class MainActivity extends AppCompatActivity {
    byte FragFlag = 1;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFrag(new fragment_Home(), false);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.nav_home) {
                    //if(!fragment_explore.fetchFlag){
                        if (FragFlag != 1) {
                            loadFrag(new fragment_Home(), true);
                            FragFlag = 1;
                        }
                  //  }

                }

                if(id==R.id.nav_explore) {
                  //  if(!fragment_explore.fetchFlag) {
                        if (FragFlag != 2) {
                            loadFrag(new fragment_explore(), true);
                            FragFlag = 2;
                        }
                  //  }
                }

                if(id==R.id.nav_chat) {
                    if(FragFlag!=3) {
                        loadFrag(new fragment_chat(), true);
                        FragFlag = 3;
                    }
                }

                if(id==R.id.nav_profile) {
                    if(FragFlag!=4) {
                        loadFrag(new fragment_profile(), true);
                        FragFlag = 4;
                    }
                }

                    return true;
            }
            });


    }

    public void loadFrag(Fragment fragment , boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!flag)
            ft.add(R.id.frame, fragment);
        else
            ft.replace(R.id.frame, fragment);
        ft.commit();


    }

}