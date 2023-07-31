package com.hellokhdev.sovary.rustv.tvprogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newdev.beta.rustv.R;

import java.util.ArrayList;

public class HelperAdapter extends RecyclerView.Adapter< HelperAdapter.MyViewClass > {
    ArrayList< String > Chanel_name;
    ArrayList< String > event;
    Context context;

    public HelperAdapter(ArrayList< String > name, ArrayList< String > event, Context context) {
        this.Chanel_name = name;
        this.event = event;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new MyViewClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        holder.Chanel_name.setText(Chanel_name.get(position));
        holder.event.setText(event.get(position));

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return Chanel_name.size();
    }

    public static class MyViewClass extends RecyclerView.ViewHolder{
        TextView Chanel_name;
        TextView event;
        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            Chanel_name = itemView.findViewById(R.id.chanel_name);
            event = itemView.findViewById(R.id.event);
        }
    }

}