package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signup;
    Button login;
    EditText password;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup=(Button)findViewById(R.id.signup_button);
        login=(Button)findViewById(R.id.signup_login);
        email=(EditText)findViewById(R.id.signup_email);
        password=(EditText)findViewById(R.id.signup_password);


        mAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_email;
                String s_password;
                s_email=email.getText().toString().trim();
                s_password=password.getText().toString().trim();
                if(s_email.isEmpty()) {
                    email.setError("Email required");
                    email.requestFocus();
                    return;
                }
                if(s_password.isEmpty()){
                    password.setError("Password required");
                    password.requestFocus();
                    return;
                }
                if(s_password.length()<6){
                    password.setError("Password too short");
                    password.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(s_email).matches()){
                    email.setError("Not a valid email :(");
                    email.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(s_email,s_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Registration successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signup.this,editprofile.class));
                            finish();


                        }
                        else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Some error occured :(",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,Login.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
