package com.example.alexi.b_ubble;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    Button logout, param;
    FirebaseAuth user = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logout = (Button) findViewById(R.id.buttonLogOut);
        param = (Button) findViewById(R.id.buttonParam);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.signOut();

                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
        });

        param.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AccountActivity.this, ParamActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        user.signOut();

        startActivity(new Intent(AccountActivity.this, sea.class));
    }
}