package org.itl.display.graphics;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

public class Grid2D {

    protected RectF gridbounds;
    protected Paint labels;
    protected Paint axes;
    protected int gridxinc;
    protected int gridyinc;
    protected double maxx;
    protected double minx;
    protected double maxy;
    protected double miny;
    protected double maxy2;
    protected double miny2;
    protected String measurement;
    protected String units;
    protected int selection;
    protected boolean drawXaxis;
    protected boolean drawYaxis;
    protected boolean drawY2axis;
    protected boolean drawXgridlines;
    protected boolean drawYgridlines;
    protected int xInset;
    protected int yInset;
    protected boolean yCategorical;
    protected boolean secondYAxis;
    protected String[] yCategories;
    protected String xlab;
    protected String ylab;
    protected DateManipulator form;
    private boolean datesInTimeSeries;

    public Grid2D() {
        this.axes = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.labels = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.axes.setColor(Color.WHITE);
        this.labels.setColor(Color.WHITE);

        this.axes.setStrokeWidth((float) 1.0);
        this.axes.setStyle(Style.STROKE);
        this.axes.setTextAlign(Paint.Align.CENTER);

        this.labels.setStyle(Style.FILL);
        this.labels.setTextAlign(Paint.Align.CENTER);

        this.gridbounds = new RectF();

        this.gridxinc = 8;
        this.gridyinc = 8;

        this.maxx = 1.0;
        this.minx = 0.0;
        this.maxy = 1.0;
        this.miny = 0.0;
        this.maxy2 = 1.0;
        this.miny2 = 0.0;

        this.selection = -1;

        this.form = new DateManipulator();
        this.datesInTimeSeries = false;
        this.form.setOutputDateFormat("yyyy-MM-dd HH:mm");
        this.units = "kWh";

        this.drawXaxis = true;
        this.drawYaxis = true;
        this.drawXgridlines = true;
        this.drawYgridlines = true;
        this.xInset = 25;
        this.yInset = 25;
        this.yCategorical = false;
        this.yCategories = null;

        this.secondYAxis = false;

        this.xlab = "X Category";
        this.ylab = "Y Category";
    }

    public void setXAxisAsDatetime(boolean ax) {
        this.datesInTimeSeries = ax;
    }

    public void setXAxisTimestampFormat(String frm) {
        this.form.setOutputDateFormat(frm);
    }

    public void setXAxisLabel(String lab) {
        this.xlab = lab;
    }

    public void setYAxisLabel(String lab) {
        this.ylab = lab;
    }

    public double getMaxX() {
        return this.maxx;
    }

    public void setMaxX(double maxx) {
        this.maxx = maxx;
    }

    public double getMinX() {
        return this.minx;
    }

    public void setMinX(double minx) {
        this.minx = minx;
    }

    public double getMaxY() {
        return this.maxy;
    }

    public void setMaxY(double maxy) {
        this.maxy = maxy;
    }

    public double getMinY() {
        return this.miny;
    }

    public void setMinY(double miny) {
        this.miny = miny;
    }

    public double getMaxY2() {
        return this.maxy2;
    }

    public void setMaxY2(double maxy) {
        this.maxy2 = maxy;
    }

    public double getMinY2() {
        return this.miny2;
    }

    public void setMinY2(double miny) {
        this.miny2 = miny;
    }

    public RectF getBounds() {
        return this.gridbounds;
    }

    public void setBounds(float x, float y, float w, float h) {
        this.gridbounds.set(2 * this.xInset + x, y + this.yInset, 2 * this.xInset + x + w / 1.1F - this.xInset, y + this.yInset + h / 1.1F - 2 * this.yInset);
    }

    public void setDrawXAxis(boolean drawXaxis) {
        this.drawXaxis = drawXaxis;
    }

    public void setDrawYAxis(boolean drawYaxis) {
        this.drawYaxis = drawYaxis;
    }

    public void setDrawY2Axis(boolean drawYaxis) {
        this.secondYAxis = drawYaxis;
    }

