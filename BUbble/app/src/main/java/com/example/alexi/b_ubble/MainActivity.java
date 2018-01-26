package com.example.alexi.b_ubble;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    
    private BubblePicker picker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker1 = findViewById(R.id.picker1);

        final String[] titles = {"Bienvenue"};
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        //final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker1.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(MainActivity.this.getApplicationContext(), android.R.color.white));
                //item.setBackgroundImage(ContextCompat.getDrawable(DemoActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });

        picker1.onPause();
        picker1.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                /*Bubble bounce animation*/
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                picker1.startAnimation(myAnim);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                picker1.startAnimation(myAnim);
                Intent intent = new Intent(MainActivity.this, sea.class);
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
        picker1.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker1.onPause();
    }

}



