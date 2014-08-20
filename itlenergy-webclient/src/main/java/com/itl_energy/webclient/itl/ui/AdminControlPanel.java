package com.itl_energy.webclient.itl.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JOptionPane;

import com.itl_energy.webclient.itl.ITLClient;
import com.itl_energy.webclient.itl.model.DeployedSensor;
import com.itl_energy.webclient.itl.model.DeploymentSite;
import com.itl_energy.webclient.itl.model.Hub;
import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.model.MeteredPremises;
import com.itl_energy.webclient.itl.model.Sensor;
import com.itl_energy.webclient.itl.util.ApiException;

/**
 * User interface for browsing and maintaining ITL metering database.
 *
 * @author Bruce Stephen
 * @date 18th July 2013
 * @date 24th September 2013
 */
public class AdminControlPanel extends JFrame implements ActionListener, WindowListener, MouseListener, Runnable {

    private static final long serialVersionUID = 6380247734052061093L;

    protected AuthenticationDialog gate;
    protected JList days;
    protected JTree households;
    protected LoadProfileDisplayControl loadprofile;
    protected JSplitPane horizontal;
    protected JSplitPane vertical;

    protected JMenuItem addSite;
    protected JMenuItem editSite;
    protected JMenuItem deleteSite;
    protected JMenuItem querySite;

    protected JMenuItem addHouse;
    protected JMenuItem editHouse;
    protected JMenuItem deleteHouse;
    protected JMenuItem queryHouse;

    protected JMenuItem addHub;
    protected JMenuItem editHub;
    protected JMenuItem deleteHub;
    protected JMenuItem queryHub;

    protected JMenuItem addSensor;
    protected JMenuItem editSensor;
    protected JMenuItem deleteSensor;
    protected JMenuItem querySensor;

    protected JMenuItem addDeployedSensor;
    protected JMenuItem editDeployedSensor;
    protected JMenuItem deleteDeployedSensor;
    protected JMenuItem queryDeployedSensor;

    protected JMenuItem selectData;
    protected JMenuItem importData;
    protected JMenuItem exportData;

    protected JPopupMenu sitePopup;
    protected JPopupMenu housePopup;
    protected JPopupMenu hubPopup;
    protected JPopupMenu sensorPopup;
    protected JPopupMenu deployedSensorPopup;
    protected JPopupMenu dataPopup;

    protected ITLClient controller;
    protected TrialContextStore lookup;

    public AdminControlPanel() {
        super("ITL Domestic Metering Database");

        this.controller = new ITLClient("http://itl.itl-energy.com/api");
        
        this.lookup = TrialContextStore.load();

        this.loadprofile = new LoadProfileDisplayControl();

        this.horizontal = new JSplitPane();
        this.vertical = new JSplitPane();

        this.addWindowListener(this);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.createLayout();
        this.createMenus();

        //SwingUtilities.invokeLater(this);
        Thread runner = new Thread(this);

        runner.start();

        this.setSize(800, 700);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        this.setVisible(true);

        this.horizontal.setDividerLocation(0.2);
        this.vertical.setDividerLocation(0.8);
    }

    protected void createLayout() {
        this.households = new JTree();
        this.households.addMouseListener(this);
        this.households.setCellRenderer(new TrialSiteCellRenderer());
        this.households.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        this.initialiseList();

        JScrollPane jsp1 = new JScrollPane(this.households);
        JScrollPane jsp2 = new JScrollPane(this.days);

        jsp1.setBorder(new TitledBorder("Metered Premises"));
        jsp2.setBorder(new TitledBorder("Monitored Days"));

        this.loadprofile.setBorder(new BevelBorder(BevelBorder.LOWERED));

        this.horizontal.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.vertical.setOrientation(JSplitPane.VERTICAL_SPLIT);

        this.horizontal.setLeftComponent(jsp1);
        this.horizontal.setRightComponent(this.vertical);

        this.vertical.setTopComponent(this.loadprofile);
        this.vertical.setBottomComponent(jsp2);

        this.getContentPane().add(this.horizontal);

        this.loadprofile.addData(null);
        this.loadprofile.repaint();
    }

    private void initialiseList() {
        this.days = new JList();
        this.days.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.days.setLayoutOrientation(JList.VERTICAL);
        this.days.setVisibleRowCount(-1);

        DefaultListModel listModel = new DefaultListModel();

        this.days.setModel(listModel);

        listModel.addElement("01-Jan-2013");
        listModel.addElement("02-Jan-2013");
        listModel.addElement("03-Jan-2013");
        listModel.addElement("04-Jan-2013");
        listModel.addElement("05-Jan-2013");
        listModel.addElement("06-Jan-2013");
        listModel.addElement("07-Jan-2013");
        listModel.addElement("08-Jan-2013");
    }

