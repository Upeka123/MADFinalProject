package com.example.expensetrackerforuniversitystudent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class ToDo_Home_New extends AppCompatActivity {
   private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;


    private String key="";
    private String task;
    private String description;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;
    //SharedPreferences



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do__home__new);

        toolbar=findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ToDo List");


        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID=mUser.getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);


        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener((v) -> {addTask();});



    }



    private void addTask() {
        AlertDialog.Builder mydialog= new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_todo,null);
        mydialog.setView(myView);

        AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);

        final EditText task =myView.findViewById(R.id.addTask);
        final EditText description = myView.findViewById(R.id.addDiscription);
        Button save=myView.findViewById(R.id.saveBtn);
        Button cancel=myView.findViewById(R.id.canselBtn);

        cancel.setOnClickListener((v)->{dialog.dismiss();});

        save.setOnClickListener((v)->{
            String mTask=task.getText().toString().trim();
            String mDescription=description.getText().toString().trim();
            String id=reference.push().getKey();
            String date= DateFormat.getDateInstance().format(new Date());
            if(TextUtils.isEmpty(mTask)){
                task.setError("Task Required");
                return;

            }
            if(TextUtils.isEmpty(mDescription)){
                description.setError("Description Required");
                return;
            }else{
                loader.setMessage("Adding your date");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                Model_ToDo model=new Model_ToDo(mTask,mDescription,id,date);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(ToDo_Home_New.this,"Task has been inserted successfully",Toast.LENGTH_SHORT).show();
                          loader.dismiss();
                      }else{
                          String error = task.getException().toString();
                          Toast.makeText(ToDo_Home_New.this,"Failed"+error,Toast.LENGTH_SHORT).show();
                          loader.dismiss();
                      }

                    }
                });
            }


            dialog.dismiss();
        });

        dialog.show();

    }
    protected void onStart(){
     super.onStart();

        FirebaseRecyclerOptions<Model_ToDo> options=new FirebaseRecyclerOptions.Builder<Model_ToDo>()
                .setQuery(reference, Model_ToDo.class)
                .build();
        FirebaseRecyclerAdapter<Model_ToDo,MyViewHolder> adapter= new FirebaseRecyclerAdapter<Model_ToDo, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Model_ToDo model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key=getRef(position).getKey();
                        task=model.getTask();
                        description=model.getDescription();

                        updateTask();
                    }
                });



            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
                return  new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
            View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTask(String task){
            TextView taskTectView=mView.findViewById(R.id.taskTv);
            taskTectView.setText(task);
        }
        public void setDesc(String desc){
            TextView descTecView=mView.findViewById(R.id.descriptionTv);
            descTecView.setText(desc);
        }
        public void setDate(String date){
            TextView dateTextView= mView.findViewById(R.id.dateTv);
            dateTextView.setText(date);
        }
    }
    private void updateTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_data,null);
        myDialog.setView(view);

        AlertDialog dialog=myDialog.create();
        dialog.show();
        EditText mTask=view.findViewById(R.id.mEditTextTask);
        EditText mDescription=view.findViewById(R.id.mEditTextDescription);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button delButton=view.findViewById(R.id.btnDelete);
        Button updateButton=view.findViewById(R.id.btnUpdate);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task=mTask.getText().toString().trim();
                description=mDescription.getText().toString().trim();

                String date=DateFormat.getDateInstance().format(new Date());
                Model_ToDo model=new Model_ToDo(task,description,key,date);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ToDo_Home_New.this,"Data has been updated successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            String err= task.getException().toString();
                            Toast.makeText(ToDo_Home_New.this,"Update failled"+err,Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.dismiss();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ToDo_Home_New.this,"Deleted successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            String err=task.getException().toString();
                            Toast.makeText(ToDo_Home_New.this,"Failed to delete"+err,Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                sharedPreferences.edit().remove("stuID").apply();

                mAuth.signOut();
                Intent intent=new Intent(ToDo_Home_New.this, Log_in.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}