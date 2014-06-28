package com.itl_energy.webclient.profile.visualisation;

import com.itl_energy.webclient.itl.util.ITLClientUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;

/**
 * Class for representing 2 dimensional data in a basic scatter plot with option
 * class colouring and bubble plot type sizing.
 *
 * @author bstephen
 * @version 28th April 2011
 */
public class BivariateScatterControl extends JComponent {

    private static final long serialVersionUID = -5197338783142654842L;
    protected Vector<double[][]> series;
    protected Vector<Integer> labels;
    protected Set<Integer> ulabels;
    protected Vector<Double> bubblesize;
    protected Basic2DGrid grid;
    protected int border;

    protected double maxx;
    protected double maxy;
    protected double minx;
    protected double miny;

    protected boolean groupLabels;

    public BivariateScatterControl() {
        this.series = new Vector<double[][]>();
        this.labels = new Vector<Integer>();
        this.ulabels = new HashSet<Integer>();
        this.bubblesize = new Vector<Double>();
        this.grid = new Basic2DGrid();
        this.border = 50;
        this.groupLabels = true;
    }

    public void resetControl() {
        this.maxx = Double.NEGATIVE_INFINITY;
        this.maxy = Double.NEGATIVE_INFINITY;
        this.minx = Double.POSITIVE_INFINITY;
        this.miny = Double.POSITIVE_INFINITY;

        this.grid.resetControl();

        this.series.clear();
        this.labels.clear();
        this.ulabels.clear();

        this.groupLabels = true;
    }

    public void setXAxisLabel(String lab) {
        this.grid.setXAxisLabel(lab);
    }

    public void setYAxisLabel(String lab) {
        this.grid.setYAxisLabel(lab);
    }

    public void setAsTimestampedSeries(boolean stamp) {
        this.grid.setXAxisAsDatetime(stamp);
    }

    public void addData(double[][] data, int[] label) {
        this.groupLabels = false;

        for (int i = 0; i < label.length; i++) {
            this.labels.add(new Integer(label[i]));
            this.ulabels.add(new Integer(label[i]));
        }

        this.addData(data);
    }

    public void addData(double[][] data, int label) {
        this.groupLabels = true;

        this.labels.add(new Integer(label));
        this.ulabels.add(new Integer(label));

        this.addData(data);
    }

    public void addData(double[][] data) {
        if (data == null) {
            return;
        }

        this.groupLabels = false;

        for (int i = 0; i < data.length; i++) {
            if (data[i][0] > this.maxx) {
                this.maxx = data[i][0];
            }
            if (data[i][0] < this.minx) {
                this.minx = data[i][0];
            }
            if (data[i][1] > this.maxy) {
                this.maxy = data[i][1];
            }
            if (data[i][1] < this.miny) {
                this.miny = data[i][1];
            }
        }

        this.grid.processXAxis(this.maxx, this.minx);
        this.grid.processYAxis(this.maxy, this.miny);

        this.series.add(data);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = this.getSize();

        double insetx = this.border;
        double insety = this.border;
        double wdth = d.width - 2 * this.border;
        double hght = d.height - 2 * this.border;

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, d.width, d.height);
        g2d.setColor(Color.black);

        this.grid.setBounds(insetx, insety, wdth, hght);
        this.grid.paint(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (this.series.isEmpty()) {
            this.grid.drawNoData(g2d);
        }
        else {
            this.drawData(g2d);
        }
    }

