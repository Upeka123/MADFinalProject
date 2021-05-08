package com.example.expensetrackerforuniversitystudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {


    private EditText email,password;
    private Button regButton;
    private TextView regQn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        regButton  = findViewById(R.id.regButton);
        regQn    = findViewById(R.id.regQn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        regQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivityExpenses.class);
                startActivity(intent);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if(TextUtils.isEmpty(emailString)){
                    email.setError("Email is required");

                }

                if(TextUtils.isEmpty(passwordString)){
                    email.setError("Password is required");

                }
                else{
                    progressDialog.setMessage("Registration is progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(RegistrationActivity.this, MainActivityExpenses.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();

                            }else{
                                Toast.makeText(RegistrationActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }



                        }

                    });

                }



            }
        });
    }
}
