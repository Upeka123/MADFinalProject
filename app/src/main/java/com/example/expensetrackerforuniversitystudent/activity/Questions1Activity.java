package com.example.expensetrackerforuniversitystudent.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerforuniversitystudent.models.Question1Model;
import com.example.expensetrackerforuniversitystudent.Question1Adapter;
import com.example.expensetrackerforuniversitystudent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.expensetrackerforuniversitystudent.activity.Category1Activity1.catList;
import static com.example.expensetrackerforuniversitystudent.activity.Category1Activity1.selectedCatIndex;
import static com.example.expensetrackerforuniversitystudent.activity.Sets1Activity.seleted_set_index;
import static com.example.expensetrackerforuniversitystudent.activity.Sets1Activity.setList;

public class Questions1Activity extends AppCompatActivity {
    RecyclerView question_recycler;
    TextView addquestion;
    private Dialog isLoading;
    private Question1Adapter adapter;
    private FirebaseFirestore firestore;

    public static List<Question1Model> questionList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions1);
        question_recycler=findViewById(R.id.questionRecycler);
        addquestion=findViewById(R.id.addquestionbutton);



        isLoading=new Dialog(Questions1Activity.this);
        isLoading.setContentView(R.layout.loader1);
        isLoading.setCancelable(false);
        isLoading.getWindow().setBackgroundDrawableResource(R.drawable.edittext);
        isLoading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Questions1Activity.this, Quiz1Activity.class);
                intent.putExtra("ACTION","ADD");

                startActivity(intent);

            }
        });

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        question_recycler.setLayoutManager(layoutManager);

        firestore=FirebaseFirestore.getInstance();
        loadData();

    }

    private void loadData() {

        questionList.clear();
        isLoading.show();

        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId()).
                collection(setList.get(seleted_set_index)).get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String,QueryDocumentSnapshot> docList=new ArrayMap<>();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    docList.put(doc.getId(),doc);
                }
                QueryDocumentSnapshot quesListDoc=  docList.get("QUESTIONS_LIST");

                String count=quesListDoc.getString("COUNT");
                for ( int i=0;i< Integer.valueOf(count);i++)
                {
                    String questionId=quesListDoc.getString("Q"+ String.valueOf(i + 1) +"_ID");
                    QueryDocumentSnapshot quesDoc=docList.get(questionId);
                    questionList.add(new Question1Model(
                            questionId,
                       quesDoc.getString("QUESTION"),
                            quesDoc.getString("A"),
                            quesDoc.getString("B"),
                            quesDoc.getString("C"),
                            quesDoc.getString("D"),
                            Integer.valueOf(quesDoc.getString("ANSWER"))
                    ));
                }
                adapter=new Question1Adapter(questionList);
                question_recycler.setAdapter(adapter);
                isLoading.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Questions1Activity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

isLoading.dismiss();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}