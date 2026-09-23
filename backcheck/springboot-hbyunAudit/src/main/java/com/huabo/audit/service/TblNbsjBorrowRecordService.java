package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;

public interface TblNbsjBorrowRecordService extends IService<TblNbsjBorrowRecordEntity>{
//	public PageBean findBorrowCountByprojectid(String projectid,Integer pageNumber, int pageSize);
	
	//申请借阅
	JsonBean tjspBorrow(TblNbsjBorrowRecordEntity br, String token,String borrowDate,String backDate)throws Exception;
	
	//借阅日志-借阅次数列表
	JsonBean jyrzCountPageList(String token, Integer pageNumber, Integer pageSize,BigDecimal projectid) throws Exception;

	JsonBean submitRecordApproval(String token, BigDecimal borrowid) throws Exception ;

	JsonBean getRecordApprovalInfo(String token, BigDecimal borrowid, String taskId, BigDecimal cyId) throws Exception ;

	JsonBean dealRecordApporvalInfo(String token, BigDecimal cyId, String taskId, String transition, String optDesc,
			String borrowid) throws Exception ;

	JsonBean tjspBorrowDetail(BigDecimal borrowId,String token) throws Exception;
	
}
