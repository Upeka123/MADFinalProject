package com.example.expensetrackerforuniversitystudent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicInteger;

public class QandA extends AppCompatActivity {
    private static final String TAG = "checkedData";
    FloatingActionButton fltBtn;
    FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseDb.collection("Questions");
    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    MaterialButtonToggleGroup materialButtonToggleGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qand_a);
            // Inflate the layout for this fragment

        fltBtn = findViewById(R.id.addQFltBtn);
        fltBtn.setOnClickListener( v -> {
            Intent intent = new Intent(QandA.this,AddQuestion.class);
            startActivity(intent);
        });
        materialButtonToggleGroup = findViewById(R.id.materialButtonToggleGroup);
        materialButtonToggleGroup.setSelectionRequired(true);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipelayout);


        recyclerView = findViewById(R.id.Questions_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                            if(isChecked){
                                if(checkedId == R.id.allBtn){
                                    fetchData("All");
                                }
                                if(checkedId == R.id.myQBtn){
                                    fetchData("My Questions");
                                }
                            }
                        });
                    }
                });

        fetchData("All");//to initially load the data

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                materialButtonToggleGroup.check(R.id.allBtn);
                fetchData("All");
            }
        });
    }
    public void fetchData(String type){
        if(type.equals("All")){
            Query query = collectionReference.orderBy("studentID").whereNotEqualTo("studentID","IT19951100").orderBy("timestamp", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                    .setQuery(query, Question.class)
                    .build();


            questionAdapter = new QuestionAdapter(options);
            // Connecting Adapter class with the Recycler view*/
            recyclerView.setAdapter(questionAdapter);
            questionAdapter.setItemClickListener(new QuestionAdapter.onItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    String id = documentSnapshot.getId();
                    Question q = documentSnapshot.toObject(Question.class);
                    Intent intent = new Intent(QandA.this, SubmitAnswers.class);
                    intent.putExtra("qID",id);
                    intent.putExtra("Question", q);
                    startActivity(intent);
                }
            });
            questionAdapter.startListening();
        }
        if(type.equals("My Questions")){
            Query query = collectionReference.orderBy("timestamp",Query.Direction.DESCENDING).whereEqualTo("studentID","IT19951100");

            FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                    .setQuery(query, Question.class)
                    .build();

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            questionAdapter = new QuestionAdapter(options);
            // Connecting Adapter class with the Recycler view*/
            recyclerView.setAdapter(questionAdapter);
            questionAdapter.setItemClickListener(new QuestionAdapter.onItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    String id = documentSnapshot.getId();
                    Question q = documentSnapshot.toObject(Question.class);
                    Intent intent = new Intent(QandA.this, Answers.class);
                    intent.putExtra("qID",id);
                    intent.putExtra("Question", q);
                    startActivity(intent);
                }
            });
            questionAdapter.startListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        questionAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        questionAdapter.startListening();
    }

}