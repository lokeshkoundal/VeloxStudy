package com.example.veloxstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent iHome = new Intent(getApplicationContext(),MainActivity.class);
        Intent iLogin = new Intent(getApplicationContext(),Login_Activity.class);



        new Handler().postDelayed(() -> {
            SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
            boolean check =  pref.getBoolean("flag",false);

            if(check){
                startActivity(iHome);

            }
            else{
                startActivity(iLogin);

            }

            finish();
        },1600);




    }
}