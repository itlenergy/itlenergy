package com.itl_energy.webclient.alertme;

/**
 * Represents a device from an AlertMe API call result.  The client deserialises
 * the JSON response into this class.
 */
public class AlertMeDevice {
    private String name;
    private String type;
    private String id;
    
    /**
     * Gets the name of the device.
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the type of the device.
     * @return 
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the device's ID.
     * @return 
     */
    public String getID() {
        return id;
    }
}