    protected void drawData(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        double xinc = this.grid.getBounds().getWidth() / (this.grid.getMaxX() - this.grid.getMinX());
        double yinc = this.grid.getBounds().getHeight() / (this.grid.getMaxY() - this.grid.getMinY());

        Ellipse2D ep2 = new Ellipse2D.Double();
        double x, y;
        Stroke s = g.getStroke();

        g.setStroke(bs);

        for (int i = 0; i < this.series.size(); i++) {
            Color srsClr;

            if (this.groupLabels) {
                //need to set the colour according to class...
                if (!this.labels.isEmpty()) {
                    float frac =/*360.0F**/ ((float) this.labels.get(i).intValue() / (float) this.ulabels.size());
                    srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
                }
                else {
                    srsClr = Color.red;
                }

                g.setColor(srsClr);
            }

            for (int j = 0; j < this.series.get(i).length; j++) {
                if (!this.groupLabels) {
                    //need to set the colour according to class...
                    if (!this.labels.isEmpty()) {
                        float frac =/*360.0F**/ ((float) this.labels.get(j).intValue() / (float) this.ulabels.size());
                        srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
                    }
                    else {
                        srsClr = Color.red;
                    }

                    g.setColor(srsClr);
                }

                x = this.grid.getBounds().getMinX() + ((this.series.get(i)[j][0] - this.grid.getMinX()) * xinc);
                y = this.grid.getBounds().getMinY() + this.grid.getBounds().getHeight() - (((this.series.get(i)[j][1] - this.grid.getMinY()) * yinc));

                ep2.setFrame(x - 1.0, y - 1.0, 2.0, 2.0);
                g.fill(ep2);
            }
        }

        g.setStroke(s);
    }

    protected class Basic2DGrid {

        protected Rectangle2D gridbounds;
        protected int gridxinc;
        protected int gridyinc;
        protected double maxx;
        protected double minx;
        protected double maxy;
        protected double miny;
        protected String measurement;
        protected String units;
        protected int selection;
        protected boolean drawXaxis;
        protected boolean drawYaxis;
        protected boolean drawXgridlines;
        protected boolean drawYgridlines;
        protected int xInset;
        protected boolean yCategorical;
        protected String[] yCategories;
        protected String xlab;
        protected String ylab;
        private boolean datesInTimeSeries;

        public Basic2DGrid() {
            this.gridbounds = new Rectangle2D.Double();
            this.gridxinc = 8;
            this.gridyinc = 8;

            this.selection = -1;

            this.datesInTimeSeries = false;
            this.units = "";

            this.drawXaxis = true;
            this.drawYaxis = true;
            this.drawXgridlines = true;
            this.drawYgridlines = true;
            this.xInset = 0;
            this.yCategorical = false;
            this.yCategories = null;

            this.xlab = null;
            this.ylab = null;
        }

