package org.itl.display.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class TimeSeriesGraph extends BivariateGraph {

    protected boolean drawLines;
    protected boolean drawPoints;
    protected boolean drawImpulses;

    public TimeSeriesGraph() {
        super();

        this.grid.setXAxisAsDatetime(true);
        //this.grid.setDrawY2Axis(true);

        this.drawLines = true;
        this.drawPoints = false;
        this.drawImpulses = false;
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
        this.drawImpulses = !this.drawLines;
    }

    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;
        this.drawImpulses = !this.drawPoints;
    }

    public void setDrawImpulses(boolean drawImpulses) {
        this.drawImpulses = drawImpulses;
        this.drawPoints = !this.drawImpulses;
        this.drawLines = !this.drawImpulses;
    }

    public void setTimestampResolutionInSeconds() {
        this.grid.setXAxisTimestampFormat("mm:SS");
    }

    public void setTimestampResolutionInMinutes() {
        this.grid.setXAxisTimestampFormat("HH:mm");
    }

    public void setTimestampResolutionInHours() {
        this.grid.setXAxisTimestampFormat("DD HH:00");
    }

    public void setTimestampResolutionInDays() {
        this.grid.setXAxisTimestampFormat("MM/DD HH:00");
    }

    public void draw(Canvas c) {
        if (this.data.getSize() > 0) {
            this.grid.setMaxX(this.data.getTimeAt(this.data.getSize() - 1));
            this.grid.setMinX(this.data.getTimeAt(0));
            //this.grid.setMaxY(this.data.getMax(0));
            //this.grid.setMinY(this.data.getMin(0));
            //this.grid.setMaxY2(this.data.getMax(1));
            //this.grid.setMinY2(this.data.getMin(1));

            this.grid.setMaxY(this.data.getMax(0));
            this.grid.setMinY(this.data.getMin(0));

            //this.data.setMax(0,1000.0D);
            //this.data.setMin(0,0.0D);
            if (this.data.getDimension() > 1) {
                this.grid.setMaxY2(this.data.getMax(1));
                this.grid.setMinY2(this.data.getMin(1));

                //this.data.setMax(1,80.0D);
                //this.data.setMin(1,0.0D);
            }
        }

        this.grid.drawGrid(c);
        this.drawData(c);
    }

    protected void drawData(Canvas c) {
        //draw points
        int sz = this.data.getSize();
        RectF dtpt = new RectF(0, 0, 5, 5);
        float[] hsv = new float[3];

        hsv[0] = 0.0F;
        hsv[1] = 1.0F;
        hsv[2] = 1.0F;

        if (sz == 0) {
            return;
        }

        float xrng = (float) this.data.times.get(this.data.times.size() - 1).floatValue() - (float) this.data.times.firstElement().floatValue();
        //float yrng=(float)this.data.getMax(0)-(float)this.data.getMin(0);
        //float y2rng=(float)this.data.getMax(1)-(float)this.data.getMin(1);
        float zrng = 1.0F;

        if (this.data.getDimension() > 2) {
            zrng = (float) this.data.getMax(2) - (float) this.data.getMin(2);
        }

        int dim = this.data.getDimension();

        c.clipRect(this.grid.gridbounds);

        for (int j = 0; j < this.data.getDimension(); j++) {
            float xm1 = this.grid.getBounds().left;
            float ym1 = 0.0F;
            float yrng = (float) this.data.getMax(j) - (float) this.data.getMin(j);

            hsv[0] = 360.0F * (float) (dim - j) / (float) dim;

            this.points.setColor(Color.HSVToColor(180, hsv));

            if (j == 1) {
                this.points.setColor(Color.RED);
                this.points.setStrokeWidth(3.0F);
            }
            else {
                this.points.setColor(Color.YELLOW);
                this.points.setStrokeWidth(1.5F);
            }

            for (int i = 0; i < sz; i++) {
                double[] pt = this.data.getDataAt(i);

                //change colour over length of sequence
                float x = this.grid.getBounds().left + this.grid.getBounds().width() * ((float) this.data.times.get(i).floatValue() - (float) this.data.times.firstElement().floatValue()) / xrng;
                float y = this.grid.getBounds().bottom - this.grid.getBounds().height() * ((float) pt[j] - (float) this.data.getMin(j)) / yrng;

                //change size of point according to Z accn  
                float z = 5.0F;

                if (this.data.getDimension() > 2) {
                    z = 20.0F * ((float) pt[1] - (float) this.data.getMin(2)) / zrng;
                }

                dtpt.left = x - z * 0.5F;
                dtpt.right = x + z * 0.5F;
                dtpt.top = y - z * 0.5F;
                dtpt.bottom = y + z * 0.5F;

                if (this.drawLines && !this.drawImpulses) {
                    if (i > 0) {
                        c.drawLine(xm1, ym1, x, y, this.points);
                    }
                }
                if (this.drawImpulses && !this.drawLines) {
                    c.drawLine(x, this.grid.getBounds().bottom, x, y, this.points);
                }
                if (this.drawPoints) {
                    c.drawOval(dtpt, this.points);
                }

                xm1 = x;
                ym1 = y;
            }
        }

        c.restore();
    }
}
