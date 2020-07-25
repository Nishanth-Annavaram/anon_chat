package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class editprofile extends AppCompatActivity {
    String nickname;
    String interest_string;
    Button nick_button;
    Button interest_button;
    EditText nick;
    EditText interest;
    Button confirm;
    Boolean flag;
    String uid;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    User user;
    usernick userNick;
    EditText aboutu;
    String about;
    ArrayList<String> interests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        databaseReference=FirebaseDatabase.getInstance().getReference();
        flag=false;
        auth=FirebaseAuth.getInstance();


        nick_button=(Button)findViewById(R.id.edp1_nickbutton);
        interest_button=(Button)findViewById(R.id.edp1_addinterestbutton);
        nick=(EditText)findViewById(R.id.edp1_nickedittext);
        interest=(EditText)findViewById(R.id.edp1_interestedittext);
        confirm=(Button)findViewById(R.id.edp1_confirmbutton);
        aboutu=(EditText)findViewById(R.id.editText_aboutyou);
        interests=new ArrayList<String>();



        nick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname=nick.getText().toString();
                if(nickname.isEmpty()){
                    toaster("Please enter a nick..");
                }
                else{
                    flag=true;
                    if(interests.size()==0) {
                        toaster("Add a few interests..");
                    }
                    nick.setText("");
                    nick.setHint("Change nick");
                }
            }
        });

        interest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interest_string=interest.getText().toString().toLowerCase();
                if(interest_string.isEmpty()){
                    toaster("Field is Empty");

                }
                else{
                    interests.add(interest_string);
                    interest_string="";
                    interest.setText("");
                    interest.setHint("Add more interests..");
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    toaster("Please choose a nick..");
                }
                else if(interests.size()==0){
                    toaster("Please choose atleast one interest..");
                }
                else{
                    if(auth.getCurrentUser()==null){
                        toaster("no user logged in!");
                    }else {
                        uid=auth.getCurrentUser().getUid();
                        about=aboutu.getText().toString().trim();

                        user = new User(uid, nickname, interests,about);
                        databaseReference.child("users").child(uid).setValue(user);
                        userNick=new usernick(uid,nickname);
                        databaseReference.child("nicks").child(nickname).setValue(userNick);
                        for (int i = 0; i < interests.size(); i++) {
                            databaseReference.child("interests").child(interests.get(i)).child(nickname).setValue(userNick);
                        }
                        startActivity(new Intent(editprofile.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });








    }


    public void toaster(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

}
