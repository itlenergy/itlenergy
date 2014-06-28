package com.itl_energy.webclient.itl.model;

/**
 * Weather record container class.
 *
 * @author Bruce Stephen
 * @version 22/05/2013
 * @version 10/07/2013
 */
public class Status {

    protected int statusId;
    protected String statusDescription;

    public Status() {
        super();
        this.statusId = -1;
        this.statusDescription = "";
    }

    public Status(int statusId, String statusDescription) {
        super();
        this.statusId = statusId;
        this.statusDescription = statusDescription;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
