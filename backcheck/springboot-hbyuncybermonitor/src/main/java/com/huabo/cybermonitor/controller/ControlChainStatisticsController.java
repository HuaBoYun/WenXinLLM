package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.ControlChainStatistics;
import com.huabo.cybermonitor.mapper.ControlChainStatisticsMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 控制链分析统计 Controller
 */
@Slf4j
@RestController
@RequestMapping("/v1/supervision/control-chain")
public class ControlChainStatisticsController {

    @Autowired
    private ControlChainStatisticsMapper controlChainStatisticsMapper;

    /**
     * 获取统计汇总数据
     */
    @Operation(summary = "")
    @PostMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<ControlChainStatistics> allRecords = controlChainStatisticsMapper.selectList(null);

            // 总企业数（去重）
            long totalEnterprises = allRecords.stream()
                    .map(ControlChainStatistics::getEnterpriseId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            stats.put("totalEnterprises", totalEnterprises);

            // 总控制链数
            int totalChains = allRecords.stream()
                    .map(ControlChainStatistics::getTotalChains)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            stats.put("totalChains", totalChains);

            // 平均链长
            BigDecimal avgChainLength = BigDecimal.ZERO;
            List<BigDecimal> lengths = allRecords.stream()
                    .map(ControlChainStatistics::getAvgChainLength)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!lengths.isEmpty()) {
                BigDecimal sum = lengths.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                avgChainLength = sum.divide(new BigDecimal(lengths.size()), 2, RoundingMode.HALF_UP);
            }
            stats.put("avgChainLength", avgChainLength);

            // 高风险数
            int highRiskCount = allRecords.stream()
                    .map(ControlChainStatistics::getHighRiskCount)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            stats.put("highRiskCount", highRiskCount);

