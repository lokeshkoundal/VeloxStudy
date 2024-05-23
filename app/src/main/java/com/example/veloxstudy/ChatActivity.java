package com.example.veloxstudy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class ChatActivity extends AppCompatActivity {
    String receiverName,receiverUID,receiverPFP,senderID,senderRoom,receiverRoom;
    public static String senderImg,receiverImg;

    TextView nameTv,status;
    ImageView imgChat,startConvo;

    EditText writeMsgET;
    ImageButton sendMsgIB;
    Uri imgUri;
    RecyclerView chatRv;
    ArrayList<MsgModel> messagesArrayList;
    MessagesAdapter messagesAdapter;

    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseApp.initializeApp(getApplicationContext());


        receiverName = getIntent().getStringExtra("name");
        receiverUID = getIntent().getStringExtra("uid");
        receiverPFP = getIntent().getStringExtra("img");

        messagesArrayList = new ArrayList<>();

        nameTv = findViewById(R.id.NameChat);
        status = findViewById(R.id.StatusChat);
        imgChat = findViewById(R.id.ImageChat);
        writeMsgET = findViewById(R.id.writeMsgET);
        sendMsgIB = findViewById(R.id.sendMsgButton);
        chatRv = findViewById(R.id.ChatRecyclerView);
        startConvo = findViewById(R.id.startConvo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRv.setLayoutManager(linearLayoutManager);

        messagesAdapter = new MessagesAdapter(ChatActivity.this,messagesArrayList);
        chatRv.setAdapter(messagesAdapter);
        nameTv.setText(receiverName);
        imgUri = Uri.parse(receiverPFP);

        if (imgUri != null) {
            Glide.with(this)
                    .load(imgUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgChat);
        } else {
            imgChat.setImageResource(R.drawable.ic_profile);
        }
       // SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
        senderID = FirebaseAuth.getInstance().getUid();

        database = FirebaseDatabase.getInstance();

        senderRoom = senderID + receiverUID;
        receiverRoom = receiverUID + senderID;

        DatabaseReference reference = database.getReference().child("user").child(senderID);

        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MsgModel messages = dataSnapshot.getValue(MsgModel.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();

                if (messagesArrayList.isEmpty())
                    startConvo.setVisibility(View.VISIBLE);
                else
                    startConvo.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                senderImg = (Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl())).toString();

                receiverImg = receiverPFP;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = (writeMsgET.getText()).toString().trim();
                if (message.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Message",Toast.LENGTH_SHORT).show();
                    return;
                }
                writeMsgET.setText("");
                Date date = new Date();
                MsgModel messages = new MsgModel(message,senderID,date.getTime());

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(receiverRoom).child("messages").push()
                                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                chatRv.smoothScrollToPosition(messagesArrayList.size() - 1);
                                            }
                                        });
                            }
                        });
            }
        });

    }
}