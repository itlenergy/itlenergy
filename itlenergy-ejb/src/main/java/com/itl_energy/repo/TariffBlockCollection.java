package com.itl_energy.repo;

import javax.ejb.Stateless;
import com.itl_energy.model.TariffBlock;
import java.util.Date;
import java.util.List;

/**
 * Provides access to individual premises energy tariff rates.
 * 
 * @author Bruce Stephen
 * @date 14th October 2014
 * @date 5th December 2014
 */

@Stateless
public class TariffBlockCollection extends CollectionBase<TariffBlock> {

	public TariffBlockCollection() {
		super(TariffBlock.class);
	}
        
        public List<TariffBlock> findAll() {
        return super.createNamedQuery("findAll")
                .getResultList();
        }
        
        public List<TariffBlock> findByTariffBlockId(Integer tariffBlockId) {
        return super.createNamedQuery("findByTariffBlockId")
                .setParameter("tariffBlockId", tariffBlockId)
                .getResultList();
        }
    
        public List<TariffBlock> findByStartTime(Date startTime) {
            return super.createNamedQuery("findByStartTime")
                    .setParameter("startTime", startTime)
                    .getResultList();
        }
    
        public List<TariffBlock> findByStopTime(Date stopTime) {
            return super.createNamedQuery("findByStopTime")
                    .setParameter("stopTime", stopTime)
                    .getResultList();
        }
        
        public List<TariffBlock> findByUnitPrice(Float unitPrice) {
            return super.createNamedQuery("findByUnitPrice")
                    .setParameter("unitPrice", unitPrice)
                    .getResultList();
        }
        
	@Override
	public int getId(TariffBlock entity) {
		return entity.getTariffBlockId();
	}
}