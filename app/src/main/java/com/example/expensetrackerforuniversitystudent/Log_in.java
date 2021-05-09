package com.example.expensetrackerforuniversitystudent;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Log_in extends AppCompatActivity {


    private static final String TAG = "checkedDat";
    private  TextView register;
    private TextView forgotPass;
    private EditText loginUserId,loginPassword;
    private CheckBox remember;
    private Button logInBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userCollection;
    private String stuId;
    private final static String MyPref = "MyPref";
    private final static String SID = "SID";
    SharedPreferences sharedPreferences;
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        sharedPreferences  = getSharedPreferences(MyPref, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollection = firebaseFirestore.collection("Users");
        loader=new ProgressDialog(this);



        register=findViewById(R.id.registerText);
        forgotPass=findViewById(R.id.forgetPassword);
        loginUserId=findViewById(R.id.userId);
        loginPassword=findViewById(R.id.logInPassword);
        logInBtn=findViewById(R.id.logInBtn);
        remember=findViewById(R.id.rememberMeCheck);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Log_in.this,Register.class);
                startActivity(intent);
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Log_in.this, ResetPassword.class);
                startActivity(intent);
            }
        });


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUserID=loginUserId.getText().toString().trim();
                String  lpass=loginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(loginUserID)){
                    loginUserId.setError("UserId is required");
                    return;

                }
                if(TextUtils.isEmpty(lpass)){
                    loginPassword.setError("Password is required");
                    return;
                }else{
                    loader.setMessage("Loading progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(loginUserID,lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userCollection.whereEqualTo("email", loginUserID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        stuId = document.get("id").toString();
                                                        myEdit.putString("stuID",stuId);
                                                        myEdit.apply();

                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                                Intent intent=new Intent(Log_in.this,MainActivityExpenses.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();

                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(Log_in.this,"Login failed"+error,Toast.LENGTH_SHORT).show();;
                                loader.dismiss();
                            }
                        }
                    });
                }
            }
        });
        SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent=new Intent(Log_in.this,ToDo_Home_New.class);
           startActivity(intent);
        }else if(checkbox.equals("false")){
            Toast.makeText(this,"Please Sign in",Toast.LENGTH_SHORT).show();
        }


       remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(compoundButton.isChecked()){
                   SharedPreferences preferences= getSharedPreferences("checkBox",MODE_PRIVATE);
                   SharedPreferences.Editor editor=preferences.edit();
                   editor.putString("remember","true");
                   Toast.makeText(Log_in.this,"checked",Toast.LENGTH_SHORT).show();
               }else if(!compoundButton.isChecked()){
                   SharedPreferences preferences= getSharedPreferences("checkBox",MODE_PRIVATE);
                   SharedPreferences.Editor editor=preferences.edit();
                   editor.putString("remember","false");
                   Toast.makeText(Log_in.this,"Unchecked",Toast.LENGTH_SHORT).show();
               }

           }
       });
    }



}