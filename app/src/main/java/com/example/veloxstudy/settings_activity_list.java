package com.example.veloxstudy;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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


        FirebaseFirestore Store = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid = user.getUid();

        DocumentReference docRef = Store.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        String usernameReceived = Objects.requireNonNull(document.get("Name")).toString();
                        username_settings.setText(usernameReceived);

                    }
                }

                pfpSetting = findViewById(R.id.profilePhotoImageView);


                for (UserInfo profile : user.getProviderData()) {

                    Uri photoUrl = profile.getPhotoUrl();

                    if (photoUrl != null) {

                        Glide.with(getApplicationContext())
                                .load(photoUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .into(pfpSetting);
                    } else {
                        pfpSetting.setImageResource(R.drawable.ic_profile);
                    }


                }
                ArrayAdapter<String> adapterList = new ArrayAdapter<>(getApplicationContext(), R.layout.textviewsetting, arr);
                lv.setAdapter(adapterList);


                lv.setOnItemClickListener((adapterView, view, position, l) -> {

                    if (position == 4) {
                        Intent iContactus = new Intent(getApplicationContext(), ContactUs.class);
                        startActivity(iContactus);

                    }

                    if (position == 5) {
                        Intent iPrivacy = new Intent(getApplicationContext(), PrivacyPolicy.class);
                        startActivity(iPrivacy);
                    }

                    if (position == 6) {

                        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(settings_activity_list.this);
                        logoutDialog.setTitle("Log out?");
                        logoutDialog.setMessage("Do you want to Log out?");
                        logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();


                                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("flag", false);
                                editor.apply();

                                Intent iLogin = new Intent(getApplicationContext(), Login_Activity.class);
                                startActivity(iLogin);

                                finishAffinity();
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
        });
    }
}