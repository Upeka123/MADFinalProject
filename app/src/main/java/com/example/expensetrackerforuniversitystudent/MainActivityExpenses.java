package com.example.expensetrackerforuniversitystudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivityExpenses extends AppCompatActivity {

    private CardView budgetCardView, QandACard;//incomeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_expenses);

        budgetCardView =findViewById(R.id.homeBtn);
        QandACard= findViewById(R.id.QandACard);
       //incomeCardView = findViewById(R.id.incomeCardView);

        budgetCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivityExpenses.this,budgetActivity.class);
                startActivity(intent);
            }


        });
        QandACard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivityExpenses.this,QandA.class);
                startActivity(intent);
            }


        });
       // incomeCardView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent =new Intent(MainActivity.this,IncomeActivity.class);
          //     startActivity(intent);
        //    }
      //  });
    }
}