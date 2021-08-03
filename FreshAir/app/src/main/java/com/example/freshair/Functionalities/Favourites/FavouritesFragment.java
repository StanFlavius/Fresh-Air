package com.example.freshair.Functionalities.Favourites;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.freshair.Functionalities.AirPollutionBlog.AirPollutionBlogViewModel;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.R;
import com.example.freshair.Utils.AdapterFavourites;
import com.example.freshair.Utils.AdapterNavigationBar;
import com.example.freshair.Utils.AdapterRecAirPollutionBlog;
import com.example.freshair.Utils.ItemFavourite;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    private List<Favourite> favs;
    private List<ItemFavourite> itemsFavs;
    private FavouritesViewModel vModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterFavourites adapter;
    private AdapterFavourites.OnItemClickListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
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
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton buttonExit;
        buttonExit = view.findViewById(R.id.buttonFavsToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_favouritesFragment_to_homeScreenFragment);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewFavourites);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterFavourites(new ArrayList<ItemFavourite>(), getContext());
        recyclerView.setAdapter(adapter);

        vModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        vModel.init();

        vModel.getFavs().observe((LifecycleOwner) requireContext(), new Observer<List<ItemFavourite>>() {
            @Override
            public void onChanged(List<ItemFavourite> items) {
                listener = new AdapterFavourites.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final NavController navController = Navigation.findNavController(view);
                        //NavDirections action = FavouritesFragmentDirections.ActionFavouritesFragmentToSingleFavouriteFragment;

                        FavouritesFragmentDirections.ActionFavouritesFragmentToSingleFavouriteFragment action =
                                FavouritesFragmentDirections.actionFavouritesFragmentToSingleFavouriteFragment(
                                        items.get(position).getAddress(),
                                        items.get(position).getLatitude(),
                                        items.get(position).getLongitude());

                        navController.navigate(action);
                    }
                };
                adapter.updateData(items, listener);
            }
        });
    }

}