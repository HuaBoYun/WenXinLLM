package com.huabo.audit.service;

import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ZhFormEntity;
import com.huabo.audit.oracle.vo.ZhFormVo;

public interface ZhFormService {
	/**
	 * 分页查询当前公司所有审计类型
	 * @param startIndex
	 * @param orgid
	 * @param pageSize
	 * @return
	 */
//	public PageBean findAll(Integer startIndex,String orgid,Integer pageSize);
	/**
	 * 新增，修改审计类型
	 * @param tnt
	 */
	public void save(ZhFormEntity tnt);
	/**
	 * 根据id查询审计类型详情
	 * @param id
	 * @return
	 */
	public ZhFormEntity findByid(String id);
	/**
	 * 删除
	 * @param tnt
	 */
	public void del(ZhFormEntity tnt);
	/**
	 * 查询当前项目下的表单
	 * @param orgid
	 * @return
	 */
	public List<ZhFormEntity> findByProject(Integer projectId);
//	/**
//	 * 根据不同登录用户，查询表单展现列表
//	 * @param currentUserId
//	 * @param projectId
//	 * @param startIndex
//	 * @param pageSize
//	 * @return
//	 */
//	public PageBean findFormList(BigDecimal currentUserId,Integer projectId,Integer startIndex,Integer pageSize);
//	/**
//	 * 查询当前用户需要评价的其他人的质量报告
//	 * @param currentUserId
//	 * @param projectId
//	 * @param startIndex
//	 * @param pageSize
//	 * @return
//	 */
//	public PageBean findSpReportByProject(BigDecimal currentUserId,Integer startIndex,Integer pageSize);
//	/**
//	 * 稽核部负责人查看当前项目下所有质量报告
//	 * @param projectId
//	 * @param startIndex
//	 * @param pageSize
//	 * @return
//	 */
//	public PageBean findFormListByProject(Integer projectId,Integer startIndex,Integer pageSize);
	
	
	
	//==
	JsonBean reportPageList(String token, Integer pageNumber, Integer pageSize,ZhFormVo zhFormVo) throws Exception;
	
	JsonBean reportAdd(ZhFormEntity zf, String token)throws Exception;
    
    JsonBean reportDelete(Integer formid, String token) throws Exception;
    
    JsonBean findNbsjWorkReportDetail(String token, Integer formid) throws Exception;
	
	
	
	
}
