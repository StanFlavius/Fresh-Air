package com.example.freshair.Functionalities.AirPollutionNews;

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
import android.widget.Toast;

import com.example.freshair.Functionalities.Favourites.FavouritesFragmentDirections;
import com.example.freshair.Models.ModelsAirPollutionNews.Article;
import com.example.freshair.R;
import com.example.freshair.Utils.AdapterFavourites;
import com.example.freshair.Utils.AdapterNavigationBar;
import com.example.freshair.Utils.AdapterRecAirPollutionBlog;
import com.example.freshair.Utils.AdapterRecAirPollutionNews;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AirPollutionNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AirPollutionNewsFragment extends Fragment {

    private AirPollutionNewsViewModel vModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterRecAirPollutionNews adapter;
    private ImageButton buttonExit;
    private AdapterRecAirPollutionNews.OnItemClickListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AirPollutionNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AirPollutionNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AirPollutionNewsFragment newInstance(String param1, String param2) {
        AirPollutionNewsFragment fragment = new AirPollutionNewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_air_pollution_news, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonNewsToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_airPollutionNewsFragment_to_homeScreenFragment);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewAirPollutionNews);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterRecAirPollutionNews(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        vModel = ViewModelProviders.of(this).get(AirPollutionNewsViewModel.class);
        vModel.init();

        vModel.getArticles().observe((LifecycleOwner) requireContext(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                listener = new AdapterRecAirPollutionNews.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articles.get(position).getUrl()));
                        startActivity(intent);
                    }
                };
                adapter.updateData(articles, listener);
            }
        });
    }

}