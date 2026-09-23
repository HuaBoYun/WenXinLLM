package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.contract.entity.TbllegalAttorney;

public interface TbllegalAttorneyService {
		
	List<TbllegalAttorney> getlegalAttorneyByDisputeid(BigDecimal disputeid);
	
	void addlegalAttorney(TbllegalAttorney tla);
	
	TbllegalAttorney getlegalAttorneyById(BigDecimal id);
	
	void deletelegalAttorneyById(BigDecimal id);

	List<TbllegalAttorney> getlegalAttorneyByArbitrationid(BigDecimal arbitrationid);

	List<TbllegalAttorney> getlegalAttorneyByLawsuitid(BigDecimal lawsuitid);

	List<TbllegalAttorney> getlegalAttorneyByNegotiationid(BigDecimal negotiationid);
}
