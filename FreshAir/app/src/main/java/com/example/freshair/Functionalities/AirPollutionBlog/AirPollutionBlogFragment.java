package com.example.freshair.Functionalities.AirPollutionBlog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.freshair.Functionalities.Favourites.FavouritesFragmentDirections;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.R;
import com.example.freshair.Utils.AdapterRecAirPollutionBlog;
import com.example.freshair.Utils.AdapterRecAirPollutionNews;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AirPollutionBlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AirPollutionBlogFragment extends Fragment {

    private AirPollutionBlogViewModel vModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterRecAirPollutionBlog adapter;
    private ImageButton buttonExit, buttonAdd;
    private AdapterRecAirPollutionBlog.OnItemClickListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AirPollutionBlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AirPollutionBlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AirPollutionBlogFragment newInstance(String param1, String param2) {
        AirPollutionBlogFragment fragment = new AirPollutionBlogFragment();
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
        return inflater.inflate(R.layout.fragment_air_pollution_blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonForumToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_airPollutionBlogFragment_to_homeScreenFragment);
            }
        });

        buttonAdd = view.findViewById(R.id.buttonToAddPost);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_airPollutionBlogFragment_to_addPostFragment);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewAirPollutionBlog);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterRecAirPollutionBlog(new ArrayList<Post>(), getContext());
        recyclerView.setAdapter(adapter);

        vModel = ViewModelProviders.of(this).get(AirPollutionBlogViewModel.class);
        vModel.init();

        vModel.getPosts().observe((LifecycleOwner) requireContext(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                listener = new AdapterRecAirPollutionBlog.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final NavController navController = Navigation.findNavController(view);

                        AirPollutionBlogFragmentDirections.ActionAirPollutionBlogFragmentToCompletePostFragment action =
                                AirPollutionBlogFragmentDirections.actionAirPollutionBlogFragmentToCompletePostFragment(
                                  posts.get(position).getFirstName(),
                                  posts.get(position).getLastName(),
                                  posts.get(position).getTitlePost(),
                                  posts.get(position).getContentPost(),
                                        posts.get(position).getCreatedAt(),
                                        posts.get(position).getUrlImage()
                                );

                        navController.navigate(action);
                    }
                };
                adapter.updateData(posts, listener);
            }
        });
    }
}