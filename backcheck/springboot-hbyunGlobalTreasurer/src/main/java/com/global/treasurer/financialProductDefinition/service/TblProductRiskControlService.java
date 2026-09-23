package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblProductRiskControl;

import java.util.List;
import java.util.Map;

/**
 * 产品风控规则管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductRiskControlService extends IService<TblProductRiskControl> {

    IPage<TblProductRiskControl> getPage(Integer pageNo, Integer pageSize, String riskControlCode,
                                          String riskControlName, String productType, Integer isEnabled, Long orgId);

    IPage<TblProductRiskControl> getPageWithFilters(Integer pageNo, Integer pageSize,
                                                    String riskControlCode, String riskControlName,
                                                    String riskLevel, String riskType,
                                                    String monitoringFrequency,
                                                    Integer isEnabled, Long orgId);

    TblProductRiskControl getDetail(Long id);

    TblProductRiskControl create(TblProductRiskControl entity, String createBy);

    boolean update(TblProductRiskControl entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductRiskControl> getEnabledList(Long orgId);

    boolean checkCodeUnique(String riskControlCode, Long excludeId);

    List<TblProductRiskControl> getByProductType(String productType, Long orgId);

    TblProductRiskControl copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

