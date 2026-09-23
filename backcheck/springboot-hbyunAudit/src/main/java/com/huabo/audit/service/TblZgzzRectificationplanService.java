package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;

public interface TblZgzzRectificationplanService{

	/**
	 *  整改方案 新增、修改  获取选择的方案项目列表
	 * @param token 登录用户token
	 * @param project 筛选条件实体
	 * @param planType 方案类别 1-审计 2-风控 3-外部
	 * @return
	 * @throws Exception
	 */
	JsonBean getSolutionProjectList(String token, TblZgzzProjectVo project, Integer planType) throws Exception;

	/**
	 * 整改方案新增 保存 方法
	 * @param token   登录用户令牌
	 * @param rectification 整改方案实体
	 * @param issuesIds  关联新增整改清单主键
	 * @return
	 * @throws Exception
	 */
	JsonBean saveRectificationPlan(String token, TblZgzzRectificationplan rectification) throws Exception;

	/**
	 * 整改方案根据主键获取详情
	 * @param token 登录用户令牌
	 * @param planId 整改方案主键
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationPlanDetail(String token, String planId) throws Exception;

	/**
	 * 整改方案删除附件关系表 
	 * @param token		用户登录令牌
	 * @param planId	整改方案主键
	 * @param attId		附件主键
	 * @return
	 * @throws Exception
	 */
	JsonBean removeRectificationFile(String token, String planId, String attId) throws Exception;

	/**
	 * 整改方案删除 整改清单中间关系表的数据
	 * @param token		用户登录令牌
	 * @param planId	整改方案主键
	 * @param issuesId	整改问题清单主键
	 * @return
	 */
	JsonBean removeRectificationIssues(String token, String planId, String issuesId) throws Exception;

	/**
	 * 整改方案删除接口
	 * @param token  	用户登录令牌
	 * @param issuesId	整改方案主键
	 * @return
	 * @throws Exception
	 */
	JsonBean removeRectification(String token, String planId) throws Exception;

	/**
	 * 整改方案获取数据当前状态
	 * @param token  	用户登录令牌
	 * @param issuesId	整改方案主键
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationStatus(String token, String planId) throws Exception;

	/**
	 * 整改方案 获取分页列表
	 * @param loginStaff		用户登录信息
	 * @param plan		整改方案 筛选条件封装实体
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationPlanList(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan) throws Exception;

	/**
	 * 整改方案 详情 修改 页面 获取 整改清单所有详细数据
	 * @param token
	 * @param relaId
	 * @return
	 * @throws Exception
	 */
	JsonBean getIssuesDetailByPlan(String token, String relaId) throws Exception;

	/**
	 * 整改方案与整改清单中间关系表保存  整改信息
	 * @param token
	 * @param rela  中间表实体
	 * @param orgIds 承办部门数组
	 * @return
	 * @throws Exception
	 */
	JsonBean saveIssuesRelaPlan(String token, TblRectificationIssues rela, String[] orgIds) throws Exception;

	/**
	 * 整改分派 点击分派按钮 获取整改清单分页集合
	 * @param token		用户登录令牌
	 * @param issues	整改清单筛选条件
	 * @return
	 */
	JsonBean getRectificationAllocationIssuesList(String token, TblRectificationIssuesVo issues) throws Exception ;

	/**
	 * 整改分派 保存整改清单整改落实人信息
	 * @param token				用户登录令牌
	 * @param impementerId		整改落实人主键
	 * @param relaId			中间关系表主键
	 * @return
	 * @throws Exception
	 */
	JsonBean saveIssuesRelaImpementer(String token, BigDecimal impementerId, String relaId) throws Exception;

	
	/**
	 * 我的整改，获取所有登录用户整改的整改清单数据
	 * @param token		用户登录令牌
	 * @param reiss		整改方案清单中间表筛选条件
	 * @param issues	整改清单筛选条件
	 *  * @param plan	整改方案筛选条件
	 * @return
	 */
	JsonBean getMyRectificationList(String token, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan) throws Exception;

