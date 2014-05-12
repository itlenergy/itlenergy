package com.itl_energy.webclient.profile.visualisation;

import java.awt.AlphaComposite;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;

import com.itl_energy.webclient.profile.distribution.VonMisesDistribution;

/**
 * Class for representing periodic data.
 *
 * @author bstephen
 * @version 11th May 2011
 */
public class PolarControl extends JComponent {

    private static final long serialVersionUID = 8934488503514345296L;
    protected PolarGrid grid;
    protected int border;
    protected Vector<double[][]> series;
    protected Vector<double[]> data;
    protected Vector<Integer> labels;
    protected Set<Integer> ulabels;
    protected double[] density;
    protected int resolution;

    protected boolean showPDF;
    protected boolean showDistributions;
    protected boolean showDataPoints;

	//TODO need to have max/min radii
    //TODO need proper labels
    //TODO need density/scatter/contour modes
    public PolarControl() {
        this.grid = new PolarGrid();
        this.border = 10;
        this.series = new Vector<double[][]>();
        this.data = new Vector<double[]>();
        this.labels = new Vector<Integer>();
        this.ulabels = new HashSet<Integer>();
        this.resolution = 180;
        this.density = new double[this.resolution];

        this.showPDF = true;
        this.showDistributions = false;
        this.showDataPoints = true;
    }

    public void resetControl() {
        this.series.clear();
        this.data.clear();
        this.labels.clear();
        this.ulabels.clear();
    }

    public void setShowPDF(boolean b) {
        this.showPDF = b;
    }

    public void setShowDataPoints(boolean b) {
        this.showDataPoints = b;
    }

    public void setShowDistributions(boolean b) {
        this.showDistributions = b;
    }

    public void setAsTimeMode() {
        this.grid.setDisplayMode(PolarGrid.DISPLAY_MODE_DAYHOUR);
    }

    public void setAsYearMode() {
        this.grid.setDisplayMode(PolarGrid.DISPLAY_MODE_YEARDAY);
    }

    public void setAsAngleMode() {
        this.grid.setDisplayMode(PolarGrid.DISPLAY_MODE_ANGLE);
    }

    public void setAsRadiianMode() {
        this.grid.setDisplayMode(PolarGrid.DISPLAY_MODE_RADIIAN);
    }

    public void setAsCompassMode() {
        this.grid.setDisplayMode(PolarGrid.DISPLAY_MODE_COMPASS);
    }

    protected static Point2D getFromPolar(double rad, double angle) {
        Point2D polar = new Point2D.Double();

        polar.setLocation(Math.cos(angle) * rad, Math.sin(angle) * rad);

        return polar;
    }

    public void addData(double[][] d) {
        this.series.add(d);
    }

    public void addData(double[][] d, int label) {
        this.labels.add(new Integer(label));
        this.ulabels.add(new Integer(label));
        this.series.add(d);
    }

    public void addData(double r, double a, int label) {
        this.labels.add(new Integer(label));
        this.ulabels.add(new Integer(label));

        this.addData(r, a);
    }

    public void addData(double r, double a) {
        double[] d = new double[2];

        d[0] = r;
        d[1] = a;

        this.data.add(d);
    }

