package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillRiskAlert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 风险预警Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface RiskAlertMapper extends BaseMapper<TblBillRiskAlert> {

    /**
     * 按状态统计预警数量
     *
     * @return 统计结果列表
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 按预警类型统计
     *
     * @return 统计结果列表
     */
    List<Map<String, Object>> countByType();

    /**
     * 按预警级别统计
     *
     * @return 统计结果列表
     */
    List<Map<String, Object>> countByLevel();
}
