package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.IntegrationConfigRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 集成配置规则 Mapper 接口
 *
 * @author system
 * @date 2026-04-16
 */
@Mapper
public interface IntegrationConfigRuleMapper extends BaseMapper<IntegrationConfigRule> {

    /**
     * 根据配置ID查询规则列表
     *
     * @param configId 配置ID
     * @return 规则列表
     */
    List<IntegrationConfigRule> listByConfigId(@Param("configId") String configId);
}
