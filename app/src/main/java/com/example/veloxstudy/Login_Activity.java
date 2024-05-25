package com.example.veloxstudy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    FirebaseFirestore fireStore;
    String uid;
    String userName;
    String userMail;
    String  photoUrl;
    DocumentReference docRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.googleBtn);

        signInButton.setOnClickListener(view -> signInWithGoogle(mGoogleSignInClient));

//
    }
    private void signInWithGoogle(GoogleSignInClient mGoogleSignInClient) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data);
        }
    }
    private void handleGoogleSignInResult(Intent data) {
        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(getApplicationContext(),"Login failed due to server failure !!",Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user!=null) {

                            uid = user.getUid();


                            SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("UserID",uid);
                            editor.apply();


                            userName = user.getDisplayName();
                            userMail = user.getEmail();
                            photoUrl = String.valueOf(user.getPhotoUrl());

                            docRef = fireStore.collection("users").document(uid);

                            docRef.get().addOnCompleteListener(task1 -> {

                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document = task1.getResult();

                                        Map<String, Object> usermap = new HashMap<>();
                                        usermap.put("Email", userMail);
                                        usermap.put("Name", userName);
                                        usermap.put("uri", photoUrl);

                                    if (document.exists()) {

                                        docRef.update(usermap);
                                    }
                                    else
                                        docRef.set(usermap);
                                }
                            });
                      }

                        Toast.makeText(getApplicationContext(),"Login Success !!",Toast.LENGTH_SHORT).show();


                        Intent iForm = new Intent(getApplicationContext(),FormActivity.class);
                        Intent iHome = new Intent(getApplicationContext(),MainActivity.class);

                        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("flag",true);
                        editor.apply();

                        docRef.get().addOnCompleteListener(task12 -> {
                            if(task12.isSuccessful()){
                                DocumentSnapshot document = task12.getResult();

                                if(document.exists()){
                                    if(document.contains("OldUser")){
                                        startActivity(iHome);
                                        finish();
                                    }
                                    else{
                                        startActivity(iForm);
                                        finish();
                                    }
                                }
                            }
                        });





                    } else {
                        Toast.makeText(getApplicationContext(),"Login failed !!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}