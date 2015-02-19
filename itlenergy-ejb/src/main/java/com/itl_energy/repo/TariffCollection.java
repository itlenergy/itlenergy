package com.itl_energy.repo;

import javax.ejb.Stateless;
import java.util.List;
import com.itl_energy.model.Tariff;

/**
 * Provides access to individual premises energy tariffs.
 * 
 * @author Bruce Stephen
 * @date 19th August 2014
 */
@Stateless
public class TariffCollection extends CollectionBase<Tariff> {

	public TariffCollection() {
		super(Tariff.class);
	}
        
        public List<Tariff> findByHouseId(Integer houseId) {
        return super.createNamedQuery("findByHouseId")
                .setParameter("houseId", houseId)
                .getResultList();
    }
	@Override
	public int getId(Tariff entity) {
		return entity.getTariffId();
	}
}
