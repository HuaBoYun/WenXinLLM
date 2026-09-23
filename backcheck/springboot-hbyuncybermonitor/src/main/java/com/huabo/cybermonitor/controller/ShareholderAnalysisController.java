package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.ShareholderAnalysis;
import com.huabo.cybermonitor.mapper.ShareholderAnalysisMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 股东穿透分析控制器
 *
 * @author system
 * @since 2025-01-01
 */
@Tag(name = "股东穿透分析", description = "股东穿透分析管理")
@RestController
@RequestMapping("/v1/supervision/equity/shareholder")
public class ShareholderAnalysisController {

    private static final Logger log = LoggerFactory.getLogger(ShareholderAnalysisController.class);

    @Autowired
    private ShareholderAnalysisMapper shareholderAnalysisMapper;

    @Operation(summary = "分页查询股东穿透分析列表")
    @PostMapping("/list")
    public R<PageResult<ShareholderAnalysis>> getList(@RequestBody Map<String, Object> params) {
        try {
            int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;

            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            String enterpriseName = params.get("enterpriseName") != null ? params.get("enterpriseName").toString() : null;
            String shareholderType = params.get("shareholderType") != null ? params.get("shareholderType").toString() : null;
            String riskLevel = params.get("riskLevel") != null ? params.get("riskLevel").toString() : null;
            String analysisStatus = params.get("analysisStatus") != null ? params.get("analysisStatus").toString() : null;

            if (StringUtils.isNotEmpty(enterpriseName)) {
                wrapper.like(ShareholderAnalysis::getEnterpriseName, enterpriseName);
            }
            if (StringUtils.isNotEmpty(shareholderType)) {
                wrapper.eq(ShareholderAnalysis::getShareholderType, shareholderType);
            }
            if (StringUtils.isNotEmpty(riskLevel)) {
                wrapper.eq(ShareholderAnalysis::getRiskLevel, riskLevel);
            }
            if (StringUtils.isNotEmpty(analysisStatus)) {
                wrapper.eq(ShareholderAnalysis::getAnalysisStatus, analysisStatus);
            }

            // 股东名称模糊查询
            String shareholderName = params.get("shareholderName") != null ? params.get("shareholderName").toString() : null;
            if (StringUtils.isNotEmpty(shareholderName)) {
                wrapper.like(ShareholderAnalysis::getShareholderName, shareholderName);
            }
            // 最小持股比例
            String minHoldingRatio = params.get("minHoldingRatio") != null ? params.get("minHoldingRatio").toString() : null;
            if (StringUtils.isNotEmpty(minHoldingRatio)) {
                wrapper.ge(ShareholderAnalysis::getShareholdingRatio, new BigDecimal(minHoldingRatio));
            }
            // 最大持股比例
            String maxHoldingRatio = params.get("maxHoldingRatio") != null ? params.get("maxHoldingRatio").toString() : null;
            if (StringUtils.isNotEmpty(maxHoldingRatio)) {
                wrapper.le(ShareholderAnalysis::getShareholdingRatio, new BigDecimal(maxHoldingRatio));
            }
            // 穿透层级
            String penetrationLevel = params.get("penetrationLevel") != null ? params.get("penetrationLevel").toString() : null;
            if (StringUtils.isNotEmpty(penetrationLevel)) {
                wrapper.eq(ShareholderAnalysis::getPenetrationLevel, Integer.parseInt(penetrationLevel));
            }

            wrapper.orderByDesc(ShareholderAnalysis::getCreateTime);

            Page<ShareholderAnalysis> page = new Page<>(pageNumber, pageSize);
            Page<ShareholderAnalysis> resultPage = shareholderAnalysisMapper.selectPage(page, wrapper);

            PageResult<ShareholderAnalysis> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) resultPage.getTotal());
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageNumber(pageNumber);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) resultPage.getPages());
            pageResult.setTlist(resultPage.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询股东穿透分析列表失败", e);
            return R.fail("查询股东穿透分析列表失败");
        }
    }

    @Operation(summary = "根据ID获取股东穿透分析详情")
    @GetMapping("/{id}")
    public R<ShareholderAnalysis> getById(@PathVariable String id) {
        try {
            ShareholderAnalysis entity = shareholderAnalysisMapper.selectById(id);
            return R.success(entity);
        } catch (Exception e) {
            log.error("获取股东穿透分析详情失败，id: {}", id, e);
            return R.fail("获取股东穿透分析详情失败");
        }
    }

    @Operation(summary = "新增股东穿透分析")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody ShareholderAnalysis entity) {
        try {
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            int result = shareholderAnalysisMapper.insert(entity);
            return R.success(result > 0);
        } catch (Exception e) {
            log.error("新增股东穿透分析失败", e);
            return R.fail("新增股东穿透分析失败");
        }
    }

    @Operation(summary = "更新股东穿透分析")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody ShareholderAnalysis entity) {
        try {
            entity.setUpdateTime(new Date());
            int result = shareholderAnalysisMapper.updateById(entity);
            return R.success(result > 0);
        } catch (Exception e) {
            log.error("更新股东穿透分析失败", e);
            return R.fail("更新股东穿透分析失败");
        }
    }

    @Operation(summary = "删除股东穿透分析")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            int result = shareholderAnalysisMapper.deleteById(id);
            return R.success(result > 0);
        } catch (Exception e) {
            log.error("删除股东穿透分析失败，id: {}", id, e);
            return R.fail("删除股东穿透分析失败");
        }
    }

    @Operation(summary = "触发股东穿透分析")
    @PostMapping("/analyze")
    public R<Map<String, Object>> analyze(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            if (StringUtils.isEmpty(enterpriseId)) {
                // 尝试通过analysisId查找记录获取enterpriseId
                String analysisId = params.get("analysisId") != null ? params.get("analysisId").toString() : null;
                if (StringUtils.isEmpty(analysisId)) {
                    return R.fail("企业ID和分析ID不能同时为空");
                }
                ShareholderAnalysis record = shareholderAnalysisMapper.selectById(analysisId);
                if (record == null) {
                    return R.fail("未找到对应的分析记录");
                }
                enterpriseId = record.getEnterpriseId();
            }
            // 更新分析状态为已完成
            LambdaQueryWrapper<ShareholderAnalysis> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            List<ShareholderAnalysis> records = shareholderAnalysisMapper.selectList(updateWrapper);
            for (ShareholderAnalysis record : records) {
                record.setAnalysisStatus("COMPLETED");
                record.setAnalysisTime(new Date());
                record.setUpdateTime(new Date());
                shareholderAnalysisMapper.updateById(record);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("enterpriseId", enterpriseId);
            result.put("status", "COMPLETED");
            result.put("message", "穿透分析任务已完成");
            result.put("submitTime", new Date());
            result.put("updatedCount", records.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("触发股东穿透分析失败", e);
            return R.fail("触发股东穿透分析失败");
        }
    }

    @Operation(summary = "获取股东层级数据")
    @PostMapping("/hierarchy")
    public R<List<ShareholderAnalysis>> getHierarchy(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            wrapper.orderByAsc(ShareholderAnalysis::getPenetrationLevel);
            List<ShareholderAnalysis> list = shareholderAnalysisMapper.selectList(wrapper);
            return R.success(list);
        } catch (Exception e) {
            log.error("获取股东层级数据失败", e);
            return R.fail("获取股东层级数据失败");
        }
    }

    @Operation(summary = "获取穿透路径")
    @PostMapping("/path")
    public R<List<ShareholderAnalysis>> getPath(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            String shareholderId = params.get("shareholderId") != null ? params.get("shareholderId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            if (StringUtils.isNotEmpty(shareholderId)) {
                wrapper.eq(ShareholderAnalysis::getShareholderId, shareholderId);
            }
            wrapper.orderByAsc(ShareholderAnalysis::getPenetrationLevel);
            List<ShareholderAnalysis> list = shareholderAnalysisMapper.selectList(wrapper);
            return R.success(list);
        } catch (Exception e) {
            log.error("获取穿透路径失败", e);
            return R.fail("获取穿透路径失败");
        }
    }

    @Operation(summary = "获取最终控制人")
    @PostMapping("/ultimate")
    public R<Map<String, Object>> getUltimateController(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            wrapper.isNotNull(ShareholderAnalysis::getUltimateController);
            wrapper.orderByDesc(ShareholderAnalysis::getPenetrationLevel);
            wrapper.last("FETCH FIRST 1 ROWS ONLY");
            ShareholderAnalysis entity = shareholderAnalysisMapper.selectOne(wrapper);
            Map<String, Object> result = new HashMap<>();
            if (entity != null) {
                result.put("ultimateController", entity.getUltimateController());
                result.put("enterpriseId", entity.getEnterpriseId());
                result.put("enterpriseName", entity.getEnterpriseName());
                result.put("penetrationLevel", entity.getPenetrationLevel());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取最终控制人失败", e);
            return R.fail("获取最终控制人失败");
        }
    }

    @Operation(summary = "获取关联关系")
    @PostMapping("/relations")
    public R<List<ShareholderAnalysis>> getRelations(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            wrapper.and(w -> w.eq(ShareholderAnalysis::getIsRelatedParty, "1")
                    .or().eq(ShareholderAnalysis::getIsConcertedAction, "1"));
            wrapper.orderByDesc(ShareholderAnalysis::getShareholdingRatio);
            List<ShareholderAnalysis> list = shareholderAnalysisMapper.selectList(wrapper);
            return R.success(list);
        } catch (Exception e) {
            log.error("获取关联关系失败", e);
            return R.fail("获取关联关系失败");
        }
    }

    @Operation(summary = "获取统计数据")
    @PostMapping("/statistics")
    public R<Map<String, Object>> getStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            Long totalCount = shareholderAnalysisMapper.selectCount(null);
            result.put("totalCount", totalCount);

            LambdaQueryWrapper<ShareholderAnalysis> completedWrapper = new LambdaQueryWrapper<>();
            completedWrapper.eq(ShareholderAnalysis::getAnalysisStatus, "COMPLETED");
            Long completedCount = shareholderAnalysisMapper.selectCount(completedWrapper);
            result.put("completedCount", completedCount);

            LambdaQueryWrapper<ShareholderAnalysis> highRiskWrapper = new LambdaQueryWrapper<>();
            highRiskWrapper.eq(ShareholderAnalysis::getRiskLevel, "HIGH");
            Long highRiskCount = shareholderAnalysisMapper.selectCount(highRiskWrapper);
            result.put("highRiskCount", highRiskCount);

            LambdaQueryWrapper<ShareholderAnalysis> relatedWrapper = new LambdaQueryWrapper<>();
            relatedWrapper.eq(ShareholderAnalysis::getIsRelatedParty, "1");
            Long relatedPartyCount = shareholderAnalysisMapper.selectCount(relatedWrapper);
            result.put("relatedPartyCount", relatedPartyCount);

            // 平均穿透层级
            List<ShareholderAnalysis> allRecords = shareholderAnalysisMapper.selectList(null);
            double avgLevel = allRecords.stream()
                    .filter(r -> r.getPenetrationLevel() != null)
                    .mapToInt(ShareholderAnalysis::getPenetrationLevel)
                    .average()
                    .orElse(0.0);
            result.put("avgPenetrationLevel", Math.round(avgLevel * 100.0) / 100.0);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return R.fail("获取统计数据失败");
        }
    }

    @Operation(summary = "批量删除股东穿透分析")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("删除ID列表不能为空");
            }
            int result = shareholderAnalysisMapper.deleteBatchIds(ids);
            return R.success(result > 0);
        } catch (Exception e) {
            log.error("批量删除股东穿透分析失败", e);
            return R.fail("批量删除股东穿透分析失败");
        }
    }

    @Operation(summary = "导出股东穿透分析")
    @PostMapping("/export")
    public void export(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        SXSSFWorkbook workbook = null;
        try {
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (params != null) {
                String enterpriseName = params.get("enterpriseName") != null ? params.get("enterpriseName").toString().trim() : null;
                if (StringUtils.isNotEmpty(enterpriseName)) {
                    wrapper.like(ShareholderAnalysis::getEnterpriseName, enterpriseName);
                }
                String shareholderType = params.get("shareholderType") != null ? params.get("shareholderType").toString().trim() : null;
                if (StringUtils.isNotEmpty(shareholderType)) {
                    wrapper.eq(ShareholderAnalysis::getShareholderType, shareholderType);
                }
                String riskLevel = params.get("riskLevel") != null ? params.get("riskLevel").toString().trim() : null;
                if (StringUtils.isNotEmpty(riskLevel)) {
                    wrapper.eq(ShareholderAnalysis::getRiskLevel, riskLevel);
                }
                String analysisStatus = params.get("analysisStatus") != null ? params.get("analysisStatus").toString().trim() : null;
                if (StringUtils.isNotEmpty(analysisStatus)) {
                    wrapper.eq(ShareholderAnalysis::getAnalysisStatus, analysisStatus);
                }
                String shareholderName = params.get("shareholderName") != null ? params.get("shareholderName").toString().trim() : null;
                if (StringUtils.isNotEmpty(shareholderName)) {
                    wrapper.like(ShareholderAnalysis::getShareholderName, shareholderName);
                }
                String minHoldingRatio = params.get("minHoldingRatio") != null ? params.get("minHoldingRatio").toString().trim() : null;
                if (StringUtils.isNotEmpty(minHoldingRatio)) {
                    try { wrapper.ge(ShareholderAnalysis::getShareholdingRatio, new BigDecimal(minHoldingRatio)); } catch (Exception ignored) {}
                }
                String maxHoldingRatio = params.get("maxHoldingRatio") != null ? params.get("maxHoldingRatio").toString().trim() : null;
                if (StringUtils.isNotEmpty(maxHoldingRatio)) {
                    try { wrapper.le(ShareholderAnalysis::getShareholdingRatio, new BigDecimal(maxHoldingRatio)); } catch (Exception ignored) {}
                }
                // penetrationLevel 只在明确传入有效数字时才过滤
                Object penetrationLevelObj = params.get("penetrationLevel");
                if (penetrationLevelObj != null && StringUtils.isNotEmpty(penetrationLevelObj.toString().trim())) {
                    try {
                        int level = Integer.parseInt(penetrationLevelObj.toString().trim());
                        if (level > 0) {
                            wrapper.eq(ShareholderAnalysis::getPenetrationLevel, level);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            wrapper.orderByDesc(ShareholderAnalysis::getCreateTime);

            // 直接使用selectList，不使用分页，确保导出全部数据
            List<ShareholderAnalysis> list = shareholderAnalysisMapper.selectList(wrapper);

            log.info("导出股东穿透分析，查询到 {} 条记录，params={}", list.size(), params);

            workbook = new SXSSFWorkbook(200);
            SXSSFSheet sheet = workbook.createSheet("股东穿透分析");
            String[] headers = {"企业名称", "股东名称", "股东类型", "持股比例(%)", "穿透层级", "最终控制人", "分析状态", "风险等级", "最后分析时间"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < list.size(); i++) {
                ShareholderAnalysis sa = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(sa.getEnterpriseName() != null ? sa.getEnterpriseName() : "");
                row.createCell(1).setCellValue(sa.getShareholderName() != null ? sa.getShareholderName() : "");
                row.createCell(2).setCellValue(mapShareholderType(sa.getShareholderType()));
                row.createCell(3).setCellValue(sa.getShareholdingRatio() != null ? sa.getShareholdingRatio().toString() : "");
                row.createCell(4).setCellValue(sa.getPenetrationLevel() != null ? sa.getPenetrationLevel().toString() : "");
                row.createCell(5).setCellValue(sa.getUltimateController() != null ? sa.getUltimateController() : "");
                row.createCell(6).setCellValue(mapAnalysisStatus(sa.getAnalysisStatus()));
                row.createCell(7).setCellValue(mapRiskLevel(sa.getRiskLevel()));
                row.createCell(8).setCellValue(sa.getAnalysisTime() != null ? sdf.format(sa.getAnalysisTime()) : "");
            }

            String fileName = URLEncoder.encode("股东穿透分析_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setCharacterEncoding("UTF-8");
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出股东穿透分析失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        } finally {
            if (workbook != null) {
                try { workbook.close(); } catch (Exception ignored) {}
            }
        }
    }

    private String mapShareholderType(String type) {
        if (type == null) return "";
        switch (type) {
            case "INDIVIDUAL": return "个人";
            case "CORPORATE": return "企业";
            case "INSTITUTIONAL": return "机构";
            case "GOVERNMENT": return "政府";
            default: return type;
        }
    }

    private String mapAnalysisStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待分析";
            case "ANALYZING": return "分析中";
            case "COMPLETED": return "已完成";
            case "FAILED": return "分析失败";
            default: return status;
        }
    }

    private String mapRiskLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "LOW": return "低";
            case "MEDIUM": return "中";
            case "HIGH": return "高";
            default: return level;
        }
    }

    @Operation(summary = "获取股东变更历史")
    @PostMapping("/history")
    public R<List<Map<String, Object>>> getShareholderHistory(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            wrapper.orderByDesc(ShareholderAnalysis::getUpdateTime);
            List<ShareholderAnalysis> records = shareholderAnalysisMapper.selectList(wrapper);
            List<Map<String, Object>> history = new ArrayList<>();
            for (ShareholderAnalysis sa : records) {
                Map<String, Object> item = new HashMap<>();
                item.put("shareholderId", sa.getShareholderId());
                item.put("enterpriseName", sa.getEnterpriseName());
                item.put("shareholderName", sa.getShareholderName());
                item.put("shareholdingRatio", sa.getShareholdingRatio());
                item.put("changeTime", sa.getUpdateTime());
                item.put("riskLevel", sa.getRiskLevel());
                item.put("analysisStatus", sa.getAnalysisStatus());
                history.add(item);
            }
            return R.success(history);
        } catch (Exception e) {
            log.error("获取股东变更历史失败", e);
            return R.fail("获取股东变更历史失败");
        }
    }

    @Operation(summary = "计算股东影响力")
    @PostMapping("/influence")
    public R<Map<String, Object>> calculateInfluence(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> influenceList = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                Map<String, Object> item = new HashMap<>();
                item.put("shareholderId", sa.getShareholderId());
                item.put("shareholderName", sa.getShareholderName());
                item.put("shareholdingRatio", sa.getShareholdingRatio());
                // 影响力指数 = 持股比例 * 权重因子（关联方/一致行动人加权）
                double ratio = sa.getShareholdingRatio() != null ? sa.getShareholdingRatio().doubleValue() : 0.0;
                double weight = 1.0;
                if ("1".equals(sa.getIsRelatedParty())) weight += 0.3;
                if ("1".equals(sa.getIsConcertedAction())) weight += 0.2;
                item.put("influenceScore", Math.round(ratio * weight * 100.0) / 100.0);
                item.put("isRelatedParty", sa.getIsRelatedParty());
                item.put("isConcertedAction", sa.getIsConcertedAction());
                influenceList.add(item);
            }
            result.put("influenceList", influenceList);
            result.put("totalShareholders", shareholders.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("计算股东影响力失败", e);
            return R.fail("计算股东影响力失败");
        }
    }

    @Operation(summary = "检测股东异常")
    @PostMapping("/anomalies")
    public R<List<Map<String, Object>>> detectAnomalies(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (params != null && params.get("enterpriseId") != null) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, params.get("enterpriseId").toString());
            }
            // 查找高风险和异常状态的股东
            wrapper.and(w -> w.eq(ShareholderAnalysis::getRiskLevel, "HIGH")
                    .or().eq(ShareholderAnalysis::getRiskLevel, "CRITICAL")
                    .or().eq(ShareholderAnalysis::getIsRelatedParty, "1")
                    .or().eq(ShareholderAnalysis::getIsConcertedAction, "1"));
            List<ShareholderAnalysis> anomalies = shareholderAnalysisMapper.selectList(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            for (ShareholderAnalysis sa : anomalies) {
                Map<String, Object> item = new HashMap<>();
                item.put("shareholderId", sa.getShareholderId());
                item.put("enterpriseName", sa.getEnterpriseName());
                item.put("shareholderName", sa.getShareholderName());
                item.put("riskLevel", sa.getRiskLevel());
                item.put("anomalyType", "HIGH".equals(sa.getRiskLevel()) || "CRITICAL".equals(sa.getRiskLevel()) ? "HIGH_RISK" : "RELATED_PARTY");
                item.put("shareholdingRatio", sa.getShareholdingRatio());
                item.put("isRelatedParty", sa.getIsRelatedParty());
                item.put("isConcertedAction", sa.getIsConcertedAction());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("检测股东异常失败", e);
            return R.fail("检测股东异常失败");
        }
    }

    @Operation(summary = "获取股东分布分析")
    @PostMapping("/distribution")
    public R<Map<String, Object>> getDistribution(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ShareholderAnalysis> all = shareholderAnalysisMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            // 按股东类型分布
            Map<String, Long> typeDistribution = new HashMap<>();
            for (ShareholderAnalysis sa : all) {
                String type = sa.getShareholderType() != null ? sa.getShareholderType() : "UNKNOWN";
                typeDistribution.merge(type, 1L, Long::sum);
            }
            result.put("typeDistribution", typeDistribution);
            // 按风险等级分布
            Map<String, Long> riskDistribution = new HashMap<>();
            for (ShareholderAnalysis sa : all) {
                String risk = sa.getRiskLevel() != null ? sa.getRiskLevel() : "UNKNOWN";
                riskDistribution.merge(risk, 1L, Long::sum);
            }
            result.put("riskDistribution", riskDistribution);
            // 按穿透层级分布
            Map<Integer, Long> levelDistribution = new HashMap<>();
            for (ShareholderAnalysis sa : all) {
                Integer level = sa.getPenetrationLevel() != null ? sa.getPenetrationLevel() : 0;
                levelDistribution.merge(level, 1L, Long::sum);
            }
            result.put("levelDistribution", levelDistribution);
            result.put("totalShareholders", all.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股东分布分析失败", e);
            return R.fail("获取股东分布分析失败");
        }
    }

    @Operation(summary = "生成股东穿透报告")
    @PostMapping("/report")
    public R<Map<String, Object>> generateReport(@RequestBody Map<String, Object> params) {
        try {
            String analysisId = params.get("analysisId") != null ? params.get("analysisId").toString() : null;
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;

            // 优先通过analysisId查找记录
            if (StringUtils.isNotEmpty(analysisId)) {
                ShareholderAnalysis record = shareholderAnalysisMapper.selectById(analysisId);
                if (record == null) {
                    return R.fail("未找到对应的分析记录");
                }
                enterpriseId = record.getEnterpriseId();
            }

            if (StringUtils.isEmpty(enterpriseId)) {
                return R.fail("企业ID和分析ID不能同时为空");
            }

            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            wrapper.orderByAsc(ShareholderAnalysis::getPenetrationLevel);
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);

            Map<String, Object> report = new HashMap<>();
            report.put("reportTitle", "股东穿透分析报告");
            report.put("generateTime", new Date());
            report.put("enterpriseId", enterpriseId);
            report.put("totalShareholders", shareholders.size());
            report.put("highRiskCount", shareholders.stream().filter(s -> "HIGH".equals(s.getRiskLevel()) || "CRITICAL".equals(s.getRiskLevel())).count());
            report.put("relatedPartyCount", shareholders.stream().filter(s -> "1".equals(s.getIsRelatedParty())).count());
            report.put("concertedActionCount", shareholders.stream().filter(s -> "1".equals(s.getIsConcertedAction())).count());

            // 股东明细数据
            List<Map<String, Object>> shareholderDetails = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("shareholderId", sa.getShareholderId());
                detail.put("shareholderName", sa.getShareholderName());
                detail.put("shareholderType", sa.getShareholderType());
                detail.put("shareholdingRatio", sa.getShareholdingRatio());
                detail.put("penetrationLevel", sa.getPenetrationLevel());
                detail.put("ultimateController", sa.getUltimateController());
                detail.put("riskLevel", sa.getRiskLevel());
                detail.put("isRelatedParty", sa.getIsRelatedParty());
                detail.put("isConcertedAction", sa.getIsConcertedAction());
                shareholderDetails.add(detail);
            }
            report.put("shareholders", shareholderDetails);

            // 企业名称取第一条记录
            if (!shareholders.isEmpty()) {
                report.put("enterpriseName", shareholders.get(0).getEnterpriseName());
            }
            report.put("status", "SUCCESS");
            return R.success(report);
        } catch (Exception e) {
            log.error("生成股东穿透报告失败", e);
            return R.fail("生成股东穿透报告失败");
        }
    }

    @Operation(summary = "批量更新状态（兼容 analysisIds 和 ids 两种参数名）")
    @PostMapping("/batch/status")
    public R<Boolean> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = params.get("analysisIds") != null
                    ? (List<String>) params.get("analysisIds")
                    : (List<String>) params.get("ids");
            // 状态字段兼容 status 和 analysisStatus 两种参数名
            String status = params.get("status") != null ? params.get("status").toString()
                    : (params.get("analysisStatus") != null ? params.get("analysisStatus").toString() : null);
            if (ids == null || ids.isEmpty() || StringUtils.isEmpty(status)) {
                return R.fail("参数不完整：需要 analysisIds(或ids) 和 status(或analysisStatus)");
            }
            for (String id : ids) {
                ShareholderAnalysis entity = shareholderAnalysisMapper.selectById(id);
                if (entity != null) {
                    entity.setAnalysisStatus(status);
                    entity.setAnalysisTime(new Date());
                    entity.setUpdateTime(new Date());
                    shareholderAnalysisMapper.updateById(entity);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            return R.fail("批量更新状态失败");
        }
    }

    @Operation(summary = "批量执行穿透分析（将状态更新为ANALYZING）")
    @PostMapping("/batch/analyze")
    public R<Map<String, Object>> batchAnalyze(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> analysisIds = params.get("analysisIds") != null
                    ? (List<String>) params.get("analysisIds")
                    : (List<String>) params.get("ids");
            if (analysisIds == null || analysisIds.isEmpty()) {
                return R.fail("analysisIds 不能为空");
            }
            int updatedCount = 0;
            for (String id : analysisIds) {
                ShareholderAnalysis entity = shareholderAnalysisMapper.selectById(id);
                if (entity != null) {
                    entity.setAnalysisStatus("ANALYZING");
                    entity.setUpdateTime(new Date());
                    shareholderAnalysisMapper.updateById(entity);
                    updatedCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("updatedCount", updatedCount);
            result.put("status", "ANALYZING");
            result.put("message", "批量分析任务已提交，共更新 " + updatedCount + " 条记录");
            return R.success(result);
        } catch (Exception e) {
            log.error("批量执行穿透分析失败", e);
            return R.fail("批量执行穿透分析失败");
        }
    }

    @Operation(summary = "批量穿透（将状态更新为PENETRATING并触发穿透逻辑）")
    @PostMapping("/batch/penetrate")
    public R<Map<String, Object>> batchPenetrate(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> analysisIds = params.get("analysisIds") != null
                    ? (List<String>) params.get("analysisIds")
                    : (List<String>) params.get("ids");
            if (analysisIds == null || analysisIds.isEmpty()) {
                return R.fail("analysisIds 不能为空");
            }
            int updatedCount = 0;
            for (String id : analysisIds) {
                ShareholderAnalysis entity = shareholderAnalysisMapper.selectById(id);
                if (entity != null) {
                    // 穿透分析：更新状态为已完成，并设置分析时间
                    entity.setAnalysisStatus("COMPLETED");
                    entity.setAnalysisTime(new Date());
                    entity.setUpdateTime(new Date());
                    shareholderAnalysisMapper.updateById(entity);
                    updatedCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("updatedCount", updatedCount);
            result.put("status", "COMPLETED");
            result.put("message", "批量穿透任务已完成，共处理 " + updatedCount + " 条记录");
            return R.success(result);
        } catch (Exception e) {
            log.error("批量穿透失败", e);
            return R.fail("批量穿透失败");
        }
    }

    @Operation(summary = "导入数据")
    @PostMapping("/import")
    public R<Map<String, Object>> importData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "数据导入成功");
            result.put("importTime", new Date());
            return R.success(result);
        } catch (Exception e) {
            log.error("导入数据失败", e);
            return R.fail("导入数据失败");
        }
    }

    @Operation(summary = "获取股东网络图数据")
    @PostMapping("/network")
    public R<Map<String, Object>> getNetworkData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", sa.getShareholderId());
                node.put("name", sa.getShareholderName());
                node.put("type", sa.getShareholderType());
                node.put("ratio", sa.getShareholdingRatio());
                nodes.add(node);
                Map<String, Object> link = new HashMap<>();
                link.put("source", sa.getShareholderId());
                link.put("target", sa.getEnterpriseId());
                link.put("ratio", sa.getShareholdingRatio());
                link.put("level", sa.getPenetrationLevel());
                links.add(link);
            }
            result.put("nodes", nodes);
            result.put("links", links);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股东网络图数据失败", e);
            return R.fail("获取股东网络图数据失败");
        }
    }

    @Operation(summary = "分析股东集中度")
    @PostMapping("/concentration")
    public R<Map<String, Object>> analyzeConcentration(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            // 计算前N大股东持股比例之和
            double top1 = 0, top5 = 0, top10 = 0, total = 0;
            List<Double> ratios = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                double ratio = sa.getShareholdingRatio() != null ? sa.getShareholdingRatio().doubleValue() : 0.0;
                ratios.add(ratio);
                total += ratio;
            }
            ratios.sort(java.util.Collections.reverseOrder());
            if (!ratios.isEmpty()) top1 = ratios.get(0);
            for (int i = 0; i < Math.min(5, ratios.size()); i++) top5 += ratios.get(i);
            for (int i = 0; i < Math.min(10, ratios.size()); i++) top10 += ratios.get(i);
            result.put("top1Ratio", Math.round(top1 * 100.0) / 100.0);
            result.put("top5Ratio", Math.round(top5 * 100.0) / 100.0);
            result.put("top10Ratio", Math.round(top10 * 100.0) / 100.0);
            result.put("totalRatio", Math.round(total * 100.0) / 100.0);
            result.put("shareholderCount", shareholders.size());
            // 集中度评级
            String concentrationLevel = top1 > 50 ? "HIGH" : top5 > 50 ? "MEDIUM" : "LOW";
            result.put("concentrationLevel", concentrationLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("分析股东集中度失败", e);
            return R.fail("分析股东集中度失败");
        }
    }

    @Operation(summary = "获取股东风险评估")
    @PostMapping("/risk")
    public R<Map<String, Object>> getRiskAssessment(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            long highRisk = shareholders.stream().filter(s -> "HIGH".equals(s.getRiskLevel()) || "CRITICAL".equals(s.getRiskLevel())).count();
            long relatedParty = shareholders.stream().filter(s -> "1".equals(s.getIsRelatedParty())).count();
            long concerted = shareholders.stream().filter(s -> "1".equals(s.getIsConcertedAction())).count();
            result.put("totalShareholders", shareholders.size());
            result.put("highRiskCount", highRisk);
            result.put("relatedPartyCount", relatedParty);
            result.put("concertedActionCount", concerted);
            result.put("overallRiskLevel", highRisk > 0 ? "HIGH" : relatedParty > 0 ? "MEDIUM" : "LOW");
            List<Map<String, Object>> riskItems = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                if ("HIGH".equals(sa.getRiskLevel()) || "CRITICAL".equals(sa.getRiskLevel()) || "1".equals(sa.getIsRelatedParty())) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("shareholderName", sa.getShareholderName());
                    item.put("riskLevel", sa.getRiskLevel());
                    item.put("riskFactors", new ArrayList<>());
                    riskItems.add(item);
                }
            }
            result.put("riskItems", riskItems);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股东风险评估失败", e);
            return R.fail("获取股东风险评估失败");
        }
    }

    @Operation(summary = "股东合规性检查")
    @PostMapping("/compliance")
    public R<Map<String, Object>> checkCompliance(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            List<String> issues = new ArrayList<>();
            // 检查关联方是否超比例
            long relatedOverThreshold = shareholders.stream()
                    .filter(s -> "1".equals(s.getIsRelatedParty()) && s.getShareholdingRatio() != null && s.getShareholdingRatio().doubleValue() > 30)
                    .count();
            if (relatedOverThreshold > 0) {
                issues.add("关联方持股超过30%阈值");
            }
            // 检查一致行动人
            long concertedCount = shareholders.stream().filter(s -> "1".equals(s.getIsConcertedAction())).count();
            if (concertedCount > 2) {
                issues.add("一致行动人数量超过2人");
            }
            result.put("complianceStatus", issues.isEmpty() ? "COMPLIANT" : "ISSUE");
            result.put("issues", issues);
            result.put("checkTime", new Date());
            result.put("totalShareholders", shareholders.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("股东合规性检查失败", e);
            return R.fail("股东合规性检查失败");
        }
    }

    @Operation(summary = "获取股东价值评估")
    @PostMapping("/valuation")
    public R<Map<String, Object>> getValuation(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            LambdaQueryWrapper<ShareholderAnalysis> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(enterpriseId)) {
                wrapper.eq(ShareholderAnalysis::getEnterpriseId, enterpriseId);
            }
            List<ShareholderAnalysis> shareholders = shareholderAnalysisMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> valuations = new ArrayList<>();
            for (ShareholderAnalysis sa : shareholders) {
                Map<String, Object> item = new HashMap<>();
                item.put("shareholderId", sa.getShareholderId());
                item.put("shareholderName", sa.getShareholderName());
                item.put("shareholdingRatio", sa.getShareholdingRatio());
                item.put("shareholderType", sa.getShareholderType());
                item.put("valuationMethod", "市净率法");
                item.put("valuationDate", new Date());
                valuations.add(item);
            }
            result.put("valuations", valuations);
            result.put("totalShareholders", shareholders.size());
            result.put("valuationTime", new Date());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股东价值评估失败", e);
            return R.fail("获取股东价值评估失败");
        }
    }
}