    public void setCircularPDF(double[] p) {
        this.density = p;
        this.resolution = p.length;
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

        if (this.showPDF) {
            if (!this.data.isEmpty()) {
                this.drawDensity(g2d);
            }
        }

        this.grid.paint(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (this.showDataPoints) {
            if (this.data.isEmpty()) {
                this.grid.drawNoData(g2d);
            }
            else {
                this.drawData(g2d);
            }
        }

        if (this.showDistributions) {
            if (this.series.size() > 0) {
                this.drawDistributions(g2d);
            }
        }
    }

    protected void drawData(Graphics2D g) {
        Ellipse2D ep2 = new Ellipse2D.Double();

        g.setColor(Color.red);

        for (int i = 0; i < this.data.size(); i++) {
            Color srsClr;

            if (!this.labels.isEmpty()) {
                float frac = ((float) this.labels.get(i).intValue() / (float) this.ulabels.size());
                srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
            }
            else {
                srsClr = Color.red;
            }

            g.setColor(srsClr);

            double rad = this.data.get(i)[0] * this.grid.getRadius();
            double ang = this.data.get(i)[1];

            if (!this.grid.isClockwise()) {
                ang = 2.0 * Math.PI - ang;
            }

            ang += Math.toRadians(this.grid.degreeOffset());

            Point2D p = PolarControl.getFromPolar(rad, ang);

            ep2.setFrame(this.grid.getOrigin().getX() + p.getX() - 2.0, this.grid.getOrigin().getY() + p.getY() - 2.0, 4.0, 4.0);

            g.fill(ep2);
        }
    }

    protected void drawDensity(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        AlphaComposite ac1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        Stroke s = g.getStroke();
        GeneralPath pth = new GeneralPath();
        Ellipse2D ep2 = new Ellipse2D.Double();
        Point2D p;

        this.estimateCircularPDFForSeries();

        for (int j = 0; j < this.resolution; j++) {
            double ang = ((double) j) * 2.0 * Math.PI / (double) this.resolution;

            if (!this.grid.isClockwise()) {
                ang = 2.0 * Math.PI - ang;
            }

            ang += Math.toRadians(this.grid.degreeOffset());

            p = PolarControl.getFromPolar((0.5 + 0.5 * this.density[j]) * this.grid.getRadius(), ang);

            if (j == 0) {
                pth.moveTo(this.grid.getOrigin().getX() + p.getX(), this.grid.getOrigin().getY() + p.getY());
            }
            else {
                pth.lineTo(this.grid.getOrigin().getX() + p.getX(), this.grid.getOrigin().getY() + p.getY());
            }
        }

        pth.closePath();

        g.setComposite(ac2);
        g.setColor(Color.RED);
        g.fill(pth);

        g.setComposite(ac1);
        g.setStroke(bs);
        g.setColor(Color.BLACK);
        g.draw(pth);

        ep2.setFrame(this.grid.getOrigin().getX() - 0.5 * this.grid.getRadius(), this.grid.getOrigin().getY() - 0.5 * this.grid.getRadius(), this.grid.getRadius(), this.grid.getRadius());
        g.setColor(Color.WHITE);
        g.fill(ep2);

        g.setStroke(s);
    }

    protected void drawDistributions(Graphics2D g) {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        AlphaComposite ac1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        Stroke s = g.getStroke();
        Ellipse2D ep2 = new Ellipse2D.Double();
        Point2D p;
        Color srsClr;

        for (int i = 0; i < this.series.size(); i++) {
            GeneralPath pth = new GeneralPath();

            if (!this.labels.isEmpty()) {
                float frac =/*360.0F**/ ((float) this.labels.get(i).intValue() / (float) this.ulabels.size());
                srsClr = Color.getHSBColor(frac, 1.0F, 1.0F);
            }
            else {
                srsClr = Color.RED;
            }

            for (int j = 0; j < this.series.get(i).length; j++) {
                double ang =/*((double)j)*2.0*Math.PI/(double)this.resolution*/ this.series.get(i)[j][0];

                if (!this.grid.isClockwise()) {
                    ang = 2.0 * Math.PI - ang;
                }

                ang += Math.toRadians(this.grid.degreeOffset());

                p = PolarControl.getFromPolar((0.5 + 0.5  */*this.density[j]*/this.series.get(i)[j][1]) * this.grid.getRadius(), ang);

                if (j == 0) {
                    pth.moveTo(this.grid.getOrigin().getX() + p.getX(), this.grid.getOrigin().getY() + p.getY());
                }
                else {
                    pth.lineTo(this.grid.getOrigin().getX() + p.getX(), this.grid.getOrigin().getY() + p.getY());
                }
            }

            pth.closePath();

            g.setComposite(ac2);
            g.setColor(srsClr);
            g.fill(pth);

            g.setComposite(ac1);
            g.setStroke(bs);
            g.setColor(Color.BLACK);
            g.draw(pth);
        }

        ep2.setFrame(this.grid.getOrigin().getX() - 0.5 * this.grid.getRadius(), this.grid.getOrigin().getY() - 0.5 * this.grid.getRadius(), this.grid.getRadius(), this.grid.getRadius());
        g.setColor(Color.WHITE);
        g.fill(ep2);

        g.setStroke(s);
    }

    protected void estimateCircularPDFForSeries() {
		//TODO replace the following with the appropriate Kernel class

        double[] dens = new double[this.data.size()];

        for (int i = 0; i < dens.length; i++) {
            dens[i] = this.data.get(i)[1];
        }

        Arrays.sort(dens);

        double min = 0.0;
        double max = 2.0 * Math.PI;

        double span = max - min;
        double inc = span / (double) resolution;

        double maxp = Double.MIN_VALUE;
        double kappa = 16.0;
        double Z = 1.0 / (2.0 * Math.PI * VonMisesDistribution.BesselI0(kappa));

        for (int i = 0; i < this.resolution; i++) {
            double x =/*dens[0]+*/ i * inc;
            double classsum = 0.0;

            for (int j = 0; j < dens.length; j++) {
                classsum += Math.exp(kappa * Math.cos(x - dens[j]));
            }

            this.density[i] = Z * classsum;

            if (this.density[i] > maxp) {
                maxp = this.density[i];
            }
        }

        for (int k = 0; k < this.resolution; k++) {
            this.density[k] /= maxp;
        }
    }

    protected class PolarGrid {

        public static final int DISPLAY_MODE_ANGLE = 0;
        public static final int DISPLAY_MODE_RADIIAN = 1;
        public static final int DISPLAY_MODE_COMPASS = 2;
        public static final int DISPLAY_MODE_DAYHOUR = 3;
        public static final int DISPLAY_MODE_YEARDAY = 4;

        protected Rectangle2D gridbounds;
        protected double mina;
        protected double minr;
        protected double maxa;
        protected double maxr;
        protected int gridainc;
        protected int gridrinc;
        protected boolean drawAgridlines;
        protected boolean drawRgridlines;
        protected Point2D origin;
        protected double radius;
        protected int mode;
        protected boolean clockwise;
        protected int offset;

        public PolarGrid() {
            this.gridbounds = new Rectangle2D.Double();
            this.origin = new Point2D.Double();
            this.gridainc = 8;
            this.gridrinc = 8;
            this.radius = 0.0;

            this.offset = 0;
            this.mode = PolarGrid.DISPLAY_MODE_ANGLE;
            this.clockwise = false;

            this.drawAgridlines = true;
            this.drawRgridlines = true;
        }

        public int degreeOffset() {
            return this.offset;
        }

        public boolean isClockwise() {
            return this.clockwise;
        }

        public int getDisplayMode() {
            return this.mode;
        }

        public void setDisplayMode(int mode) {
            this.mode = mode;

            switch (this.mode) {
                case PolarGrid.DISPLAY_MODE_ANGLE: {
                    this.gridainc = 12;
                    this.offset = 0;
                    this.clockwise = false;

                    break;
                }
                case PolarGrid.DISPLAY_MODE_RADIIAN: {
                    this.gridainc = 4;
                    this.offset = 0;
                    this.clockwise = false;

                    break;
                }
                case PolarGrid.DISPLAY_MODE_COMPASS: {
                    this.gridainc = 8;
                    this.offset = -90;
                    this.clockwise = true;

                    break;
                }
                case PolarGrid.DISPLAY_MODE_DAYHOUR: {
                    this.gridainc = 24;
                    this.offset = -90;
                    this.clockwise = true;

                    break;
                }
                case PolarGrid.DISPLAY_MODE_YEARDAY: {
                    this.gridainc = 12;
                    this.offset = -90;
                    this.clockwise = true;

                    break;
                }
                default:
                    break;
            }
        }

        void SetMaximumRadius(double maxr) {
            this.maxr = maxr;
        }

        void SetMinimumRadius(double minr) {
            this.minr = minr;
        }

        void SetMaximumAngle(double maxa) {
            this.maxa = maxa;
        }

        void SetMinimumAngle(double mina) {
            this.mina = mina;
        }

        double GetMaximumRadius() {
            return this.maxr;
        }

        double GetMinimumRadius() {
            return this.minr;
        }

        public double GetMaximumAngle() {
            return this.maxa;
        }

        public double GetMinimumAngle() {
            return this.mina;
        }

        public double getRadius() {
            return this.radius;
        }

        public Point2D getOrigin() {
            return this.origin;
        }

        public Rectangle2D getBounds() {
            return this.gridbounds;
        }

        public void setBounds(double x, double y, double w, double h) {
            this.gridbounds.setRect(x, y, w, h);
        }

        public void paint(Graphics2D g) {
            this.drawGrid(g);
        }

        protected void drawGrid(Graphics2D g) {
            float[] dashPattern = {5, 5, 5, 5};
            String[] complab = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
            String[] monlab = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};//not wise...
            this.radius = this.gridbounds.getWidth() < this.gridbounds.getHeight() ? this.gridbounds.getWidth() / 2.0 : this.gridbounds.getHeight() / 2.0;
            double a = 0.0;
            double inset = 40.0;
            double inc = 2.0 * Math.PI / this.gridainc;
            DecimalFormat numf = new DecimalFormat();

            BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            BasicStroke bstr = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0);
            Stroke s = g.getStroke();

            Font ft = new Font("SansSerif", Font.PLAIN, 6);
            Font ftu = new Font("SansSerif", Font.PLAIN, 12);
            FontRenderContext frc = g.getFontRenderContext();
            Line2D l2 = new Line2D.Double();
            Ellipse2D ep2 = new Ellipse2D.Double();

            g.setFont(ftu);
            g.setStroke(bs);
            g.setColor(Color.black);

            this.radius -= inset;

            this.origin.setLocation(this.gridbounds.getMinX() + 0.5 * this.gridbounds.getWidth(), this.gridbounds.getMinY() + 0.5 * this.gridbounds.getHeight());

            ep2.setFrame(origin.getX() - this.radius, origin.getY() - this.radius, 2.0 * this.radius, 2.0 * this.radius);
            g.draw(ep2);//draw black...

            g.setStroke(bstr);

            for (int i = 0; i < (int) this.gridainc; i++) {
                Point2D polar = PolarControl.getFromPolar(this.radius, a);

                l2.setLine(origin.getX(), origin.getY(), origin.getX() + polar.getX(), origin.getY() + polar.getY());

                g.draw(l2);

                a += inc;
            }

            g.setStroke(bs);

            for (int i = 1; i < this.gridrinc; i++) {
                double rad = (((double) i) * this.radius) / this.gridrinc;

                ep2.setFrame(origin.getX() - rad, origin.getY() - rad, 2.0 * rad, 2.0 * rad);
                g.draw(ep2);
            }

            numf.setMaximumFractionDigits(2);

            for (int i = 0; i < this.gridainc; i++) {
                double ang = 2.0 * ((double) i) * Math.PI / this.gridainc;

                if (this.clockwise) {
                    ang = 2.0 * Math.PI - ang;
                }

                Point2D pt = PolarControl.getFromPolar(this.radius, ang + Math.toRadians(this.offset));
                String label;

                switch (this.mode) {
                    case PolarGrid.DISPLAY_MODE_ANGLE: {
                        label = numf.format(Math.toDegrees(ang));

                        break;
                    }
                    case PolarGrid.DISPLAY_MODE_RADIIAN: {
                        label = numf.format(ang);

                        break;
                    }
                    case PolarGrid.DISPLAY_MODE_COMPASS: {
                        label = complab[i];

                        break;
                    }
                    case PolarGrid.DISPLAY_MODE_DAYHOUR: {
                        label = numf.format(ang * 12.0 / Math.PI) + ":00";

                        break;
                    }
                    case PolarGrid.DISPLAY_MODE_YEARDAY: {
                        label = monlab[i];

                        break;
                    }
                    default: {
                        label = numf.format(ang);

                        break;
                    }
                }

                Rectangle2D bnd = ftu.getStringBounds(label, frc);
                TextLayout layout = new TextLayout(label, ftu, frc);
                float x, y;

                if (ang <= Math.PI) {
                    x = (float) (this.origin.getX() + pt.getX());

                    if (ang <= 0.5 * Math.PI || ang >= 1.5 * Math.PI) {
                        y = (float) ((this.origin.getY() + pt.getY()) - bnd.getHeight());
                    }
                    else {
                        y = (float) ((this.origin.getY() + pt.getY()) + bnd.getHeight());
                    }
                }
                else {
                    x = (float) (this.origin.getX() + pt.getX() - bnd.getWidth());

                    if (ang <= 0.5 * Math.PI || ang >= 1.5 * Math.PI) {
                        y = (float) ((this.origin.getY() + pt.getY()) - bnd.getHeight());
                    }
                    else {
                        y = (float) ((this.origin.getY() + pt.getY()) + bnd.getHeight());
                    }
                }

                layout.draw(g, x, y);
            }

            g.setStroke(s);
        }

        protected void drawNoData(Graphics2D g) {
            RoundRectangle2D rr = new RoundRectangle2D.Float();
            BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            Stroke s = g.getStroke();

            Font ftu = new Font("Arial", Font.BOLD, 20);
            FontRenderContext frc = g.getFontRenderContext();

            String note = "No data selected.";

            Rectangle2D bnd = ftu.getStringBounds(note, frc);
            TextLayout layout = new TextLayout(note, ftu, frc);

            double x = this.getBounds().getMinX() + 0.5 * (this.getBounds().getWidth() - bnd.getWidth());
            double y = this.getBounds().getMinY() + 0.5 * (this.getBounds().getHeight() - bnd.getHeight());

            rr.setFrame(this.gridbounds);
            g.setStroke(bs);
            g.setColor(Color.red);
            g.draw(rr);

            layout.draw(g, (float) x, (float) (y - 0.5 * bnd.getHeight()));

            g.setStroke(s);
        }
    }
}