    public void setDrawXGridlines(boolean drawXgridlines) {
        this.drawXgridlines = drawXgridlines;
    }

    public void setDrawYGridlines(boolean drawYgridlines) {
        this.drawYgridlines = drawYgridlines;
    }

    public void setYCategories(String[] yCategories) {
        this.yCategorical = true;
        this.yCategories = yCategories;
    }

    public void setxInset(int xInset) {
        this.xInset = xInset;
    }

    public void resetControl() {
        this.gridxinc = 8;
        this.gridyinc = 8;
    }

    public void processXAxis(double maxval, double minval) {
        double[] kBreakValues = {7.071068, 3.162278, 1.414214, 0.0};
        double[] kBreakIntervals = {10.0, 5.0, 2.0, 1.0};
        int desired = this.gridxinc;

        double interval = (maxval - minval) / desired;
        double magnitude = Math.floor(Math.log10(interval) + 0.5);

        if (interval < 1.0) {
            magnitude -= 1.0;
        }

        magnitude = Math.pow(10.0, magnitude);

        double tmpVal = interval / magnitude;

        for (int i = 0; i < 4; ++i) {
            if (tmpVal >= kBreakValues[i]) {
                interval = kBreakIntervals[i] * magnitude;

                break;
            }
        }

        minval = Math.floor(minval / interval) * interval;
        maxval = Math.ceil(maxval / interval) * interval;

        this.maxx = maxval;
        this.minx = minval;

        this.gridxinc = (int) ((maxval - minval) / interval);
    }

    public void processYAxis(double maxval, double minval) {
        double[] kBreakValues = {7.071068, 3.162278, 1.414214, 0.0};
        double[] kBreakIntervals = {10.0, 5.0, 2.0, 1.0};
        int desired = this.gridyinc;

        double interval = (maxval - minval) / desired;
        double magnitude = Math.floor(Math.log10(interval) + 0.5);

        if (interval < 1.0) {
            magnitude -= 1.0;
        }

        magnitude = Math.pow(10.0, magnitude);

        double tmpVal = interval / magnitude;

        for (int i = 0; i < 4; ++i) {
            if (tmpVal >= kBreakValues[i]) {
                interval = kBreakIntervals[i] * magnitude;

                break;
            }
        }

        minval = Math.floor(minval / interval) * interval;
        maxval = Math.ceil(maxval / interval) * interval;

        this.maxy = maxval;
        this.miny = minval;

        this.gridyinc = (int) ((maxval - minval) / interval);
    }

    public void processY2Axis(double maxval, double minval) {
        double[] kBreakValues = {7.071068, 3.162278, 1.414214, 0.0};
        double[] kBreakIntervals = {10.0, 5.0, 2.0, 1.0};
        int desired = this.gridyinc;

        double interval = (maxval - minval) / desired;
        double magnitude = Math.floor(Math.log10(interval) + 0.5);

        if (interval < 1.0) {
            magnitude -= 1.0;
        }

        magnitude = Math.pow(10.0, magnitude);

        double tmpVal = interval / magnitude;

        for (int i = 0; i < 4; ++i) {
            if (tmpVal >= kBreakValues[i]) {
                interval = kBreakIntervals[i] * magnitude;

                break;
            }
        }

        minval = Math.floor(minval / interval) * interval;
        maxval = Math.ceil(maxval / interval) * interval;

        this.maxy2 = maxval;
        this.miny2 = minval;

        //this.gridyinc=(int)((maxval-minval)/interval);
    }

