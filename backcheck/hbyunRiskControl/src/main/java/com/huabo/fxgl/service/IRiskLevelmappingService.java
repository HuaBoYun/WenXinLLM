package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskLevelmapping;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */

public interface IRiskLevelmappingService extends IService<RiskLevelmapping> {
    public RiskLevelmapping getRiskLevelMappingBymentIdAndDegreeId(BigDecimal mentId, String DegreeId);

    RiskLevelmapping getByInfluId(String id);
    Map<String,Object> forCycle(BigDecimal planId);
    Map<String,Object> forSyCycle(String orgid,String type,String value)throws Exception;
    
    
    Map<String,Object> getFxpgRlt() throws Exception;
}
