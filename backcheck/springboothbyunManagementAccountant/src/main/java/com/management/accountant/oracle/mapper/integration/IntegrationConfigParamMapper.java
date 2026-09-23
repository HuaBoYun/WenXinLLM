package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.IntegrationConfigParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 集成配置参数 Mapper 接口
 *
 * @author system
 * @date 2026-04-16
 */
@Mapper
public interface IntegrationConfigParamMapper extends BaseMapper<IntegrationConfigParam> {

    /**
     * 根据配置ID查询参数列表
     *
     * @param configId 配置ID
     * @return 参数列表
     */
    List<IntegrationConfigParam> listByConfigId(@Param("configId") String configId);
}