    protected void drawGrid(Canvas c) {
        DecimalFormat numf = new DecimalFormat();

        c.save();

        DashPathEffect dashPath = new DashPathEffect(new float[]{10, 10}, 0);

        this.labels.setPathEffect(dashPath);
        this.labels.setStrokeWidth(1);

        c.translate((float) (this.gridbounds.left - this.xInset), this.gridbounds.centerY());
        c.rotate((float) -90);

        if (this.ylab != null) {
            Rect bnd = new Rect();

            this.axes.getTextBounds(this.ylab, 0, this.ylab.length(), bnd);

            c.drawText(this.ylab, (int) ((-0.35 * (bnd.width() - this.gridbounds.height()))), (int) (bnd.height() - 1.85 * this.xInset), this.axes);
        }

        c.restore();
        c.restore();

        c.drawRect(this.gridbounds, this.axes);

        if (this.xlab != null) {
            Rect bnd = new Rect();

            this.axes.getTextBounds(this.xlab, 0, this.xlab.length(), bnd);

            c.drawText(this.xlab, (int) ((int) this.gridbounds.left + 0.5 * (this.gridbounds.width() - bnd.width())), (int) (this.gridbounds.bottom + 2.85 * bnd.height()), this.axes);
        }

        for (int i = 0; i <= this.gridxinc; i++) {
            float x = this.gridbounds.left + i * this.gridbounds.width() / this.gridxinc;

            //if(i!=this.gridxinc)
            //	l2.setLine(x,(int)this.gridbounds.bottom,x,(int)this.gridbounds.top);
            if (this.drawXgridlines) {
                if (i != 0 && i != this.gridxinc) {
                    c.drawLine(x, (int) this.gridbounds.bottom, x, (int) this.gridbounds.top, this.labels);
                }
            }

            if (this.drawXaxis) {
                String lab;
                Rect bnd = new Rect();

                if (this.datesInTimeSeries) {
                    lab = this.form.utcToTime((new Double(this.minx + i * (this.maxx - this.minx) / this.gridxinc)).longValue());
                }
                else {
                    lab = numf.format(this.minx + i * (this.maxx - this.minx) / this.gridxinc);
                }

                this.axes.getTextBounds(lab, 0, lab.length(), bnd);

                if (lab.contains(" ")) {
                    int sppos = lab.indexOf(" ");
                    String tlab = lab.substring(0, sppos - 1);
                    String llab = lab.substring(sppos);

                    this.axes.getTextBounds(tlab, 0, tlab.length(), bnd);
                    c.drawText(tlab, (float) (x/*-0.5*bnd.width()*/), (float) (this.gridbounds.bottom + bnd.height() * 1.5), this.axes);

                    this.axes.getTextBounds(llab, 0, llab.length(), bnd);
                    c.drawText(llab, (float) (x/*-0.5*bnd.width()*/), (float) (this.gridbounds.bottom + 2.0 * bnd.height() * 1.5), this.axes);
                }
                else {
                    c.drawText(lab, (float) (x/*-0.5*bnd.width()*/), (float) (this.gridbounds.bottom + bnd.height() * 1.5), this.axes);
                }
            }
        }

        if (!this.yCategorical) {
            for (int i = 0; i <= this.gridyinc; i++) {
                float y = this.gridbounds.bottom + ((float) (i - this.gridyinc)) * this.gridbounds.height() / (float) this.gridyinc;
                String lab = numf.format(this.maxy - i * (this.maxy - this.miny) / this.gridyinc);
                Rect bnd = new Rect();

                this.axes.getTextBounds(lab, 0, lab.length(), bnd);

                //if(i!=this.gridyinc)
                //	l2.setLine(this.gridbounds.left,y,this.gridbounds.right,y);
                if (this.drawYgridlines) {
                    if (i != 0 && i != this.gridyinc) {
                        c.drawLine(this.gridbounds.left, y, this.gridbounds.right, y, this.labels);
                    }
                }
                if (this.drawYaxis) {
                    c.drawText(lab, (int) (this.gridbounds.left - bnd.right), (int) (y + 0.5 * bnd.height()), this.axes);
                }
                if (this.secondYAxis) {
                    String lab2 = numf.format(this.maxy2 - i * (this.maxy2 - this.miny2) / this.gridyinc);

                    this.axes.getTextBounds(lab2, 0, lab2.length(), bnd);

                    c.drawText(lab2, (int) (this.gridbounds.right + 15.0), (int) (y + 0.5 * bnd.height()), this.axes);
                }
            }
        }
        else {
            if (this.yCategories != null) {

            }
        }
    }
}
