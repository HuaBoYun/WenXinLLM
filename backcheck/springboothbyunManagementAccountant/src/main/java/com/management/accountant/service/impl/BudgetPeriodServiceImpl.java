package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetPeriod;
import com.management.accountant.oracle.entity.budget.PeriodHistory;
import com.management.accountant.oracle.mapper.budget.BudgetPeriodMapper;
import com.management.accountant.service.BudgetPeriodService;
import com.management.accountant.service.PeriodHistoryService;
import com.management.accountant.util.excel.ExcelExport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
public class BudgetPeriodServiceImpl implements BudgetPeriodService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetPeriodMapper periodMapper;

    @Resource
    private PeriodHistoryService periodHistoryService;

    /** 记录操作历史 */
    private void recordHistory(String periodId, String opType, String desc, String before, String after) {
        try {
            PeriodHistory h = new PeriodHistory();
            h.setPeriodId(periodId);
            h.setOperationType(opType);
            h.setOperationDesc(desc);
            h.setBeforeValue(before);
            h.setAfterValue(after);
            h.setOperator("system");
            h.setOperateTime(new Date());
            h.setCreateTime(new Date());
            periodHistoryService.save(h);
        } catch (Exception e) {
            log.warn("记录操作历史失败: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetPeriod create(BudgetPeriod p) {
        if (p == null) throw new ServiceException("期间信息不能为空");
        if (p.getDelFlag() == null) p.setDelFlag(0);
        if (p.getPeriodStatus() == null) p.setPeriodStatus("CLOSED");
        p.setCreateTime(new Date()); p.setUpdateTime(new Date());
        periodMapper.insert(p);
        recordHistory(p.getPeriodId(), "CREATE", "创建期间：" + p.getPeriodName(), null, p.getPeriodName());
        return p;
    }

    @Override
    public BudgetPeriod getById(String id) {
        QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
        w.eq("PERIOD_ID", id).eq("DEL_FLAG", 0L);
        return periodMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetPeriod p) {
        if (p == null || !StringUtils.hasText(p.getPeriodId())) throw new ServiceException("期间ID不能为空");
        BudgetPeriod old = getById(p.getPeriodId());
        p.setUpdateTime(new Date());
        periodMapper.updateById(p);
        recordHistory(p.getPeriodId(), "UPDATE", "更新期间",
                old != null ? old.getPeriodName() : null, p.getPeriodName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetPeriod u = new BudgetPeriod();
        u.setPeriodId(id); u.setDelFlag(1); u.setUpdateTime(new Date());
        periodMapper.updateById(u);
        recordHistory(id, "DELETE", "删除期间", null, null);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (params.get("periodName") != null && !"".equals(params.get("periodName"))) {
            w.like("PERIOD_NAME", params.get("periodName"));
        }
        if (params.get("budgetYear") != null && !"".equals(params.get("budgetYear"))) {
            w.eq("BUDGET_YEAR", Integer.parseInt(params.get("budgetYear").toString()));
        }
        if (params.get("periodType") != null && !"".equals(params.get("periodType"))) {
            w.eq("PERIOD_TYPE", params.get("periodType"));
        }
        if (params.get("periodStatus") != null && !"".equals(params.get("periodStatus"))) {
            w.eq("PERIOD_STATUS", params.get("periodStatus"));
        }
        w.orderByAsc("BUDGET_YEAR").orderByAsc("START_DATE");
        IPage<BudgetPeriod> pageResult = periodMapper.selectPage(new Page<>(pageNum, pageSize), w);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openPeriod(String id) {
        updatePeriodStatus(id, "OPEN");
        recordHistory(id, "OPEN", "开启期间", "CLOSED", "OPEN");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closePeriod(String id) {
        updatePeriodStatus(id, "CLOSED");
        recordHistory(id, "CLOSE", "关闭期间", "OPEN", "CLOSED");
    }

    private void updatePeriodStatus(String id, String status) {
        BudgetPeriod u = new BudgetPeriod();
        u.setPeriodId(id); u.setPeriodStatus(status); u.setUpdateTime(new Date());
        periodMapper.updateById(u);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLockStatus(String id, Boolean isLocked) {
        BudgetPeriod u = new BudgetPeriod();
        u.setPeriodId(id);
        u.setIsLocked(isLocked ? 1 : 0);
        u.setUpdateTime(new Date());
        periodMapper.updateById(u);
        recordHistory(id, isLocked ? "LOCK" : "UNLOCK",
                isLocked ? "锁定期间" : "解锁期间",
                isLocked ? "0" : "1",
                isLocked ? "1" : "0");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initYear(Map<String, Object> params) {
        log.info("初始化年度期间: {}", params.get("year"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeYear(Map<String, Object> params) {
        log.info("关闭年度期间: {}", params.get("year"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOpen(List<String> ids) { for (String id : ids) openPeriod(id); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchClose(List<String> ids) { for (String id : ids) closePeriod(id); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCurrent(String id) {
        // 先取消所有当前期间
        QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L).eq("IS_CURRENT", 1);
        List<BudgetPeriod> currents = periodMapper.selectList(w);
        for (BudgetPeriod p : currents) {
            BudgetPeriod u = new BudgetPeriod();
            u.setPeriodId(p.getPeriodId()); u.setIsCurrent(0); u.setUpdateTime(new Date());
            periodMapper.updateById(u);
        }
        BudgetPeriod u = new BudgetPeriod();
        u.setPeriodId(id); u.setIsCurrent(1); u.setUpdateTime(new Date());
        periodMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getStats() {
        QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        List<BudgetPeriod> allPeriods = periodMapper.selectList(w);

        Map<String, Object> stats = new HashMap<>();

        // 期间总数
        long totalPeriods = allPeriods.size();
        stats.put("totalPeriods", totalPeriods);

        // 开启期间数
        long openPeriods = allPeriods.stream()
                .filter(p -> "OPEN".equals(p.getPeriodStatus()))
                .count();
        stats.put("openPeriods", openPeriods);

        // 锁定期间数
        long lockedPeriods = allPeriods.stream()
                .filter(p -> p.getIsLocked() != null && p.getIsLocked() == 1)
                .count();
        stats.put("lockedPeriods", lockedPeriods);

        // 当前期间
        BudgetPeriod current = allPeriods.stream()
                .filter(p -> p.getIsCurrent() != null && p.getIsCurrent() == 1)
                .findFirst()
                .orElse(null);
        String currentPeriod = current != null ? current.getPeriodName() : "";
        stats.put("currentPeriod", currentPeriod);

        // 计算百分比
        double openRate = totalPeriods > 0 ? (double) openPeriods / totalPeriods * 100 : 0;
        stats.put("openRate", Math.round(openRate * 10.0) / 10.0);

        double lockedRate = totalPeriods > 0 ? (double) lockedPeriods / totalPeriods * 100 : 0;
        stats.put("lockedRate", Math.round(lockedRate * 10.0) / 10.0);

        // 当前期间进度（假设当前期间是开启的，计算已过天数占比）
        double currentProgress = 0;
        if (current != null && "OPEN".equals(current.getPeriodStatus())) {
            if (current.getStartDate() != null && current.getEndDate() != null) {
                long totalDays = (current.getEndDate().getTime() - current.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
                long elapsedDays = (new Date().getTime() - current.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
                if (totalDays > 0) {
                    currentProgress = (double) elapsedDays / totalDays * 100;
                }
            }
        }
        stats.put("currentProgress", Math.round(currentProgress * 10.0) / 10.0);

        return stats;
    }

    @Override
    public void exportPeriods(Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetPeriod> list;

            // 优先按 periodIds 导出（勾选导出）
            Object periodIdsObj = params.get("periodIds");
            if (periodIdsObj instanceof List && !((List<?>) periodIdsObj).isEmpty()) {
                List<String> periodIds = ((List<?>) periodIdsObj).stream()
                        .map(Object::toString)
                        .collect(java.util.stream.Collectors.toList());
                QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
                w.eq("DEL_FLAG", 0L).in("PERIOD_ID", periodIds);
                w.orderByAsc("BUDGET_YEAR").orderByAsc("START_DATE");
                list = periodMapper.selectList(w);
            } else {
                // 无勾选则按条件查询当前页数据
                QueryWrapper<BudgetPeriod> w = new QueryWrapper<>();
                w.eq("DEL_FLAG", 0L);
                if (params.get("periodName") != null && !"".equals(params.get("periodName"))) {
                    w.like("PERIOD_NAME", params.get("periodName"));
                }
                if (params.get("budgetYear") != null && !"".equals(params.get("budgetYear"))) {
                    w.eq("BUDGET_YEAR", Integer.parseInt(params.get("budgetYear").toString()));
                }
                if (params.get("periodType") != null && !"".equals(params.get("periodType"))) {
                    w.eq("PERIOD_TYPE", params.get("periodType"));
                }
                if (params.get("periodStatus") != null && !"".equals(params.get("periodStatus"))) {
                    w.eq("PERIOD_STATUS", params.get("periodStatus"));
                }
                // 按分页大小限制导出数量
                int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
                int pageNum  = params.get("pageNum")  != null ? Integer.parseInt(params.get("pageNum").toString())  : 1;
                w.orderByAsc("BUDGET_YEAR").orderByAsc("START_DATE");
                com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetPeriod> page =
                        periodMapper.selectPage(
                                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), w);
                list = page.getRecords();
            }

            String budgetYear = params.get("budgetYear") != null ? params.get("budgetYear").toString() : "all";
            String fileName = "预算期间_" + budgetYear + ".xlsx";
            try (ExcelExport ee = new ExcelExport("预算期间数据", BudgetPeriod.class)) {
                ee.setDataList(list).write(response, fileName);
            }
        } catch (Exception e) {
            log.error("导出期间数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }
}

