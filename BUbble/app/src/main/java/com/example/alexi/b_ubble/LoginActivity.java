package com.example.alexi.b_ubble;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private DatabaseReference myDB;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        myDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mEmailField = (EditText) findViewById(R.id.editEmail);
        mPasswordField = (EditText) findViewById(R.id.editMdp);
        mLoginBtn = (Button) findViewById(R.id.button2);
        progBar = (ProgressBar) findViewById(R.id.pBar2);

        progBar.setVisibility(View.GONE);

        findViewById(R.id.txtRegister).setOnClickListener(this);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progBar.setVisibility(View.VISIBLE);
                startSignIn();

            }
        });
    }
    private void startSignIn(){

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();

        }else{

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this, sea.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        progBar.setVisibility(View.GONE);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                        progBar.setVisibility(View.GONE);
                    }
                }
            });

        }

    }

    //If the user has not a B-Ubble account (register)
    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.txtRegister:

                startActivity(new Intent(this, RegisterActivity.class));

                break;
        }
    }
}