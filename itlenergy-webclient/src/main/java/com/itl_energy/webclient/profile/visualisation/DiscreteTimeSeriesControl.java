package com.itl_energy.webclient.profile.visualisation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import com.itl_energy.webclient.profile.strat.StratumTimeSeries;

public class DiscreteTimeSeriesControl extends TimeSeriesControl {

    private static final long serialVersionUID = 1L;

    public DiscreteTimeSeriesControl() {
        super();

        this.grid.setDrawYGridlines(false);
    }

    public void setSeriesData(StratumTimeSeries sts) {
        this.data = sts;

        this.grid.setYCategories(null);
        this.grid.processXAxis(this.millisecondsFromTimestamp(sts.getEndTime()), this.millisecondsFromTimestamp(sts.getBeginTime()));
    }

    @Override
    protected void drawData(Graphics2D g) {
        StratumTimeSeries dat = ((StratumTimeSeries) this.data);
        String[] srs = dat.getStrataInSeries();
        double xinc = this.grid.getBounds().getWidth() / (this.grid.getMaxX() - this.grid.getMinX());
        double yinc = this.grid.getBounds().getHeight() / srs.length;

        double x, y;

        g.setColor(Color.red);

        x = this.grid.getBounds().getMinX() + ((this.millisecondsFromTimestamp(this.data.getBeginTime()) - this.grid.getMinX()) * xinc);
        y = this.grid.getBounds().getMinY() + dat.getBeginStrata() * yinc;

        int lastsymbol = dat.getBeginStrata();
        Rectangle2D len = new Rectangle2D.Double();
        Color fillc = Color.getHSBColor((float) lastsymbol / (float) srs.length, 1.0F, 1.0F);

        len.setRect(x, y, 0.0, yinc);

        for (int j = 1; j < dat.getLength(); j++) {
            int sym = dat.getStrataAtSeriesIndex(j);

            x = this.grid.getBounds().getMinX() + ((this.millisecondsFromTimestamp(this.data.getTimeAtSeriesIndex(j)) - this.grid.getMinX()) * xinc);
            y = this.grid.getBounds().getMinY() + sym * yinc;

            if (sym != lastsymbol) {
                fillc = Color.getHSBColor((float) lastsymbol / (float) srs.length, 1.0F, 1.0F);

                g.setColor(fillc);
                g.fill(len);
                g.setColor(Color.black);
                g.draw(len);

                len.setRect(x, y, 0.0, yinc);
            }
            else {
                len.setRect(len.getMinX(), len.getMinY(), x - len.getMinX(), yinc);
            }

            lastsymbol = sym;
        }

        g.setColor(Color.red);
        g.fill(len);
        g.setColor(Color.black);
        g.draw(len);
    }
}
