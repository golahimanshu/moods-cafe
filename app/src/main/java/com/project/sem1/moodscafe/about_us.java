package com.project.sem1.moodscafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class about_us extends AppCompatActivity {
    private LinearLayout him_button, him_k_button, js, sahil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
            //getWindow().setTitleColor(Color.BLACK);
        }
        him_button = (LinearLayout)findViewById(R.id.him_button);
        him_k_button = (LinearLayout)findViewById(R.id.him_k_button);
        sahil = (LinearLayout)findViewById(R.id.sahil_button);
        js = (LinearLayout)findViewById(R.id.jas_button);

        him_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(about_us.this,himanshu_gola.class);
                startActivity(i);
            }
        });
        him_k_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(about_us.this,himanshu_kohlil.class);
                startActivity(i);
            }
        });
        sahil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(about_us.this,sahil.class);
                startActivity(i);
            }
        });
        js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(about_us.this,jasmine .class);
                startActivity(i);
            }
        });
    }
}