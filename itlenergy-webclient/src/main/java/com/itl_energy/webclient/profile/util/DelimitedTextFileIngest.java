package com.itl_energy.webclient.profile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Utility class for loading and storing plain text files.
 *
 * @author bstephen
 * @version 17th May 2011
 */
public class DelimitedTextFileIngest {

    protected Vector<Vector<String>> data;
    protected String delimiter;

    public DelimitedTextFileIngest() {
        this.data = null;
        this.delimiter = ",";//comma delimited by default...
    }

    public void setDelimiter(String delim) {
        this.delimiter = delim;
    }

    public boolean loadDataFromFile(String fname) {
        this.data = this.ingestCSVFileAsRows(fname);

        if (this.data == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getRows() {
        return this.data.size();
    }

    public int getColumnsAtRow(int r) {
        return this.data.get(r).size();
    }

    public String getStringValueAtRowColumn(int r, int c) {
        return this.data.get(r).get(c);
    }

    public int getIntegerValueAtRowColumn(int r, int c) {
        return Integer.parseInt(this.getStringValueAtRowColumn(r, c));
    }

    public double getDoubleValueAtRowColumn(int r, int c) {
        return Double.parseDouble(this.getStringValueAtRowColumn(r, c));
    }

    protected Vector<Vector<String>> ingestCSVFileAsRows(String fname) {
        Vector<Vector<String>> rows = new Vector<Vector<String>>();
        File txtf = new File(fname);

        if (txtf != null) {
            BufferedReader bread;

            try {
                FileInputStream fis = new FileInputStream(txtf);
                InputStreamReader isr = new InputStreamReader(fis);

                bread = new BufferedReader(isr);
            }
            catch (IOException ioe) {
                ioe.printStackTrace(System.err);

                return rows;
            }

            StringTokenizer strTok;

            try {
                String line;

                do {
                    Vector<String> ob = new Vector<String>();

                    line = bread.readLine();

                    if (line == null) {
                        break;
                    }

                    strTok = new StringTokenizer(line, this.delimiter);

                    while (strTok.hasMoreElements()) {
                        ob.add(strTok.nextToken());
                    }

                    rows.add(ob);
                } while (line != null);

                bread.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }

        return rows;
    }
}
