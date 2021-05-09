package com.example.expensetrackerforuniversitystudent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity{

    private EditText userId,studentId;
    private EditText password;
    private Button save;
    private TextView login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userCollection;

    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;

    private ProgressDialog loader;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollection = firebaseFirestore.collection("Users");
        loader=new ProgressDialog(this);

        save=(Button)findViewById(R.id.regSaveBtn);
        login=(TextView) findViewById(R.id.regLogInText);
        userId=(EditText)findViewById(R.id.regUserId);
        password=(EditText)findViewById(R.id.regPassword);
        studentId=(EditText)findViewById(R.id.regId);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this, Log_in.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID=userId.getText().toString().trim();
                String  pass=password.getText().toString().trim();
                String stuId=studentId.getText().toString().trim();


                if(TextUtils.isEmpty(userID)){
                    userId.setError("UserId is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("password is required");
                    return;
                }
                if(TextUtils.isEmpty(stuId)){
                    studentId.setError("Student ID is required");
                    return;
                }
                if(!(stuId.length() == 10)){
                    studentId.setError("Enter a valid Student ID");
                    return;
                }



                else{
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(userID,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                myEdit.putString("stuID",stuId);
                                myEdit.apply();
                                User user = new User(userID,stuId);
                                userCollection.add(user);

                                Intent intent = new Intent(Register.this,MainActivityExpenses.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }else {
                                String error=task.getException().toString();
                                Toast.makeText(Register.this,"Registration fail"+error,Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });


                }



            }
        });



    }



}