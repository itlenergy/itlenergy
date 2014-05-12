package com.itl_energy.webclient.profile.visualisation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Class for representing simple functional relationship.
 *
 * @author bstephen
 * @version 10th May 2011
 */
public class LineGraphControl extends BivariateScatterControl {

    private static final long serialVersionUID = 6325286058338449731L;
    protected boolean haslines;
    protected boolean haspoints;
    protected boolean hasimpulses;

    public LineGraphControl() {
        super();

        this.haslines = true;
        this.haspoints = true;
        this.hasimpulses = false;

        this.resetControl();
    }

    public void setAsLines(boolean set) {
        this.haslines = set;
        this.hasimpulses = false;
    }

    public void setHasPoints(boolean set) {
        this.haspoints = set;
    }

    public void setAsImpulses(boolean set) {
        this.hasimpulses = set;
        this.haslines = !set;
    }

    protected void drawData(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        double xinc = this.grid.getBounds().getWidth() / (this.grid.getMaxX() - this.grid.getMinX());
        double yinc = this.grid.getBounds().getHeight() / (this.grid.getMaxY() - this.grid.getMinY());

        Line2D l2 = new Line2D.Double();
        Ellipse2D ep2 = new Ellipse2D.Double();
        double x, y;
        Stroke s = g.getStroke();

        g.setStroke(bs);

        for (int i = 0; i < this.series.size(); i++) {
            Color srsClr;

            //need to set the colour according to class...
            if (!this.labels.isEmpty()) {
                float frac =/*360.0F**/ ((float) this.labels.get(i).intValue() / (float) this.ulabels.size());
                srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
            }
            else {
                srsClr = Color.red;
            }

            g.setColor(srsClr);

            x = this.grid.getBounds().getMinX() + ((this.series.get(i)[0][0] - this.grid.getMinX()) * xinc);
            y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - (((this.series.get(i)[0][1] - this.grid.getMinY()) * yinc));

            l2.setLine(0, 0, x, y);

            if (this.haspoints) {
                ep2.setFrame(x - 2.0, y - 2.0, 4.0, 4.0);
                g.fill(ep2);

                if (this.hasimpulses) {
                    l2.setLine(x, this.grid.getBounds().getMaxY(), x, y);
                    g.draw(l2);
                }
            }

            for (int j = 1; j < this.series.get(i).length; j++) {
                //scale points then draw...
                x = this.grid.getBounds().getMinX() + ((this.series.get(i)[j][0] - this.grid.getMinX()) * xinc);
                y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - ((this.series.get(i)[j][1] - this.grid.getMinY()) * yinc);

                if (this.hasimpulses) {
                    l2.setLine(x, this.grid.getBounds().getMaxY(), x, y);
                    g.draw(l2);
                }
                if (this.haspoints) {
                    ep2.setFrame(x - 2.0, y - 2.0, 4.0, 4.0);
                    g.fill(ep2);
                }
                if (this.haslines) {
                    l2.setLine(l2.getX2(), l2.getY2(), x, y);
                    g.draw(l2);
                }
            }
        }

        g.setStroke(s);
    }
}
