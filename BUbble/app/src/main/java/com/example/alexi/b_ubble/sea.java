package com.example.alexi.b_ubble;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class sea extends AppCompatActivity {

    private BubblePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea);
        picker = findViewById(R.id.picker);

        final String[] titles = getResources().getStringArray(R.array.planets_array);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final int[] couleurs = {
                Color.parseColor("#B9121B"),
                Color.parseColor("#046380"),
                Color.parseColor("#8E3557"),
                Color.parseColor("#AEEE00")
        };
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
                Intent intent = new Intent(sea.this, MyBubble.class);
                new CounterTask().execute(intent);
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });
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




