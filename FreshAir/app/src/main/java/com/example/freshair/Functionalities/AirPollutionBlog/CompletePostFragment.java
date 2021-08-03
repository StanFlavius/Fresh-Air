package com.example.freshair.Functionalities.AirPollutionBlog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.freshair.R;
import com.example.freshair.Utils.Utils;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletePostFragment extends Fragment {

    private ImageButton buttonExit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletePostFragment newInstance(String param1, String param2) {
        CompletePostFragment fragment = new CompletePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonCompletePostToForum);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_completePostFragment_to_airPollutionBlogFragment);
            }
        });

        String firstName = new String();
        String lastName = new String();
        String titlePost = new String();
        String contentPost = new String();
        String createdAt = new String();
        String urlImage = new String();

        if (getArguments() != null){
            CompletePostFragmentArgs args = CompletePostFragmentArgs.fromBundle(getArguments());
            firstName = args.getFirstName();
            lastName = args.getLastName();
            titlePost = args.getTitlePost();
            contentPost = args.getContentPost();
            createdAt = args.getCreatedAt();
            urlImage = args.getUrlImage();
        }

        TextView title = view.findViewById(R.id.completePostTitle);
        title.setText(titlePost);

        TextView author = view.findViewById(R.id.authorCompletePost);
        author.setText(firstName + " " + lastName);

        TextView date = view.findViewById(R.id.publishedAtCompletPost);
        date.setText(createdAt);

        TextView desc = view.findViewById(R.id.descCompletePost);
        desc.setText(contentPost);

        ImageView imageView = view.findViewById(R.id.imageCompletePost);

        ProgressBar progressBar = view.findViewById(R.id.progress_load_photo_complete);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(getContext())
                .load(urlImage)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}