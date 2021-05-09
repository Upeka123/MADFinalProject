package com.example.expensetrackerforuniversitystudent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetrackerforuniversitystudent.R;

public class ResultActivity extends AppCompatActivity {
    TextView start, score;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        //Ad inti..



        start = findViewById(R.id.start);
        score = findViewById(R.id.score);
        String score_str = getIntent().getStringExtra("Score");
        score.setText(score_str);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.expensetrackerforuniversitystudent.activity.ResultActivity.this, SetsActivity.class);
                startActivity(intent);
                com.example.expensetrackerforuniversitystudent.activity.ResultActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(com.example.expensetrackerforuniversitystudent.activity.ResultActivity.this, SetsActivity.class));
        com.example.expensetrackerforuniversitystudent.activity.ResultActivity.this.finish();

        super.onBackPressed();
    }
}