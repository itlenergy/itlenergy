package com.itl_energy.webclient.profile.visualisation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import com.itl_energy.webclient.profile.LoadTimeSeries;

public class ContinuousTimeSeriesControl extends TimeSeriesControl {

    private static final long serialVersionUID = 1L;
    protected boolean drawLines;
    protected boolean drawPoints;
    protected boolean drawImpulses;

    public ContinuousTimeSeriesControl() {
        this.drawLines = true;
        this.drawPoints = false;
        this.drawImpulses = false;
    }

    public void setSeriesData(LoadTimeSeries lts) {
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        this.data = lts;

        for (int i = 0; i < lts.getLength(); i++) {
            if (lts.getLoadAtSeriesIndex(i) > max) {
                max = lts.getLoadAtSeriesIndex(i);
            }
            if (lts.getLoadAtSeriesIndex(i) < min) {
                min = lts.getLoadAtSeriesIndex(i);
            }
        }

        this.grid.processXAxis(this.millisecondsFromTimestamp(lts.getEndTime()), this.millisecondsFromTimestamp(lts.getBeginTime()));
        this.grid.processYAxis(max, min);
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

    @Override
    protected void drawData(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        double xinc = this.grid.getBounds().getWidth() / (this.grid.getMaxX() - this.grid.getMinX());
        double yinc = this.grid.getBounds().getHeight() / (this.grid.getMaxY() - this.grid.getMinY());

        Line2D l2 = new Line2D.Double();
        Ellipse2D ep2 = new Ellipse2D.Double();
        double x, y;
        Stroke s = g.getStroke();

        g.setStroke(bs);
        g.setColor(Color.red);

        x = this.grid.getBounds().getMinX() + ((this.millisecondsFromTimestamp(this.data.getBeginTime()) - this.grid.getMinX()) * xinc);
        y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - (((((LoadTimeSeries) this.data).getBeginLoad() - this.grid.getMinY()) * yinc));

        l2.setLine(0, 0, x, y);

        ep2.setFrame(x - 2.5, y - 2.5, 5.0, 5.0);

        g.fill(ep2);

        for (int j = 0; j < this.data.getLength(); j++) {
			//scale points then draw...

            x = this.grid.getBounds().getMinX() + ((this.millisecondsFromTimestamp(this.data.getTimeAtSeriesIndex(j)) - this.grid.getMinX()) * xinc);
            y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - ((((LoadTimeSeries) this.data).getLoadAtSeriesIndex(j) - this.grid.getMinY()) * yinc);

            if (this.drawLines) {
                l2.setLine(l2.getX2(), l2.getY2(), x, y);
                g.draw(l2);
            }
            if (this.drawPoints) {
                ep2.setFrame(x - 5.0, y - 5.0, 10.0, 10.0);
                g.fill(ep2);
            }
            if (this.drawImpulses) {
                l2.setLine(x, this.grid.getBounds().getMinY(), x, y);
                g.fill(l2);
            }
        }

        g.setStroke(s);
    }
}
