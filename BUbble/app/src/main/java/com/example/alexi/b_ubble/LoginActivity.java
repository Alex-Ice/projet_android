package com.example.alexi.b_ubble;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private DatabaseReference myDB, mDatabase;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar progBar;
    private final ArrayList<String> list = new ArrayList<String>();
    private String userID,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAuth = FirebaseAuth.getInstance();
        myDB = FirebaseDatabase.getInstance().getReference().child("Users");

        userID = myAuth.getCurrentUser().getUid();
        //userID = myDB.push().getKey();
        mDatabase = myDB.child(userID).child("name");

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

            myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        final Intent intent = new Intent(LoginActivity.this, sea.class);
                        myDB.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if(ds.child("name").getValue() != null) {
                                        list.add(ds.child("name").getValue().toString());
                                        intent.putStringArrayListExtra("list_user",list);

                                    }
                                    else {
                                        Log.d("EmptyData", "EMPTY");
                                    }
                            }
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue() != null) {
                                            username = dataSnapshot.getValue().toString();
                                            Log.d("Username", username);
                                            list.remove(username);

                                            intent.putExtra("usernameL",username);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            progBar.setVisibility(View.GONE);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }else{
                        Toast.makeText(LoginActivity.this, "Wrong email address or password", Toast.LENGTH_LONG).show();
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