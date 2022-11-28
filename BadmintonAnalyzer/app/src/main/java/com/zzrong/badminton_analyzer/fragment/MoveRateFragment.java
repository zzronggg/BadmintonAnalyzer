package com.zzrong.badminton_analyzer.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.chart.BarChartCustomRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MoveRateFragment extends Fragment {

    private HorizontalBarChart chart;

    private Boolean isBluePlayer = true;
    HashMap<String,Float> moveType = new HashMap<>();

    public MoveRateFragment() {
        // Required empty public constructor
    }


    //fake input: 17筆float
    public static MoveRateFragment newInstance(HashMap<String,Float> input) {
        MoveRateFragment fragment = new MoveRateFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", input);
        args.putBoolean("is_blue", true);
        fragment.setArguments(args);
        return fragment;
    }

    public static MoveRateFragment newInstance(HashMap<String,Float> input, Boolean isBlue) {
        MoveRateFragment fragment = new MoveRateFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", input);
        args.putBoolean("is_blue", isBlue);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            moveType = (HashMap<String, Float>) getArguments().getSerializable("data");
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
        return inflater.inflate(R.layout.fragment_win_rate, container, false);
    }

    public void setChart(){
        chart = getView().findViewById(R.id.barChart_win_rate);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        setData(chart);
        chart.setVisibleXRange(8,8);
//        float l = chart.getLowestVisibleX();
//        float h = chart.getHighestVisibleX();
        chart.animateY(2000);

        ArrayList<Bitmap> imgLst = new ArrayList<>();
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_down_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_down_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_down_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_down_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_up_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_up_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_up_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_up_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_right_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_left_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_down_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_down_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_up_arrow_2));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_up_arrow));
        imgLst.add(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_prohibition));

        chart.setRenderer(new BarChartCustomRenderer(chart, chart.getAnimator(),
                chart.getViewPortHandler(), imgLst, getContext()));

        chart.moveViewToAnimated(8,16, YAxis.AxisDependency.LEFT,1500);
    }

    public void setData(BarChart barChart){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Float> vals = new ArrayList<>();

        int total = moveType.values().stream().mapToInt(Float::intValue).sum();

        vals.add(moveType.get("DLBR"));
        vals.add(moveType.get("DSBR"));
        vals.add(moveType.get("DLBL"));
        vals.add(moveType.get("DSBL"));
        vals.add(moveType.get("DLFR"));
        vals.add(moveType.get("DSFR"));
        vals.add(moveType.get("DLFL"));
        vals.add(moveType.get("DSFL"));
        vals.add(moveType.get("TLR"));
        vals.add(moveType.get("TSR"));
        vals.add(moveType.get("TLL"));
        vals.add(moveType.get("TSL"));
        vals.add(moveType.get("LLB"));
        vals.add(moveType.get("LSB"));
        vals.add(moveType.get("LLF"));
        vals.add(moveType.get("LSF"));
        vals.add(moveType.get("NM"));

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        for(int i = 0; i < vals.size(); i ++){
            entries.add(new BarEntry(i+1, Float.valueOf(decimalFormat.format(vals.get(i)/total * 100))));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        setBarColor(barDataSet, isBluePlayer);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f);

        barChart.setFitBars(true);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(17);
        xAxis.setDrawLabels(false);
        xAxis.setXOffset(10f);

        YAxis yLeftAxis = barChart.getAxisLeft();
        yLeftAxis.setAxisMinimum(0);
        yLeftAxis.setAxisMaximum((Collections.max(vals)/total*100) + 10); // 設為y中最大值+10
        yLeftAxis.setDrawAxisLine(false);
        yLeftAxis.setDrawLabels(false);
        yLeftAxis.setDrawGridLines(false);

        YAxis yRightAxis = barChart.getAxisRight();
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawGridLines(false);

        barChart.getLegend().setEnabled(false);
        barChart.getLegend().setFormSize(15);
        barChart.getLegend().setTextSize(15);
        barChart.setDescription(null);

        barChart.animateY(2000);
    }

    public void setBarColor(BarDataSet barDataSet, boolean isBlue){
        if(isBlue)
            barDataSet.setColors(Color.parseColor("#43B5F5"));
        else
            barDataSet.setColors(Color.parseColor("#FD736E"));
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}