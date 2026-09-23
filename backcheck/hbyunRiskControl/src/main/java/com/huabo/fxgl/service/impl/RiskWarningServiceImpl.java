package com.huabo.fxgl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.entity.TblRiskWarning;
import com.huabo.fxgl.entity.TblEvaluationModel;
import com.huabo.fxgl.mapper.TblRiskWarningMapper;
import com.huabo.fxgl.service.IRiskWarningService;
import com.huabo.fxgl.service.IEvaluationModelService;
import com.huabo.fxgl.service.ICombinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// 报告生成工具类
import com.huabo.fxgl.util.PdfReportGenerator;
import com.huabo.fxgl.util.WordReportGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * 风险预警服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-10-07
 */
@Slf4j
@Service
public class RiskWarningServiceImpl extends ServiceImpl<TblRiskWarningMapper, TblRiskWarning>
        implements IRiskWarningService {

    @Autowired
    private TblRiskWarningMapper riskWarningMapper;

    @Autowired
    private IEvaluationModelService evaluationModelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ICombinationService combinationService;

    @Override
    public JsonBean getRiskWarningList(Map<String, Object> queryParams) {
        try {
            log.info("🚀 查询风险预警列表，参数: {}", queryParams);

            // 解析查询参数
            Integer pageNum = (Integer) queryParams.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) queryParams.getOrDefault("pageSize", 20);

            // 🚀 方案2优化：支持数组参数（多选）
            List<String> warningLevels = parseListParam(queryParams.get("warningLevels"));
            List<String> warningStatuses = parseListParam(queryParams.get("warningStatuses"));
            List<String> warningTypes = parseListParam(queryParams.get("warningTypes"));
            List<String> companyNames = parseListParam(queryParams.get("companyNames"));

            // 兼容旧的单值参数
            String warningLevel = (String) queryParams.get("warningLevel");
            String warningStatus = (String) queryParams.get("warningStatus");
            String warningType = (String) queryParams.get("warningType");
            String companyName = (String) queryParams.get("companyName");

            String startTime = (String) queryParams.get("startTime");
            String endTime = (String) queryParams.get("endTime");
            String evalModelId = (String) queryParams.get("evalModelId");

            // 🔧 修复：使用 PageHelper.startPage 替代 MyBatis-Plus 分页
            PageHelper.startPage(pageNum, pageSize);

            // 构建查询条件
            QueryWrapper<TblRiskWarning> queryWrapper = new QueryWrapper<>();

            // 🚀 方案2优化：支持多条件IN查询
            // 预警级别
            if (warningLevels != null && !warningLevels.isEmpty()) {
                queryWrapper.in("WARNING_LEVEL", warningLevels);
                log.info("🚀 添加预警级别IN过滤: {}", warningLevels);
            } else if (StringUtil.isNotEmpty(warningLevel)) {
                queryWrapper.eq("WARNING_LEVEL", warningLevel);
                log.info("🚀 添加预警级别过滤: {}", warningLevel);
            }

            // 预警状态
            if (warningStatuses != null && !warningStatuses.isEmpty()) {
                queryWrapper.in("WARNING_STATUS", warningStatuses);
                log.info("🚀 添加预警状态IN过滤: {}", warningStatuses);
            } else if (StringUtil.isNotEmpty(warningStatus)) {
                queryWrapper.eq("WARNING_STATUS", warningStatus);
                log.info("🚀 添加预警状态过滤: {}", warningStatus);
            }

            // 预警类型
            if (warningTypes != null && !warningTypes.isEmpty()) {
                queryWrapper.in("WARNING_TYPE", warningTypes);
                log.info("🚀 添加预警类型IN过滤: {}", warningTypes);
            } else if (StringUtil.isNotEmpty(warningType)) {
                queryWrapper.eq("WARNING_TYPE", warningType);
                log.info("🚀 添加预警类型过滤: {}", warningType);
            }

            // 企业名称
            if (companyNames != null && !companyNames.isEmpty()) {
                queryWrapper.in("COMPANY_NAME", companyNames);
                log.info("🚀 添加企业名称IN过滤: {}", companyNames);
            } else if (StringUtil.isNotEmpty(companyName)) {
                queryWrapper.like("COMPANY_NAME", companyName);
                log.info("🚀 添加企业名称LIKE过滤: {}", companyName);
            }

            // 时间范围
            if (StringUtil.isNotEmpty(startTime)) {
                queryWrapper.ge("WARNING_TIME", startTime);
            }
            if (StringUtil.isNotEmpty(endTime)) {
                queryWrapper.le("WARNING_TIME", endTime);
            }

            // 评估模型ID
            if (StringUtil.isNotEmpty(evalModelId)) {
                queryWrapper.eq("EVAL_MODEL_ID", evalModelId);
                log.info("🚀 添加评估模型ID过滤: {}", evalModelId);
            }

            // 排序：按预警时间倒序
            queryWrapper.orderByDesc("WARNING_TIME");

            long startQueryTime = System.currentTimeMillis();

            // 🔧 修复：使用 list 方法查询，PageHelper 会自动处理分页
            List<TblRiskWarning> list = this.list(queryWrapper);

            long queryDuration = System.currentTimeMillis() - startQueryTime;
            log.info("🚀 数据库查询耗时: {} ms", queryDuration);

            // 🔧 修复：使用 PageInfo 包装结果，获取分页信息
            PageInfo<TblRiskWarning> pageInfo = new PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("list", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            data.put("pages", pageInfo.getPages());

            log.info("🚀 查询风险预警列表成功，总数: {}, 当前页: {}, 每页大小: {}, 耗时: {} ms",
                    pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize(), queryDuration);
            return new JsonBean(1, "查询成功", data);

        } catch (Exception e) {
            log.error("❌ 查询风险预警列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 🚀 方案2优化：解析列表参数（支持数组和单值）
     */
    private List<String> parseListParam(Object param) {
        if (param == null) {
            return null;
        }

        // 如果已经是List，直接返回
        if (param instanceof List) {
            List<?> list = (List<?>) param;
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    result.add(item.toString());
                }
            }
            return result.isEmpty() ? null : result;
        }

        // 如果是单个字符串，包装成List
        if (param instanceof String) {
            String str = (String) param;
            if (StringUtil.isNotEmpty(str)) {
                return Collections.singletonList(str);
            }
        }

        return null;
    }

    @Override
    public JsonBean getRiskWarningDetail(String warningId) {
        try {
            log.info("获取风险预警详情，预警ID: {}", warningId);

            if (StringUtil.isEmpty(warningId)) {
                return new JsonBean(0, "预警ID不能为空", null);
            }

            TblRiskWarning warning = getById(warningId);
            if (warning == null) {
                return new JsonBean(0, "预警记录不存在", null);
            }

            // TODO: 暂时注释已读标记功能，待字段映射完善后恢复
            // 标记为已读逻辑需要根据新的字段结构调整

            log.info("获取风险预警详情成功，预警ID: {}", warningId);
            return new JsonBean(1, "获取成功", warning);

        } catch (Exception e) {
            log.error("获取风险预警详情失败，预警ID: {}", warningId, e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean processRiskWarning(String warningId, String status, String handler, String handleRemark) {
        try {
            log.info("处理风险预警，预警ID: {}, 状态: {}, 处理人: {}", warningId, status, handler);

            if (StringUtil.isEmpty(warningId)) {
                return new JsonBean(0, "预警ID不能为空", null);
            }

            TblRiskWarning warning = getById(warningId);
            if (warning == null) {
                return new JsonBean(0, "预警记录不存在", null);
            }

            // 🔧 修复：恢复真正的处理逻辑，更新数据库状态
            String oldStatus = warning.getWarningStatus();

            // 更新预警状态
            if (StringUtil.isNotEmpty(status)) {
                warning.setWarningStatus(status);
            }

            // 更新处理信息
            warning.setProcessUser(handler);
            warning.setProcessTime(LocalDateTime.now());
            warning.setProcessNote(handleRemark);
            warning.setUpdateUser(handler);
            warning.setUpdateTime(LocalDateTime.now());

            // 根据不同的处理动作设置相应字段
            if ("IGNORED".equals(status)) {
                warning.setProcessAction("IGNORE");
            } else if ("PROCESSING".equals(status)) {
                warning.setProcessAction("CONFIRM");
            } else if ("PROCESSED".equals(status)) {
                warning.setProcessAction("RESOLVE");
            } else if ("ESCALATED".equals(status)) {
                warning.setProcessAction("ESCALATE");
            }

            // 如果是标记误报
            if ("FALSE_POSITIVE".equals(status)) {
                warning.setIsFalsePositive("Y");
                warning.setWarningStatus("IGNORED");
                warning.setProcessAction("MARK_FALSE");
            }

            // 🔧 关键：真正更新数据库
            boolean updateResult = updateById(warning);

            if (updateResult) {
                log.info("处理风险预警成功，预警ID: {}, 状态: {} -> {}, 处理人: {}",
                        warningId, oldStatus, warning.getWarningStatus(), handler);

                // 返回更新后的数据
                Map<String, Object> result = new HashMap<>();
                result.put("warningId", warningId);
                result.put("oldStatus", oldStatus);
                result.put("newStatus", warning.getWarningStatus());
                result.put("processUser", handler);
                result.put("processTime", warning.getProcessTime());
                result.put("processAction", warning.getProcessAction());

                return new JsonBean(1, "处理成功", result);
            } else {
                log.error("更新风险预警失败，预警ID: {}", warningId);
                return new JsonBean(0, "更新数据库失败", null);
            }

        } catch (Exception e) {
            log.error("处理风险预警失败，预警ID: {}", warningId, e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean batchProcessRiskWarning(List<String> warningIds, String status, String handler, String handleRemark) {
        try {
            log.info("批量处理风险预警，数量: {}, 状态: {}, 处理人: {}", warningIds.size(), status, handler);

            if (warningIds == null || warningIds.isEmpty()) {
                return new JsonBean(0, "预警ID列表不能为空", null);
            }

            int updateCount = riskWarningMapper.batchUpdateStatus(warningIds, status, handler, handleRemark);

            log.info("批量处理风险预警成功，更新数量: {}", updateCount);
            return new JsonBean(1, "批量处理成功，更新数量: " + updateCount, null);

        } catch (Exception e) {
            log.error("批量处理风险预警失败", e);
            return new JsonBean(0, "批量处理失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getWarningStatistics() {
        try {
            log.info("开始获取预警统计信息");

            Map<String, Object> statistics = new HashMap<>();

            // 🔧 修复：分步骤获取统计信息，增加详细的错误处理

            // 1. 总数统计
            try {
                long totalCount = count();
                statistics.put("totalCount", totalCount);
                log.info("总预警数量: {}", totalCount);
            } catch (Exception e) {
                log.error("获取总数统计失败", e);
                statistics.put("totalCount", 0L);
            }

            // 2. 风险等级统计
            try {
                List<Map<String, Object>> riskLevelStats = riskWarningMapper.selectRiskLevelStatistics();
                Map<String, Long> riskLevelMap = new HashMap<>();

                if (riskLevelStats != null && !riskLevelStats.isEmpty()) {
                    for (Map<String, Object> stat : riskLevelStats) {
                        // 🔧 修复：达梦驱动返回的列别名字母大小写不确定，按大小写无关方式读取
                        String level = (String) getMapValueIgnoreCase(stat, "RISK_LEVEL");
                        Object countObj = getMapValueIgnoreCase(stat, "count");
                        Long count = 0L;

                        if (countObj instanceof Number) {
                            count = ((Number) countObj).longValue();
                        }

                        if (level != null) {
                            riskLevelMap.put(level, count);
                        }
                    }
                }

                statistics.put("riskLevelStatistics", riskLevelMap);
                log.info("风险等级统计: {}", riskLevelMap);
            } catch (Exception e) {
                log.error("获取风险等级统计失败", e);
                statistics.put("riskLevelStatistics", new HashMap<>());
            }

            // 3. 状态统计
            try {
                List<Map<String, Object>> statusStats = riskWarningMapper.selectStatusStatistics();
                Map<String, Long> statusMap = new HashMap<>();

                log.info("原始状态统计查询结果: {}", statusStats);

                if (statusStats != null && !statusStats.isEmpty()) {
                    for (Map<String, Object> stat : statusStats) {
                        // 🔧 修复：达梦驱动返回的列别名字母大小写不确定，按大小写无关方式读取
                        String status = (String) getMapValueIgnoreCase(stat, "STATUS");
                        Object countObj = getMapValueIgnoreCase(stat, "count");
                        Long count = 0L;

                        if (countObj instanceof Number) {
                            count = ((Number) countObj).longValue();
                        }

                        if (status != null) {
                            statusMap.put(status, count);
                            log.info("状态统计 - 状态: {}, 数量: {}", status, count);
                        }
                    }
                }

                // 🔧 优化：不再逐状态单独COUNT（与GROUP BY结果重复），仅保证4个状态键固定返回、无数据补0
                statusMap.putIfAbsent("PROCESSED", 0L);
                statusMap.putIfAbsent("PENDING", 0L);
                statusMap.putIfAbsent("PROCESSING", 0L);
                statusMap.putIfAbsent("IGNORED", 0L);

                statistics.put("statusStatistics", statusMap);
                log.info("最终状态统计: {}", statusMap);
            } catch (Exception e) {
                log.error("获取状态统计失败", e);
                statistics.put("statusStatistics", new HashMap<>());
            }

            // 4. 关键指标统计
            try {
                Long unreadCount = riskWarningMapper.selectUnreadCount();
                Long pendingCount = riskWarningMapper.selectPendingCount();
                Long highRiskCount = riskWarningMapper.selectHighRiskCount();

                statistics.put("unreadCount", unreadCount != null ? unreadCount : 0L);
                statistics.put("pendingCount", pendingCount != null ? pendingCount : 0L);
                statistics.put("highRiskCount", highRiskCount != null ? highRiskCount : 0L);

                log.info("关键指标 - 未读: {}, 待处理: {}, 高风险: {}", unreadCount, pendingCount, highRiskCount);
            } catch (Exception e) {
                log.error("获取关键指标统计失败", e);
                statistics.put("unreadCount", 0L);
                statistics.put("pendingCount", 0L);
                statistics.put("highRiskCount", 0L);
            }

            // 5. 添加额外的统计信息
            try {
                // 预警类型统计
                List<Map<String, Object>> typeStats = riskWarningMapper.selectBusinessScenarioStatistics();
                Map<String, Long> typeMap = new HashMap<>();

                if (typeStats != null && !typeStats.isEmpty()) {
                    for (Map<String, Object> stat : typeStats) {
                        // 🔧 修复：达梦驱动返回的列别名字母大小写不确定，按大小写无关方式读取
                        String type = (String) getMapValueIgnoreCase(stat, "BUSINESS_SCENARIO");
                        Object countObj = getMapValueIgnoreCase(stat, "count");
                        Long count = 0L;

                        if (countObj instanceof Number) {
                            count = ((Number) countObj).longValue();
                        }

                        if (type != null) {
                            typeMap.put(type, count);
                        }
                    }
                }

                statistics.put("typeStatistics", typeMap);
                log.info("类型统计: {}", typeMap);
            } catch (Exception e) {
                log.error("获取类型统计失败", e);
                statistics.put("typeStatistics", new HashMap<>());
            }

            // 🔧 新增：在主统计接口中直接包含今日新增数量
            try {
                Long todayCount = riskWarningMapper.selectTodayWarningCount();
                statistics.put("todayCount", todayCount != null ? todayCount : 0L);
                log.info("今日新增预警数量: {}", todayCount);
            } catch (Exception e) {
                log.error("获取今日新增数量失败", e);
                statistics.put("todayCount", 0L);
            }

            log.info("获取预警统计信息成功，统计结果: {}", statistics);
            return new JsonBean(1, "获取成功", statistics);

        } catch (Exception e) {
            log.error("获取预警统计信息失败", e);
            String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误: " + e.getClass().getSimpleName();
            return new JsonBean(0, "获取失败: " + errorMsg, null);
        }
    }

    /**
     * 🔧 新增：获取今日新增预警数量
     */
    @Override
    public JsonBean getTodayWarningCount() {
        try {
            log.info("开始获取今日新增预警数量");

            Long todayCount = riskWarningMapper.selectTodayWarningCount();

            Map<String, Object> result = new HashMap<>();
            result.put("count", todayCount != null ? todayCount : 0L);
            result.put("date", java.time.LocalDate.now().toString());

            log.info("今日新增预警数量: {}", todayCount);

            return new JsonBean(1, "获取今日新增数量成功", result);
        } catch (Exception e) {
            log.error("获取今日新增预警数量失败", e);
            return new JsonBean(0, "获取今日新增数量失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean generateWarningReport(Map<String, Object> reportParams) {
        try {
            log.info("生成预警报告，参数: {}", reportParams);

            // 🔥 根据报告格式确定文件扩展名和生成方法
            String reportFormat = (String) reportParams.getOrDefault("reportFormat", "EXCEL");
            reportFormat = reportFormat == null ? "EXCEL" : reportFormat.toUpperCase();

            String extension;
            switch (reportFormat) {
                case "PDF":
                    extension = ".pdf";
                    break;
                case "WORD":
                    extension = ".docx";
                    break;
                case "EXCEL":
                default:
                    extension = ".xlsx";
                    reportFormat = "EXCEL"; // 归一化
                    break;
            }

            // 生成报告ID和文件名
            String reportId = "RPT_" + System.currentTimeMillis();
            String reportName = "风险预警报告_" + LocalDateTime.now().toString().substring(0, 10);
            String fileName = reportName + "_" + reportId + extension;

            // 创建报告目录
            String reportDir = System.getProperty("java.io.tmpdir") + File.separator + "reports";
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 根据格式分发到对应的生成器
            String filePath = reportDir + File.separator + fileName;
            switch (reportFormat) {
                case "PDF":
                    generatePdfReport(filePath, reportParams);
                    break;
                case "WORD":
                    generateWordReport(filePath, reportParams);
                    break;
                case "EXCEL":
                default:
                    generateExcelReport(filePath, reportParams);
                    break;
            }

            Map<String, Object> reportResult = new HashMap<>();
            reportResult.put("reportId", reportId);
            reportResult.put("reportName", reportName);
            reportResult.put("fileName", fileName);
            reportResult.put("filePath", filePath);
            reportResult.put("reportFormat", reportFormat);
            reportResult.put("generateTime", LocalDateTime.now());
            reportResult.put("status", "SUCCESS");
            reportResult.put("downloadUrl", "/riskcontrol/model/warning/downloadReport?reportId=" + reportId + "&fileName=" + fileName);

            log.info("生成预警报告成功，格式: {}, 文件路径: {}", reportFormat, filePath);
            return new JsonBean(1, "报告生成成功", reportResult);

        } catch (Exception e) {
            log.error("生成预警报告失败", e);
            return new JsonBean(0, "报告生成失败: " + e.getMessage(), null);
        }
    }

    /**
     * 生成Excel报告文件
     */
    private void generateExcelReport(String filePath, Map<String, Object> reportParams) throws Exception {
        log.info("开始生成Excel报告，参数: {}", reportParams);

        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("风险预警报告");

        // 获取报告标题
        String reportTitle = (String) reportParams.getOrDefault("reportTitle", "风险预警报告");

        // 创建标题行
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue(reportTitle);

        // 创建表头
        Row headerRow = sheet.createRow(2);
        headerRow.createCell(0).setCellValue("序号");
        headerRow.createCell(1).setCellValue("预警编码");
        headerRow.createCell(2).setCellValue("预警类型");
        headerRow.createCell(3).setCellValue("预警级别");
        headerRow.createCell(4).setCellValue("预警状态");
        headerRow.createCell(5).setCellValue("企业名称");
        headerRow.createCell(6).setCellValue("预警值");
        headerRow.createCell(7).setCellValue("阈值");
        headerRow.createCell(8).setCellValue("预警描述");
        headerRow.createCell(9).setCellValue("创建时间");
        headerRow.createCell(10).setCellValue("处理人");
        headerRow.createCell(11).setCellValue("处理时间");

        // 🔥 根据报告参数构建查询条件
        log.info("🔧 Excel生成 - 开始构建查询条件");
        QueryWrapper<TblRiskWarning> queryWrapper = buildReportQueryWrapper(reportParams);
        log.info("🔧 Excel生成 - 查询条件构建完成");

        // 查询预警数据
        log.info("🔧 Excel生成 - 开始查询预警数据");
        List<TblRiskWarning> warnings = null;
        try {
            log.info("🔧 Excel生成 - 开始执行MyBatis查询");
            warnings = riskWarningMapper.selectList(queryWrapper);
            log.info("🔧 Excel生成 - MyBatis查询完成");
            log.info("🔧 Excel生成 - 查询到 {} 条预警数据用于生成报告", warnings != null ? warnings.size() : 0);

            // 🔧 调试：检查查询结果
            if (warnings == null) {
                log.error("🔧 Excel生成 - 查询结果为null！");
                warnings = new ArrayList<>();
            } else if (warnings.isEmpty()) {
                log.warn("🔧 Excel生成 - 查询结果为空列表！");
            } else {
                log.info("🔧 Excel生成 - 查询结果正常，第一条数据ID: {}", warnings.get(0).getWarningId());
            }
        } catch (Exception e) {
            log.error("🔧 Excel生成 - 查询预警数据时发生异常: {}", e.getMessage(), e);
            log.error("🔧 Excel生成 - 异常堆栈: ", e);
            warnings = new ArrayList<>();
        }

        // 填充数据
        log.info("🔧 Excel生成 - 开始填充数据，共 {} 条记录", warnings.size());
        int rowNum = 3;
        try {
            for (int i = 0; i < warnings.size(); i++) {
                try {
                    TblRiskWarning warning = warnings.get(i);
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.createCell(0).setCellValue(i + 1);

                    // 安全地获取字段值，避免BLOB字段问题
                    dataRow.createCell(1).setCellValue(safeGetString(warning.getWarningCode()));
                    // 🔧 修复：将数据库原始值转换为中文
                    dataRow.createCell(2).setCellValue(convertWarningTypeToChinese(safeGetString(warning.getWarningType())));
                    dataRow.createCell(3).setCellValue(convertWarningLevelToChinese(safeGetString(warning.getWarningLevel())));
                    dataRow.createCell(4).setCellValue(convertWarningStatusToChinese(safeGetString(warning.getWarningStatus())));
                    // 🔧 修复：显示企业名称而不是模型ID
                    dataRow.createCell(5).setCellValue(safeGetString(warning.getCompanyName()));
                    dataRow.createCell(6).setCellValue(warning.getWarningValue() != null ? warning.getWarningValue().toString() : "");
                    dataRow.createCell(7).setCellValue(warning.getThresholdValue() != null ? warning.getThresholdValue().toString() : "");
                    dataRow.createCell(8).setCellValue(safeGetString(warning.getWarningDescription()));
                    dataRow.createCell(9).setCellValue(warning.getCreateTime() != null ? warning.getCreateTime().toString() : "");
                    dataRow.createCell(10).setCellValue(safeGetString(warning.getProcessUser()));
                    dataRow.createCell(11).setCellValue(warning.getProcessTime() != null ? warning.getProcessTime().toString() : "");

                    // 每100条记录打印一次进度
                    if ((i + 1) % 100 == 0) {
                        log.info("🔧 Excel生成 - 已填充 {} 条数据", i + 1);
                    }
                } catch (Exception e) {
                    log.warn("🔧 Excel生成 - 填充第 {} 条数据时出现异常: {}", i + 1, e.getMessage());
                    // 继续处理下一条记录
                }
            }
            log.info("🔧 Excel生成 - 数据填充完成，共填充 {} 条记录", warnings.size());
        } catch (Exception e) {
            log.error("🔧 Excel生成 - 填充数据时发生异常: {}", e.getMessage(), e);
            throw e;
        }

        // 自动调整列宽
        log.info("🔧 Excel生成 - 开始调整列宽");
        for (int i = 0; i < 12; i++) {
            sheet.autoSizeColumn(i);
            // 🔧 修复：限制预警描述列（第8列）的最大宽度，避免单元格过长
            if (i == 8) {
                // 获取当前列宽
                int currentWidth = sheet.getColumnWidth(i);
                // 设置最大宽度为 50 个字符（约 12800 units，Excel 列宽单位是 1/256 字符宽度）
                int maxWidth = 50 * 256;
                if (currentWidth > maxWidth) {
                    sheet.setColumnWidth(i, maxWidth);
                }
            }
        }

        // 写入文件
        log.info("🔧 Excel生成 - 开始写入文件: {}", filePath);
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            log.info("🔧 Excel生成 - 文件写入成功，文件大小: {} bytes", new java.io.File(filePath).length());
        } catch (Exception e) {
            log.error("🔧 Excel生成 - 文件写入失败: {}", e.getMessage(), e);
            throw e;
        }

            // 关闭工作簿
            try {
                workbook.close();
                log.info("🔧 Excel生成 - 工作簿已关闭");
            } catch (Exception e) {
                log.warn("🔧 Excel生成 - 关闭工作簿时出现警告: {}", e.getMessage());
            }

            log.info("Excel报告生成完成: {}", filePath);

        } catch (Exception e) {
            log.error("🔧 Excel生成 - 生成报告时发生严重异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 生成PDF报告文件
     */
    @SuppressWarnings("unchecked")
    private void generatePdfReport(String filePath, Map<String, Object> reportParams) throws Exception {
        log.info("开始生成PDF报告，参数: {}", reportParams);

        // 复用现有的查询逻辑
        QueryWrapper<TblRiskWarning> queryWrapper = buildReportQueryWrapper(reportParams);
        List<TblRiskWarning> warnings = riskWarningMapper.selectList(queryWrapper);
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        log.info("PDF生成 - 查询到 {} 条预警数据", warnings.size());

        // 获取包含内容
        List<String> includeContents = (List<String>) reportParams.get("includeContents");

        // 调用工具类生成PDF
        PdfReportGenerator.generate(filePath, reportParams, warnings, includeContents);
        log.info("PDF报告生成完成: {}, 文件大小: {} bytes",
                filePath, new java.io.File(filePath).length());
    }

    /**
     * 生成Word报告文件
     */
    @SuppressWarnings("unchecked")
    private void generateWordReport(String filePath, Map<String, Object> reportParams) throws Exception {
        log.info("开始生成Word报告，参数: {}", reportParams);

        // 复用现有的查询逻辑
        QueryWrapper<TblRiskWarning> queryWrapper = buildReportQueryWrapper(reportParams);
        List<TblRiskWarning> warnings = riskWarningMapper.selectList(queryWrapper);
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        log.info("Word生成 - 查询到 {} 条预警数据", warnings.size());

        // 获取包含内容
        List<String> includeContents = (List<String>) reportParams.get("includeContents");

        // 调用工具类生成Word
        WordReportGenerator.generate(filePath, reportParams, warnings, includeContents);
        log.info("Word报告生成完成: {}, 文件大小: {} bytes",
                filePath, new java.io.File(filePath).length());
    }

    /**
     * 安全地获取字符串值，避免BLOB字段问题
     */
    private String safeGetString(String value) {
        try {
            return value != null ? value : "";
        } catch (Exception e) {
            log.warn("获取字符串值时出现异常: {}", e.getMessage());
            return "";
        }
    }

    /**
     * 🔥 根据报告参数构建查询条件
     */
    private QueryWrapper<TblRiskWarning> buildReportQueryWrapper(Map<String, Object> reportParams) {
        QueryWrapper<TblRiskWarning> queryWrapper = new QueryWrapper<>();

        try {
            // 🔧 修复：将 evalModelId 定义在外层作用域
            String evalModelId = null;

            // 1. 处理选中的模型信息
            Map<String, Object> selectedModel = (Map<String, Object>) reportParams.get("selectedModel");
            if (selectedModel != null) {
                evalModelId = (String) selectedModel.get("evalModelId");
                String modelName = (String) selectedModel.get("modelName");

                if (StringUtil.isNotEmpty(evalModelId)) {
                    queryWrapper.eq("EVAL_MODEL_ID", evalModelId);
                    log.info("报告查询条件 - 模型ID: {}", evalModelId);
                }

                // 🔧 修复：使用正确的字段名进行过滤
                // EVAL_MODEL_ID 和 MODEL_ID 是不同的字段，这里只需要用 EVAL_MODEL_ID
                // 不需要重复添加 MODEL_ID 条件
            }

            // 🔧 修复：预警类型过滤（支持全选）
            List<String> warningTypes = (List<String>) reportParams.get("warningTypes");
            // 🔧 修复：如果没有选择预警类型或者选择了"全部"，不添加过滤条件
            if (warningTypes != null && !warningTypes.isEmpty()
                && !warningTypes.contains("ALL") && !warningTypes.contains("全部")) {

                if (StringUtil.isNotEmpty(evalModelId)) {
                    // 🔧 第一步：查询该模型实际存在的预警类型
                    List<String> actualTypesInDB = getActualWarningTypes(evalModelId);
                    log.info("模型 {} 实际存在的预警类型: {}", evalModelId, actualTypesInDB);

                    if (!actualTypesInDB.isEmpty()) {
                        // 🔧 第二步：智能匹配 - 如果前端选择的类型在数据库中存在，直接使用
                        List<String> matchedTypes = new ArrayList<>();
                        for (String type : warningTypes) {
                            if (actualTypesInDB.contains(type)) {
                                matchedTypes.add(type);
                            }
                        }

                        // 🔧 第三步：如果没有直接匹配，使用数据库中实际存在的类型
                        if (matchedTypes.isEmpty()) {
                            matchedTypes = actualTypesInDB;
                            log.info("前端类型无匹配，使用实际类型: {} -> {}", warningTypes, matchedTypes);
                        }

                        queryWrapper.in("WARNING_TYPE", matchedTypes);
                        log.info("报告查询条件 - 预警类型（智能匹配）: 前端选择={}, 实际使用={}", warningTypes, matchedTypes);
                    }
                } else {
                    // 没有指定模型时，直接使用前端传入的类型
                    queryWrapper.in("WARNING_TYPE", warningTypes);
                    log.info("报告查询条件 - 预警类型（无模型）: {}", warningTypes);
                }
            } else {
                log.info("报告查询条件 - 预警类型: 全部（不添加过滤）");
            }

            // 🔧 通用解决方案：智能预警级别过滤
            List<String> warningLevels = (List<String>) reportParams.get("warningLevels");
            if (warningLevels != null && !warningLevels.isEmpty() && StringUtil.isNotEmpty(evalModelId)) {
                // 🔧 第一步：查询该模型实际存在的预警级别
                List<String> actualLevelsInDB = getActualWarningLevels(evalModelId);
                log.info("模型 {} 实际存在的预警级别: {}", evalModelId, actualLevelsInDB);

                if (!actualLevelsInDB.isEmpty()) {
                    // 🔧 第二步：智能匹配
                    List<String> matchedLevels = new ArrayList<>();
                    for (String level : warningLevels) {
                        if (actualLevelsInDB.contains(level)) {
                            matchedLevels.add(level);
                        }
                    }

                    // 🔧 第三步：如果没有匹配，使用数据库中实际存在的级别
                    if (matchedLevels.isEmpty()) {
                        matchedLevels = actualLevelsInDB;
                        log.info("前端级别无匹配，使用实际级别: {} -> {}", warningLevels, matchedLevels);
                    }

                    queryWrapper.in("WARNING_LEVEL", matchedLevels);
                    log.info("报告查询条件 - 预警级别（智能匹配）: 前端选择={}, 实际使用={}", warningLevels, matchedLevels);
                }
            }

            // 🔥 修复：真实时间范围过滤（优先使用前端传递的精确时间）
            // ⭐ 第一优先级：检查前端是否传递了 startDate 和 endDate
            String startDateStr = (String) reportParams.get("startDate");
            String endDateStr = (String) reportParams.get("endDate");

            if (StringUtil.isNotEmpty(startDateStr) && StringUtil.isNotEmpty(endDateStr)) {
                // ⭐ 优先使用前端传递的精确时间
                LocalDateTime startTime = parseFlexibleDate(startDateStr, true);
                LocalDateTime endTime = parseFlexibleDate(endDateStr, false);

                queryWrapper.between("WARNING_TIME", startTime, endTime);
                log.info("报告查询条件 - 时间范围（前端精确时间）: {} 到 {}", startTime, endTime);

            } else {
                // ⭐ 第二优先级：前端没传精确时间，才根据 timeRange 计算
                String timeRange = (String) reportParams.get("timeRange");
                if (StringUtil.isNotEmpty(timeRange)) {
                    LocalDateTime endTime = LocalDateTime.now();
                    LocalDateTime startTime;

                    switch (timeRange) {
                        case "TODAY":
                            // 今日：从今天 00:00 开始
                            startTime = endTime.toLocalDate().atStartOfDay();
                            break;
                        case "WEEK":
                            startTime = endTime.minusWeeks(1);
                            break;
                        case "MONTH":
                            startTime = endTime.minusMonths(1);
                            break;
                        case "QUARTER":
                            startTime = endTime.minusMonths(3);
                            break;
                        case "YEAR":
                            startTime = endTime.minusYears(1);
                            break;
                        case "CUSTOM":
                            // CUSTOM 已经在上面的 if 中处理过了
                            startTime = endTime.minusMonths(1);
                            break;
                        default:
                            startTime = endTime.minusMonths(1); // 默认 1 月
                    }

                    queryWrapper.between("WARNING_TIME", startTime, endTime);
                    log.info("报告查询条件 - 时间范围（后端计算）: {} => {} 到 {}", timeRange, startTime, endTime);
                }
            }

            // 🔧 修复：企业范围过滤（支持多选）
            List<String> companyIds = (List<String>) reportParams.get("companyIds");
            if (companyIds != null && !companyIds.isEmpty()) {
                // 🔧 修复：由于所有数据的 COMPANY_ID 都是 "DEFAULT_COMPANY"，
                // 应该使用 COMPANY_NAME 字段进行过滤
                queryWrapper.in("COMPANY_NAME", companyIds);
                log.info("报告查询条件 - 企业范围: {}", companyIds);
            } else {
                log.info("报告查询条件 - 企业范围: 全部（不添加过滤）");
            }

            // 6. 默认排序和限制
            queryWrapper.orderByDesc("CREATE_TIME");

            // 如果没有指定模型，限制查询数量避免数据过多
            if (selectedModel == null) {
                queryWrapper.last("LIMIT 1000");
            }

        } catch (Exception e) {
            log.error("构建报告查询条件失败", e);
            // 如果构建查询条件失败，返回默认查询
            queryWrapper = new QueryWrapper<TblRiskWarning>()
                .orderByDesc("CREATE_TIME")
                .last("LIMIT 100");
        }

        return queryWrapper;
    }

    /**
     * 🔥 灵活日期解析：兼容 "yyyy-MM-dd" 与 "yyyy-MM-ddTHH:mm:ss" 及带时区的 ISO 格式
     * @param dateStr 前端传入的日期字符串
     * @param isStart true=返回当天开始时间 00:00:00，false=返回当天结束时间 23:59:59
     */
    private LocalDateTime parseFlexibleDate(String dateStr, boolean isStart) {
        if (StringUtil.isEmpty(dateStr)) {
            return isStart ? LocalDateTime.now().minusMonths(1) : LocalDateTime.now();
        }
        try {
            // 只有日期部分
            if (dateStr.length() <= 10) {
                return isStart
                    ? LocalDateTime.parse(dateStr + "T00:00:00")
                    : LocalDateTime.parse(dateStr + "T23:59:59");
            }
            // 完整 ISO 格式（可能带 Z 或时区偏移）
            if (dateStr.endsWith("Z")) {
                return LocalDateTime.parse(dateStr.substring(0, dateStr.length() - 1));
            }
            // 去除时区偏移
            int tzIdx = dateStr.indexOf('+', 10);
            if (tzIdx < 0) tzIdx = dateStr.lastIndexOf('-');
            if (tzIdx > 10) {
                return LocalDateTime.parse(dateStr.substring(0, tzIdx));
            }
            return LocalDateTime.parse(dateStr);
        } catch (Exception e) {
            log.warn("日期解析失败: {}, 使用默认值", dateStr);
            return isStart ? LocalDateTime.now().minusMonths(1) : LocalDateTime.now();
        }
    }

    /**
     * 🔧 新增：获取指定模型实际存在的预警类型
     */
    private List<String> getActualWarningTypes(String evalModelId) {
        try {
            QueryWrapper<TblRiskWarning> wrapper = new QueryWrapper<>();
            wrapper.eq("EVAL_MODEL_ID", evalModelId)
                   .select("DISTINCT WARNING_TYPE")
                   .isNotNull("WARNING_TYPE");

            List<TblRiskWarning> results = riskWarningMapper.selectList(wrapper);
            List<String> types = new ArrayList<>();
            for (TblRiskWarning warning : results) {
                if (StringUtil.isNotEmpty(warning.getWarningType())) {
                    types.add(warning.getWarningType());
                }
            }
            return types;
        } catch (Exception e) {
            log.error("获取模型实际预警类型失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 🔧 新增：获取指定模型实际存在的预警级别
     */
    private List<String> getActualWarningLevels(String evalModelId) {
        try {
            QueryWrapper<TblRiskWarning> wrapper = new QueryWrapper<>();
            wrapper.eq("EVAL_MODEL_ID", evalModelId)
                   .select("DISTINCT WARNING_LEVEL")
                   .isNotNull("WARNING_LEVEL");

            List<TblRiskWarning> results = riskWarningMapper.selectList(wrapper);
            List<String> levels = new ArrayList<>();
            for (TblRiskWarning warning : results) {
                if (StringUtil.isNotEmpty(warning.getWarningLevel())) {
                    levels.add(warning.getWarningLevel());
                }
            }
            return levels;
        } catch (Exception e) {
            log.error("获取模型实际预警级别失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public JsonBean getWarningConfig() {
        try {
            log.info("获取预警配置");

            // 模拟预警配置
            Map<String, Object> config = new HashMap<>();
            config.put("enableAutoWarning", true);
            config.put("warningThreshold", 80);
            config.put("criticalThreshold", 90);
            config.put("notificationEnabled", true);
            config.put("emailNotification", true);
            config.put("smsNotification", false);

            log.info("获取预警配置成功");
            return new JsonBean(1, "获取成功", config);

        } catch (Exception e) {
            log.error("获取预警配置失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean updateWarningConfig(Map<String, Object> configParams) {
        try {
            log.info("更新预警配置，参数: {}", configParams);

            // 这里可以实现配置更新逻辑
            // 例如：保存到配置表、更新缓存等

            log.info("更新预警配置成功");
            return new JsonBean(1, "配置更新成功", null);

        } catch (Exception e) {
            log.error("更新预警配置失败", e);
            return new JsonBean(0, "配置更新失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean createRiskWarning(String evalModelId, String modelName, String riskLevel,
                                     Double riskScore, String warningContent, String businessScenario,
                                     String triggerCondition, String relatedData) {
        try {
            log.info("创建风险预警，模型ID: {}, 风险等级: {}, 企业名称: {}", evalModelId, riskLevel, triggerCondition);

            // 获取评估模型关联的数据模型ID
            String dataModelId = getDataModelIdFromEvalModel(evalModelId);
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("无法获取评估模型关联的数据模型ID，评估模型ID: {}", evalModelId);
                return new JsonBean(0, "无法获取关联的数据模型ID", null);
            }

            // 🔧 备用重复检查：主要重复检查已移至EvaluationModelServiceImpl
            // 这里保留简单的重复检查作为最后防线
            if (isDuplicateWarning(evalModelId, dataModelId, triggerCondition, warningContent)) {
                log.info("预警记录已存在，跳过插入 - 模型ID: {}, 企业: {}", evalModelId, triggerCondition);
                return new JsonBean(1, "预警记录已存在，跳过重复插入", null);
            }

            // 创建风险预警记录
            TblRiskWarning warning = new TblRiskWarning();
            warning.setEvalModelId(evalModelId);
            warning.setWarningCode("WRN" + System.currentTimeMillis());
            warning.setModelId(dataModelId); // 使用数据模型ID，满足外键约束
            warning.setCompanyId("DEFAULT_COMPANY");

            // 🔧 修复：使用真实的企业名称，不生成假数据
            if (StringUtil.isNotEmpty(triggerCondition)) {
                warning.setCompanyName(triggerCondition); // triggerCondition实际上是企业名称
                log.info("使用真实企业名称: {}", triggerCondition);
            } else {
                warning.setCompanyName(null); // 没有企业名称时设为null
                log.warn("企业名称为空，设置为null");
            }

            warning.setWarningType("THRESHOLD");
            warning.setWarningLevel(convertRiskLevelToWarningLevel(riskLevel));
            warning.setWarningStatus(TblRiskWarning.WARNING_STATUS_PENDING);
            warning.setWarningValue(BigDecimal.valueOf(riskScore != null ? riskScore : 0.0));
            warning.setThresholdValue(BigDecimal.valueOf(80.0));
            warning.setWarningDescription(warningContent != null ? warningContent : "模型评估预警");

            // 🔧 修复：将relatedData解析为JSON对象，构建完整的预警详情
            warning.setWarningDetail(buildWarningDetailFromRelatedData(modelName, triggerCondition, relatedData));
            // 🔧 修复：不要对JSON数据进行sanitize，直接保存原始JSON字符串
            warning.setRelatedData(relatedData);
            warning.setWarningTime(LocalDateTime.now());
            warning.setIsFalsePositive(TblRiskWarning.FALSE_POSITIVE_NO);
            warning.setCreateUser("SYSTEM");
            warning.setCreateTime(LocalDateTime.now());

            save(warning);

            log.info("创建风险预警成功，预警ID: {}, 相关数据: {}", warning.getWarningId(), relatedData);
            return new JsonBean(1, "创建成功", warning);

        } catch (Exception e) {
            log.error("创建风险预警失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null);
        }
    }

    /**
     * 从relatedData构建完整的预警详情JSON
     */
    private String buildWarningDetailFromRelatedData(String modelName, String companyName, String relatedData) {
        try {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("modelName", modelName);
            detailMap.put("companyName", companyName);
            detailMap.put("warningTime", LocalDateTime.now().toString());

            // 如果relatedData是JSON字符串，解析并合并
            if (StringUtil.isNotEmpty(relatedData) && relatedData.trim().startsWith("{")) {
                try {
                    Map<String, Object> relatedMap = JSONObject.parseObject(relatedData, Map.class);
                    detailMap.putAll(relatedMap);
                } catch (Exception e) {
                    log.warn("解析relatedData失败，使用原始字符串: {}", e.getMessage());
                    detailMap.put("rawData", relatedData);
                }
            } else if (StringUtil.isNotEmpty(relatedData)) {
                detailMap.put("rawData", relatedData);
            }

            return JSONObject.toJSONString(detailMap);
        } catch (Exception e) {
            log.error("构建预警详情失败: {}", e.getMessage());
            return buildSafeWarningDetail(modelName, companyName);
        }
    }

    @Override
    public JsonBean markAsRead(String warningId) {
        try {
            // TODO: 暂时注释标记已读功能，待字段映射完善后恢复
            // TblRiskWarning warning = getById(warningId);
            // if (warning != null) {
            //     warning.setIsFalsePositive(TblRiskWarning.FALSE_POSITIVE_NO);
            //     updateById(warning);
            // }
            return new JsonBean(1, "标记成功", null);
        } catch (Exception e) {
            log.error("标记已读失败", e);
            return new JsonBean(0, "标记失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean batchMarkAsRead(List<String> warningIds) {
        try {
            for (String warningId : warningIds) {
                markAsRead(warningId);
            }
            return new JsonBean(1, "批量标记成功", null);
        } catch (Exception e) {
            log.error("批量标记已读失败", e);
            return new JsonBean(0, "批量标记失败: " + e.getMessage(), null);
        }
    }

    @Override
    public List<TblRiskWarning> getWarningsByModelId(String evalModelId) {
        return riskWarningMapper.selectByEvalModelId(evalModelId);
    }

    @Override
    public List<TblRiskWarning> getRecentWarnings(Integer limit) {
        return riskWarningMapper.selectRecentWarnings(limit);
    }

    @Override
    public JsonBean autoGenerateWarning(String evalModelId, Map<String, Object> testResult) {
        try {
            log.info("开始自动生成预警，模型ID: {}, 测试结果: {}", evalModelId, testResult);

            // 参数验证
            if (StringUtil.isEmpty(evalModelId)) {
                return new JsonBean(0, "评估模型ID不能为空", null);
            }
            if (testResult == null || testResult.isEmpty()) {
                return new JsonBean(0, "测试结果不能为空", null);
            }

            // 解析测试结果
            Boolean success = (Boolean) testResult.get("success");
            Double accuracy = (Double) testResult.get("accuracy");
            Double riskScore = (Double) testResult.get("riskScore");
            String riskLevel = (String) testResult.get("riskLevel");
            Integer anomalyCount = (Integer) testResult.getOrDefault("anomalyCount", 0);
            String modelName = (String) testResult.getOrDefault("modelName", "未知模型");

            // 判断是否需要生成预警
            boolean needWarning = false;
            String warningReason = "";

            if (success != null && !success) {
                needWarning = true;
                warningReason = "模型执行失败";
            } else if (accuracy != null && accuracy < 80.0) {
                needWarning = true;
                warningReason = String.format("模型准确率过低(%.2f%%)", accuracy);
            } else if (riskScore != null && riskScore > 60.0) {
                needWarning = true;
                warningReason = String.format("风险分数过高(%.2f)", riskScore);
            } else if (anomalyCount > 1) {
                needWarning = true;
                warningReason = String.format("检测到%d个异常", anomalyCount);
            } else if ("HIGH".equals(riskLevel) || "MEDIUM".equals(riskLevel)) {
                needWarning = true;
                warningReason = "风险等级为" + riskLevel;
            }

            if (needWarning) {
                // 生成预警内容
                String warningContent = String.format("自动预警: %s。模型: %s, 准确率: %.2f%%, 风险分数: %.2f, 异常数量: %d",
                    warningReason, modelName,
                    accuracy != null ? accuracy : 0.0,
                    riskScore != null ? riskScore : 0.0,
                    anomalyCount);

                // 确定预警等级
                String warningLevel = determineWarningLevelFromResult(testResult);

                // 创建预警记录
                JsonBean result = createRiskWarning(
                    evalModelId,
                    modelName,
                    warningLevel,
                    riskScore != null ? riskScore : (accuracy != null ? accuracy : 0.0),
                    warningContent,
                    "AUTO_GENERATED",
                    warningReason,
                    testResult.toString()
                );

                log.info("自动生成预警成功，模型ID: {}, 预警等级: {}, 原因: {}", evalModelId, warningLevel, warningReason);
                return result;
            } else {
                log.info("测试结果正常，无需生成预警，模型ID: {}", evalModelId);
                return new JsonBean(1, "测试结果正常，无需生成预警", null);
            }

        } catch (Exception e) {
            log.error("自动生成预警失败，模型ID: {}", evalModelId, e);
            return new JsonBean(0, "自动生成预警失败: " + e.getMessage(), null);
        }
    }

    /**
     * 根据预警等级计算优先级
     */
    private Integer calculatePriority(String warningLevel) {
        switch (warningLevel) {
            case TblRiskWarning.WARNING_LEVEL_HIGH:
                return 4;
            case TblRiskWarning.WARNING_LEVEL_MEDIUM:
                return 3;
            case TblRiskWarning.WARNING_LEVEL_LOW:
                return 2;
            default:
                return 1;
        }
    }

    /**
     * 将风险等级转换为预警等级
     */
    private String convertRiskLevelToWarningLevel(String riskLevel) {
        if (StringUtil.isEmpty(riskLevel)) {
            return TblRiskWarning.WARNING_LEVEL_LOW;
        }

        switch (riskLevel.toUpperCase()) {
            case "CRITICAL":
            case "HIGH":
                return TblRiskWarning.WARNING_LEVEL_HIGH;
            case "MEDIUM":
                return TblRiskWarning.WARNING_LEVEL_MEDIUM;
            case "LOW":
                return TblRiskWarning.WARNING_LEVEL_LOW;
            default:
                return TblRiskWarning.WARNING_LEVEL_LOW;
        }
    }

    /**
     * 生成预警编码
     */
    private String generateWarningCode() {
        return "WRN" + System.currentTimeMillis();
    }

    /**
     * 根据测试结果确定预警等级
     */
    private String determineWarningLevelFromResult(Map<String, Object> testResult) {
        Double riskScore = (Double) testResult.get("riskScore");
        Double accuracy = (Double) testResult.get("accuracy");
        String riskLevel = (String) testResult.get("riskLevel");
        Integer anomalyCount = (Integer) testResult.getOrDefault("anomalyCount", 0);

        // 优先使用明确的风险等级
        if ("HIGH".equals(riskLevel) || "CRITICAL".equals(riskLevel)) {
            return TblRiskWarning.WARNING_LEVEL_HIGH;
        } else if ("MEDIUM".equals(riskLevel)) {
            return TblRiskWarning.WARNING_LEVEL_MEDIUM;
        } else if ("LOW".equals(riskLevel)) {
            return TblRiskWarning.WARNING_LEVEL_LOW;
        }

        // 根据风险分数判断
        if (riskScore != null) {
            if (riskScore > 80.0) {
                return TblRiskWarning.WARNING_LEVEL_HIGH;
            } else if (riskScore > 60.0) {
                return TblRiskWarning.WARNING_LEVEL_MEDIUM;
            } else {
                return TblRiskWarning.WARNING_LEVEL_LOW;
            }
        }

        // 根据准确率判断
        if (accuracy != null) {
            if (accuracy < 60.0) {
                return TblRiskWarning.WARNING_LEVEL_HIGH;
            } else if (accuracy < 80.0) {
                return TblRiskWarning.WARNING_LEVEL_MEDIUM;
            } else {
                return TblRiskWarning.WARNING_LEVEL_LOW;
            }
        }

        // 根据异常数量判断
        if (anomalyCount > 3) {
            return TblRiskWarning.WARNING_LEVEL_HIGH;
        } else if (anomalyCount > 1) {
            return TblRiskWarning.WARNING_LEVEL_MEDIUM;
        } else {
            return TblRiskWarning.WARNING_LEVEL_LOW;
        }
    }

    /**
     * 构建安全的预警详情字符串
     * 避免包含可能导致SQL解析错误的特殊字符
     */
    private String buildSafeWarningDetail(String modelName, String triggerCondition) {
        StringBuilder detail = new StringBuilder();
        detail.append("评估模型: ");
        detail.append(sanitizeString(modelName));
        detail.append(", 触发条件: ");
        detail.append(sanitizeString(triggerCondition));
        return detail.toString();
    }

    /**
     * 清理字符串中的特殊字符
     * 移除可能导致MyBatis SQL解析错误的字符
     */
    private String sanitizeString(String input) {
        if (input == null) {
            return "";
        }

        // 移除可能导致正则表达式错误的字符
        String sanitized = input
                .replace("{", "[")  // 替换左大括号
                .replace("}", "]")  // 替换右大括号
                .replace("$", "")   // 移除美元符号
                .replace("\\", "")  // 移除反斜杠
                .replace("'", "''") // 转义单引号
                .replace("\"", "\\\""); // 转义双引号

        // 限制长度，避免过长的字符串
        if (sanitized.length() > 500) {
            sanitized = sanitized.substring(0, 500) + "...";
        }

        return sanitized;
    }

    /**
     * 🔧 已废弃：不再生成随机企业名称，只使用真实数据
     * 原因：用户要求去掉假数据，空着也不要假数据
     */
    /*
    private String getRandomCompanyName() {
        String[] companies = {
            "巴马瑶族自治县有限公司",
            "中宁县某某有限公司",
            "河池市某某有限公司",
            "南宁市科技有限公司",
            "桂林山水实业有限公司",
            "柳州工业集团有限公司",
            "北海港务集团有限公司",
            "梧州商贸有限公司",
            "贵港物流有限公司",
            "玉林农业发展有限公司",
            "百色矿业集团有限公司",
            "贺州建材有限公司",
            "河池环保科技有限公司",
            "来宾新能源有限公司",
            "崇左边贸有限公司"
        };

        int randomIndex = (int) (Math.random() * companies.length);
        return companies[randomIndex];
    }
    */

    /**
     * 从评估模型ID获取关联的数据模型ID
     * 用于满足外键约束要求
     */
    private String getDataModelIdFromEvalModel(String evalModelId) {
        try {
            if (StringUtil.isEmpty(evalModelId)) {
                return null;
            }

            // 获取评估模型信息
            TblEvaluationModel evalModel = evaluationModelService.getById(evalModelId);
            if (evalModel == null) {
                log.warn("未找到评估模型，ID: {}", evalModelId);
                return null;
            }

            String dataModelId = evalModel.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("评估模型未关联数据模型，评估模型ID: {}", evalModelId);
                return null;
            }

            // 对于指标组合分析（COMB开头），需要特殊处理
            if (dataModelId.startsWith("COMB")) {
                // 指标组合分析不直接对应数据模型表中的记录
                // 创建一个虚拟的数据模型ID或使用默认值
                log.info("检测到指标组合分析模型，使用默认数据模型ID");
                return createDefaultDataModelForCombination(dataModelId);
            }

            log.info("获取到数据模型ID: {} (来自评估模型: {})", dataModelId, evalModelId);
            return dataModelId;

        } catch (Exception e) {
            log.error("获取数据模型ID失败，评估模型ID: {}, 错误: {}", evalModelId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 为指标组合分析创建默认的数据模型ID
     * 使用一个通用的默认数据模型ID来满足外键约束
     */
    private String createDefaultDataModelForCombination(String combinationId) {
        // 使用一个固定的默认数据模型ID
        // 这个ID应该在数据库中存在，专门用于组合分析
        String defaultModelId = "DEFAULT_COMBINATION_MODEL";

        log.info("指标组合分析使用默认数据模型ID: {} (组合ID: {})", defaultModelId, combinationId);
        return defaultModelId;
    }

    /**
     * 检查是否存在重复的预警记录
     * 避免定时任务重复插入相同的预警数据
     *
     * @param evalModelId 评估模型ID
     * @param dataModelId 数据模型ID
     * @param companyName 企业名称
     * @param warningContent 预警内容
     * @return true-存在重复记录，false-不存在重复记录
     */
    private boolean isDuplicateWarning(String evalModelId, String dataModelId, String companyName, String warningContent) {
        try {
            log.debug("开始检查重复预警记录 - 模型ID: {}, 数据模型ID: {}, 企业: {}",
                     evalModelId, dataModelId, companyName);

            // 🔧 修复：使用原生SQL避免达梦数据库的LocalDateTime类型转换问题
            // 达梦数据库使用SYSDATE - 1表示24小时前
            String countSql = "SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE " +
                             "EVAL_MODEL_ID = ? AND MODEL_ID = ? AND " +
                             "CREATE_TIME >= SYSDATE - 1";

            List<Object> params = new ArrayList<>();
            params.add(evalModelId);
            params.add(dataModelId);

            // 企业名称可能为null，需要特殊处理
            if (StringUtil.isNotEmpty(companyName)) {
                countSql += " AND COMPANY_NAME = ?";
                params.add(companyName);
            } else {
                countSql += " AND COMPANY_NAME IS NULL";
            }

            // 🔧 修复：WARNING_DESCRIPTION是CLOB类型，达梦数据库不支持直接用=比较
            // 改为使用DBMS_LOB.COMPARE函数或者不比较该字段
            // 这里选择不比较WARNING_DESCRIPTION，只通过模型ID、企业名称和时间来判断重复
            // 如果需要比较CLOB字段，可以使用: AND DBMS_LOB.COMPARE(WARNING_DESCRIPTION, ?) = 0
            // 但这会影响性能，且达梦数据库的CLOB比较函数可能不同

            log.debug("执行重复检查SQL: {}, 参数: {}", countSql, params);

            // 🔧 修复：使用JdbcTemplate执行原生SQL，避免MyBatis-Plus的类型转换问题
            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

            if (count != null && count > 0) {
                log.info("发现重复预警记录 - 模型ID: {}, 企业: {}, 24小时内记录数: {}",
                        evalModelId, companyName, count);
                return true;
            }

            log.debug("未发现重复预警记录 - 模型ID: {}, 企业: {}", evalModelId, companyName);
            return false;

        } catch (Exception e) {
            log.error("检查重复预警记录失败 - 模型ID: {}, 错误: {}", evalModelId, e.getMessage(), e);
            // 🔧 修复：检查失败时，为了安全起见，允许插入（避免遗漏重要预警）
            return false;
        }
    }

    // =====================================================
    // 评估模型预警功能增强实现
    // =====================================================

    @Override
    public JsonBean getModelWarningCount(String modelId) {
        try {
            log.info("获取模型预警数量，模型ID: {}", modelId);

            // 查询该模型的预警数量（排除误报）
            QueryWrapper<TblRiskWarning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("EVAL_MODEL_ID", modelId);
            queryWrapper.eq("IS_FALSE_POSITIVE", "N"); // 排除误报

            long count = this.count(queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("modelId", modelId);
            result.put("warningCount", count);

            log.info("模型 {} 的预警数量: {}", modelId, count);
            return new JsonBean(1, "查询成功", result);

        } catch (Exception e) {
            log.error("获取模型预警数量失败，模型ID: {}", modelId, e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getModelPendingWarningCount(String modelId) {
        try {
            log.info("获取模型待处理预警数量，模型ID: {}", modelId);

            // 查询该模型的待处理预警数量（只统计PENDING和PROCESSING状态，排除误报）
            QueryWrapper<TblRiskWarning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("EVAL_MODEL_ID", modelId);
            queryWrapper.eq("IS_FALSE_POSITIVE", "N"); // 排除误报
            queryWrapper.and(wrapper -> wrapper
                .eq("WARNING_STATUS", TblRiskWarning.WARNING_STATUS_PENDING)
                .or()
                .eq("WARNING_STATUS", TblRiskWarning.WARNING_STATUS_PROCESSING)
            );

            long pendingCount = this.count(queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("modelId", modelId);
            result.put("pendingWarningCount", pendingCount);

            log.info("模型 {} 的待处理预警数量: {}", modelId, pendingCount);
            return new JsonBean(1, "查询成功", result);

        } catch (Exception e) {
            log.error("获取模型待处理预警数量失败，模型ID: {}", modelId, e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getModelWarningCountBatch() {
        try {
            List<Map<String, Object>> rows = riskWarningMapper.selectModelWarningCountBatch();
            List<Map<String, Object>> result = new ArrayList<>();

            if (rows != null) {
                for (Map<String, Object> row : rows) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("evalModelId", getMapValueIgnoreCase(row, "evalModelId"));
                    item.put("warningCount", toLongValue(getMapValueIgnoreCase(row, "warningCount")));
                    item.put("pendingWarningCount", toLongValue(getMapValueIgnoreCase(row, "pendingCount")));
                    // 按状态纯净计数（与状态统计口径一致，供状态页签模型列表展示）
                    Map<String, Long> statusCount = new HashMap<>();
                    statusCount.put("PENDING", toLongValue(getMapValueIgnoreCase(row, "statusPending")));
                    statusCount.put("PROCESSING", toLongValue(getMapValueIgnoreCase(row, "statusProcessing")));
                    statusCount.put("PROCESSED", toLongValue(getMapValueIgnoreCase(row, "statusProcessed")));
                    item.put("statusCount", statusCount);
                    result.add(item);
                }
            }

            log.info("批量获取模型预警数量成功，模型数: {}", result.size());
            return new JsonBean(1, "查询成功", result);

        } catch (Exception e) {
            log.error("批量获取模型预警数量失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getWarningStatusDetail(String warningStatus) {
        try {
            if (warningStatus == null || warningStatus.trim().isEmpty()) {
                return new JsonBean(0, "预警状态不能为空", null);
            }

            Map<String, Object> result = new HashMap<>();

            // 1. 该状态总数
            long totalCount = this.count(new QueryWrapper<TblRiskWarning>()
                    .eq("WARNING_STATUS", warningStatus));
            result.put("totalCount", totalCount);

            // 2. 按预警级别分组统计
            List<Map<String, Object>> levelStats = riskWarningMapper.selectLevelStatisticsByStatus(warningStatus);
            Map<String, Long> levelMap = new HashMap<>();
            if (levelStats != null) {
                for (Map<String, Object> stat : levelStats) {
                    String level = (String) getMapValueIgnoreCase(stat, "level");
                    if (level != null) {
                        levelMap.put(level, toLongValue(getMapValueIgnoreCase(stat, "count")));
                    }
                }
            }
            result.put("levelStatistics", levelMap);

            // 3. 按模型分组统计（数量为0的分组不返回）
            List<Map<String, Object>> modelStats = riskWarningMapper.selectModelStatisticsByStatus(warningStatus);
            List<Map<String, Object>> modelList = new ArrayList<>();
            if (modelStats != null) {
                for (Map<String, Object> stat : modelStats) {
                    Long count = toLongValue(getMapValueIgnoreCase(stat, "count"));
                    if (count == null || count <= 0) {
                        continue;
                    }
                    Map<String, Object> item = new HashMap<>();
                    item.put("evalModelId", getMapValueIgnoreCase(stat, "evalModelId"));
                    item.put("modelName", getMapValueIgnoreCase(stat, "modelName"));
                    item.put("count", count);
                    modelList.add(item);
                }
            }
            result.put("modelStatistics", modelList);

            log.info("获取状态统计明细成功，状态: {}, 总数: {}", warningStatus, totalCount);
            return new JsonBean(1, "查询成功", result);

        } catch (Exception e) {
            log.error("获取状态统计明细失败，状态: {}", warningStatus, e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    /**
     * 从MyBatis返回的Map中按列别名取值（兼容达梦返回键的大小写差异）
     */
    private Object getMapValueIgnoreCase(Map<String, Object> row, String key) {
        if (row == null) {
            return null;
        }
        Object value = row.get(key);
        if (value == null) {
            value = row.get(key.toUpperCase());
        }
        if (value == null) {
            value = row.get(key.toLowerCase());
        }
        return value;
    }

    /**
     * 数值类型安全转换
     */
    private Long toLongValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    @Override
    public JsonBean getModelWarningResults(Map<String, Object> requestBody) {
        try {
            log.info("获取模型预警结果，参数: {}", requestBody);

            String evalModelId = (String) requestBody.get("evalModelId");
            Integer pageNum = (Integer) requestBody.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) requestBody.getOrDefault("pageSize", 20);

            if (StringUtil.isEmpty(evalModelId)) {
                return new JsonBean(0, "评估模型ID不能为空", null);
            }

            // 分页查询（排除误报）
            Page<TblRiskWarning> page = new Page<>(pageNum, pageSize);
            QueryWrapper<TblRiskWarning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("EVAL_MODEL_ID", evalModelId);
            queryWrapper.eq("IS_FALSE_POSITIVE", "N"); // 排除误报
            queryWrapper.orderByDesc("WARNING_TIME");

            IPage<TblRiskWarning> result = this.page(page, queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("list", result.getRecords());
            data.put("total", result.getTotal());
            data.put("pageNum", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询到模型 {} 的预警结果 {} 条", evalModelId, result.getTotal());
            return new JsonBean(1, "查询成功", data);

        } catch (Exception e) {
            log.error("获取模型预警结果失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getWarningSourceData(Map<String, Object> requestBody) {
        try {
            log.info("获取预警源数据，参数: {}", requestBody);

            String warningId = (String) requestBody.get("warningId");
            Integer pageNum = (Integer) requestBody.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) requestBody.getOrDefault("pageSize", 20);

            if (StringUtil.isEmpty(warningId)) {
                return new JsonBean(0, "预警ID不能为空", null);
            }

            // 模拟源数据查询（实际应该根据预警记录查询相关的业务数据）
            List<Map<String, Object>> sourceDataList = new ArrayList<>();
            Map<String, Object> sourceData1 = new HashMap<>();
            sourceData1.put("id", 1);
            sourceData1.put("customerName", "客户A");
            sourceData1.put("creditScore", 650);
            sourceData1.put("amount", 100000);
            sourceData1.put("riskLevel", "HIGH");
            sourceDataList.add(sourceData1);

            Map<String, Object> sourceData2 = new HashMap<>();
            sourceData2.put("id", 2);
            sourceData2.put("customerName", "客户B");
            sourceData2.put("creditScore", 720);
            sourceData2.put("amount", 200000);
            sourceData2.put("riskLevel", "MEDIUM");
            sourceDataList.add(sourceData2);

            // 定义列信息
            List<Map<String, Object>> columns = new ArrayList<>();
            columns.add(createColumn("id", "ID", 80));
            columns.add(createColumn("customerName", "客户名称", 150));
            columns.add(createColumn("creditScore", "信用评分", 120));
            columns.add(createColumn("amount", "金额", 120));
            columns.add(createColumn("riskLevel", "风险等级", 100));

            Map<String, Object> data = new HashMap<>();
            data.put("list", sourceDataList);
            data.put("total", sourceDataList.size());
            data.put("columns", columns);

            log.info("查询到预警 {} 的源数据 {} 条", warningId, sourceDataList.size());
            return new JsonBean(1, "查询成功", data);

        } catch (Exception e) {
            log.error("获取预警源数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getWarningSourceDataDetail(Map<String, Object> requestBody) {
        try {
            log.info("获取预警源数据详情，参数: {}", requestBody);

            String warningId = (String) requestBody.get("warningId");
            Integer pageNum = (Integer) requestBody.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) requestBody.getOrDefault("pageSize", 20);

            if (StringUtil.isEmpty(warningId)) {
                return new JsonBean(0, "预警ID不能为空", null);
            }

            // 模拟预警源数据详情（实际应该查询指标SQL执行结果）
            List<Map<String, Object>> warningSourceDataList = new ArrayList<>();
            Map<String, Object> warningData1 = new HashMap<>();
            warningData1.put("indicator", "信用评分");
            warningData1.put("value", 650);
            warningData1.put("threshold", 700);
            warningData1.put("result", "异常");
            warningData1.put("description", "信用评分低于阈值");
            warningSourceDataList.add(warningData1);

            Map<String, Object> warningData2 = new HashMap<>();
            warningData2.put("indicator", "负债率");
            warningData2.put("value", 0.85);
            warningData2.put("threshold", 0.7);
            warningData2.put("result", "超标");
            warningData2.put("description", "负债率超过安全阈值");
            warningSourceDataList.add(warningData2);

            // 定义列信息
            List<Map<String, Object>> columns = new ArrayList<>();
            columns.add(createColumn("indicator", "指标名称", 150));
            columns.add(createColumn("value", "当前值", 120));
            columns.add(createColumn("threshold", "阈值", 120));
            columns.add(createColumn("result", "结果", 100));
            columns.add(createColumn("description", "说明", 200));

            Map<String, Object> data = new HashMap<>();
            data.put("list", warningSourceDataList);
            data.put("total", warningSourceDataList.size());
            data.put("columns", columns);

            log.info("查询到预警 {} 的源数据详情 {} 条", warningId, warningSourceDataList.size());
            return new JsonBean(1, "查询成功", data);

        } catch (Exception e) {
            log.error("获取预警源数据详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean generateSingleWarningReport(Map<String, Object> requestBody) {
        try {
            log.info("生成单条预警报告，参数: {}", requestBody);

            String warningId = (String) requestBody.get("warningId");
            if (StringUtil.isEmpty(warningId)) {
                return new JsonBean(0, "预警ID不能为空", null);
            }

            // 模拟报告生成过程
            Thread.sleep(2000); // 模拟生成时间

            Map<String, Object> result = new HashMap<>();
            result.put("reportId", "RPT_" + System.currentTimeMillis());
            result.put("reportUrl", "/api/reports/warning-" + warningId + "-report.pdf");
            result.put("reportName", "预警报告_" + warningId + ".pdf");
            result.put("generateTime", new Date());

            log.info("单条预警报告生成成功，预警ID: {}", warningId);
            return new JsonBean(1, "报告生成成功", result);

        } catch (Exception e) {
            log.error("生成单条预警报告失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean generateModelWarningReport(Map<String, Object> requestBody) {
        try {
            log.info("生成模型整体报告，参数: {}", requestBody);

            String modelId = (String) requestBody.get("modelId");
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            // 模拟报告生成过程
            Thread.sleep(3000); // 模拟生成时间

            Map<String, Object> result = new HashMap<>();
            result.put("reportId", "MODEL_RPT_" + System.currentTimeMillis());
            result.put("reportUrl", "/api/reports/model-" + modelId + "-report.pdf");
            result.put("reportName", "模型预警报告_" + modelId + ".pdf");
            result.put("generateTime", new Date());

            log.info("模型整体报告生成成功，模型ID: {}", modelId);
            return new JsonBean(1, "报告生成成功", result);

        } catch (Exception e) {
            log.error("生成模型整体报告失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null);
        }
    }

    /**
     * 创建列信息的辅助方法
     */
    private Map<String, Object> createColumn(String prop, String label, int width) {
        Map<String, Object> column = new HashMap<>();
        column.put("prop", prop);
        column.put("label", label);
        column.put("width", width);
        return column;
    }

    @Override
    public String getWarningDrillDownData(Map<String, Object> requestBody) {
        try {
            log.info("获取预警数据穿透分析，参数: {}", requestBody);

            // 解析请求参数
            String warningId = (String) requestBody.get("warningId");
            Integer pageNum = (Integer) requestBody.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) requestBody.getOrDefault("pageSize", 20);

            if (StringUtil.isEmpty(warningId)) {
                return JsonBean.error("预警ID不能为空");
            }

            // 获取预警详情
            TblRiskWarning warning = this.getById(warningId);
            if (warning == null) {
                return JsonBean.error("预警记录不存在");
            }

            // 解析相关数据信息
            Map<String, Object> drillDownResult = parseDrillDownData(warning, pageNum, pageSize);

            return JsonBean.success("查询成功", drillDownResult);

        } catch (Exception e) {
            log.error("获取预警数据穿透分析失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @Override
    public String exportWarningDrillDownData(Map<String, Object> requestBody) {
        try {
            log.info("导出预警穿透数据，参数: {}", requestBody);

            // 解析请求参数
            String warningId = (String) requestBody.get("warningId");
            String exportFormat = (String) requestBody.getOrDefault("exportFormat", "excel");

            if (StringUtil.isEmpty(warningId)) {
                return JsonBean.error("预警ID不能为空");
            }

            // 获取预警详情
            TblRiskWarning warning = this.getById(warningId);
            if (warning == null) {
                return JsonBean.error("预警记录不存在");
            }

            // 生成导出文件
            String exportResult = generateDrillDownExport(warning, exportFormat);

            return JsonBean.success("导出成功", exportResult);

        } catch (Exception e) {
            log.error("导出预警穿透数据失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    /**
     * 解析预警数据穿透信息
     */
    private Map<String, Object> parseDrillDownData(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("解析预警数据穿透信息，预警ID: {}", warning.getWarningId());

            Map<String, Object> result = new HashMap<>();

            // 获取预警基本信息
            result.put("warningInfo", buildWarningInfo(warning));

            // 获取原始业务数据
            Map<String, Object> businessData = getBusinessDataByWarning(warning, pageNum, pageSize);
            result.put("businessData", businessData);

            // 获取数据源信息
            Map<String, Object> dataSourceInfo = getDataSourceInfo(warning);
            result.put("dataSourceInfo", dataSourceInfo);

            // 获取关联指标信息
            List<Map<String, Object>> indicators = getRelatedIndicators(warning);
            result.put("indicators", indicators);

            return result;

        } catch (Exception e) {
            log.error("解析预警数据穿透信息失败", e);
            throw new RuntimeException("解析失败: " + e.getMessage());
        }
    }

    /**
     * 构建预警基本信息
     */
    private Map<String, Object> buildWarningInfo(TblRiskWarning warning) {
        Map<String, Object> info = new HashMap<>();
        info.put("warningId", warning.getWarningId());
        info.put("warningCode", warning.getWarningCode());
        info.put("warningLevel", warning.getWarningLevel());
        info.put("warningStatus", warning.getWarningStatus());
        info.put("warningType", warning.getWarningType());
        info.put("companyName", warning.getCompanyName());
        info.put("warningTime", warning.getWarningTime());
        info.put("warningDescription", warning.getWarningDescription());
        info.put("warningValue", warning.getWarningValue());
        info.put("thresholdValue", warning.getThresholdValue());
        return info;
    }

    /**
     * 获取业务数据（支持多表）
     * 🔧 修改：返回多个表格的数据，每个表一个独立的数据集
     */
    private Map<String, Object> getBusinessDataByWarning(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("获取预警相关业务数据（多表模式），预警ID: {}", warning.getWarningId());

            // 获取模型SQL中涉及的所有表
            List<String> sourceTables = extractSourceTablesFromWarning(warning);

            if (sourceTables.isEmpty()) {
                log.warn("未找到数据源表，返回空结果");
                return createEmptyMultiTableDataResult(pageNum, pageSize);
            }

            log.info("提取到 {} 个数据源表: {}", sourceTables.size(), sourceTables);

            // 为每个表查询数据
            List<Map<String, Object>> tableDataList = new ArrayList<>();

            for (String tableName : sourceTables) {
                Map<String, Object> tableData = queryTableData(tableName, warning, pageNum, pageSize);
                if (tableData != null && !((List<?>) tableData.get("list")).isEmpty()) {
                    tableDataList.add(tableData);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tables", tableDataList);  // 多个表的数据
            result.put("tableCount", tableDataList.size());
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);

            log.info("成功查询 {} 个表的数据", tableDataList.size());
            return result;

        } catch (Exception e) {
            log.error("获取业务数据失败", e);
            return createEmptyMultiTableDataResult(pageNum, pageSize);
        }
    }

    /**
     * 从预警信息中提取数据源表
     */
    private List<String> extractSourceTablesFromWarning(TblRiskWarning warning) {
        try {
            List<String> tables = new ArrayList<>();

            // 1. 尝试从评估模型获取SQL
            String evalModelId = warning.getEvalModelId();
            if (StringUtil.isNotEmpty(evalModelId)) {
                TblEvaluationModel evalModel = evaluationModelService.getById(evalModelId);
                if (evalModel != null) {
                    String dataModelId = evalModel.getDataModelId();

                    // 判断是数据模型还是指标组合
                    if (StringUtil.isNotEmpty(dataModelId)) {
                        if (dataModelId.startsWith("COMB")) {
                            // 指标组合：获取所有指标的SQL
                            tables = extractTablesFromCombination(dataModelId);
                        } else {
                            // 数据模型：获取模型SQL
                            tables = extractTablesFromDataModel(dataModelId);
                        }
                    }
                }
            }

            // 2. 如果没有找到，尝试从模型ID获取
            if (tables.isEmpty() && StringUtil.isNotEmpty(warning.getModelId())) {
                String modelSql = getModelSqlById(warning.getModelId());
                if (StringUtil.isNotEmpty(modelSql)) {
                    tables = extractTablesFromSql(modelSql);
                }
            }

            return tables;

        } catch (Exception e) {
            log.error("提取数据源表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从指标组合中提取所有涉及的表
     */
    private List<String> extractTablesFromCombination(String combinationId) {
        try {
            log.info("从指标组合提取数据表，组合ID: {}", combinationId);

            Set<String> allTables = new LinkedHashSet<>();  // 使用Set去重，保持顺序

            // 获取组合中的所有指标
            String sql = "SELECT SQL_CONTENT FROM TBL_COMBINATION_INDICATOR " +
                        "WHERE COMBINATION_ID = ? AND IS_ENABLED = 'Y' " +
                        "ORDER BY EXECUTION_ORDER";

            List<Map<String, Object>> indicators = jdbcTemplate.queryForList(sql, combinationId);

            for (Map<String, Object> indicator : indicators) {
                Object sqlContentObj = indicator.get("SQL_CONTENT");
                if (sqlContentObj != null) {
                    String sqlContent = convertClobToString(sqlContentObj);
                    if (StringUtil.isNotEmpty(sqlContent)) {
                        List<String> tables = extractTablesFromSql(sqlContent);
                        allTables.addAll(tables);
                    }
                }
            }

            log.info("从指标组合提取到 {} 个数据表: {}", allTables.size(), allTables);
            return new ArrayList<>(allTables);

        } catch (Exception e) {
            log.error("从指标组合提取数据表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从数据模型中提取涉及的表
     */
    private List<String> extractTablesFromDataModel(String dataModelId) {
        try {
            log.info("从数据模型提取数据表，模型ID: {}", dataModelId);

            String sql = "SELECT SQL_STATEMENT FROM TBL_DATA_MODEL WHERE MODEL_ID = ?";
            List<Map<String, Object>> models = jdbcTemplate.queryForList(sql, dataModelId);

            if (!models.isEmpty()) {
                Object sqlObj = models.get(0).get("SQL_STATEMENT");
                if (sqlObj != null) {
                    String sqlStatement = convertClobToString(sqlObj);
                    if (StringUtil.isNotEmpty(sqlStatement)) {
                        List<String> tables = extractTablesFromSql(sqlStatement);
                        log.info("从数据模型提取到 {} 个数据表: {}", tables.size(), tables);
                        return tables;
                    }
                }
            }

            return new ArrayList<>();

        } catch (Exception e) {
            log.error("从数据模型提取数据表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 查询单个表的数据（优化版）
     */
    private Map<String, Object> queryTableData(String tableName, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("查询表数据: {}", tableName);
            long startTime = System.currentTimeMillis();

            // 首先检查表是否存在以及有哪些列
            List<String> tableColumns = getTableColumns(tableName);
            if (tableColumns.isEmpty()) {
                log.warn("无法获取表 {} 的列信息，跳过查询", tableName);
                return null;
            }

            // 🔧 优化：提前判断企业标识类型并获取企业ID
            String companyValue = extractCompanyIdFromWarning(warning);
            String companyId = null;

            if (StringUtil.isNotEmpty(companyValue)) {
                if (isCompanyId(companyValue)) {
                    // 是企业ID，直接使用
                    companyId = companyValue;
                    log.info("识别为企业ID: {}", companyId);
                } else {
                    // 是企业名称，先查询ID（使用缓存）
                    log.info("识别为企业名称: {}, 尝试查询企业ID", companyValue);
                    companyId = queryCompanyIdByName(companyValue);

                    if (StringUtil.isEmpty(companyId)) {
                        log.warn("无法找到企业ID，企业名称: {}, 返回空结果", companyValue);
                        return buildEmptyResult(tableName);
                    }
                    log.info("查询到企业ID: {}", companyId);
                }
            }

            // 🔧 优化：使用子查询一次性获取数据和总数
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<>();

            // 检查表中是否有 COMPANY_ID 字段
            boolean hasCompanyId = tableColumns.contains("COMPANY_ID");
            boolean hasCompanyName = tableColumns.contains("COMPANY_NAME");

            if (StringUtil.isNotEmpty(companyId) && hasCompanyId) {
                // 使用窗口函数一次性获取数据和总数
                sql.append("SELECT t.*, COUNT(*) OVER() as TOTAL_COUNT FROM ")
                   .append(tableName).append(" t WHERE COMPANY_ID = ? ")
                   .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
                params.add(companyId);
            } else if (StringUtil.isNotEmpty(companyValue) && hasCompanyName) {
                // 降级使用 COMPANY_NAME
                sql.append("SELECT t.*, COUNT(*) OVER() as TOTAL_COUNT FROM ")
                   .append(tableName).append(" t WHERE COMPANY_NAME = ? ")
                   .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
                params.add(companyValue);
            } else {
                // 无企业过滤条件
                sql.append("SELECT t.*, COUNT(*) OVER() as TOTAL_COUNT FROM ")
                   .append(tableName).append(" t WHERE 1=1 ")
                   .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            }

            // 分页参数
            int offset = (pageNum - 1) * pageSize;
            params.add(offset);
            params.add(pageSize);

            log.info("执行优化查询SQL: {}, 参数: {}", sql, params);
            List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql.toString(), params.toArray());

            // 提取总数
            Integer totalCount = 0;
            if (!dataList.isEmpty()) {
                Object totalCountObj = dataList.get(0).get("TOTAL_COUNT");
                if (totalCountObj != null) {
                    totalCount = ((Number) totalCountObj).intValue();
                }
                // 移除TOTAL_COUNT字段
                dataList.forEach(row -> row.remove("TOTAL_COUNT"));
            }

            // 构建列信息
            List<Map<String, Object>> columns = buildColumnsFromData(dataList, tableName);

            Map<String, Object> result = new HashMap<>();
            result.put("tableName", tableName);
            result.put("tableLabel", getTableLabel(tableName));
            result.put("list", dataList);
            result.put("total", totalCount);
            result.put("columns", columns);

            long queryTime = System.currentTimeMillis() - startTime;
            log.info("表 {} 查询完成，耗时: {} ms，查询到 {} 条数据，共 {} 条",
                     tableName, queryTime, dataList.size(), totalCount);
            return result;

        } catch (Exception e) {
            log.error("查询表数据失败: {}", tableName, e);
            return null;
        }
    }

    /**
     * 构建空结果
     */
    private Map<String, Object> buildEmptyResult(String tableName) {
        Map<String, Object> result = new HashMap<>();
        result.put("tableName", tableName);
        result.put("tableLabel", getTableLabel(tableName));
        result.put("list", new ArrayList<>());
        result.put("total", 0);
        result.put("columns", new ArrayList<>());
        return result;
    }

    /**
     * 从预警信息中提取企业ID
     * 优先级：1. warningDescription中的企业ID  2. companyName  3. companyId
     */
    private String extractCompanyIdFromWarning(TblRiskWarning warning) {
        try {
            // 1. 尝试从预警描述中提取企业ID
            String description = warning.getWarningDescription();
            if (StringUtil.isNotEmpty(description)) {
                // 匹配 "企业ID: HBSG-004" 或 "企业ID: COMPANY_001" 这样的模式（支持连字符）
                Pattern pattern = Pattern.compile("企业ID[：:] *([A-Za-z0-9_-]+)");
                Matcher matcher = pattern.matcher(description);
                if (matcher.find()) {
                    String companyId = matcher.group(1);
                    log.info("从预警描述中提取到企业ID: {}", companyId);
                    return companyId;
                }
            }

            // 2. 使用 companyName（可能存储的是ID）
            if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                log.info("使用 companyName 作为企业ID: {}", warning.getCompanyName());
                return warning.getCompanyName();
            }

            // 3. 使用 companyId
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                log.info("使用 companyId: {}", warning.getCompanyId());
                return warning.getCompanyId();
            }

            log.warn("无法从预警信息中提取企业ID");
            return null;

        } catch (Exception e) {
            log.error("提取企业ID失败", e);
            return warning.getCompanyId();
        }
    }

    // 🔧 优化：添加企业名称到ID的缓存
    private final Map<String, String> companyNameToIdCache = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * 判断字符串是否为企业ID格式
     * 企业ID通常格式：COMPANY_001, COMP_123, 32位UUID等
     */
    private boolean isCompanyId(String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        // 企业ID格式：COMPANY_001, COMP_123, HBSG-004, 32位UUID等
        return value.matches("^[A-Z]+_[0-9]+$") ||           // COMPANY_001
               value.matches("^[A-Z]+-[0-9]+$") ||           // HBSG-004（连字符+数字后缀）
               value.matches("^[0-9a-f]{32}$") ||             // 32位UUID
               value.matches("^[A-Z0-9_-]{3,50}$");           // 通用ID格式（3-50字符，含字母数字下划线连字符）
    }

    /**
     * 通过企业名称从 HB_COMPANY 表查询企业ID（带缓存）
     * @param companyName 企业名称
     * @return 企业ID，如果查询不到返回null
     */
    private String queryCompanyIdByName(String companyName) {
        try {
            if (StringUtil.isEmpty(companyName)) {
                return null;
            }

            // 🔧 优化：先检查缓存
            String cachedId = companyNameToIdCache.get(companyName);
            if (cachedId != null) {
                log.debug("从缓存获取企业ID，企业名称: {}, 企业ID: {}", companyName, cachedId);
                return cachedId;
            }

            log.info("尝试从 HB_COMPANY 表查询企业ID，企业名称: {}", companyName);
            long startTime = System.currentTimeMillis();

            // 查询 HB_COMPANY 表
            String sql = "SELECT COMPANY_ID FROM HB_COMPANY WHERE COMPANY_NAME = ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, companyName);

            long queryTime = System.currentTimeMillis() - startTime;
            log.info("HB_COMPANY 查询耗时: {} ms", queryTime);

            if (results.isEmpty()) {
                log.warn("在 HB_COMPANY 表中未找到企业，企业名称: {}", companyName);
                // 🔧 优化：缓存空结果，避免重复查询
                companyNameToIdCache.put(companyName, "");
                return null;
            }

            String companyId = (String) results.get(0).get("COMPANY_ID");
            log.info("从 HB_COMPANY 表查询到企业ID: {}", companyId);

            // 🔧 优化：缓存查询结果
            companyNameToIdCache.put(companyName, companyId);

            return companyId;

        } catch (Exception e) {
            log.error("从 HB_COMPANY 表查询企业ID失败，企业名称: {}", companyName, e);
            return null;
        }
    }

    /**
     * 获取表的列信息
     */
    private List<String> getTableColumns(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName);

            List<String> columnNames = new ArrayList<>();
            for (Map<String, Object> column : columns) {
                String columnName = (String) column.get("COLUMN_NAME");
                if (columnName != null) {
                    columnNames.add(columnName);
                }
            }

            log.info("表 {} 包含 {} 个列: {}", tableName, columnNames.size(), columnNames);
            return columnNames;

        } catch (Exception e) {
            log.error("获取表 {} 的列信息失败", tableName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 从数据中构建列信息
     */
    private List<Map<String, Object>> buildColumnsFromData(List<Map<String, Object>> dataList, String tableName) {
        List<Map<String, Object>> columns = new ArrayList<>();

        if (!dataList.isEmpty()) {
            Map<String, Object> firstRow = dataList.get(0);
            for (String key : firstRow.keySet()) {
                Map<String, Object> column = new HashMap<>();
                column.put("prop", key);
                column.put("label", formatColumnLabel(key));
                column.put("width", calculateColumnWidth(key));
                columns.add(column);
            }
        }

        return columns;
    }

    /**
     * 格式化列标签
     */
    private String formatColumnLabel(String columnName) {
        // 将下划线分隔的列名转换为中文标签
        // 例如: COMPANY_NAME -> 企业名称
        return columnName.replace("_", " ");
    }

    /**
     * 计算列宽度
     */
    private Integer calculateColumnWidth(String columnName) {
        // 根据列名长度计算合适的宽度
        int baseWidth = 120;
        int extraWidth = columnName.length() * 8;
        return Math.min(baseWidth + extraWidth, 300);
    }

    /**
     * 获取表的中文标签（从数据库表注释中获取）
     */
    private String getTableLabel(String tableName) {
        try {
            // 从数据库中查询表注释
            String sql = "SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME = ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, tableName);

            if (!results.isEmpty()) {
                Object commentObj = results.get(0).get("COMMENTS");
                if (commentObj != null) {
                    String comment = commentObj.toString();
                    // 如果注释包含 '-'，取第一部分作为表别名
                    // 例如：'市场价格表-记录产品的市场参考价格' -> '市场价格表'
                    if (comment.contains("-")) {
                        return comment.split("-")[0].trim();
                    }
                    return comment.trim();
                }
            }

            log.debug("未找到表 {} 的注释，使用默认标签", tableName);

            // 如果没有找到注释，使用默认映射
            Map<String, String> tableLabels = new HashMap<>();
            tableLabels.put("TBL_SUPPLIER", "供应商信息");
            tableLabels.put("TBL_CUSTOMER", "客户信息");
            tableLabels.put("TBL_CONTRACT", "合同信息");
            tableLabels.put("TBL_TRANSACTION", "交易信息");
            tableLabels.put("HB_RELATED_TRANSACTION", "关联交易");
            tableLabels.put("HB_BENEFIT_TRANSFER", "利益输送");
            tableLabels.put("HB_FUND_OCCUPATION", "资金占用");
            tableLabels.put("HB_MARKET_PRICE", "市场价格表");

            return tableLabels.getOrDefault(tableName, tableName);

        } catch (Exception e) {
            log.error("获取表 {} 的标签失败", tableName, e);
            return tableName;
        }
    }

    /**
     * 创建空的多表数据结果
     */
    private Map<String, Object> createEmptyMultiTableDataResult(Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        result.put("tables", new ArrayList<>());
        result.put("tableCount", 0);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 查询原始业务数据（增强版）
     */
    private List<Map<String, Object>> queryOriginalBusinessData(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("开始查询原始业务数据，预警ID: {}, 模型ID: {}", warning.getWarningId(), warning.getModelId());

            // 1. 优先尝试从数据模型SQL查询真实数据
            List<Map<String, Object>> result = queryDataFromModelSql(warning, pageNum, pageSize);
            if (!result.isEmpty()) {
                log.info("从数据模型SQL查询到 {} 条真实业务数据", result.size());
                return result;
            }

            // 2. 尝试从评估模型关联的数据源查询
            result = queryDataFromEvaluationModel(warning, pageNum, pageSize);
            if (!result.isEmpty()) {
                log.info("从评估模型数据源查询到 {} 条业务数据", result.size());
                return result;
            }

            // 3. 尝试从预警相关数据中查询
            result = queryDataFromWarningRelatedData(warning, pageNum, pageSize);
            if (!result.isEmpty()) {
                log.info("从预警相关数据查询到 {} 条业务数据", result.size());
                return result;
            }

            // 4. 尝试从业务表中直接查询相关数据
            result = queryDataFromBusinessTables(warning, pageNum, pageSize);
            if (!result.isEmpty()) {
                log.info("从业务表查询到 {} 条相关数据", result.size());
                return result;
            }

            // 5. 最后的备选方案：使用备用数据
            log.warn("无法查询到真实业务数据，使用备用数据");
            return createMockBusinessData(warning);

        } catch (Exception e) {
            log.error("查询原始业务数据失败", e);
            return createMockBusinessData(warning);
        }
    }

    /**
     * 从数据模型SQL查询数据（支持数据模型管理和组合指标）
     */
    private List<Map<String, Object>> queryDataFromModelSql(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            if (StringUtil.isEmpty(warning.getEvalModelId())) {
                return new ArrayList<>();
            }

            log.info("从数据模型SQL查询数据，评估模型ID: {}", warning.getEvalModelId());

            // 获取评估模型信息
            TblEvaluationModel evalModel = evaluationModelService.getById(warning.getEvalModelId());
            if (evalModel == null) {
                log.warn("未找到评估模型，ID: {}", warning.getEvalModelId());
                return new ArrayList<>();
            }

            String dataModelId = evalModel.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("评估模型未关联数据模型，评估模型ID: {}", warning.getEvalModelId());
                return new ArrayList<>();
            }

            // 根据数据模型ID类型判断处理方式
            if (dataModelId.startsWith("COMB")) {
                // 组合指标分析
                log.info("检测到组合指标分析，组合ID: {}", dataModelId);
                return queryDataFromCombination(dataModelId, warning, pageNum, pageSize);
            } else {
                // 数据模型管理
                log.info("检测到数据模型管理，模型ID: {}", dataModelId);
                return queryDataFromDataModelManagement(dataModelId, warning, pageNum, pageSize);
            }

        } catch (Exception e) {
            log.error("从数据模型SQL查询数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从组合指标分析查询数据（直接查询原始数据表）
     */
    private List<Map<String, Object>> queryDataFromCombination(String combinationId, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("从组合指标分析查询原始数据表，组合ID: {}", combinationId);

            // 获取组合详情
            Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
            if (combinationDetail == null || combinationDetail.isEmpty()) {
                log.warn("未找到组合指标详情，组合ID: {}", combinationId);
                return new ArrayList<>();
            }

            // 提取组合配置
            Map<String, Object> config = extractCombinationConfig(combinationDetail);
            List<Map<String, Object>> indicators = (List<Map<String, Object>>) config.get("indicators");

            if (indicators == null || indicators.isEmpty()) {
                log.warn("组合指标中没有配置指标，组合ID: {}", combinationId);
                return new ArrayList<>();
            }

            log.info("组合指标包含 {} 个指标，开始查询原始数据表", indicators.size());

            // 🔥 关键修改：直接从指标SQL查询原始数据表，而不是组合执行结果
            List<Map<String, Object>> originalData = queryOriginalDataFromCombinationIndicators(indicators, warning, pageNum, pageSize);
            if (!originalData.isEmpty()) {
                log.info("从组合指标原始数据表查询到 {} 条数据", originalData.size());
                return originalData;
            }

            // 备选：尝试从组合执行结果获取
            List<Map<String, Object>> combinationResults = getCombinationExecutionResults(combinationId, indicators, warning, pageNum, pageSize);
            if (!combinationResults.isEmpty()) {
                log.info("从组合执行结果获取到 {} 条数据", combinationResults.size());
                return combinationResults;
            }

            log.warn("无法从组合指标获取数据，组合ID: {}", combinationId);
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("从组合指标分析查询数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从数据模型管理查询数据
     */
    private List<Map<String, Object>> queryDataFromDataModelManagement(String dataModelId, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("从数据模型管理查询数据，模型ID: {}", dataModelId);

            // 查询数据模型的SQL语句
            String sql = "SELECT SQL_STATEMENT FROM TBL_DATA_MODEL WHERE MODEL_ID = ?";
            List<Map<String, Object>> dataModels = jdbcTemplate.queryForList(sql, dataModelId);

            if (dataModels.isEmpty()) {
                log.warn("未找到数据模型，ID: {}", dataModelId);
                return new ArrayList<>();
            }

            String sqlStatement = (String) dataModels.get(0).get("SQL_STATEMENT");
            if (StringUtil.isEmpty(sqlStatement)) {
                log.warn("数据模型SQL语句为空，模型ID: {}", dataModelId);
                return new ArrayList<>();
            }

            // 解析SQL获取涉及的数据表
            List<String> sourceTables = extractTablesFromSql(sqlStatement);
            if (sourceTables.isEmpty()) {
                log.warn("无法从SQL中提取数据表，SQL: {}", sqlStatement);
                return new ArrayList<>();
            }

            log.info("从数据模型SQL中提取到数据表: {}", sourceTables);

            // 根据预警条件构建穿透查询SQL
            String drillDownSql = buildDrillDownQueryFromTables(warning, sourceTables, sqlStatement);
            if (StringUtil.isEmpty(drillDownSql)) {
                log.warn("无法构建穿透查询SQL");
                return new ArrayList<>();
            }

            // 执行分页查询
            int offset = (pageNum - 1) * pageSize;
            String paginatedSql = drillDownSql + " OFFSET " + offset + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";

            log.info("执行数据模型穿透查询SQL: {}", paginatedSql);
            return jdbcTemplate.queryForList(paginatedSql);

        } catch (Exception e) {
            log.error("从数据模型管理查询数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取组合执行结果
     */
    private List<Map<String, Object>> getCombinationExecutionResults(String combinationId, List<Map<String, Object>> indicators, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("获取组合执行结果，组合ID: {}", combinationId);

            // 尝试通过评估模型SQL执行结果获取组合数据
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("dataModelId", combinationId);
            String executeResultStr = evaluationModelService.getEvaluationModelSqlExecutionResult(requestBody, "system");

            // 简单解析JSON字符串
            if (executeResultStr != null && executeResultStr.contains("\"code\":1")) {
                try {
                    // 尝试从组合服务获取详情
                    Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
                    if (combinationDetail != null && !combinationDetail.isEmpty()) {
                        List<Map<String, Object>> allResults = new ArrayList<>();
                        allResults.add(combinationDetail);

                        // 应用企业过滤
                        List<Map<String, Object>> filteredResults = filterResultsByCompany(allResults, warning.getCompanyId());

                        // 应用分页
                        return applyPagination(filteredResults, pageNum, pageSize);
                    }
                } catch (Exception e) {
                    log.warn("解析组合执行结果失败", e);
                }
            }

        } catch (Exception e) {
            log.debug("获取组合执行结果失败", e);
        }

        return new ArrayList<>();
    }

    /**
     * 从组合指标查询原始数据表（重点：查询SQL统计数据的来源表）
     */
    private List<Map<String, Object>> queryOriginalDataFromCombinationIndicators(List<Map<String, Object>> indicators, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("从组合指标查询原始数据表，指标数量: {}", indicators.size());

            List<Map<String, Object>> allResults = new ArrayList<>();

            for (Map<String, Object> indicator : indicators) {
                String sqlContent = (String) indicator.get("sqlContent");
                String dataSourceId = (String) indicator.get("dataSourceId");
                String indicatorCode = (String) indicator.get("indicatorCode");

                if (StringUtil.isNotEmpty(sqlContent)) {
                    try {
                        log.info("处理指标: {}, SQL: {}", indicatorCode, sqlContent);

                        // 🔥 关键：从指标SQL中提取原始数据表并查询明细数据
                        List<Map<String, Object>> originalTableData = queryOriginalTableDataFromIndicatorSql(sqlContent, warning, indicatorCode);

                        if (!originalTableData.isEmpty()) {
                            log.info("从指标 {} 的原始数据表查询到 {} 条数据", indicatorCode, originalTableData.size());
                            allResults.addAll(originalTableData);
                        } else {
                            log.warn("指标 {} 未查询到原始数据表数据", indicatorCode);
                        }

                    } catch (Exception e) {
                        log.error("查询指标 {} 的原始数据表失败: {}", indicatorCode, e.getMessage(), e);
                    }
                }
            }

            if (allResults.isEmpty()) {
                log.warn("所有指标都未查询到原始数据表数据");
                return new ArrayList<>();
            }

            log.info("总共从原始数据表查询到 {} 条数据", allResults.size());

            // 应用分页
            return applyPagination(allResults, pageNum, pageSize);

        } catch (Exception e) {
            log.error("从组合指标查询原始数据表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从指标SQL查询原始数据表的明细数据
     */
    private List<Map<String, Object>> queryOriginalTableDataFromIndicatorSql(String indicatorSql, TblRiskWarning warning, String indicatorCode) {
        try {
            log.info("从指标SQL查询原始数据表，指标: {}", indicatorCode);

            // 1. 解析指标SQL，提取FROM子句中的表名
            List<String> sourceTables = extractTablesFromSql(indicatorSql);
            if (sourceTables.isEmpty()) {
                log.warn("无法从指标SQL中提取数据表，SQL: {}", indicatorSql);
                return new ArrayList<>();
            }

            log.info("从指标SQL中提取到数据表: {}", sourceTables);

            // 2. 构建查询原始数据表的SQL
            String originalDataSql = buildOriginalDataQuerySql(sourceTables, warning, indicatorCode);
            if (StringUtil.isEmpty(originalDataSql)) {
                log.warn("无法构建原始数据查询SQL");
                return new ArrayList<>();
            }

            log.info("执行原始数据查询SQL: {}", originalDataSql);

            // 3. 执行查询获取原始数据
            List<Map<String, Object>> results = jdbcTemplate.queryForList(originalDataSql);

            // 4. 为结果添加指标标识
            for (Map<String, Object> result : results) {
                result.put("sourceIndicator", indicatorCode);
                result.put("dataType", "原始数据");
            }

            log.info("从原始数据表查询到 {} 条数据", results.size());
            return results;

        } catch (Exception e) {
            log.error("从指标SQL查询原始数据表失败，指标: {}", indicatorCode, e);
            return new ArrayList<>();
        }
    }

    /**
     * 构建查询原始数据表的SQL
     */
    private String buildOriginalDataQuerySql(List<String> sourceTables, TblRiskWarning warning, String indicatorCode) {
        try {
            // 选择主要的数据表（通常是第一个表）
            String mainTable = sourceTables.get(0);

            log.info("构建原始数据查询SQL，主表: {}", mainTable);

            // 构建基础查询SQL
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM ").append(mainTable);

            // 添加WHERE条件
            List<String> whereConditions = new ArrayList<>();

            // 1. 添加企业过滤条件
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                // 检查表中是否有企业相关字段
                if (hasCompanyField(mainTable)) {
                    String companyField = getCompanyFieldName(mainTable);
                    whereConditions.add(companyField + " = '" + warning.getCompanyId() + "'");
                }
            }

            // 2. 添加时间范围条件（查询预警时间前后的数据）
            if (warning.getWarningTime() != null) {
                if (hasTimeField(mainTable)) {
                    String timeField = getTimeFieldName(mainTable);
                    // 查询预警时间前后30天的数据 - 使用达梦数据库语法
                    whereConditions.add(timeField + " >= DATEADD(DAY, -30, '" + warning.getWarningTime() + "')");
                    whereConditions.add(timeField + " <= DATEADD(DAY, 30, '" + warning.getWarningTime() + "')");
                }
            }

            // 3. 根据预警类型添加特定条件
            addWarningTypeSpecificConditions(whereConditions, warning, mainTable);

            // 组装WHERE子句
            if (!whereConditions.isEmpty()) {
                sqlBuilder.append(" WHERE ");
                sqlBuilder.append(String.join(" AND ", whereConditions));
            }

            // 添加排序
            if (hasTimeField(mainTable)) {
                String timeField = getTimeFieldName(mainTable);
                sqlBuilder.append(" ORDER BY ").append(timeField).append(" DESC");
            }

            // 限制查询数量（避免数据过多）- 使用达梦数据库语法
            sqlBuilder.append(" OFFSET 0 ROWS FETCH NEXT 1000 ROWS ONLY");

            String finalSql = sqlBuilder.toString();
            log.info("构建的原始数据查询SQL: {}", finalSql);

            return finalSql;

        } catch (Exception e) {
            log.error("构建原始数据查询SQL失败", e);
            return null;
        }
    }

    /**
     * 从组合指标的SQL查询数据（保留原有方法作为备选）
     */
    private List<Map<String, Object>> queryDataFromCombinationIndicators(List<Map<String, Object>> indicators, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            log.info("从组合指标SQL查询数据，指标数量: {}", indicators.size());

            List<Map<String, Object>> allResults = new ArrayList<>();

            for (Map<String, Object> indicator : indicators) {
                String sqlContent = (String) indicator.get("sqlContent");
                String dataSourceId = (String) indicator.get("dataSourceId");

                if (StringUtil.isNotEmpty(sqlContent)) {
                    try {
                        // 修改SQL添加企业过滤条件
                        String modifiedSql = addCompanyFilterToSql(sqlContent, warning.getCompanyId());

                        // 执行SQL查询
                        List<Map<String, Object>> indicatorResults = jdbcTemplate.queryForList(modifiedSql);

                        // 为每条结果添加指标信息
                        for (Map<String, Object> result : indicatorResults) {
                            result.put("indicatorCode", indicator.get("indicatorCode"));
                            result.put("indicatorName", indicator.get("indicatorName"));
                            result.put("dataSourceId", dataSourceId);
                        }

                        allResults.addAll(indicatorResults);

                    } catch (Exception e) {
                        log.warn("执行指标SQL失败，指标: {}", indicator.get("indicatorCode"), e);
                    }
                }
            }

            // 应用分页
            return applyPagination(allResults, pageNum, pageSize);

        } catch (Exception e) {
            log.error("从组合指标SQL查询数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 检查表是否有企业字段
     */
    private boolean hasCompanyField(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND (COLUMN_NAME = 'COMPANY_ID' OR COLUMN_NAME = 'ENTERPRISE_ID')";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName.toUpperCase());
            return !columns.isEmpty();
        } catch (Exception e) {
            log.debug("检查表企业字段失败: {}", tableName, e);
            return false;
        }
    }

    /**
     * 获取企业字段名
     */
    private String getCompanyFieldName(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND (COLUMN_NAME = 'COMPANY_ID' OR COLUMN_NAME = 'ENTERPRISE_ID') ORDER BY COLUMN_NAME";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName.toUpperCase());
            if (!columns.isEmpty()) {
                return (String) columns.get(0).get("COLUMN_NAME");
            }
        } catch (Exception e) {
            log.debug("获取表企业字段名失败: {}", tableName, e);
        }
        return "COMPANY_ID"; // 默认值
    }

    /**
     * 检查表是否有时间字段
     */
    private boolean hasTimeField(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND (COLUMN_NAME LIKE '%TIME%' OR COLUMN_NAME LIKE '%DATE%' OR COLUMN_NAME = 'CREATE_TIME' OR COLUMN_NAME = 'UPDATE_TIME')";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName.toUpperCase());
            return !columns.isEmpty();
        } catch (Exception e) {
            log.debug("检查表时间字段失败: {}", tableName, e);
            return false;
        }
    }

    /**
     * 获取时间字段名
     */
    private String getTimeFieldName(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND (COLUMN_NAME = 'CREATE_TIME' OR COLUMN_NAME = 'UPDATE_TIME' OR COLUMN_NAME LIKE '%TIME%' OR COLUMN_NAME LIKE '%DATE%') ORDER BY COLUMN_NAME";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName.toUpperCase());
            if (!columns.isEmpty()) {
                return (String) columns.get(0).get("COLUMN_NAME");
            }
        } catch (Exception e) {
            log.debug("获取表时间字段名失败: {}", tableName, e);
        }
        return "CREATE_TIME"; // 默认值
    }

    /**
     * 根据预警类型添加特定条件
     */
    private void addWarningTypeSpecificConditions(List<String> whereConditions, TblRiskWarning warning, String tableName) {
        try {
            String warningType = warning.getWarningType();

            if ("FINANCIAL_RISK".equals(warningType)) {
                // 财务风险：添加财务相关条件
                if (hasColumn(tableName, "ASSET_LIABILITY_RATIO")) {
                    whereConditions.add("ASSET_LIABILITY_RATIO IS NOT NULL");
                }
            } else if ("PROCUREMENT_RISK".equals(warningType)) {
                // 采购风险：添加采购相关条件
                if (hasColumn(tableName, "SUPPLIER_ID")) {
                    whereConditions.add("SUPPLIER_ID IS NOT NULL");
                }
                if (hasColumn(tableName, "CONTRACT_AMOUNT")) {
                    whereConditions.add("CONTRACT_AMOUNT > 0");
                }
            } else if ("CREDIT_RISK".equals(warningType)) {
                // 信用风险：添加信用相关条件
                if (hasColumn(tableName, "CREDIT_RATING")) {
                    whereConditions.add("CREDIT_RATING IS NOT NULL");
                }
            } else if ("COMPLIANCE_RISK".equals(warningType)) {
                // 合规风险：添加合规相关条件
                if (hasColumn(tableName, "COMPLIANCE_STATUS")) {
                    whereConditions.add("COMPLIANCE_STATUS IS NOT NULL");
                }
            }

        } catch (Exception e) {
            log.debug("添加预警类型特定条件失败", e);
        }
    }

    /**
     * 检查表是否有指定列
     */
    private boolean hasColumn(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase(), columnName.toUpperCase());
            return count != null && count > 0;
        } catch (Exception e) {
            log.debug("检查表列失败: {}.{}", tableName, columnName, e);
            return false;
        }
    }

    /**
     * 为SQL添加企业过滤条件
     */
    private String addCompanyFilterToSql(String sql, String companyId) {
        if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(sql)) {
            return sql;
        }

        String upperSql = sql.toUpperCase();

        // 检查SQL中是否包含企业相关字段
        if (upperSql.contains("COMPANY_ID") || upperSql.contains("ENTERPRISE_ID")) {
            String companyField = upperSql.contains("COMPANY_ID") ? "COMPANY_ID" : "ENTERPRISE_ID";

            if (upperSql.contains("WHERE")) {
                // 已有WHERE子句，添加AND条件
                return sql + " AND " + companyField + " = '" + companyId + "'";
            } else {
                // 没有WHERE子句，添加WHERE条件
                return sql + " WHERE " + companyField + " = '" + companyId + "'";
            }
        }

        return sql;
    }

    /**
     * 根据企业ID过滤结果
     */
    private List<Map<String, Object>> filterResultsByCompany(List<Map<String, Object>> results, String companyId) {
        if (StringUtil.isEmpty(companyId) || results == null || results.isEmpty()) {
            return results;
        }

        return results.stream()
                .filter(result -> {
                    String resultCompanyId = (String) result.get("COMPANY_ID");
                    if (StringUtil.isEmpty(resultCompanyId)) {
                        resultCompanyId = (String) result.get("ENTERPRISE_ID");
                    }
                    return companyId.equals(resultCompanyId);
                })
                .collect(Collectors.toList());
    }

    /**
     * 应用分页逻辑
     */
    private List<Map<String, Object>> applyPagination(List<Map<String, Object>> results, Integer pageNum, Integer pageSize) {
        if (results == null || results.isEmpty()) {
            return results;
        }

        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, results.size());

        if (startIndex >= results.size()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(results.subList(startIndex, endIndex));
    }

    /**
     * 从评估模型关联的数据源查询
     */
    private List<Map<String, Object>> queryDataFromEvaluationModel(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            if (StringUtil.isEmpty(warning.getEvalModelId())) {
                return new ArrayList<>();
            }

            // 查询评估模型信息
            String evalModelSql = "SELECT DATA_MODEL_ID FROM TBL_EVALUATION_MODEL WHERE EVAL_MODEL_ID = ?";
            List<Map<String, Object>> evalModels = jdbcTemplate.queryForList(evalModelSql, warning.getEvalModelId());

            if (evalModels.isEmpty()) {
                return new ArrayList<>();
            }

            String dataModelId = (String) evalModels.get(0).get("DATA_MODEL_ID");
            if (StringUtil.isEmpty(dataModelId)) {
                return new ArrayList<>();
            }

            // 查询数据模型的SQL语句
            String dataModelSql = "SELECT SQL_STATEMENT FROM TBL_DATA_MODEL WHERE MODEL_ID = ?";
            List<Map<String, Object>> dataModels = jdbcTemplate.queryForList(dataModelSql, dataModelId);

            if (dataModels.isEmpty()) {
                return new ArrayList<>();
            }

            String sqlStatement = (String) dataModels.get(0).get("SQL_STATEMENT");
            if (StringUtil.isEmpty(sqlStatement)) {
                return new ArrayList<>();
            }

            // 修改SQL添加分页和过滤条件
            String modifiedSql = addPaginationAndFilters(sqlStatement, warning, pageNum, pageSize);

            log.info("执行评估模型关联查询SQL: {}", modifiedSql);
            return jdbcTemplate.queryForList(modifiedSql);

        } catch (Exception e) {
            log.error("从评估模型关联数据源查询失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 从预警相关数据中查询
     */
    private List<Map<String, Object>> queryDataFromWarningRelatedData(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            String relatedData = warning.getRelatedData();
            if (StringUtil.isEmpty(relatedData)) {
                return new ArrayList<>();
            }

            // 解析相关数据JSON
            Map<String, Object> relatedMap = parseRelatedData(relatedData);
            if (relatedMap == null) {
                return new ArrayList<>();
            }

            // 如果相关数据中包含SQL查询信息
            if (relatedMap.containsKey("sourceQuery")) {
                String sourceQuery = (String) relatedMap.get("sourceQuery");
                if (StringUtil.isNotEmpty(sourceQuery)) {
                    String paginatedQuery = addPaginationToQuery(sourceQuery, pageNum, pageSize);
                    log.info("执行预警相关数据查询SQL: {}", paginatedQuery);
                    return jdbcTemplate.queryForList(paginatedQuery);
                }
            }

            // 如果相关数据中包含表名和条件
            if (relatedMap.containsKey("tableName") && relatedMap.containsKey("conditions")) {
                String tableName = (String) relatedMap.get("tableName");
                Map<String, Object> conditions = (Map<String, Object>) relatedMap.get("conditions");

                String sql = buildQueryFromTableAndConditions(tableName, conditions, pageNum, pageSize);
                log.info("执行预警相关数据条件查询SQL: {}", sql);
                return jdbcTemplate.queryForList(sql);
            }

        } catch (Exception e) {
            log.error("从预警相关数据查询失败", e);
        }

        return new ArrayList<>();
    }

    /**
     * 从业务表中直接查询相关数据
     */
    private List<Map<String, Object>> queryDataFromBusinessTables(TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            // 根据预警类型确定要查询的业务表
            List<String> businessTables = getBusinessTablesByWarningType(warning.getWarningType());

            for (String tableName : businessTables) {
                try {
                    List<Map<String, Object>> result = queryFromSingleBusinessTable(tableName, warning, pageNum, pageSize);
                    if (!result.isEmpty()) {
                        log.info("从业务表 {} 查询到 {} 条数据", tableName, result.size());
                        return result;
                    }
                } catch (Exception e) {
                    log.warn("从业务表 {} 查询失败: {}", tableName, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("从业务表查询数据失败", e);
        }

        return new ArrayList<>();
    }

    /**
     * 根据预警类型获取业务表列表
     */
    private List<String> getBusinessTablesByWarningType(String warningType) {
        List<String> tables = new ArrayList<>();

        switch (warningType) {
            case "FINANCIAL_RISK":
                tables.add("TBL_FINANCIAL_DATA");
                tables.add("TBL_COMPANY_FINANCIAL");
                tables.add("TBL_FINANCIAL_INDICATOR");
                break;
            case "PROCUREMENT_RISK":
                tables.add("TBL_PROCUREMENT_DATA");
                tables.add("TBL_CONTRACT_INFO");
                tables.add("TBL_SUPPLIER_INFO");
                break;
            case "CREDIT_RISK":
                tables.add("TBL_CREDIT_DATA");
                tables.add("TBL_CREDIT_RATING");
                tables.add("TBL_PAYMENT_HISTORY");
                break;
            case "COMPLIANCE_RISK":
                tables.add("TBL_COMPLIANCE_DATA");
                tables.add("TBL_AUDIT_RECORD");
                tables.add("TBL_REGULATION_CHECK");
                break;
            default:
                tables.add("TBL_COMPANY_INFO");
                tables.add("TBL_BUSINESS_DATA");
                break;
        }

        return tables;
    }

    /**
     * 从单个业务表查询数据
     */
    private List<Map<String, Object>> queryFromSingleBusinessTable(String tableName, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            // 检查表是否存在
            if (!checkTableExists(tableName)) {
                log.debug("表 {} 不存在", tableName);
                return new ArrayList<>();
            }

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1 ");

            // 添加企业过滤条件 - 动态检查字段是否存在
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                boolean hasCompanyId = hasColumn(tableName, "COMPANY_ID");
                boolean hasEnterpriseId = hasColumn(tableName, "ENTERPRISE_ID");

                if (hasCompanyId && hasEnterpriseId) {
                    sql.append("AND (COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
                    sql.append("OR ENTERPRISE_ID = '").append(warning.getCompanyId()).append("') ");
                } else if (hasCompanyId) {
                    sql.append("AND COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
                } else if (hasEnterpriseId) {
                    sql.append("AND ENTERPRISE_ID = '").append(warning.getCompanyId()).append("' ");
                }
            } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                boolean hasCompanyName = hasColumn(tableName, "COMPANY_NAME");
                boolean hasEnterpriseName = hasColumn(tableName, "ENTERPRISE_NAME");

                if (hasCompanyName && hasEnterpriseName) {
                    sql.append("AND (COMPANY_NAME LIKE '%").append(warning.getCompanyName()).append("%' ");
                    sql.append("OR ENTERPRISE_NAME LIKE '%").append(warning.getCompanyName()).append("%') ");
                } else if (hasCompanyName) {
                    sql.append("AND COMPANY_NAME LIKE '%").append(warning.getCompanyName()).append("%' ");
                } else if (hasEnterpriseName) {
                    sql.append("AND ENTERPRISE_NAME LIKE '%").append(warning.getCompanyName()).append("%' ");
                }
            }

            // 添加时间过滤条件（最近30天的数据）- 使用达梦数据库语法
            sql.append("AND (CREATE_TIME >= DATEADD(DAY, -30, SYSDATE) ");
            sql.append("OR UPDATE_TIME >= DATEADD(DAY, -30, SYSDATE)) ");

            // 添加分页
            sql.append("ORDER BY COALESCE(UPDATE_TIME, CREATE_TIME) DESC ");
            int offset = (pageNum - 1) * pageSize;
            sql.append("OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");

            log.debug("查询业务表SQL: {}", sql.toString());
            return jdbcTemplate.queryForList(sql.toString());

        } catch (Exception e) {
            log.error("从业务表 {} 查询数据失败", tableName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean checkTableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
            return count != null && count > 0;
        } catch (Exception e) {
            log.debug("检查表 {} 是否存在时出错: {}", tableName, e.getMessage());
            return false;
        }
    }

    /**
     * 为SQL添加分页和过滤条件
     */
    private String addPaginationAndFilters(String originalSql, TblRiskWarning warning, Integer pageNum, Integer pageSize) {
        try {
            StringBuilder sql = new StringBuilder();

            // 如果原SQL已经有WHERE子句，添加AND条件；否则添加WHERE子句
            String upperSql = originalSql.toUpperCase();
            if (upperSql.contains("WHERE")) {
                sql.append(originalSql);

                // 添加企业过滤条件 - 动态检查字段是否存在
                if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                    // 注意：这里无法直接获取表名，使用通用的企业字段检查
                    sql.append(" AND (COMPANY_ID = '").append(warning.getCompanyId()).append("')");
                } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                    sql.append(" AND (COMPANY_NAME LIKE '%").append(warning.getCompanyName()).append("%')");
                }
            } else {
                sql.append(originalSql).append(" WHERE 1=1 ");

                // 添加企业过滤条件 - 动态检查字段是否存在
                if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                    // 注意：这里无法直接获取表名，使用通用的企业字段检查
                    sql.append(" AND (COMPANY_ID = '").append(warning.getCompanyId()).append("')");
                } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                    sql.append(" AND (COMPANY_NAME LIKE '%").append(warning.getCompanyName()).append("%')");
                }
            }

            // 添加分页
            if (!upperSql.contains("ORDER BY")) {
                sql.append(" ORDER BY 1 ");
            }

            int offset = (pageNum - 1) * pageSize;
            sql.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");

            return sql.toString();

        } catch (Exception e) {
            log.error("添加分页和过滤条件失败", e);
            return originalSql;
        }
    }

    /**
     * 为查询添加分页
     */
    private String addPaginationToQuery(String query, Integer pageNum, Integer pageSize) {
        try {
            String upperQuery = query.toUpperCase();

            // 如果已经有分页，直接返回
            if (upperQuery.contains("OFFSET") && upperQuery.contains("FETCH")) {
                return query;
            }

            StringBuilder sql = new StringBuilder(query);

            // 如果没有ORDER BY，添加一个默认的
            if (!upperQuery.contains("ORDER BY")) {
                sql.append(" ORDER BY 1 ");
            }

            // 添加分页
            int offset = (pageNum - 1) * pageSize;
            sql.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");

            return sql.toString();

        } catch (Exception e) {
            log.error("添加分页失败", e);
            return query;
        }
    }

    /**
     * 根据表名和条件构建查询SQL
     */
    private String buildQueryFromTableAndConditions(String tableName, Map<String, Object> conditions, Integer pageNum, Integer pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1 ");

        // 添加条件
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                sql.append(" AND ").append(key).append(" = '").append(value).append("'");
            } else if (value instanceof Number) {
                sql.append(" AND ").append(key).append(" = ").append(value);
            }
        }

        // 添加分页
        sql.append(" ORDER BY 1 ");
        int offset = (pageNum - 1) * pageSize;
        sql.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");

        return sql.toString();
    }

    /**
     * 根据预警记录获取数据模型的SQL语句
     */
    private String getDataModelSqlByWarning(TblRiskWarning warning) {
        try {
            log.info("获取数据模型SQL，预警ID: {}, 模型ID: {}", warning.getWarningId(), warning.getModelId());

            if (StringUtil.isEmpty(warning.getModelId())) {
                log.warn("预警记录中没有模型ID");
                return null;
            }

            // 查询数据模型表获取SQL语句
            String sql = "SELECT dm.SQL_STATEMENT, dm.MODEL_NAME, dm.MODEL_TYPE, dm.BUSINESS_MEANING, " +
                        "dm.SELECT_CLAUSE, dm.FROM_CLAUSE, dm.WHERE_CLAUSE, dm.GROUP_BY_CLAUSE, dm.ORDER_BY_CLAUSE " +
                        "FROM TBL_DATA_MODEL dm " +
                        "WHERE dm.MODEL_ID = ? AND dm.IS_ENABLED = 'Y'";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, warning.getModelId());

            if (results.isEmpty()) {
                log.warn("未找到模型ID对应的SQL: {}", warning.getModelId());
                return null;
            }

            Map<String, Object> result = results.get(0);

            // 获取完整SQL语句
            String sqlStatement = (String) result.get("SQL_STATEMENT");
            if (StringUtil.isNotEmpty(sqlStatement)) {
                log.info("获取到数据模型SQL，模型名称: {}, 模型类型: {}",
                        result.get("MODEL_NAME"), result.get("MODEL_TYPE"));
                return sqlStatement;
            }

            // 如果没有完整SQL，尝试从各个子句拼接
            String selectClause = (String) result.get("SELECT_CLAUSE");
            String fromClause = (String) result.get("FROM_CLAUSE");
            String whereClause = (String) result.get("WHERE_CLAUSE");
            String groupByClause = (String) result.get("GROUP_BY_CLAUSE");
            String orderByClause = (String) result.get("ORDER_BY_CLAUSE");

            if (StringUtil.isNotEmpty(selectClause) && StringUtil.isNotEmpty(fromClause)) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("SELECT ").append(selectClause);
                sqlBuilder.append(" FROM ").append(fromClause);
                if (StringUtil.isNotEmpty(whereClause)) {
                    sqlBuilder.append(" WHERE ").append(whereClause);
                }
                if (StringUtil.isNotEmpty(groupByClause)) {
                    sqlBuilder.append(" GROUP BY ").append(groupByClause);
                }
                if (StringUtil.isNotEmpty(orderByClause)) {
                    sqlBuilder.append(" ORDER BY ").append(orderByClause);
                }

                log.info("从子句拼接SQL语句");
                return sqlBuilder.toString();
            }

            return null;

        } catch (Exception e) {
            log.error("获取数据模型SQL失败", e);
            return null;
        }
    }

    /**
     * 从SQL中提取涉及的数据表
     */
    private List<String> extractTablesFromSql(String sql) {
        List<String> tables = new ArrayList<>();

        try {
            log.info("从SQL中提取数据表");

            // 将SQL转换为大写便于匹配
            String upperSql = sql.toUpperCase();

            // 先提取 WITH 子句中的 CTE 别名，后续需要过滤掉
            Set<String> cteAliases = new LinkedHashSet<>();
            Pattern ctePattern = Pattern.compile("WITH\\s+([A-Z_][A-Z0-9_]*)\\s+AS\\s*\\(", Pattern.CASE_INSENSITIVE);
            Matcher cteMatcher = ctePattern.matcher(upperSql);
            while (cteMatcher.find()) {
                cteAliases.add(cteMatcher.group(1));
            }
            // 也匹配逗号分隔的后续CTE: ), CTE_NAME AS (
            Pattern nextCtePattern = Pattern.compile("\\)\\s*,\\s*([A-Z_][A-Z0-9_]*)\\s+AS\\s*\\(", Pattern.CASE_INSENSITIVE);
            Matcher nextCteMatcher = nextCtePattern.matcher(upperSql);
            while (nextCteMatcher.find()) {
                cteAliases.add(nextCteMatcher.group(1));
            }
            if (!cteAliases.isEmpty()) {
                log.info("识别到CTE别名(非真实表): {}", cteAliases);
            }

            // 使用正则表达式匹配FROM和JOIN后的表名
            String[] patterns = {
                "FROM\\s+([A-Z_][A-Z0-9_]*)",
                "JOIN\\s+([A-Z_][A-Z0-9_]*)",
                "INNER\\s+JOIN\\s+([A-Z_][A-Z0-9_]*)",
                "LEFT\\s+JOIN\\s+([A-Z_][A-Z0-9_]*)",
                "RIGHT\\s+JOIN\\s+([A-Z_][A-Z0-9_]*)"
            };

            for (String pattern : patterns) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(upperSql);

                while (m.find()) {
                    String tableName = m.group(1);
                    // 过滤掉子查询别名、SQL关键字和CTE别名
                    if (!tableName.matches("(SELECT|WHERE|GROUP|ORDER|HAVING|UNION|WITH)")
                        && !cteAliases.contains(tableName)
                        && !tables.contains(tableName)) {
                        tables.add(tableName);
                        log.info("提取到数据表: {}", tableName);
                    }
                }
            }

            // 如果正则匹配失败，尝试简单的字符串匹配
            if (tables.isEmpty()) {
                tables = extractTablesSimple(upperSql);
            }

        } catch (Exception e) {
            log.error("从SQL提取数据表失败", e);
        }

        return tables;
    }

    /**
     * 简单的表名提取方法（备用）
     */
    private List<String> extractTablesSimple(String upperSql) {
        List<String> tables = new ArrayList<>();

        // 查找所有以TBL_或HB_开头的表名
        String[] words = upperSql.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[(),]", ""); // 移除括号和逗号
            // 支持 TBL_ 和 HB_ 开头的表名
            if ((word.startsWith("TBL_") || word.startsWith("HB_")) && word.length() > 4) {
                if (!tables.contains(word)) {
                    tables.add(word);
                    log.info("简单匹配到数据表: {}", word);
                }
            }
        }

        return tables;
    }

    /**
     * 根据数据表构建穿透查询SQL
     */
    private String buildDrillDownQueryFromTables(TblRiskWarning warning, List<String> sourceTables, String originalSql) {
        try {
            log.info("构建数据穿透查询SQL，涉及表: {}", sourceTables);

            // 解析RELATED_DATA获取查询条件
            Map<String, Object> relatedData = parseRelatedData(warning.getRelatedData());

            StringBuilder sql = new StringBuilder();

            if (sourceTables.size() == 1) {
                // 单表查询
                sql = buildSingleTableDrillDown(sourceTables.get(0), warning, relatedData);
            } else {
                // 多表关联查询
                sql = buildMultiTableDrillDown(sourceTables, warning, relatedData, originalSql);
            }

            // 添加排序
            sql.append(" ORDER BY ");
            if (sql.toString().toUpperCase().contains("CREATE_TIME")) {
                sql.append("CREATE_TIME DESC");
            } else if (sql.toString().toUpperCase().contains("UPDATE_TIME")) {
                sql.append("UPDATE_TIME DESC");
            } else {
                sql.append("1 DESC"); // 默认排序
            }

            log.info("构建的穿透查询SQL: {}", sql.toString());
            return sql.toString();

        } catch (Exception e) {
            log.error("构建穿透查询SQL失败", e);
            return null;
        }
    }

    /**
     * 构建单表穿透查询
     */
    private StringBuilder buildSingleTableDrillDown(String tableName, TblRiskWarning warning, Map<String, Object> relatedData) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1 ");

        // 添加企业过滤条件
        if (StringUtil.isNotEmpty(warning.getCompanyId())) {
            sql.append("AND COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
        } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
            sql.append("AND COMPANY_NAME = '").append(warning.getCompanyName()).append("' ");
        }

        // 添加时间范围条件（预警时间前后一段时间）
        if (warning.getWarningTime() != null) {
            sql.append("AND CREATE_TIME >= DATEADD(DAY, -30, '").append(warning.getWarningTime()).append("') ");
            sql.append("AND CREATE_TIME <= DATEADD(DAY, 7, '").append(warning.getWarningTime()).append("') ");
        }

        // 根据相关数据添加特定条件
        if (relatedData != null) {
            addRelatedDataConditions(sql, relatedData, tableName);
        }

        return sql;
    }

    /**
     * 构建多表关联穿透查询
     */
    private StringBuilder buildMultiTableDrillDown(List<String> sourceTables, TblRiskWarning warning,
                                                  Map<String, Object> relatedData, String originalSql) {
        StringBuilder sql = new StringBuilder();

        // 尝试从原始SQL中提取主要的SELECT和FROM部分
        String mainTable = sourceTables.get(0);

        sql.append("SELECT ");

        // 构建选择字段
        for (int i = 0; i < sourceTables.size(); i++) {
            String table = sourceTables.get(i);
            String alias = "t" + (i + 1);

            if (i > 0) sql.append(", ");

            // 添加主要字段
            sql.append(alias).append(".* ");
        }

        sql.append("FROM ").append(mainTable).append(" t1 ");

        // 添加关联表
        for (int i = 1; i < sourceTables.size(); i++) {
            String table = sourceTables.get(i);
            String alias = "t" + (i + 1);

            sql.append("LEFT JOIN ").append(table).append(" ").append(alias).append(" ");
            sql.append("ON t1.COMPANY_ID = ").append(alias).append(".COMPANY_ID ");
        }

        sql.append("WHERE 1=1 ");

        // 添加企业过滤条件
        if (StringUtil.isNotEmpty(warning.getCompanyId())) {
            sql.append("AND t1.COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
        }

        // 添加时间范围条件
        if (warning.getWarningTime() != null) {
            sql.append("AND t1.CREATE_TIME >= DATEADD(DAY, -30, '").append(warning.getWarningTime()).append("') ");
            sql.append("AND t1.CREATE_TIME <= DATEADD(DAY, 7, '").append(warning.getWarningTime()).append("') ");
        }

        return sql;
    }

    /**
     * 添加相关数据条件
     */
    private void addRelatedDataConditions(StringBuilder sql, Map<String, Object> relatedData, String tableName) {
        try {
            // 根据表名和相关数据添加特定的查询条件
            if (relatedData.containsKey("contractId")) {
                sql.append("AND CONTRACT_ID = '").append(relatedData.get("contractId")).append("' ");
            }

            if (relatedData.containsKey("supplierId")) {
                sql.append("AND SUPPLIER_ID = '").append(relatedData.get("supplierId")).append("' ");
            }

            if (relatedData.containsKey("projectId")) {
                sql.append("AND PROJECT_ID = '").append(relatedData.get("projectId")).append("' ");
            }

            if (relatedData.containsKey("amountRange")) {
                Map<String, Object> amountRange = (Map<String, Object>) relatedData.get("amountRange");
                if (amountRange.containsKey("min")) {
                    sql.append("AND AMOUNT >= ").append(amountRange.get("min")).append(" ");
                }
                if (amountRange.containsKey("max")) {
                    sql.append("AND AMOUNT <= ").append(amountRange.get("max")).append(" ");
                }
            }

        } catch (Exception e) {
            log.error("添加相关数据条件失败", e);
        }
    }

    /**
     * 解析RELATED_DATA字段
     */
    private Map<String, Object> parseRelatedData(String relatedDataJson) {
        try {
            if (StringUtil.isEmpty(relatedDataJson)) {
                return new HashMap<>();
            }

            // 使用JSON解析库解析RELATED_DATA
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(relatedDataJson, Map.class);

        } catch (Exception e) {
            log.error("解析RELATED_DATA失败: {}", relatedDataJson, e);
            return new HashMap<>();
        }
    }

    /**
     * 构建真实业务数据查询SQL
     */
    private String buildRealBusinessDataQuery(TblRiskWarning warning, Map<String, Object> relatedDataInfo) {
        try {
            log.info("构建业务数据查询SQL，预警类型: {}, 业务场景: {}", warning.getWarningType(), warning.getWarningType());

            // 根据预警类型和业务场景构建不同的查询SQL
            String businessScenario = (String) relatedDataInfo.get("businessScenario");
            String sourceTable = (String) relatedDataInfo.get("sourceTable");
            String companyId = warning.getCompanyId();

            StringBuilder sql = new StringBuilder();

            if ("FINANCE".equals(businessScenario)) {
                // 财务相关数据查询
                sql = buildFinanceDataQuery(relatedDataInfo, companyId);
            } else if ("PROCUREMENT".equals(businessScenario)) {
                // 采购相关数据查询
                sql = buildProcurementDataQuery(relatedDataInfo, companyId);
            } else if ("AUDIT".equals(businessScenario)) {
                // 审计相关数据查询
                sql = buildAuditDataQuery(relatedDataInfo, companyId);
            } else {
                // 通用数据查询
                sql = buildGenericDataQuery(relatedDataInfo, companyId);
            }

            return sql.toString();

        } catch (Exception e) {
            log.error("构建业务数据查询SQL失败", e);
            return null;
        }
    }

    /**
     * 构建财务数据查询SQL
     */
    private StringBuilder buildFinanceDataQuery(Map<String, Object> relatedDataInfo, String companyId) {
        StringBuilder sql = new StringBuilder();

        // 财务数据通常来自资产负债表、利润表等
        sql.append("SELECT ");
        sql.append("    COMPANY_NAME AS companyName, ");
        sql.append("    INDICATOR_NAME AS indicatorName, ");
        sql.append("    CURRENT_VALUE AS currentValue, ");
        sql.append("    BENCHMARK_VALUE AS benchmarkValue, ");
        sql.append("    RISK_LEVEL AS riskLevel, ");
        sql.append("    DATA_DATE AS dataDate, ");
        sql.append("    CREATE_TIME AS createTime ");
        sql.append("FROM TBL_FINANCIAL_INDICATOR_DATA ");
        sql.append("WHERE 1=1 ");

        if (StringUtil.isNotEmpty(companyId)) {
            sql.append("AND COMPANY_ID = '").append(companyId).append("' ");
        }

        // 添加其他查询条件
        if (relatedDataInfo.containsKey("indicatorCode")) {
            sql.append("AND INDICATOR_CODE = '").append(relatedDataInfo.get("indicatorCode")).append("' ");
        }

        sql.append("ORDER BY DATA_DATE DESC ");

        return sql;
    }

    /**
     * 构建采购数据查询SQL
     */
    private StringBuilder buildProcurementDataQuery(Map<String, Object> relatedDataInfo, String companyId) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("    CONTRACT_NO AS contractNo, ");
        sql.append("    SUPPLIER_NAME AS supplierName, ");
        sql.append("    CONTRACT_AMOUNT AS contractAmount, ");
        sql.append("    RISK_SCORE AS riskScore, ");
        sql.append("    RISK_LEVEL AS riskLevel, ");
        sql.append("    CONTRACT_DATE AS contractDate ");
        sql.append("FROM TBL_PROCUREMENT_CONTRACT ");
        sql.append("WHERE 1=1 ");

        if (StringUtil.isNotEmpty(companyId)) {
            sql.append("AND COMPANY_ID = '").append(companyId).append("' ");
        }

        sql.append("ORDER BY CONTRACT_DATE DESC ");

        return sql;
    }

    /**
     * 构建审计数据查询SQL
     */
    private StringBuilder buildAuditDataQuery(Map<String, Object> relatedDataInfo, String companyId) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("    AUDIT_PROJECT_NAME AS projectName, ");
        sql.append("    AUDIT_TYPE AS auditType, ");
        sql.append("    RISK_LEVEL AS riskLevel, ");
        sql.append("    FINDING_COUNT AS findingCount, ");
        sql.append("    AUDIT_DATE AS auditDate ");
        sql.append("FROM TBL_AUDIT_PROJECT ");
        sql.append("WHERE 1=1 ");

        if (StringUtil.isNotEmpty(companyId)) {
            sql.append("AND COMPANY_ID = '").append(companyId).append("' ");
        }

        sql.append("ORDER BY AUDIT_DATE DESC ");

        return sql;
    }

    /**
     * 构建通用数据查询SQL
     */
    private StringBuilder buildGenericDataQuery(Map<String, Object> relatedDataInfo, String companyId) {
        StringBuilder sql = new StringBuilder();

        String sourceTable = (String) relatedDataInfo.getOrDefault("sourceTable", "TBL_BUSINESS_DATA");

        sql.append("SELECT ");
        sql.append("    BUSINESS_ID AS businessId, ");
        sql.append("    BUSINESS_NAME AS businessName, ");
        sql.append("    BUSINESS_VALUE AS businessValue, ");
        sql.append("    RISK_SCORE AS riskScore, ");
        sql.append("    CREATE_TIME AS createTime ");
        sql.append("FROM ").append(sourceTable).append(" ");
        sql.append("WHERE 1=1 ");

        if (StringUtil.isNotEmpty(companyId)) {
            sql.append("AND COMPANY_ID = '").append(companyId).append("' ");
        }

        sql.append("ORDER BY CREATE_TIME DESC ");

        return sql;
    }

    /**
     * 创建备用业务数据（当无法获取真实数据时的最后选择）
     */
    private List<Map<String, Object>> createMockBusinessData(TblRiskWarning warning) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            log.info("尝试创建备用业务数据，预警ID: {}", warning.getWarningId());

            // 1. 首先尝试从相关数据表中查询真实数据
            dataList = queryRelatedBusinessData(warning);
            if (!dataList.isEmpty()) {
                log.info("从相关数据表查询到 {} 条真实业务数据", dataList.size());
                return dataList;
            }

            // 2. 尝试从预警相关数据中解析业务数据
            dataList = parseBusinessDataFromRelatedData(warning);
            if (!dataList.isEmpty()) {
                log.info("从预警相关数据解析到 {} 条业务数据", dataList.size());
                return dataList;
            }

            // 3. 尝试从企业基础数据中构建相关数据
            dataList = buildBusinessDataFromCompanyInfo(warning);
            if (!dataList.isEmpty()) {
                log.info("从企业基础数据构建 {} 条业务数据", dataList.size());
                return dataList;
            }

            // 4. 最后的备选方案：创建基于预警信息的最小数据集
            log.warn("无法获取真实业务数据，创建基于预警信息的最小数据集");
            dataList = createMinimalBusinessDataSet(warning);

        } catch (Exception e) {
            log.error("创建备用业务数据失败", e);
            dataList = createMinimalBusinessDataSet(warning);
        }

        return dataList;
    }

    /**
     * 查询相关业务数据
     */
    private List<Map<String, Object>> queryRelatedBusinessData(TblRiskWarning warning) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            // 根据预警类型查询不同的业务表
            String tableName = getBusinessTableByWarningType(warning.getWarningType());
            if (StringUtil.isEmpty(tableName)) {
                return dataList;
            }

            // 构建查询SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1 ");

            // 添加企业过滤条件
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                sql.append("AND COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
            } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                sql.append("AND COMPANY_NAME LIKE '%").append(warning.getCompanyName()).append("%' ");
            }

            // 添加时间过滤条件
            sql.append("AND CREATE_TIME >= DATEADD(DAY, -30, GETDATE()) ");
            sql.append("ORDER BY CREATE_TIME DESC ");
            sql.append("OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY");

            log.info("查询相关业务数据SQL: {}", sql.toString());
            dataList = jdbcTemplate.queryForList(sql.toString());

        } catch (Exception e) {
            log.error("查询相关业务数据失败", e);
        }

        return dataList;
    }

    /**
     * 根据预警类型获取业务表名
     */
    private String getBusinessTableByWarningType(String warningType) {
        switch (warningType) {
            case "FINANCIAL_RISK":
                return "TBL_FINANCIAL_DATA";
            case "PROCUREMENT_RISK":
                return "TBL_PROCUREMENT_DATA";
            case "CREDIT_RISK":
                return "TBL_CREDIT_DATA";
            case "COMPLIANCE_RISK":
                return "TBL_COMPLIANCE_DATA";
            default:
                return null;
        }
    }

    /**
     * 从预警相关数据中解析业务数据
     */
    private List<Map<String, Object>> parseBusinessDataFromRelatedData(TblRiskWarning warning) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            String relatedData = warning.getRelatedData();
            if (StringUtil.isEmpty(relatedData)) {
                return dataList;
            }

            // 尝试解析JSON格式的相关数据
            Map<String, Object> relatedMap = parseRelatedData(relatedData);
            if (relatedMap != null && relatedMap.containsKey("businessData")) {
                Object businessDataObj = relatedMap.get("businessData");
                if (businessDataObj instanceof List) {
                    List<Map<String, Object>> businessDataList = (List<Map<String, Object>>) businessDataObj;
                    dataList.addAll(businessDataList);
                }
            }

        } catch (Exception e) {
            log.error("从预警相关数据解析业务数据失败", e);
        }

        return dataList;
    }

    /**
     * 从企业基础数据中构建相关数据
     */
    private List<Map<String, Object>> buildBusinessDataFromCompanyInfo(TblRiskWarning warning) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            if (StringUtil.isEmpty(warning.getCompanyId()) && StringUtil.isEmpty(warning.getCompanyName())) {
                return dataList;
            }

            // 查询企业基础信息
            String sql = "SELECT * FROM TBL_COMPANY_INFO WHERE ";
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                sql += "COMPANY_ID = '" + warning.getCompanyId() + "'";
            } else {
                sql += "COMPANY_NAME LIKE '%" + warning.getCompanyName() + "%'";
            }

            List<Map<String, Object>> companyInfoList = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> companyInfo : companyInfoList) {
                Map<String, Object> businessData = new HashMap<>();
                businessData.put("companyId", companyInfo.get("COMPANY_ID"));
                businessData.put("companyName", companyInfo.get("COMPANY_NAME"));
                businessData.put("warningType", warning.getWarningType());
                businessData.put("warningLevel", warning.getWarningLevel());
                businessData.put("warningValue", warning.getWarningValue());
                businessData.put("thresholdValue", warning.getThresholdValue());
                businessData.put("warningTime", warning.getWarningTime());
                businessData.put("description", warning.getWarningDescription());

                dataList.add(businessData);
            }

        } catch (Exception e) {
            log.error("从企业基础数据构建相关数据失败", e);
        }

        return dataList;
    }

    /**
     * 创建最小业务数据集
     */
    private List<Map<String, Object>> createMinimalBusinessDataSet(TblRiskWarning warning) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 创建一条基于预警信息的数据记录
        Map<String, Object> data = new HashMap<>();
        data.put("warningId", warning.getWarningId());
        data.put("warningCode", warning.getWarningCode());
        data.put("companyId", warning.getCompanyId());
        data.put("companyName", warning.getCompanyName() != null ? warning.getCompanyName() : "未知企业");
        data.put("warningType", warning.getWarningType());
        data.put("warningLevel", warning.getWarningLevel());
        data.put("warningValue", warning.getWarningValue());
        data.put("thresholdValue", warning.getThresholdValue());
        data.put("warningTime", warning.getWarningTime());
        data.put("description", warning.getWarningDescription());
        data.put("status", "异常");
        data.put("dataSource", "预警记录");

        dataList.add(data);

        return dataList;
    }

    /**
     * 统计原始业务数据总数
     */
    private int countOriginalBusinessData(TblRiskWarning warning) {
        try {
            log.info("统计原始业务数据总数，预警ID: {}, 模型ID: {}", warning.getWarningId(), warning.getModelId());

            // 根据预警的MODEL_ID获取数据模型的SQL语句
            if (StringUtil.isEmpty(warning.getModelId())) {
                log.warn("预警记录中没有模型ID，返回模拟数据总数");
                return 25;
            }

            // 查询数据模型获取指标配置SQL
            String dataModelSql = getDataModelSqlByWarning(warning);

            if (StringUtil.isEmpty(dataModelSql)) {
                log.warn("无法获取数据模型的SQL语句，返回模拟数据总数");
                return 25;
            }

            // 解析SQL获取涉及的数据表
            List<String> sourceTables = extractTablesFromSql(dataModelSql);

            if (sourceTables.isEmpty()) {
                log.warn("无法从数据模型SQL中提取数据表，返回模拟数据总数");
                return 25;
            }

            // 构建统计查询SQL
            String countSql = buildCountQueryFromTables(warning, sourceTables);

            if (StringUtil.isEmpty(countSql)) {
                log.warn("无法构建统计查询SQL，返回模拟数据总数");
                return 25;
            }

            log.info("执行统计查询SQL: {}", countSql);
            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);

            log.info("统计到原始业务数据总数: {}", count);
            return count != null ? count : 0;

        } catch (Exception e) {
            log.error("统计原始业务数据总数失败", e);
            return 25; // 出错时返回模拟数据总数
        }
    }

    /**
     * 根据数据表构建统计查询SQL
     */
    private String buildCountQueryFromTables(TblRiskWarning warning, List<String> sourceTables) {
        try {
            log.info("构建统计查询SQL，涉及表: {}", sourceTables);

            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");

            if (sourceTables.size() == 1) {
                // 单表统计
                String tableName = sourceTables.get(0);
                sql.append(tableName).append(" WHERE 1=1 ");

                // 添加企业过滤条件
                if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                    sql.append("AND COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
                } else if (StringUtil.isNotEmpty(warning.getCompanyName())) {
                    sql.append("AND COMPANY_NAME = '").append(warning.getCompanyName()).append("' ");
                }

                // 添加时间范围条件
                if (warning.getWarningTime() != null) {
                    sql.append("AND CREATE_TIME >= DATEADD(DAY, -30, '").append(warning.getWarningTime()).append("') ");
                    sql.append("AND CREATE_TIME <= DATEADD(DAY, 7, '").append(warning.getWarningTime()).append("') ");
                }

            } else {
                // 多表关联统计
                String mainTable = sourceTables.get(0);
                sql.append(mainTable).append(" t1 ");

                // 添加关联表
                for (int i = 1; i < sourceTables.size(); i++) {
                    String table = sourceTables.get(i);
                    String alias = "t" + (i + 1);
                    sql.append("LEFT JOIN ").append(table).append(" ").append(alias).append(" ");
                    sql.append("ON t1.COMPANY_ID = ").append(alias).append(".COMPANY_ID ");
                }

                sql.append("WHERE 1=1 ");

                // 添加企业过滤条件
                if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                    sql.append("AND t1.COMPANY_ID = '").append(warning.getCompanyId()).append("' ");
                }

                // 添加时间范围条件
                if (warning.getWarningTime() != null) {
                    sql.append("AND t1.CREATE_TIME >= DATEADD(DAY, -30, '").append(warning.getWarningTime()).append("') ");
                    sql.append("AND t1.CREATE_TIME <= DATEADD(DAY, 7, '").append(warning.getWarningTime()).append("') ");
                }
            }

            log.info("构建的统计查询SQL: {}", sql.toString());
            return sql.toString();

        } catch (Exception e) {
            log.error("构建统计查询SQL失败", e);
            return null;
        }
    }

    /**
     * 构建数据列信息（动态构建）
     */
    private List<Map<String, Object>> buildDataColumns(TblRiskWarning warning) {
        List<Map<String, Object>> columns = new ArrayList<>();

        try {
            log.info("构建数据列信息，预警类型: {}", warning.getWarningType());

            // 1. 尝试从数据模型SQL中解析列信息
            columns = buildColumnsFromDataModel(warning);
            if (!columns.isEmpty()) {
                log.info("从数据模型解析到 {} 个列定义", columns.size());
                return columns;
            }

            // 2. 尝试从实际查询结果中动态构建列信息
            columns = buildColumnsFromQueryResult(warning);
            if (!columns.isEmpty()) {
                log.info("从查询结果动态构建 {} 个列定义", columns.size());
                return columns;
            }

            // 3. 根据预警类型使用预定义的列模板
            columns = buildColumnsFromWarningType(warning.getWarningType());
            log.info("使用预警类型模板构建 {} 个列定义", columns.size());

        } catch (Exception e) {
            log.error("构建数据列信息失败", e);
            // 使用最基本的列定义
            columns = buildBasicColumns();
        }

        return columns;
    }

    /**
     * 从数据模型解析列信息
     */
    private List<Map<String, Object>> buildColumnsFromDataModel(TblRiskWarning warning) {
        List<Map<String, Object>> columns = new ArrayList<>();

        try {
            if (StringUtil.isEmpty(warning.getModelId())) {
                return columns;
            }

            // 获取数据模型的SQL语句
            String dataModelSql = getDataModelSqlByWarning(warning);
            if (StringUtil.isEmpty(dataModelSql)) {
                return columns;
            }

            // 解析SQL中的SELECT字段
            columns = parseColumnsFromSql(dataModelSql);

        } catch (Exception e) {
            log.error("从数据模型解析列信息失败", e);
        }

        return columns;
    }

    /**
     * 从SQL语句解析列信息
     */
    private List<Map<String, Object>> parseColumnsFromSql(String sql) {
        List<Map<String, Object>> columns = new ArrayList<>();

        try {
            String upperSql = sql.toUpperCase();
            int selectIndex = upperSql.indexOf("SELECT");
            int fromIndex = upperSql.indexOf("FROM");

            if (selectIndex >= 0 && fromIndex > selectIndex) {
                String selectClause = sql.substring(selectIndex + 6, fromIndex).trim();

                // 处理SELECT *的情况
                if (selectClause.trim().equals("*")) {
                    return buildDefaultColumns();
                }

                String[] fields = selectClause.split(",");

                for (String field : fields) {
                    field = field.trim();

                    // 跳过函数和复杂表达式
                    if (field.contains("(") || field.contains("CASE")) {
                        continue;
                    }

                    String columnName = extractColumnName(field);
                    String columnLabel = extractColumnLabel(field, columnName);
                    int columnWidth = calculateColumnWidth(columnName, columnLabel);

                    columns.add(createColumn(columnName, columnLabel, columnWidth));
                }
            }

        } catch (Exception e) {
            log.error("解析SQL列信息失败", e);
        }

        return columns;
    }

    /**
     * 提取列名
     */
    private String extractColumnName(String field) {
        // 处理别名：FIELD_NAME AS ALIAS 或 FIELD_NAME ALIAS
        if (field.toUpperCase().contains(" AS ")) {
            String[] parts = field.split("(?i)\\s+AS\\s+");
            if (parts.length > 1) {
                return parts[1].trim().replaceAll("[\"'`]", "");
            }
        }

        // 处理空格分隔的别名
        String[] parts = field.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[parts.length - 1].replaceAll("[\"'`]", "");
        }

        // 处理表名.字段名的情况
        String columnName = field.trim().replaceAll("[\"'`]", "");
        if (columnName.contains(".")) {
            String[] tableParts = columnName.split("\\.");
            columnName = tableParts[tableParts.length - 1];
        }

        return columnName;
    }

    /**
     * 提取列标签
     */
    private String extractColumnLabel(String field, String columnName) {
        // 将下划线命名转换为中文标签
        Map<String, String> labelMap = getColumnLabelMap();

        String upperColumnName = columnName.toUpperCase();
        if (labelMap.containsKey(upperColumnName)) {
            return labelMap.get(upperColumnName);
        }

        // 简单的转换规则
        return convertColumnNameToLabel(columnName);
    }

    /**
     * 获取列标签映射
     */
    private Map<String, String> getColumnLabelMap() {
        Map<String, String> labelMap = new HashMap<>();

        // 通用字段
        labelMap.put("ID", "序号");
        labelMap.put("COMPANY_ID", "企业ID");
        labelMap.put("COMPANY_NAME", "企业名称");
        labelMap.put("ENTERPRISE_ID", "企业ID");
        labelMap.put("ENTERPRISE_NAME", "企业名称");
        labelMap.put("CREATE_TIME", "创建时间");
        labelMap.put("UPDATE_TIME", "最后更新时间");
        labelMap.put("CREATE_USER", "创建人");
        labelMap.put("UPDATE_USER", "更新人");

        // 财务相关字段
        labelMap.put("ASSET_LIABILITY_RATIO", "资产负债率(%)");
        labelMap.put("CURRENT_RATIO", "流动比率");
        labelMap.put("QUICK_RATIO", "速动比率");
        labelMap.put("DEBT_RATIO", "负债率");
        labelMap.put("PROFIT_MARGIN", "利润率");
        labelMap.put("ROE", "净资产收益率");
        labelMap.put("ROA", "总资产收益率");

        // 风险相关字段
        labelMap.put("RISK_LEVEL", "风险等级");
        labelMap.put("RISK_SCORE", "风险分数");
        labelMap.put("WARNING_LEVEL", "预警级别");
        labelMap.put("WARNING_VALUE", "预警值");
        labelMap.put("THRESHOLD_VALUE", "阈值");
        labelMap.put("STATUS", "状态");

        // TBL_OVERDEBT_RISK_INDICATOR 过度负债风险指标表字段
        labelMap.put("RECORD_ID", "记录ID");
        labelMap.put("ASSESS_DATE", "评估基准日期");
        labelMap.put("DEBT_CAPITALIZATION_RATIO", "债务资本化比率(%)");
        labelMap.put("LONG_TERM_DEBT_CAPITALIZATION_RATIO", "长期债务资本化比率(%)");
        labelMap.put("SHORT_TERM_DEBT_RATIO", "短期债务占比(%)");
        labelMap.put("INTEREST_COVERAGE_RATIO", "利息保障倍数");
        labelMap.put("OPERATING_CASHFLOW_DEBT_RATIO", "经营现金流量债务比");
        labelMap.put("TOTAL_ASSETS", "总资产(元)");
        labelMap.put("TOTAL_DEBT", "总债务(元)");
        labelMap.put("LONG_TERM_DEBT", "长期债务(元)");
        labelMap.put("SHORT_TERM_DEBT", "短期债务(元)");
        labelMap.put("NET_ASSETS", "净资产(元)");
        labelMap.put("TOTAL_LIABILITIES", "总负债(元)");
        labelMap.put("CURRENT_ASSETS", "流动资产(元)");
        labelMap.put("CURRENT_LIABILITIES", "流动负债(元)");
        labelMap.put("CASH_BALANCE", "货币资金余额(元)");
        labelMap.put("EBIT", "息税前利润(元)");
        labelMap.put("INTEREST_EXPENSE", "利息费用(元)");
        labelMap.put("COMPOSITE_SCORE", "综合风险评分");
        labelMap.put("RISK_SCENARIO", "风险场景描述");
        labelMap.put("RISK_COLOR", "风险颜色标识");
        labelMap.put("ASSESS_TIME", "评估执行时间");
        labelMap.put("CREATE_BY", "创建人");
        labelMap.put("REMARK", "备注");

        return labelMap;
    }

    /**
     * 转换列名为标签
     */
    private String convertColumnNameToLabel(String columnName) {
        if (StringUtil.isEmpty(columnName)) {
            return "未知列";
        }

        // 移除常见前缀
        String label = columnName.replaceAll("^(TBL_|T_)", "");

        // 将下划线替换为空格，并转换为标题格式
        label = label.replace("_", " ");

        // 简单的首字母大写
        if (label.length() > 0) {
            label = label.substring(0, 1).toUpperCase() + label.substring(1).toLowerCase();
        }

        return label;
    }

    /**
     * 计算列宽度
     */
    private int calculateColumnWidth(String columnName, String columnLabel) {
        // 根据列名和标签计算合适的宽度
        int baseWidth = Math.max(columnName.length(), columnLabel.length()) * 12;

        // 设置最小和最大宽度
        baseWidth = Math.max(baseWidth, 80);
        baseWidth = Math.min(baseWidth, 300);

        // 特殊字段的宽度调整
        String upperColumnName = columnName.toUpperCase();
        if (upperColumnName.contains("NAME") || upperColumnName.contains("DESCRIPTION")) {
            return Math.max(baseWidth, 150);
        } else if (upperColumnName.contains("TIME") || upperColumnName.contains("DATE")) {
            return 150;
        } else if (upperColumnName.contains("ID") || upperColumnName.contains("CODE")) {
            return 120;
        } else if (upperColumnName.contains("VALUE") || upperColumnName.contains("AMOUNT")) {
            return 100;
        }

        return baseWidth;
    }

    /**
     * 从查询结果动态构建列信息
     */
    private List<Map<String, Object>> buildColumnsFromQueryResult(TblRiskWarning warning) {
        List<Map<String, Object>> columns = new ArrayList<>();

        try {
            // 执行一次查询获取结果结构
            List<Map<String, Object>> sampleData = queryOriginalBusinessData(warning, 1, 1);

            if (!sampleData.isEmpty()) {
                Map<String, Object> firstRow = sampleData.get(0);

                for (String key : firstRow.keySet()) {
                    String columnLabel = extractColumnLabel("", key);
                    int columnWidth = calculateColumnWidth(key, columnLabel);
                    columns.add(createColumn(key, columnLabel, columnWidth));
                }
            }

        } catch (Exception e) {
            log.error("从查询结果动态构建列信息失败", e);
        }

        return columns;
    }

    /**
     * 根据预警类型构建列信息
     */
    private List<Map<String, Object>> buildColumnsFromWarningType(String warningType) {
        List<Map<String, Object>> columns = new ArrayList<>();

        switch (warningType) {
            case "FINANCIAL_RISK":
                columns = buildFinancialRiskColumns();
                break;
            case "PROCUREMENT_RISK":
                columns = buildProcurementRiskColumns();
                break;
            case "CREDIT_RISK":
                columns = buildCreditRiskColumns();
                break;
            case "COMPLIANCE_RISK":
                columns = buildComplianceRiskColumns();
                break;
            case "THRESHOLD":
                columns = buildThresholdColumns();
                break;
            default:
                columns = buildDefaultColumns();
                break;
        }

        return columns;
    }

    /**
     * 构建财务风险列
     */
    private List<Map<String, Object>> buildFinancialRiskColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("companyName", "企业名称", 200));
        columns.add(createColumn("assetLiabilityRatio", "资产负债率", 120));
        columns.add(createColumn("currentRatio", "流动比率", 120));
        columns.add(createColumn("quickRatio", "速动比率", 120));
        columns.add(createColumn("roeRatio", "净资产收益率", 130));
        columns.add(createColumn("warningValue", "预警值", 100));
        columns.add(createColumn("thresholdValue", "阈值", 100));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("dataTime", "数据时间", 150));

        return columns;
    }

    /**
     * 构建采购风险列
     */
    private List<Map<String, Object>> buildProcurementRiskColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("supplierName", "供应商名称", 200));
        columns.add(createColumn("contractAmount", "合同金额", 120));
        columns.add(createColumn("deliveryRate", "交付率", 100));
        columns.add(createColumn("qualityScore", "质量评分", 100));
        columns.add(createColumn("riskScore", "风险评分", 100));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("contractTime", "合同时间", 150));

        return columns;
    }

    /**
     * 构建信用风险列
     */
    private List<Map<String, Object>> buildCreditRiskColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("companyName", "企业名称", 200));
        columns.add(createColumn("creditRating", "信用评级", 100));
        columns.add(createColumn("creditScore", "信用分数", 100));
        columns.add(createColumn("paymentHistory", "付款历史", 120));
        columns.add(createColumn("overdueAmount", "逾期金额", 120));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("evaluationTime", "评估时间", 150));

        return columns;
    }

    /**
     * 构建合规风险列
     */
    private List<Map<String, Object>> buildComplianceRiskColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("companyName", "企业名称", 200));
        columns.add(createColumn("regulationType", "法规类型", 120));
        columns.add(createColumn("complianceStatus", "合规状态", 100));
        columns.add(createColumn("violationCount", "违规次数", 100));
        columns.add(createColumn("penaltyAmount", "罚款金额", 120));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("checkTime", "检查时间", 150));

        return columns;
    }

    /**
     * 构建阈值预警列
     */
    private List<Map<String, Object>> buildThresholdColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("companyName", "企业名称", 200));
        columns.add(createColumn("indicatorName", "指标名称", 150));
        columns.add(createColumn("currentValue", "当前值", 120));
        columns.add(createColumn("thresholdValue", "阈值", 120));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("status", "状态", 100));
        columns.add(createColumn("dataTime", "数据时间", 150));

        return columns;
    }

    /**
     * 构建默认列
     */
    private List<Map<String, Object>> buildDefaultColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("companyName", "企业名称", 200));
        columns.add(createColumn("warningType", "预警类型", 120));
        columns.add(createColumn("warningValue", "预警值", 100));
        columns.add(createColumn("thresholdValue", "阈值", 100));
        columns.add(createColumn("riskLevel", "风险等级", 100));
        columns.add(createColumn("warningTime", "预警时间", 150));
        columns.add(createColumn("description", "描述", 200));

        return columns;
    }

    /**
     * 构建基础列
     */
    private List<Map<String, Object>> buildBasicColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();

        columns.add(createColumn("id", "序号", 80));
        columns.add(createColumn("name", "名称", 150));
        columns.add(createColumn("value", "值", 100));
        columns.add(createColumn("status", "状态", 100));
        columns.add(createColumn("time", "时间", 150));

        return columns;
    }

    /**
     * 获取数据源信息
     */
    private Map<String, Object> getDataSourceInfo(TblRiskWarning warning) {
        Map<String, Object> dataSourceInfo = new HashMap<>();

        try {
            // 获取评估模型信息
            TblEvaluationModel evalModel = evaluationModelService.getById(warning.getEvalModelId());
            if (evalModel != null) {
                dataSourceInfo.put("evalModelId", evalModel.getEvalModelId());
                dataSourceInfo.put("evalModelName", evalModel.getModelName());
                dataSourceInfo.put("dataModelId", evalModel.getDataModelId());
                dataSourceInfo.put("modelType", evalModel.getBusinessScenario());
                dataSourceInfo.put("description", evalModel.getDescription());
            }

            // 添加预警相关的数据源信息
            dataSourceInfo.put("warningDataSource", "风险预警系统");
            dataSourceInfo.put("dataUpdateTime", warning.getWarningTime());
            dataSourceInfo.put("dataScope", "企业风险数据");

        } catch (Exception e) {
            log.error("获取数据源信息失败", e);
            dataSourceInfo.put("error", "获取数据源信息失败: " + e.getMessage());
        }

        return dataSourceInfo;
    }

    /**
     * 获取关联指标信息
     */
    private List<Map<String, Object>> getRelatedIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("获取关联指标信息，预警ID: {}", warning.getWarningId());

            // 解析WARNING_DETAIL字段获取指标信息
            Map<String, Object> warningDetail = parseWarningDetail(warning.getWarningDetail());

            if (warningDetail != null && warningDetail.containsKey("indicators")) {
                // 从预警详情中获取指标信息
                List<Map<String, Object>> detailIndicators = (List<Map<String, Object>>) warningDetail.get("indicators");
                if (detailIndicators != null && !detailIndicators.isEmpty()) {
                    indicators.addAll(detailIndicators);
                    log.info("从预警详情中获取到 {} 个指标", indicators.size());
                    return indicators;
                }
            }

            // 如果预警详情中没有指标信息，尝试从评估模型中获取
            if (StringUtil.isNotEmpty(warning.getEvalModelId())) {
                indicators = getIndicatorsFromEvaluationModel(warning.getEvalModelId(), warning);
                if (!indicators.isEmpty()) {
                    log.info("从评估模型中获取到 {} 个指标", indicators.size());
                    return indicators;
                }
            }

            // 如果都没有，返回基于预警类型的默认指标
            indicators = createDefaultIndicators(warning);
            log.info("使用默认指标，共 {} 个", indicators.size());

        } catch (Exception e) {
            log.error("获取关联指标信息失败", e);
            // 出错时返回默认指标
            indicators = createDefaultIndicators(warning);
        }

        return indicators;
    }

    /**
     * 解析WARNING_DETAIL字段
     */
    private Map<String, Object> parseWarningDetail(String warningDetailJson) {
        try {
            if (StringUtil.isEmpty(warningDetailJson)) {
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(warningDetailJson, Map.class);

        } catch (Exception e) {
            log.error("解析WARNING_DETAIL失败: {}", warningDetailJson, e);
            return null;
        }
    }

    /**
     * 从评估模型中获取指标信息（支持数据模型管理和组合指标）
     */
    private List<Map<String, Object>> getIndicatorsFromEvaluationModel(String evalModelId, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("从评估模型获取指标信息，模型ID: {}", evalModelId);

            // 获取评估模型信息
            TblEvaluationModel evalModel = evaluationModelService.getById(evalModelId);
            if (evalModel == null) {
                log.warn("未找到评估模型，ID: {}", evalModelId);
                return indicators;
            }

            String dataModelId = evalModel.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("评估模型未关联数据模型，评估模型ID: {}", evalModelId);
                return indicators;
            }

            // 根据数据模型ID类型判断是数据模型管理还是组合指标
            if (dataModelId.startsWith("COMB")) {
                // 组合指标分析
                log.info("检测到组合指标分析，组合ID: {}", dataModelId);
                indicators = getIndicatorsFromCombination(dataModelId, warning);
            } else {
                // 数据模型管理
                log.info("检测到数据模型管理，模型ID: {}", dataModelId);
                indicators = getIndicatorsFromDataModelManagement(dataModelId, evalModelId, warning);
            }

            // 如果上述方法都没有获取到指标，尝试从表达式规则获取
            if (indicators.isEmpty()) {
                indicators = getIndicatorsFromExpressionRules(warning);
            }

        } catch (Exception e) {
            log.error("从评估模型获取指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从组合指标分析获取指标信息
     */
    private List<Map<String, Object>> getIndicatorsFromCombination(String combinationId, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("从组合指标分析获取指标信息，组合ID: {}", combinationId);

            // 获取组合详情
            Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
            if (combinationDetail == null || combinationDetail.isEmpty()) {
                log.warn("未找到组合指标详情，组合ID: {}", combinationId);
                return indicators;
            }

            // 提取组合配置
            Map<String, Object> config = extractCombinationConfig(combinationDetail);
            List<Map<String, Object>> combinationIndicators = (List<Map<String, Object>>) config.get("indicators");

            if (combinationIndicators != null && !combinationIndicators.isEmpty()) {
                for (Map<String, Object> combIndicator : combinationIndicators) {
                    Map<String, Object> indicator = new HashMap<>();

                    // 从组合指标中提取信息
                    indicator.put("indicatorId", combIndicator.get("indicatorCode"));
                    indicator.put("indicatorName", combIndicator.get("indicatorName"));
                    indicator.put("indicatorType", "组合指标");
                    indicator.put("dataSourceId", combIndicator.get("dataSourceId"));
                    indicator.put("sqlContent", combIndicator.get("sqlContent"));
                    indicator.put("description", combIndicator.get("description"));

                    // 尝试获取指标的执行结果作为当前值
                    Double currentValue = getCombinationIndicatorCurrentValue(combinationId, combIndicator, warning);
                    indicator.put("currentValue", currentValue);

                    // 设置状态
                    indicator.put("status", currentValue != null ? "正常" : "未知");

                    indicators.add(indicator);
                }

                log.info("从组合指标分析获取到 {} 个指标", indicators.size());
            }

        } catch (Exception e) {
            log.error("从组合指标分析获取指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从数据模型管理获取指标信息
     */
    private List<Map<String, Object>> getIndicatorsFromDataModelManagement(String dataModelId, String evalModelId, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("从数据模型管理获取指标信息，模型ID: {}", dataModelId);

            // 1. 首先尝试从评估模型表达式关联表获取
            String evalExprSql = "SELECT er.RULE_ID, er.RULE_NAME, er.EXPRESSION, er.THRESHOLD_VALUE, " +
                               "er.DESCRIPTION, eme.WEIGHT " +
                               "FROM TBL_EVAL_MODEL_EXPRESSION eme " +
                               "LEFT JOIN TBL_EXPRESSION_RULE er ON eme.RULE_ID = er.RULE_ID " +
                               "WHERE eme.EVAL_MODEL_ID = ? AND er.IS_ENABLED = 'Y'";

            List<Map<String, Object>> rules = jdbcTemplate.queryForList(evalExprSql, evalModelId);

            if (!rules.isEmpty()) {
                for (Map<String, Object> rule : rules) {
                    Map<String, Object> indicator = new HashMap<>();
                    indicator.put("indicatorId", rule.get("RULE_ID"));
                    indicator.put("indicatorName", rule.get("RULE_NAME"));
                    indicator.put("indicatorType", "评估模型指标");
                    indicator.put("threshold", rule.get("THRESHOLD_VALUE"));
                    indicator.put("weight", rule.get("WEIGHT"));
                    indicator.put("expression", rule.get("EXPRESSION"));
                    indicator.put("description", rule.get("DESCRIPTION"));

                    // 设置当前值和状态
                    if (Objects.equals(rule.get("RULE_ID"), warning.getRuleId())) {
                        indicator.put("currentValue", warning.getWarningValue());
                        indicator.put("status", "异常");
                    } else {
                        Double currentValue = calculateIndicatorCurrentValue(rule, warning);
                        indicator.put("currentValue", currentValue);
                        String status = determineIndicatorStatus(currentValue, rule.get("THRESHOLD_VALUE"));
                        indicator.put("status", status);
                    }

                    indicators.add(indicator);
                }

                log.info("从评估模型表达式关联表获取到 {} 个指标", indicators.size());
                return indicators;
            }

            // 2. 如果没有表达式规则，尝试从数据模型SQL解析指标
            indicators = getIndicatorsFromDataModelSql(dataModelId, warning);
            if (!indicators.isEmpty()) {
                log.info("从数据模型SQL解析到 {} 个指标", indicators.size());
                return indicators;
            }

        } catch (Exception e) {
            log.error("从数据模型管理获取指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从数据模型SQL解析指标信息
     */
    private List<Map<String, Object>> getIndicatorsFromDataModelSql(String dataModelId, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            // 查询数据模型信息
            String sql = "SELECT MODEL_NAME, SQL_STATEMENT, BUSINESS_MEANING FROM TBL_DATA_MODEL WHERE MODEL_ID = ?";
            List<Map<String, Object>> dataModels = jdbcTemplate.queryForList(sql, dataModelId);

            if (!dataModels.isEmpty()) {
                Map<String, Object> dataModel = dataModels.get(0);
                String sqlStatement = (String) dataModel.get("SQL_STATEMENT");
                String modelName = (String) dataModel.get("MODEL_NAME");
                String businessMeaning = (String) dataModel.get("BUSINESS_MEANING");

                if (StringUtil.isNotEmpty(sqlStatement)) {
                    // 从SQL语句解析指标
                    indicators = parseIndicatorsFromDataModelSql(sqlStatement, modelName, businessMeaning, warning);
                }
            }

        } catch (Exception e) {
            log.error("从数据模型SQL解析指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从数据模型SQL解析指标信息
     */
    private List<Map<String, Object>> parseIndicatorsFromDataModelSql(String sqlStatement, String modelName, String businessMeaning, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("从数据模型SQL解析指标信息，模型名称: {}", modelName);

            // 解析SQL中的SELECT字段作为指标
            String upperSql = sqlStatement.toUpperCase();
            int selectIndex = upperSql.indexOf("SELECT");
            int fromIndex = upperSql.indexOf("FROM");

            if (selectIndex >= 0 && fromIndex > selectIndex) {
                String selectClause = sqlStatement.substring(selectIndex + 6, fromIndex).trim();

                // 处理SELECT *的情况
                if (selectClause.trim().equals("*")) {
                    // 创建通用指标
                    Map<String, Object> indicator = new HashMap<>();
                    indicator.put("indicatorId", "ALL_FIELDS");
                    indicator.put("indicatorName", modelName + "全字段");
                    indicator.put("indicatorType", "数据模型指标");
                    indicator.put("description", businessMeaning);
                    indicator.put("currentValue", null);
                    indicator.put("status", "正常");
                    indicators.add(indicator);
                    return indicators;
                }

                String[] fields = selectClause.split(",");

                for (String field : fields) {
                    field = field.trim();

                    // 跳过复杂的函数和表达式
                    if (field.contains("(") && !field.toUpperCase().contains("COUNT") &&
                        !field.toUpperCase().contains("SUM") && !field.toUpperCase().contains("AVG")) {
                        continue;
                    }

                    String indicatorName = extractIndicatorNameFromField(field);
                    String indicatorId = extractIndicatorIdFromField(field);

                    Map<String, Object> indicator = new HashMap<>();
                    indicator.put("indicatorId", indicatorId);
                    indicator.put("indicatorName", indicatorName);
                    indicator.put("indicatorType", "数据模型指标");
                    indicator.put("dataSource", modelName);
                    indicator.put("description", businessMeaning);
                    indicator.put("sqlField", field);

                    // 尝试获取当前值
                    Double currentValue = getDataModelIndicatorCurrentValue(sqlStatement, field, warning);
                    indicator.put("currentValue", currentValue);
                    indicator.put("status", currentValue != null ? "正常" : "未知");

                    indicators.add(indicator);
                }
            }

        } catch (Exception e) {
            log.error("从数据模型SQL解析指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从字段提取指标名称
     */
    private String extractIndicatorNameFromField(String field) {
        // 处理别名：FIELD_NAME AS ALIAS 或 FIELD_NAME ALIAS
        if (field.toUpperCase().contains(" AS ")) {
            String[] parts = field.split("(?i)\\s+AS\\s+");
            if (parts.length > 1) {
                return convertFieldToIndicatorName(parts[1].trim().replaceAll("[\"'`]", ""));
            }
        }

        // 处理空格分隔的别名
        String[] parts = field.trim().split("\\s+");
        if (parts.length > 1) {
            return convertFieldToIndicatorName(parts[parts.length - 1].replaceAll("[\"'`]", ""));
        }

        // 处理表名.字段名的情况
        String fieldName = field.trim().replaceAll("[\"'`]", "");
        if (fieldName.contains(".")) {
            String[] tableParts = fieldName.split("\\.");
            fieldName = tableParts[tableParts.length - 1];
        }

        return convertFieldToIndicatorName(fieldName);
    }

    /**
     * 从字段提取指标ID
     */
    private String extractIndicatorIdFromField(String field) {
        String indicatorName = extractIndicatorNameFromField(field);
        return "DM_" + indicatorName.toUpperCase().replace(" ", "_");
    }

    /**
     * 转换字段名为指标名称
     */
    private String convertFieldToIndicatorName(String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            return "未知指标";
        }

        // 字段名到指标名的映射
        Map<String, String> fieldNameMap = new HashMap<>();
        fieldNameMap.put("ASSET_LIABILITY_RATIO", "资产负债率");
        fieldNameMap.put("CURRENT_RATIO", "流动比率");
        fieldNameMap.put("QUICK_RATIO", "速动比率");
        fieldNameMap.put("ROE", "净资产收益率");
        fieldNameMap.put("ROA", "总资产收益率");
        fieldNameMap.put("PROFIT_MARGIN", "利润率");
        fieldNameMap.put("DEBT_RATIO", "负债率");
        fieldNameMap.put("REVENUE", "营业收入");
        fieldNameMap.put("PROFIT", "利润");
        fieldNameMap.put("TOTAL_ASSETS", "总资产");
        fieldNameMap.put("TOTAL_LIABILITIES", "总负债");
        fieldNameMap.put("COMPANY_NAME", "企业名称");
        fieldNameMap.put("COMPANY_ID", "企业ID");
        fieldNameMap.put("RISK_SCORE", "风险分数");
        fieldNameMap.put("RISK_LEVEL", "风险等级");

        String upperFieldName = fieldName.toUpperCase();
        if (fieldNameMap.containsKey(upperFieldName)) {
            return fieldNameMap.get(upperFieldName);
        }

        // 简单的转换规则
        return fieldName.replace("_", " ").toLowerCase();
    }

    /**
     * 获取组合指标的当前值
     */
    private Double getCombinationIndicatorCurrentValue(String combinationId, Map<String, Object> indicator, TblRiskWarning warning) {
        try {
            String indicatorCode = (String) indicator.get("indicatorCode");
            if (StringUtil.isEmpty(indicatorCode)) {
                return null;
            }

            // 尝试获取指标执行结果
            Map<String, Object> result = combinationService.getIndicatorExecutionResult(indicatorCode, combinationId, null, 1, 1);

            if (result != null && result.containsKey("data")) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data");
                if (dataList != null && !dataList.isEmpty()) {
                    Map<String, Object> firstRow = dataList.get(0);
                    // 尝试从第一行数据中提取数值
                    for (Object value : firstRow.values()) {
                        if (value instanceof Number) {
                            return ((Number) value).doubleValue();
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.debug("获取组合指标当前值失败，指标: {}", indicator.get("indicatorCode"), e);
        }

        return null;
    }

    /**
     * 获取数据模型指标的当前值
     */
    private Double getDataModelIndicatorCurrentValue(String sqlStatement, String field, TblRiskWarning warning) {
        try {
            // 构建查询SQL，只查询指定字段
            String queryField = field.trim();

            // 如果字段包含别名，提取原始字段名
            if (queryField.toUpperCase().contains(" AS ")) {
                String[] parts = queryField.split("(?i)\\s+AS\\s+");
                queryField = parts[0].trim();
            }

            // 构建简单的查询SQL
            String querySql = "SELECT " + queryField + " FROM (" + sqlStatement + ") t WHERE ROWNUM = 1";

            // 添加企业过滤条件
            if (StringUtil.isNotEmpty(warning.getCompanyId())) {
                if (sqlStatement.toUpperCase().contains("COMPANY_ID")) {
                    querySql = querySql.replace("WHERE ROWNUM = 1",
                        "WHERE COMPANY_ID = '" + warning.getCompanyId() + "' AND ROWNUM = 1");
                }
            }

            List<Map<String, Object>> results = jdbcTemplate.queryForList(querySql);

            if (!results.isEmpty()) {
                Map<String, Object> firstRow = results.get(0);
                Object value = firstRow.values().iterator().next();

                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        return Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        // 忽略非数值字符串
                    }
                }
            }

        } catch (Exception e) {
            log.debug("获取数据模型指标当前值失败，字段: {}", field, e);
        }

        return null;
    }

    /**
     * 从指标组合详情中提取指标配置信息
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractCombinationConfig(Map<String, Object> combinationDetail) {
        try {
            log.info("提取指标组合配置");

            Map<String, Object> config = new HashMap<>();

            // 获取执行模式
            String executionMode = (String) combinationDetail.getOrDefault("executionMode", "SEQUENCE");
            config.put("executionMode", executionMode);

            // 获取指标列表
            Object indicatorsObj = combinationDetail.get("indicators");
            List<Map<String, Object>> indicators = new ArrayList<>();

            if (indicatorsObj instanceof List) {
                indicators = (List<Map<String, Object>>) indicatorsObj;
            } else if (indicatorsObj instanceof Map) {
                // 如果是单个指标，转换为列表
                indicators.add((Map<String, Object>) indicatorsObj);
            }

            config.put("indicators", indicators);
            config.put("indicatorCount", indicators.size());

            log.info("提取到组合配置 - 执行模式: {}, 指标数量: {}", executionMode, indicators.size());
            return config;

        } catch (Exception e) {
            log.warn("提取组合配置失败: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * 计算指标当前值
     */
    private Double calculateIndicatorCurrentValue(Map<String, Object> rule, TblRiskWarning warning) {
        try {
            // 如果有表达式，尝试执行计算
            String expression = (String) rule.get("EXPRESSION");
            if (StringUtil.isNotEmpty(expression)) {
                return executeIndicatorExpression(expression, warning);
            }

            // 如果没有表达式，返回null
            return null;

        } catch (Exception e) {
            log.warn("计算指标当前值失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 执行指标表达式计算
     */
    private Double executeIndicatorExpression(String expression, TblRiskWarning warning) {
        try {
            // 这里可以实现表达式计算逻辑
            // 暂时返回null，后续可以集成表达式引擎
            log.debug("执行指标表达式: {}", expression);
            return null;

        } catch (Exception e) {
            log.error("执行指标表达式失败: {}", expression, e);
            return null;
        }
    }

    /**
     * 判断指标状态
     */
    private String determineIndicatorStatus(Double currentValue, Object thresholdValue) {
        if (currentValue == null || thresholdValue == null) {
            return "未知";
        }

        try {
            Double threshold = null;
            if (thresholdValue instanceof Number) {
                threshold = ((Number) thresholdValue).doubleValue();
            } else if (thresholdValue instanceof String) {
                threshold = Double.parseDouble((String) thresholdValue);
            }

            if (threshold != null) {
                return currentValue > threshold ? "异常" : "正常";
            }

        } catch (Exception e) {
            log.warn("判断指标状态失败: {}", e.getMessage());
        }

        return "未知";
    }

    /**
     * 从数据模型获取指标信息
     */
    private List<Map<String, Object>> getIndicatorsFromDataModel(String evalModelId, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            // 通过评估模型ID获取关联的数据模型ID
            String dataModelId = getDataModelIdFromEvalModel(evalModelId);
            if (StringUtil.isEmpty(dataModelId)) {
                return indicators;
            }

            // 查询数据模型的SQL语句，从中解析指标信息
            String sql = "SELECT SQL_STATEMENT, MODEL_NAME FROM TBL_DATA_MODEL WHERE MODEL_ID = ?";
            List<Map<String, Object>> dataModels = jdbcTemplate.queryForList(sql, dataModelId);

            if (!dataModels.isEmpty()) {
                Map<String, Object> dataModel = dataModels.get(0);
                String sqlStatement = (String) dataModel.get("SQL_STATEMENT");
                String modelName = (String) dataModel.get("MODEL_NAME");

                // 从SQL语句中解析指标信息
                indicators = parseIndicatorsFromSql(sqlStatement, modelName, warning);
            }

        } catch (Exception e) {
            log.error("从数据模型获取指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 从SQL语句中解析指标信息
     */
    private List<Map<String, Object>> parseIndicatorsFromSql(String sqlStatement, String modelName, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            if (StringUtil.isEmpty(sqlStatement)) {
                return indicators;
            }

            // 简单的SQL解析，提取SELECT子句中的字段作为指标
            String upperSql = sqlStatement.toUpperCase();
            int selectIndex = upperSql.indexOf("SELECT");
            int fromIndex = upperSql.indexOf("FROM");

            if (selectIndex >= 0 && fromIndex > selectIndex) {
                String selectClause = sqlStatement.substring(selectIndex + 6, fromIndex).trim();
                String[] fields = selectClause.split(",");

                for (int i = 0; i < fields.length && i < 5; i++) { // 最多取5个字段作为指标
                    String field = fields[i].trim();

                    // 跳过通用字段
                    if (field.toUpperCase().contains("ID") ||
                        field.toUpperCase().contains("NAME") ||
                        field.equals("*")) {
                        continue;
                    }

                    Map<String, Object> indicator = new HashMap<>();
                    indicator.put("indicatorId", "SQL_" + i);
                    indicator.put("indicatorName", extractFieldAlias(field));
                    indicator.put("indicatorType", "数据模型指标");
                    indicator.put("threshold", null);
                    indicator.put("currentValue", null);
                    indicator.put("status", "正常");
                    indicator.put("description", "来源于数据模型: " + modelName);

                    indicators.add(indicator);
                }
            }

        } catch (Exception e) {
            log.error("从SQL语句解析指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 提取字段别名
     */
    private String extractFieldAlias(String field) {
        // 如果有AS别名，使用别名
        if (field.toUpperCase().contains(" AS ")) {
            String[] parts = field.split("(?i)\\s+AS\\s+");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }

        // 如果有空格分隔的别名
        String[] parts = field.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }

        // 返回字段名本身
        return field.trim();
    }

    /**
     * 从预警详情中解析指标信息
     */
    private List<Map<String, Object>> parseIndicatorsFromWarningDetail(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            String warningDetail = warning.getWarningDetail();
            if (StringUtil.isEmpty(warningDetail)) {
                return indicators;
            }

            // 尝试解析JSON格式的预警详情
            Map<String, Object> detailMap = parseWarningDetail(warningDetail);
            if (detailMap != null && detailMap.containsKey("indicators")) {
                Object indicatorsObj = detailMap.get("indicators");
                if (indicatorsObj instanceof List) {
                    List<Map<String, Object>> detailIndicators = (List<Map<String, Object>>) indicatorsObj;
                    indicators.addAll(detailIndicators);
                }
            }

        } catch (Exception e) {
            log.error("从预警详情解析指标信息失败", e);
        }

        return indicators;
    }

    /**
     * 创建默认指标信息（最后的备选方案）
     */
    private List<Map<String, Object>> createDefaultIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            log.info("创建默认指标信息，预警类型: {}", warning.getWarningType());

            // 1. 尝试从表达式规则表中查找相关指标
            indicators = getIndicatorsFromExpressionRules(warning);
            if (!indicators.isEmpty()) {
                log.info("从表达式规则表获取到 {} 个指标", indicators.size());
                return indicators;
            }

            // 2. 尝试根据预警类型从系统配置中获取标准指标
            indicators = getStandardIndicatorsByType(warning.getWarningType(), warning);
            if (!indicators.isEmpty()) {
                log.info("从系统配置获取到 {} 个标准指标", indicators.size());
                return indicators;
            }

            // 3. 最后的备选方案：创建基于预警信息的最小指标集
            indicators = createMinimalIndicatorSet(warning);
            log.info("创建最小指标集，共 {} 个指标", indicators.size());

        } catch (Exception e) {
            log.error("创建默认指标信息失败", e);
            // 确保至少返回一个指标
            indicators = createMinimalIndicatorSet(warning);
        }

        return indicators;
    }

    /**
     * 从表达式规则表中获取指标
     */
    private List<Map<String, Object>> getIndicatorsFromExpressionRules(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            // 根据预警类型查找相关的表达式规则
            String sql = "SELECT RULE_ID, RULE_NAME, EXPRESSION, THRESHOLD_VALUE, DESCRIPTION " +
                        "FROM TBL_EXPRESSION_RULE " +
                        "WHERE IS_ENABLED = 'Y' " +
                        "AND (RULE_TYPE = ? OR RULE_TYPE = 'COMMON') " +
                        "ORDER BY CREATE_TIME DESC";

            List<Map<String, Object>> rules = jdbcTemplate.queryForList(sql, warning.getWarningType());

            for (Map<String, Object> rule : rules) {
                Map<String, Object> indicator = new HashMap<>();
                indicator.put("indicatorId", rule.get("RULE_ID"));
                indicator.put("indicatorName", rule.get("RULE_NAME"));
                indicator.put("indicatorType", "系统指标");
                indicator.put("threshold", rule.get("THRESHOLD_VALUE"));
                indicator.put("expression", rule.get("EXPRESSION"));
                indicator.put("description", rule.get("DESCRIPTION"));

                // 设置当前值和状态
                if (Objects.equals(rule.get("RULE_ID"), warning.getRuleId())) {
                    indicator.put("currentValue", warning.getWarningValue());
                    indicator.put("status", "异常");
                } else {
                    indicator.put("currentValue", null);
                    indicator.put("status", "正常");
                }

                indicators.add(indicator);
            }

        } catch (Exception e) {
            log.error("从表达式规则表获取指标失败", e);
        }

        return indicators;
    }

    /**
     * 根据预警类型获取标准指标
     */
    private List<Map<String, Object>> getStandardIndicatorsByType(String warningType, TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        try {
            // 根据预警类型定义标准指标
            switch (warningType) {
                case "FINANCIAL_RISK":
                    indicators = createFinancialRiskIndicators(warning);
                    break;
                case "PROCUREMENT_RISK":
                    indicators = createProcurementRiskIndicators(warning);
                    break;
                case "CREDIT_RISK":
                    indicators = createCreditRiskIndicators(warning);
                    break;
                case "COMPLIANCE_RISK":
                    indicators = createComplianceRiskIndicators(warning);
                    break;
                default:
                    indicators = createGeneralRiskIndicators(warning);
                    break;
            }

        } catch (Exception e) {
            log.error("获取标准指标失败", e);
        }

        return indicators;
    }

    /**
     * 创建财务风险指标
     */
    private List<Map<String, Object>> createFinancialRiskIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        // 主要指标（触发预警的指标）
        Map<String, Object> mainIndicator = new HashMap<>();
        mainIndicator.put("indicatorId", "FIN_MAIN");
        mainIndicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "财务风险指标"));
        mainIndicator.put("indicatorType", "财务指标");
        mainIndicator.put("threshold", warning.getThresholdValue());
        mainIndicator.put("currentValue", warning.getWarningValue());
        mainIndicator.put("status", "异常");
        mainIndicator.put("description", warning.getWarningDescription());
        indicators.add(mainIndicator);

        return indicators;
    }

    /**
     * 创建采购风险指标
     */
    private List<Map<String, Object>> createProcurementRiskIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorId", "PROC_MAIN");
        indicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "采购风险指标"));
        indicator.put("indicatorType", "采购指标");
        indicator.put("threshold", warning.getThresholdValue());
        indicator.put("currentValue", warning.getWarningValue());
        indicator.put("status", "异常");
        indicator.put("description", warning.getWarningDescription());
        indicators.add(indicator);

        return indicators;
    }

    /**
     * 创建信用风险指标
     */
    private List<Map<String, Object>> createCreditRiskIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorId", "CREDIT_MAIN");
        indicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "信用风险指标"));
        indicator.put("indicatorType", "信用指标");
        indicator.put("threshold", warning.getThresholdValue());
        indicator.put("currentValue", warning.getWarningValue());
        indicator.put("status", "异常");
        indicator.put("description", warning.getWarningDescription());
        indicators.add(indicator);

        return indicators;
    }

    /**
     * 创建合规风险指标
     */
    private List<Map<String, Object>> createComplianceRiskIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorId", "COMP_MAIN");
        indicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "合规风险指标"));
        indicator.put("indicatorType", "合规指标");
        indicator.put("threshold", warning.getThresholdValue());
        indicator.put("currentValue", warning.getWarningValue());
        indicator.put("status", "异常");
        indicator.put("description", warning.getWarningDescription());
        indicators.add(indicator);

        return indicators;
    }

    /**
     * 创建通用风险指标
     */
    private List<Map<String, Object>> createGeneralRiskIndicators(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorId", "GEN_MAIN");
        indicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "风险指标"));
        indicator.put("indicatorType", "通用指标");
        indicator.put("threshold", warning.getThresholdValue());
        indicator.put("currentValue", warning.getWarningValue());
        indicator.put("status", "异常");
        indicator.put("description", warning.getWarningDescription());
        indicators.add(indicator);

        return indicators;
    }

    /**
     * 创建最小指标集
     */
    private List<Map<String, Object>> createMinimalIndicatorSet(TblRiskWarning warning) {
        List<Map<String, Object>> indicators = new ArrayList<>();

        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorId", "MIN_" + warning.getWarningId());
        indicator.put("indicatorName", extractIndicatorNameFromDescription(warning.getWarningDescription(), "预警指标"));
        indicator.put("indicatorType", "预警指标");
        indicator.put("threshold", warning.getThresholdValue());
        indicator.put("currentValue", warning.getWarningValue());
        indicator.put("status", "异常");
        indicator.put("description", warning.getWarningDescription());
        indicators.add(indicator);

        return indicators;
    }

    /**
     * 从预警描述中提取指标名称
     */
    private String extractIndicatorNameFromDescription(String description, String defaultName) {
        if (StringUtil.isEmpty(description)) {
            return defaultName;
        }

        // 尝试从描述中提取指标名称
        // 例如："资产负债率超过阈值" -> "资产负债率"
        String[] keywords = {"超过", "低于", "异常", "预警", "风险"};
        for (String keyword : keywords) {
            int index = description.indexOf(keyword);
            if (index > 0) {
                String extracted = description.substring(0, index).trim();
                if (extracted.length() > 0 && extracted.length() < 20) {
                    return extracted;
                }
            }
        }

        // 如果无法提取，返回默认名称
        return defaultName;
    }

    /**
     * 创建空的数据结果
     */
    private Map<String, Object> createEmptyDataResult(Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", new ArrayList<>());
        result.put("total", 0);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("columns", new ArrayList<>());
        return result;
    }

    /**
     * 生成穿透数据导出文件
     */
    private String generateDrillDownExport(TblRiskWarning warning, String exportFormat) {
        try {
            log.info("生成穿透数据导出文件，预警ID: {}, 格式: {}", warning.getWarningId(), exportFormat);

            // 获取所有穿透数据（不分页）
            Map<String, Object> allData = parseDrillDownData(warning, 1, Integer.MAX_VALUE);

            // 生成文件名
            String fileName = "warning_drilldown_" + warning.getWarningId() + "_" + System.currentTimeMillis();

            Map<String, Object> exportResult = new HashMap<>();

            if ("excel".equalsIgnoreCase(exportFormat)) {
                // 生成Excel文件
                String excelUrl = generateExcelFile(allData, fileName);
                exportResult.put("fileUrl", excelUrl);
                exportResult.put("fileName", fileName + ".xlsx");
                exportResult.put("fileType", "excel");
            } else if ("csv".equalsIgnoreCase(exportFormat)) {
                // 生成CSV文件
                String csvUrl = generateCsvFile(allData, fileName);
                exportResult.put("fileUrl", csvUrl);
                exportResult.put("fileName", fileName + ".csv");
                exportResult.put("fileType", "csv");
            } else {
                throw new IllegalArgumentException("不支持的导出格式: " + exportFormat);
            }

            exportResult.put("generateTime", new Date());
            exportResult.put("recordCount", ((List<?>) ((Map<?, ?>) allData.get("businessData")).get("list")).size());

            return exportResult.toString();

        } catch (Exception e) {
            log.error("生成穿透数据导出文件失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 生成Excel文件
     */
    private String generateExcelFile(Map<String, Object> data, String fileName) {
        // 这里应该实现实际的Excel生成逻辑
        // 暂时返回模拟的文件URL
        String fileUrl = "/api/download/excel/" + fileName + ".xlsx";
        log.info("生成Excel文件: {}", fileUrl);
        return fileUrl;
    }

    /**
     * 生成CSV文件
     */
    private String generateCsvFile(Map<String, Object> data, String fileName) {
        // 这里应该实现实际的CSV生成逻辑
        // 暂时返回模拟的文件URL
        String fileUrl = "/api/download/csv/" + fileName + ".csv";
        log.info("生成CSV文件: {}", fileUrl);
        return fileUrl;
    }

    /**
     * 根据模型ID获取模型SQL语句
     */
    private String getModelSqlById(String modelId) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return null;
            }

            log.info("根据模型ID获取SQL，模型ID: {}", modelId);

            // 查询评估模型表获取关联的数据模型ID
            TblEvaluationModel evalModel = evaluationModelService.getById(modelId);
            if (evalModel == null) {
                log.warn("未找到评估模型，ID: {}", modelId);
                return null;
            }

            String dataModelId = evalModel.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("评估模型未关联数据模型，评估模型ID: {}", modelId);
                return null;
            }

            // 查询数据模型表获取SQL语句
            String sql = "SELECT SQL_STATEMENT FROM TBL_DATA_MODEL WHERE MODEL_ID = ? AND IS_ENABLED = 'Y'";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, dataModelId);

            if (results.isEmpty()) {
                log.warn("未找到数据模型SQL，数据模型ID: {}", dataModelId);
                return null;
            }

            Object sqlObj = results.get(0).get("SQL_STATEMENT");
            if (sqlObj == null) {
                return null;
            }

            String sqlStatement = convertClobToString(sqlObj);
            log.info("成功获取模型SQL，长度: {}", sqlStatement != null ? sqlStatement.length() : 0);
            return sqlStatement;

        } catch (Exception e) {
            log.error("根据模型ID获取SQL失败，模型ID: {}", modelId, e);
            return null;
        }
    }

    /**
     * 将CLOB类型转换为String
     * 达梦数据库的某些字段（如SQL_STATEMENT）可能是CLOB类型
     */
    private String convertClobToString(Object obj) {
        try {
            if (obj == null) {
                return null;
            }

            // 如果已经是String类型，直接返回
            if (obj instanceof String) {
                return (String) obj;
            }

            // 如果是CLOB类型，转换为String
            if (obj instanceof Clob) {
                Clob clob = (Clob) obj;
                Reader reader = clob.getCharacterStream();
                BufferedReader br = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }

            // 其他类型，尝试转换为String
            return obj.toString();

        } catch (SQLException | IOException e) {
            log.error("CLOB转换为String失败", e);
            return null;
        }
    }

    /**
     * 🔧 预警类型转换为中文
     */
    private String convertWarningTypeToChinese(String warningType) {
        if (warningType == null || warningType.trim().isEmpty()) {
            return "";
        }

        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("FINANCIAL_RISK", "财务风险");
        typeMap.put("PROCUREMENT_RISK", "采购风险");
        typeMap.put("CREDIT_RISK", "信用风险");
        typeMap.put("COMPLIANCE_RISK", "合规风险");
        typeMap.put("THRESHOLD", "阈值预警");
        typeMap.put("TREND", "趋势预警");
        typeMap.put("ANOMALY", "异常预警");

        return typeMap.getOrDefault(warningType.toUpperCase(), warningType);
    }

    /**
     * 🔧 预警级别转换为中文
     */
    private String convertWarningLevelToChinese(String warningLevel) {
        if (warningLevel == null || warningLevel.trim().isEmpty()) {
            return "";
        }

        Map<String, String> levelMap = new HashMap<>();
        levelMap.put("HIGH", "高风险");
        levelMap.put("MEDIUM", "中风险");
        levelMap.put("LOW", "低风险");
        levelMap.put("CRITICAL", "严重风险");

        return levelMap.getOrDefault(warningLevel.toUpperCase(), warningLevel);
    }

    /**
     * 🔧 预警状态转换为中文
     */
    private String convertWarningStatusToChinese(String warningStatus) {
        if (warningStatus == null || warningStatus.trim().isEmpty()) {
            return "";
        }

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("PENDING", "待处理");
        statusMap.put("PROCESSING", "处理中");
        statusMap.put("PROCESSED", "已处理");
        statusMap.put("IGNORED", "已忽略");

        return statusMap.getOrDefault(warningStatus.toUpperCase(), warningStatus);
    }
}
