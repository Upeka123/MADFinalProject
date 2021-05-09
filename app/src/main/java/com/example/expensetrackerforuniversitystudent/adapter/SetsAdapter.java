package com.example.expensetrackerforuniversitystudent.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensetrackerforuniversitystudent.R;
import com.example.expensetrackerforuniversitystudent.activity.QuizActivity;

public class SetsAdapter extends BaseAdapter {
    private int numberofSets;

    public SetsAdapter(int numberofSets) {
        this.numberofSets = numberofSets;
    }

    @Override
    public int getCount() {
        return numberofSets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item, parent, false);
        } else {
            view = convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuizActivity.class);
                intent.putExtra("SETNO", position);
                parent.getContext().startActivity(intent);
            }
        });
        ((TextView) view.findViewById(R.id.setNumber)).setText(String.valueOf(position + 1));

        return view;
    }
}
