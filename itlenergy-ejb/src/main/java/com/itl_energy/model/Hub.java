package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the hub database table. An ITL Hub is the access point
 * for sensing and actuation in a given premises.
 * 
 * @author Bruce Stephen
 * @date 18th August 2014
 */

@Entity
@Table(name = "hub")
@NamedQueries({
    @NamedQuery(name = "Hub.findAll", query = "SELECT h FROM Hub h"),
    @NamedQuery(name = "Hub.findByHubId", query = "SELECT h FROM Hub h WHERE h.hubId = :hubId"),
    @NamedQuery(name = "Hub.findByLastUpdate", query = "SELECT h FROM Hub h WHERE h.lastUpdate = :lastUpdate"),
    @NamedQuery(name="Hub.findByHouseId", query="SELECT h FROM Hub h WHERE h.houseId = :houseId"),
    @NamedQuery(name = "Hub.findByFreeStorage", query = "SELECT h FROM Hub h WHERE h.freeStorage = :freeStorage")})
@XmlRootElement
public class Hub implements Serializable {

    private static final long serialVersionUID = 1L;
        private Integer hubId;
    @NotNull
    private Date lastUpdate;
    @NotNull
    private Integer freeStorage;
        private Collection<Sensor> sensorCollection;
        private Collection<ForecastStatus> forecastStatusCollection;
        private Integer houseId;

    public Hub() {
    }

    public Hub(Integer hubId) {
        this.hubId = hubId;
    }

    public Hub(Integer hubId, Date lastUpdate, int freeStorage) {
        this.hubId = hubId;
        this.lastUpdate = lastUpdate;
        this.freeStorage = freeStorage;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hub_id")
    public Integer getHubId() {
        return this.hubId;
    }

    public void setHubId(Integer hubId) {
        this.hubId = hubId;
    }

    @Basic(optional = false)
    @Column(name = "free_storage")
    public Integer getFreeStorage() {
        return this.freeStorage;
    }

    public void setFreeStorage(Integer freeStorage) {
        this.freeStorage = freeStorage;
    }

    @JoinColumn(name = "house_id", referencedColumnName = "house_id")
    @ManyToOne(optional = false)
    @Column(name = "house_id")
    public Integer getHouseId() {
        return this.houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    @Basic(optional = false)
    @Column(name = "last_update")
    @Temporal(TemporalType.DATE)
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hubId")
    @XmlTransient
    public Collection<Sensor> getSensorCollection() {
        return sensorCollection;
    }

    public void setSensorCollection(Collection<Sensor> sensorCollection) {
        this.sensorCollection = sensorCollection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hubId")
    @XmlTransient
    public Collection<ForecastStatus> getForecastStatusCollection() {
        return forecastStatusCollection;
    }

    public void setForecastStatusCollection(Collection<ForecastStatus> forecastStatusCollection) {
        this.forecastStatusCollection = forecastStatusCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hubId != null ? hubId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hub)) {
            return false;
        }
        Hub other = (Hub) object;
        if ((this.hubId == null && other.hubId != null) || (this.hubId != null && !this.hubId.equals(other.hubId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Hub[ hubId=" + hubId + " ]";
    }
}