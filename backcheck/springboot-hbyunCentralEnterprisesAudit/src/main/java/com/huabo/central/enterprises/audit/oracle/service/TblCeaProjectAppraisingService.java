package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectAppraising;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectAppraisingQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ImplementationPlanResult;

import java.util.List;

public interface TblCeaProjectAppraisingService {

	/**
	 * 项目评优-评优 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectAppraising> getList(TblCeaProjectAppraisingQueryParam param);

	/**
	 * 项目评优-评优 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectAppraising saveOrUpdate(TblCeaProjectAppraising param);

	/**
	 * 项目评优-评优 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-评优 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectAppraising findById(Long id);

	/**
	 * 根据项目管理实施方案ID查询信息
	 * @param implementationPlanId
	 * @return
	 */
	List<ImplementationPlanResult> getImplementationPlanList(String implementationPlanId);

	/**
	 * 已被选择了的申报IDS
	 * @return
	 */
	List<Long> getNoQuality();
}
