package com.huabo.audit.service;

import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblReportVo;
import com.huabo.audit.util.R;

public interface TblReportService {
	public void add(TblReportEntity tblReport);
	public List findAll();
	public void detele(String id);
	public TblReportEntity findByid(String id);
	public void modify(TblReportEntity tblReport);
	public List search(String name,String time);
//	public PageBean findAll(Integer pageNumber, Integer pageSize, Find find, String type, Integer orgid);
	/**
	 * 该项目下，是否有此类型的报告
	 * @param projectid
	 * @param reportType
	 * @return
	 */
	public List<TblReportEntity> findReportByProjectId(Integer projectid,String reportType,Integer reportStatus);
	/**
	 * 未审批完成的报告
	 * @param integer
	 * @return
	 */
	public List<TblReportEntity> findNoReportByProjectId(Integer integer);
	
	
	
	//==
	JsonBean zdyPageList(String token, Integer pageNumber, Integer pageSize,TblReportVo tblReportVo,Integer projectId) throws Exception;
	
	JsonBean zdyAdd(TblReportEntity report, String token,String attids)throws Exception;
    
    JsonBean zdyDelete(Integer reportid, String token) throws Exception;
    
    JsonBean findZdyReportDetail(String token, Integer reportid) throws Exception;

	/**
	 * 报告编制-导出
	 */
	TblReportEntity findReportByreportId(String reportid);
	public JsonBean submitReportFhApproval(String token, Integer reportid) throws Exception ;
	public JsonBean submitReportZqyjApproval(String token, Integer reportid) throws Exception ;
	public JsonBean submitReportSpApproval(String token, Integer reportid) throws Exception;
	public JsonBean getReportFhApprovalInfo(String token, Integer reportid, String taskId, Integer cyId) throws Exception ;
	public JsonBean dealReportFhApporvalInfo(String token, Integer cyId, String taskId, String transition,String optDesc, String reportid) throws Exception ;
	
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	
	public JsonBean submitAuditUserApproval(String token, Integer staffid) throws Exception;
	
	public JsonBean dealAuditUserApporvalInfo(String token, String staffid,String taskId,Integer cyId,String transitionName,String examination,String processInstanceId,String processDefinitionId) throws Exception ;
	public JsonBean getAuditUserApprovalInfo(String token, Integer staffid, Integer cyId) throws Exception ;
	
	public JsonBean submitStaffScoreApproval(String token, Integer staffScoreid) throws Exception;
	public JsonBean dealStaffScoreApporvalInfo(String token, Integer cyId, String taskId, String transition,String optDesc, String staffScoreid) throws Exception ;
	public JsonBean getStaffScoreApprovalInfo(String token, Integer staffScoreid, Integer cyId) throws Exception ;
	
	
	Map<String, Object> submitStaffScoreApproval(Integer staffScoreid, String examination, String token)throws Exception ;
	public JsonBean getStaffScoreApprovalView(String token, Integer staffScoreid, String taskId) throws Exception;
	public JsonBean dealStaffScoreApprovalInfo(String token, Integer staffScoreid, String taskId, Integer cyId,
			String transitionName, String examination, String processInstanceId, String processDefinitionId) throws Exception;
	public JsonBean getAuditUserApprovalView(String token, Integer staffid, String taskId) throws Exception;
	
}
