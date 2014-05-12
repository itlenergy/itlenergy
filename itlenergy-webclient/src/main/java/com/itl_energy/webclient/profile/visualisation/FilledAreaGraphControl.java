package com.itl_energy.webclient.profile.visualisation;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * Class for displaying curves with filled in areas.
 *
 * @author bstephen
 * @version 23rd May 2011
 */
public class FilledAreaGraphControl extends LineGraphControl {

    private static final long serialVersionUID = 2565609457098497204L;

    public FilledAreaGraphControl() {
        super();
    }

    protected void drawData(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        AlphaComposite ac1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

        double xinc = this.grid.getBounds().getWidth() / (this.grid.getMaxX() - this.grid.getMinX());
        double yinc = this.grid.getBounds().getHeight() / (this.grid.getMaxY() - this.grid.getMinY());

        Line2D l2 = new Line2D.Double();
        double x, y;
        Stroke s = g.getStroke();

        g.setStroke(bs);

        for (int i = 0; i < this.series.size(); i++) {
            Color srsClr;
            GeneralPath pth = new GeneralPath();

            //need to set the colour according to class...
            if (!this.labels.isEmpty()) {
                float frac =/*360.0F**/ ((float) this.labels.get(i).intValue() / (float) this.ulabels.size());
                srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
            }
            else {
                srsClr = Color.RED;
            }

            g.setColor(Color.BLACK);

            x = this.grid.getBounds().getMinX() + ((this.series.get(i)[0][0] - this.grid.getMinX()) * xinc);
            y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - (((this.series.get(i)[0][1] - this.grid.getMinY()) * yinc));

            l2.setLine(0, 0, x, y);

            pth.moveTo(x, this.grid.getBounds().getMaxY());
            pth.lineTo(x, y);

            g.setComposite(ac1);

            for (int j = 1; j < this.series.get(i).length; j++) {
                //scale points then draw...
                x = this.grid.getBounds().getMinX() + ((this.series.get(i)[j][0] - this.grid.getMinX()) * xinc);
                y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - ((this.series.get(i)[j][1] - this.grid.getMinY()) * yinc);

                l2.setLine(l2.getX2(), l2.getY2(), x, y);
                pth.lineTo(x, y);

                g.draw(l2);
            }

            pth.lineTo(l2.getX2(), this.grid.getBounds().getMaxY());
            pth.closePath();

            g.setComposite(ac2);

            g.setColor(srsClr);
            g.fill(pth);

            g.setComposite(ac1);
        }

        g.setStroke(s);
    }
}
