package com.huabo.audit.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.Pamas;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblBeforeZgzzListEntity;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import org.apache.poi.ss.usermodel.Sheet;

public interface TblZgzzIssuesilistService{

	/**
	 * 整改追责问题新增 or 修改
	 * @param token  当前登录用户主键
	 * @param issues 保存实体
	 * @param projectName 项目名称
	 * @param projectNo 项目编号
	 * @param attIds 附件主键数组
	 * @param auditTypes 
	 * @param audits 
	 * @return
	 * @throws Exception
	 */
	JsonBean saveIssues(String token, TblZgzzIssuesilist issues, String projectName, String projectNo, String[] attIds) throws Exception;
	
	/**
	 * 整改清单 通过Id 获取详情
	 * @param token 当前登录用户主键
	 * @param issuesId 整改清单主键
	 * @return
	 * @throws Exception
	 */
	JsonBean getIssues(String token, String issuesId) throws Exception;

	/**
	 *  整改清单 通过主键删除
	 * @param token
	 * @param issuesId
	 * @return
	 * @throws Exception
	 */
	JsonBean removeIssues(String token, String issuesId) throws Exception;

	/**
	 * 整改清单获取分页列表信息
	 * @param token
	 * @param issues
	 * @return
	 * @throws Exception
	 */
	JsonBean getIssuesList(String token, TblZgzzIssuesilistVo issues) throws Exception;

	/**
	 * 整改清单导出
	 * @param token
	 * @param issues
	 * @param response 
	 * @return
	 * @throws Exception
	 */
	JsonBean exportIssuesList(String token, TblZgzzIssuesilistVo issues, HttpServletResponse response) throws Exception;

	/**
	 * 整改清单新增、修改  获取选择的审计清单接口
	 * @param token
	 * @param tBlNbsjSheetVo
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	JsonBean getSummaryAudit_list(String token, TBlNbsjSheetVo tBlNbsjSheetVo, Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 * 整改问题清单删除附件关系
	 * @param token
	 * @param issuesId
	 * @param attIds
	 * @return
	 * @throws Exception
	 */
	JsonBean removeIssuesFile(String token, String issuesId, String attId) throws Exception;

	/**
	 * 整改清单新增、修改获取内控清单接口
	 * @param token
	 * @param problem
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	JsonBean getProblemLedgerList(String token, TblTesttaskProblemFind problem, Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 *      整改 方案 新增修改，选择关联项目后 获取项目相关的所有整改清单 
	 * @param token
	 * @param issues
	 * @return
	 */
	JsonBean getAfterProjectIssues(String token, TblZgzzIssuesilistVo issues) throws Exception;

	/**
	 * 整改清单台账
	 * @param token		--用户登录令牌
	 * @param issu		--整改落实查询参数实体
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationIssuesLedgetList(String token, TblZgzzIssuesilistVo issu) throws Exception;

	/**
	 * 整改清单台账导出
	 * @param token		--用户登录令牌
	 * @param issu		--整改落实查询参数实体
	 * @param response 
	 * @return
	 * @throws Exception
	 */
	JsonBean exportIssuesLedgetList(String token, TblZgzzIssuesilistVo issu, HttpServletResponse response) throws Exception;

	/**
	 *       未销号问题，再次发起整改接口；
	 * @param token		--用户登录令牌
	 * @param issuesId	--未销号问题整改清单主键
	 * @return
	 * @throws Exception
	 */
	JsonBean issuesRectificationAgain(String token, String issuesId) throws Exception;

	/**
	 * 整改清单 问题来源为审计 并且底稿的被审计对象类型 为公司或者部门  调用此接口获取下拉被审计对象数据
	 * @param token
	 * @param sheetId
	 * @param auditOrgId
	 * @return
	 * @throws Exception
	 */
	JsonBean getAuditedObjectList(String token, BigDecimal sheetId, BigDecimal auditOrgId,BigDecimal projectId) throws Exception;

	/**
	 * 整改清单修改状态
	 * @param token
	 * @param issuesId   --整改清单主键
	 * @param status     --状态
	 * @return
	 * @throws Exception
	 */
	JsonBean modifyIssuesStatus(String token, String issuesId, Integer status) throws Exception;



	//整改清单批量导入
	void resolveSheet(Sheet sheet, String token) throws Exception;

	//往期整改清单批量导入
	String resolveSheetBefore(Sheet sheet, String token) throws Exception;
	//往期整改清单查询
	JsonBean getbeforeZgzzList(String token, TblBeforeZgzzListEntity tblBeforeZgzzListEntity, Integer pageNum, Integer pageSize) throws Exception;
	//往期整改清单删除
	JsonBean deleteBeforeList(String token, String ids) throws Exception;

	JsonBean exportBeforeIssuesList(TblStaffUtil loginStaff, TblBeforeZgzzListEntity tblBeforeZgzzListEntity, HttpServletResponse response) throws Exception;

	JsonBean exportBeforeResponseList(TblStaffUtil loginStaff, HttpServletResponse response,
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity) throws Exception;

	JsonBean exportBeforeZgList(TblStaffUtil loginStaff, TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			HttpServletResponse response) throws Exception;


	JsonBean importdealSendDoubtful(String token, Pamas pamas, HttpServletResponse response) throws Exception;


}
