package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.entity.Staff;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-11
 */
public interface IRiskAssplanRiskService extends IService<RiskAssplanRisk> {
    List<RiskAssplanRisk> findRiskByRiskType(String typeId,BigDecimal planId);
    IPage<RiskAssplanRisk> findRiskInRiskIdAndAssId(String ids, BigDecimal planId, IPage pageBean);
    List<RiskAssplanRisk> getByPlanid(BigDecimal planid);
    void delete(RiskAssplanRisk assPlanRisk);
    List<RiskAssplanRisk> findRiskByRisk(BigDecimal id);
    List<RiskAssplanRisk> get(BigDecimal planid, BigDecimal riskid);
    List<RiskAssplanRisk> findRiskByPlanId(BigDecimal planId);

    //显示详情
    List<RiskAssplanRisk> findRiskByRiskid(BigDecimal id);

    IPage<RiskAssplanRisk> findRiskByAssplanidAndStaffid(BigDecimal planId, BigDecimal staffId, IPage pageBean);

	JsonBean findRiskInRiskIdAndAssId(String token, Integer pageNumber, Integer pageSize, String riskIds,
			BigDecimal planId) throws Exception;
	IPage findRiskByRiskAndLevel(BigDecimal bigDecimal, BigDecimal staffid, IPage page, String level) throws Exception;
	
	IPage findRiskResultById(BigDecimal riskid, BigDecimal staffid, IPage page) throws Exception;

	
	IPage fingRiskByAssIdAndRiskId(BigDecimal assrisks, BigDecimal riskid, IPage page);
	JsonBean findRiskAssplanRiskByRiskId(String token, BigDecimal riskid) throws Exception;
	JsonBean findRiskAssplanRiskCountByRiskId(String token, BigDecimal riskid) throws Exception;

	Map<String, Object> getRiskCatnameAnalysis(String token,String company)throws Exception;
	
	Map<String, Object> getRiskAnalysis(String token,String company)throws Exception;
	
	Map<String, Object> getRiskAreasAnalysis(String token,String company)throws Exception;
	
	Map<String, Object> getRiskByDepartmentAnalysis(String token,String company)throws Exception;

	Map<String, Object> getRiskByCompanyAnalysis(String token,String company)throws Exception;

	Map<String, Object> getRiskCompanyList(String token)throws Exception;

	

	
}
