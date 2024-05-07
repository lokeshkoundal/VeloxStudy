package com.example.veloxstudy;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class fragment_chat extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore FireStore;
    ArrayList<Model> arrModel;
    String currentCollection;

    FirebaseUser user;

    public fragment_chat() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         user = FirebaseAuth.getInstance().getCurrentUser();

        View rootView =  inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = rootView.findViewById(R.id.chatRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FireStore = FirebaseFirestore.getInstance();
        arrModel = new ArrayList<>();

        fetchAllUsersData();

        RecyclerAdapter adapter = new RecyclerAdapter(requireContext(),arrModel);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void fetchAllUsersData() {
        FireStore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Uri pfp = Uri.parse(document.getString("uri"));
                            String name = document.getString("Name");
                            String email = document.getString("Email");

                            String id = document.getId();
                            Log.e("DOCUMENT ID",id);
                            String uid = user.getUid();
                            Log.e("UID",uid);

                            if(!uid.equals(id)) {
                                Model model = new Model(pfp, name, email);
                                arrModel.add(model);
                            }
                        }

                        RecyclerAdapter adapter = new RecyclerAdapter(requireContext(), arrModel);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("TAG","Task Failed");
                    }
                });
    }

}