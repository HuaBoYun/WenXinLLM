package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.dto.MappingRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.util.MyJsonBean;

/**
 * 映射规则Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface MappingRuleService {

    /**
     * 分页查询映射规则
     *
     * @param param 查询参数
     * @param orgId 组织ID
     * @return 分页结果
     */
    MyJsonBean queryPage(MappingRuleQueryParam param, String orgId);

    /**
     * 根据ID查询映射规则
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @return 映射规则
     */
    MyJsonBean queryById(String ruleId, String orgId);

    /**
     * 保存映射规则
     *
     * @param mappingRule 映射规则
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean saveMappingRule(TblMappingRule mappingRule, String orgId, String userId);

    /**
     * 删除映射规则
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean deleteMappingRule(String ruleId, String orgId);

    /**
     * 启用/禁用映射规则
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean toggleEnabled(String ruleId, String isEnabled, String orgId, String userId);

    /**
     * 复制映射规则
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean copyMappingRule(String ruleId, String orgId, String userId);

    /**
     * 更新排序号
     *
     * @param ruleId 规则ID
     * @param sortNo 排序号
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean updateSortNo(String ruleId, Integer sortNo, String orgId, String userId);
}

