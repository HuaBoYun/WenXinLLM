package com.global.treasurer.service;

import com.global.treasurer.entity.TblSettlementException;
import java.util.List;
import java.util.Map;

public interface SettlementExceptionService {
    Map<String, Object> getExceptionPage(Map<String, Object> params);
    TblSettlementException getExceptionById(Long exceptionId);
    int createException(TblSettlementException exception);
    int updateException(TblSettlementException exception);
    int deleteException(List<Long> exceptionIds);
    int assignException(Long exceptionId, String assignee);
    int resolveException(Long exceptionId, String resolveNotes);
    int escalateException(Long exceptionId, String escalateTo);
    List<TblSettlementException> getUnresolvedExceptions(Long orgId);
    List<TblSettlementException> getCriticalExceptions(Long orgId);
    Map<String, Object> getExceptionSummary(Map<String, Object> params);
    List<Map<String, Object>> getExceptionTrend(Map<String, Object> params);
    List<Map<String, Object>> getRecurringPatterns(Map<String, Object> params);
}
