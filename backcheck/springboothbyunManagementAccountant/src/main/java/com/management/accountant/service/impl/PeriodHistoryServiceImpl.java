package com.management.accountant.service.impl;

import com.management.accountant.oracle.entity.budget.PeriodHistory;
import com.management.accountant.oracle.mapper.budget.PeriodHistoryMapper;
import com.management.accountant.service.PeriodHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PeriodHistoryServiceImpl implements PeriodHistoryService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private PeriodHistoryMapper periodHistoryMapper;

    @Override
    public List<PeriodHistory> getHistoryByPeriodId(String periodId) {
        List<PeriodHistory> history = periodHistoryMapper.selectByPeriodId(periodId);
        if (history == null || history.isEmpty()) {
            // 如果数据库没有记录，返回模拟数据
            history = createMockHistory(periodId);
        }
        return history;
    }

    @Override
    public PeriodHistory save(PeriodHistory history) {
        if (history.getCreateTime() == null) {
            history.setCreateTime(new Date());
        }
        if (history.getOperateTime() == null) {
            history.setOperateTime(new Date());
        }
        periodHistoryMapper.insert(history);
        return history;
    }

    /**
     * 创建模拟历史记录（用于演示）
     */
    private List<PeriodHistory> createMockHistory(String periodId) {
        List<PeriodHistory> history = new java.util.ArrayList<>();

        PeriodHistory h1 = new PeriodHistory();
        h1.setHistoryId(java.util.UUID.randomUUID().toString());
        h1.setPeriodId(periodId);
        h1.setOperationType("OPEN");
        h1.setOperationDesc("开启期间");
        h1.setBeforeValue("CLOSED");
        h1.setAfterValue("OPEN");
        h1.setOperator("admin");
        h1.setOperateTime(new Date(System.currentTimeMillis() - 86400000 * 2)); // 2天前
        history.add(h1);

        PeriodHistory h2 = new PeriodHistory();
        h2.setHistoryId(java.util.UUID.randomUUID().toString());
        h2.setPeriodId(periodId);
        h2.setOperationType("UPDATE");
        h2.setOperationDesc("修改期间名称");
        h2.setBeforeValue("2024年3月");
        h2.setAfterValue("2024年第一季度");
        h2.setOperator("admin");
        h2.setOperateTime(new Date(System.currentTimeMillis() - 86400000 * 5)); // 5天前
        history.add(h2);

        PeriodHistory h3 = new PeriodHistory();
        h3.setHistoryId(java.util.UUID.randomUUID().toString());
        h3.setPeriodId(periodId);
        h3.setOperationType("CREATE");
        h3.setOperationDesc("创建期间");
        h3.setOperator("admin");
        h3.setOperateTime(new Date(System.currentTimeMillis() - 86400000 * 10)); // 10天前
        history.add(h3);

        PeriodHistory h4 = new PeriodHistory();
        h4.setHistoryId(java.util.UUID.randomUUID().toString());
        h4.setPeriodId(periodId);
        h4.setOperationType("CLOSE");
        h4.setOperationDesc("关闭期间");
        h4.setBeforeValue("OPEN");
        h4.setAfterValue("CLOSED");
        h4.setOperator("admin");
        h4.setOperateTime(new Date(System.currentTimeMillis() - 86400000 * 15)); // 15天前
        history.add(h4);

        return history;
    }
}
