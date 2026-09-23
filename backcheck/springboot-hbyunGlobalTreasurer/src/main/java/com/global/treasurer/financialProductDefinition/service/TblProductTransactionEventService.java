package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblProductTransactionEvent;

import java.util.List;
import java.util.Map;

/**
 * 产品交易事件管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblProductTransactionEventService extends IService<TblProductTransactionEvent> {

    IPage<TblProductTransactionEvent> getPage(Integer pageNo, Integer pageSize, String eventCode,
                                               String eventName, String eventType, String productType, String eventStatus, Integer isEnabled, Long orgId);

    TblProductTransactionEvent getDetail(Long id);

    TblProductTransactionEvent create(TblProductTransactionEvent entity, String createBy);

    boolean update(TblProductTransactionEvent entity, String updateBy);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    List<TblProductTransactionEvent> getEnabledList(Long orgId);

    boolean checkCodeUnique(String eventCode, Long excludeId);

    List<TblProductTransactionEvent> getByProductType(String productType, Long orgId);

    List<TblProductTransactionEvent> getByTransactionTypeId(Long transactionTypeId, Long orgId);

    TblProductTransactionEvent copy(Long id, String newCode, String newName, String createBy);

    Map<String, Object> validateDelete(Long id);

    Map<String, Object> getUsage(Long id);

    /**
     * 获取事件监控数据
     */
    Map<String, Object> getEventMonitorData(Integer pageNo, Integer pageSize);
}

