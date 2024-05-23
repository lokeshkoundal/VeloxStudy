package com.example.veloxstudy;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_Home extends Fragment {
    RecyclerView recyclerView;
    FirebaseFirestore FireStore;
    ArrayList<Model2> arrModel;

    Button addReminderBtn;

    CircleImageView animeIv;
    TextView noClassTv;
    String uid;


    public fragment_Home() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        animeIv = view.findViewById(R.id.animeIv);
        noClassTv = view.findViewById(R.id.noClassTv);
        addReminderBtn = view.findViewById(R.id.addReminderButton);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshHome);
        recyclerView  = view.findViewById(R.id.upcomingRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FireStore = FirebaseFirestore.getInstance();
        arrModel = new ArrayList<>();
        uid =  Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        arrModel.clear();
                        fetchData();

                        //RefreshCode

                    }
                }, 1000);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                R.color.greyApp,
                R.color.black,
                R.color.white
        );

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AddReminder.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void fetchData(){
        FireStore.collection("reminders").document(uid).collection( "data").orderBy("createdAt")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String className = document.getString("Classname");
                            String time = document.getString("Time");
                            String date = document.getString("Date");
                            String tutorName = document.getString("Tutorname");

                            Model2 model2 = new Model2(className,tutorName,time,date);
                            arrModel.add(model2);
                        }

                        if(arrModel.isEmpty()){
                            animeIv.setVisibility(View.VISIBLE);
                            noClassTv.setVisibility(View.VISIBLE);
                        }
                        else{
                            animeIv.setVisibility(View.GONE);
                            noClassTv.setVisibility(View.GONE);
                        }

                        RecyclerReminder adapter = new RecyclerReminder(requireContext(), arrModel);
                        recyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(requireContext(),"Error fetching data",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}