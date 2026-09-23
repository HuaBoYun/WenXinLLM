package com.huabo.fxgl.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.RiskCoping;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskCopingService extends IService<RiskCoping> {

	 boolean save(RiskCoping riskCoping, String attids);

	 boolean updateById(RiskCoping riskCoping, String attids);

	JsonBean findRiskCopyingCountByRiskId(String token, BigDecimal riskid) throws Exception;
	
	boolean updateByEval(RiskCoping riskCoping);
	
	Map<String, Object> getCompanyRiskResponse(String token,String year) throws Exception;
	
	void convertControlMeasures(String token) throws Exception;
}
