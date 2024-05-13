package com.example.veloxstudy;

import static com.example.veloxstudy.ChatActivity.receiverImg;
import static com.example.veloxstudy.ChatActivity.senderImg;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter  {
    Context context;
    ArrayList<MsgModel> messagesArrayListAdapter;
    public MessagesAdapter(Context context, ArrayList<MsgModel> messagesArrayListAdapter) {
        this.context = context;
        this.messagesArrayListAdapter = messagesArrayListAdapter;
    }




    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MsgModel messages = messagesArrayListAdapter.get(position);
        if(holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.msgTv.setText(messages.getMessage());

            if (senderImg != null) {
                Glide.with(context)
                        .load(Uri.parse(senderImg))
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewHolder.pfpIv);

            }
        }

        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.msgTv.setText(messages.getMessage());

            if (receiverImg != null) {
                Glide.with(context)
                        .load(Uri.parse(receiverImg))
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewHolder.pfpIv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayListAdapter.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgModel messages = messagesArrayListAdapter.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderID())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView pfpIv;
        TextView msgTv;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            pfpIv = itemView.findViewById(R.id.pfpSender);
            msgTv = itemView.findViewById(R.id.senderText);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView pfpIv;
        TextView msgTv;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            pfpIv = itemView.findViewById(R.id.pfpReceiver);
            msgTv = itemView.findViewById(R.id.receiverText);
        }
    }
}
