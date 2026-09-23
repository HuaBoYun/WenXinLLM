package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblProductTermRule;

import java.util.List;
import java.util.Map;

/**
 * 产品期限规则管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductTermRuleService extends IService<TblProductTermRule> {

    IPage<TblProductTermRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                       String ruleName, String productType, String termType, String termUnit, Integer isEnabled, Long orgId);

    TblProductTermRule getDetail(Long id);

    TblProductTermRule create(TblProductTermRule entity, String createBy);

    boolean update(TblProductTermRule entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductTermRule> getEnabledList(Long orgId);

    boolean checkCodeUnique(String ruleCode, Long excludeId);

    List<TblProductTermRule> getByProductType(String productType, Long orgId);

    TblProductTermRule copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

