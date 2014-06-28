package com.itl_energy.webclient.itl.model;

/**
 * Container class for a single dwelling. Includes building information for
 * contextualising load profiles.
 *
 * @author Bruce Stephen
 * @version 22/05/2013
 * @version 10/07/2013
 * @version 07/08/2013
 */
public class MeteredPremises {

    protected int houseId;
    protected int siteId;
    protected int rooms;
    protected int floors;
    protected int occupants;
    protected boolean centralHeatingGas;
    protected boolean cookingGas;

    public MeteredPremises() {
        this.houseId = -1;
        this.siteId = -1;
        this.rooms = 0;
        this.floors = 0;
        this.occupants = 0;
        this.centralHeatingGas = false;
        this.cookingGas = false;
    }

    public MeteredPremises(int houseId, int siteId, int rooms, int floors, int occupants, boolean centralHeatingGas, boolean cookingGas) {
        super();
        this.houseId = houseId;
        this.siteId = siteId;
        this.rooms = rooms;
        this.floors = floors;
        this.occupants = occupants;
        this.centralHeatingGas = centralHeatingGas;
        this.cookingGas = cookingGas;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getOccupants() {
        return occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public boolean isCentralHeatingGas() {
        return centralHeatingGas;
    }

    public void setCentralHeatingGas(boolean centralHeatingGas) {
        this.centralHeatingGas = centralHeatingGas;
    }

    public boolean isCookingGas() {
        return cookingGas;
    }

    public void setCookingGas(boolean cookingGas) {
        this.cookingGas = cookingGas;
    }
}
