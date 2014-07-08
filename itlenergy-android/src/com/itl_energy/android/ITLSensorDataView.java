package com.itl_energy.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.itl_energy.android.graphics.TimeSeriesDataSet;
import com.itl_energy.android.graphics.TimeSeriesGraph;

public class ITLSensorDataView extends View {

    private Paint erase;
    private Paint values;
    private Paint overlays;
    private TimeSeriesGraph viewer1;
    private TimeSeriesDataSet sensMeasure1;
    private int width;
    private int height;

    public ITLSensorDataView(Context context) {
        super(context);

        this.erase = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.values = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.overlays = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.overlays.setColor(Color.RED);
        this.overlays.setStyle(Style.STROKE);

        this.values.setColor(Color.WHITE);
        this.values.setStyle(Style.FILL);

        this.values.setTextAlign(Paint.Align.LEFT);

        this.erase.setColor(Color.BLACK);
        this.erase.setStyle(Style.FILL);

        this.sensMeasure1 = new TimeSeriesDataSet();
        this.sensMeasure1.setDimension(1);
        this.sensMeasure1.setCap(5000);

        this.viewer1 = new TimeSeriesGraph();
        this.viewer1.setData(this.sensMeasure1);

        this.viewer1.setXAxisLabel("Observation Date");
        this.viewer1.setYAxisLabel("Load (kWh)");

        this.viewer1.setTimestampResolutionInDays();

        this.viewer1.setDrawPoints(true);
    }

    public ITLSensorDataView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.erase = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.values = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.overlays = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.overlays.setColor(Color.RED);
        this.overlays.setStyle(Style.STROKE);

        this.values.setColor(Color.WHITE);
        this.values.setStyle(Style.FILL);

        this.values.setTextAlign(Paint.Align.LEFT);

        this.erase.setColor(Color.BLACK);
        this.erase.setStyle(Style.FILL);

        this.sensMeasure1 = new TimeSeriesDataSet();
        this.sensMeasure1.setDimension(1);
        this.sensMeasure1.setCap(5000);

        this.viewer1 = new TimeSeriesGraph();
        this.viewer1.setData(this.sensMeasure1);

        this.viewer1.setXAxisLabel("Observation Date");
        this.viewer1.setYAxisLabel("Load (kWh)");

        this.viewer1.setTimestampResolutionInDays();

        this.viewer1.setDrawPoints(true);
    }

    public void reset() {
        this.sensMeasure1.clear();
        this.sensMeasure1.reset();
    }

    public void addData(String timestamp, double[] dat) {
        this.sensMeasure1.addData(timestamp, dat);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRect((float) this.getLeft(), (float) this.getTop(), (float) this.getRight(), (float) this.getBottom(), this.erase);

        this.viewer1.setPosition(this.getLeft(), this.getTop());
        this.viewer1.setDimension((int) (this.width * 0.85), this.height);

        this.viewer1.draw(canvas);
    }

    public void setXAxisLabel(String label) {
        this.viewer1.setXAxisLabel(label);
    }

    public void setYAxisLabel(String label) {
        this.viewer1.setYAxisLabel(label);
    }
}
