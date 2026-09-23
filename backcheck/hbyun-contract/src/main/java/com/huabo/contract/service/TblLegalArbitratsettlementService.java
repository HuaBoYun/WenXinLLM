package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblLegalArbitratsettlement;

public interface TblLegalArbitratsettlementService {

	TblCyhwUnit findContractByDisputeId(BigDecimal disputeId) throws Exception;

	TblLegalArbitratsettlement findById(BigDecimal arbitraId) throws Exception;

	void findListByPageInfo(PageInfo<TblLegalArbitratsettlement> pageInfo, TblLegalArbitratsettlement negotia,
			BigDecimal disputeid) throws Exception;

	void addDiputregistration(TblLegalArbitratsettlement arbitrat) throws Exception;

	void modifyNegotiatedSettlement(TblLegalArbitratsettlement arbitrat) throws Exception;

	void removeLegalNegotiatedSettlemen(BigDecimal arbitraid) throws Exception;
	
	
}
