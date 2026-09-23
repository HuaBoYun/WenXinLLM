package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.vo.XmdqVo;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblYqnsXmdqService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param xmqd
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsXmdq xmqd,String attids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param xmdqid
	 */
	JsonBean findByid(String token, BigDecimal xmdqid) throws Exception;
 
	/**
	 * 查询底稿附件
	 * @param token
	 * @param xmdqid
	 */
	JsonBean findattlistByid(String token, BigDecimal xmdqid) throws Exception;
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param tBlNbsjSheetVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,XmdqVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteone(String token, BigDecimal xmdqid) throws Exception;
	
	/**
	 * 删除附件
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteatt(String token, String attid) throws Exception;
	
	
	/**
	 * 启动项目
	 * @param token
	 * @param sheetid
	 */
	JsonBean qdproject(String token, BigDecimal xmdqid) throws Exception;
	
	/**
	 * 查询启动项目内容，列表
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	JsonBean findqdAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception;
	
	JsonBean auditProjectPlanAnalysis(String token, Integer pageNumber, Integer pageSize, Integer xmnd) throws Exception;
	
	JsonBean findPlanAnalysisXmList(String token, Integer pageNumber, Integer pageSize, Integer xmnd, Integer dataType) throws Exception;
	
	JsonBean getMx11(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectType, String projectName) throws Exception;
	
	JsonBean getMx21(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectName) throws Exception;
	
	JsonBean getMx22(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectName) throws Exception;
	
	JsonBean getMxhzList(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String glType,String projectName) throws Exception;
	
	JsonBean getMxOtherAuditList(String token, Integer pageNumber, Integer pageSize, Integer xmnd,String auditItemName) throws Exception;
	
	JsonBean getTipList(String token, XmdqVo vo) throws Exception;
	
	JsonBean tzproject(String token, BigDecimal xmdqid) throws Exception;
	
}
