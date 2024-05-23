package com.example.veloxstudy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


public class RecyclerReminder extends RecyclerView.Adapter <RecyclerReminder.ViewHolder>{
    Context context;

    ArrayList<Model2> arrReminder;

    RecyclerReminder(Context context, ArrayList<Model2> arrReminder){
        this.context = context;
        this.arrReminder = arrReminder;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_reminder,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.classnameTv.setText(arrReminder.get(position).className);
        String tutorname = "Tutor : " + arrReminder.get(position).tutorName;
        holder.tutorTv.setText(tutorname);
        holder.timeStampTv.setText(arrReminder.get(position).date);
        holder.timeTv.setText(arrReminder.get(position).time);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String DocName = arrReminder.get(position).className + arrReminder.get(position).tutorName;

        DocumentReference docRef = db.collection("reminders").document(uid).collection("data").document(DocName);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(context);
                logoutDialog.setTitle("Delete this reminder?");
                logoutDialog.setMessage("Are you sure you want delete this reminder?");
                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Document successfully deleted
                                        Toast.makeText( context,"Reminder deleted !", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText( context,"Reminder couldn't be deleted !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                logoutDialog.show();
            }
        });


            }


    @Override
    public int getItemCount() {

        return arrReminder.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView classnameTv,tutorTv,timeStampTv,timeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classnameTv = itemView.findViewById(R.id.class_name);
            tutorTv = itemView.findViewById(R.id.tutor_name);
            timeStampTv = itemView.findViewById(R.id.timing);
            timeTv = itemView.findViewById(R.id.countdown);


        }
    }

}
