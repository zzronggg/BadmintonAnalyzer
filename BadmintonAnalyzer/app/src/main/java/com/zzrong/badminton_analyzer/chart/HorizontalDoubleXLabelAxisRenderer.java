package com.zzrong.badminton_analyzer.chart;
import android.graphics.Canvas;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * XAxis renderer for both-sided horizontal bar charts.<br> Use as follows:<br>
 * <pre>chart.setXAxisRenderer(new HorizontalDoubleXLabelAxisRenderer(chart.getViewPortHandler(),
 *                                                                    chart.getXAxis(),
 *                                                                    chart.getTransformer(YAxis.AxisDependency.RIGHT),
 *                                                                    new IndexAxisValueFormatter(categoriesLeft),
 *                                                                    new IndexAxisValueFormatter(categoriesRight),
 *                                                                    chart));</pre>
 */
public class HorizontalDoubleXLabelAxisRenderer extends XAxisRendererHorizontalBarChart {

    private ValueFormatter leftValueVFormatter;
    private final ValueFormatter rightValueFormatter;

    public HorizontalDoubleXLabelAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer transformer, ValueFormatter leftValueVFormatter, ValueFormatter rightValueFormatter, BarChart barChart) {
        super(viewPortHandler, xAxis, transformer, barChart);
        this.leftValueVFormatter = leftValueVFormatter;
        this.rightValueFormatter = rightValueFormatter;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float xoffset = mXAxis.getXOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0,0);

        if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
            pointF.x = 0.0f;
            pointF.y = 0.5f;
            drawLabels(c, mViewPortHandler.contentRight() + xoffset, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
            pointF.x = 1.0f;
            pointF.y = 0.5f;
            drawLabels(c, mViewPortHandler.contentRight() - xoffset, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            pointF.x = 1.0f;
            pointF.y = 0.5f;
            drawLabels(c, mViewPortHandler.contentLeft() - xoffset, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 1.0f;
            pointF.y = 0.5f;
            drawLabels(c, mViewPortHandler.contentLeft() + xoffset, pointF);

        } else { // BOTH SIDED
            pointF.x = 1.0f;  //已修改
            pointF.y = 0.5f;
            drawBothSidedLabels(c, mViewPortHandler.contentRight() + xoffset, pointF, true);
            pointF.x = 1.0f;
            pointF.y = 0.5f;
            drawBothSidedLabels(c, mViewPortHandler.contentLeft() - xoffset, pointF, false);
        }

        MPPointF.recycleInstance(pointF);
    }

    private void drawBothSidedLabels(Canvas c, float pos, MPPointF anchor, boolean left) {

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {

            // only fill x values
            if (centeringEnabled) {
                positions[i + 1] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i + 1] = mXAxis.mEntries[i / 2];
            }
        }

        mTrans.pointValuesToPixel(positions);

        for (int i = 0; i < positions.length; i += 2) {

            float y = positions[i + 1];

            if (mViewPortHandler.isInBoundsY(y)) {

                if (left) {
                    String label = leftValueVFormatter.getAxisLabel(mXAxis.mEntries[i / 2], mXAxis);
                    drawLabel(c, label, pos, y, anchor, labelRotationAngleDegrees);
                }
                else {
                    String label = rightValueFormatter.getAxisLabel(mXAxis.mEntries[i / 2], mXAxis);
                    drawLabel(c, label, pos, y, anchor, labelRotationAngleDegrees);
                }
            }
        }

    }
}