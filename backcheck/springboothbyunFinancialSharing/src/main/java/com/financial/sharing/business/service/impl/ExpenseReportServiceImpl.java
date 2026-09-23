package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblExpenseReport;
import com.financial.sharing.business.entity.TblExpenseReportDetail;
import com.financial.sharing.business.mapper.ExpenseReportMapper;
import com.financial.sharing.business.mapper.ExpenseReportDetailMapper;
import com.financial.sharing.business.service.ExpenseReportService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 报销单服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service
public class ExpenseReportServiceImpl implements ExpenseReportService {

    @Autowired
    private ExpenseReportMapper expenseReportMapper;

    @Autowired
    private ExpenseReportDetailMapper expenseReportDetailMapper;

    @Override
    public MyJsonBean<PageResult<TblExpenseReport>> getList(Map<String, Object> param) {
        try {
            log.info("查询报销单列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblExpenseReport> page = new Page<>(pageNum, pageSize);
            QueryWrapper<TblExpenseReport> queryWrapper = new QueryWrapper<>();

            // 动态添加查询条件
            if (param.get("reportCode") != null && StringUtils.hasText((String) param.get("reportCode"))) {
                queryWrapper.like("REPORT_CODE", param.get("reportCode"));
            }
            if (param.get("reportTitle") != null && StringUtils.hasText((String) param.get("reportTitle"))) {
                queryWrapper.like("REPORT_TITLE", param.get("reportTitle"));
            }
            if (param.get("applicantName") != null && StringUtils.hasText((String) param.get("applicantName"))) {
                queryWrapper.like("APPLICANT_NAME", param.get("applicantName"));
            }
            if (param.get("reportStatus") != null && StringUtils.hasText((String) param.get("reportStatus"))) {
                queryWrapper.eq("REPORT_STATUS", param.get("reportStatus"));
            }
            if (param.get("reportType") != null && StringUtils.hasText((String) param.get("reportType"))) {
                queryWrapper.eq("REPORT_TYPE", param.get("reportType"));
            }
            if (param.get("applicantDeptId") != null && StringUtils.hasText((String) param.get("applicantDeptId"))) {
                queryWrapper.eq("APPLICANT_DEPT_ID", param.get("applicantDeptId"));
            }
            if (param.get("startDate") != null && StringUtils.hasText((String) param.get("startDate"))) {
                queryWrapper.ge("CREATE_TIME", param.get("startDate"));
            }
            if (param.get("endDate") != null && StringUtils.hasText((String) param.get("endDate"))) {
                queryWrapper.le("CREATE_TIME", param.get("endDate"));
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            Page<TblExpenseReport> resultPage = expenseReportMapper.selectPage(page, queryWrapper);

            PageResult<TblExpenseReport> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) resultPage.getTotal());
            pageResult.setCurrentPage((int) resultPage.getCurrent());
            pageResult.setPageSize((int) resultPage.getSize());
            pageResult.setTotalPage((int) resultPage.getPages());
            pageResult.setTlist(resultPage.getRecords());

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询报销单列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("查询报销单详情，reportId={}", reportId);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            // 查询费用明细
            List<TblExpenseReportDetail> details = expenseReportDetailMapper.selectByReportId(reportId);
            report.setExpenseItems(details);

            return MyJsonBean.successData(report);
        } catch (Exception e) {
            log.error("查询报销单详情失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByReportCode(String reportCode) {
        try {
            if (!StringUtils.hasText(reportCode)) {
                return MyJsonBean.errorData("报销单号不能为空");
            }

            log.info("根据报销单号查询，reportCode={}", reportCode);

            TblExpenseReport report = expenseReportMapper.selectByReportCode(reportCode);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            return MyJsonBean.successData(report);
        } catch (Exception e) {
            log.error("根据报销单号查询失败，reportCode={}", reportCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblExpenseReport report) {
        try {
            log.info("保存或更新报销单，参数：{}", report);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(report.getReportId())) {
                // 新增
                report.setReportId(UUID.randomUUID().toString().replace("-", ""));
                report.setCreateTime(now);
                report.setCreateUser(report.getCreateUser() != null ? report.getCreateUser() : "system");
                report.setReportStatus("DRAFT");
                
                // 生成报销单号
                report.setReportCode("EXP" + System.currentTimeMillis());
            } else {
                // 更新
                report.setUpdateTime(now);
                report.setUpdateUser(report.getUpdateUser() != null ? report.getUpdateUser() : "system");
            }

            int result = StringUtils.hasText(report.getReportId()) && 
                expenseReportMapper.selectById(report.getReportId()) != null 
                ? expenseReportMapper.updateById(report) 
                : expenseReportMapper.insert(report);

            if (result > 0) {
                // 处理费用明细
                if (report.getExpenseItems() != null && !report.getExpenseItems().isEmpty()) {
                    expenseReportDetailMapper.deleteByReportId(report.getReportId());
                    for (TblExpenseReportDetail detail : report.getExpenseItems()) {
                        detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                        detail.setReportId(report.getReportId());
                        detail.setCreateTime(now);
                        if (detail.getExpenseDate() != null && detail.getExpenseDate().contains("T")) {
                            detail.setExpenseDate(detail.getExpenseDate().substring(0, 10));
                        }
                        expenseReportDetailMapper.insert(detail);
                    }
                    java.math.BigDecimal totalAmount = report.getExpenseItems().stream()
                        .map(TblExpenseReportDetail::getExpenseAmount)
                        .filter(Objects::nonNull)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                    TblExpenseReport updateReport = new TblExpenseReport();
                    updateReport.setReportId(report.getReportId());
                    updateReport.setTotalAmount(totalAmount);
                    updateReport.setUpdateTime(now);
                    expenseReportMapper.updateById(updateReport);
                }
                return MyJsonBean.successData("保存成功", report.getReportId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新报销单失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("删除报销单，reportId={}", reportId);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(report.getReportStatus())) {
                return MyJsonBean.errorData("只有草稿状态的报销单可以删除");
            }

            int result = expenseReportMapper.deleteById(reportId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除报销单失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> reportIds) {
        try {
            if (reportIds == null || reportIds.isEmpty()) {
                return MyJsonBean.errorData("报销单 ID 列表不能为空");
            }

            log.info("批量删除报销单，reportIds={}", reportIds);

            int count = 0;
            for (String reportId : reportIds) {
                TblExpenseReport report = expenseReportMapper.selectById(reportId);
                if (report != null && "DRAFT".equals(report.getReportStatus())) {
                    count += expenseReportMapper.deleteById(reportId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除报销单失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("提交报销单，reportId={}", reportId);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            if (!"DRAFT".equals(report.getReportStatus())) {
                return MyJsonBean.errorData("只有草稿状态的报销单可以提交");
            }

            report.setReportStatus("PENDING");

            int result = expenseReportMapper.updateById(report);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交报销单失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String reportId, String action, String opinion) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("审批报销单，reportId={}, action={}, opinion={}", reportId, action, opinion);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            if (!"PENDING".equals(report.getReportStatus())) {
                return MyJsonBean.errorData("只有待审批状态的报销单可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            report.setApproveTime(now);
            report.setApproveOpinion(opinion);

            if ("APPROVE".equals(action)) {
                report.setReportStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                report.setReportStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = expenseReportMapper.updateById(report);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批报销单失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean withdraw(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("撤回报销单，reportId={}", reportId);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            if (!"PENDING".equals(report.getReportStatus())) {
                return MyJsonBean.errorData("只有待审批状态的报销单可以撤回");
            }

            report.setReportStatus("DRAFT");

            int result = expenseReportMapper.updateById(report);
            if (result > 0) {
                return MyJsonBean.successData("撤回成功");
            } else {
                return MyJsonBean.errorData("撤回失败");
            }
        } catch (Exception e) {
            log.error("撤回报销单失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String reportId, String reportStatus) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("更新报销单状态，reportId={}, reportStatus={}", reportId, reportStatus);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            report.setReportStatus(reportStatus);

            int result = expenseReportMapper.updateById(report);
            if (result > 0) {
                return MyJsonBean.successData("状态更新成功");
            } else {
                return MyJsonBean.errorData("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新报销单状态失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean confirmPayment(String reportId, String paymentVoucherNo) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }

            log.info("确认付款，reportId={}, paymentVoucherNo={}", reportId, paymentVoucherNo);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            if (!"APPROVED".equals(report.getReportStatus())) {
                return MyJsonBean.errorData("只有已审批状态的报销单可以付款");
            }

            LocalDateTime now = LocalDateTime.now();
            report.setPaymentStatus("PAID");
            report.setPaymentTime(now);
            report.setPaymentVoucherNo(paymentVoucherNo);

            int result = expenseReportMapper.updateById(report);
            if (result > 0) {
                return MyJsonBean.successData("付款确认成功");
            } else {
                return MyJsonBean.errorData("付款确认失败");
            }
        } catch (Exception e) {
            log.error("确认付款失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("付款确认失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        try {
            log.info("获取报销单统计信息，参数：{}", param);

            Map<String, Object> statistics = new HashMap<>();

            // 构建查询条件
            QueryWrapper<TblExpenseReport> queryWrapper = new QueryWrapper<>();
            if (param.get("applicantId") != null && StringUtils.hasText((String) param.get("applicantId"))) {
                queryWrapper.eq("APPLICANT_ID", param.get("applicantId"));
            }
            if (param.get("applicantDeptId") != null && StringUtils.hasText((String) param.get("applicantDeptId"))) {
                queryWrapper.eq("APPLICANT_DEPT_ID", param.get("applicantDeptId"));
            }
            if (param.get("startDate") != null && StringUtils.hasText((String) param.get("startDate"))) {
                queryWrapper.ge("CREATE_TIME", param.get("startDate"));
            }
            if (param.get("endDate") != null && StringUtils.hasText((String) param.get("endDate"))) {
                queryWrapper.le("CREATE_TIME", param.get("endDate"));
            }

            // 总数和总金额
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(queryWrapper);
            statistics.put("totalCount", allReports.size());
            
            java.math.BigDecimal totalAmount = allReports.stream()
                .map(TblExpenseReport::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            statistics.put("totalAmount", totalAmount);

            // 按状态统计
            Map<String, Long> statusCount = allReports.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    TblExpenseReport::getReportStatus, 
                    java.util.stream.Collectors.counting()
                ));
            statistics.put("statusCount", statusCount);

            // 待审批数量
            long pendingCount = allReports.stream()
                .filter(r -> "PENDING".equals(r.getReportStatus()))
                .count();
            statistics.put("pendingCount", pendingCount);

            // 已审批数量
            long approvedCount = allReports.stream()
                .filter(r -> "APPROVED".equals(r.getReportStatus()))
                .count();
            statistics.put("approvedCount", approvedCount);

            // 已付款数量
            long paidCount = allReports.stream()
                .filter(r -> "PAID".equals(r.getPaymentStatus()))
                .count();
            statistics.put("paidCount", paidCount);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("获取报销单统计信息失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        try {
            log.info("导出报销单，参数：{}", param);

            QueryWrapper<TblExpenseReport> queryWrapper = new QueryWrapper<>();
            if (param.get("reportCode") != null && StringUtils.hasText((String) param.get("reportCode"))) {
                queryWrapper.like("REPORT_CODE", param.get("reportCode"));
            }
            if (param.get("applicantName") != null && StringUtils.hasText((String) param.get("applicantName"))) {
                queryWrapper.like("APPLICANT_NAME", param.get("applicantName"));
            }
            if (param.get("reportStatus") != null && StringUtils.hasText((String) param.get("reportStatus"))) {
                queryWrapper.eq("REPORT_STATUS", param.get("reportStatus"));
            }
            if (param.get("reportType") != null && StringUtils.hasText((String) param.get("reportType"))) {
                queryWrapper.eq("REPORT_TYPE", param.get("reportType"));
            }
            if (param.get("startDate") != null && StringUtils.hasText((String) param.get("startDate"))) {
                queryWrapper.ge("CREATE_TIME", param.get("startDate"));
            }
            if (param.get("endDate") != null && StringUtils.hasText((String) param.get("endDate"))) {
                queryWrapper.le("CREATE_TIME", param.get("endDate"));
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblExpenseReport> list = expenseReportMapper.selectList(queryWrapper);

            StringBuilder csv = new StringBuilder();
            csv.append("\uFEFF");
            csv.append("报销单号,报销标题,报销类型,申请人,部门,报销金额,状态,提交时间,审批时间,审批人,审批意见\n");

            for (TblExpenseReport report : list) {
                csv.append(escapeCsv(report.getReportCode())).append(",");
                csv.append(escapeCsv(report.getReportTitle())).append(",");
                csv.append(escapeCsv(report.getReportType())).append(",");
                csv.append(escapeCsv(report.getApplicantName())).append(",");
                csv.append(escapeCsv(report.getApplicantDeptName())).append(",");
                csv.append(report.getTotalAmount() != null ? report.getTotalAmount() : "").append(",");
                csv.append(escapeCsv(report.getReportStatus())).append(",");
                csv.append(report.getCreateTime() != null ? report.getCreateTime().toString() : "").append(",");
                csv.append(report.getApproveTime() != null ? report.getApproveTime().toString() : "").append(",");
                csv.append(escapeCsv(report.getApproverName())).append(",");
                csv.append(escapeCsv(report.getApproveOpinion())).append("\n");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("fileName", "报销单导出_" + System.currentTimeMillis() + ".csv");
            result.put("content", csv.toString());

            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            log.error("导出报销单失败", e);
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getDetails(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }
            log.info("查询报销单明细，reportId={}", reportId);
            List<TblExpenseReportDetail> details = expenseReportDetailMapper.selectByReportId(reportId);
            return MyJsonBean.successData(details);
        } catch (Exception e) {
            log.error("查询报销单明细失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveDetails(String reportId, List<Map<String, Object>> details) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }
            log.info("保存报销单明细，reportId={}，明细数量={}", reportId, details != null ? details.size() : 0);

            expenseReportDetailMapper.deleteByReportId(reportId);

            LocalDateTime now = LocalDateTime.now();
            java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

            if (details != null && !details.isEmpty()) {
                for (Map<String, Object> itemMap : details) {
                    TblExpenseReportDetail detail = new TblExpenseReportDetail();
                    detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                    detail.setReportId(reportId);
                    detail.setExpenseItemId((String) itemMap.get("expenseItemId"));
                    detail.setExpenseItemName((String) itemMap.get("expenseItemName"));
                    Object amountObj = itemMap.get("expenseAmount");
                    if (amountObj != null) {
                        detail.setExpenseAmount(new java.math.BigDecimal(amountObj.toString()));
                        totalAmount = totalAmount.add(detail.getExpenseAmount());
                    }
                    Object dateObj = itemMap.get("expenseDate");
                    if (dateObj != null) {
                        String dateStr = dateObj.toString();
                        if (dateStr.contains("T")) {
                            dateStr = dateStr.substring(0, 10);
                        }
                        detail.setExpenseDate(dateStr);
                    }
                    Object receiptObj = itemMap.get("receiptCount");
                    if (receiptObj != null) {
                        detail.setReceiptCount(Integer.valueOf(receiptObj.toString()));
                    }
                    detail.setDescription((String) itemMap.get("description"));
                    detail.setCreateTime(now);
                    expenseReportDetailMapper.insert(detail);
                }
            }

            TblExpenseReport report = new TblExpenseReport();
            report.setReportId(reportId);
            report.setTotalAmount(totalAmount);
            report.setUpdateTime(now);
            expenseReportMapper.updateById(report);

            return MyJsonBean.successData("保存明细成功");
        } catch (Exception e) {
            log.error("保存报销单明细失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean allocate(String reportId, Map<String, Object> allocateData) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }
            log.info("费用分摊，reportId={}，allocateData={}", reportId, allocateData);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            String allocateInfo = "费用分摊记录：" + allocateData.toString();
            report.setRemark((report.getRemark() != null ? report.getRemark() + "; " : "") + allocateInfo);
            report.setUpdateTime(LocalDateTime.now());
            expenseReportMapper.updateById(report);

            return MyJsonBean.successData("分摊成功");
        } catch (Exception e) {
            log.error("费用分摊失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("分摊失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean print(String reportId) {
        try {
            if (!StringUtils.hasText(reportId)) {
                return MyJsonBean.errorData("报销单 ID 不能为空");
            }
            log.info("打印报销单，reportId={}", reportId);

            TblExpenseReport report = expenseReportMapper.selectById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报销单不存在");
            }

            List<TblExpenseReportDetail> details = expenseReportDetailMapper.selectByReportId(reportId);
            report.setExpenseItems(details);

            return MyJsonBean.successData(report);
        } catch (Exception e) {
            log.error("打印报销单失败，reportId={}", reportId, e);
            return MyJsonBean.errorData("打印失败：" + e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
