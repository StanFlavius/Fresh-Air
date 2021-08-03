package com.example.freshair.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshair.Models.ModelsAirPollutionNews.Article;
import com.example.freshair.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPollutants extends RecyclerView.Adapter<AdapterPollutants.MyViewHolder> {

    private List<ItemPollutants> items;
    private Context context;
    private AdapterPollutants.OnItemClickListener onItemClickListener;

    public AdapterPollutants(Context context, List<ItemPollutants> itemPollutants){
        this.context = context;

        this.items = itemPollutants;
    }

    @NonNull
    @Override
    public AdapterPollutants.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pollutant, parent, false);

        return new AdapterPollutants.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPollutants.MyViewHolder holders, int position) {
        final AdapterPollutants.MyViewHolder holder = holders;
        ItemPollutants itemPollutants = items.get(position);

        holder.name.setText(itemPollutants.getName());
        holder.value.setText(String.valueOf(itemPollutants.getValue()));
        holder.unitM.setText(String.valueOf(itemPollutants.getUnitM()));
    }

    @Override
    public int getItemCount() {
        return items.toArray().length;
    }

    public void setOnItemClickListener(AdapterPollutants.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, value, unitM;
        AdapterPollutants.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, AdapterPollutants.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.namePoll);
            value = itemView.findViewById(R.id.valuePoll);
            unitM = itemView.findViewById(R.id.unitMPoll);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v){
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void updateData(List<ItemPollutants> items){
        this.items = items;
        notifyDataSetChanged();
    }
}