	/**
	 * 获取未销号问题列表页面
	 * @param token
	 * @param issues
	 * @return
	 * @throws Exception
	 */
	JsonBean getUnresolvedIssuesList(String token, TblRectificationIssuesVo issues) throws Exception;

	/**
	 * 整改清单问题台账查询
	 * @param token		用户登录令牌
	 * @param issues	整改清单与整改方案中间关系表，包含整改清单和整改方案的查询条件
	 * @param isAll 	是否查看整改问题清单最后一次整改关联的相关信息  1-是  0否 默认0 查看所有信息
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationIssuesLedgetList(String token, TblRectificationIssuesVo issues, Integer isAll) throws Exception;

	/**
	 *  整改清单台账导出方法
	 * @param token			用户登录令牌
	 * @param issues		整改清单与整改方案中间关系表，包含整改清单和整改方案的查询条件
	 * @param response
	 * @param isAll			是否查看整改问题清单最后一次整改关联的相关信息  1-是  0否 默认0 查看所有信息
	 * @return
	 */
	JsonBean exportIssuesLedgetList(String token, TblRectificationIssuesVo issues, HttpServletResponse response,Integer isAll) throws Exception;

	/**
	 * 整改方案台账导出
	 * @param loginStaff		--登录用户
	 * @param plan				--筛选条件
	 * @param response			--响应
	 * @throws Exception
	 */
	JsonBean exportRectificationPlanLedger(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan,
			HttpServletResponse response) throws Exception;

	/**
	 * 通过整改报告类型查询整改方案
	 * @param loginStaff	--登录用户
	 * @param plan			--整改方案查询条件
	 * @param reporttype	--整改报告类型
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationPlanListByReportType(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan,
			Integer reporttype) throws Exception;

	/**
	 * 整改评价分页列表
	 * @param token
	 * @param reiss
	 * @param issues
	 * @param plan
	 * @return
	 */
	JsonBean getRectificationValuation(String token, TblRectificationIssuesVo reiss, TblZgzzIssuesilistVo issues,
			TblZgzzRectificationplanVo plan) throws Exception;

	JsonBean modifyRectificationPlanStatus(String token, TblZgzzRectificationplan rectification) throws Exception;

	/**
	 * 查找次整改方案下此整改清单的所有相关信息
	 * @param token
	 * @param planId
	 * @param issuesId
	 * @return
	 * @throws Exception
	 */
	JsonBean getIssuesAllDetailInfoByPlanIssuesId(String token, String planId, String issuesId) throws Exception;

	JsonBean completeRectificationEval(String token, String planId) throws Exception;

	JsonBean saveIssuesRelaPlan(String token, TblRectificationIssues rela, String[] orgIds, String[] attIds) throws Exception;

	JsonBean removePlanIssuesFile(String token, String relaId, String attId) throws Exception;
	
	JsonBean exportMyRectificationList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan,
			HttpServletResponse response, TblZgzzRectificationimplVo reimpl) throws Exception;

	JsonBean exportMyRectificationIssuesList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response, TblZgzzRectificationimplVo reimpl) throws Exception;

	JsonBean exportMyRectificationResponseList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response,TblZgzzRectificationimplVo reimpl) throws Exception;

	JsonBean getRecitfiacationImpleLedger(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response, TblZgzzRectificationimplVo reimpl) throws Exception;
	
 	 String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception ;
 	 
 	 
 	 /**
 	  * 后续整改列表
 	  * @param token
 	  * @param reiss
 	  * @param issues
 	  * @param plan
 	  * @return
 	  * @throws Exception
 	  */
 	JsonBean getMyhxzgList(String token, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan) throws Exception;
 	
 	
 	
 	
 	JsonBean getbyUserlist(String token, String relaId) throws Exception;

}
