package com.itl_ernergy.android.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class BivariateGraph {

    protected Grid2D grid;
    protected DataSet data;
    protected Paint points;

    public BivariateGraph() {
        this.grid = new Grid2D();
        this.points = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.points.setColor(Color.RED);
        this.points.setStyle(Style.FILL);
    }

    public void setXAxisLabel(String lab) {
        this.grid.setXAxisLabel(lab);
    }

    public void setYAxisLabel(String lab) {
        this.grid.setYAxisLabel(lab);
    }

    public void setData(DataSet d) {
        this.data = d;
    }

    public void setPosition(float x, float y) {
        this.grid.setBounds(x, y, this.grid.getBounds().width(), this.grid.getBounds().height());
    }

    public void setDimension(float w, float h) {
        this.grid.setBounds(this.grid.getBounds().left, this.grid.getBounds().top, w, h);
    }

    protected void drawData(Canvas c) {
        //draw points
        int sz = this.data.getSize();
        RectF dtpt = new RectF(0, 0, 5, 5);
        float[] hsv = new float[3];

        hsv[0] = 0.0F;
        hsv[1] = 1.0F;
        hsv[2] = 1.0F;

        float xrng = (float) this.data.getMax(0) - (float) this.data.getMin(0);
        float yrng = (float) this.data.getMax(1) - (float) this.data.getMin(1);
        float zrng = 1.0F;

        if (this.data.getDimension() > 2) {
            zrng = (float) this.data.getMax(2) - (float) this.data.getMin(2);
        }

        for (int i = 0; i < sz; i++) {
            double[] pt = this.data.getDataAt(i);

            //change colour over length of sequence
            hsv[0] = 360.0F * (float) (sz - i) / (float) sz;

            this.points.setColor(Color.HSVToColor(180, hsv));

            float x = this.grid.getBounds().left + this.grid.getBounds().width() * ((float) pt[0] - (float) this.data.getMin(0)) / xrng;
            float y = this.grid.getBounds().bottom - this.grid.getBounds().height() * ((float) pt[1] - (float) this.data.getMin(1)) / yrng;

            //change size of point according to Z accn  
            float z = 5.0F;

            if (this.data.getDimension() > 2) {
                z = 20.0F * ((float) pt[1] - (float) this.data.getMin(2)) / zrng;
            }

            dtpt.left = x - z * 0.5F;
            dtpt.right = x + z * 0.5F;
            dtpt.top = y - z * 0.5F;
            dtpt.bottom = y + z * 0.5F;

            c.drawOval(dtpt, this.points);
        }
    }

    public void draw(Canvas c) {
        this.grid.setMaxX(this.data.getMax(0));
        this.grid.setMinX(this.data.getMin(0));
        this.grid.setMaxY(this.data.getMax(1));
        this.grid.setMinY(this.data.getMin(1));

        this.grid.drawGrid(c);

        this.drawData(c);
    }
}
