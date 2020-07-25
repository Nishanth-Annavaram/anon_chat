package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myInterestAdapter extends RecyclerView.Adapter<myInterestAdapter.ViewHolder>{

    ArrayList<String> interests;


    public myInterestAdapter(ArrayList<String> interests) {
        this.interests = interests;
    }

    @NonNull
    @Override
    public myInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_tem,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myInterestAdapter.ViewHolder holder, int position) {
        holder.interest.setText(interests.get(position));

    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView interest;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interest=itemView.findViewById(R.id.interest_body_1);

        }
    }


}