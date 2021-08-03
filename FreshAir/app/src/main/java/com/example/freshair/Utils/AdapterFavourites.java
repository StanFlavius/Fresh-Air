package com.example.freshair.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterFavourites extends RecyclerView.Adapter<AdapterFavourites.MyViewHolder>{
    private List<ItemFavourite> favouritesList;
    private Context context;
    private AdapterFavourites.OnItemClickListener onItemClickListener;

    public AdapterFavourites(List<ItemFavourite> favourites, Context context){
        this.context = context;
        this.favouritesList = favourites;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterFavourites.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourites, parent, false);

        return new AdapterFavourites.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterFavourites.MyViewHolder holders, int position) {
        final AdapterFavourites.MyViewHolder holder = holders;
        ItemFavourite model = favouritesList.get(position);

        holder.address.setText(model.getAddress());
        holder.aqi.setText("Air Quality Index: " + String.valueOf(model.getValue()));
        holder.desc.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return favouritesList.toArray().length;
    }

    /*public void setOnItemClickListener(AdapterFavourites.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }*/

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView address, aqi, desc;
        ImageView imageView;
        AdapterFavourites.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, AdapterFavourites.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            address = itemView.findViewById(R.id.addressListFav);
            aqi = itemView.findViewById(R.id.aqiListFav);
            desc = itemView.findViewById(R.id.descListsFav);
            imageView = itemView.findViewById(R.id.imgListFav);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v){
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void updateData(List<ItemFavourite> favourites, OnItemClickListener onItemClickListener){
        this.favouritesList = favourites;
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }
}
