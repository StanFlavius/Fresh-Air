package com.example.freshair.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshair.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterHealthRecomm extends RecyclerView.Adapter<AdapterHealthRecomm.MyViewHolder> {


    private List<ItemHealth> items;
    private Context context;
    private AdapterHealthRecomm.OnItemClickListener onItemClickListener;

    public AdapterHealthRecomm(Context context, List<ItemHealth> myItems){
        this.context = context;

        this.items = myItems;
    }

    @NonNull
    @Override
    public AdapterHealthRecomm.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_health_recomm, parent, false);

        return new AdapterHealthRecomm.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHealthRecomm.MyViewHolder holders, int position) {
        final AdapterHealthRecomm.MyViewHolder holder = holders;
        ItemHealth itemHealth = items.get(position);

        holder.name.setText(itemHealth.getName());
        holder.image.setImageResource(itemHealth.getPath());
        holder.content.setText(String.valueOf(itemHealth.getContent()));
    }

    @Override
    public int getItemCount() {
        return items.toArray().length;
    }

    public void setOnItemClickListener(AdapterHealthRecomm.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, content;
        ImageView image;
        AdapterHealthRecomm.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, AdapterHealthRecomm.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.nameHealth);
            content = itemView.findViewById(R.id.contentHealth);
            image = itemView.findViewById(R.id.imgHealth);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v){
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void updateData(List<ItemHealth> items){
        this.items = items;
        notifyDataSetChanged();
    }

}
