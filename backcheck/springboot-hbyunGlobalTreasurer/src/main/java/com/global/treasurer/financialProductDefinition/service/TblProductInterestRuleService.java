package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblProductInterestRule;

import java.util.List;
import java.util.Map;

/**
 * 产品利息规则管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductInterestRuleService extends IService<TblProductInterestRule> {

    IPage<TblProductInterestRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                           String ruleName, String productType, Integer isEnabled, Long orgId);

    TblProductInterestRule getDetail(Long id);

    TblProductInterestRule create(TblProductInterestRule entity, String createBy);

    boolean update(TblProductInterestRule entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductInterestRule> getEnabledList(Long orgId);

    boolean checkCodeUnique(String ruleCode, Long excludeId);

    List<TblProductInterestRule> getByProductType(String productType, Long orgId);

    TblProductInterestRule copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

