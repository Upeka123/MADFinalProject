package com.example.expensetrackerforuniversitystudent.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerforuniversitystudent.Category1Adapter;
import com.example.expensetrackerforuniversitystudent.models.Category1Model;
import com.example.expensetrackerforuniversitystudent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Category1Activity1 extends AppCompatActivity {
    RecyclerView category_recycler;
    private Dialog isLoading, addCategoryDialog;
    private FirebaseFirestore firestore;
    private EditText addfield;
    private TextView addcategorybtn, addbtn;
    private Category1Adapter adapter;
    public static List<Category1Model> catList = new ArrayList<>();
    public static int selectedCatIndex = 0;

    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category1);
        category_recycler = findViewById(R.id.categoryRecycler);
        addbtn = findViewById(R.id.addbutton);
        isLoading = new Dialog(Category1Activity1.this);
        isLoading.setContentView(R.layout.loader1);
        isLoading.setCancelable(false);
        isLoading.getWindow().setBackgroundDrawableResource(R.drawable.edittext);
        isLoading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addCategoryDialog = new Dialog(Category1Activity1.this);
        addCategoryDialog.setContentView(R.layout.add_cat_d1);
        addCategoryDialog.setCancelable(true);
        addCategoryDialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext);
        //  addCategoryDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addfield = addCategoryDialog.findViewById(R.id.add_category_name);
        addcategorybtn = addCategoryDialog.findViewById(R.id.add_category_btn);


        firestore = FirebaseFirestore.getInstance();


        addcategorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addfield.getText().toString().isEmpty()) {
                    addfield.setError("Enter Category");
                    return;
                }
                addNewCategory(addfield.getText().toString());
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfield.getText().clear();
                addCategoryDialog.show();
            }
        });


        loadData();

    }


    private void loadData() {
        isLoading.show();
        catList.clear();
        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "dataobject";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        long count = (long) doc.get("COUNT");
                        for (int i = 1; i <= count; i++) {
                            String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME");
                            String catId = doc.getString("CAT" + String.valueOf(i) + "_ID");
                            catList.add(new Category1Model(catId, catName, "0", "1"));
                            Log.d(TAG, "onComplete: "+catId);
                        }

                        adapter = new Category1Adapter(catList);
                        category_recycler.setAdapter(adapter);

                    } else {
                        Toast.makeText(Category1Activity1.this, "No Category Document Exist ! ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(Category1Activity1.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                }
                isLoading.dismiss();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewCategory(String categoryName) {
        addCategoryDialog.dismiss();
        isLoading.show();
        Map<String, Object> catData = new ArrayMap<>();
        catData.put("NAME", categoryName);
        catData.put("SETS", 0);
        catData.put("COUNTER", "1");
        String doc_id = firestore.collection("QUIZ").document().getId();
        firestore.collection("QUIZ").document(doc_id).set(catData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> catDoc = new ArrayMap<>();
                catDoc.put("CAT" + String.valueOf(catList.size() + 1) + "_NAME", categoryName);
                catDoc.put("CAT" + String.valueOf(catList.size() + 1) + "_ID", doc_id);
                catDoc.put("COUNT", catList.size() + 1);
                firestore.collection("QUIZ").document("Categories").update(catDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Category1Activity1.this, "Successfully Add Categry", Toast.LENGTH_SHORT).show();
                        catList.add(new Category1Model(doc_id, categoryName, "0", "1"));
                        adapter.notifyItemInserted(catList.size());
                        isLoading.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Category1Activity1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Category1Activity1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading.dismiss();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}