        public void setXAxisAsDatetime(boolean ax) {
            this.datesInTimeSeries = ax;
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

        public Rectangle2D getBounds() {
            return this.gridbounds;
        }

        public void setBounds(double x, double y, double w, double h) {
            this.gridbounds.setRect(this.xInset + x, y, w, h);
        }

        public void paint(Graphics2D g) {
            this.drawGrid(g);
        }

        public void setDrawXAxis(boolean drawXaxis) {
            this.drawXaxis = drawXaxis;
        }

        public void setDrawYAxis(boolean drawYaxis) {
            this.drawYaxis = drawYaxis;
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

        protected void drawGrid(Graphics2D g) {
            float[] dashPattern = {5, 5, 5, 5};

            DecimalFormat numf = new DecimalFormat();

            AffineTransform mvTxt = new AffineTransform();
            AffineTransform rotTxt = new AffineTransform();
            AffineTransform txCur = g.getTransform();

            mvTxt.translate(this.gridbounds.getMinX() - 30.0, this.gridbounds.getCenterY());
            rotTxt.rotate(Math.toRadians(-90));
            mvTxt.concatenate(rotTxt);

            BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            BasicStroke bstr = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0);
            Stroke s = g.getStroke();

            Font ft = new Font("SansSerif", Font.PLAIN, 10);
            Font ftu = new Font("SansSerif", Font.PLAIN, 12);
            FontRenderContext frc = g.getFontRenderContext();
            Line2D l2 = new Line2D.Double();

            g.setFont(ftu);
            g.setStroke(bs);
            g.setColor(Color.black);
            g.draw(this.gridbounds);

            g.setStroke(bstr);

            g.transform(mvTxt);

            //if(this.drawYaxis && !this.yCategorical)
            if (this.ylab != null) {
                Rectangle2D bnd = ft.getStringBounds(this.ylab, frc);

                g.drawString(this.ylab, (int) (-0.5 * (bnd.getWidth())), (int) (-0.1 * bnd.getHeight()));
            }

            g.setTransform(txCur);

            if (this.xlab != null) {
                Rectangle2D bnd = ft.getStringBounds(this.xlab, frc);

                g.drawString(this.xlab, (int) ((int) this.gridbounds.getMinX() + 0.5 * (this.gridbounds.getWidth() - bnd.getWidth())), (int) (this.gridbounds.getHeight() + 7.0 * bnd.getHeight()));
            }

            g.setFont(ft);

            for (int i = 0; i <= this.gridxinc; i++) {
                double x = this.gridbounds.getMinX() + i * this.gridbounds.getWidth() / this.gridxinc;

                if (i != this.gridxinc) {
                    l2.setLine(x, (int) this.gridbounds.getMinY(), x, (int) this.gridbounds.getMaxY());
                }

                if (this.drawXgridlines) {
                    g.draw(l2);
                }

                if (this.drawXaxis) {
                    String lab;

                    if (this.datesInTimeSeries) {
                        lab = ITLClientUtilities.millisecondsToDateString((new Double(this.minx + i * (this.maxx - this.minx) / this.gridxinc)).longValue());
                    }
                    else {
                        lab = numf.format(this.minx + i * (this.maxx - this.minx) / this.gridxinc);
                    }

                    Rectangle2D bnd = ft.getStringBounds(lab, frc);
                    TextLayout layout = new TextLayout(lab, ft, frc);

                    layout.draw(g, (float) (x - 0.5 * bnd.getWidth()), (float) (this.gridbounds.getMaxY() + bnd.getHeight() * 1.5));
                }
            }

            if (!this.yCategorical) {
                for (int i = 0; i <= this.gridyinc; i++) {
                    double y = (int) this.gridbounds.getMinY() + i * this.gridbounds.getHeight() / this.gridyinc;
                    String lab = numf.format(this.maxy - i * (this.maxy - this.miny) / this.gridyinc);
                    Rectangle2D bnd = ft.getStringBounds(lab, frc);

                    if (i != this.gridyinc) {
                        l2.setLine(this.gridbounds.getMinX(), y, this.gridbounds.getMaxX(), y);
                    }

                    if (this.drawYgridlines) {
                        g.draw(l2);
                    }
                    if (this.drawYaxis) {
                        g.drawString(lab, (int) (this.gridbounds.getMinX() - bnd.getMaxX()), (int) (y + 0.5 * bnd.getHeight()));
                    }
                }
            }
            else {
                if (this.yCategories != null) {

                }
            }

            g.setStroke(s);
        }

        protected void drawNoData(Graphics2D g) {
            RoundRectangle2D rr = new RoundRectangle2D.Float();
            BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            Stroke s = g.getStroke();

            Font ftu = new Font("Courier", Font.PLAIN, 20);
            FontRenderContext frc = g.getFontRenderContext();

            String note = "No data for selected time period.";

            Rectangle2D bnd = ftu.getStringBounds(note, frc);
            TextLayout layout = new TextLayout(note, ftu, frc);

            double x = this.getBounds().getMinX() + 0.5 * bnd.getWidth();
            double y = this.getBounds().getMinY() + 0.5 * bnd.getHeight();

            rr.setFrame(this.gridbounds);
            g.setStroke(bs);
            g.setColor(Color.black);
            g.draw(rr);

            layout.draw(g, (float) (x + 50.0), (float) y);

            layout = new TextLayout(note, ftu, frc);

            layout.draw(g, (float) (x + 50.0), (float) (y + bnd.getHeight()));

            g.setStroke(s);
        }
    }
}
