package com.example.expensetrackerforuniversitystudent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerforuniversitystudent.models.Question1Model;
import com.example.expensetrackerforuniversitystudent.activity.Quiz1Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import static com.example.expensetrackerforuniversitystudent.activity.Category1Activity1.catList;
import static com.example.expensetrackerforuniversitystudent.activity.Category1Activity1.selectedCatIndex;
import static com.example.expensetrackerforuniversitystudent.activity.Sets1Activity.seleted_set_index;
import static com.example.expensetrackerforuniversitystudent.activity.Sets1Activity.setList;

public class Question1Adapter extends RecyclerView.Adapter<Question1Adapter.ViewHolder> {
    public Question1Adapter(List<Question1Model> ques_List) {
        this.ques_List = ques_List;
    }

    private List<Question1Model>ques_List;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_items1,parent,false);
        return new ViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(position,this);

    }

    @Override
    public int getItemCount() {
      return  ques_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questiontitle;
        private ImageView deleteQuestionbtn;
        private Dialog isLoading;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questiontitle=itemView.findViewById(R.id.questionId);
            deleteQuestionbtn=itemView.findViewById(R.id.delete_question);

            isLoading=new Dialog(itemView.getContext());
            isLoading.setContentView(R.layout.loader1);
            isLoading.setCancelable(true);
            isLoading.getWindow().setBackgroundDrawableResource(R.drawable.edittext);
            isLoading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        private void setData(int position, Question1Adapter adapter)
        {
        questiontitle.setText("QUESTION " +String.valueOf(position +1));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Quiz1Activity.class);
                    intent.putExtra("ACTION","EDIT");
                    intent.putExtra("Q_ID", position);
                    itemView.getContext().startActivity(intent);
                }
            });
        deleteQuestionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(itemView.getContext()).setTitle("Delete Question")
                        .setMessage("Do you want to delete this  Question ?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteQuestionMethod(position,itemView.getContext(),adapter);

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext);

                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00FFCB"));
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00FFCB"));

            }
        });
        }

        private void deleteQuestionMethod(int pos, Context context,final Question1Adapter adapter)
        {
            isLoading.show();
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
            firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId()).
                    collection(setList.get(seleted_set_index)).document(ques_List.get(pos).getQuetionId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(Void aVoid) {
                    Map<String,Object> catDoc=new ArrayMap<>();
                    int index = 1;
                    for(int i=0;i<ques_List.size(); i++)
                    {
                        if(i!=pos)
                        {


                            catDoc.put("Q"+String.valueOf(index)+"_ID",ques_List.get(i).getQuetionId());
                            index++;
                        }
                    }
                    catDoc.put("COUNT",String.valueOf(index-1));
                    firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                            .collection(setList.get(seleted_set_index)).document("QUESTIONS_LIST").set(catDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Successfully Deleted",Toast.LENGTH_SHORT).show();

                            ques_List.remove(pos);
                            adapter.notifyDataSetChanged();
                            isLoading.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            isLoading.dismiss();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    isLoading.dismiss();

                }
            });
        }
    }
}
