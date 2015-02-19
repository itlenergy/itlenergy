package com.itl_energy.webclient.itl.model;

import java.util.Collection;
import java.util.Date;

/**
 * Class representation of a tariff - a tariff can encode pricing structures
 * of arbitrary complexity using instances of the TariffBlock class.
 * 
 * @author Bruce Stephen
 * @version 10th October 2014
 * @version 16th January 2015
 */
public class Tariff
    {
    private Integer tariffId;
    private String tariffName;
    private String priceUnit;
    private Date issueDate;
    private Collection<TariffBlock> tariffBlockCollection;

    public Tariff(Integer tariffId, String tariffName, String priceUnit, Date issueDate) {
        this.tariffId = tariffId;
        this.tariffName = tariffName;
        this.priceUnit = priceUnit;
        this.issueDate = issueDate;
    }

    public Tariff() {
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Collection<TariffBlock> getTariffBlockCollection() {
        return tariffBlockCollection;
    }

    public void setTariffBlockCollection(Collection<TariffBlock> tariffBlockCollection) {
        this.tariffBlockCollection = tariffBlockCollection;
    }
   
    }
