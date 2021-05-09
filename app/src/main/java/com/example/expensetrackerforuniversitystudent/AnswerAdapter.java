package com.example.expensetrackerforuniversitystudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AnswerAdapter extends FirestoreRecyclerAdapter<Answer, AnswerAdapter.answersViewholder> {
    public AnswerAdapter(@NonNull FirestoreRecyclerOptions<Answer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnswerAdapter.answersViewholder holder, int position, @NonNull Answer model) {
        holder.idNo.setText(model.getIdNo());
        holder.ans.setText(model.getAnswer());
    }

    @NonNull
    @Override
    public answersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new AnswerAdapter.answersViewholder(view);
    }

    public class answersViewholder extends RecyclerView.ViewHolder {
        TextView idNo, ans;
        public answersViewholder(@NonNull View itemView) {
            super(itemView);
            idNo = itemView.findViewById(R.id.answer_item_studentID);
            ans = itemView.findViewById(R.id.answer_item_answer);
        }
    }
}
