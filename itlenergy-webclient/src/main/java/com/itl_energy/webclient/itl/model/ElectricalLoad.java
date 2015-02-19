/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Representation of electrical load for a given site at a particular time.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class ElectricalLoad
    {
    private Integer id;
    private Date observed;
    private float loadkwh;
    private Integer siteId;

    public ElectricalLoad(Integer id, Date observed, float loadkwh, Integer siteId) {
        this.id = id;
        this.observed = observed;
        this.loadkwh = loadkwh;
        this.siteId = siteId;
    }

    public ElectricalLoad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getObserved() {
        return observed;
    }

    public void setObserved(Date observed) {
        this.observed = observed;
    }

    public float getLoadkwh() {
        return loadkwh;
    }

    public void setLoadkwh(float loadkwh) {
        this.loadkwh = loadkwh;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    
    
    }