    protected void createMenus() {
        this.sitePopup = new JPopupMenu();
        this.housePopup = new JPopupMenu();
        this.hubPopup = new JPopupMenu();
        this.sensorPopup = new JPopupMenu();
        this.deployedSensorPopup = new JPopupMenu();
        this.dataPopup = new JPopupMenu();

        this.addSite = new JMenuItem("Add");
        this.editSite = new JMenuItem("Edit");
        this.deleteSite = new JMenuItem("Delete");
        this.querySite = new JMenuItem("Properties");

        this.addSite.addActionListener(this);

        this.addHouse = new JMenuItem("Add");
        this.editHouse = new JMenuItem("Edit");
        this.deleteHouse = new JMenuItem("Delete");
        this.queryHouse = new JMenuItem("Properties");

        this.addHouse.addActionListener(this);

        this.addHub = new JMenuItem("Add");
        this.editHub = new JMenuItem("Edit");
        this.deleteHub = new JMenuItem("Delete");
        this.queryHub = new JMenuItem("Properties");

        this.addHub.addActionListener(this);

        this.addSensor = new JMenuItem("Add");
        this.editSensor = new JMenuItem("Edit");
        this.deleteSensor = new JMenuItem("Delete");
        this.querySensor = new JMenuItem("Properties");

        this.addSensor.addActionListener(this);

        this.addDeployedSensor = new JMenuItem("Add");
        this.editDeployedSensor = new JMenuItem("Edit");
        this.deleteDeployedSensor = new JMenuItem("Delete");
        this.queryDeployedSensor = new JMenuItem("Properties");

        this.addDeployedSensor.addActionListener(this);

        this.selectData = new JMenuItem("Select data by date");
        this.importData = new JMenuItem("Import data from legacy system");
        this.exportData = new JMenuItem("Export data to file");

        this.selectData.addActionListener(this);
        this.importData.addActionListener(this);
        this.exportData.addActionListener(this);

        this.sitePopup.add(this.addSite);
        this.sitePopup.add(this.editSite);
        this.sitePopup.add(this.deleteSite);
        this.sitePopup.addSeparator();
        this.sitePopup.add(this.querySite);

        this.housePopup.add(this.addSite);
        this.housePopup.add(this.editSite);
        this.housePopup.add(this.deleteSite);
        this.housePopup.addSeparator();
        this.housePopup.add(this.querySite);

        this.hubPopup.add(this.addSite);
        this.hubPopup.add(this.editSite);
        this.hubPopup.add(this.deleteSite);
        this.hubPopup.addSeparator();
        this.hubPopup.add(this.querySite);

        this.sensorPopup.add(this.addSite);
        this.sensorPopup.add(this.editSite);
        this.sensorPopup.add(this.deleteSite);
        this.sensorPopup.addSeparator();
        this.sensorPopup.add(this.querySite);

        this.deployedSensorPopup.add(this.addDeployedSensor);
        this.deployedSensorPopup.add(this.editDeployedSensor);
        this.deployedSensorPopup.add(this.deleteDeployedSensor);
        this.deployedSensorPopup.addSeparator();
        this.deployedSensorPopup.add(this.queryDeployedSensor);

        this.dataPopup.add(this.selectData);
        this.dataPopup.add(this.importData);
        this.dataPopup.add(this.exportData);
    }

