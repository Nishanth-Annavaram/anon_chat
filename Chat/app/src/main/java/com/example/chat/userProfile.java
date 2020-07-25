package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userProfile extends AppCompatActivity {

    Button messageButton;
    TextView nick;
    TextView aboutline;
    RecyclerView recyclerView;
    myInterestAdapter adapter;
    String result_uid;
    String nickname;
    User result_user;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> interests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("User Profile");

        nick=(TextView)findViewById(R.id.nick_line_1);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_interests_1);
        messageButton=(Button)findViewById(R.id.button_message);
        aboutline=(TextView)findViewById(R.id.about_line);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        Intent intent=getIntent();
        result_uid=intent.getStringExtra("result_uid");
        nickname=intent.getStringExtra("nickname");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(result_uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result_user=dataSnapshot.getValue(User.class);
                interests=result_user.getInterests();
                aboutline.setText(result_user.getAboutme());
                nick.setText(result_user.getNick());
                adapter=new myInterestAdapter(interests);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
;

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(userProfile.this,chatActivity.class);
                intent.putExtra("friend_id",result_user.getUid());
                intent.putExtra("friend_nick",result_user.getNick());
                intent.putExtra("my_nick",nickname);
                startActivity(intent);
            }
        });

    }



}


