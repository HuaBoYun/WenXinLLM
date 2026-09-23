package com.huabo.system.service;

import java.util.List;

import com.huabo.system.entity.TblNbsjSheetReport;

public interface TblNbsjSheetReportService {
	
    List<TblNbsjSheetReport> findReportListBySheet(String toString);

	void update(TblNbsjSheetReport tnsr);
}
