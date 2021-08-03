package com.example.freshair.Functionalities.RealtimeMap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshair.Functionalities.AdviceRoot.AdviceRootViewModel;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.R;
import com.example.freshair.Repo.RealtimeMapRepo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RealtimeMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealtimeMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap map;
    private Dialog mDialog;
    private ImageButton buttonExit;
    private RealtimeMapModelView vModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<LatLng> dataCoordinates = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RealtimeMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RealtimeMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RealtimeMapFragment newInstance(String param1, String param2) {
        RealtimeMapFragment fragment = new RealtimeMapFragment();
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
        View view = inflater.inflate(R.layout.fragment_realtime_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonRealtimeToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_realtimeMapFragment_to_homeScreenFragment);
            }
        });

        mDialog = new Dialog(getContext());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapClick(@NonNull @NotNull LatLng latLng) {
        //Toast.makeText(getActivity(), Double.toString(latLng.latitude) + " " + Double.toString(latLng.longitude), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.popup, null);
        TextView addressV = mView.findViewById(R.id.addressFav);
        TextView valueV = mView.findViewById(R.id.valueFav);
        ImageView img = mView.findViewById(R.id.heartImg);

        vModel = ViewModelProviders.of(this).get(RealtimeMapModelView.class);
        vModel.init(latLng.latitude, latLng.longitude);
        vModel.getData().observe((LifecycleOwner) requireContext(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                Geocoder geocoder = new Geocoder(getContext());

                try{
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    addressV.setText(addressList.get(0).getAddressLine(0));
                    valueV.setText(data.getIndexes().getBaqi().getAqi().toString());
                }
                catch (Exception e){
                }
            }
        });


        vModel.getFav().observe((LifecycleOwner) requireContext(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> addresses) {
                Geocoder geocoder = new Geocoder(getContext());
                try{
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    int pp = 0;
                    for (String address : addresses){
                        if (address.equals(addressList.get(0).getAddressLine(0))){
                            pp = 1;
                            break;
                        }
                    }
                    if (pp == 0){
                        img.setImageResource(R.drawable.no_heart);
                    }
                    else{
                        img.setImageResource(R.drawable.favourites_item);
                    }
                }
                catch (Exception e){
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try{
                            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            int pp2 = 0;
                            for (String address : addresses){
                                if (address.equals(addressList.get(0).getAddressLine(0))){
                                    pp2 = 1;
                                    break;
                                }
                            }
                            if (pp2 == 0){
                                img.setImageResource(R.drawable.favourites_item);
                                Favourite fav = new Favourite(
                                            mAuth.getCurrentUser().getUid(),
                                            addressList.get(0).getAddressLine(0),
                                            latLng.latitude,
                                            latLng.longitude
                                    );
                                    vModel.addFav(fav);
                                Toast.makeText(getContext(), "Added new favourite location", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                img.setImageResource(R.drawable.no_heart);
                                vModel.deleteFav(addressList.get(0).getAddressLine(0), mAuth.getCurrentUser().getUid());
                                Toast.makeText(getContext(), "Location deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                        }
                    }
                });
            }
        });

        builder.setView(mView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setOnMapClickListener(this);
        /*try {
            List<Address> address = new Geocoder(getContext()).getFromLocationName("Romania", 1);
            if (address == null) {
            } else {
                Address loc = address.get(0);
                LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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
        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                System.out.println(location);
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }
                else{
                    Toast.makeText(getActivity(), "Activate location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String s = String.format("https://tiles.breezometer.com/v1/air-quality/breezometer-aqi/current-conditions/%d/%d/%d.png?key=0ce43f13b56b428face8a2810d10af21&breezometer_aqi_color=indiper", zoom, x, y);

                System.out.println(s);
                if(!checkTileExists(x, y, zoom)){
                    return null;
                }
                try{
                    return new URL(s);
                }catch (MalformedURLException e){
                    throw new AssertionError(e);
                }
            }

            private boolean checkTileExists(int x, int y, int zoom){
                int minZoom = 1;
                int maxZoom = 300;

                return (zoom >= minZoom && zoom <= maxZoom);
            }
        };
        TileOverlay tileOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider).transparency(0.5f));
    }
}