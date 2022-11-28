package com.zzrong.badminton_analyzer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.zzrong.badminton_analyzer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StatisticFragment extends Fragment {

    HorizontalBarChart chartTop;
    HorizontalBarChart chartBot;


    public static StatisticFragment newInstance(HashMap<String, Integer> blue, HashMap<String, Integer> red){
        StatisticFragment frag = new StatisticFragment();

        Bundle args = new Bundle();
        args.putSerializable("blue",blue);
        args.putSerializable("red",red);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);
        return setChart(v);
    }

    public View setChart(View view){
        chartTop = view.findViewById(R.id.barChart_up);
        chartTop.setScaleEnabled(false);
        setData(chartTop,true);

        chartBot = view.findViewById(R.id.barChart_down);
        chartBot.setScaleEnabled(false);
        setData(chartBot,false);
        return view;
    }

    public void setData(BarChart barChart,boolean isTopPlayer){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("","撲球","平球","小球","挑球","殺球","切球","長球"));
        HashMap<String, Integer> map;
        if(isTopPlayer){
            map = (HashMap<String, Integer>) getArguments().getSerializable("blue");
        }
        else{
            map = (HashMap<String, Integer>) getArguments().getSerializable("red");
        }

        entries.add(new BarEntry(1, map.get("撲球")));
        entries.add(new BarEntry(2,map.get("平球")));
        entries.add(new BarEntry(3,map.get("小球")));
        entries.add(new BarEntry(4,map.get("挑球")));
        entries.add(new BarEntry(5,map.get("殺球")));
        entries.add(new BarEntry(6,map.get("切球")));
        entries.add(new BarEntry(7,map.get("長球")));

        BarDataSet barDataSet = new BarDataSet(entries, "");

        if(isTopPlayer) barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.elegant_blue));
        else   barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.elegant_red));

        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new MyValueFormatter());

        barChart.setFitBars(false);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yLeftAxis = barChart.getAxisLeft();
        yLeftAxis.setAxisMinimum(0);
//        yLeftAxis.setAxisMaximum(50f); // 設為y中最大值+10
        yLeftAxis.setDrawAxisLine(false);
        yLeftAxis.setDrawLabels(false);
        yLeftAxis.setDrawGridLines(false);

        YAxis yRightAxis = barChart.getAxisRight();
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawGridLines(false);

        barChart.getLegend().setEnabled(false);
        barChart.setDescription(null);

        barChart.animateY(2000);
    }

   class MyValueFormatter extends ValueFormatter {
       private DecimalFormat mFormat;

       public MyValueFormatter() {
           mFormat = new DecimalFormat("###,###,##0"); // use one decimal
       }

       @Override
       public String getFormattedValue(float value) {
           // write your logic here
           return mFormat.format(value) + "";
       }
    }
}
