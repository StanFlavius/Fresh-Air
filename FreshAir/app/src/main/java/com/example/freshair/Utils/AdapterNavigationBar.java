package com.example.freshair.Utils;

import android.content.ClipData;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.freshair.Models.ModelsAirPollutionNews.Article;
import com.example.freshair.R;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.List;

public class AdapterNavigationBar extends RecyclerView.Adapter<AdapterNavigationBar.MyViewHolder> {

    private List<ItemNavBar> items;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AdapterNavigationBar(Context context, OnItemClickListener listener){
        this.context = context;
        this.onItemClickListener = listener;
        List<ItemNavBar> myItems = new ArrayList<>();

        myItems.add(new ItemNavBar("Favourites", R.drawable.favourites_item));
        myItems.add(new ItemNavBar("Realtime Map", R.drawable.map_item));
        myItems.add(new ItemNavBar("My Device", R.drawable.device_item));
        myItems.add(new ItemNavBar("Forum", R.drawable.forum_item));
        myItems.add(new ItemNavBar("News", R.drawable.news_item));
        myItems.add(new ItemNavBar("Advice Root", R.drawable.road_item));
        myItems.add(new ItemNavBar("Info", R.drawable.info_item));
        myItems.add(new ItemNavBar("Feedback", R.drawable.feedback_item));

        this.items = myItems;
    }

    @NonNull
    @Override
    public AdapterNavigationBar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nav_bar, parent, false);

        return new AdapterNavigationBar.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNavigationBar.MyViewHolder holders, int position) {
        final AdapterNavigationBar.MyViewHolder holder = holders;
        ItemNavBar itemNavBar = items.get(position);

        holder.desc.setText(itemNavBar.getName());
        holder.imageView.setImageResource(itemNavBar.getPath());
    }

    @Override
    public int getItemCount() {
        return items.toArray().length;
    }

    /*public void setOnItemClickListener(AdapterNavigationBar.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }*/

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView desc;
        ImageView imageView;
        AdapterNavigationBar.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, AdapterNavigationBar.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            desc = itemView.findViewById(R.id.textViewItem);
            imageView = itemView.findViewById(R.id.imageViewItem);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v){
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
