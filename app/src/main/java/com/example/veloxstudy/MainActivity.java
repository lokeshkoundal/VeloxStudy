package com.example.veloxstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    byte FragFlag = 1;
    BottomNavigationView bottomNavigationView;
    Fragment fragmentHome,fragmentExplore,fragmentChat,fragmentProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentHome = new fragment_Home();
        fragmentExplore = new fragment_explore();
        fragmentChat = new fragment_chat();
        fragmentProfile = new fragment_profile();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.frame, fragmentHome);
        ft.add(R.id.frame, fragmentExplore).hide(fragmentExplore);
        ft.add(R.id.frame, fragmentChat).hide(fragmentChat);
        ft.add(R.id.frame, fragmentProfile).hide(fragmentProfile);
        ft.commit();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.nav_home) {
                    //if(!fragment_explore.fetchFlag){
                        if (FragFlag != 1) {
                            loadFrag(fragmentHome);
                            FragFlag = 1;
                        }
                  //  }

                }

                if(id==R.id.nav_explore) {
                  //  if(!fragment_explore.fetchFlag) {
                        if (FragFlag != 2) {
                            loadFrag(fragmentExplore);
                            FragFlag = 2;
                        }
                  //  }
                }

                if(id==R.id.nav_chat) {
                    if(FragFlag!=3) {
                        loadFrag(fragmentChat);
                        FragFlag = 3;
                    }
                }

                if(id==R.id.nav_profile) {
                    if(FragFlag!=4) {
                        loadFrag(fragmentProfile);
                        FragFlag = 4;
                    }
                }

                    return true;
            }
            });


    }

    public void loadFrag(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for (Fragment frag : fm.getFragments()) {
            if (frag != null) {
                ft.hide(frag);
            }
        }

        ft.show(fragment);

        ft.commit();

    }

}