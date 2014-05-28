package com.itl_energy.webclient.itl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data storage for contextual information not being stored in the web service
 * for security or privacy reasons. This will be held locally only.
 *
 * @author Bruce Stephen
 * @date 7th October 2013
 */
public class TrialContextStore implements Serializable {

    private static final long serialVersionUID = -8183642739853597364L;
    protected Map<Integer, String> lookup;
    protected Map<Integer, String> authentications;

    public TrialContextStore() {
        this.lookup = new HashMap<Integer, String>();
    }

    public String getPremisesDetails(int houseid) {
        return this.lookup.get(new Integer(houseid));
    }

    public void addPremisesDetails(int houseid, String details) {
        this.lookup.put(new Integer(houseid), details);
    }

    public static TrialContextStore load() {
        TrialContextStore tcs = new TrialContextStore();

        try {
            File f = new File("D:\\itl.itl");
            f.createNewFile();

            FileInputStream in = new FileInputStream(f);
            ObjectInputStream s = new ObjectInputStream(in);
            tcs = (TrialContextStore) s.readObject();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);

            return tcs;
        }

        return tcs;
    }

    public boolean unload() {
        try {
            FileOutputStream f = new FileOutputStream("D:\\itl.itl");
            ObjectOutput s = new ObjectOutputStream(f);
            s.writeObject(this);
            s.flush();
            s.close();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);

            return false;
        }

        return true;
    }
}
