package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskInfludegree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
public interface IRiskInfludegreeService extends IService<RiskInfludegree> {
    public BigDecimal getInflu(BigDecimal assId, String level);
	
    Map<String, Object>  v_list_jb(BigDecimal assstdid) throws Exception;

}