    @Override
    public void run() {
        //Show authentication dialog
        //Once authenticated, start renew thread
        try {
            int attempt=0;
            
            this.gate = new AuthenticationDialog(this);

            this.gate.promptForCredentials();

            while (this.controller.beginSession(this.gate.getUsername(), this.gate.getPassword()) == false) {
                JOptionPane.showMessageDialog(this, "The username and/or password are not recognised", "Login Failed", JOptionPane.ERROR_MESSAGE);
                
                if(attempt<3)
                    this.gate.promptForCredentials();
                else
                    {
                    JOptionPane.showMessageDialog(this, "Too many login attempts.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                    }
            }

            DefaultMutableTreeNode top = new DefaultMutableTreeNode("Deployment Sites");
            TrialSiteObject tsob = new TrialSiteObject();

            tsob.setDisplayName("Deployment Sites");
            tsob.setTypeID(TrialSiteObject.ID_SITES_GROUP);

            top.setUserObject(tsob);

            List<DeploymentSite> dep = this.controller.getDeploymentSites();

            for (int i = 0; i < dep.size(); i++) {
                DefaultMutableTreeNode site = new DefaultMutableTreeNode(dep.get(i).getName());
                DefaultMutableTreeNode ds = new DefaultMutableTreeNode("Deployed Sensors");
                DefaultMutableTreeNode mp = new DefaultMutableTreeNode("Metered Premises");

                TrialSiteObject tsob1 = new TrialSiteObject();
                TrialSiteObject tsob2 = new TrialSiteObject();
                TrialSiteObject tsob3 = new TrialSiteObject();

                tsob1.setDisplayName("Deployed Sensors");
                tsob1.setTypeID(TrialSiteObject.ID_DEPLOYED_SENSORS);

                tsob2.setDisplayName("Metered Premises");
                tsob2.setTypeID(TrialSiteObject.ID_PREMISES_ON_SITE);

                tsob3.setDisplayName(dep.get(i).getName());
                tsob3.setTypeID(TrialSiteObject.ID_SITE);

                tsob1.addToPath(dep.get(i).getSiteid());

                ds.setUserObject(tsob1);
                mp.setUserObject(tsob2);

                site.setUserObject(tsob3);

                //TODO: what's going on here?
                List<DeployedSensor> depsens = this.controller.getDeployedSensors();

                for (DeployedSensor depsen : depsens) {
                    DefaultMutableTreeNode dsense = new DefaultMutableTreeNode(depsen.getDescription());
                    TrialSiteObject stsob = new TrialSiteObject();
                    stsob.setDisplayName(depsen.getDescription());
                    stsob.setTypeID(TrialSiteObject.ID_DEPLOYED_SENSOR);
                    stsob.addToPath(dep.get(i).getSiteid());
                    stsob.addToPath(depsen.getTypeid());
                    dsense.setUserObject(stsob);
                    ds.add(dsense);
                }

                site.add(ds);

                List<MeteredPremises> mpremise = this.controller.getMeteredPremisesForSite(dep.get(i));

                for (int j = 0; j < mpremise.size(); j++) {
                    DefaultMutableTreeNode dhouse = new DefaultMutableTreeNode(mpremise.get(j).getHouseId());
                    TrialSiteObject htsob = new TrialSiteObject();

                    htsob.setDisplayName(Integer.toOctalString(mpremise.get(j).getHouseId()));
                    htsob.setTypeID(TrialSiteObject.ID_PREMISES);
                    htsob.addToPath(dep.get(i).getSiteid());

                    dhouse.setUserObject(htsob);

                    List<Hub> hubs = this.controller.getHubsForMeteredPremises(mpremise.get(j));

                    for (int k = 0; k < hubs.size(); k++) {
                        DefaultMutableTreeNode hb = new DefaultMutableTreeNode("Hub #" + hubs.get(k).getHubId());
                        List<Sensor> sense = this.controller.getSensorsForHub(hubs.get(k).getHubId());
                        TrialSiteObject husob = new TrialSiteObject();

                        husob.setDisplayName("Hub #" + hubs.get(k).getHubId());
                        husob.setTypeID(TrialSiteObject.ID_HUBS_IN_PREMISES);

                        hb.setUserObject(husob);

                        for (Sensor sense1 : sense) {
                            DefaultMutableTreeNode sns = new DefaultMutableTreeNode(sense1.getDescription());
                            TrialSiteObject ssob = new TrialSiteObject();
                            ssob.setDisplayName(sense1.getDescription());
                            ssob.setTypeID(TrialSiteObject.ID_SENSOR);
                            ssob.addToPath(dep.get(i).getSiteid());
                            ssob.addToPath(mpremise.get(j).getHouseId());
                            ssob.addToPath(hubs.get(k).getHubId());
                            ssob.addToPath(sense1.getSensorId());
                            sns.setUserObject(ssob);
                            hb.add(sns);
                        }

                        dhouse.add(hb);
                    }

                    mp.add(dhouse);
                }

                site.add(mp);

                top.add(site);
            }

            DefaultTreeModel dtm = new DefaultTreeModel(top);

            this.households.setModel(dtm);
        }
        catch (ApiException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to API: " + ex.getMessage());
        }
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        this.lookup.unload();
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == this.addSite) {
                JOptionPane.showMessageDialog(this, "Add Site!");
            }
            else if (ae.getSource() == this.addHouse) {
                JOptionPane.showMessageDialog(this, "Add House!");
            }
            else if (ae.getSource() == this.addHub) {
                JOptionPane.showMessageDialog(this, "Add Hub!");
            }
            else if (ae.getSource() == this.addSensor) {
                JOptionPane.showMessageDialog(this, "Add Sensor!");
            }
            else if (ae.getSource() == this.selectData) {
                //TODO send this out on another thread...
                TrialSiteObject tso = (TrialSiteObject) ((DefaultMutableTreeNode) this.households.getSelectionPath().getLastPathComponent()).getUserObject();

                List<Measurement> ms = this.controller.getMeasurementsForSensor(tso.getPath()[3].intValue(), "2013-09-23 00:00:00", "2013-10-15 00:00:00");//TODO - Bad Hack! Choose date properly!
                DateFormat df = new SimpleDateFormat("YYYY-mm-DD HH:MM:SS");
                double[][] dat = new double[ms.size()][2];

                try {
                    for (int i = 0; i < ms.size(); i++) {
                        Measurement m = ms.get(i);

                        dat[i][0] = df.parse(m.getObservationTime()).getTime();
                        dat[i][1] = m.getObservation();
                        //System.out.println(m.getObservationTime()+"->"+m.getObservation());
                    }
                }
                catch (Exception e) {

                }

                this.loadprofile.resetControl();

                this.loadprofile.setAsTimestampedSeries(true);

                this.loadprofile.setYAxisLabel("Units?");
                this.loadprofile.setXAxisLabel(tso.getDisplayText() + " Measurements");

                this.loadprofile.addData(dat);
                this.loadprofile.repaint();

                //JOptionPane.showMessageDialog(this,"Select Data:"+tso.toString());
            }
            else if (ae.getSource() == this.importData) {

            }
            else if (ae.getSource() == this.exportData) {

            }
        }
        catch (ApiException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to API: " + ex.getMessage());
        }
    }

