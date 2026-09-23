package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblComplianceRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 合规检查规则Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface ComplianceRuleMapper extends BaseMapper<TblComplianceRule> {

    /**
     * 分页查询合规检查规则列表
     *
     * @param params 查询参数
     * @return 合规检查规则列表
     */
    List<TblComplianceRule> selectRuleList(Map<String, Object> params);

    /**
     * 根据ID查询合规检查规则详情
     *
     * @param ruleId 规则ID
     * @return 合规检查规则
     */
    TblComplianceRule selectRuleById(@Param("ruleId") String ruleId);

    /**
     * 根据规则代码查询
     *
     * @param ruleCode 规则代码
     * @return 合规检查规则
     */
    TblComplianceRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 查询可执行的规则
     *
     * @return 合规检查规则列表
     */
    List<TblComplianceRule> selectExecutableRules();

    /**
     * 批量删除合规检查规则（逻辑删除）
     *
     * @param ruleIds 规则ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ruleIds") List<String> ruleIds);

    /**
     * 更新规则状态
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @return 影响行数
     */
    int updateRuleStatus(@Param("ruleId") String ruleId, @Param("isEnabled") Integer isEnabled);
}

