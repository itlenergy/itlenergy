package com.itl_energy.webclient.itl.model;

import java.util.Date;

/**
 * Class representing aggregated site generation.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class Generation
    {
    private Integer id;
    private Date observed;
    private float active;
    private float reactive;
    private float phase;
    private Integer siteId;

    public Generation(Integer id, Date observed, float active, float reactive, float phase, Integer siteId) {
        this.id = id;
        this.observed = observed;
        this.active = active;
        this.reactive = reactive;
        this.phase = phase;
        this.siteId = siteId;
    }

    public Generation() {
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

    public float getActive() {
        return active;
    }

    public void setActive(float active) {
        this.active = active;
    }

    public float getReactive() {
        return reactive;
    }

    public void setReactive(float reactive) {
        this.reactive = reactive;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    
    
    }
