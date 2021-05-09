package com.example.expensetrackerforuniversitystudent.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.expensetrackerforuniversitystudent.R;
import com.example.expensetrackerforuniversitystudent.adapter.SetsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.expensetrackerforuniversitystudent.SplashActivity.catList;
import static com.example.expensetrackerforuniversitystudent.SplashActivity.selected_cat_index;

public class SetsActivity extends AppCompatActivity {
    GridView setview;
    private FirebaseFirestore firestore;
    private Dialog isLoading;
    TextView textView;
    ImageView back;
    public static List<String> setList = new ArrayList<>();
    public static int category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);







        setview = findViewById(R.id.setview);
        textView = findViewById(R.id.sets);
        back = findViewById(R.id.backbtn);


        isLoading = new Dialog(SetsActivity.this);
        isLoading.setContentView(R.layout.loading_screen);
        isLoading.setCancelable(false);
        isLoading.getWindow().setBackgroundDrawableResource(R.drawable.edittext);
        isLoading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        isLoading.show();

        firestore = FirebaseFirestore.getInstance();

        textView.setText(catList.get(selected_cat_index).getId());

        loadsets();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetsActivity.this, CategoryActivity.class));
                SetsActivity.this.finish();
            }
        });

    }

    public void loadsets() {
        setList.clear();

        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getName())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfSets = (long) documentSnapshot.get("SETS");

                for (int i = 1; i <= noOfSets; i++) {
                    setList.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                }

                SetsAdapter adapter = new SetsAdapter(setList.size());
                setview.setAdapter(adapter);

                isLoading.dismiss();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading.dismiss();
                    }
                });


    }
}