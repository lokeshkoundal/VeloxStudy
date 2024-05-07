package com.example.veloxstudy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login_Activity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    FirebaseFirestore fireStore;
    String uid;
    String userName;
    String userMail;
    String  photoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.googleBtn);

        signInButton.setOnClickListener(view -> {
            // Call the method to initiate Google Sign-In
            signInWithGoogle(mGoogleSignInClient);
        });

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
                            userName = user.getDisplayName();
                            userMail = user.getEmail();
                            photoUrl = String.valueOf(user.getPhotoUrl());

                            DocumentReference docRef = fireStore.collection("users").document(uid);

                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                            Map<String, Object> usermap = new HashMap<>();
                                            usermap.put("Email", userMail);
                                            usermap.put("Name", userName);
                                            usermap.put("uri", photoUrl);

                                        if (document.exists()) {

                                            docRef.update(usermap);
                                            Log.e("tatti", "pappu pass hogya");
                                        }
                                        else
                                            docRef.set(usermap);
                                    }
                                }
                            });
                      }
                        Log.e("tatti2", "bahar wala");

                        Toast.makeText(getApplicationContext(),"Login Success !!",Toast.LENGTH_SHORT).show();


                        Intent iHome = new Intent(getApplicationContext(),MainActivity.class);

                        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putBoolean("flag",true);
                        editor.apply();
                        startActivity(iHome);
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(),"Login failed !!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}