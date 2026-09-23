package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblTransactionType;

import java.util.List;
import java.util.Map;

/**
 * 交易类型管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblTransactionTypeService extends IService<TblTransactionType> {

    IPage<TblTransactionType> getPage(Integer pageNo, Integer pageSize, String transactionTypeCode,
                                       String transactionTypeName, String transactionCategory,
                                       String direction, String riskLevel, Integer isEnabled, Long orgId);

    TblTransactionType getDetail(Long id);

    TblTransactionType create(TblTransactionType entity, String createBy);

    boolean update(TblTransactionType entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblTransactionType> getEnabledList(Long orgId);

    boolean checkCodeUnique(String transactionTypeCode, Long excludeId);

    List<TblTransactionType> getTree(Long orgId);

    boolean sort(List<TblTransactionType> list);

    TblTransactionType copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);
}

