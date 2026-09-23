package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.FinancingRiskMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资风险监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface FinancingRiskMonitoringMapper extends BaseMapper<FinancingRiskMonitoring> {

    /**
     * 分页查询融资风险监控列表
     * @param params 查询参数
     * @return 融资风险监控列表
     */
    List<FinancingRiskMonitoring> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询融资风险监控详情
     * @param id 主键ID
     * @return 融资风险监控详情
     */
    FinancingRiskMonitoring selectDetailById(@Param("id") Long id);

    /**
     * 统计融资风险监控数量
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);
}

