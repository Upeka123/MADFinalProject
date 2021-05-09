package com.example.expensetrackerforuniversitystudent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.expensetrackerforuniversitystudent.activity.CategoryActivity;
import com.example.expensetrackerforuniversitystudent.models.CategoryModel;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    TextView logo;
    public static List<CategoryModel> catList=new ArrayList<>();
    public static int selected_cat_index=0;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.logo);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splashanimantion);
        logo.setAnimation(animation);
        firestore=FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("QUIZ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }).start();


    }

    private void loadData() {
        catList.clear();
        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists())
                    {
                        long count=(long)doc.get("COUNT");
                        for(int i=1;i<= count;i++)
                        {
                            String catName=doc.getString("CAT"+String.valueOf(i)+"_NAME");
                            String catID=doc.getString("CAT"+String.valueOf(i)+"_ID");
                            catList.add(new CategoryModel(catName,catID));
                        }
                        Intent intent=new Intent(SplashActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this,"No Category Document Exist ! ",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(SplashActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}