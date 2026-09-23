package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.IntegrationConfigVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 集成配置版本 Mapper 接口
 *
 * @author system
 * @date 2026-04-16
 */
@Mapper
public interface IntegrationConfigVersionMapper extends BaseMapper<IntegrationConfigVersion> {

    /**
     * 根据配置ID查询版本列表
     *
     * @param configId 配置ID
     * @return 版本列表
     */
    List<IntegrationConfigVersion> listByConfigId(@Param("configId") String configId);
}
