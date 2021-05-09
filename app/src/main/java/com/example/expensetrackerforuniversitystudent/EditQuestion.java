package com.example.expensetrackerforuniversitystudent;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;

public class EditQuestion extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinner;
    EditText editText;
    Button edtBtn;
    FirebaseFirestore firebaseDb;
    CollectionReference collectionReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit question");
        setSupportActionBar(toolbar);

        firebaseDb = FirebaseFirestore.getInstance();
        collectionReference = firebaseDb.collection("Questions");

        spinner = findViewById(R.id.editQuestion_dep);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.departments, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((adapter));

        editText = findViewById(R.id.editQuestion_question);
        edtBtn = findViewById(R.id.editQuestion_edtBtn);

        String qID = getIntent().getStringExtra("qID");
        Question q = (Question) getIntent().getSerializableExtra("Question");
        editText.setText(q.getQuestion());

        int spinnerPosition = 0;
        if("BM".equals(q.getDepartment())){
            spinnerPosition = 1;
        }
        else if("EN".equals(q.getDepartment())){
            spinnerPosition = 2;
        }
        spinner.setSelection(spinnerPosition);

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedQuestion = editText.getText().toString().trim();
                String editedDep = spinner.getSelectedItem().toString();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                Question updatedQuestion = new Question(q.getStudentID(),editedQuestion,editedDep,timestamp.toString());
                collectionReference.document(qID).set(updatedQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),QandA.class);
                                intent.putExtra("qID",qID);
                                intent.putExtra("Question", updatedQuestion);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //
                            }
                        });
            }
        });
    }
}