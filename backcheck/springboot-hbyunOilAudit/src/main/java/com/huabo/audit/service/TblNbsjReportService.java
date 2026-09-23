package com.huabo.audit.service;

import com.huabo.audit.oracle.entity.TblNbsjReportEntity;

public interface TblNbsjReportService {
	/**
	 * 审计报告保存
	 */
	public void saveTblNbsjReport(TblNbsjReportEntity nbsjReport);
	
	/**
	 * 审计报告修改
	 */
	public void updateTblNbsjReport(TblNbsjReportEntity nbsjReport);
	
	/**
	 * 审计报告删除
	 */
	public void deleteTblNbsjReport(TblNbsjReportEntity nbsjReport);
	
	/**
	 * 审计报告查询
	 */
//	public PageBean findAll(Find find,Integer pageNumber,Integer pageSize);
	
	public TblNbsjReportEntity getTblNbsjReport(Integer reportid);
}
