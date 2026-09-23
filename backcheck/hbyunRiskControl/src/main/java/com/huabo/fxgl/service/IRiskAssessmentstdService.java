package com.huabo.fxgl.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.vo.fieldActivationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskAssessmentstdService extends IService<RiskAssessmentstd> {

	/**
	 * 查询评估标准
	 * 
	 * @param assName
	 *            评估标准名
	 * @param orgid 
	 * @return
	 */
	PageInfo<RiskAssessmentstd> selectEvaluateStandard(String assName, BigDecimal orgid,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil,String secrectLevelld) throws Exception;

	// 获取风险评估标准列表
	PageInfo<RiskAssessmentstd> findByAllList(RiskAssessmentstd risk, Integer pageNo,Integer pageSize,TblStaffUtil staffUtil,Integer authorityType) throws Exception;

	Map<String, Object> deleteRisk(String riskid) throws Exception;

	Map<String, Object> saveBz(String assNumber, String assName, String assDes, TblStaffUtil staffUitl, String possibilityStr, String infludegreeStr, String levelStr,BigDecimal secrectLevelId,String staffScopeNames,String staffScopeIds,fieldActivationVo vo) throws Exception;

	Map<String, Object> getRiskAssessMentstd(BigDecimal assstdid) throws Exception;

	Map<String, Object> updateRiskAssessMentstd(RiskAssessmentstd risk, String possibilityStr, String infludegreeStr, String levelStr) throws Exception;

	Map<String, Object> v_list_yxcd(BigDecimal assstdid) throws Exception;
	
	JsonBean get_riskpgbz_no(String token) throws Exception;

}
