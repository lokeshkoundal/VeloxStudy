package com.example.veloxstudy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class settings_activity_list extends AppCompatActivity {

   // String  receivedVariableName = getIntent().getStringExtra("name");
    ListView lv;
    ImageView pfpSetting;
    String[] arr = {"Personal Information","Class history","Configure Notification","Theme","Contact Us","Privacy Policy","Logout"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        TextView username_settings = findViewById(R.id.textViewSetting);
        lv = findViewById(R.id.listVIewSettings);

        pfpSetting = findViewById(R.id.profilePhotoImageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                Uri photoUrl = profile.getPhotoUrl();

                username_settings.setText(profile.getDisplayName());
                if (photoUrl != null) {

                    Glide.with(this)
                            .load(photoUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(pfpSetting);
                } else {
                    pfpSetting.setImageResource(R.drawable.ic_profile);
                }


            }
        }

        ArrayAdapter<String> adapterList = new ArrayAdapter<>(getApplicationContext(),R.layout.textviewsetting,arr);
        lv.setAdapter(adapterList);


        lv.setOnItemClickListener((adapterView, view, position, l) -> {

            if(position==4){
                Intent iContactus = new Intent(getApplicationContext(),ContactUs.class);
                startActivity(iContactus);

            }

            if(position==5){
                Intent iPrivacy = new Intent(getApplicationContext(),PrivacyPolicy.class);
                startActivity(iPrivacy);
            }

            if(position==6){

                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(settings_activity_list.this);
                logoutDialog.setTitle("Log out?");
                logoutDialog.setMessage("Do you want to Log out?");
                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("flag",false);
                        editor.apply();

                        Intent iLogin = new Intent(getApplicationContext(),Login_Activity.class);
                        startActivity(iLogin);
                        finish();
                    }
                });

                logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                logoutDialog.show();

            }

        });



    }
}