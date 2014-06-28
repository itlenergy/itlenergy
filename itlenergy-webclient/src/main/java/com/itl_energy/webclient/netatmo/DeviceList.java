/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itl_energy.webclient.netatmo;

/**
 *
 * @author stewart
 */
public class DeviceList {
    private NetatmoModule[] modules;
    private NetatmoDevice[] devices;
    
    public NetatmoModule[] getModules() {
        return modules;
    }
    
    public NetatmoDevice[] getDevices() {
        return devices;
    }
}
