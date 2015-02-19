package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Block of a tariff for ToU or realtime pricing applications.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class TariffBlock
    {
    private Integer tariffBlockId;
    private Date startTime;
    private Date stopTime;
    private float unitPrice;
    private Integer tariffId;

    public TariffBlock(Integer tariffBlockId, Date startTime, Date stopTime, float unitPrice, Integer tariffId) {
        this.tariffBlockId = tariffBlockId;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.unitPrice = unitPrice;
        this.tariffId = tariffId;
    }

    public TariffBlock() {
    }

    public Integer getTariffBlockId() {
        return tariffBlockId;
    }

    public void setTariffBlockId(Integer tariffBlockId) {
        this.tariffBlockId = tariffBlockId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }
    
    
    }