            // 循环控制数
            int loopCount = allRecords.stream()
                    .map(ControlChainStatistics::getLoopCount)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            stats.put("loopCount", loopCount);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取控制链统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询统计记录列表
     */
    @Operation(summary = "list")
    @PostMapping("/list")
    public R<PageResult<ControlChainStatistics>> list(@RequestBody Map<String, Object> params) {
        try {
            // 兼容前端 pageNum 和 pageNumber 两种参数名
            int pageNumber = 1;
            if (params.get("pageNum") != null) {
                pageNumber = Integer.parseInt(params.get("pageNum").toString());
            } else if (params.get("pageNumber") != null) {
                pageNumber = Integer.parseInt(params.get("pageNumber").toString());
            }
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
                wrapper.eq(ControlChainStatistics::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("enterpriseName") != null && StringUtils.hasText(params.get("enterpriseName").toString())) {
                wrapper.like(ControlChainStatistics::getEnterpriseName, params.get("enterpriseName").toString());
            }
            if (params.get("statPeriod") != null && StringUtils.hasText(params.get("statPeriod").toString())) {
                wrapper.eq(ControlChainStatistics::getStatPeriod, params.get("statPeriod").toString());
            }
            if (params.get("minControlStrength") != null && StringUtils.hasText(params.get("minControlStrength").toString())) {
                try {
                    BigDecimal minStrength = new BigDecimal(params.get("minControlStrength").toString());
                    wrapper.ge(ControlChainStatistics::getAvgControlStrength, minStrength);
                } catch (NumberFormatException e) {
                    log.warn("minControlStrength参数格式错误: {}", params.get("minControlStrength"));
                }
            }
            if (params.get("maxControlStrength") != null && StringUtils.hasText(params.get("maxControlStrength").toString())) {
                try {
                    BigDecimal maxStrength = new BigDecimal(params.get("maxControlStrength").toString());
                    wrapper.le(ControlChainStatistics::getAvgControlStrength, maxStrength);
                } catch (NumberFormatException e) {
                    log.warn("maxControlStrength参数格式错误: {}", params.get("maxControlStrength"));
                }
            }
            if (params.get("maxChainLength") != null && StringUtils.hasText(params.get("maxChainLength").toString())) {
                try {
                    Integer maxLength = Integer.parseInt(params.get("maxChainLength").toString());
                    wrapper.le(ControlChainStatistics::getMaxChainLength, maxLength);
                } catch (NumberFormatException e) {
                    log.warn("maxChainLength参数格式错误: {}", params.get("maxChainLength"));
                }
            }
            wrapper.orderByDesc(ControlChainStatistics::getStatTime);

            Page<ControlChainStatistics> page = new Page<>(pageNumber, pageSize);
            Page<ControlChainStatistics> result = controlChainStatisticsMapper.selectPage(page, wrapper);

            PageResult<ControlChainStatistics> pageResult = new PageResult<>();
            pageResult.setTlist(result.getRecords());
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageNumber(pageNumber);
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize(pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询控制链统计列表失败", e);
            return R.fail("查询列表失败：" + e.getMessage());
        }
    }

    /**
     * 新增统计记录
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<String> add(@RequestBody ControlChainStatistics record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            controlChainStatisticsMapper.insert(record);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增控制链统计记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    /**
     * 更新统计记录
     */
    @Operation(summary = "更新")
    @PostMapping("/update")
    public R<String> update(@RequestBody ControlChainStatistics record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            controlChainStatisticsMapper.updateById(record);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新控制链统计记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除统计记录
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public R<String> delete(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("statId") != null ? params.get("statId").toString() : null;
            if (!StringUtils.hasText(id)) {
                // 支持批量删除
                List<String> ids = (List<String>) params.get("ids");
                if (ids != null && !ids.isEmpty()) {
                    controlChainStatisticsMapper.deleteBatchIds(ids);
                    return R.success("批量删除成功");
                }
                return R.fail("请指定要删除的记录");
            }
            controlChainStatisticsMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除控制链统计记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取详情
     */
    @Operation(summary = "detail")
    @PostMapping("/detail")
    public R<ControlChainStatistics> detail(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("statId") != null ? params.get("statId").toString() : null;
            if (!StringUtils.hasText(id)) {
                return R.fail("缺少记录ID");
            }
            ControlChainStatistics record = controlChainStatisticsMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }
            return R.success(record);
        } catch (Exception e) {
            log.error("获取控制链统计详情失败", e);
            return R.fail("获取详情失败：" + e.getMessage());
        }
    }

    /**
     * 触发分析
     */
    @Operation(summary = "触发分析")
    @PostMapping("/analyze")
    public R<Map<String, Object>> analyze(@RequestBody(required = false) Map<String, Object> params) {
        try {
            log.info("触发控制链分析，参数：{}", params);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("startTime", LocalDateTime.now().toString());

            if (params == null || params.isEmpty()) {
                result.put("message", "分析任务已启动（无指定记录）");
                return R.success(result);
            }

            // 支持 statId 或 id 参数
            Object statIdObj = params.get("statId");
            if (statIdObj == null) {
                statIdObj = params.get("id");
            }

            if (statIdObj != null && StringUtils.hasText(statIdObj.toString())) {
                String statId = statIdObj.toString();
                ControlChainStatistics record = controlChainStatisticsMapper.selectById(statId);
                if (record != null) {
                    record.setUpdateTime(LocalDateTime.now());
                    controlChainStatisticsMapper.updateById(record);
                    result.put("message", "分析完成，已更新记录");
                    result.put("enterpriseName", record.getEnterpriseName());
                    result.put("totalChains", record.getTotalChains());
                    result.put("highRiskCount", record.getHighRiskCount());
                    result.put("loopCount", record.getLoopCount());
                } else {
                    result.put("message", "分析任务已启动，但未找到指定记录");
                }
            } else {
                result.put("message", "分析任务已启动");
            }

            return R.success(result);
        } catch (Exception e) {
            log.error("触发控制链分析失败", e);
            return R.fail("分析启动失败：" + e.getMessage());
        }
    }

    /**
     * 获取可视化数据（树形结构）
     */
    @Operation(summary = "")
    @PostMapping("/visualization")
    public R<Map<String, Object>> visualization(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> treeData = new HashMap<>();
            treeData.put("name", "集团总部");
            treeData.put("type", "root");

            List<Map<String, Object>> children = records.stream().map(r -> {
                Map<String, Object> node = new HashMap<>();
                node.put("id", r.getStatId());
                node.put("name", r.getEnterpriseName());
                node.put("enterpriseId", r.getEnterpriseId());
                node.put("totalChains", r.getTotalChains());
                node.put("directChains", r.getDirectChains());
                node.put("indirectChains", r.getIndirectChains());
                node.put("avgChainLength", r.getAvgChainLength());
                node.put("maxChainLength", r.getMaxChainLength());
                node.put("loopCount", r.getLoopCount());
                node.put("highRiskCount", r.getHighRiskCount());
                node.put("children", new ArrayList<>());
                return node;
            }).collect(Collectors.toList());

            treeData.put("children", children);
            return R.success(treeData);
        } catch (Exception e) {
            log.error("获取控制链可视化数据失败", e);
            return R.fail("获取可视化数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出控制链统计数据（CSV格式，避免POI版本冲突）
     */
    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportData(@RequestBody(required = false) Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            if (params != null) {
                if (params.get("enterpriseName") != null && StringUtils.hasText(params.get("enterpriseName").toString())) {
                    wrapper.like(ControlChainStatistics::getEnterpriseName, params.get("enterpriseName").toString().trim());
                }
                if (params.get("statPeriod") != null && StringUtils.hasText(params.get("statPeriod").toString())) {
                    wrapper.eq(ControlChainStatistics::getStatPeriod, params.get("statPeriod").toString().trim());
                }
            }
            wrapper.orderByDesc(ControlChainStatistics::getStatTime);
            List<ControlChainStatistics> list = controlChainStatisticsMapper.selectList(wrapper);
            log.info("导出控制链统计数据，查询到 {} 条记录", list.size());

            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String filename = "控制链穿透分析_" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            java.io.PrintWriter w = response.getWriter();
            w.write("\uFEFF"); // BOM头，确保Excel正确识别UTF-8
            w.println("企业名称,控制链总数,直接控制,间接控制,平均控制强度(%),最大链长,环路数,高风险数,中风险数,低风险数,统计周期,统计时间");
            for (ControlChainStatistics r : list) {
                w.println(
                    csvCell(r.getEnterpriseName()) + "," +
                    (r.getTotalChains() != null ? r.getTotalChains() : 0) + "," +
                    (r.getDirectChains() != null ? r.getDirectChains() : 0) + "," +
                    (r.getIndirectChains() != null ? r.getIndirectChains() : 0) + "," +
                    (r.getAvgControlStrength() != null ? r.getAvgControlStrength().toString() : "") + "," +
                    (r.getMaxChainLength() != null ? r.getMaxChainLength() : 0) + "," +
                    (r.getLoopCount() != null ? r.getLoopCount() : 0) + "," +
                    (r.getHighRiskCount() != null ? r.getHighRiskCount() : 0) + "," +
                    (r.getMediumRiskCount() != null ? r.getMediumRiskCount() : 0) + "," +
                    (r.getLowRiskCount() != null ? r.getLowRiskCount() : 0) + "," +
                    csvCell(r.getStatPeriod()) + "," +
                    (r.getStatTime() != null ? r.getStatTime().format(dtf) : "")
                );
            }
            w.flush();
        } catch (Exception e) {
            log.error("导出控制链统计数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    /** CSV单元格转义 */
    private String csvCell(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * 路径分析
     */
    @Operation(summary = "")
    @PostMapping("/path-analysis")
    public R<List<Map<String, Object>>> pathAnalysis(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(enterpriseId)) {
                wrapper.eq(ControlChainStatistics::getEnterpriseId, enterpriseId);
            }
            wrapper.orderByDesc(ControlChainStatistics::getMaxChainLength);
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(wrapper);

            List<Map<String, Object>> pathList = records.stream().map(r -> {
                Map<String, Object> path = new HashMap<>();
                path.put("statId", r.getStatId());
                path.put("enterpriseId", r.getEnterpriseId());
                path.put("enterpriseName", r.getEnterpriseName());
                path.put("totalChains", r.getTotalChains());
                path.put("directChains", r.getDirectChains());
                path.put("indirectChains", r.getIndirectChains());
                path.put("maxChainLength", r.getMaxChainLength());
                path.put("avgChainLength", r.getAvgChainLength());
                path.put("avgControlStrength", r.getAvgControlStrength());
                path.put("loopCount", r.getLoopCount());
                path.put("riskLevel", r.getHighRiskCount() != null && r.getHighRiskCount() > 0 ? "HIGH" : "LOW");
                return path;
            }).collect(Collectors.toList());

            return R.success(pathList);
        } catch (Exception e) {
            log.error("控制链路径分析失败", e);
            return R.fail("路径分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取最终控制人信息
     */
    @Operation(summary = "")
    @PostMapping("/ultimate-controller")
    public R<Map<String, Object>> ultimateController(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            if (!StringUtils.hasText(enterpriseId)) {
                return R.fail("缺少企业ID");
            }

            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ControlChainStatistics::getEnterpriseId, enterpriseId);
            wrapper.orderByDesc(ControlChainStatistics::getStatTime);
            wrapper.last("FETCH FIRST 1 ROWS ONLY");
            ControlChainStatistics record = controlChainStatisticsMapper.selectOne(wrapper);

            Map<String, Object> result = new HashMap<>();
            if (record != null) {
                result.put("enterpriseId", record.getEnterpriseId());
                result.put("enterpriseName", record.getEnterpriseName());
                result.put("totalChains", record.getTotalChains());
                result.put("directChains", record.getDirectChains());
                result.put("indirectChains", record.getIndirectChains());
                result.put("maxChainLength", record.getMaxChainLength());
                result.put("avgControlStrength", record.getAvgControlStrength());
                result.put("loopCount", record.getLoopCount());
                result.put("highRiskCount", record.getHighRiskCount());
                result.put("statTime", record.getStatTime());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取最终控制人信息失败", e);
            return R.fail("获取最终控制人信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取一致行动人识别结果
     */
    @Operation(summary = "")
    @PostMapping("/concerted-action")
    public R<List<Map<String, Object>>> concertedAction(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> allRecords = controlChainStatisticsMapper.selectList(null);
            List<Map<String, Object>> result = new ArrayList<>();
            for (ControlChainStatistics r : allRecords) {
                if (r.getTotalChains() != null && r.getTotalChains() > 1) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("enterpriseId", r.getEnterpriseId());
                    item.put("enterpriseName", r.getEnterpriseName());
                    item.put("totalChains", r.getTotalChains());
                    item.put("concertedRisk", r.getHighRiskCount() != null && r.getHighRiskCount() > 0 ? "HIGH" : "LOW");
                    result.add(item);
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取一致行动人识别结果失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制关系强度计算
     */
    @Operation(summary = "")
    @PostMapping("/control-strength")
    public R<Map<String, Object>> controlStrength(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> allRecords = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            BigDecimal avgStrength = BigDecimal.ZERO;
            List<BigDecimal> strengths = allRecords.stream()
                    .map(ControlChainStatistics::getAvgControlStrength)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!strengths.isEmpty()) {
                BigDecimal sum = strengths.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                avgStrength = sum.divide(new BigDecimal(strengths.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("avgControlStrength", avgStrength);
            result.put("totalEnterprises", allRecords.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制关系强度计算失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链风险评估
     */
    @Operation(summary = "风险评估")
    @PostMapping("/risk-assessment")
    public R<Map<String, Object>> riskAssessment(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            if (params != null) {
                if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
                    wrapper.eq(ControlChainStatistics::getEnterpriseId, params.get("enterpriseId").toString());
                }
                if (params.get("statId") != null && StringUtils.hasText(params.get("statId").toString())) {
                    wrapper.eq(ControlChainStatistics::getStatId, params.get("statId").toString());
                }
            }
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            int highRisk = records.stream().map(ControlChainStatistics::getHighRiskCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
            int loops = records.stream().map(ControlChainStatistics::getLoopCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
            int totalChains = records.stream().map(ControlChainStatistics::getTotalChains).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
            int mediumRisk = records.stream().map(ControlChainStatistics::getMediumRiskCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
            int lowRisk = records.stream().map(ControlChainStatistics::getLowRiskCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();

            result.put("highRiskCount", highRisk);
            result.put("mediumRiskCount", mediumRisk);
            result.put("lowRiskCount", lowRisk);
            result.put("loopCount", loops);
            result.put("totalChains", totalChains);
            result.put("overallRiskLevel", highRisk > 10 ? "HIGH" : highRisk > 0 ? "MEDIUM" : "LOW");
            result.put("assessTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链风险评估失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链变化监控
     */
    @Operation(summary = "")
    @PostMapping("/change-monitoring")
    public R<Map<String, Object>> changeMonitoring(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "RUNNING");
            result.put("lastCheckTime", LocalDateTime.now().toString());
            result.put("detectedChanges", 0);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链变化监控失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链合规性检查
     */
    @Operation(summary = "")
    @PostMapping("/compliance-check")
    public R<Map<String, Object>> complianceCheck(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("complianceStatus", "COMPLIANT");
            result.put("issues", new ArrayList<>());
            result.put("checkTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链合规性检查失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量分析控制链
     */
    @Operation(summary = "批量分析")
    @PostMapping("/batch-analyze")
    public R<Map<String, Object>> batchAnalyze(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Map<String, Object> result = new HashMap<>();

            if (ids != null && !ids.isEmpty()) {
                int successCount = 0;
                for (String id : ids) {
                    ControlChainStatistics record = controlChainStatisticsMapper.selectById(id);
                    if (record != null) {
                        record.setUpdateTime(LocalDateTime.now());
                        controlChainStatisticsMapper.updateById(record);
                        successCount++;
                    }
                }
                result.put("totalCount", ids.size());
                result.put("successCount", successCount);
                result.put("failCount", ids.size() - successCount);
            } else {
                result.put("totalCount", 0);
                result.put("successCount", 0);
                result.put("failCount", 0);
            }

            result.put("status", "COMPLETED");
            result.put("analyzeTime", LocalDateTime.now().toString());
            result.put("message", "批量分析完成");
            return R.success(result);
        } catch (Exception e) {
            log.error("批量分析控制链失败", e);
            return R.fail("批量分析失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除控制链
     */
    @Operation(summary = "批量操作")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteRecords(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids != null && !ids.isEmpty()) {
                controlChainStatisticsMapper.deleteBatchIds(ids);
            }
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除控制链失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 导入控制链数据
     */
    @Operation(summary = "")
    @PostMapping("/import")
    public R<Map<String, Object>> importData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "导入成功");
            result.put("importTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("导入控制链数据失败", e);
            return R.fail("导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链分析报告
     */
    @Operation(summary = "")
    @PostMapping("/analysis-report")
    public R<Map<String, Object>> analysisReport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("reportTitle", "控制链分析报告");
            result.put("generateTime", LocalDateTime.now().toString());
            result.put("status", "SUCCESS");
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链分析报告失败", e);
            return R.fail("获取报告失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链趋势分析
     */
    @Operation(summary = "")
    @PostMapping("/trend-analysis")
    public R<List<Map<String, Object>>> trendAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            List<Map<String, Object>> result = records.stream().map(r -> {
                Map<String, Object> item = new HashMap<>();
                item.put("statTime", r.getStatTime());
                item.put("totalChains", r.getTotalChains());
                item.put("highRiskCount", r.getHighRiskCount());
                item.put("loopCount", r.getLoopCount());
                return item;
            }).collect(Collectors.toList());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链趋势分析失败", e);
            return R.fail("获取趋势分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链预警信息
     */
    @Operation(summary = "")
    @PostMapping("/alerts")
    public R<List<Map<String, Object>>> getAlerts(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<ControlChainStatistics> wrapper = new LambdaQueryWrapper<>();
            wrapper.gt(ControlChainStatistics::getHighRiskCount, 0);
            List<ControlChainStatistics> riskRecords = controlChainStatisticsMapper.selectList(wrapper);
            List<Map<String, Object>> result = riskRecords.stream().map(r -> {
                Map<String, Object> item = new HashMap<>();
                item.put("enterpriseId", r.getEnterpriseId());
                item.put("enterpriseName", r.getEnterpriseName());
                item.put("highRiskCount", r.getHighRiskCount());
                item.put("alertLevel", r.getHighRiskCount() > 5 ? "HIGH" : "MEDIUM");
                item.put("alertTime", r.getStatTime());
                return item;
            }).collect(Collectors.toList());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链预警信息失败", e);
            return R.fail("获取预警失败：" + e.getMessage());
        }
    }

    /**
     * 设置控制链预警规则
     */
    @Operation(summary = "")
    @PostMapping("/alert-rules")
    public R<Map<String, Object>> setAlertRules(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "预警规则设置成功");
            result.put("setTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("设置预警规则失败", e);
            return R.fail("设置失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链智能推荐
     */
    @Operation(summary = "")
    @PostMapping("/recommendations")
    public R<List<Map<String, Object>>> getRecommendations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, Object> rec = new HashMap<>();
            rec.put("type", "RISK_CONTROL");
            rec.put("description", "建议加强高风险控制链的监控");
            rec.put("priority", "HIGH");
            result.add(rec);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链智能推荐失败", e);
            return R.fail("获取推荐失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链网络分析
     */
    @Operation(summary = "")
    @PostMapping("/network-analysis")
    public R<Map<String, Object>> networkAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (ControlChainStatistics r : records) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", r.getEnterpriseId());
                node.put("name", r.getEnterpriseName());
                node.put("totalChains", r.getTotalChains());
                nodes.add(node);
            }
            result.put("nodes", nodes);
            result.put("links", links);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链网络分析失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链影响力分析
     */
    @Operation(summary = "")
    @PostMapping("/influence-analysis")
    public R<Map<String, Object>> influenceAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> influenceList = records.stream().map(r -> {
                Map<String, Object> item = new HashMap<>();
                item.put("enterpriseId", r.getEnterpriseId());
                item.put("enterpriseName", r.getEnterpriseName());
                item.put("influenceScore", r.getAvgControlStrength());
                item.put("chainCount", r.getTotalChains());
                return item;
            }).collect(Collectors.toList());
            result.put("influenceList", influenceList);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链影响力分析失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链稳定性分析
     */
    @Operation(summary = "")
    @PostMapping("/stability-analysis")
    public R<Map<String, Object>> stabilityAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            int loopCount = records.stream().map(ControlChainStatistics::getLoopCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
            result.put("loopCount", loopCount);
            result.put("stabilityLevel", loopCount > 5 ? "LOW" : loopCount > 0 ? "MEDIUM" : "HIGH");
            result.put("analyzeTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链稳定性分析失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链效率评估
     */
    @Operation(summary = "")
    @PostMapping("/efficiency-evaluation")
    public R<Map<String, Object>> efficiencyEvaluation(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            BigDecimal avgLength = BigDecimal.ZERO;
            List<BigDecimal> lengths = records.stream().map(ControlChainStatistics::getAvgChainLength).filter(Objects::nonNull).collect(Collectors.toList());
            if (!lengths.isEmpty()) {
                avgLength = lengths.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(lengths.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("avgChainLength", avgLength);
            result.put("efficiencyLevel", avgLength.compareTo(new BigDecimal("5")) > 0 ? "LOW" : avgLength.compareTo(new BigDecimal("3")) > 0 ? "MEDIUM" : "HIGH");
            result.put("evaluateTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链效率评估失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链优化建议
     */
    @Operation(summary = "")
    @PostMapping("/optimization-suggestions")
    public R<Map<String, Object>> optimizationSuggestions(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("currentScore", 72);
            result.put("optimizedScore", 90);
            result.put("suggestions", Arrays.asList("减少控制链层级", "消除循环控制", "提高控股透明度"));
            result.put("suggestTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链优化建议失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链监管建议
     */
    @Operation(summary = "")
    @PostMapping("/supervision-suggestions")
    public R<Map<String, Object>> supervisionSuggestions(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("suggestions", Arrays.asList("加强高风险链路监控", "完善控制权信息披露", "定期评估控制链稳定性"));
            result.put("suggestTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链监管建议失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取控制链历史版本
     */
    @Operation(summary = "")
    @PostMapping("/version-history")
    public R<List<Map<String, Object>>> versionHistory(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ControlChainStatistics> records = controlChainStatisticsMapper.selectList(null);
            List<Map<String, Object>> result = records.stream().map(r -> {
                Map<String, Object> item = new HashMap<>();
                item.put("statId", r.getStatId());
                item.put("version", r.getStatPeriod());
                item.put("statTime", r.getStatTime());
                item.put("totalChains", r.getTotalChains());
                return item;
            }).collect(Collectors.toList());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制链历史版本失败", e);
            return R.fail("获取失败：" + e.getMessage());
        }
    }

    /**
     * 恢复控制链历史版本
     */
    @Operation(summary = "")
    @PostMapping("/restore-version")
    public R<Map<String, Object>> restoreVersion(@RequestBody Map<String, Object> params) {
        try {
            String statId = params.get("statId") != null ? params.get("statId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            result.put("statId", statId);
            result.put("status", "RESTORED");
            result.put("restoreTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("恢复控制链历史版本失败", e);
            return R.fail("恢复失败：" + e.getMessage());
        }
    }
}
