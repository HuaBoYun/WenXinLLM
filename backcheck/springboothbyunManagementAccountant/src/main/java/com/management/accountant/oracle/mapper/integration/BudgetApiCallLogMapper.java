package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetApiCallLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * API调用日志Mapper
 *
 * @author AI Agent
 * @date 2026-04-15
 */
@Mapper
public interface BudgetApiCallLogMapper extends BaseMapper<BudgetApiCallLog> {

    /**
     * 根据API ID查询调用日志列表（按调用时间倒序）
     *
     * @param apiId API ID
     * @return 调用日志列表
     */
    List<BudgetApiCallLog> listByApiId(@Param("apiId") String apiId);
}
