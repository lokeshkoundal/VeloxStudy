package com.example.veloxstudy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {
    Button saveBtn,changePfpBtn;
    EditText bioEt, usernameEt, websiteEt, phoneET;
    ImageView pfp;
    FirebaseFirestore fireStore;
    String uid, displayName;
    Uri photoUrl;
    SwitchCompat tutorSwitch, studentSwitch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        saveBtn = findViewById(R.id.saveProfileBtn);
        bioEt = findViewById(R.id.bioEditText);
        usernameEt = findViewById(R.id.usernameEditText);
        websiteEt = findViewById(R.id.websiteEditText);
        pfp = findViewById(R.id.profilePicture);
        phoneET = findViewById(R.id.PhoneEditText);
        tutorSwitch = findViewById(R.id.switch1);
        studentSwitch = findViewById(R.id.switch2);
        changePfpBtn = findViewById(R.id.changeProfilePictureButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            uid = user.getUid();
            displayName = user.getDisplayName();
            photoUrl = user.getPhotoUrl();
        }

        fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("users").document(uid);


        usernameEt.setText(displayName);

        if (photoUrl != null) {
            Glide.with(getApplicationContext())
                    .load(photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(pfp);
        } else {
            pfp.setImageResource(R.drawable.ic_profile);
        }


//        changePfpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
//            }
//        });


        saveBtn.setOnClickListener(v -> {
            String username = usernameEt.getText().toString();
            String bio = bioEt.getText().toString().trim();
            String website = websiteEt.getText().toString().trim();
            String phoneNumber = phoneET.getText().toString().trim();
            boolean tutorSwitchValue = tutorSwitch.isChecked();
            boolean studentSwitchValue = studentSwitch.isChecked();

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    if(username.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please Enter your Username !",Toast.LENGTH_LONG).show();
                    }

                    else if(!tutorSwitchValue&& !studentSwitchValue){
                        Toast.makeText(getApplicationContext(),"Please Select Tutor or Student !",Toast.LENGTH_LONG).show();

                    }
                    else if(phoneNumber.length()!= 10){
                        Toast.makeText(getApplicationContext(),"Please Enter a valid number",Toast.LENGTH_LONG).show();

                    }

                    else{

                        DocumentSnapshot document = task.getResult();
                        Map<String, Object> usermap = new HashMap<>();

                        usermap.put("Website", website);
                        usermap.put("Name", username);
                        usermap.put("Bio", bio);
                        usermap.put("phoneNumber",phoneNumber);
                        usermap.put("tutorSwitchValue", tutorSwitchValue);
                        usermap.put("studentSwitchValue", studentSwitchValue);
                        usermap.put("OldUser",true);

//                                if(!(setImageUri.toString().isEmpty())){
//                                    usermap.put("uri",setImageUri.toString());
//                                }

                        if (document.exists()) {
                            docRef.update(usermap);

                        } else {
                            docRef.set(usermap);
                        }

                        Toast.makeText(getApplicationContext(),"Login Success !!",Toast.LENGTH_SHORT).show();
                        Intent iHome = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(iHome);
                        finish();
                    }


                }
                    });

        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
//            setImageUri = data.getData();
//            if (setImageUri != null) {
//                pfp.setImageURI(setImageUri);
//            }
//        }
//
//    }
}