    protected class AuthenticationRefresh implements Runnable {

        public void run() {

        }
    }

    protected class RequestService implements Runnable {

        public void run() {

        }
    }

    public static void main(String[] args) {
        
        
        AdminControlPanel acp = new AdminControlPanel();
        
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int row = this.households.getClosestRowForLocation(e.getX(), e.getY());

            this.households.setSelectionRow(row);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.households.getSelectionPath().getLastPathComponent();

            int type = ((TrialSiteObject) node.getUserObject()).getTypeID();
            JPopupMenu popup = null;

            switch (type) {
                case TrialSiteObject.ID_SITES_GROUP: {
                    popup = this.sitePopup;

                    break;
                }
                case TrialSiteObject.ID_PREMISES_ON_SITE: {
                    popup = this.housePopup;

                    break;
                }
                case TrialSiteObject.ID_HUBS_IN_PREMISES: {
                    popup = this.deployedSensorPopup;

                    break;
                }
                case TrialSiteObject.ID_SENSORS_ON_HUB:
                case TrialSiteObject.ID_DEPLOYED_SENSORS:
                case TrialSiteObject.ID_SITE:
                case TrialSiteObject.ID_PREMISES:
                case TrialSiteObject.ID_HUB: {
                    popup = this.sensorPopup;

                    break;
                }
                case TrialSiteObject.ID_DEPLOYED_SENSOR: {
                    break;
                }
                case TrialSiteObject.ID_SENSOR: {
                    popup = this.dataPopup;

                    break;
                }
                default: {
                    popup = new JPopupMenu();

                    break;
                }
            }

            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    protected class TrialSiteObject {

        public static final int ID_SITES_GROUP = 0;
        public static final int ID_PREMISES_ON_SITE = 1;
        public static final int ID_HUBS_IN_PREMISES = 2;
        public static final int ID_SENSORS_ON_HUB = 3;
        public static final int ID_DEPLOYED_SENSORS = 4;
        public static final int ID_SITE = 5;
        public static final int ID_PREMISES = 6;
        public static final int ID_HUB = 7;
        public static final int ID_DEPLOYED_SENSOR = 8;
        public static final int ID_SENSOR = 9;

        protected int typeid;
        protected String display;
        protected List<Integer> path;

        public TrialSiteObject() {
            this.path = new ArrayList<Integer>();
        }

        public void addToPath(int p) {
            this.path.add(p);
        }

        public void setDisplayName(String name) {
            this.display = name;
        }

        public void setTypeID(int id) {
            this.typeid = id;
        }

        public String getDisplayText() {
            return this.display;
        }

        public int getTypeID() {
            return this.typeid;
        }

        public Integer[] getPath() {
            Integer[] array = new Integer[this.path.size()];

            for (int i = 0; i < array.length; i++) {
                array[i] = this.path.get(i);
            }

            return array;
        }

        public String toString() {
            return this.display + ":" + this.typeid;
        }
    }

    protected class TrialSiteCellRenderer extends JLabel implements TreeCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 3302116939575365998L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            if (value != null && value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();

                if (userObject instanceof TrialSiteObject) {
                    TrialSiteObject tso = (TrialSiteObject) userObject;

                    this.setText(tso.getDisplayText());

                    if (tso.getTypeID() == TrialSiteObject.ID_SENSOR) {
                        //JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

                        if (selected && leaf) {
                            this.setForeground(Color.RED);
                            this.setBackground(Color.BLACK);
                        }
                        else {
                            this.setForeground(Color.BLACK);
                            this.setBackground(Color.RED);
                        }
                    }
                }
            }

            return this;
        }
    }
}
