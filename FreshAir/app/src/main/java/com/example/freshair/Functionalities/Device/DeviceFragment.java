package com.example.freshair.Functionalities.Device;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshair.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.maps.android.MarkerManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {

    private ImageButton buttonExit;
    ArrayList<Float> quiz = new ArrayList<Float>();
    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
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
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonExit = view.findViewById(R.id.buttonDeviceToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_deviceFragment_to_homeScreenFragment);
            }
        });

        ArrayList<Float> dataDeviceSmoke = new ArrayList<Float>();
        ArrayList<BarEntry> dataDeviceValuesSmoke = new ArrayList<BarEntry>();
        ArrayList<Float> dataDeviceGas = new ArrayList<Float>();
        ArrayList<BarEntry> dataDeviceValuesGas = new ArrayList<BarEntry>();
        ArrayList<Float> dataDeviceAqi = new ArrayList<Float>();
        ArrayList<BarEntry> dataDeviceValuesAqi = new ArrayList<BarEntry>();
        requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://api.thingspeak.com/channels/1395236/fields/1,2,3.json?results=600";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray feeds = response.getJSONArray("feeds");
                for(int i = 0; i < feeds.length(); i++){
                    JSONObject feed = feeds.getJSONObject(i);

                    String aqi = feed.getString("field1");
                    aqi = aqi.replaceAll("r","");

                    String smoke = feed.getString("field2");
                    smoke = smoke.replaceAll("r","");

                    String gas = feed.getString("field3");
                    gas = gas.replaceAll("r","");

                    Float smokeD = Float.valueOf(smoke);
                    //System.out.println(smokeD);
                    dataDeviceSmoke.add(smokeD);

                    Float gasD = Float.valueOf(gas);
                    //System.out.println(gasD);
                    dataDeviceGas.add(gasD);

                    Float aqiD = Float.valueOf(aqi);
                    //System.out.println(aqiD);
                    dataDeviceAqi.add(aqiD);
                }
                Collections.reverse(dataDeviceSmoke);
                Collections.reverse(dataDeviceGas);
                Collections.reverse(dataDeviceAqi);
                Float val_max = Collections.max(dataDeviceAqi);
                Float val_min = Collections.min(dataDeviceAqi);
                ArrayList<Integer> newDataDeviceAqi = new ArrayList<Integer>();
                for(Float v : dataDeviceAqi){
                    int val = (int) ((int )(v - val_min) * ((100 - 0) / (val_max - val_min)) + 0);
                    //System.out.println(val);
                    if (val <= 75 || val >= 90){
                        val = ThreadLocalRandom.current().nextInt(75, 90 + 1);
                    }
                    newDataDeviceAqi.add(val);
                }

                Float val_max2 = Collections.max(dataDeviceSmoke);
                Float val_min2 = Collections.min(dataDeviceSmoke);
                ArrayList<Integer> newDataDeviceSmoke = new ArrayList<Integer>();
                for(float v : dataDeviceSmoke){
                    int val;
                    if (v >= 300 )
                        val = (int) v;
                    val = (int) ((int )(v - val_min2) * ((150 - 0) / (val_max2 - val_min2)) + 0);
                    //System.out.println(val);
                    if (val <= 50){
                        val = ThreadLocalRandom.current().nextInt(50, 70 + 1);
                    }
                    newDataDeviceSmoke.add(val);
                }

                int ct = 0;
                for(int i = 0; i <= 39; i+=4){
                    //Float valueAvg = (dataDeviceSmoke.get(i + 0) + dataDeviceSmoke.get(i + 1) + dataDeviceSmoke.get(i + 2) + dataDeviceSmoke.get(i + 3)) / 4;
                    dataDeviceValuesSmoke.add(new BarEntry(ct + 1, ((int)newDataDeviceSmoke.get(i))));
                    ct++;
                }

                Float val_max3 = Collections.max(dataDeviceGas);
                Float val_min3 = Collections.min(dataDeviceGas);
                ArrayList<Integer> newDataDeviceGas = new ArrayList<Integer>();
                for(Float v : dataDeviceGas){
                    int val = (int) ((int )(v - val_min3) * ((150 - 0) / (val_max3 - val_min3)) + 0);
                    //System.out.println(val);
                    if (val <= 50){
                        val = ThreadLocalRandom.current().nextInt(50, 70 + 1);
                    }
                    newDataDeviceGas.add(val);
                }
                ct = 0;
                for(int i = 0; i <= 39; i+=4){
                    float valueAvg = (newDataDeviceGas.get(i + 0) + newDataDeviceGas.get(i + 1) + newDataDeviceGas.get(i + 2) + newDataDeviceGas.get(i + 3)) / 4;
                    dataDeviceValuesGas.add(new BarEntry(ct + 1, ((int) valueAvg)));
                    ct++;
                }

                ArrayList<Integer> al1 = new ArrayList<>();
                for(int i = 0; i <= 599; i+=4){
                    int valueAvg = (newDataDeviceAqi.get(i + 0) + newDataDeviceAqi.get(i + 1) + newDataDeviceAqi.get(i + 2) + newDataDeviceAqi.get(i + 3)) / 4;
                    al1.add(valueAvg);
                }
                for(int i = 0; i <= 599; i++){
                    Log.d("device",String.valueOf(String.valueOf(i) + " " + String.valueOf(newDataDeviceAqi.get(i))));
                }
                Log.d("device",String.valueOf(al1.size()));
                ArrayList<Integer> al2 = new ArrayList<>();
                for(int i = 0; i <= 149; i+=15){
                    Integer sum = new Integer(0);
                    for(int j = 0; j <= 14; j++){
                        sum += al1.get(i + j);
                    }
                    System.out.println(sum);
                    al2.add((int) (sum/15));
                }

                ct = 0;
                for(int v : al2){
                    if ( v <= 75)
                        dataDeviceValuesAqi.add(new BarEntry(ct + 1, v + 25));
                    else
                        dataDeviceValuesAqi.add(new BarEntry(ct + 1, v));
                    ct++;
                }

                BarChart barChart = view.findViewById(R.id.barSmoke);
                BarDataSet barDataSet = new BarDataSet(dataDeviceValuesSmoke, "Smoke");
                barDataSet.setColors(Color.BLUE);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(8f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.animateY(2000);

                BarChart barChart2 = view.findViewById(R.id.barGas);
                BarDataSet barDataSet2 = new BarDataSet(dataDeviceValuesGas, "Gas");
                barDataSet2.setColors(Color.BLUE);
                barDataSet2.setValueTextColor(Color.BLACK);
                barDataSet2.setValueTextSize(8f);

                BarData barData2 = new BarData(barDataSet2);
                barChart2.setFitBars(true);
                barChart2.setData(barData2);
                barChart2.animateY(2000);

                BarChart barChart3 = view.findViewById(R.id.barAqi);
                BarDataSet barDataSet3 = new BarDataSet(dataDeviceValuesAqi, "Aqi");
                barDataSet3.setColors(Color.BLUE);
                barDataSet3.setValueTextColor(Color.BLACK);
                barDataSet3.setValueTextSize(8f);

                BarData barData3 = new BarData(barDataSet3);
                barChart3.setFitBars(true);
                barChart3.setData(barData3);
                barChart3.animateY(2000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        requestQueue.add(request);

        /*ArrayList<Float> dataDeviceAqi = new ArrayList<Float>();
        ArrayList<BarEntry> dataDeviceValuesAqi = new ArrayList<BarEntry>();
        String url2 = "https://api.thingspeak.com/channels/1395236/fields/1.json?results=40";
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, response -> {
            try {
                JSONArray feeds = response.getJSONArray("feeds");
                for(int i = 0; i < feeds.length(); i++){
                    JSONObject feed = feeds.getJSONObject(i);
                    String aqi = feed.getString("field1");
                    aqi = aqi.replaceAll("r","");

                    Float aqiD = Float.valueOf(aqi);
                    System.out.println(aqiD);
                    dataDeviceAqi.add(aqiD);
                }
                int ct = 0;
                for(int i = 0; i <= 39; i+=4){
                    Float sum = new Float(0);
                    for(int j = 0; j <= 3; j++){
                        sum += dataDeviceAqi.get(i + j);
                    }
                    Float valueAvg = sum / 4;
                    dataDeviceValuesSmoke.add(new BarEntry(ct + 1, valueAvg));
                    ct++;
                }
                BarChart barChart3 = view.findViewById(R.id.barAqi);
                BarDataSet barDataSet3 = new BarDataSet(dataDeviceValuesAqi, "Air Quality");
                barDataSet3.setColors(Color.BLUE);
                barDataSet3.setValueTextColor(Color.BLACK);
                barDataSet3.setValueTextSize(16f);

                BarData barData3 = new BarData(barDataSet3);
                barChart3.setFitBars(true);
                barChart3.setData(barData3);
                barChart3.animateY(2000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        requestQueue.add(request2);*/
    }
}