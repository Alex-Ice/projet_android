package com.example.alexi.b_ubble;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private DatabaseReference dbRef, mDatabase;
    private FirebaseAuth mAuth;
    private String userID;
    private ListView lv;
    private ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //On récupère la ListView que l'on va utiliser
        lv=(ListView)findViewById(R.id.listview_contacts);

        //Utilisation d'un ArrayAdapter pour afficher la liste de tous les contacts (qui est la liste de tous les utilisateurs inscrits)
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        lv.setAdapter(adapter);

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase = dbRef.child(userID).child("name");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentUser = dataSnapshot.getValue().toString();
                        list.remove(currentUser);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.child("name").getValue() != null) {
                            list.add(ds.child("name").getValue().toString());
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Log.d("EmptyData", "EMPTY");
                        }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("name");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String interlocutor = ((TextView)view).getText().toString();
                Intent intent = new Intent(Contacts.this, ChatMP.class);
                intent.putExtra("friend",interlocutor);
                startActivity(intent);
            }
        });

    }
}
