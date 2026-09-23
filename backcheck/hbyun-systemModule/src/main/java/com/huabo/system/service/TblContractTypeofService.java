package com.huabo.system.service;



import java.util.Map;

import com.huabo.system.entity.TblContractTypeof;

public interface TblContractTypeofService {


	Map<String, Object> findPageInfoList(Integer pageNumber, Integer pageSize, String choiceTypeName, String token, String staffId);

	TblContractTypeof findByid(String typeId);

	void updateContractTypeof(TblContractTypeof typeof);

}
