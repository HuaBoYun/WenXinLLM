package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TreasuryBusinessRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 财资公共模块 - 业务活动规则管理Mapper接口
 * 
 * @author HuaBo Cloud
 * @since 2025-09-22
 */
@Mapper
public interface TreasuryBusinessRuleMapper extends BaseMapper<TreasuryBusinessRule> {

    /**
     * 分页查询业务活动规则列表
     * 
     * @param page 分页参数
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param businessModule 业务模块
     * @param businessDefinitionId 业务品种ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @return 分页结果
     */
    IPage<TreasuryBusinessRule> selectBusinessRulePage(Page<TreasuryBusinessRule> page,
                                                       @Param("ruleCode") String ruleCode,
                                                       @Param("ruleName") String ruleName,
                                                       @Param("ruleType") String ruleType,
                                                       @Param("businessModule") String businessModule,
                                                       @Param("businessDefinitionId") Long businessDefinitionId,
                                                       @Param("isEnabled") Integer isEnabled,
                                                       @Param("orgId") Long orgId);

    /**
     * 根据规则编码和组织ID查询业务活动规则
     * 
     * @param ruleCode 规则编码
     * @param orgId 组织ID
     * @return 业务活动规则
     */
    TreasuryBusinessRule selectByCodeAndOrg(@Param("ruleCode") String ruleCode, @Param("orgId") Long orgId);

    /**
     * 查询所有启用的业务活动规则
     * 
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> selectEnabledRules(@Param("orgId") Long orgId);

    /**
     * 根据规则类型查询业务活动规则列表
     * 
     * @param ruleType 规则类型
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> selectByRuleType(@Param("ruleType") String ruleType, @Param("orgId") Long orgId);

    /**
     * 根据业务模块查询业务活动规则列表
     * 
     * @param businessModule 业务模块
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> selectByBusinessModule(@Param("businessModule") String businessModule, @Param("orgId") Long orgId);

    /**
     * 根据业务品种ID查询业务活动规则列表
     * 
     * @param businessDefinitionId 业务品种ID
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> selectByBusinessDefinition(@Param("businessDefinitionId") Long businessDefinitionId, @Param("orgId") Long orgId);

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param orgId 组织ID
     * @param excludeId 排除的ID
     * @return 数量
     */
    int checkRuleCodeExists(@Param("ruleCode") String ruleCode, 
                           @Param("orgId") Long orgId, 
                           @Param("excludeId") Long excludeId);

    /**
     * 根据优先级查询业务活动规则列表
     * 
     * @param ruleType 规则类型
     * @param businessModule 业务模块
     * @param orgId 组织ID
     * @return 业务活动规则列表（按优先级排序）
     */
    List<TreasuryBusinessRule> selectByPriority(@Param("ruleType") String ruleType,
                                                @Param("businessModule") String businessModule,
                                                @Param("orgId") Long orgId);

    /**
     * 查询规则引擎类型列表
     * 
     * @param orgId 组织ID
     * @return 规则引擎类型列表
     */
    List<String> selectRuleEngineTypes(@Param("orgId") Long orgId);

    /**
     * 根据规则版本查询业务活动规则列表
     * 
     * @param ruleVersion 规则版本
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> selectByRuleVersion(@Param("ruleVersion") String ruleVersion, @Param("orgId") Long orgId);
}
