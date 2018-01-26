package com.example.alexi.b_ubble;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MyBubble extends AppCompatActivity {

    String arrayName[] = {"Settings",
                           "Private Messages",
                            "Pictures",
                            "B-Humor"};

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
                        Toast.makeText(MyBubble.this, "You selected"+arrayName[index], Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
