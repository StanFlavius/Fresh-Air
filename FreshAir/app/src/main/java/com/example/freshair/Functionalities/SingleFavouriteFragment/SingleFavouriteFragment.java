package com.example.freshair.Functionalities.SingleFavouriteFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
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
import android.widget.TextView;

import com.example.freshair.Functionalities.AirPollutionBlog.AirPollutionBlogViewModel;
import com.example.freshair.Functionalities.AirPollutionBlog.CompletePostFragmentArgs;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsForecast.DataF;
import com.example.freshair.R;
import com.example.freshair.Utils.AdapterHealthRecomm;
import com.example.freshair.Utils.AdapterPollutants;
import com.example.freshair.Utils.AdapterRecAirPollutionBlog;
import com.example.freshair.Utils.ItemHealth;
import com.example.freshair.Utils.ItemPollutants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleFavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleFavouriteFragment extends Fragment {

    private ImageButton buttonExit;
    private DecoView decoView;
    private TextView aqiV, addressTV, desc;
    private Double latitude;
    private Double longitude;
    private String address;
    private SingleFragmentModelView vModel;
    private RecyclerView recyclerViewPoll, recyclerViewHealth;
    private RecyclerView.LayoutManager layoutManagerPoll, layoutManagerHealth;
    private AdapterPollutants adapterPoll;
    private AdapterHealthRecomm adapterHealthRecomm;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SingleFavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleFavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleFavouriteFragment newInstance(String param1, String param2) {
        SingleFavouriteFragment fragment = new SingleFavouriteFragment();
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
        return inflater.inflate(R.layout.fragment_single_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            SingleFavouriteFragmentArgs args = SingleFavouriteFragmentArgs.fromBundle(getArguments());
            latitude = args.getLatitude();
            longitude = args.getLongitude();
            address = args.getAddress();
        }

        buttonExit = view.findViewById(R.id.buttonSingleFavToHome);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_singleFavouriteFragment_to_favouritesFragment);
            }
        });

        recyclerViewPoll = view.findViewById(R.id.recyclerViewPollutants);
        layoutManagerPoll = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPoll.setLayoutManager(layoutManagerPoll);
        adapterPoll = new AdapterPollutants(getContext(), new ArrayList<ItemPollutants>());
        recyclerViewPoll.setAdapter(adapterPoll);

        recyclerViewHealth = view.findViewById(R.id.recyclerViewHR);
        layoutManagerHealth = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHealth.setLayoutManager(layoutManagerHealth);
        adapterHealthRecomm = new AdapterHealthRecomm(getContext(), new ArrayList<ItemHealth>());
        recyclerViewHealth.setAdapter(adapterHealthRecomm);

        vModel = ViewModelProviders.of(this).get(SingleFragmentModelView.class);
        vModel.init(latitude, longitude);

        vModel.getData().observe((LifecycleOwner) requireContext(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {

                decoView = view.findViewById(R.id.favDeco);
                desc = view.findViewById(R.id.favDescription);
                aqiV = view.findViewById(R.id.favValue);
                addressTV = view.findViewById(R.id.favAddress);

                aqiV.setText(data.getIndexes().getBaqi().getAqi_display());
                addressTV.setText(address);
                desc.setText(data.getIndexes().getBaqi().getCategory());

                Integer baqi = data.getIndexes().getBaqi().getAqi();
                //Create data series track
                String color = new String();
                if (baqi >= 80)
                    color = "#FF00FF00";
                else if(baqi >= 60)
                    color = "#ECA600";
                else
                    color = "#EC0000";


                SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor(color))
                        .setRange(0, 100, 0)
                        .setLineWidth(40f)
                        .build();

                int series1Index = decoView.addSeries(seriesItem1);

                decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_COLOR_CHANGE, Color.parseColor(color))
                        .setDelay(0)
                        .setDuration(500)
                        .build());

                decoView.addEvent(new DecoEvent.Builder(data.getIndexes().getBaqi().getAqi()).setIndex(series1Index).setDelay(0).build());

                List<ItemPollutants> itemPollutants = new ArrayList<>();
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getCo().getDisplay_name(),
                        data.getPollutants().getCo().getConcentration().getValue(),
                        data.getPollutants().getCo().getConcentration().getUnits()
                ));
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getNo2().getDisplay_name(),
                        data.getPollutants().getNo2().getConcentration().getValue(),
                        data.getPollutants().getNo2().getConcentration().getUnits()
                ));
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getSo2().getDisplay_name(),
                        data.getPollutants().getSo2().getConcentration().getValue(),
                        data.getPollutants().getSo2().getConcentration().getUnits()
                ));
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getO3().getDisplay_name(),
                        data.getPollutants().getO3().getConcentration().getValue(),
                        data.getPollutants().getO3().getConcentration().getUnits()
                ));
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getPm25().getDisplay_name(),
                        data.getPollutants().getPm25().getConcentration().getValue(),
                        data.getPollutants().getPm25().getConcentration().getUnits()
                ));
                itemPollutants.add(new ItemPollutants(
                        data.getPollutants().getPm10().getDisplay_name(),
                        data.getPollutants().getPm25().getConcentration().getValue(),
                        data.getPollutants().getPm10().getConcentration().getUnits()
                ));
                adapterPoll.updateData(itemPollutants);

                String infoGeneral = new String();
                if (data.getHealth_recommendations().getGeneral_population() == null){
                    infoGeneral = "There is no information for this group of people.";
                }
                else
                    infoGeneral = data.getHealth_recommendations().getGeneral_population();

                String infoChildren = new String();
                if (data.getHealth_recommendations().getChildren() == null){
                    infoChildren = "There is no information for this group of people.";
                }
                else
                    infoChildren = data.getHealth_recommendations().getChildren();

                String infoElderly = new String();
                if (data.getHealth_recommendations().getElderly() == null){
                    infoElderly = "There is no information for this group of people.";
                }
                else
                    infoElderly = data.getHealth_recommendations().getElderly();
                String infoPregnant = new String();
                if (data.getHealth_recommendations().getPregnant_woman() == null){
                    infoPregnant = "There is no information for this group of people.";
                }
                else
                    infoPregnant = data.getHealth_recommendations().getPregnant_woman();
                List<ItemHealth> itemsHealt = new ArrayList<>();
                itemsHealt.add(new ItemHealth("General", R.drawable.general, infoGeneral));
                itemsHealt.add(new ItemHealth("Children", R.drawable.children, infoElderly));
                itemsHealt.add(new ItemHealth("Elderly", R.drawable.elderly, infoChildren));
                itemsHealt.add(new ItemHealth("Pregnant", R.drawable.pregnant, infoPregnant));
                adapterHealthRecomm.updateData(itemsHealt);
            }
        });

        vModel.setDataForecast(latitude, longitude);
        vModel.getDataForecast().observe((LifecycleOwner) requireContext(), new Observer<List<DataF>>() {
            @Override
            public void onChanged(List<DataF> dataF) {
                ArrayList<Integer> dataDeviceF = new ArrayList<Integer>();
                ArrayList<BarEntry> dataDeviceValuesF = new ArrayList<BarEntry>();

                int ct = 0;
                for(DataF d : dataF){
                    dataDeviceF.add(d.getIndexesF().getBaqiF().getAqi());
                    dataDeviceValuesF.add(new BarEntry(ct + 1, d.getIndexesF().getBaqiF().getAqi()));
                    ct++;
                }

                BarChart barChart = view.findViewById(R.id.barForecast);
                BarDataSet barDataSet = new BarDataSet(dataDeviceValuesF, "Air Quality Index Forecast");
                barDataSet.setColors(Color.BLUE);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.animateY(2000);
            }
        });
    }
}