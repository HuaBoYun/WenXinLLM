package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateRequest;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateResponse;
import com.global.treasurer.financialProductDefinition.entity.TblProductPrincipalRule;

import java.util.List;
import java.util.Map;

/**
 * 产品本金规则管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductPrincipalRuleService extends IService<TblProductPrincipalRule> {

    IPage<TblProductPrincipalRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                            String ruleName, String productType, Integer isEnabled, Long orgId);

    TblProductPrincipalRule getDetail(Long id);

    TblProductPrincipalRule create(TblProductPrincipalRule entity, String createBy);

    boolean update(TblProductPrincipalRule entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductPrincipalRule> getEnabledList(Long orgId);

    boolean checkCodeUnique(String ruleCode, Long excludeId);

    List<TblProductPrincipalRule> getByProductType(String productType, Long orgId);

    TblProductPrincipalRule copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);

    /**
     * 本金计算
     *
     * @param request 计算请求
     * @return 计算结果
     */
    PrincipalCalculateResponse calculate(PrincipalCalculateRequest request);
}

