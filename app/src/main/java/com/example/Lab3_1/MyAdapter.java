package com.example.Lab3_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Lab3_1.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List itemIDs;
    private List itemFIOs;
    private List itemDates;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textID;
        public TextView textFIO;
        public TextView textDate;

        public MyViewHolder(View v){
            super(v);
            textID = v.findViewById(R.id.textID);
            textFIO = v.findViewById(R.id.textFIO);
            textDate = v.findViewById(R.id.textDate);
        }
    }

    public MyAdapter(List IDs, List FIOs, List Dates){
        itemIDs = IDs;
        itemFIOs = FIOs;
        itemDates = Dates;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = (View) LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textID.setText(""+(long)itemIDs.get(position));
        holder.textFIO.setText((String)itemFIOs.get(position));
        holder.textDate.setText((String)itemDates.get(position));
    }

    @Override
    public int getItemCount() {
        return itemIDs.size();
    }
}
