package com.example.alexi.b_ubble;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMP extends AppCompatActivity {

    private EditText editMsg;
    private DatabaseReference myDb;
    private DatabaseReference myDb_username;
    private RecyclerView mMsgList;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthListener;
    private FirebaseUser myCurrentUser;
    private DatabaseReference myDBUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_mp);

        editMsg = (EditText) findViewById(R.id.editMsg);
        myDb = FirebaseDatabase.getInstance().getReference().child("Messages");
        myDb_username = FirebaseDatabase.getInstance().getReference().child("Users");

        mMsgList = (RecyclerView) findViewById(R.id.msgrcy);
        mMsgList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMsgList.setLayoutManager(linearLayoutManager);

        myAuth = FirebaseAuth.getInstance();

        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(ChatMP.this, RegisterActivity.class));
                }
            }
        };
    }

    public void sendButtonClicked(View view){
        myCurrentUser = myAuth.getCurrentUser();
        myDBUser = FirebaseDatabase.getInstance().getReference().child("Users").child(myCurrentUser.getUid());
        final String MsgValue = editMsg.getText().toString().trim();
        if(!TextUtils.isEmpty(MsgValue)){
            final DatabaseReference newPost = myDb.push();
            final DatabaseReference newPost_username = myDb_username.push();
            myDBUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("content").setValue(MsgValue);
                    newPost_username.child("Username").setValue(dataSnapshot.child("Username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Automatic scroll when a new msg appears
            mMsgList.scrollToPosition(mMsgList.getAdapter().getItemCount());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(myAuthListener);
        FirebaseRecyclerAdapter <Message,MessageViewHolder> FBRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(

            Message.class,
            R.layout.single_msg_layout,
            MessageViewHolder.class,
            myDb
            )
        {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent((model.getContent()));
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDate(model.getDate());
            }
        };
        mMsgList.setAdapter(FBRecyclerAdapter);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        View myView;
        public MessageViewHolder(View itemView){
            super(itemView);
            myView = itemView;
        }

        public void setContent(String content){
            TextView msg_content = (TextView) myView.findViewById(R.id.msgtxt);
            msg_content.setText(content);
        }

        public void setUsername(String username){
            TextView username_content = (TextView) myView.findViewById(R.id.usernametxt);
            username_content.setText(username);
        }

        public void setDate(String date){
            TextView date_content = (TextView) myView.findViewById(R.id.datetxt);
            date = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy  ", Locale.getDefault()).format(new Date());
            date_content.setText(date);
        }
    }
}
