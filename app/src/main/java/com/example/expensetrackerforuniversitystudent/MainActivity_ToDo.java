package com.example.expensetrackerforuniversitystudent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_ToDo extends AppCompatActivity {

    private static final int SPLASH=3300;
    Animation topAnimation,bottomAnimation;
    ImageView logo;
    TextView appName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        logo=findViewById(R.id.logoIcon);
        appName=findViewById(R.id.headingName);

        logo.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity_ToDo.this,Log_in.class);
                startActivity(intent);
                finish();

            }
        },SPLASH);


    }


}