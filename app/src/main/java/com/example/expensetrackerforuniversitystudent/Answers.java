package com.example.expensetrackerforuniversitystudent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Answers extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseFirestore firebaseFirestore;
    CollectionReference questionsReference, answersReference;
    TextView idNo, question, dep;
    Button edtBtn, dltBtn;
    AnswerAdapter answerAdapter;
    RecyclerView recyclerView;
    String stuID;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Answers");
        setSupportActionBar(toolbar);

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        stuID = sharedPreferences.getString("stuID", null);

        idNo = findViewById(R.id.answers_question_item_studentID);
        question = findViewById(R.id.answers_question_item_question);
        dep = findViewById(R.id.answers_question_item_dep);

        String qID = getIntent().getStringExtra("qID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        questionsReference = firebaseFirestore.collection("Questions");
        answersReference = firebaseFirestore.collection("Answers");

        answersReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

            }
        });

        Question q = (Question) getIntent().getSerializableExtra("Question");
        idNo.setText(q.getStudentID());
        question.setText(q.getQuestion());
        dep.setText(q.getDepartment());

        recyclerView = findViewById(R.id.answers_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Answers.this));

        edtBtn = findViewById(R.id.EditQuestionBtn);
        dltBtn = findViewById(R.id.DeleteQuestionBtn);

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditQuestion.class);
                intent.putExtra("qID", qID);
                intent.putExtra("Question", q);
                startActivity(intent);
            }
        });

        dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Answers.this);
                builder.setTitle("Do you want to delete this?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        questionsReference.document(qID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Successfully deleted!", Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),QandA.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
            AlertDialog ad = builder.create();
            ad.show();
            }
        });
        Query query = answersReference.whereEqualTo("questionID", qID).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Answer> options = new FirestoreRecyclerOptions.Builder<Answer>()
                .setQuery(query, Answer.class)
                .build();

        answerAdapter = new AnswerAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(answerAdapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.answers_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                recreate();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        answerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        answerAdapter.stopListening();
    }
}