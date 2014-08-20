package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the house database table. This should actually be
 * renamed to 'MeteredPremises' as it will end up being used for non-residential
 * buildings as well.
 *
 * @author Bruce Stephen
 * @date 18th August 2014
 */
@Entity
@Table(name = "house")
@NamedQueries({
    @NamedQuery(name = "House.findAll", query = "SELECT h FROM House h"),
    @NamedQuery(name = "House.findBySiteId", query = "SELECT h FROM House h WHERE h.siteId = :siteId"),
    @NamedQuery(name = "House.findByHouseId", query = "SELECT h FROM House h WHERE h.houseId = :houseId"),
    @NamedQuery(name = "House.findByRooms", query = "SELECT h FROM House h WHERE h.rooms = :rooms"),
    @NamedQuery(name = "House.findByFloors", query = "SELECT h FROM House h WHERE h.floors = :floors"),
    @NamedQuery(name = "House.findByOccupants", query = "SELECT h FROM House h WHERE h.occupants = :occupants"),
    @NamedQuery(name = "House.findByCentralHeatingGas", query = "SELECT h FROM House h WHERE h.centralHeatingGas = :centralHeatingGas"),
    @NamedQuery(name = "House.findByCookingGas", query = "SELECT h FROM House h WHERE h.cookingGas = :cookingGas"),
    @NamedQuery(name = "House.findByTariffId", query = "SELECT h FROM House h WHERE h.tariffId = :tariffId")})
@XmlRootElement
public class House implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer tariffId;
    private Collection<Hub> hubCollection;
    private Integer houseId;
    private Boolean centralHeatingGas;
    private Boolean cookingGas;
    private Integer floors;
    private Integer occupants;
    private Integer rooms;
    private Integer siteId;

    public House() {
    }

    public House(Integer houseId) {
        this.houseId = houseId;
    }

    public House(Integer houseId, int rooms, int floors, int occupants, boolean centralHeatingGas, boolean cookingGas) {
        this.houseId = houseId;
        this.rooms = rooms;
        this.floors = floors;
        this.occupants = occupants;
        this.centralHeatingGas = centralHeatingGas;
        this.cookingGas = cookingGas;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "house_id")
    public Integer getHouseId() {
        return this.houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    @Basic(optional = false)
    @Column(name = "central_heating_gas")
    public Boolean getCentralHeatingGas() {
        return this.centralHeatingGas;
    }

    public void setCentralHeatingGas(Boolean centralHeatingGas) {
        this.centralHeatingGas = centralHeatingGas;
    }

    @Basic(optional = false)
    @Column(name = "cooking_gas")
    public Boolean getCookingGas() {
        return this.cookingGas;
    }

    public void setCookingGas(Boolean cookingGas) {
        this.cookingGas = cookingGas;
    }

    @Basic(optional = false)
    @Column(name = "floors")
    public Integer getFloors() {
        return this.floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    @Basic(optional = false)
    @Column(name = "occupants")
    public Integer getOccupants() {
        return this.occupants;
    }

    public void setOccupants(Integer occupants) {
        this.occupants = occupants;
    }

    @Basic(optional = false)
    @Column(name = "rooms")
    public Integer getRooms() {
        return this.rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    @ManyToOne(optional = false)
    @Column(name = "site_id")
    public Integer getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    
    @Column(name = "tariff_id")
    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "houseId")
    @XmlTransient
    public Collection<Hub> getHubCollection() {
        return hubCollection;
    }

    public void setHubCollection(Collection<Hub> hubCollection) {
        this.hubCollection = hubCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (houseId != null ? houseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof House)) {
            return false;
        }
        House other = (House) object;
        if ((this.houseId == null && other.houseId != null) || (this.houseId != null && !this.houseId.equals(other.houseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.House[ houseId=" + houseId + " ]";
    }
}