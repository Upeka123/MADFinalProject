package com.example.expensetrackerforuniversitystudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class QuestionAdapter extends FirestoreRecyclerAdapter <Question, QuestionAdapter.questionsViewholder> {
    private onItemClickListener listener;
    public QuestionAdapter(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionAdapter.questionsViewholder holder, int position, @NonNull Question model) {
        holder.studentID.setText(model.getStudentID());
        holder.question.setText(model.getQuestion());
        holder.department.setText(model.getDepartment());
    }

    public questionsViewholder onCreateViewHolder(@NonNull ViewGroup parent,int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionAdapter.questionsViewholder(view);
    }

    public class questionsViewholder extends RecyclerView.ViewHolder{
        TextView studentID, question, department;
        public questionsViewholder(@NonNull View itemView) {
            super(itemView);
            studentID = itemView.findViewById(R.id.question_item_studentID);
            question = itemView.findViewById(R.id.question_item_question);
            department =itemView.findViewById(R.id.question_item_dep);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }
    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
