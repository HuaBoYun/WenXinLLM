package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialCategory;

import java.util.List;
import java.util.Map;

/**
 * 金融分类管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblFinancialCategoryService extends IService<TblFinancialCategory> {

    IPage<TblFinancialCategory> getPage(Integer pageNo, Integer pageSize, String categoryCode,
                                         String categoryName, Integer isEnabled, Long orgId, Long parentCategoryId);

    TblFinancialCategory getDetail(Long id);

    TblFinancialCategory create(TblFinancialCategory entity, String createBy);

    boolean update(TblFinancialCategory entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblFinancialCategory> getEnabledList(Long orgId);

    boolean checkCodeUnique(String categoryCode, Long excludeId);

    List<TblFinancialCategory> getTree(Long orgId);

    List<TblFinancialCategory> getByParentId(Long parentId, Long orgId);

    boolean sort(List<TblFinancialCategory> list);

    TblFinancialCategory copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> getStatistics(Long orgId);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

