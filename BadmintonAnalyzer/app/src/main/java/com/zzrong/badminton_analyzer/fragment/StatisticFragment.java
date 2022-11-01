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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zzrong.badminton_analyzer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class StatisticFragment extends Fragment {

    HorizontalBarChart upChart;
    HorizontalBarChart downChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);
        return setChart(v);
    }

    public View setChart(View view){
        upChart = view.findViewById(R.id.barChart_up);
        upChart.setScaleEnabled(false);
        setData(upChart,0);

        downChart = view.findViewById(R.id.barChart_down);
        downChart.setScaleEnabled(false);
        setData(downChart,1);
        return view;
    }

    public void setData(BarChart barChart,int player){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("","高遠球","切球","殺球","平球","撲球","挑球","短球"));

        //fake data
        entries.add(new BarEntry(1,3));
        entries.add(new BarEntry(2,7));
        entries.add(new BarEntry(3,5));
        entries.add(new BarEntry(4,8));
        entries.add(new BarEntry(5,2));
        entries.add(new BarEntry(6,10));
        entries.add(new BarEntry(7,6));

        BarDataSet barDataSet = new BarDataSet(entries, "");

        if(player == 0) barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.elegant_blue));
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
