package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class search_users extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText searchtext;
    Button button;
    String text;
    LinearLayout linearLayout;
    LinearLayoutManager layoutManager;
    String my_uid;
    String my_nick;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseRecyclerAdapter adapter;



    public static class NickViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView nickname_text;

        public NickViewHolder(@NonNull View itemView) {
            super(itemView);
            root=(LinearLayout)itemView.findViewById(R.id.user_item_root);
            nickname_text=(TextView)itemView.findViewById(R.id.nick_search);

        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        setTitle("Search");

        recyclerView=(RecyclerView)findViewById(R.id.rv_search_users);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        my_nick=intent.getStringExtra("my_nick");
        searchtext=(EditText)findViewById(R.id.searchText);
        button=(Button)findViewById(R.id.searchbutton);


        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(search_users.this,Login.class));
            finish();
        }
        else{
            my_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(searchtext.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please enter something.. ",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        text=searchtext.getText().toString();
                        Query query=databaseReference.child("nicks").orderByChild("nick").startAt(text);

                        SnapshotParser<usernick> parser=new SnapshotParser<usernick>() {
                            @NonNull
                            @Override
                            public usernick parseSnapshot(@NonNull DataSnapshot snapshot) {
                                usernick userNick=snapshot.getValue(usernick.class);
                                return userNick;
                            }
                        };
                        FirebaseRecyclerOptions<usernick> options=new FirebaseRecyclerOptions.Builder<usernick>().setQuery(query,parser).build();
                        adapter=new FirebaseRecyclerAdapter<usernick,NickViewHolder>(options) {

                            @NonNull
                            @Override
                            public NickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                return  new NickViewHolder(inflater.inflate(R.layout.useritem,parent,false));
                            }

                            @Override
                            protected void onBindViewHolder(@NonNull final NickViewHolder nickViewHolder, int i, @NonNull final usernick usernick) {
                                nickViewHolder.nickname_text.setText(usernick.getNick());
                                nickViewHolder.root.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(usernick.getUid().equals(my_uid)){
                                            Intent intent=new Intent(search_users.this,edit_profile2.class);
                                            intent.putExtra("uid",my_uid);
                                            startActivity(intent);
                                        }

                                        //Toast.makeText(getApplicationContext(),"Navigate to "+usernick.getNick(),Toast.LENGTH_SHORT).show();
                                        else{
                                            Intent intent2 = new Intent(search_users.this, userProfile.class);
                                            intent2.putExtra("result_uid", usernick.getUid());
                                            intent2.putExtra("nickname", usernick.getNick());
                                            startActivity(intent2);
                                        }
                                    }
                                });
                            }
                        };
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();


                    }
                }
            });


        }






    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

}
