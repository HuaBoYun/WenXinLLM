package com.financial.sharing.dataCollection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dataCollection.dto.DataQualityRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityRule;

import java.util.List;

/**
 * 数据质量规则服务接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface DataQualityRuleService {

    /**
     * 分页查询质量规则
     *
     * @param param 查询参数
     * @param orgId 组织ID
     * @return 分页结果
     */
    IPage<TblDataQualityRule> queryPage(DataQualityRuleQueryParam param, Long orgId);

    /**
     * 根据ID查询质量规则
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @return 质量规则
     */
    TblDataQualityRule queryById(Long ruleId, Long orgId);

    /**
     * 保存质量规则
     *
     * @param rule 质量规则
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean saveRule(TblDataQualityRule rule, Long orgId, String userId);

    /**
     * 删除质量规则
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean deleteRule(Long ruleId, Long orgId);

    /**
     * 批量删除质量规则
     *
     * @param ruleIds 规则ID列表
     * @param orgId 组织ID
     * @return 删除数量
     */
    int batchDeleteRule(List<Long> ruleIds, Long orgId);

    /**
     * 启用/禁用质量规则
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean toggleEnabled(Long ruleId, String isEnabled, Long orgId, String userId);

    /**
     * 查询所有启用的规则
     *
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<TblDataQualityRule> queryEnabledRules(Long orgId);
}

