package com.example.alexi.b_ubble;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MyBubble extends AppCompatActivity {

    String arrayName[] = {"B-Humor",
                           "Pictures",
                            "Private Messages",
                            "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bubble);

        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circlemenu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_add_circle_black_36dp, R.drawable.ic_clear_black_36dp)
                .addSubMenu(Color.parseColor("#6DE1D5"), R.drawable.bubble_icon)
                .addSubMenu(Color.parseColor("#20599A"), R.drawable.picture_icon)
                .addSubMenu(Color.parseColor("#0D5FCA"), R.drawable.mp_icon)
                .addSubMenu(Color.parseColor("#EA6E10"), R.drawable.settings_icon1)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if(arrayName[index]=="Settings")
                        {Intent intent = new Intent(MyBubble.this, ParamActivity.class);
                            new CounterTask().execute(intent);
                            }
                        if(arrayName[index]=="Private Messages")
                        {Intent intent = new Intent(MyBubble.this, ChatMP.class);
                            new CounterTask().execute(intent);
                        }

                        Toast.makeText(MyBubble.this, "You selected"+arrayName[index], Toast.LENGTH_SHORT).show();
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
}
