package com.example.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    Query query;
    FirebaseUser user;
    String nickname;
    String uid;
    String friend_uid;
    String sentext;
    String friend_nick;


    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView nick_text;
        public  TextView lastmessage_text;
        public TextView sender_text;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            root=(LinearLayout)itemView.findViewById(R.id.list_item_id);
            nick_text=(TextView)itemView.findViewById(R.id.nick_sender);
            lastmessage_text=(TextView)itemView.findViewById(R.id.message);
            sender_text=(TextView)itemView.findViewById(R.id.message_sender);


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("1","Fine");
        setTitle("Your chats");
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            send_to_login();
            Log.v("1","Fine5");
        }
        else{
            Log.v("1","Fine6");
            mAuth=FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
            uid=user.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user1=dataSnapshot.getValue(User.class);
                    nickname=user1.getNick();
                    toaster("Welcome "+nickname);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    toaster("couldn't retrieve user..");
                }
            });
            databaseReference2=FirebaseDatabase.getInstance().getReference();
            Log.v("1","Fine1");
            query=databaseReference2.child("user messages").child(uid).child("messages").orderByKey();
            //query=databaseReference2.child("users").orderByKey();
            Log.v("1","Fine2");

            SnapshotParser<LastMessageItem> parser=new SnapshotParser<LastMessageItem>() {

                @Override
                public LastMessageItem parseSnapshot(DataSnapshot snapshot) {

                    LastMessageItem lastMessageItem=snapshot.getValue(LastMessageItem.class);

                    if(lastMessageItem!=null){
                        if(lastMessageItem.getSender_uid().equals(uid)){
                            lastMessageItem.setSender_nick("Me");
                        }

                    }

                    return lastMessageItem;
                }
            };
            FirebaseRecyclerOptions<LastMessageItem> options=new FirebaseRecyclerOptions.Builder<LastMessageItem>().setQuery(query,parser).build();
            adapter=new FirebaseRecyclerAdapter<LastMessageItem,ChatViewHolder>(options){

                @NonNull
                @Override
                public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    return  new ChatViewHolder(inflater.inflate(R.layout.list_item,parent,false));

                }
                @Override
                protected void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int i, @NonNull final LastMessageItem lastMessageItem) {
                    chatViewHolder.nick_text.setText(lastMessageItem.getFriend_nick());
                    sentext=lastMessageItem.getSender_nick()+":";
                    chatViewHolder.sender_text.setText(sentext);
                    chatViewHolder.lastmessage_text.setText(lastMessageItem.getMessage());
                    chatViewHolder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(MainActivity.this,chatActivity.class);
                            friend_uid=lastMessageItem.getFriend_uid();
                            intent.putExtra("friend_id",friend_uid);
                            intent.putExtra("friend_nick",lastMessageItem.getFriend_nick());
                            intent.putExtra("my_nick",nickname);


                            startActivity(intent);
                        }
                    });

                }
            };

            //update this later
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                }
            });


            Log.v("1","Fine3");
            recyclerView.setAdapter(adapter);
            Log.v("1","Fine7");
            adapter.startListening();
            Log.v("1","Fine8");

        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.v("1","Fine4");

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

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void send_to_login(){
        startActivity(new Intent(MainActivity.this,Login.class));
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_logout){
            mAuth.signOut();
            send_to_login();
            return true;
        }
        else if(item.getItemId()==R.id.search_users){
            Intent intent=new Intent(MainActivity.this,search_users.class);
            intent.putExtra("my_nick",nickname);
            startActivity(intent);

            return true;

        }
        else if(item.getItemId()==R.id.search_by_interests){

            Intent intent=new Intent(MainActivity.this,search_interests.class);
            intent.putExtra("my_nick",nickname);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.edit_profile){
            if(mAuth.getCurrentUser()!=null) {
                uid=mAuth.getCurrentUser().getUid();

                Intent intent = new Intent(MainActivity.this, edit_profile2.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                return true;
            }
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void toaster(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }


}
