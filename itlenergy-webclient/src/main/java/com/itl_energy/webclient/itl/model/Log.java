package com.itl_energy.webclient.itl.model;

/**
 * Log record container class.
 *
 * @author Bruce Stephen
 * @version 21/05/2013
 * @version 10/07/2013
 */
public class Log {

    protected int hubLogId;
    protected int hubId;
    protected String hubLogMessage;
    protected String hubLogTime;
    protected int hubLogCode;

    public Log(int hubLogId, int hubId, String hubLogMessage, String hubLogTime, int hubLogCode) {
        super();
        this.hubLogId = hubLogId;
        this.hubId = hubId;
        this.hubLogMessage = hubLogMessage;
        this.hubLogTime = hubLogTime;
        this.hubLogCode = hubLogCode;
    }

    public int getHubLogId() {
        return hubLogId;
    }

    public void setHubLogId(int hubLogId) {
        this.hubLogId = hubLogId;
    }

    public int getHubId() {
        return hubId;
    }

    public void setHubId(int hubId) {
        this.hubId = hubId;
    }

    public String getHubLogMessage() {
        return hubLogMessage;
    }

    public void setHubLogMessage(String hubLogMessage) {
        this.hubLogMessage = hubLogMessage;
    }

    public String getHubLogTime() {
        return hubLogTime;
    }

    public void setHubLogTime(String hubLogTime) {
        this.hubLogTime = hubLogTime;
    }

    public int getHubLogCode() {
        return hubLogCode;
    }

    public void setHubLogCode(int hubLogCode) {
        this.hubLogCode = hubLogCode;
    }
}
