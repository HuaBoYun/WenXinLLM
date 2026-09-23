package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.vo.TblGkProjectVo;
import com.huabo.audit.oracle.vo.TblGkQuestionVo;
import com.huabo.audit.oracle.vo.TblGkZgQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectRwfpVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectZXYLVo;
import com.huabo.audit.util.R;

/**
* 描述: 审计项目IDService
* @author: ziyao
* @date: 2022-04-12
*/
public interface TblNbsjProjectService {
	
	
	JsonBean getNbsjProjecGdtPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo project,String projectStartDate, String projectEndDate) throws Exception;

	//获取当前用户实施项目
	TblNbsjProject getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception;
	
	JsonBean getNbsjProjectPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo project,
			String projectStartDate, String projectEndDate) throws Exception;

	//==
	JsonBean projectProposalPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	JsonBean findProjectProposalDetail(String token, Integer dataId) throws Exception;

	JsonBean getAuditFileInfoList(String token, String projectName, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean getAuditFileDetailTreeInfo(String token, Integer projectId) throws Exception;

	
	//档案列表
	JsonBean sjgdNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	JsonBean findSjgdNewDetail(String token, Integer projectid) throws Exception;
	
	//档案借阅
	JsonBean dajyNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	//档案日志
	JsonBean jyrzNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	
	//审计分析-审计项目情况表
	JsonBean findProjectItemReport(String token, Integer pageNumber, Integer pageSize,Integer year) throws Exception;
	
	//审计分析-审计类型项目
	JsonBean findAuditTypeCount(String token,Integer year) throws Exception;
	
	//审计分析-年度查询条件
	JsonBean findReportYearList(String token) throws Exception;
	
	//审计分析-各公司审计项目数
	JsonBean findNbsjProjectCountByCompanyId(String token,Integer year) throws Exception;
		
	
	//审计项目管理-项目管理
	JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	JsonBean projectAdd(TblNbsjProject pj, String token,String planStartDate,String planEndDate,String attids,String pd_dx,String pjTeamJson)throws Exception;
    
    JsonBean projectDelete(Integer projectid, String token) throws Exception;
    
    JsonBean findProjectDetail(String token, Integer projectid) throws Exception;
    
    //项目任务分配
    JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception;
    
    
    //项目执行一览
    JsonBean projectZxylPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;

    
    JsonBean findSSProjectDetail(String token,Integer projectId) throws Exception;
    
    //==项目实施
    JsonBean projectSS(String token,Integer projectid) throws Exception;
    
    //==
    TblNbsjProject getSSProjectDetail(String token) throws Exception;
    
    //==
    TblNbsjProject getProjectById(Integer projectid) throws Exception;

    //==修改项目经理
    JsonBean pjPmModi(String token,BigDecimal pmId,Integer projectid)throws Exception;
    
    //==启动
    JsonBean pjStart(String token,Integer projectid)throws Exception;

	JsonBean submitProjectArrpoval(String token, Integer projectId) throws Exception ;

	JsonBean getProjectApprovalInfo(String token, Integer projectId, String taskId, Integer cyId,String v) throws Exception ;

	JsonBean dealProjectApporvalInfo(String token, Integer cyId, String taskId, String transition, String optDesc,
			String projectId,String processDefinitionId,String processInstanceId) throws Exception ;
	
	//==选择人员保存
	JsonBean jsfpRoleManageSave(String token, String ids, Integer teamId, Integer projectId) throws Exception ;

	//==前期审计资料--导入资料列表
	JsonBean auditPlanListPlanIdIn(String token, Integer pageNumber, Integer pageSize,String projectname)throws Exception;

	//==前期审计资料--导入资料选择保存
	JsonBean auditPlanInAdd(String ids, String token) throws Exception;

	public JsonBean getGkProjectInfo(String token, Integer pageNumber, Integer pageSize,TblGkProjectVo project) throws Exception ;
	
	public JsonBean getGkQuestionInfo(String token, Integer pageNumber, Integer pageSize,TblGkQuestionVo question) throws Exception ;
	
	public JsonBean getGkZgContentInfo(String token, Integer pageNumber, Integer pageSize,TblGkZgQuestionVo question) throws Exception ;
	
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	//==
	JsonBean getTempeleBizCnt(String token,Integer templeteId) throws Exception;
	
	public JsonBean getUserAuditItem(String token,BigDecimal staffId) throws Exception ;

	JsonBean getProjectPlanList(String token, Integer projectId, Integer planId) throws Exception ;

	
	//整改跟踪-项目列表
	JsonBean getZgprojectPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
    Map<String, Object> viewOppsiteActiviti(Integer planId,String businessKey)throws Exception;

	
	JsonBean selectSjsQuestList(Date startdate,Date enddate,String projectid,String token)throws Exception ;
	
	
	public List<Object[]> getGkProjectInfoExport(String token,TblGkProjectVo project) throws Exception;
	
	public List<Object[]> getGkQuestionInfoExport(String token,TblGkQuestionVo question) throws Exception;

	public List<Object[]>  getGkZgContentInfoExport(String token,TblGkZgQuestionVo question) throws Exception;
	
	public JsonBean getprojectLisbystaffidt(String token,  TblnbsjProjectVo project) throws Exception;

}
