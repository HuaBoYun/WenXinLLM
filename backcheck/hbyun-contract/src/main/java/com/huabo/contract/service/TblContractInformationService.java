package com.huabo.contract.service;


import java.math.BigDecimal;
import java.util.Map;

import com.huabo.contract.entity.TblContractInformation;

public interface TblContractInformationService {


	Map<String, Object> saveContractInfoMation(BigDecimal contractid,TblContractInformation information);

	Map<String, Object> removeContractInfoMation(BigDecimal infoId);

    Map<String, Object> findInformationListById(BigDecimal contractId);

}
