package com.example.expensetrackerforuniversitystudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;

public class SubmitAnswers extends AppCompatActivity {
    private static final String TAG = "checkedData";
    Toolbar toolbar;
    TextView idNo,question,dep;
    FirebaseFirestore firebaseFirestore;
    CollectionReference questionReference, answerReference;
    EditText userAnswer;
    Button submitBtn;
    Answer answer;
    AnswerAdapter answerAdapter;
    RecyclerView recyclerView;
    String stuID;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_answers);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Submit answers");
        setSupportActionBar(toolbar);

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        stuID = sharedPreferences.getString("stuID", null);

        recyclerView = findViewById(R.id.submit_answers_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SubmitAnswers.this));

        idNo = findViewById(R.id.submitanswers_question_item_studentID);
        question = findViewById(R.id.submitanswers_question_item_question);
        dep = findViewById(R.id.submitanswers_question_item_dep);
        userAnswer = findViewById(R.id.submit_answers_answer);
        submitBtn = findViewById(R.id.submit_answers_btn);

        String qID = getIntent().getStringExtra("qID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        questionReference = firebaseFirestore.collection("Questions");
        answerReference = firebaseFirestore.collection("Answers");

        answerReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                Log.d(TAG, "Current cites in CA: "+ qID);
            }
        });

        Question q = (Question) getIntent().getSerializableExtra("Question");
        idNo.setText(q.getStudentID());
        question.setText(q.getQuestion());
        dep.setText(q.getDepartment());


        submitBtn.setOnClickListener(v -> {
            String answerInput = userAnswer.getText().toString().trim();

            if(answerInput.isEmpty()){
                Toast.makeText(SubmitAnswers.this, "Enter an answer to submit!",Toast.LENGTH_LONG);
            }else{
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                answer = new Answer(stuID, qID, timestamp.toString(), answerInput);

                answerReference.add(answer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SubmitAnswers.this, "Answer added successfully", Toast.LENGTH_LONG).show();
                        userAnswer.setText("");
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, ""+ e.getMessage());
                    }
                });
            }

        });
        Query query = answerReference.whereEqualTo("questionID", qID).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Answer> options = new FirestoreRecyclerOptions.Builder<Answer>()
                .setQuery(query, Answer.class)
                .build();

        answerAdapter = new AnswerAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(answerAdapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.submit_answers_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                recreate();
            }
        });
    }
//
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