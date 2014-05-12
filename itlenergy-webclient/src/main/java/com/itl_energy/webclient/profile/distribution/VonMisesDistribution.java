package com.itl_energy.webclient.profile.distribution;

/**
 * Von Mises distribution for circular data.
 *
 * @author bstephen
 * @version 26th May 2011
 */
public class VonMisesDistribution extends UnivariateDistribution {

    protected double nu;
    protected double kappa;

    public VonMisesDistribution() {
        this.nu = 0.0;
        this.kappa = 1.0;
    }

    public VonMisesDistribution(double nu, double kappa) {
        this.nu = nu;
        this.kappa = kappa;
    }

    public double getNu() {
        return this.nu;
    }

    public void setNu(double nu) {
        this.nu = nu;
    }

    public double getKappa() {
        return this.kappa;
    }

    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    public double likelihood(double x) {
        double likelihood = 0.0;
        double Zrad = 1.0 / (2.0 * Math.PI * VonMisesDistribution.BesselI0(this.kappa));

        likelihood = Zrad * Math.exp(this.kappa * Math.cos(x - this.nu));

        return likelihood;
    }

    public void maximumLikelihoodEstimate(double[] data) {
        double cbar = 0.0;
        double sbar = 0.0;

        for (int i = 0; i < data.length; i++) {
            cbar += Math.cos(data[i]);
            sbar += Math.sin(data[i]);
        }

        cbar /= (double) data.length;
        sbar /= (double) data.length;

        if (cbar >= 0.0) {
            this.nu = Math.atan(sbar / cbar);
        }
        else {
            this.nu = Math.atan(sbar / cbar) + Math.PI;
        }

        double rbar = Math.sqrt(Math.pow(cbar, 2.0) + Math.pow(sbar, 2.0));

        if (rbar > 0.53 && rbar < 0.85) {
            this.kappa = -0.4 + 1.39 * rbar + 0.43 / (1.0 - rbar);
        }
        else if (rbar <= 0.53) {
            this.kappa = 2.0 * rbar + Math.pow(rbar, 3.0) + 5.0 * Math.pow(rbar, 5.0) / 6.0;
        }
        else //if(rbar>=0.85)
        {
            this.kappa = 1.0 / (2.0 - 2.0 * rbar);
        }
    }

    public static double BesselI0(double kappa) {
        double a;
        double b;
        double exp40 = 2.353852668370199854E+17;
        int i;
        double[] p = {
            -5.2487866627945699800E-18,
            -1.5982226675653184646E-14,
            -2.6843448573468483278E-11,
            -3.0517226450451067446E-08,
            -2.5172644670688975051E-05,
            -1.5453977791786851041E-02,
            -7.0935347449210549190E+00,
            -2.4125195876041896775E+03,
            -5.9545626019847898221E+05,
            -1.0313066708737980747E+08,
            -1.1912746104985237192E+10,
            -8.4925101247114157499E+11,
            -3.2940087627407749166E+13,
            -5.5050369673018427753E+14,
            -2.2335582639474375249E+15};
        double[] pp = {
            -3.9843750000000000000E-01,
            2.9205384596336793945E+00,
            -2.4708469169133954315E+00,
            4.7914889422856814203E-01,
            -3.7384991926068969150E-03,
            -2.6801520353328635310E-03,
            9.9168777670983678974E-05,
            -2.1877128189032726730E-06};
        double[] q = {
            -3.7277560179962773046E+03,
            6.5158506418655165707E+06,
            -6.5626560740833869295E+09,
            3.7604188704092954661E+12,
            -9.7087946179594019126E+14};
        double[] qq = {
            -3.1446690275135491500E+01,
            8.5539563258012929600E+01,
            -6.0228002066743340583E+01,
            1.3982595353892851542E+01,
            -1.1151759188741312645E+00,
            3.2547697594819615062E-02,
            -5.5194330231005480228E-04};

        double rec15 = 6.6666666666666666666E-02;
        double sump;
        double sumq;
        double value = 0.0;
        double x;
        double xmax = 91.9;
        double xsmall = 2.98E-08;
        double xx;

        x = Math.abs(kappa);

        if (x < xsmall) {
            value = 1.0;
        }
        else if (x < 15.0) {
			//
            //  XSMALL <= ABS(ARG) < 15.0
            //
            xx = x * x;
            sump = p[0];

            for (i = 1; i < 15; i++) {
                sump = sump * xx + p[i];
            }

            xx = xx - 225.0;

            sumq = ((((xx + q[0])
                    * xx + q[1])
                    * xx + q[2])
                    * xx + q[3])
                    * xx + q[4];

            value = sump / sumq;
        }
        else if (15.0 <= x) {
            if (xmax < x) {
                value = Double.MAX_VALUE;
            }
            else {
				//
                //  15.0 <= ABS(ARG)
                //
                xx = 1.0 / x - rec15;

                sump = ((((((pp[0]
                        * xx + pp[1])
                        * xx + pp[2])
                        * xx + pp[3])
                        * xx + pp[4])
                        * xx + pp[5])
                        * xx + pp[6])
                        * xx + pp[7];

                sumq = ((((((xx + qq[0])
                        * xx + qq[1])
                        * xx + qq[2])
                        * xx + qq[3])
                        * xx + qq[4])
                        * xx + qq[5])
                        * xx + qq[6];

                value = sump / sumq;
				//
                //  Calculation reformulated to avoid premature overflow.
                //

                if (x <= xmax - 15.0) {
                    a = Math.exp(x);
                    b = 1.0;
                }
                else {
                    a = Math.exp(x - 40.0);
                    b = exp40;
                }

                value = ((value * a - pp[0] * a) / Math.sqrt(x)) * b;
            }
        }

        return value;
    }

    public String toString() {
        return "M(" + this.getNu() + "," + this.getKappa() + ")";
    }
}
