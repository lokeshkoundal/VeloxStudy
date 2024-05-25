package com.example.veloxstudy;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileView extends AppCompatActivity {
    TextView bioTv,nameTv,emailTv,websiteTv;
    CardView tutorCv,studentCv;
    ImageView pfp;
    FirebaseFirestore Store;

    String name,uid,pfpData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        FirebaseApp.initializeApp(getApplicationContext());

        bioTv = findViewById(R.id.TextViewBio);
        nameTv = findViewById(R.id.textViewName);
        emailTv = findViewById(R.id.textViewEmail);
        websiteTv = findViewById(R.id.textViewWebsite);
        tutorCv = findViewById(R.id.cardViewTutor);
        studentCv = findViewById(R.id.cardViewStudent);
        pfp =  findViewById(R.id.imageViewProfile);


        name = getIntent().getStringExtra("name");
        uid = getIntent().getStringExtra("uid");
        pfpData = getIntent().getStringExtra("img");

        nameTv.setText(name);

        Store = FirebaseFirestore.getInstance();

        DocumentReference docRef = Store.collection("users").document(uid);
        Log.d("ProfileView", "UID: " + uid);

        docRef.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {

                    String emailReceived = Objects.requireNonNull(document.get("Email")).toString();
                    String BioReceived = "";
                    String websiteReceived = "";
                    boolean websiteValid = false;
                    boolean studentSwitchValueReceived= false;
                    boolean tutorSwitchValueReceived = false;

                    if(document.contains("Bio"))
                        BioReceived = Objects.requireNonNull(document.get("Bio")).toString();


                    if(document.contains("Website")) {
                        websiteReceived = Objects.requireNonNull(document.get("Website")).toString();
                        websiteValid = Patterns.WEB_URL.matcher(websiteReceived).matches();

                        if(websiteValid){
                            websiteTv.setText(websiteReceived);
                        }
                    }


                    if(document.contains("studentSwitchValue"))
                          studentSwitchValueReceived = Objects.requireNonNull(document.getBoolean("studentSwitchValue"));

                    if(document.contains("tutorSwitchValue"))
                        tutorSwitchValueReceived = Objects.requireNonNull(document.getBoolean("tutorSwitchValue"));


                    bioTv.setText(BioReceived);
                    emailTv.setText(emailReceived);

                    if(websiteReceived.equals("") || !websiteValid){
                        websiteTv.setVisibility(View.INVISIBLE);
                    }

                    if(tutorSwitchValueReceived){
                        tutorCv.setVisibility(View.VISIBLE);
                    }

                    if(studentSwitchValueReceived){
                        studentCv.setVisibility(View.VISIBLE);
                    }



                }
            }
        });

        if (pfpData != null) {
            // Load the photo into the ImageView using Glide
            Glide.with(this)
                    .load(pfpData)
                    .apply(RequestOptions.circleCropTransform())
                    .into(pfp);
        } else {
            pfp.setImageResource(R.drawable.ic_profile);
        }
    }
}