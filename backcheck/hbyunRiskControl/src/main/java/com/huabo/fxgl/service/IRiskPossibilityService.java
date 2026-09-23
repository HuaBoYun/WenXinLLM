package com.huabo.fxgl.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.RiskPossibility;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskPossibilityService extends IService<RiskPossibility> {

	
	Map<String, Object>  v_list_knx(BigDecimal assstdid) throws Exception;

}
