package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.budget.BudgetPeriod;
import com.management.accountant.service.BudgetPeriodService;
import com.management.accountant.service.PeriodReportService;
import com.management.accountant.util.excel.ExcelExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
public class PeriodReportServiceImpl implements PeriodReportService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetPeriodService periodService;

    @Override
    public Map<String, Object> getReportData(String periodId) {
        BudgetPeriod period = periodService.getById(periodId);

        Map<String, Object> reportData = new HashMap<>();

        // 统计数据（模拟）
        Random random = new Random();
        int budgetTaskCount = 10 + random.nextInt(20);
        double budgetAmount = (100000 + random.nextInt(500000)) * 1.0;
        double executionAmount = budgetAmount * (0.7 + random.nextDouble() * 0.25);
        double executionRate = (executionAmount / budgetAmount) * 100;
        int adjustmentCount = random.nextInt(10);

        reportData.put("budgetTaskCount", budgetTaskCount);
        reportData.put("budgetAmount", budgetAmount);
        reportData.put("executionAmount", executionAmount);
        reportData.put("executionRate", Math.round(executionRate * 10.0) / 10.0);
        reportData.put("adjustmentCount", adjustmentCount);

        // 期间基本信息
        if (period != null) {
            reportData.put("periodInfo", period);
        }

        return reportData;
    }

    @Override
    public Map<String, Object> getDetailPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 模拟详细报告数据
        List<Map<String, Object>> detailList = new ArrayList<>();
        Random random = new Random();
        String[] accounts = {"6001", "6002", "6003", "6101", "6102", "6201", "6202", "6301", "6302", "6401"};
        String[] accountNames = {
            "管理费用-办公费", "管理费用-差旅费", "管理费用-会议费",
            "销售费用-广告费", "销售费用-业务招待费",
            "财务费用-利息支出", "财务费用-手续费",
            "研发费用-材料费", "研发费用-测试费",
            "其他费用-咨询费"
        };

        for (int i = 0; i < 30; i++) {
            Map<String, Object> item = new HashMap<>();
            double budgetAmount = (10000 + random.nextInt(90000)) * 1.0;
            double executionAmount = budgetAmount * (0.8 + random.nextDouble() * 0.3);
            double variance = executionAmount - budgetAmount;
            double varianceRate = (variance / budgetAmount) * 100;

            item.put("accountCode", accounts[i % accounts.length] + String.format("%02d", i / 10 + 1));
            item.put("accountName", accountNames[i % accountNames.length]);
            item.put("budgetAmount", budgetAmount);
            item.put("executionAmount", executionAmount);
            item.put("variance", variance);
            item.put("varianceRate", Math.round(varianceRate * 100.0) / 100.0);
            detailList.add(item);
        }

        // 分页
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, detailList.size());
        List<Map<String, Object>> pageList = detailList.subList(fromIndex, toIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageList);
        result.put("total", detailList.size());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public void exportReport(String periodId, HttpServletResponse response) {
        try {
            BudgetPeriod period = periodService.getById(periodId);
            if (period != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("periodId", periodId);
                params.put("pageNum", 1);
                params.put("pageSize", 10000); // 导出所有数据

                Map<String, Object> reportData = getDetailPage(params);
                List<Map<String, Object>> detailList = (List<Map<String, Object>>) reportData.get("records");

                // 创建临时表格数据
                List<List<Object>> exportData = new ArrayList<>();
                // 表头
                exportData.add(Arrays.asList("科目编码", "科目名称", "预算金额", "执行金额", "差异", "差异率"));
                // 数据行
                for (Map<String, Object> item : detailList) {
                    exportData.add(Arrays.asList(
                        item.get("accountCode"),
                        item.get("accountName"),
                        formatAmount((Double) item.get("budgetAmount")),
                        formatAmount((Double) item.get("executionAmount")),
                        formatAmount((Double) item.get("variance")),
                        item.get("varianceRate") + "%"
                    ));
                }

                // 使用简单的Excel导出
                List<String> headers = Arrays.asList("科目编码", "科目名称", "预算金额", "执行金额", "差异", "差异率");
                List<Integer> widths = Arrays.asList(4000, 6000, 5000, 5000, 5000, 4000);

                new ExcelExport("期间报告", headers, widths)
                        .setDataList(exportData)
                        .write(response, "期间报告_" + periodId + ".xlsx");
            }
        } catch (Exception e) {
            log.error("导出期间报告异常", e);
        }
    }

    private String formatAmount(double amount) {
        return String.format("%,.2f", amount);
    }
}
