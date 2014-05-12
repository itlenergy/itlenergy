package com.itl_energy.webclient.profile.visualisation;

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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;

import com.itl_energy.webclient.profile.TimeSeries;

public abstract class TimeSeriesControl extends JComponent {

    private static final long serialVersionUID = 1L;
    protected Dimension minimumSize;
    protected TimeSeries data;
    protected TimeSeriesGrid grid;
    protected SimpleDateFormat parse;

    protected int border;

    public TimeSeriesControl() {
        this.minimumSize = new Dimension(50, 100);
        this.setMinimumSize(this.minimumSize);
        this.data = null;
        this.grid = new TimeSeriesGrid();
        this.border = 10;
        this.parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    }

    public void setSeriesData(TimeSeries ts) {
        this.data = ts;
    }

    protected double millisecondsFromTimestamp(String time) {
        try {
            Date d = this.parse.parse(time);
            Calendar c = Calendar.getInstance();

            c.setTime(d);

            return (double) c.getTimeInMillis();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
    /*
     public double getTimeAt(int idx)
     {
     return this.obs.get(idx)[0];
     }
	
     public String getFirstDate()
     {
     Date measDate=new Date((new Double(this.obs.firstElement()[0]).longValue()));
		
     return this.form.format(measDate);
     }
	
     public String getLastDate()
     {
     Date measDate=new Date((new Double(this.obs.lastElement()[0]).longValue()));
		
     return this.form.format(measDate);
     } 
     */

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = this.getSize();

        double insetx = this.border;
        double insety = this.border;
        double wdth = d.width - 2 * this.border;
        double hght = d.height - 2 * this.border;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, d.width, d.height);
        g2d.setColor(Color.black);

        this.grid.setBounds(insetx, insety, wdth, hght);
        this.grid.paint(g2d);

        if (this.data == null) {
            this.grid.drawNoData(g2d);
        }
        else {
            this.drawData(g2d);
        }
    }

    protected abstract void drawData(Graphics2D g);

    protected class TimeSeriesGrid {

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
        protected boolean bivariate;
        protected boolean drawXaxis;
        protected boolean drawYaxis;
        protected boolean drawXgridlines;
        protected boolean drawYgridlines;
        protected int xInset;
        protected boolean yCategorical;
        protected String[] yCategories;

        public TimeSeriesGrid() {
            this.gridbounds = new Rectangle2D.Double();
            this.gridxinc = 8;
            this.gridyinc = 4;

            this.selection = -1;

            this.bivariate = false;

            this.units = "";

            this.drawXaxis = true;
            this.drawYaxis = true;
            this.drawXgridlines = true;
            this.drawYgridlines = true;
            this.xInset = 0;
            this.yCategorical = false;
            this.yCategories = null;
        }

        public double getMaxX() {
            return maxx;
        }

        public void setMaxX(double maxx) {
            this.maxx = maxx;
        }

        public double getMinX() {
            return minx;
        }

        public void setMinX(double minx) {
            this.minx = minx;
        }

        public double getMaxY() {
            return maxy;
        }

        public void setMaxY(double maxy) {
            this.maxy = maxy;
        }

        public double getMinY() {
            return miny;
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

        //TODO fix this for time axis - is it 8640000,3600000,60000?
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

            SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd HH:mm:SS");
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

            Font ft = new Font("SansSerif", Font.PLAIN, 8);
            Font ftu = new Font("SansSerif", Font.PLAIN, 10);
            FontRenderContext frc = g.getFontRenderContext();
            Line2D l2 = new Line2D.Double();

            g.setFont(ftu);
            g.setStroke(bs);
            g.setColor(Color.black);
            g.draw(this.gridbounds);

            g.setStroke(bstr);

            g.transform(mvTxt);

            if (this.drawYaxis && !this.yCategorical) {
                g.drawString(this.units, 0, 0);
            }

            g.setTransform(txCur);

            g.setFont(ft);

            for (int i = 0; i <= this.gridxinc; i++) {
                double x = this.gridbounds.getMinX() + i * this.gridbounds.getWidth() / this.gridxinc;
                String lab;

                if (!this.bivariate) {
                    Date measDate = new Date((new Double(this.minx + i * (this.maxx - this.minx) / this.gridxinc)).longValue());
                    lab = form.format(measDate);
                }
                else {
                    lab = numf.format(this.maxx + i * (this.maxx - this.minx) / this.gridxinc);
                }

                Rectangle2D bnd = ft.getStringBounds(lab, frc);
                TextLayout layout = new TextLayout(lab, ft, frc);

                if (i != this.gridxinc) {
                    l2.setLine(x, (int) this.gridbounds.getMinY(), x, (int) this.gridbounds.getMaxY());
                }

                if (this.drawXgridlines) {
                    g.draw(l2);//causes JVM to crash!!!
                }
                if (this.drawXaxis) {
                    g.drawString(lab, (int) (x - 0.5 * bnd.getWidth()), (int) (this.gridbounds.getMaxY() + bnd.getHeight()));
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
