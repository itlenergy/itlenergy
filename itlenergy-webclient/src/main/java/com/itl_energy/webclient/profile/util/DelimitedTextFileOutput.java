package com.itl_energy.webclient.profile.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Utility class for outputting data to delimited text format.
 *
 * @author bstephen
 * @version 19th May 2011
 */
public class DelimitedTextFileOutput {

    protected FileOutputStream fos;
    protected BufferedWriter bwriter;
    protected String delimiter;
    protected int column;

    public DelimitedTextFileOutput(String fname) throws IOException {
        this.fos = new FileOutputStream(fname);
        this.bwriter = new BufferedWriter(new OutputStreamWriter(this.fos));
        this.delimiter = ",";
        this.column = 0;
    }

    public void setDelimiter(String delim) {
        this.delimiter = delim;
    }

    public void write(Object o) throws IOException {
        if (this.column != 0) {
            this.bwriter.write(this.delimiter);
        }

        this.bwriter.write(o.toString());
        this.column++;
    }

    public void finishRow() throws IOException {
        this.bwriter.append('\n');
        this.column = 0;
    }

    public void closeFile() throws IOException {
        this.bwriter.flush();
        this.bwriter.close();
    }
}
