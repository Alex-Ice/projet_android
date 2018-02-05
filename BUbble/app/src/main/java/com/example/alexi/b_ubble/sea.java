package com.example.alexi.b_ubble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class sea extends AppCompatActivity {

    private BubblePicker picker;
    private String  username;
    private String profileImageUrl;
    private final ArrayList<String> list = new ArrayList<String>();
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea);
        picker = findViewById(R.id.picker);

        //We catch the name of the current user in the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("usernameR") != "") {
                username = extras.getString("usernameR");
            }
            if(extras.getString("usernameL") != "") {
                username = extras.getString("usernameL");
            }
            Toast.makeText(sea.this, "Welcome  " + username, Toast.LENGTH_LONG).show();
        }
        list.add(username);
        DrawBubble(list);

    }

    class CounterTask extends AsyncTask<Intent, Void, Intent> {
        @Override
        protected Intent doInBackground(Intent... intents) {
            int a = 0;
            while (a != 200000000) {
                a++;
            } //environ 2 secondes
            return intents[0];
        }

        protected void onPostExecute(Intent result) {
            startActivity(result);
        }
    }


    public void DrawBubble(ArrayList<String> mList) {
        if (mList != null) {
            //List are more scalable
            mList.add("User1");
            mList.add("User2");
            mList.add("User3");

            String[] titles = new String[mList.size()];
            titles = mList.toArray(titles);

            final TypedArray colors = getResources().obtainTypedArray(R.array.colors);

            //final TypedArray images = getResources().obtainTypedArray(R.array.images);

            ArrayList<PickerItem> listItems = new ArrayList<PickerItem>() {};

            for (int i = 0; i < titles.length; i++) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[i]);
                item.setTextColor(ContextCompat.getColor(sea.this.getApplicationContext(), android.R.color.white));

                item.setGradient(new BubbleGradient(colors.getColor((i * 2) % 8, 0),
                        colors.getColor((i * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                listItems.add(item);
            }
            picker.setItems(listItems);

            //To get all selected items picker.getSelectedItems() method in Java.

            picker.setListener(new BubblePickerListener() {
                @Override
                public void onBubbleSelected(@NotNull PickerItem item) {
                    //If we click on our bubble, we go to our settings
                    Intent intent = new Intent(sea.this, MyBubble.class);
                    new CounterTask().execute(intent);
                }

                @Override
                public void onBubbleDeselected(@NotNull PickerItem item) {

                }
            });
        }
        else {
            Log.d("ArrayListBubblePicker", "EMPTY BUBBLES");
            Intent intent = new Intent(sea.this, MyBubble.class);
            new CounterTask().execute(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }
}




