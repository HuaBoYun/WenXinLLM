package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.mapper.GzctEnterpriseDataBackupMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "企业数据管理", description = "数据录入、数据质量")
@RestController
@RequestMapping("/v1/enterprise/data")
@Slf4j
public class EnterpriseDataController {

    @Autowired private GzctEnterpriseDataEntryMapper dataEntryMapper;
    @Autowired private GzctEnterpriseDataQualityMapper dataQualityMapper;
    @Autowired private GzctEnterpriseDataBackupMapper dataBackupMapper;

    // ==================== 数据录入 ====================
    @Operation(summary = "entryList")
    @PostMapping("/entry/list")
    public R<PageResult<GzctEnterpriseDataEntry>> entryList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("dataType") != null && StringUtils.isNotBlank(params.get("dataType").toString())) w.eq(GzctEnterpriseDataEntry::getDataType, params.get("dataType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctEnterpriseDataEntry::getStatus, params.get("status").toString());
            if (params.get("entryStatus") != null && StringUtils.isNotBlank(params.get("entryStatus").toString())) w.eq(GzctEnterpriseDataEntry::getStatus, params.get("entryStatus").toString());
            if (params.get("submitter") != null && StringUtils.isNotBlank(params.get("submitter").toString())) w.like(GzctEnterpriseDataEntry::getSubmitter, params.get("submitter").toString());
            if (params.get("auditor") != null && StringUtils.isNotBlank(params.get("auditor").toString())) w.like(GzctEnterpriseDataEntry::getAuditor, params.get("auditor").toString());
            // 日期范围筛选（录入时间）
            if (params.get("entryDateStart") != null && StringUtils.isNotBlank(params.get("entryDateStart").toString())) {
                w.ge(GzctEnterpriseDataEntry::getCreateTime, LocalDateTime.parse(params.get("entryDateStart").toString() + "T00:00:00"));
            }
            if (params.get("entryDateEnd") != null && StringUtils.isNotBlank(params.get("entryDateEnd").toString())) {
                w.le(GzctEnterpriseDataEntry::getCreateTime, LocalDateTime.parse(params.get("entryDateEnd").toString() + "T23:59:59"));
            }
            w.orderByDesc(GzctEnterpriseDataEntry::getCreateTime);
            Page<GzctEnterpriseDataEntry> page = new Page<>(pn, ps);
            Page<GzctEnterpriseDataEntry> r = dataEntryMapper.selectPage(page, w);
            int total = (int) r.getTotal();
            if (total == 0 && r.getRecords() != null && !r.getRecords().isEmpty()) {
                total = dataEntryMapper.selectCount(w).intValue();
            }
            PageResult<GzctEnterpriseDataEntry> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage(total > 0 ? (int) Math.ceil((double) total / ps) : 0); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "entryDetail")
    @GetMapping("/entry/{id}")
    public R<GzctEnterpriseDataEntry> entryDetail(@PathVariable String id) { try { return R.success(dataEntryMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/entry/add")
    public R<Boolean> addEntry(@RequestBody GzctEnterpriseDataEntry record) { try { record.setCreateTime(LocalDateTime.now()); record.setStatus("草稿"); dataEntryMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/entry/update")
    public R<Boolean> updateEntry(@RequestBody GzctEnterpriseDataEntry record) { try { record.setUpdateTime(LocalDateTime.now()); dataEntryMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/entry/{id}")
    public R<Boolean> deleteEntry(@PathVariable String id) {
        try {
            // 1. 检查记录是否存在
            GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }

            // 2. 检查状态,只允许删除草稿状态的数据
            if (!"草稿".equals(record.getStatus()) && !"DRAFT".equals(record.getStatus())) {
                return R.fail("只能删除草稿状态的数据,当前状态: " + record.getStatus());
            }

            // 3. 执行删除
            int result = dataEntryMapper.deleteById(id);
            if (result > 0) {
                log.info("删除数据录入成功, ID: {}, 企业: {}", id, record.getEnterpriseName());
                return R.success(true);
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("删除数据录入失败, ID: {}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @PostMapping("/entry/batch-delete")
    public R<Map<String, Object>> batchDeleteEntry(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }

            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();

            for (String id : ids) {
                try {
                    GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
                    if (record == null) {
                        failCount++;
                        errors.add("记录不存在: " + id);
                        continue;
                    }
                    if (!"草稿".equals(record.getStatus()) && !"DRAFT".equals(record.getStatus())) {
                        failCount++;
                        errors.add("只能删除草稿状态: " + record.getEnterpriseName());
                        continue;
                    }
                    dataEntryMapper.deleteById(id);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("删除失败: " + e.getMessage());
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            log.info("批量删除完成, 成功: {}, 失败: {}", successCount, failCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @PostMapping("/entry/batch-submit")
    public R<Map<String, Object>> batchSubmitEntry(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要提交的数据");
            }

            int successCount = 0;
            int failCount = 0;

            for (String id : ids) {
                try {
                    GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
                    if (record != null && ("草稿".equals(record.getStatus()) || "DRAFT".equals(record.getStatus()))) {
                        record.setStatus("已提交");
                        record.setSubmitTime(LocalDateTime.now());
                        record.setUpdateTime(LocalDateTime.now());
                        dataEntryMapper.updateById(record);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            log.info("批量提交完成, 成功: {}, 失败: {}", successCount, failCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量提交失败", e);
            return R.fail("批量提交失败：" + e.getMessage());
        }
    }

    @Operation(summary = "提交")
    @PostMapping("/entry/submit")
    public R<Boolean> submitEntry(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctEnterpriseDataEntry r = dataEntryMapper.selectById(id);
            if (r == null) return R.fail("记录不存在");
            if (!"草稿".equals(r.getStatus()) && !"已退回".equals(r.getStatus())) {
                return R.fail("只有草稿或已退回状态可以提交，当前状态：" + r.getStatus());
            }
            r.setStatus("已提交");
            r.setSubmitTime(LocalDateTime.now());
            r.setUpdateTime(LocalDateTime.now());
            dataEntryMapper.updateById(r);
            return R.success(true);
        } catch (Exception e) {
            return R.fail("提交失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审批")
    @PostMapping("/entry/audit")
    public R<Boolean> auditEntry(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String result = (String) params.get("result");
            String remark = (String) params.get("remark");
            GzctEnterpriseDataEntry r = dataEntryMapper.selectById(id);
            if (r == null) return R.fail("记录不存在");
            if (!"已提交".equals(r.getStatus())) {
                return R.fail("只有已提交状态可以审核，当前状态：" + r.getStatus());
            }
            r.setStatus("approved".equals(result) ? "已审核" : "已退回");
            r.setAuditor((String) params.get("auditor"));
            r.setAuditTime(LocalDateTime.now());
            r.setUpdateTime(LocalDateTime.now());
            if (StringUtils.isNotBlank(remark)) r.setRemark(remark);
            dataEntryMapper.updateById(r);
            return R.success(true);
        } catch (Exception e) {
            return R.fail("审核失败：" + e.getMessage());
        }
    }

    @Operation(summary = "withdrawEntry")
    @PostMapping("/entry/withdraw")
    public R<Boolean> withdrawEntry(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctEnterpriseDataEntry r = dataEntryMapper.selectById(id);
            if (r == null) return R.fail("记录不存在");
            if (!"已提交".equals(r.getStatus())) {
                return R.fail("只有已提交状态可以撤回，当前状态：" + r.getStatus());
            }
            r.setStatus("草稿");
            r.setSubmitTime(null);
            r.setUpdateTime(LocalDateTime.now());
            dataEntryMapper.updateById(r);
            return R.success(true);
        } catch (Exception e) {
            return R.fail("撤回失败：" + e.getMessage());
        }
    }

    // ==================== 报送进度 ====================
    @Operation(summary = "查询报送进度")
    @PostMapping("/entry/progress")
    public R<Map<String, Object>> getSubmissionProgress(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> result = new HashMap<>();

            if (StringUtils.isNotBlank(id)) {
                GzctEnterpriseDataEntry entry = dataEntryMapper.selectById(id);
                if (entry == null) return R.fail("记录不存在");

                List<Map<String, Object>> steps = new ArrayList<>();
                Map<String, Object> step1 = new HashMap<>();
                step1.put("step", 1); step1.put("name", "数据录入"); step1.put("status", "completed");
                step1.put("time", entry.getCreateTime() != null ? entry.getCreateTime().toString() : "");
                steps.add(step1);

                Map<String, Object> step2 = new HashMap<>();
                step2.put("step", 2); step2.put("name", "数据提交");
                step2.put("status", entry.getSubmitTime() != null ? "completed" : "pending");
                step2.put("time", entry.getSubmitTime() != null ? entry.getSubmitTime().toString() : "");
                steps.add(step2);

                Map<String, Object> step3 = new HashMap<>();
                step3.put("step", 3); step3.put("name", "数据审核");
                step3.put("status", "已审核".equals(entry.getStatus()) ? "completed" : "已提交".equals(entry.getStatus()) ? "in_progress" : "pending");
                step3.put("time", entry.getAuditTime() != null ? entry.getAuditTime().toString() : "");
                steps.add(step3);

                Map<String, Object> step4 = new HashMap<>();
                step4.put("step", 4); step4.put("name", "审核完成");
                step4.put("status", "已审核".equals(entry.getStatus()) ? "completed" : "pending");
                step4.put("time", "已审核".equals(entry.getStatus()) && entry.getAuditTime() != null ? entry.getAuditTime().toString() : "");
                steps.add(step4);

                result.put("steps", steps);
                result.put("currentStep", "已审核".equals(entry.getStatus()) ? 4 : "已提交".equals(entry.getStatus()) ? 3 : entry.getSubmitTime() != null ? 2 : 1);
                result.put("status", entry.getStatus());
            } else {
                LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
                long total = dataEntryMapper.selectCount(w);
                LambdaQueryWrapper<GzctEnterpriseDataEntry> sw = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(enterpriseId)) sw.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
                sw.in(GzctEnterpriseDataEntry::getStatus, Arrays.asList("已提交", "已审核"));
                long completed = dataEntryMapper.selectCount(sw);
                result.put("totalTasks", total);
                result.put("completedTasks", completed);
                result.put("pendingTasks", total - completed);
                result.put("completionRate", total > 0 ? Math.round((double) completed / total * 100) : 0);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询进度失败：" + e.getMessage()); }
    }

    @Operation(summary = "查询报送日志")
    @PostMapping("/entry/progress/logs")
    public R<PageResult<Map<String, Object>>> getProgressLogs(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w.isNotNull(GzctEnterpriseDataEntry::getSubmitTime);
            w.orderByDesc(GzctEnterpriseDataEntry::getUpdateTime);
            Page<GzctEnterpriseDataEntry> page = new Page<>(pn, ps);
            Page<GzctEnterpriseDataEntry> r = dataEntryMapper.selectPage(page, w);

            List<Map<String, Object>> logs = new ArrayList<>();
            for (GzctEnterpriseDataEntry entry : r.getRecords()) {
                Map<String, Object> logItem = new HashMap<>();
                logItem.put("id", entry.getId());
                logItem.put("enterpriseName", entry.getEnterpriseName());
                logItem.put("dataType", entry.getDataType());
                logItem.put("action", "已审核".equals(entry.getStatus()) ? "审核通过" : "已退回".equals(entry.getStatus()) ? "审核退回" : "提交报送");
                logItem.put("operator", entry.getAuditor() != null ? entry.getAuditor() : entry.getSubmitter());
                logItem.put("operateTime", entry.getUpdateTime() != null ? entry.getUpdateTime().toString() : "");
                logItem.put("status", entry.getStatus());
                logItem.put("remark", entry.getRemark());
                logs.add(logItem);
            }

            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) r.getTotal() > 0 ? (int) Math.ceil((double) r.getTotal() / ps) : 0);
            pr.setPageSize(ps); pr.setTlist(logs);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询日志失败：" + e.getMessage()); }
    }

    @Operation(summary = "下载报送日志")
    @GetMapping("/entry/progress/logs/download/{id}")
    public void downloadProgressLog(@PathVariable String id, HttpServletResponse response) {
        try {
            GzctEnterpriseDataEntry entry = dataEntryMapper.selectById(id);
            if (entry == null) { response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"result\":500,\"msg\":\"记录不存在\"}"); return; }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=progress_log_" + id + ".xls");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("报送日志");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "数据类型", "操作", "操作人", "操作时间", "状态", "备注"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

            org.apache.poi.ss.usermodel.Row row = sheet.createRow(1);
            row.createCell(0).setCellValue(entry.getEnterpriseName() != null ? entry.getEnterpriseName() : "");
            row.createCell(1).setCellValue(entry.getDataType() != null ? entry.getDataType() : "");
            row.createCell(2).setCellValue("报送");
            row.createCell(3).setCellValue(entry.getSubmitter() != null ? entry.getSubmitter() : "");
            row.createCell(4).setCellValue(entry.getSubmitTime() != null ? entry.getSubmitTime().toString() : "");
            row.createCell(5).setCellValue(entry.getStatus() != null ? entry.getStatus() : "");
            row.createCell(6).setCellValue(entry.getRemark() != null ? entry.getRemark() : "");

            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("下载报送日志失败", e); }
    }


    @Operation(summary = "删除")
    @DeleteMapping("/submission/{id}")
    public R<Boolean> deleteSubmission(@PathVariable String id) {
        try {
            GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }
            if (!"草稿".equals(record.getStatus()) && !"DRAFT".equals(record.getStatus()) && !"PENDING".equals(record.getStatus())) {
                return R.fail("只能删除草稿/待报送状态的数据");
            }
            int result = dataEntryMapper.deleteById(id);
            if (result > 0) {
                log.info("删除报送任务成功, ID: {}", id);
                return R.success(true);
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("删除报送任务失败, ID: {}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/audit/{id}")
    public R<Boolean> deleteAudit(@PathVariable String id) {
        try {
            GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }
            if (!"草稿".equals(record.getStatus()) && !"DRAFT".equals(record.getStatus()) && !"PENDING".equals(record.getStatus())) {
                return R.fail("只能删除草稿/待审核状态的数据");
            }
            int result = dataEntryMapper.deleteById(id);
            if (result > 0) {
                log.info("删除审核记录成功, ID: {}", id);
                return R.success(true);
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("删除审核记录失败, ID: {}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/audit/workflow/{id}")
    public R<Map<String, Object>> getAuditWorkflow(@PathVariable String id) {
        try {
            Map<String, Object> result = new HashMap<>();
            GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }

            List<Map<String, Object>> workflow = new ArrayList<>();
            Map<String, Object> step1 = new HashMap<>();
            step1.put("step", 1);
            step1.put("name", "提交审核");
            step1.put("status", "completed");
            step1.put("time", record.getSubmitTime() != null ? record.getSubmitTime().toString() : "");
            workflow.add(step1);

            Map<String, Object> step2 = new HashMap<>();
            step2.put("step", 2);
            step2.put("name", "初审");
            step2.put("status", "in_progress");
            step2.put("time", "");
            workflow.add(step2);

            Map<String, Object> step3 = new HashMap<>();
            step3.put("step", 3);
            step3.put("name", "复审");
            step3.put("status", "pending");
            step3.put("time", "");
            workflow.add(step3);

            result.put("workflow", workflow);
            result.put("currentStep", 2);
            return R.success(result);
        } catch (Exception e) {
            return R.fail("获取审核流程失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/audit/history/{id}")
    public R<List<Map<String, Object>>> getAuditHistory(@PathVariable String id) {
        try {
            GzctEnterpriseDataEntry entry = dataEntryMapper.selectById(id);
            if (entry == null) return R.fail("记录不存在");

            List<Map<String, Object>> history = new ArrayList<>();

            // 根据记录的实际状态和时间构建审核历史
            // 1. 创建记录
            if (entry.getCreateTime() != null) {
                Map<String, Object> createRecord = new HashMap<>();
                createRecord.put("action", "创建录入");
                createRecord.put("auditor", entry.getSubmitter() != null ? entry.getSubmitter() : "系统");
                createRecord.put("auditTime", entry.getCreateTime().toString());
                createRecord.put("result", "创建");
                createRecord.put("remark", "数据录入创建");
                history.add(createRecord);
            }

            // 2. 提交记录
            if (entry.getSubmitTime() != null) {
                Map<String, Object> submitRecord = new HashMap<>();
                submitRecord.put("action", "提交报送");
                submitRecord.put("auditor", entry.getSubmitter() != null ? entry.getSubmitter() : "-");
                submitRecord.put("auditTime", entry.getSubmitTime().toString());
                submitRecord.put("result", "已提交");
                submitRecord.put("remark", "数据提交至审核流程");
                history.add(submitRecord);
            }

            // 3. 审核记录
            if (entry.getAuditTime() != null) {
                Map<String, Object> auditRecord = new HashMap<>();
                auditRecord.put("action", "已退回".equals(entry.getStatus()) ? "审核拒绝" : "审核通过");
                auditRecord.put("auditor", entry.getAuditor() != null ? entry.getAuditor() : "-");
                auditRecord.put("auditTime", entry.getAuditTime().toString());
                auditRecord.put("result", entry.getStatus());
                auditRecord.put("remark", entry.getRemark() != null ? entry.getRemark() : "");
                history.add(auditRecord);
            }

            return R.success(history);
        } catch (Exception e) {
            return R.fail("获取审核历史失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @PostMapping("/audit/batch-audit")
    public R<Map<String, Object>> batchAuditEntry(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            String result = (String) params.get("result");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要审核的数据");
            }

            int successCount = 0;
            int failCount = 0;

            for (String id : ids) {
                try {
                    GzctEnterpriseDataEntry record = dataEntryMapper.selectById(id);
                    if (record != null) {
                        record.setStatus("approved".equals(result) ? "已审核" : "已退回");
                        record.setAuditTime(LocalDateTime.now());
                        record.setUpdateTime(LocalDateTime.now());
                        dataEntryMapper.updateById(record);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }

            Map<String, Object> auditResult = new HashMap<>();
            auditResult.put("successCount", successCount);
            auditResult.put("failCount", failCount);
            log.info("批量审核完成, 成功: {}, 失败: {}", successCount, failCount);
            return R.success(auditResult);
        } catch (Exception e) {
            log.error("批量审核失败", e);
            return R.fail("批量审核失败：" + e.getMessage());
        }
    }
    @Operation(summary = "导出")
    @GetMapping("/entry/export")
    public void exportEntry(HttpServletResponse response) {
        try {
            List<GzctEnterpriseDataEntry> list = dataEntryMapper.selectList(
                new LambdaQueryWrapper<GzctEnterpriseDataEntry>().orderByDesc(GzctEnterpriseDataEntry::getCreateTime)
            );
            if (list == null) list = new ArrayList<>();

            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("数据录入_" + System.currentTimeMillis() + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            java.io.OutputStream os = response.getOutputStream();
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);

            String[] headers = {"企业名称", "数据类型", "数据类别", "报告期间", "报告年度", "状态", "录入人", "质量评分", "备注", "创建时间"};
            writer.println(String.join(",", headers));

            for (GzctEnterpriseDataEntry item : list) {
                String[] row = {
                    escapeCsv(item.getEnterpriseName()),
                    escapeCsv(item.getDataType()),
                    escapeCsv(item.getDataCategory()),
                    escapeCsv(item.getReportPeriod()),
                    escapeCsv(item.getReportYear()),
                    escapeCsv(item.getStatus()),
                    escapeCsv(item.getSubmitter()),
                    item.getQualityScore() != null ? item.getQualityScore().toString() : "0",
                    escapeCsv(item.getRemark()),
                    item.getCreateTime() != null ? item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""
                };
                writer.println(String.join(",", row));
            }
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
                }
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
    @Operation(summary = "")
    @GetMapping("/entry/statistics")
    public R<Map<String, Object>> entryStatistics(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) {
                w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            }
            result.put("totalEntries", dataEntryMapper.selectCount(w));

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w2 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w2.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w2.eq(GzctEnterpriseDataEntry::getStatus, "已提交");
            result.put("submittedCount", dataEntryMapper.selectCount(w2));

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w3 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w3.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w3.eq(GzctEnterpriseDataEntry::getStatus, "已审核");
            result.put("auditedCount", dataEntryMapper.selectCount(w3));

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w4 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w4.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w4.eq(GzctEnterpriseDataEntry::getStatus, "已退回");
            result.put("rejectedCount", dataEntryMapper.selectCount(w4));

            return R.success(result);
        } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getTypeDistribution(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String dataType) {
        try {
            List<Map<String, Object>> distribution = new ArrayList<>();
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) {
                w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            }
            if (StringUtils.isNotBlank(dataType)) {
                w.eq(GzctEnterpriseDataEntry::getDataType, dataType);
            }
            List<GzctEnterpriseDataEntry> list = dataEntryMapper.selectList(w);

            Map<String, Long> typeCount = new HashMap<>();
            for (GzctEnterpriseDataEntry item : list) {
                String type = item.getDataType() != null ? item.getDataType() : "未分类";
                typeCount.put(type, typeCount.getOrDefault(type, 0L) + 1);
            }

            for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                distribution.add(item);
            }
            return R.success(distribution);
        } catch (Exception e) {
            return R.fail("获取类型分布失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/trend")
    public R<List<Map<String, Object>>> getDataTrend(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String dataType,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> trend = new ArrayList<>();
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) {
                w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            }
            if (StringUtils.isNotBlank(dataType)) {
                w.eq(GzctEnterpriseDataEntry::getDataType, dataType);
            }
            if (StringUtils.isNotBlank(startDate)) {
                w.ge(GzctEnterpriseDataEntry::getCreateTime, LocalDateTime.parse(startDate + "T00:00:00"));
            }
            if (StringUtils.isNotBlank(endDate)) {
                w.le(GzctEnterpriseDataEntry::getCreateTime, LocalDateTime.parse(endDate + "T23:59:59"));
            }
            w.orderByAsc(GzctEnterpriseDataEntry::getCreateTime);
            List<GzctEnterpriseDataEntry> list = dataEntryMapper.selectList(w);

            // 根据period参数决定分组粒度
            String pattern;
            if ("daily".equals(period)) {
                pattern = "yyyy-MM-dd";
            } else if ("weekly".equals(period)) {
                pattern = "yyyy-ww"; // ISO周
            } else if ("yearly".equals(period)) {
                pattern = "yyyy";
            } else {
                pattern = "yyyy-MM"; // 默认月
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            Map<String, Long> periodCount = new LinkedHashMap<>();
            for (GzctEnterpriseDataEntry item : list) {
                if (item.getCreateTime() != null) {
                    String key = item.getCreateTime().format(formatter);
                    periodCount.put(key, periodCount.getOrDefault(key, 0L) + 1);
                }
            }

            for (Map.Entry<String, Long> entry : periodCount.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", entry.getKey());
                item.put("count", entry.getValue());
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            return R.fail("获取数据趋势失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/quality-trend")
    public R<List<Map<String, Object>>> getQualityTrend(@RequestParam(required = false) String enterpriseId) {
        try {
            List<Map<String, Object>> trend = new ArrayList<>();
            LambdaQueryWrapper<GzctEnterpriseDataQuality> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) {
                w.eq(GzctEnterpriseDataQuality::getEnterpriseId, enterpriseId);
            }
            w.orderByAsc(GzctEnterpriseDataQuality::getCreateTime);
            List<GzctEnterpriseDataQuality> list = dataQualityMapper.selectList(w);

            Map<String, Map<String, Long>> monthStats = new LinkedHashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            for (GzctEnterpriseDataQuality item : list) {
                if (item.getCreateTime() != null) {
                    String month = item.getCreateTime().format(formatter);
                    if (!monthStats.containsKey(month)) {
                        Map<String, Long> stats = new HashMap<>();
                        stats.put("pass", 0L);
                        stats.put("fail", 0L);
                        monthStats.put(month, stats);
                    }
                    if ("通过".equals(item.getCheckResult())) {
                        monthStats.get(month).put("pass", monthStats.get(month).get("pass") + 1);
                    } else {
                        monthStats.get(month).put("fail", monthStats.get(month).get("fail") + 1);
                    }
                }
            }

            for (Map.Entry<String, Map<String, Long>> entry : monthStats.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", entry.getKey());
                item.put("pass", entry.getValue().get("pass"));
                item.put("fail", entry.getValue().get("fail"));
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            return R.fail("获取质量趋势失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/dashboard")
    public R<Map<String, Object>> getDashboardStatistics(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> result = new HashMap<>();

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w1 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w1.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            result.put("totalEntries", dataEntryMapper.selectCount(w1));

            LambdaQueryWrapper<GzctEnterpriseDataQuality> w2 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w2.eq(GzctEnterpriseDataQuality::getEnterpriseId, enterpriseId);
            long total = dataQualityMapper.selectCount(w2);

            LambdaQueryWrapper<GzctEnterpriseDataQuality> w3 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w3.eq(GzctEnterpriseDataQuality::getEnterpriseId, enterpriseId);
            w3.eq(GzctEnterpriseDataQuality::getCheckResult, "通过");
            long passCount = dataQualityMapper.selectCount(w3);

            result.put("qualityScore", total > 0 ? Math.round((double) passCount / total * 1000.0) / 10.0 : 0);

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w4 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w4.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w4.eq(GzctEnterpriseDataEntry::getStatus, "已提交");
            long submittedCount = dataEntryMapper.selectCount(w4);
            long totalEntries = dataEntryMapper.selectCount(w1);
            result.put("submissionRate", totalEntries > 0 ? Math.round((double) submittedCount / totalEntries * 100) : 0);

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w5 = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w5.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w5.eq(GzctEnterpriseDataEntry::getStatus, "已提交");
            result.put("pendingAudits", dataEntryMapper.selectCount(w5));

            return R.success(result);
        } catch (Exception e) {
            return R.fail("获取仪表盘统计失败：" + e.getMessage());
        }
    }

    // ==================== 数据备份 ====================
    @Operation(summary = "备份列表")
    @PostMapping("/backup/list")
    public R<PageResult<GzctEnterpriseDataBackup>> backupList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseDataBackup> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseDataBackup::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("backupType") != null && StringUtils.isNotBlank(params.get("backupType").toString())) {
                w.eq(GzctEnterpriseDataBackup::getBackupType, params.get("backupType").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctEnterpriseDataBackup::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctEnterpriseDataBackup::getBackupTime);
            Page<GzctEnterpriseDataBackup> page = new Page<>(pn, ps);
            Page<GzctEnterpriseDataBackup> r = dataBackupMapper.selectPage(page, w);
            int total = (int) r.getTotal();
            PageResult<GzctEnterpriseDataBackup> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent());
            pr.setTotalPage(total > 0 ? (int) Math.ceil((double) total / ps) : 0); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) {
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建备份")
    @PostMapping("/backup/create")
    public R<GzctEnterpriseDataBackup> createBackup(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            String enterpriseName = (String) params.get("enterpriseName");
            String backupType = params.get("backupType") != null ? params.get("backupType").toString() : "full";
            String remark = (String) params.get("remark");

            // 统计当前企业数据量
            LambdaQueryWrapper<GzctEnterpriseDataEntry> countW = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) countW.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            long recordCount = dataEntryMapper.selectCount(countW);

            GzctEnterpriseDataBackup backup = new GzctEnterpriseDataBackup();
            backup.setEnterpriseId(enterpriseId);
            backup.setEnterpriseName(enterpriseName);
            backup.setBackupName((enterpriseName != null ? enterpriseName : "企业") + "_" + backupType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            backup.setBackupType(backupType);
            backup.setDataSource("GZCT_ENTERPRISE_DATA_ENTRY");
            backup.setStatus("success");
            backup.setBackupSize(new BigDecimal(String.valueOf(Math.max(0.1, recordCount * 0.02))));
            backup.setRecordCount((int) recordCount);
            backup.setBackupTime(LocalDateTime.now());
            backup.setExpireTime(LocalDateTime.now().plusDays(90));
            backup.setOperator((String) params.get("operator"));
            backup.setRemark(remark);
            backup.setCreateTime(LocalDateTime.now());

            dataBackupMapper.insert(backup);
            log.info("创建备份成功, 企业: {}, 类型: {}, 记录数: {}", enterpriseId, backupType, recordCount);
            return R.success(backup);
        } catch (Exception e) {
            return R.fail("创建备份失败：" + e.getMessage());
        }
    }

    @Operation(summary = "恢复备份")
    @PostMapping("/backup/restore/{id}")
    public R<Boolean> restoreBackup(@PathVariable String id) {
        try {
            GzctEnterpriseDataBackup backup = dataBackupMapper.selectById(id);
            if (backup == null) return R.fail("备份记录不存在");
            if (!"success".equals(backup.getStatus())) return R.fail("只能恢复成功状态的备份");
            backup.setStatus("restored");
            backup.setUpdateTime(LocalDateTime.now());
            dataBackupMapper.updateById(backup);
            log.info("恢复备份成功: {}", id);
            return R.success(true);
        } catch (Exception e) {
            return R.fail("恢复备份失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除备份")
    @DeleteMapping("/backup/{id}")
    public R<Boolean> deleteBackup(@PathVariable String id) {
        try {
            GzctEnterpriseDataBackup backup = dataBackupMapper.selectById(id);
            if (backup == null) return R.fail("备份记录不存在");
            dataBackupMapper.deleteById(id);
            log.info("删除备份成功: {}", id);
            return R.success(true);
        } catch (Exception e) {
            return R.fail("删除备份失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出备份记录")
    @GetMapping("/backup/export")
    public void exportBackup(@RequestParam(required = false) String enterpriseId, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseDataBackup> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataBackup::getEnterpriseId, enterpriseId);
            w.orderByDesc(GzctEnterpriseDataBackup::getBackupTime);
            List<GzctEnterpriseDataBackup> list = dataBackupMapper.selectList(w);
            if (list == null) list = new ArrayList<>();

            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("备份记录_" + System.currentTimeMillis() + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            java.io.OutputStream os = response.getOutputStream();
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);

            writer.println("备份ID,备份名称,备份类型,数据源,记录数,备份大小(MB),状态,备份时间,过期时间,操作人,备注");
            for (GzctEnterpriseDataBackup item : list) {
                String[] row = {
                    escapeCsv(item.getId() != null ? item.getId() : ""),
                    escapeCsv(item.getBackupName()),
                    escapeCsv(item.getBackupType()),
                    escapeCsv(item.getDataSource()),
                    item.getRecordCount() != null ? item.getRecordCount().toString() : "0",
                    item.getBackupSize() != null ? item.getBackupSize().toString() : "0",
                    escapeCsv(item.getStatus()),
                    item.getBackupTime() != null ? item.getBackupTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "",
                    item.getExpireTime() != null ? item.getExpireTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "",
                    escapeCsv(item.getOperator()),
                    escapeCsv(item.getRemark())
                };
                writer.println(String.join(",", row));
            }
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出备份记录失败", e);
            try { if (!response.isCommitted()) { response.reset(); response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"result\":500,\"msg\":\"导出失败\"}"); } } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }

    // ==================== 数据质量 ====================
    @Operation(summary = "qualityList")
    @PostMapping("/quality/list")
    public R<PageResult<GzctEnterpriseDataQuality>> qualityList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseDataQuality> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctEnterpriseDataQuality::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("checkType") != null && StringUtils.isNotBlank(params.get("checkType").toString())) w.eq(GzctEnterpriseDataQuality::getCheckType, params.get("checkType").toString());
            if (params.get("checkResult") != null && StringUtils.isNotBlank(params.get("checkResult").toString())) w.eq(GzctEnterpriseDataQuality::getCheckResult, params.get("checkResult").toString());
            // 检查时间范围筛选
            if (params.get("entryDateStart") != null && StringUtils.isNotBlank(params.get("entryDateStart").toString())) {
                w.ge(GzctEnterpriseDataQuality::getCreateTime, LocalDateTime.parse(params.get("entryDateStart").toString() + "T00:00:00"));
            }
            if (params.get("entryDateEnd") != null && StringUtils.isNotBlank(params.get("entryDateEnd").toString())) {
                w.le(GzctEnterpriseDataQuality::getCreateTime, LocalDateTime.parse(params.get("entryDateEnd").toString() + "T23:59:59"));
            }
            w.orderByDesc(GzctEnterpriseDataQuality::getCreateTime);
            Page<GzctEnterpriseDataQuality> page = new Page<>(pn, ps);
            Page<GzctEnterpriseDataQuality> r = dataQualityMapper.selectPage(page, w);
            int total = (int) r.getTotal();
            if (total == 0 && r.getRecords() != null && !r.getRecords().isEmpty()) {
                total = dataQualityMapper.selectCount(w).intValue();
            }
            PageResult<GzctEnterpriseDataQuality> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage(total > 0 ? (int) Math.ceil((double) total / ps) : 0); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "qualityDetail")
    @GetMapping("/quality/{id}")
    public R<GzctEnterpriseDataQuality> qualityDetail(@PathVariable String id) { try { return R.success(dataQualityMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "删除")
    @DeleteMapping("/quality/{id}")
    public R<Boolean> deleteQuality(@PathVariable String id) {
        try {
            GzctEnterpriseDataQuality record = dataQualityMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }
            int result = dataQualityMapper.deleteById(id);
            if (result > 0) {
                log.info("删除数据质量记录成功, ID: {}", id);
                return R.success(true);
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("删除数据质量记录失败, ID: {}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }
    @Operation(summary = "新增")
    @PostMapping("/quality/add")
    public R<Boolean> addQuality(@RequestBody GzctEnterpriseDataQuality record) { try { record.setCreateTime(LocalDateTime.now()); dataQualityMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "")
    @PostMapping("/quality/check")
    public R<Map<String, Object>> runQualityCheck(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            String enterpriseName = (String) params.get("enterpriseName");
            String checkType = (String) params.get("checkType");

            // 查询该企业的所有数据录入记录进行质量检查
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) {
                w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            }
            List<GzctEnterpriseDataEntry> entries = dataEntryMapper.selectList(w);

            int passCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();

            for (GzctEnterpriseDataEntry entry : entries) {
                boolean pass = true;
                StringBuilder errorMsg = new StringBuilder();

                // 完整性检查
                if ("完整性检查".equals(checkType) || StringUtils.isBlank(checkType)) {
                    if (StringUtils.isBlank(entry.getDataType())) { pass = false; errorMsg.append("数据类型为空;"); }
                    if (StringUtils.isBlank(entry.getReportPeriod())) { pass = false; errorMsg.append("报告期间为空;"); }
                    if (StringUtils.isBlank(entry.getSubmitter())) { pass = false; errorMsg.append("录入人为空;"); }
                }
                // 准确性检查
                if ("准确性检查".equals(checkType) || StringUtils.isBlank(checkType)) {
                    if (entry.getQualityScore() != null && entry.getQualityScore().doubleValue() < 0) { pass = false; errorMsg.append("质量评分为负数;"); }
                }

                // 记录检查结果
                GzctEnterpriseDataQuality quality = new GzctEnterpriseDataQuality();
                quality.setEnterpriseId(entry.getEnterpriseId());
                quality.setEnterpriseName(entry.getEnterpriseName());
                quality.setCheckType(StringUtils.isNotBlank(checkType) ? checkType : "完整性检查");
                quality.setCheckItem(entry.getDataType() + " - " + entry.getReportPeriod());
                quality.setCheckResult(pass ? "通过" : "不通过");
                quality.setErrorDesc(pass ? null : errorMsg.toString());
                quality.setDataSource(entry.getDataType());
                quality.setCheckTime(LocalDateTime.now());
                quality.setCreateTime(LocalDateTime.now());
                dataQualityMapper.insert(quality);

                if (pass) passCount++; else { failCount++; errors.add(entry.getEnterpriseName() + ": " + errorMsg); }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalChecked", entries.size());
            result.put("passCount", passCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            log.info("质量检查完成: 总计{}, 通过{}, 不通过{}", entries.size(), passCount, failCount);
            return R.success(result);
        } catch (Exception e) {
            return R.fail("检查失败：" + e.getMessage());
        }
    }
    @Operation(summary = "导出质量报告")
    @GetMapping("/quality/export")
    public void exportQuality(HttpServletResponse response) {
        try {
            List<GzctEnterpriseDataQuality> list = dataQualityMapper.selectList(new LambdaQueryWrapper<GzctEnterpriseDataQuality>().orderByDesc(GzctEnterpriseDataQuality::getCreateTime));
            if (list == null) list = new ArrayList<>();

            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("数据质量报告_" + System.currentTimeMillis() + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            java.io.OutputStream os = response.getOutputStream();
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);

            writer.println("企业名称,检查类型,检查项,检查结果,错误描述,数据来源,检查时间");
            for (GzctEnterpriseDataQuality item : list) {
                String[] row = {
                    escapeCsv(item.getEnterpriseName()),
                    escapeCsv(item.getCheckType()),
                    escapeCsv(item.getCheckItem()),
                    escapeCsv(item.getCheckResult()),
                    escapeCsv(item.getErrorDesc()),
                    escapeCsv(item.getDataSource()),
                    item.getCheckTime() != null ? item.getCheckTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""
                };
                writer.println(String.join(",", row));
            }
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出质量报告失败", e);
            try { if (!response.isCommitted()) { response.reset(); response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}"); } } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }
    @Operation(summary = "")
    @GetMapping("/quality/statistics")
    public R<Map<String, Object>> qualityStatistics() { try { Map<String, Object> result = new HashMap<>(); long total = dataQualityMapper.selectCount(null); long passCount = dataQualityMapper.selectCount(new LambdaQueryWrapper<GzctEnterpriseDataQuality>().eq(GzctEnterpriseDataQuality::getCheckResult, "通过")); long failCount = dataQualityMapper.selectCount(new LambdaQueryWrapper<GzctEnterpriseDataQuality>().eq(GzctEnterpriseDataQuality::getCheckResult, "不通过")); result.put("totalChecks", total); result.put("passCount", passCount); result.put("failCount", failCount); result.put("passRate", total > 0 ? Math.round((double) passCount / total * 1000.0) / 10.0 : 0); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }

    // ==================== 数据验证 ====================
    @Operation(summary = "")
    @PostMapping("/entry/validate")
    public R<Map<String, Object>> validateData(@RequestBody GzctEnterpriseDataEntry record) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<String> errors = new ArrayList<>();

            // 验证必填字段
            if (StringUtils.isBlank(record.getEnterpriseId())) errors.add("企业ID不能为空");
            if (StringUtils.isBlank(record.getEnterpriseName())) errors.add("企业名称不能为空");
            if (StringUtils.isBlank(record.getDataType())) errors.add("数据类型不能为空");
            if (StringUtils.isBlank(record.getReportPeriod())) errors.add("报告期间不能为空");
            if (StringUtils.isBlank(record.getSubmitter())) errors.add("提交人不能为空");

            boolean valid = errors.isEmpty();

            // 计算质量评分
            int qualityScore = 0;
            if (valid) {
                qualityScore = 85; // 基础分
                // 可以根据更多条件增加评分
                if (StringUtils.isNotBlank(record.getDataCategory())) qualityScore += 5;
                if (StringUtils.isNotBlank(record.getRemark())) qualityScore += 5;
                if (qualityScore > 100) qualityScore = 100;
            }

            result.put("valid", valid);
            result.put("errors", errors);
            result.put("qualityScore", qualityScore);

            return R.success(result);
        } catch (Exception e) { return R.fail("验证失败：" + e.getMessage()); }
    }

    // ==================== 数据历史 ====================
    @Operation(summary = "查询数据")
    @PostMapping("/entry/history")
    public R<PageResult<GzctEnterpriseDataEntry>> getHistory(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("dataType") != null && StringUtils.isNotBlank(params.get("dataType").toString())) w.eq(GzctEnterpriseDataEntry::getDataType, params.get("dataType").toString());
            w.orderByDesc(GzctEnterpriseDataEntry::getUpdateTime);
            Page<GzctEnterpriseDataEntry> page = new Page<>(pn, ps);
            Page<GzctEnterpriseDataEntry> r = dataEntryMapper.selectPage(page, w);
            int total = (int) r.getTotal();
            PageResult<GzctEnterpriseDataEntry> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage(total > 0 ? (int) Math.ceil((double) total / ps) : 0); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "数据历史列表(兼容路径)")
    @PostMapping("/history/list")
    public R<PageResult<GzctEnterpriseDataEntry>> getHistoryList(@RequestBody Map<String, Object> params) {
        return getHistory(params);
    }

    // ==================== 组织架构图 ====================
    @Operation(summary = "组织架构图")
    @PostMapping("/organization/chart")
    public R<List<Map<String, Object>>> getOrganizationChart(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            // 从数据录入记录中按企业名称构建简单的组织结构
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            w.groupBy(GzctEnterpriseDataEntry::getEnterpriseName);
            List<GzctEnterpriseDataEntry> entries = dataEntryMapper.selectList(w);

            List<Map<String, Object>> chart = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            for (GzctEnterpriseDataEntry entry : entries) {
                if (entry.getEnterpriseName() != null && !seen.contains(entry.getEnterpriseName())) {
                    seen.add(entry.getEnterpriseName());
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", entry.getEnterpriseId());
                    node.put("name", entry.getEnterpriseName());
                    node.put("parentId", null);
                    node.put("level", 1);
                    chart.add(node);
                }
            }
            return R.success(chart);
        } catch (Exception e) { return R.fail("获取组织架构失败：" + e.getMessage()); }
    }


    // ==================== 复制录入 ====================
    @Operation(summary = "copyEntry")
    @PostMapping("/entry/copy")
    public R<Boolean> copyEntry(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctEnterpriseDataEntry original = dataEntryMapper.selectById(id);
            if (original == null) return R.fail("原记录不存在");

            GzctEnterpriseDataEntry copy = new GzctEnterpriseDataEntry();
            copy.setEnterpriseId(original.getEnterpriseId());
            copy.setEnterpriseName(original.getEnterpriseName());
            copy.setDataType(original.getDataType());
            copy.setDataCategory(original.getDataCategory());
            copy.setReportPeriod(original.getReportPeriod());
            copy.setReportYear(original.getReportYear());
            copy.setStatus("草稿");
            copy.setRemark("复制自：" + original.getId());
            copy.setCreateTime(LocalDateTime.now());
            copy.setUpdateTime(LocalDateTime.now());

            dataEntryMapper.insert(copy);
            return R.success(true);
        } catch (Exception e) { return R.fail("复制失败：" + e.getMessage()); }
    }

    // ==================== 历史导出 ====================
    @Operation(summary = "导出历史记录")
    @PostMapping("/entry/history/export")
    public void exportHistory(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseDataEntry::getEnterpriseId, params.get("enterpriseId").toString());
            }
            w.orderByDesc(GzctEnterpriseDataEntry::getUpdateTime);
            List<GzctEnterpriseDataEntry> list = dataEntryMapper.selectList(w);

            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("数据历史记录_" + System.currentTimeMillis() + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            java.io.OutputStream os = response.getOutputStream();
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);

            writer.println("企业名称,数据类型,状态,提交人,更新时间,备注");
            for (GzctEnterpriseDataEntry item : list) {
                String[] row = {
                    escapeCsv(item.getEnterpriseName()),
                    escapeCsv(item.getDataType()),
                    escapeCsv(item.getStatus()),
                    escapeCsv(item.getSubmitter()),
                    item.getUpdateTime() != null ? item.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "",
                    escapeCsv(item.getRemark())
                };
                writer.println(String.join(",", row));
            }
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出历史失败", e);
            try { if (!response.isCommitted()) { response.reset(); response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}"); } } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }

    // ==================== 导入相关 ====================
    @Operation(summary = "文件上传")
    @PostMapping("/import/upload")
    public R<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();

            // 解析Excel获取预览数据
            List<Map<String, String>> previewRecords = new ArrayList<>();
            int totalRows = 0;
            org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = null;
            org.apache.poi.xssf.usermodel.XSSFWorkbook xworkbook = null;
            org.apache.poi.ss.usermodel.Sheet sheet = null;

            try {
                if (fileName != null && fileName.endsWith(".xlsx")) {
                    xworkbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(file.getInputStream());
                    sheet = xworkbook.getSheetAt(0);
                } else {
                    workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook(file.getInputStream());
                    sheet = workbook.getSheetAt(0);
                }
                totalRows = sheet.getLastRowNum();
                // 读取前10行作为预览
                org.apache.poi.ss.usermodel.Row headerRow = sheet.getRow(0);
                if (headerRow != null) {
                    for (int i = 1; i <= Math.min(10, totalRows); i++) {
                        org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                        if (row == null) continue;
                        Map<String, String> record = new LinkedHashMap<>();
                        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                            String key = getCellValue(headerRow.getCell(j));
                            String val = getCellValue(row.getCell(j));
                            record.put(key, val);
                        }
                        previewRecords.add(record);
                    }
                }
            } finally {
                if (workbook != null) workbook.close();
                if (xworkbook != null) xworkbook.close();
            }

            String fileId = "FILE_" + System.currentTimeMillis();
            Map<String, Object> result = new HashMap<>();
            result.put("fileId", fileId);
            result.put("fileName", fileName);
            result.put("fileSize", fileSize);
            result.put("totalRecords", totalRows);
            result.put("previewRecords", previewRecords);

            return R.success(result);
        } catch (Exception e) { return R.fail("上传失败：" + e.getMessage()); }
    }

    private String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    @Operation(summary = "导入预览")
    @PostMapping("/import/preview")
    public R<Map<String, Object>> previewImport(@RequestBody Map<String, Object> params) {
        try {
            // 前端上传后已经在upload接口返回了预览数据
            // 这里提供字段映射信息
            Map<String, Object> result = new HashMap<>();
            List<Map<String, String>> fieldMapping = new ArrayList<>();
            String[][] mappings = {
                {"企业名称", "enterpriseName"}, {"数据类型", "dataType"}, {"数据类别", "dataCategory"},
                {"报告期间", "reportPeriod"}, {"报告年度", "reportYear"}, {"提交人", "submitter"}, {"备注", "remark"}
            };
            for (String[] m : mappings) {
                Map<String, String> fm = new HashMap<>();
                fm.put("sourceField", m[0]);
                fm.put("targetField", m[1]);
                fieldMapping.add(fm);
            }
            result.put("fieldMapping", fieldMapping);
            result.put("totalRecords", params.get("totalRecords"));
            result.put("validRecords", params.get("totalRecords"));
            result.put("errorRecords", 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("预览失败：" + e.getMessage()); }
    }

    @Operation(summary = "执行导入")
    @PostMapping("/import")
    public R<Map<String, Object>> importData(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> records = (List<Map<String, Object>>) params.get("records");
            String enterpriseId = (String) params.get("enterpriseId");
            String enterpriseName = (String) params.get("enterpriseName");

            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();

            if (records != null) {
                for (Map<String, Object> record : records) {
                    try {
                        GzctEnterpriseDataEntry entry = new GzctEnterpriseDataEntry();
                        entry.setEnterpriseId(enterpriseId);
                        entry.setEnterpriseName(enterpriseName != null ? enterpriseName : (String) record.get("enterpriseName"));
                        entry.setDataType((String) record.get("dataType"));
                        entry.setDataCategory((String) record.get("dataCategory"));
                        entry.setReportPeriod((String) record.get("reportPeriod"));
                        entry.setReportYear((String) record.get("reportYear"));
                        entry.setSubmitter((String) record.get("submitter"));
                        entry.setRemark((String) record.get("remark"));
                        entry.setStatus("草稿");
                        entry.setCreateTime(LocalDateTime.now());
                        entry.setUpdateTime(LocalDateTime.now());
                        dataEntryMapper.insert(entry);
                        successCount++;
                    } catch (Exception e) {
                        failCount++;
                        errors.add("第" + (successCount + failCount) + "条导入失败: " + e.getMessage());
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            log.info("数据导入完成, 成功: {}, 失败: {}", successCount, failCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("导入失败：" + e.getMessage()); }
    }

    @Operation(summary = "下载模板")
    @GetMapping("/template/download")
    public void downloadTemplate(@RequestParam(defaultValue = "GENERAL") String type, HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("数据录入模板_" + type + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            java.io.OutputStream os = response.getOutputStream();
            // 写入UTF-8 BOM，确保Excel正确识别中文编码
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);
            String[] headers = {"企业名称", "数据类型", "数据类别", "报告期间", "报告年度", "提交人", "备注"};
            writer.println(String.join(",", headers));

            // 写入示例数据行（帮助用户理解格式）
            writer.println("示例企业,财务数据,资产负债表,2024-Q1,2024,张三,示例数据请删除");
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("下载模板失败", e);
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"code\":0,\"msg\":\"下载模板失败：" + e.getMessage() + "\"}");
                    response.flushBuffer();
                }
            } catch (Exception ex) {
                log.error("发送错误响应失败", ex);
            }
        }
    }

    // ==================== 数据清洗 ====================
    @Operation(summary = "数据清洗")
    @PostMapping("/quality/clean")
    public R<Map<String, Object>> cleanData(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            String cleanType = (String) params.get("cleanType");

            LambdaQueryWrapper<GzctEnterpriseDataEntry> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataEntry::getEnterpriseId, enterpriseId);
            List<GzctEnterpriseDataEntry> entries = dataEntryMapper.selectList(w);

            int cleanedCount = 0;
            int skippedCount = 0;

            for (GzctEnterpriseDataEntry entry : entries) {
                boolean needClean = false;
                // 去除空白字符
                if (entry.getEnterpriseName() != null && !entry.getEnterpriseName().equals(entry.getEnterpriseName().trim())) {
                    entry.setEnterpriseName(entry.getEnterpriseName().trim());
                    needClean = true;
                }
                if (entry.getDataType() != null && !entry.getDataType().equals(entry.getDataType().trim())) {
                    entry.setDataType(entry.getDataType().trim());
                    needClean = true;
                }
                if (entry.getRemark() != null && !entry.getRemark().equals(entry.getRemark().trim())) {
                    entry.setRemark(entry.getRemark().trim());
                    needClean = true;
                }
                // 修复空字段为默认值
                if (StringUtils.isBlank(entry.getDataCategory())) {
                    entry.setDataCategory("未分类");
                    needClean = true;
                }
                if (needClean) {
                    entry.setUpdateTime(LocalDateTime.now());
                    dataEntryMapper.updateById(entry);
                    cleanedCount++;
                } else {
                    skippedCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("cleanedCount", cleanedCount);
            result.put("skippedCount", skippedCount);
            result.put("totalProcessed", entries.size());
            log.info("数据清洗完成, 清洗: {}, 跳过: {}", cleanedCount, skippedCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("清洗失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成质量报告")
    @PostMapping("/quality/report")
    public R<Map<String, Object>> generateQualityReport(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> result = new HashMap<>();
            result.put("reportId", "REPORT_" + System.currentTimeMillis());
            result.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            LambdaQueryWrapper<GzctEnterpriseDataQuality> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseDataQuality::getEnterpriseId, enterpriseId);
            long total = dataQualityMapper.selectCount(w);

            LambdaQueryWrapper<GzctEnterpriseDataQuality> pw = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) pw.eq(GzctEnterpriseDataQuality::getEnterpriseId, enterpriseId);
            pw.eq(GzctEnterpriseDataQuality::getCheckResult, "通过");
            long passCount = dataQualityMapper.selectCount(pw);

            result.put("totalChecks", total);
            result.put("passCount", passCount);
            result.put("failCount", total - passCount);
            result.put("passRate", total > 0 ? Math.round((double) passCount / total * 1000.0) / 10.0 : 0);
            result.put("status", "completed");
            return R.success(result);
        } catch (Exception e) { return R.fail("生成报告失败：" + e.getMessage()); }
    }

    // ==================== 数据导出扩展 ====================
    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<GzctEnterpriseDataEntry> list = dataEntryMapper.selectList(new LambdaQueryWrapper<GzctEnterpriseDataEntry>().orderByDesc(GzctEnterpriseDataEntry::getCreateTime));
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            String filename = java.net.URLEncoder.encode("数据导出_" + System.currentTimeMillis() + ".csv", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            java.io.OutputStream os = response.getOutputStream();
            // 写入UTF-8 BOM，确保Excel正确识别中文编码
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(os, "UTF-8"), true);
            String[] headers = {"企业名称","数据类型","数据类别","报告期间","报告年度","状态","提交人","质量评分","创建时间"};
            writer.println(String.join(",", headers));

            for (GzctEnterpriseDataEntry item : list) {
                String[] row = {
                    escapeCsv(item.getEnterpriseName()),
                    escapeCsv(item.getDataType()),
                    escapeCsv(item.getDataCategory()),
                    escapeCsv(item.getReportPeriod()),
                    escapeCsv(item.getReportYear()),
                    escapeCsv(item.getStatus()),
                    escapeCsv(item.getSubmitter()),
                    item.getQualityScore() != null ? item.getQualityScore().toString() : "0",
                    item.getCreateTime() != null ? item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""
                };
                writer.println(String.join(",", row));
            }
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
                    response.flushBuffer();
                }
            } catch (Exception ex) {
                log.error("发送错误响应失败", ex);
            }
        }
    }

    /** CSV字段转义：包含逗号、引号、换行的字段用双引号包裹 */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
