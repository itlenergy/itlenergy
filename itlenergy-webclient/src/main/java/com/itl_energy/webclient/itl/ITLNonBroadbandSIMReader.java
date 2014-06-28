package com.itl_energy.webclient.itl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.itl_energy.webclient.itl.model.Measurement;

/**
 * Class for reading APAtSCHE non-broadband data copied from SIM cards.
 *
 * @author Bruce Stephen
 * @date 7th March
 */
public class ITLNonBroadbandSIMReader {

    protected String topnode;
    protected List<NonBroadbandHub> hubs;

    /*
     * Take a directory and enumerate all folders - each folder
     * represents a SIM card taken from a hub. Each hub has multiple
     * sensors attached. 
     */
    public ITLNonBroadbandSIMReader(String dir) throws IOException {
        this.topnode = dir;
        this.hubs = new ArrayList<NonBroadbandHub>();

        File[] files = new File(this.topnode).listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());

                NonBroadbandHub bbh = new NonBroadbandHub(this.topnode + "\\" + file.getName());

                this.hubs.add(bbh);
            }
        }
    }

    /**
     * Non Broadband hubs typically have metered appliance data, with columns
     * 'id', UNIX time stamp and instantaneous power measured in W. Environment
     * data consisting of a UNIX timestamp a temperature measurement and a
     * humidity. Some units may have outdoor temperature as well. PIR occupancy
     * sensors have a UNIX timestamp for occupancy occurrence - final column
     * denoted by 1. This is an asynchronous measurement.
     */
    protected class NonBroadbandHub {

        protected final static String ENVIRONMENT_CHANNEL = "environment";
        protected final static String METER_CHANNEL = "meter";
        protected final static String SUBMETER_CHANNEL = "appliance";
        protected final static String OCCUPANCY_CHANNEL = "motion";

        protected String[][] locations;

        public NonBroadbandHub(String location) throws IOException {
            File[] files = new File(location).listFiles();

            for (File file : files) {
                if (!file.isDirectory()) {
                    System.out.println("	File: " + file.getName());

                    TinyCSVReader csv = new TinyCSVReader(file);
                    String[] rws;

                    do {
                        rws = csv.getRows();

                        if (rws != null) {
                            for (int i = 0; i < rws.length; i++) {
                                System.out.print(rws[i] + " ");
                            }
                        }

                        System.out.println();
                    } while (rws != null);
                }
            }
        }

        protected Measurement[] processEnvironmentalData(String[] rws) {
            Measurement[] m = new Measurement[rws.length];

            m[1] = new Measurement();
            m[1].setObservationTime(rws[0]);

			//appliance: Id,time_created,GMT time,power1
            //environment: Id,time_created,GMT,mid,temp,hum
            //meter: "Id","time_created","power0"
            //motion: Id,time_created,GMT,mid,motion
            return m;
        }

        protected class TinyCSVReader {

            protected int dimension;
            protected StringTokenizer parser;
            protected BufferedReader reader;
            protected String delimiter;

            public TinyCSVReader(File f) throws IOException {
                FileInputStream fis = new FileInputStream(f);

                this.reader = new BufferedReader(new InputStreamReader(fis));
                this.delimiter = ",";
            }

            public void setDelimiter(String delim) {
                this.delimiter = delim;
            }

            public String getDelimiter() {
                return this.delimiter;
            }

            public String[] getRows() throws IOException {
                String line = this.reader.readLine();

                if (line == null) {
                    this.reader.close();

                    return null;
                }

                this.parser = new StringTokenizer(line, this.delimiter);
                this.dimension = this.parser.countTokens();

                String[] rows = new String[this.dimension];
                int idx = 0;

                while (this.parser.hasMoreTokens()) {
                    rows[idx++] = this.parser.nextToken(this.delimiter);
                }

                return rows;
            }
        }
    }
}
