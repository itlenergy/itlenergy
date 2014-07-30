package com.itl_energy.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.itl_energy.android.graphics.DateManipulator;
import com.itl_energy.android.util.SystemUiHider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ListView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.itl_energy.webclient.itl.ITLClient;
import com.itl_energy.webclient.itl.model.DeployedSensor;
import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.model.Sensor;
import com.itl_energy.webclient.itl.util.ApiException;
import java.text.ParseException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ITLDisplayActivity extends Activity implements OnItemClickListener, OnSeekBarChangeListener {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    protected ITLClient controller;

    protected ListView controlsView;
    protected SeekBar dateView;
    protected ITLSensorDataView timeSeriesView;
    protected ProgressDialog pd;

    protected HashMap<String, Sensor> id2sensor;
    protected HashMap<Integer, DeployedSensor> id2dsensor;

    protected int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //enable internet access:
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itl_display);

        View contentView = findViewById(R.id.measurementview);

        controlsView = (ListView) findViewById(R.id.sensorview);
        timeSeriesView = (ITLSensorDataView) findViewById(R.id.measurementview);
        dateView = (SeekBar) findViewById(R.id.timeline);

        controlsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        controlsView.setTextFilterEnabled(true);
        controlsView.setOnItemClickListener(this);

        dateView.setOnSeekBarChangeListener(this);

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);

            String beginTime = "2013-06-01 00:00";
            String endTime = "2014-02-01 00:00";

            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            cal.setTime(df.parse(beginTime));
            cal2.setTime(df.parse(endTime));

            cal2.add(Calendar.DATE, 7);

            java.util.Date begin = df.parse(beginTime);
            java.util.Date end = df.parse(endTime);

            long diff = end.getTime() - begin.getTime();
            long updays = (diff / 1000) / 86400;

            //System.out.println("Up for "+updays+" days. Weeks: "+((diff/1000)/86400)/7);
            dateView.setMax((int) updays);//60 day window?
        }
        catch (ParseException e) {
            ExceptionDialog dialog = new ExceptionDialog(this);
            dialog.show("Could not parse dates.");
        }

        //this.controller = new ITLClient("http://10.0.2.2:8282/itlenergy-web/api");
        this.controller = new ITLClient("http://itl.itl-energy.com/api");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        id2sensor = new HashMap<>();
        id2dsensor = new HashMap<>();
        final Context context = (Context)this;

        // show dialog here
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final View loginView = inflater.inflate(R.layout.authentication_itl_display, null);
            
            final AlertDialog loginDialog = builder
                .setView(loginView)
                .setTitle("ITL Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            EditText username = (EditText)loginView.findViewById(R.id.username);
                            EditText password = (EditText)loginView.findViewById(R.id.password);

                            if (!controller.beginSession(username.getText().toString(), password.getText().toString())) {
                                ExceptionDialog exdialog = new ExceptionDialog(context);
                                exdialog.show("Invalid username or password");
                            }
                            else
                            {
                                //progress bar
                                int hid = 19;

                                //list sensors
                                List<DeployedSensor> dsns = controller.getDeployedSensors();
                                List<Sensor> sns = controller.getSensorsForHub(hid);
                                String[] sensorArray = new String[sns.size()];

                                for (DeployedSensor dsn : dsns) {
                                    id2dsensor.put(dsn.getTypeid(), dsn);
                                }

                                //populate list
                                for (int i = 0; i < sns.size(); i++) {
                                    sensorArray[i] = sns.get(i).getDescription();
                                    id2sensor.put(sns.get(i).getDescription(), sns.get(i));
                                }

                                // This is the array adapter, it takes the context of the activity as a 
                                // first parameter, the type of list view as a second parameter and your 
                                // array as a third parameter.
                                CustomArrayAdapter arrayAdapter = new CustomArrayAdapter(controlsView.getContext(), android.R.layout.simple_list_item_1, sensorArray);

                                controlsView.setAdapter(arrayAdapter);
                            }
                        }
                        catch (ApiException ex) {
                            ExceptionDialog exdialog = new ExceptionDialog(context);
                            exdialog.show(ex.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create();

            loginDialog.show();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected void onDestroy() {
        if (pd != null) {
            pd.dismiss();
        }

        super.onDestroy();
    }

    
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        try {
            //show graph from 1st sensor
            String label = (String) this.controlsView.getItemAtPosition(position);

            Sensor select = this.id2sensor.get(label);
            DeployedSensor type = this.id2dsensor.get(select.getTypeId());
            List<Measurement> readings = controller.getMeasurementsForSensor(select, "2013-09-15 00:00:00", "2013-09-20 00:00:00");

            this.listPosition = position;

            timeSeriesView.setYAxisLabel(type.getMeasurementUnits());

            timeSeriesView.reset();

            if (label.contains("MeterReader"))//the meter reader...
            {
                for (int m = 1; m < readings.size(); m++)//do advances rather than absolute readings...
                {
                    timeSeriesView.addData(readings.get(m).getObservationTime(), new double[]{readings.get(m).getObservation() - readings.get(m - 1).getObservation()});
                }
            }
            else {
                for (Measurement reading : readings) {
                    timeSeriesView.addData(reading.getObservationTime(), new double[]{reading.getObservation()});
                }
            }

            timeSeriesView.invalidate();
        }
        catch (ApiException ex) {
            ExceptionDialog exdialog = new ExceptionDialog(this);
            exdialog.show(ex.getMessage());
        }
    }

    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final String[] values;
        private LayoutInflater mInflater;
        private Bitmap mIcon1;

        public CustomArrayAdapter(Context context, int resource, String[] values) {
            super(context, resource, values);

            this.context = context;
            this.values = values;

            mInflater = LayoutInflater.from(context);

            // Icons bound to the rows.
            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_sensor);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_element, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);

                convertView.setTag(holder);
            }
            else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            holder.text.setText(values[position]);
            holder.text.setTextSize(11.0F);
            holder.icon.setImageBitmap(mIcon1);

            return convertView;
        }

        protected class ViewHolder {

            TextView text;
            ImageView icon;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        DateManipulator dm = new DateManipulator();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long utc1 = dm.timeToUTC("2013-06-01 00:00:00");
        long utc2 = dm.timeToUTC("2014-02-01 00:00:00");

        long diff = (long) (utc1 + (long) (utc2 - utc1) * (double) seekBar.getProgress() / (double) seekBar.getMax());

        String res = dm.utcToTime(diff);

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            cal.setTime(df.parse(res));
            cal2.setTime(df.parse(res));

            cal.add(Calendar.DATE, -3);
            cal2.add(Calendar.DATE, 3);

            Toast.makeText(getApplicationContext(), df.format(cal.getTime()) + " to " + df.format(cal2.getTime()), Toast.LENGTH_LONG).show();

            String label = (String) this.controlsView.getItemAtPosition(listPosition);

            Sensor select = this.id2sensor.get(label);
            DeployedSensor type = this.id2dsensor.get(select.getTypeId());
            List<Measurement> readings = controller.getMeasurementsForSensor(select, df.format(cal.getTime()), df.format(cal2.getTime()));

            timeSeriesView.setYAxisLabel(type.getMeasurementUnits());

            timeSeriesView.reset();

            if (label.contains("MeterReader"))//the meter reader...
            {
                for (int m = 1; m < readings.size(); m++)//do advances rather than absolute readings...
                {
                    timeSeriesView.addData(readings.get(m).getObservationTime(), new double[]{readings.get(m).getObservation() - readings.get(m - 1).getObservation()});
                }
            }
            else {
                for (int m = 0; m < readings.size(); m++) {
                    timeSeriesView.addData(readings.get(m).getObservationTime(), new double[]{readings.get(m).getObservation()});
                }
            }

            timeSeriesView.invalidate();
        }
        catch (ParseException | ApiException ex) {
            ExceptionDialog exdialog = new ExceptionDialog(this);
            exdialog.show(ex.getMessage());
        }
    }
}
