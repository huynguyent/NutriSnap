package com.example.huyng.nutrisnap;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huyng.nutrisnap.database.Entry;
import com.example.huyng.nutrisnap.database.EntryRepository;
import com.example.huyng.nutrisnap.database.Food;
import com.example.huyng.nutrisnap.database.FoodRepository;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProgressFragment extends Fragment {
    private final String TAG = "ProgressFragment";
    private final int DEFAULT_RANGE = 7;
    private Activity mContext;
    private Toolbar toolbar;
    private EntryRepository entryRepository;
    private FoodRepository foodRepository;
    private List<BarEntry> chartEntries;
    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        // Get entries from the last 7 days
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        entryRepository = new EntryRepository(mContext);
        foodRepository = new FoodRepository(mContext);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        chartEntries = new ArrayList<BarEntry>();

        // Get data from each day and add to the chart
        for (int i = 0; i <= DEFAULT_RANGE; i++) {
            cal.add(Calendar.DATE, -1);
            Date date = cal.getTime();
            List<Entry> entryList = entryRepository.findEntryByDate(date);
            // Calculate calories sum
            int sum = 0;
            for (Entry entry : entryList) {
                Food food = foodRepository.findFoodByCode(entry.getFoodCode());
                sum += entry.getAmount() *  food.getCalories();
            }
            chartEntries.add(new BarEntry(DEFAULT_RANGE-i, sum, df.format(date)));
            Log.d(TAG, DEFAULT_RANGE-i + " " + sum + " " + df.format(date));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set up tool bar
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);

        // Display bar chart
        BarChart chart = (BarChart) getView().findViewById(R.id.chart);
        BarDataSet dataSet = new BarDataSet(chartEntries, "");
        dataSet.setValueTextSize(15f);
        BarData data = new BarData(dataSet);
        chart.setData(data);
        chart.setFitBars(true);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.invalidate();



    }
    public Toolbar getToolbar() {
        return toolbar;
    }
}
