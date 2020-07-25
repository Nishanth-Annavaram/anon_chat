package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class editInterestAdapter extends RecyclerView.Adapter<editInterestAdapter.ViewHolder>{

    ArrayList<String> interests;


    public editInterestAdapter(ArrayList<String> interests) {
        this.interests = interests;
    }

    @NonNull
    @Override
    public editInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_interest_item,parent,false);
        return new editInterestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editInterestAdapter.ViewHolder holder, final int position) {
        holder.interest.setText(interests.get(position));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interests.remove(position);
                editInterestAdapter.this.notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView interest;
        Button remove;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interest=itemView.findViewById(R.id.interest_body_1);
            remove=itemView.findViewById(R.id.remove_button);

        }
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
        editInterestAdapter.this.notifyDataSetChanged();
    }
}