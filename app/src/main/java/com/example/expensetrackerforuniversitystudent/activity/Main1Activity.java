package com.example.expensetrackerforuniversitystudent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetrackerforuniversitystudent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Main1Activity extends AppCompatActivity {

    EditText id, pass;
    Button login;
    private FirebaseAuth mAuth;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        id = findViewById(R.id.id);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.btLogin);

        mAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginUserID=id.getText().toString().trim();
                String  lpass=pass.getText().toString().trim();

                if(TextUtils.isEmpty(loginUserID)){
                    id.setError("UserId is required");
                    return;

                }
                if(TextUtils.isEmpty(lpass)){
                    pass.setError("Password is required");
                    return;
                }



                    mAuth.signInWithEmailAndPassword(loginUserID,lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d("abc",user.getEmail().toString());
                                Intent intent=new Intent(Main1Activity.this,Category1Activity1.class);
                                startActivity(intent);
                                finish();

                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(Main1Activity.this,"Login faild",Toast.LENGTH_SHORT).show();;

                            }
                        }
                    });






            }
        });






    }

    }
