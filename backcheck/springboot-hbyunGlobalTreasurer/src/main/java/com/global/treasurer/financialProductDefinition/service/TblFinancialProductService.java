package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialProduct;

import java.util.List;
import java.util.Map;

/**
 * 金融产品管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblFinancialProductService extends IService<TblFinancialProduct> {

    IPage<TblFinancialProduct> getPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    TblFinancialProduct getDetail(Long id);

    TblFinancialProduct create(TblFinancialProduct entity, String createBy);

    boolean update(TblFinancialProduct entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, String productStatus, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, String productStatus, String updateBy);

    boolean updateShelfStatus(Long id, String shelfStatus, String updateBy);

    boolean batchUpdateShelfStatus(List<Long> ids, String shelfStatus, String updateBy);

    List<TblFinancialProduct> getEnabledList(Long orgId);

    boolean checkCodeUnique(String productCode, Long excludeId);

    List<TblFinancialProduct> getByCategoryId(Long categoryId);

    TblFinancialProduct copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> getStatistics(Long orgId);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

