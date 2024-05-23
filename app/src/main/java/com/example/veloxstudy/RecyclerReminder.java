package com.example.veloxstudy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


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
        holder.timeStampTv.setText(arrReminder.get(position).timeStamp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                    Intent intent = new Intent(context,ProfileView.class);
//
//                    intent.putExtra("uid", arrPopular.get(position).uid);
//                    intent.putExtra("name", arrPopular.get(position).name);
//                    intent.putExtra("img", uri);
//
//                    context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {

        return arrReminder.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView classnameTv,tutorTv,timeStampTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classnameTv = itemView.findViewById(R.id.class_name);
            tutorTv = itemView.findViewById(R.id.tutor_name);
            timeStampTv = itemView.findViewById(R.id.timing);

        }
    }

}
