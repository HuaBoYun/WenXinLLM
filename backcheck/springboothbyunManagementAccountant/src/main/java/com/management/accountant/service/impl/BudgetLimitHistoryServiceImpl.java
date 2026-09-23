package com.management.accountant.service.impl;

import com.management.accountant.oracle.entity.budget.BudgetLimitHistory;
import com.management.accountant.oracle.mapper.budget.BudgetLimitHistoryMapper;
import com.management.accountant.service.BudgetLimitHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BudgetLimitHistoryServiceImpl implements BudgetLimitHistoryService {

    @Resource
    private BudgetLimitHistoryMapper historyMapper;

    @Override
    public List<BudgetLimitHistory> getHistoryByLimitId(String limitId) {
        List<BudgetLimitHistory> history = historyMapper.selectByLimitId(limitId);
        return history != null ? history : Collections.emptyList();
    }

    @Override
    public BudgetLimitHistory recordHistory(String limitId, String operationType, String operationDesc,
                                             String beforeValue, String afterValue, String operator) {
        BudgetLimitHistory history = new BudgetLimitHistory();
        history.setHistoryId(UUID.randomUUID().toString().replace("-", ""));
        history.setLimitId(limitId);
        history.setOperationType(operationType);
        history.setOperationDesc(operationDesc);
        history.setBeforeValue(beforeValue);
        history.setAfterValue(afterValue);
        history.setOperator(operator != null ? operator : "system");
        history.setOperateTime(new Date());
        history.setCreateTime(new Date());
        historyMapper.insert(history);
        return history;
    }


}
