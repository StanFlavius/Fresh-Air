package com.example.freshair.Functionalities.AdviceRoot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.freshair.BuildConfig;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdviceRootFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdviceRootFragment extends Fragment implements OnMapReadyCallback {

    private ImageButton buttonExit;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    MarkerOptions place1;
    MarkerOptions place2;
    LatLng origin;
    LatLng destination;
    private TextView txt;
    private Polyline currentPolyline;
    private int REQUEST_CODE = 111;
    private AdviceRootViewModel vModel;
    private ArrayList<LatLng> directionPositionList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdviceRootFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdviceRootFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdviceRootFragment newInstance(String param1, String param2) {
        AdviceRootFragment fragment = new AdviceRootFragment();
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
        View view = inflater.inflate(R.layout.fragment_advice_root, container, false);

        return view;
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
                System.out.println(location);
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    origin = latLng;

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                    map.addMarker(markerOptions).showInfoWindow();

                    TileProvider tileProvider = new UrlTileProvider(256, 256) {
                        @Nullable
                        @org.jetbrains.annotations.Nullable
                        @Override
                        public URL getTileUrl(int x, int y, int zoom) {
                            String s = String.format("https://tiles.breezometer.com/v1/air-quality/breezometer-aqi/current-conditions/%d/%d/%d.png?key=0ce43f13b56b428face8a2810d10af21&breezometer_aqi_color=indiper", zoom, x, y);

                            System.out.println(s);
                            if (!checkTileExists(x, y, zoom)) {
                                return null;
                            }
                            try {
                                return new URL(s);
                            } catch (MalformedURLException e) {
                                throw new AssertionError(e);
                            }
                        }

                        private boolean checkTileExists(int x, int y, int zoom) {
                            int minZoom = 1;
                            int maxZoom = 100;

                            return (zoom >= minZoom && zoom <= maxZoom);
                        }
                    };

                    TileOverlay tileOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider).transparency(0.5f));

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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonRouteToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_adviceRootFragment_to_homeScreenFragment);
            }
        });

        vModel = ViewModelProviders.of(this).get(AdviceRootViewModel.class);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map2);

        txt = view.findViewById(R.id.textViewDATATEST);
        vModel.getData().observe((LifecycleOwner) requireContext(), new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> datas) {
                System.out.println(datas);
                String result = new String();
                Integer resultInt = new Integer(0);
                for(Data data : datas){
                    resultInt += data.getIndexes().getBaqi().getAqi();
                }
                resultInt/=10;
                if (resultInt > 80)
                    result = "Excellent Air Quality. The average value is " + String.valueOf(resultInt) + ".";
                if (resultInt <= 80 && resultInt > 60)
                    result = "Good Air Quality. The average value is " + String.valueOf(resultInt) + ".";
                if (resultInt <= 60 && resultInt > 40)
                    result = "Moderate Air Quality. The average value is " + String.valueOf(resultInt) + ".";
                if (resultInt <= 40 && resultInt > 20)
                    result = "Low Air Quality. The average value is " + String.valueOf(resultInt) + ".";
                if (resultInt <= 20)
                    result = "Poor Air Quality. The average value is " + String.valueOf(resultInt) + ".";
                txt.setText(result);
                System.out.println(result);
            }
        });

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        supportMapFragment.getMapAsync(this);

        EditText address = view.findViewById(R.id.textViewGetLocation);
        TextView drawRoad = view.findViewById(R.id.buttonGetLocation);
        drawRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressText = address.getText().toString();
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try{
                    List<Address> addressList = geocoder.getFromLocationName(addressText, 1);
                    if ( addressList.size() == 0)
                        Toast.makeText(getActivity(),"The destination could not be found", Toast.LENGTH_SHORT).show();
                    else{
                        destination = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                        requestDirection();
                    }
                    //Toast.makeText(getActivity(), Double.toString(addressList.get(0).getLatitude()) + " " + Double.toString(addressList.get(0).getLongitude()), Toast.LENGTH_SHORT).show();

                }
                catch (IOException e){
                }
            }
        });
    }

    private void requestDirection(){
        GoogleDirection.withServerKey("AIzaSyDOTU5_Eu2v1catik8wYJ9DhInyjXIU8uI")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable @org.jetbrains.annotations.Nullable Direction direction) {
                        if(direction.isOK()){
                            Route route = direction.getRouteList().get(0);
                            map.addMarker(new MarkerOptions().position(origin));
                            map.addMarker(new MarkerOptions().position(destination));
                            directionPositionList = route.getLegList().get(0).getDirectionPoint();

                            vModel.init(directionPositionList);

                            map.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
                            setCameraWithCoordinatesBounds(route);
                        }
                    }

                    @Override
                    public void onDirectionFailure(@NonNull @NotNull Throwable t) {
                        Toast.makeText(getActivity(), "Esec", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setCameraWithCoordinatesBounds(Route route){
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        map = googleMap;
    }
}