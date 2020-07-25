package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class chatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseAuth mAuth;
    ChatAdapter adapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    Query query;
    FirebaseUser user;
    String nickname;
    String uid;
    EditText messageTxt;
    Button sendButton;
    Intent intent;
    String room_id;
    String friend_uid;
    String friend_nick;
    String msg;
    String key1;
    boolean flag1;
    boolean flag2;
    String key2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_chat);
        layoutManager=new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        intent=getIntent();
        friend_uid=intent.getStringExtra("friend_id");
        friend_nick=intent.getStringExtra("friend_nick");
        nickname=intent.getStringExtra("my_nick");
        setTitle(friend_nick);
        sendButton=(Button)findViewById(R.id.send_button);
        messageTxt=(EditText)findViewById(R.id.chat_typemessage);
        flag1=true;
        flag2=true;

        Log.d("2","entered activity");
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            send_to_login();
        }
        else{
            mAuth=FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
            uid=user.getUid();
            if(uid.compareTo(friend_uid)>0){
                room_id=uid+"_"+friend_uid;
            }
            else{
                room_id=friend_uid+"_"+uid;
            }

            databaseReference= FirebaseDatabase.getInstance().getReference();
            query=databaseReference.child("chats").child(room_id).orderByKey();

            SnapshotParser<Message> parser=new SnapshotParser<Message>(){

                @NonNull
                @Override
                public Message parseSnapshot(@NonNull DataSnapshot snapshot) {
                    Message message=snapshot.getValue(Message.class);
                    return message;
                }
            };

            FirebaseRecyclerOptions<Message> options=new FirebaseRecyclerOptions.Builder<Message>().setQuery(query,parser).build();
            adapter=new ChatAdapter(options);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                }
            });

            recyclerView.setAdapter(adapter);
            adapter.startListening();

            databaseReference2=FirebaseDatabase.getInstance().getReference();
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(messageTxt.getText().toString().length()==0){
                        toaster("Type some text..");
                    }
                    else{
                        msg=messageTxt.getText().toString();
                        databaseReference2.child("chats").child(room_id).push().setValue(new Message(msg,uid));
                        final String key=databaseReference2.child("user messages").child(uid).child("messages").push().getKey();

                        databaseReference2.child("user messages").child(uid).child("messages").child(key).setValue(new LastMessageItem(nickname,uid,msg,friend_uid,friend_nick));
                        databaseReference2.child("user messages").child(uid).child("map").child(friend_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                key1=dataSnapshot.getValue(String.class);
                                if(key1!=null)
                                    databaseReference2.child("user messages").child(uid).child("messages").child(key1).setValue(null);
                                databaseReference2.child("user messages").child(uid).child("map").child(friend_uid).setValue(key);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("3","been here");
                                flag1=false;
                            }
                        });

                        final String key3=databaseReference2.child("user messages").child(friend_uid).child("messages").push().getKey();

                        databaseReference2.child("user messages").child(friend_uid).child("messages").child(key3).setValue(new LastMessageItem(nickname,uid,msg,uid,nickname));
                        databaseReference2.child("user messages").child(friend_uid).child("map").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                key2=dataSnapshot.getValue(String.class);
                                if(key2!=null)
                                    databaseReference2.child("user messages").child(friend_uid).child("messages").child(key2).setValue(null);
                                databaseReference2.child("user messages").child(friend_uid).child("map").child(uid).setValue(key3);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("3","been here too");
                                flag2=false;
                            }
                        });


                        messageTxt.setText("");



                    }
                }
            });

        }




    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    public void send_to_login(){
        startActivity(new Intent(chatActivity.this,Login.class));
        finish();
    }

    public void toaster(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

    public class ChatAdapter extends FirebaseRecyclerAdapter<Message, ChatAdapter.ViewHolder>{
        public static final int MSG_TYPE_LEFT = 0;
        public static final int MSG_TYPE_RIGHT = 1;

        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public ChatAdapter(@NonNull FirebaseRecyclerOptions<Message> options) {
            super(options);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {
            holder.message.setText(message.getMessage());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            if(viewType==MSG_TYPE_RIGHT){
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.message_temr_right,parent,false);

            }
            else{
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.message_tem,parent,false);
            }
            return new ViewHolder(v);
        }

        @Override
        public int getItemViewType(int position) {
            if(getItem(position).getSender_uid().equals(uid)){
                return MSG_TYPE_RIGHT;
            }
            else{
                return MSG_TYPE_LEFT;
            }
        }



        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView message;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                message=(TextView)itemView.findViewById(R.id.text_message_body);
            }
        }
    }
}


