package com.management.accountant.oracle.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

/**
 * 管理会计智慧首页 - 数据访问层
 * 从现有业务表实时计算KPI数据
 *
 * @author system
 * @date 2025-01-21
 */
@Mapper
public interface MaSmartHomeMapper {

    /**
     * 查询预算执行率、偏差率、成本节约率（全量数据）
     */
    Map<String, Object> selectBudgetRates();

    /**
     * 查询预算任务完成数量（全量）
     */
    Map<String, Object> selectBudgetTaskCount();

    /**
     * 查询预算控制待处理数（全量）
     */
    int selectControlPendingCount();
}
