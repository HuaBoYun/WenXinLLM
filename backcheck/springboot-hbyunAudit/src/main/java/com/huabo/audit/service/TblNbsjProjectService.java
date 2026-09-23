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
	
	JsonBean findProjectProposalDetail(String token, BigDecimal dataId) throws Exception;

	JsonBean getAuditFileInfoList(String token, String projectName, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean getAuditFileDetailTreeInfo(String token, BigDecimal projectId) throws Exception;

	
	//档案列表
	JsonBean sjgdNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	JsonBean findSjgdNewDetail(String token, BigDecimal projectid) throws Exception;
	
	//档案借阅
	JsonBean dajyNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	//档案日志
	JsonBean jyrzNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
	
	//审计分析-审计项目情况表
	JsonBean findProjectItemReport(String token, Integer pageNumber, Integer pageSize,Integer year) throws Exception;
	
	//审计分析-审计类型项目
	JsonBean findAuditTypeCount(String token,Integer year) throws Exception;
	
	//审计分析-查询月份项目数
	JsonBean selectPlanyfcount(String token, Integer queryYear) throws Exception;

	//审计分析-查询项目状态
	JsonBean selectPlanZtcount(String token, Integer queryYear) throws Exception ;


	//审计分析-查询本年度计划项目数
	JsonBean selectPlancount(String token, Integer queryYear) throws Exception;

	//审计分析-查询审计项目详细数据(统一接口,支持按公司/审计类型/状态/月份查询)
	JsonBean selectPlancountDetail(String token, String year, String companyName, String auditType, String status, String month, Integer pageNumber, Integer pageSize) throws Exception;

	// 审计项目类型详细数据查询(已废弃,使用selectPlancountDetail代替)
	@Deprecated
	JsonBean selectAuditTypeDetail(String token, String year, String auditType, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean sjsl(String token,Integer year,Integer pageNumber,Integer pageSize);

	JsonBean zgwt(String token,Integer year,Integer pageNumber,Integer pageSize);

	//审计分析-年度查询条件
	JsonBean findReportYearList(String token) throws Exception;

	//审计分析-各公司审计项目数
	JsonBean findNbsjProjectCountByCompanyId(String token,Integer year) throws Exception;


//	//审计项目管理-项目管理
//	JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
//	JsonBean projectAdd(TblNbsjProject pj, String token,String planStartDate,String planEndDate,String attids,String pd_dx,String pjTeamJson)throws Exception;
    
    JsonBean projectDelete(BigDecimal projectid, String token) throws Exception;
    
    JsonBean findProjectDetail(String token, BigDecimal projectid) throws Exception;
    
    //项目任务分配
    JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception;
    
    
    //项目执行一览
    JsonBean projectZxylPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;

    
    JsonBean findSSProjectDetail(String token,BigDecimal projectId) throws Exception;
    
    //==项目实施
    JsonBean projectSS(String token,BigDecimal projectid) throws Exception;
    
    //==
    TblNbsjProject getSSProjectDetail(String token) throws Exception;
    
    //==
    TblNbsjProject getProjectById(BigDecimal projectid) throws Exception;

    //==修改项目经理
    JsonBean pjPmModi(String token,BigDecimal pmId,BigDecimal projectid)throws Exception;
    
    //==启动
    JsonBean pjStart(String token,BigDecimal projectid)throws Exception;

	JsonBean submitProjectArrpoval(String token, BigDecimal projectId) throws Exception ;

	JsonBean getProjectApprovalInfo(String token, BigDecimal projectId, String taskId, BigDecimal cyId,String v) throws Exception ;

	JsonBean dealProjectApporvalInfo(String token, BigDecimal cyId, String taskId, String transition, String optDesc,
			String projectId,String processDefinitionId,String processInstanceId) throws Exception ;
	
	//==选择人员保存
	JsonBean jsfpRoleManageSave(String token, String ids, BigDecimal teamId, BigDecimal projectId) throws Exception ;

	//==前期审计资料--导入资料列表
	JsonBean auditPlanListPlanIdIn(String token, Integer pageNumber, Integer pageSize,String projectname)throws Exception;

	//==前期审计资料--导入资料选择保存
	JsonBean auditPlanInAdd(String ids, String token) throws Exception;

	public JsonBean getGkProjectInfo(String token, Integer pageNumber, Integer pageSize,TblGkProjectVo project) throws Exception ;
	
	public JsonBean getGkQuestionInfo(String token, Integer pageNumber, Integer pageSize,TblGkQuestionVo question) throws Exception ;
	
	public JsonBean getGkZgContentInfo(String token, Integer pageNumber, Integer pageSize,TblGkZgQuestionVo question) throws Exception ;
	
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	//==
	JsonBean getTempeleBizCnt(String token,BigDecimal templeteId) throws Exception;
	
	public JsonBean getUserAuditItem(String token,BigDecimal staffId) throws Exception ;

	JsonBean getProjectPlanList(String token, BigDecimal projectId, BigDecimal planId) throws Exception ;

	
	//整改跟踪-项目列表
	JsonBean getZgprojectPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
	
    Map<String, Object> viewOppsiteActiviti(BigDecimal planId,String businessKey)throws Exception;

	
	JsonBean selectSjsQuestList(Date startdate,Date enddate,String projectid,String token)throws Exception ;
	
	
	public List<Object[]> getGkProjectInfoExport(String token,TblGkProjectVo project) throws Exception;
	
	public List<Object[]> getGkQuestionInfoExport(String token,TblGkQuestionVo question) throws Exception;

	public List<Object[]>  getGkZgContentInfoExport(String token,TblGkZgQuestionVo question) throws Exception;
	
	public JsonBean getprojectLisbystaffidt(String token,  TblnbsjProjectVo project) throws Exception;
	
	JsonBean projectAdd(TblNbsjProject pj, String token,String planStartDate,String planEndDate,String attids,String pd_dx,String pjTeamJson)throws Exception;
	
	//审计项目管理-项目管理
	JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo tblnbsjProjectVo, String sortFields, String sortFlag) throws Exception;
	
	//项目任务分配
    JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo, String sortFields, String sortFlag) throws Exception;

	/**
	 * 整改问题详细数据查询
	 * @param token 用户token
	 * @param year 年度
	 * @param orgName 主管部门名称
	 * @param projectId 项目ID
	 * @param audittype 审计类型
	 * @param projectsource 审计来源
	 * @param queryType 查询类型
	 * @param pageNumber 页码
	 * @param pageSize 每页数量
	 * @return 整改问题详细数据
	 */
	JsonBean zgwtDetail(String token, Integer year, String orgName, String projectId, String audittype, String projectsource, String queryType, Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 * 整改问题一览表详细数据查询（基于整改清单表）
	 * @param token 用户token
	 * @param year 年度
	 * @param orgName 主管部门名称
	 * @param queryType 查询类型: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)
	 * @param pageNumber 页码
	 * @param pageSize 每页数量
	 * @return 整改问题详细数据
	 */
	JsonBean zgwtIssuesDetail(String token, Integer year, String orgName, String queryType, Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 * 项目数趋势变化查询（最近12个月）
	 * @param token 用户token
	 * @return 项目数趋势数据列表
	 */
	JsonBean selectProjectTrend(String token) throws Exception;

	/**
	 * 整改问题状态统计查询
	 * @param token 用户token
	 * @return 整改问题状态统计数据列表
	 */
	JsonBean selectIssuesStatusStatistics(String token) throws Exception;

}
