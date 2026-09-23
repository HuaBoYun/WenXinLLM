package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.HashMap;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemSheetTable;

public interface YMFormDataService {

	HashMap<String, Object> setYmFormData(TblSystemSheetTable sheet, BigDecimal fromId, TblStaffUtil loginStaff) throws Exception;

	
	HashMap<String, Object> setYmFormDataMessage(TblSystemSheetTable sheet, BigDecimal fromId) throws Exception;


	BigDecimal selectContractTypeIdByContracrtId(BigDecimal fromId) throws Exception;

}
