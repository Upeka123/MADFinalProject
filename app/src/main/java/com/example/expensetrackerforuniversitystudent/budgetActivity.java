package com.example.expensetrackerforuniversitystudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;


import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;

public class budgetActivity extends AppCompatActivity {

    // view
    private TextView totalBudgetAmount;
    private RecyclerView recycleView;
    //
    private FloatingActionButton fab;

    private DatabaseReference budgetRef;
    private FirebaseAuth nAuth;
    private ProgressDialog loader;

    private String post_key = "";
    private String Item = "";
    private int amount = 0;

    private String note = ("");
    Toolbar toolbar;
    private final static String MyPref = "MyPref";
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        toolbar=findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expense Tracker");

        sharedPreferences  = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();

        nAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(nAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        totalBudgetAmount = findViewById(R.id.totalBudgetAmount);
        recycleView = findViewById(R.id.recycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap:snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount += data.getAmount();
                    String sTotal = String.valueOf("Month budget : Rs:" + totalAmount);
                    totalBudgetAmount.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });
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
                Intent intent=new Intent(budgetActivity.this, Log_in.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void additem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemsSpinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);

        final Button cancel = myView.findViewById(R.id.cancelBtn);
        final Button save = myView.findViewById(R.id.saveBtn);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation

                String budgetAmount = amount.getText().toString();
                String budgetItems = itemSpinner.getSelectedItem().toString();
                String budgetNote = note.getText().toString();

                if (TextUtils.isEmpty(budgetAmount)) {

                    amount.setError("Amount is required");
                    return;
                }

                if (budgetItems.equals("Select item")) {
                    Toast.makeText(budgetActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();

                } else {

                    loader.setMessage("adding a budget item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch, now);


                    Data data = new Data(budgetItems, date, id, budgetNote, Integer.parseInt(budgetAmount), months.getMonths());
                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(budgetActivity.this, "Budget item added successfully ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(budgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();

                        }
                    });


                }


                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef, Data.class)
                .build();
        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder,final int position, @NonNull final Data model) {

                myViewHolder.setItemAmount("Allocate amount : Rs."+ model.getAmount());

                myViewHolder.setNote("Note :"+ model.getNote());



                myViewHolder.setDate("On :" + model.getDate() );
                myViewHolder.setItemName("BudgetItem :" + model.getItem() );





                //myViewHolder.note.setVisibility(View.GONE);

                switch ((model.getItem())){
                    case "Transport":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_transport);
                        break;

                    case "Food":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_food);
                        break;

                    case "Boarding Place":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_house);
                        break;

                    case "Entertainment":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;

                    case "Education":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_education);
                        break;

                    case "Charity":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_consultancy);
                        break;

                    case "Health":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_health);
                        break;

                    case "Others":
                        myViewHolder.imageView.setImageResource(R.drawable.ic_other);
                        break;

                }

                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        Item = model.getItem();
                        amount = model.getAmount();
                        note = model.getNote();
                        updateData();
                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);

            }
        };

        recycleView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView note;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            imageView =itemView.findViewById(R.id.Imageview);
            note =itemView.findViewById(R.id.note);
        }
        public void setItemName (String itemName){

            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setNote (String note){

            TextView nNote = mView.findViewById(R.id.note);
            nNote.setText(note);
        }

        public void setItemAmount (String itemAmount){

            TextView item = mView.findViewById(R.id.amount);
            item.setText(itemAmount);
        }

        public void setDate (String itemDate){

            TextView item = mView.findViewById(R.id.date);
            item.setText(itemDate);
        }
    }

    private void updateData(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout,null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        final TextView nItem = mView.findViewById(R.id.itemName);
        final EditText nAmount = mView.findViewById(R.id.amount);
        final EditText nNotes = mView.findViewById(R.id.note);

        // nNotes.setVisibility(View.GONE);

        nItem.setText(Item);

        nAmount.setText(String.valueOf(amount));
        nAmount.setSelection(String.valueOf(amount).length());


        nNotes.setText(String.valueOf(note));
        nNotes.setSelection(String.valueOf(note).length());

        Button delBtn = mView.findViewById(R.id.deleteBtn);
        Button upBtn = mView.findViewById(R.id.updateBtn);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = Integer.parseInt(nAmount.getText().toString());
                String note = nNotes.getText().toString();




                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);


                Data data = new Data(Item, date, post_key,note, amount, months.getMonths());
                budgetRef.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(budgetActivity.this, "Updated successfully ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(budgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }



                    }
                });

                dialog.dismiss();


            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(budgetActivity.this, "Deleted successfully ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(budgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }



                    }
                });

                dialog.dismiss();
            }
        });





        dialog.show();
    }
}