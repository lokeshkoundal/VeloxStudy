package com.example.veloxstudy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    Button submitButton,cancelBtn;
    EditText bioEt,usernameEt, websiteEt,phoneEt;

    SwitchCompat switchTutor,switchStudent;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        submitButton = findViewById(R.id.saveProfileBtn);
        bioEt = findViewById(R.id.bioEditText);
        usernameEt = findViewById(R.id.usernameEditText);
        cancelBtn = findViewById(R.id.CancelBtn);
        websiteEt = findViewById(R.id.websiteEditText);
        phoneEt = findViewById(R.id.PhoneEditText);
        switchTutor = findViewById(R.id.switch1);
        switchStudent = findViewById(R.id.switch2);


        fireStore = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            documentReference = fireStore.collection("users").document(uid);
        }

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                if(document.exists()){
                    String bioReceived = Objects.requireNonNull(document.get("Bio")).toString();
                    String usernameReceived = Objects.requireNonNull(document.get("Name")).toString();
                    String websiteReceived = Objects.requireNonNull(document.get("Website")).toString();
                    String phoneReceived = Objects.requireNonNull(document.get("phoneNumber")).toString();
                    boolean studentSwitchValueReceived = Objects.requireNonNull(document.getBoolean("studentSwitchValue"));
                    boolean tutorSwitchValueReceived = Objects.requireNonNull(document.getBoolean("tutorSwitchValue"));

                    bioEt.setText(bioReceived);
                    usernameEt.setText(usernameReceived);
                    websiteEt.setText(websiteReceived);
                    phoneEt.setText(phoneReceived);
                    switchStudent.setChecked(studentSwitchValueReceived);
                    switchTutor.setChecked(tutorSwitchValueReceived);
                }
            }
        });



        submitButton.setOnClickListener(view -> {

            String bio = bioEt.getText().toString().trim();
            String username = usernameEt.getText().toString().trim();
            String website = websiteEt.getText().toString().trim();
            String phoneNumber =  phoneEt.getText().toString().trim();
            boolean studentSwitchValue = switchStudent.isChecked();
            boolean tutorSwitchValue = switchTutor.isChecked();



                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();



                        if (username.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"Please enter Username!",Toast.LENGTH_SHORT).show();
                        }

                        else if(!studentSwitchValue && !tutorSwitchValue){
                            Toast.makeText(getApplicationContext(),"Please select student or tutor!",Toast.LENGTH_SHORT).show();
                        }

                        else {

                            Map<String, Object> usermap = new HashMap<>();
                            usermap.put("Website", website);
                            usermap.put("Name", username);
                            usermap.put("Bio", bio);
                            usermap.put("phoneNumber", phoneNumber);
                            usermap.put("tutorSwitchValue", studentSwitchValue);
                            usermap.put("studentSwitchValue", tutorSwitchValue);


                            if (document.exists())
                                documentReference.update(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            else
                                documentReference.set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Profile Updated!", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        }
                    }
                });


        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}