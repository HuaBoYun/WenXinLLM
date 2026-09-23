package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblProductAccountingAttr;

import java.util.List;
import java.util.Map;

/**
 * 产品核算属性管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductAccountingAttrService extends IService<TblProductAccountingAttr> {

    IPage<TblProductAccountingAttr> getPage(Integer pageNo, Integer pageSize, String attrCode,
                                             String attrName, String productType,
                                             String accountingSubjectType, String accountingMethod,
                                             Integer isEnabled, Long orgId);

    TblProductAccountingAttr getDetail(Long id);

    TblProductAccountingAttr create(TblProductAccountingAttr entity, String createBy);

    boolean update(TblProductAccountingAttr entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductAccountingAttr> getEnabledList(Long orgId);

    boolean checkCodeUnique(String attrCode, Long excludeId);

    List<TblProductAccountingAttr> getByProductType(String productType, Long orgId);

    TblProductAccountingAttr copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

