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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class fragment_explore extends Fragment {
    RecyclerView recyclerView;
    String uid;
    Uri pfp;
    String name;
    String email;
    FirebaseFirestore FireStore;
    ArrayList<Model> arrModel;
    String currentCollection;

  // public static boolean fetchFlag;

    public fragment_explore(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FireStore = FirebaseFirestore.getInstance();
        arrModel = new ArrayList<>();


      //  fetchFlag = true;
        fetchAllUsersData();

     //   fetchFlag = false;

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
                            String uid = document.getId();

                            Model model = new Model(pfp, name, email,uid);
                            arrModel.add(model);
                        }

                        RecyclerAdapter adapter = new RecyclerAdapter(requireContext(), arrModel);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(requireContext(),"Error fetching data",Toast.LENGTH_SHORT).show();
                        Log.d("TAG","Task Failed");
                    }
                });
    }
}