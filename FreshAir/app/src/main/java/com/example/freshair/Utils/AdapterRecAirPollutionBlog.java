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
import com.example.freshair.ModelsAirPollutionNews.Article;
import com.example.freshair.ModelsBlog.Post;
import com.example.freshair.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterRecAirPollutionBlog extends RecyclerView.Adapter<AdapterRecAirPollutionBlog.MyViewHolder> {

    private List<Post> postsList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AdapterRecAirPollutionBlog(List<Post> posts, Context context){
        this.context = context;
        this.postsList = posts;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterRecAirPollutionBlog.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_air_pollution_blog, parent, false);

        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Post model = postsList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitlePost());
        holder.description.setText(model.getContentPost());
        holder.publishedAt.setText(model.getCreatedAt());
        holder.author.setText(model.getFirstName() + " " + model.getLastName());
    }

    @Override
    public int getItemCount() {
        return postsList.toArray().length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, description, author, publishedAt;
        ImageView imageView;
        ProgressBar progressBar;
        AdapterRecAirPollutionBlog.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, AdapterRecAirPollutionBlog.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title_blog);
            description = itemView.findViewById(R.id.desc_blog);
            author = itemView.findViewById(R.id.author_blog);
            publishedAt = itemView.findViewById(R.id.publishedAt_blog);
            imageView = itemView.findViewById(R.id.imageBlog);
            progressBar = itemView.findViewById(R.id.progress_load_photo_blog);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v){
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void updateData(List<Post> posts){
        this.postsList = posts;
        notifyDataSetChanged();
    }
}

