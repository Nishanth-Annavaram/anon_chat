package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class edit_profile2 extends AppCompatActivity {


    Button confirm_button;
    EditText aboutme_line;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    editInterestAdapter adapter;
    String uid;
    Intent intent;
    TextView nickText;
    DatabaseReference databaseReference;
    User user;
    ArrayList<String> interests;
    Button interest_adder;
    String added_interest;
    EditText interest_adder_edittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        setTitle("My Profile");
        intent=getIntent();
        uid=intent.getStringExtra("uid");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_interests_1);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        confirm_button=(Button)findViewById(R.id.confirm_button);
        nickText=(TextView)findViewById(R.id.nick_line_1);
        aboutme_line=(EditText)findViewById(R.id.about_line);
        interest_adder=(Button)findViewById(R.id.button_addinterest);
        interest_adder_edittext=(EditText)findViewById(R.id.editText_add_interesr);
        databaseReference= FirebaseDatabase.getInstance().getReference();




        databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                nickText.setText(user.getNick());
                aboutme_line.setText(user.getAboutme());
                interests=user.getInterests();
                adapter=new editInterestAdapter(interests);
                recyclerView.setAdapter(adapter);

                interest_adder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(interest_adder_edittext.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"Field is empty..",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            added_interest=interest_adder_edittext.getText().toString().toLowerCase();
                            interests.add(added_interest);
                            adapter.setInterests(interests);

                        }
                    }
                });

                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.setAboutme(aboutme_line.getText().toString());
                        user.setInterests(adapter.getInterests());
                        databaseReference.child("users").child(uid).setValue(user);
                        Toast.makeText(getApplicationContext(),"Changes Sucessful",Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
