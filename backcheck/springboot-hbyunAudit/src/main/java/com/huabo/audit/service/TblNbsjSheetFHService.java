package com.huabo.audit.service;

import java.math.BigDecimal;

import com.huabo.audit.oracle.entity.TblNbsjPlanFormEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetFHEntity;

public interface TblNbsjSheetFHService {
	public void saveTblNbsjSheetFH(TblNbsjSheetFHEntity n);
    public void saveSheetFh(String option,TblNbsjSheetFHEntity sheetFH,TblNbsjSheetEntity sheet, BigDecimal staffid);
    public TblNbsjPlanFormEntity get(String id);
}
