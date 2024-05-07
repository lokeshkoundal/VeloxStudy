package com.example.veloxstudy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    Button submitButton;
    EditText editText;

    TextView resultTv;

    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        submitButton = findViewById(R.id.buttonSubmitBIO);
        editText = findViewById(R.id.editTextBio);
        fireStore = FirebaseFirestore.getInstance();
        resultTv = findViewById(R.id.resultextView);


        submitButton.setOnClickListener(view -> {

            String bio = editText.getText().toString();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String uid = user.getUid();
                DocumentReference documentReference = fireStore.collection("users").document(uid);

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        Map<String, Object> usermap = new HashMap<>();
                        usermap.put("Bio", bio);

                        if(document.exists())
                            documentReference.update(usermap).addOnSuccessListener(unused -> resultTv.setText(R.string.BioSet));

                        else
                            documentReference.set(usermap).addOnSuccessListener(unused -> resultTv.setText(R.string.BioSet));




                    }
                });


            }

        });

    }
}