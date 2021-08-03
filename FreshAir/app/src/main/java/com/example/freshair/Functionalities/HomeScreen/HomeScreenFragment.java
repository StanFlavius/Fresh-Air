package com.example.freshair.Functionalities.HomeScreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshair.Functionalities.AirPollutionNews.AirPollutionNewsViewModel;
import com.example.freshair.Models.ModelsAirPollutionNews.Article;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.R;
import com.example.freshair.Utils.AdapterNavigationBar;
import com.example.freshair.Utils.AdapterRecAirPollutionNews;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {

    HomeScreenViewModel vModel;
    FusedLocationProviderClient client;
    LatLng currLocation;
    DecoView arcView;
    Integer baqi;
    private FirebaseAuth mAuth;
    private ImageButton buttonExit;
    TextView address, aqi, desc;
    private int REQUEST_CODE = 111;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterNavigationBar adapter;
    private AdapterNavigationBar.OnItemClickListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreenFragment newInstance(String param1, String param2) {
        HomeScreenFragment fragment = new HomeScreenFragment();
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
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        buttonExit = view.findViewById(R.id.buttonToLogout);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_homeScreenFragment_to_logInFragment);
            }
        });

        address = view.findViewById(R.id.addressHome);
        aqi = view.findViewById(R.id.aqiValHome);
        desc = view.findViewById(R.id.aqiDescHome);

        recyclerView = view.findViewById(R.id.recyclerViewNavBar);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        setOnClickListener();

        adapter = new AdapterNavigationBar(getContext(), listener);
        recyclerView.setAdapter(adapter);

        vModel = ViewModelProviders.of(this).get(HomeScreenViewModel.class);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        arcView = view.findViewById(R.id.dynamicArcView);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    public void setOnClickListener(){
        listener = new AdapterNavigationBar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final NavController navController = Navigation.findNavController(view);
                if (position == 0)
                    navController.navigate(R.id.action_homeScreenFragment_to_favouritesFragment);
                if (position == 1)
                    navController.navigate(R.id.action_homeScreenFragment_to_realtimeMapFragment);
                if (position == 2)
                    navController.navigate(R.id.action_homeScreenFragment_to_deviceFragment);
                if (position == 3)
                    navController.navigate(R.id.action_homeScreenFragment_to_airPollutionBlogFragment);
                if (position == 4)
                    navController.navigate(R.id.action_homeScreenFragment_to_airPollutionNewsFragment);
                if (position == 5)
                    navController.navigate(R.id.action_homeScreenFragment_to_adviceRootFragment);
                if (position == 6)
                    navController.navigate(R.id.action_homeScreenFragment_to_infoPollutansFragment);
                if (position == 7)
                    navController.navigate(R.id.action_homeScreenFragment_to_feedbackFragment);
            }
        };
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    vModel.init(latLng.latitude, latLng.longitude);
                    vModel.getData().observe((LifecycleOwner) requireContext(), new Observer<Data>() {
                        @Override
                        public void onChanged(Data data) {
                            Geocoder geocoder = new Geocoder(getContext());

                            try{
                                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                address.setText(addressList.get(0).getAddressLine(0));
                                aqi.setText(data.getIndexes().getBaqi().getAqi().toString());
                                desc.setText(data.getIndexes().getBaqi().getCategory());
                            }
                            catch (Exception e){
                            }

                            baqi = data.getIndexes().getBaqi().getAqi();
                            //Toast.makeText(getActivity(), data.getIndexes().getBaqi().getAqi_display(), Toast.LENGTH_SHORT).show();

                            //Create data series track
                            String color = new String();
                            if (baqi >= 80)
                                color = "#FF00FF00";
                            else if(baqi >= 60)
                                color = "#ECD400";
                            else if(baqi >= 40)
                                color = "#ECA600";
                            else
                                color = "#EC0000";

                            SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(color))
                                    .setRange(0, 100, 0)
                                    .setLineWidth(80f)
                                    .build();

                            int series1Index = arcView.addSeries(seriesItem1);

                            arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_COLOR_CHANGE, Color.parseColor(color))
                                    .setDelay(0)
                                    .setDuration(500)
                                    .build());

                            arcView.addEvent(new DecoEvent.Builder(baqi).setIndex(series1Index).setDelay(0).build());
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "Activate location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else{
            Toast.makeText(getActivity(), "no permission", Toast.LENGTH_SHORT).show();
        }
    }
}