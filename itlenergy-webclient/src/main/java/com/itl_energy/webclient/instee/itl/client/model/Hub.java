package com.itl_energy.webclient.instee.itl.client.model;

import java.util.Date;

/**
 * Hub container class. This represents the connection node to the premises. May
 * be polled for diagnostic information and may be used in future for actuation
 * purposes.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 29/05/2013
 * @version 10/07/2013
 */
public class Hub {

    protected int hubId;
    protected int houseId;
    protected Date lastUpdate;
    protected int freeStorage;

    public Hub() {

    }

    public Hub(int hubId, int houseId, Date lastUpdate, int freeStorage) {
        super();
        this.hubId = hubId;
        this.houseId = houseId;
        this.lastUpdate = lastUpdate;
        this.freeStorage = freeStorage;
    }

    public int getHubId() {
        return hubId;
    }

    public void setHubId(int hubId) {
        this.hubId = hubId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getFreeStorage() {
        return freeStorage;
    }

    public void setFreeStorage(int freeStorage) {
        this.freeStorage = freeStorage;
    }
}
