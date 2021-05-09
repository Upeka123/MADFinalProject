package com.example.expensetrackerforuniversitystudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.expensetrackerforuniversitystudent.activity.CategoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivityExpenses extends AppCompatActivity {

    private CardView budgetCardView, QandACard,toDoCard,quizCard;//incomeCardView;
    Toolbar toolbar;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_expenses);

        toolbar=findViewById(R.id.homeToolbar);setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SmartUni");

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();

        budgetCardView =findViewById(R.id.homeBtn);
        QandACard= findViewById(R.id.QandACard);
        toDoCard=findViewById(R.id.toDoCard);
        quizCard=findViewById(R.id.quizCard);
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

        toDoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivityExpenses.this,ToDo_Home_New.class);
                startActivity(intent);
            }


        });

        quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivityExpenses.this, SplashActivity.class);
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

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu,menu);
           return super.onCreateOptionsMenu(menu);
       }
       @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.logOut:
                    sharedPreferences.edit().remove("stuID").apply();

                   mAuth.signOut();
                    Intent intent=new Intent(MainActivityExpenses.this, Log_in.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);finish();

            }
            return super.onOptionsItemSelected(item);
        }
}