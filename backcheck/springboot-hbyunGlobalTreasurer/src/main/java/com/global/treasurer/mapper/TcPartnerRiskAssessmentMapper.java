package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcPartnerRiskAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 合作伙伴风险评估Mapper接口
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
@Mapper
public interface TcPartnerRiskAssessmentMapper extends BaseMapper<TcPartnerRiskAssessment> {

    /**
     * 根据参数查询(包含伙伴信息)
     */
    List<TcPartnerRiskAssessment> selectByParams(@Param("params") Map<String, Object> params);

    /**
     * 根据伙伴ID查询
     */
    List<TcPartnerRiskAssessment> selectByPartnerId(@Param("partnerId") String partnerId);

    /**
     * 根据风险等级查询
     */
    List<TcPartnerRiskAssessment> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 根据风险状态查询
     */
    List<TcPartnerRiskAssessment> selectByRiskStatus(@Param("riskStatus") String riskStatus);

    /**
     * 统计风险评估数据
     */
    Map<String, Object> selectStatistics();
}

