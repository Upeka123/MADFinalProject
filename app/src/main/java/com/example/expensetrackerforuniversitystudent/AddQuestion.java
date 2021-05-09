package com.example.expensetrackerforuniversitystudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;


public class AddQuestion extends AppCompatActivity {
    //declaring objects;
    Toolbar toolbar;
    Spinner spinner;
    EditText questionInput;
    Button addbtn;
    FirebaseFirestore firebaseDb;
    CollectionReference collectionReference;
    Question question;
    String stuID;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);

        toolbar = findViewById(R.id.toolbar);//setting the toolbar
        toolbar.setTitle("Add a question");
        setSupportActionBar(toolbar);

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        stuID = sharedPreferences.getString("stuID", null);

        //dropdown
        spinner = findViewById(R.id.addQuestionbtnSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.departments, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        questionInput= findViewById(R.id.editTextAddQuestion);
        addbtn = findViewById(R.id.addQuestionbtn);//accessing elements in the xml

        firebaseDb = FirebaseFirestore.getInstance();
        collectionReference = firebaseDb.collection("Questions");//database collection reference
    }//onCreate method

    public void addQuestion(View view) {//method to add a question
        //getting user inputs to variables
        String que = questionInput.getText().toString().trim();
        String dep = spinner.getSelectedItem().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (que.isEmpty()) {
            Toast.makeText(AddQuestion.this, "Question is required", Toast.LENGTH_SHORT).show();
        } else {
            int lastIndexOfQue = que.length() - 1;// to get the last char of the question
            if(!Character.toString(que.charAt(lastIndexOfQue)).equals("?")){//adding ? at the end of the question if there isn't one
                que = que + " ?";
            }
            question = new Question(stuID, que, dep, timestamp.toString());

            collectionReference.add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AddQuestion.this, "Question added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddQuestion.this, QandA.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddQuestion.this, "Failed to add Question!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}