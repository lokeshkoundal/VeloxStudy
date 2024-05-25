package com.example.veloxstudy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.ViewHolder>{
    Context context;
    boolean fragment;
    ArrayList<Model> arrPopular;

    RecyclerAdapter(Context context, ArrayList<Model> arrPopular,boolean fragment){
        this.context = context;
        this.arrPopular = arrPopular;
        this.fragment = fragment;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_popular,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameTv.setText(arrPopular.get(position).name);
        //holder.imgTv.setImageURI(arrPopular.get(position).img);
        holder.emailTv.setText(arrPopular.get(position).email);

        Glide.with(context)
                .load(arrPopular.get(position).img)
                .into(holder.imgTv);


        holder.itemView.setOnClickListener(v -> {

            if(!fragment) {
                Intent intent = new Intent(context, ChatActivity.class);
                String uri = (arrPopular.get(position).img).toString();

                intent.putExtra("name", arrPopular.get(position).name);
                intent.putExtra("img", uri);
                intent.putExtra("uid", arrPopular.get(position).uid);
                context.startActivity(intent);
            }


            if(fragment){

                Intent intent = new Intent(context,ProfileView.class);
                String uri = (arrPopular.get(position).img).toString();

                intent.putExtra("uid", arrPopular.get(position).uid);
                intent.putExtra("name", arrPopular.get(position).name);
                intent.putExtra("img", uri);

                context.startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {

        return arrPopular.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv,emailTv;
        ImageView imgTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.recycler_nameTv);
            emailTv = itemView.findViewById(R.id.recycler_mailTv);
            imgTv = itemView.findViewById(R.id.recycler_img);

        }
    }

}
