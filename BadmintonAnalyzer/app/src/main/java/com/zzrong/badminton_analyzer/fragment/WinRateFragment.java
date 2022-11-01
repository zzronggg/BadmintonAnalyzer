package com.zzrong.badminton_analyzer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.chart.HorizontalDoubleXLabelAxisRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static java.lang.Math.rint;

public class WinRateFragment extends Fragment {

    private HorizontalBarChart chart;
    private HashMap<String, Float> totalShots;
    private HashMap<String, Float> winShots;
    private Boolean isBluePlayer;

    public WinRateFragment() {
        // Required empty public constructor
    }

    public static WinRateFragment newInstance(HashMap<String, Float> totalShot, HashMap<String, Float> winShot) {
        WinRateFragment fragment = new WinRateFragment();
        Bundle args = new Bundle();
        args.putSerializable("total_shot", totalShot);
        args.putSerializable("win_shot", winShot);
        args.putBoolean("is_blue", true);
        fragment.setArguments(args);
        return fragment;
    }

    public static WinRateFragment newInstance(HashMap<String, Float> totalShot, HashMap<String, Float> winShot, boolean isBlue) {
        WinRateFragment fragment = new WinRateFragment();
        Bundle args = new Bundle();
        args.putSerializable("total_shot", totalShot);
        args.putSerializable("win_shot", winShot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalShots = (HashMap<String, Float>) getArguments().getSerializable("total_shot");
            winShots = (HashMap<String, Float>) getArguments().getSerializable("win_shot");
            isBluePlayer = getArguments().getBoolean("is_blue");
        }
    }

    public void onResume(){
        super.onResume();
        setChart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_win_rate, container, false);
        return v;
    }

    public void setChart(){
        chart = getView().findViewById(R.id.barChart_win_rate);
        chart.setScaleEnabled(false);
        setData(chart);
    }

    public void setData(BarChart barChart){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<float[]> vals;
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("","搶攻","拉吊","撲球","平球","小球","挑球","殺球","切球","長球"));

        //load data
        ArrayList<Float> blueTotalShots = new ArrayList<>(Arrays.asList(

            totalShots.get("搶攻"),
            totalShots.get("拉吊"),
            totalShots.get("撲球"),
            totalShots.get("平球"),
            totalShots.get("小球"),
            totalShots.get("挑球"),
            totalShots.get("殺球"),
            totalShots.get("切球"),
            totalShots.get("長球")
        ));

        ArrayList blueWinShots = new ArrayList<>(Arrays.asList(
            winShots.get("搶攻"),
            winShots.get("拉吊"),
            winShots.get("撲球"),
            winShots.get("平球"),
            winShots.get("小球"),
            winShots.get("挑球"),
            winShots.get("殺球"),
            winShots.get("切球"),
            winShots.get("長球")
        ));




        //process
        vals = processedData(blueTotalShots, blueWinShots);

        for(int i = 0 ; i < vals.size() ; i++){
            entries.add(new BarEntry(i+1, vals.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        setBarColor(barDataSet, isBluePlayer);
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextSize(14f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f);
        barChart.setFitBars(false);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setTextSize(14f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(9);

        YAxis yLeftAxis = barChart.getAxisLeft();
        yLeftAxis.setAxisMinimum(0);
        yLeftAxis.setAxisMaximum(Collections.max(blueTotalShots) + 20); // 設為y中最大值 + 10
        yLeftAxis.setDrawAxisLine(false);
        yLeftAxis.setDrawLabels(false);
        yLeftAxis.setDrawGridLines(false);

        YAxis yRightAxis = barChart.getAxisRight();
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawGridLines(false);

        chart.setXAxisRenderer(new HorizontalDoubleXLabelAxisRenderer(
                chart.getViewPortHandler(),
                chart.getXAxis(),
                chart.getTransformer(YAxis.AxisDependency.RIGHT),
                new IndexAxisValueFormatter(getWinRateLabels(vals)),
                new IndexAxisValueFormatter(labels),
                chart));

        barChart.getLegend().setEnabled(false);
        barChart.setDescription(null);

        barChart.animateY(2000);
    }

    public void setBarColor(BarDataSet barDataSet, boolean isBlue){
        if(isBlue)
            barDataSet.setColors(Color.parseColor("#43B5F5"), Color.parseColor("#8E8C8C"));
        else
            barDataSet.setColors(Color.parseColor("#FD736E"), Color.parseColor("#8E8C8C"));
    }

    public ArrayList<String> getWinRateLabels(ArrayList<float[]> input){  //input [win rate,lose rate]
        ArrayList<String> lst = new ArrayList<>();
        lst.add("");
        for(float[] pair : input){
            float winRate;
            float total = pair[0] + pair[1];

            if(total != 0) {
                winRate = (pair[0] / total) * 100;
            }
            else{
                winRate = 0;
            }

            String res = "";

            //desired output: Used rate (%) / Win rate (%)

            if(rint(total) == (double) total){
                res += String.format("%d ", (int)total);
            }
            else {
                res += String.format("%.1f ", total);
            }

            res += " / ";

            if(rint(winRate) == (double) winRate){
                res += String.format("%d ", (int)winRate) + " (%)";
            }
            else {
                res += String.format("%.1f ", winRate) + " (%)";
            }

            lst.add(res);
        }

        return lst;
    }

    /**
     *
     * @param totalRate
     * @param winRate
     * @return float[] output : win rate /lose rate
     */
    public ArrayList<float[]> processedData(ArrayList<Float> totalRate, ArrayList<Float> winRate){
        ArrayList<float[]> output = new ArrayList<>();
        for(int i = 0; i <totalRate.size(); i++){
            float ratio = winRate.get(i) / 100f;
            output.add(new float[]{ totalRate.get(i)*ratio, totalRate.get(i)*(1-ratio)});
        }
        return  output;
    }

}