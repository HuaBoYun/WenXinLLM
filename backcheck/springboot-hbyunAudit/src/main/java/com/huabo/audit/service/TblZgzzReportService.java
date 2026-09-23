package com.huabo.audit.service;

import javax.annotation.Resource;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.entity.TblZgzzReport;
import com.huabo.audit.oracle.mapper.TblZgzzRctevaluationMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationimplMapper;
import com.huabo.audit.oracle.vo.TblZgzzReportVo;

public interface TblZgzzReportService {

	/**
	 *审计报告 保存
	 * @param token 		--用户登录令牌
	 * @param report		--审计报告实体
	 * @param planStrs		--关联的整改方案和整改落实内容jsonArray字符串
	 * @param attIds		--新上传的附件信息Id
	 * @return
	 */
	JsonBean saveReport(String token, TblZgzzReport report, String planStrs, String[] attIds) throws Exception;

	/**
	 * 整改报告删除接口
	 * @param token		--用户登录令牌
	 * @param reportid	--审计报告主键
	 * @return
	 * @throws Exception
	 */
	JsonBean removeReport(String token, String reportid) throws Exception;

	/**
	 * 整改报告删除附件接口
	 * @param token			--用户登录令牌
	 * @param reportId		--整改报告主键
	 * @param attId			--附件主键
	 * @return
	 * @throws Exception
	 */
	JsonBean removeReportFile(String token, String reportid, String attId) throws Exception;

	/**
	 * 整改报告获取详情
	 * @param token		--用户登录token
	 * @param reportid	--整改报告主键
	 * @return
	 * @throws Exception
	 */
	JsonBean getReportDetail(String token, String reportid) throws Exception;

	/**
	 * 整改报告分页列表获取
	 * @param token		--用户登录令牌
	 * @param report    --整改报告查询条件
	 * @return
	 * @throws Exception
	 */
	JsonBean getReportList(String token, TblZgzzReportVo report) throws Exception;
	
	JsonBean getReportMeetFileList(String token, String reportid) throws Exception;
	
	public JsonBean uploadattbyid(String reportid, String attid) throws Exception;
	
	public JsonBean delattbyid(String attid, String token) throws Exception;

	
}
