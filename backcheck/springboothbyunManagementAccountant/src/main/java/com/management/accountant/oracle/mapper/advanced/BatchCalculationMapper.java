package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.BatchCalculation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 批量计算Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BatchCalculationMapper extends BaseMapper<BatchCalculation> {

    /**
     * 按类型统计任务数量
     */
    List<Map<String, Object>> countByType();

    /**
     * 按状态统计任务数量
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 统计平均执行时间
     */
    Integer avgExecutionTime();
}

