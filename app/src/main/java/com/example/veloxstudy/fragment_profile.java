package com.example.veloxstudy;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class fragment_profile extends Fragment {
    Button editProfile;
    View rootView;

    TextView bioTv;
    FirebaseFirestore Store;
    String uid;

    Uri photoUrl;


    public fragment_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton imageButton = rootView.findViewById(R.id.settingsButton);
        TextView usernameTv = rootView.findViewById(R.id.usernameTextView);
        ImageView pfp = rootView.findViewById(R.id.profilePhotoImageView);
        editProfile = rootView.findViewById(R.id.editProfileButton);
        bioTv = rootView.findViewById(R.id.BioContentTextView);

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshProfile);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame, new fragment_profile());
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }, 1000);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

             uid = user.getUid();
             photoUrl = user.getPhotoUrl();
        }



        Store = FirebaseFirestore.getInstance();
        DocumentReference docRef = Store.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                            String BioReceived = Objects.requireNonNull(document.get("Bio")).toString();
                            String usernameReceived = Objects.requireNonNull(document.get("Name")).toString();
                            bioTv.setText(BioReceived);
                            usernameTv.setText(usernameReceived);




                    }
                }
            }
                });


                if (photoUrl != null) {
                    // Load the photo into the ImageView using Glide
                    Glide.with(this)
                            .load(photoUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(pfp);
                } else {
                    pfp.setImageResource(R.drawable.ic_profile);
                }



        editProfile.setOnClickListener(view -> {
           Intent intent = new Intent(getContext(),EditProfile.class);
           startActivity(intent);
        });

        imageButton.setOnClickListener(view -> {

            Intent ISettings = new Intent(getContext(), settings_activity_list.class);
            // ISettings.putExtra("name",displayName);
            startActivity(ISettings);
        });

        return rootView;

    }
}