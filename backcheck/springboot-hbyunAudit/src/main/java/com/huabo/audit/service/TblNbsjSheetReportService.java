package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;

public interface TblNbsjSheetReportService extends IService<TblNbsjSheetReportEntity>{
	List<TblNbsjSheetReportEntity> findReportListBySheet(String string) throws Exception;
	
//	public PageBean findByProjectId(TblNbsjSheetReportEntity sheetReport,BigDecimal projectId, Integer pageNumber,int pageSize,TblNbsjSheetEntity sheet);

	void delete(BigDecimal reportid);
}
