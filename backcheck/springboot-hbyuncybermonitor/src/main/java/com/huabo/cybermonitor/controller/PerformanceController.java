package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.service.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业绩考核穿透式监管控制器
 */
@Tag(name = "业绩考核", description = "业绩考核指标/目标/数据/监测/预警/评价/应用")
@RestController
@RequestMapping("/v1/supervision/performance")
@Slf4j
public class PerformanceController {

    @Autowired
    private ITblStateassetsPerformanceIndexService indexService;
    @Autowired
    private ITblStateassetsPerformanceIndexVersionService indexVersionService;
    @Autowired
    private ITblStateassetsPerformanceTargetService targetService;
    @Autowired
    private ITblStateassetsPerformanceTargetVersionService targetVersionService;
    @Autowired
    private ITblStateassetsPerformanceActualService actualService;
    @Autowired
    private ITblStateassetsPerformanceDataAccessService dataAccessService;
    @Autowired
    private ITblStateassetsPerformanceSchemeService schemeService;
    @Autowired
    private ITblStateassetsPerformanceSchemeIndexService schemeIndexService;
    @Autowired
    private ITblStateassetsPerformanceWarningService warningService;
    @Autowired
    private ITblStateassetsPerformanceWarningHandleService warningHandleService;
    @Autowired
    private ITblStateassetsPerformanceEvaluationService evaluationService;
    @Autowired
    private ITblStateassetsPerformanceResultApplyService resultApplyService;
    @Autowired
    private ITblStateassetsPerformanceRectifyService rectifyService;
    @Autowired
    private ITblStateassetsPerformanceRectifyRecordService rectifyRecordService;
    @Autowired
    private ITblStateassetsPerformanceAllStaffPlanService allStaffPlanService;
    @Autowired
    private ITblStateassetsPerformanceAllStaffScoreService allStaffScoreService;
    @Autowired
    private ITblStateassetsPerformanceAllStaffResultService allStaffResultService;
    @Autowired
    private ITblStateassetsPerformanceDrillLogService drillLogService;

    // ==================== 1. 首页 ====================

    @Operation(summary = "首页核心指标")
    @PostMapping("/home/kpi")
    public R<Map<String, Object>> homeKpi(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            Map<String, Object> result = new HashMap<>();

            LambdaQueryWrapper<TblStateassetsPerformanceIndex> indexWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(orgId)) indexWrapper.eq(TblStateassetsPerformanceIndex::getOrgId, orgId);
            long totalIndexCount = indexService.count(indexWrapper);

            LambdaQueryWrapper<TblStateassetsPerformanceWarning> warningWrapper = new LambdaQueryWrapper<>();
            if (year != null) warningWrapper.eq(TblStateassetsPerformanceWarning::getYear, year);
            if (StringUtils.isNotEmpty(orgId)) warningWrapper.eq(TblStateassetsPerformanceWarning::getOrgId, orgId);
            long warningCount = warningService.count(warningWrapper);
            LambdaQueryWrapper<TblStateassetsPerformanceWarning> highRiskWrapper = warningWrapper.clone();
            long highRiskCount = warningService.count(highRiskWrapper.eq(TblStateassetsPerformanceWarning::getWarningLevel, "HIGH"));

            result.put("groupCompletionRate", calculateGroupCompletionRate(year, orgId));
            result.put("totalIndexCount", totalIndexCount);
            result.put("warningCount", warningCount);
            result.put("highRiskCount", highRiskCount);
            result.put("rank", 1);
            result.put("trendList", buildTrendList(year, orgId));
            result.put("sixAspectProgress", buildSixAspectProgress(year, orgId));
            return R.success(result);
        } catch (Exception e) {
            log.error("查询首页核心指标失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 2. 指标库 ====================

    @Operation(summary = "指标列表")
    @PostMapping("/index/list")
    public R<Map<String, Object>> indexList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceIndex> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(getString(params, "indexCode"))) {
                wrapper.like(TblStateassetsPerformanceIndex::getIndexCode, getString(params, "indexCode"));
            }
            if (StringUtils.isNotEmpty(getString(params, "indexName"))) {
                wrapper.like(TblStateassetsPerformanceIndex::getIndexName, getString(params, "indexName"));
            }
            if (StringUtils.isNotEmpty(getString(params, "sixAspect"))) {
                wrapper.eq(TblStateassetsPerformanceIndex::getSixAspect, getString(params, "sixAspect"));
            }
            if (StringUtils.isNotEmpty(getString(params, "businessType"))) {
                wrapper.eq(TblStateassetsPerformanceIndex::getBusinessType, getString(params, "businessType"));
            }
            if (StringUtils.isNotEmpty(getString(params, "dataSource"))) {
                wrapper.eq(TblStateassetsPerformanceIndex::getDataSource, getString(params, "dataSource"));
            }
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceIndex::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "status"))) {
                wrapper.eq(TblStateassetsPerformanceIndex::getStatus, getString(params, "status"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceIndex::getCreateTime);

            Page<TblStateassetsPerformanceIndex> page = new Page<>(getPageNum(params), getPageSize(params));
            Page<TblStateassetsPerformanceIndex> result = indexService.page(page, wrapper);
            return R.success(pageToMap(result));
        } catch (Exception e) {
            log.error("查询指标列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标详情")
    @GetMapping("/index/{id}")
    public R<TblStateassetsPerformanceIndex> indexDetail(@PathVariable String id) {
        try {
            return R.success(indexService.getById(id));
        } catch (Exception e) {
            log.error("查询指标详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存指标")
    @PostMapping("/index/save")
    public R<Boolean> saveIndex(@RequestBody TblStateassetsPerformanceIndex index) {
        try {
            if (StringUtils.isEmpty(index.getId())) {
                index.setCreateTime(LocalDateTime.now());
                return indexService.save(index) ? R.success(true) : R.fail("新增失败");
            } else {
                index.setUpdateTime(LocalDateTime.now());
                return indexService.updateById(index) ? R.success(true) : R.fail("更新失败");
            }
        } catch (Exception e) {
            log.error("保存指标失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除指标")
    @DeleteMapping("/index/{id}")
    public R<Boolean> deleteIndex(@PathVariable String id) {
        try {
            return indexService.removeById(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除指标失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标版本列表")
    @PostMapping("/index/versions")
    public R<Map<String, Object>> indexVersions(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceIndexVersion> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(getString(params, "indexId"))) {
                wrapper.eq(TblStateassetsPerformanceIndexVersion::getIndexId, getString(params, "indexId"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceIndexVersion::getVersion);
            Page<TblStateassetsPerformanceIndexVersion> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(indexVersionService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询指标版本列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标关联数据模型")
    @PostMapping("/index/bind-model")
    public R<Boolean> bindIndexModel(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceIndex index = indexService.getById(id);
            if (index == null) return R.fail("指标不存在");
            index.setModelId(getString(params, "modelId"));
            index.setModelCode(getString(params, "modelCode"));
            index.setModelName(getString(params, "modelName"));
            index.setUpdateTime(LocalDateTime.now());
            return indexService.updateById(index) ? R.success(true) : R.fail("关联失败");
        } catch (Exception e) {
            log.error("指标关联数据模型失败", e);
            return R.fail("关联失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标关联指标组合")
    @PostMapping("/index/bind-combination")
    public R<Boolean> bindIndexCombination(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceIndex index = indexService.getById(id);
            if (index == null) return R.fail("指标不存在");
            index.setCombinationId(getString(params, "combinationId"));
            index.setCombinationCode(getString(params, "combinationCode"));
            index.setCombinationName(getString(params, "combinationName"));
            index.setUpdateTime(LocalDateTime.now());
            return indexService.updateById(index) ? R.success(true) : R.fail("关联失败");
        } catch (Exception e) {
            log.error("指标关联指标组合失败", e);
            return R.fail("关联失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标数据模型详情")
    @PostMapping("/index/model-detail")
    public R<Map<String, Object>> indexModelDetail(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceIndex index = indexService.getById(id);
            Map<String, Object> result = new HashMap<>();
            if (index != null) {
                result.put("modelId", index.getModelId());
                result.put("modelCode", index.getModelCode());
                result.put("modelName", index.getModelName());
                result.put("sourceSystem", index.getSourceSystem());
                result.put("sourceTable", index.getSourceTable());
                result.put("sourceField", index.getSourceField());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("查询指标数据模型详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标指标组合详情")
    @PostMapping("/index/combination-detail")
    public R<Map<String, Object>> indexCombinationDetail(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceIndex index = indexService.getById(id);
            Map<String, Object> result = new HashMap<>();
            if (index != null) {
                result.put("combinationId", index.getCombinationId());
                result.put("combinationCode", index.getCombinationCode());
                result.put("combinationName", index.getCombinationName());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("查询指标组合详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "指标引用模型列表")
    @GetMapping("/index/model-list/{indexId}")
    public R<Map<String, Object>> indexModelList(@PathVariable String indexId) {
        try {
            TblStateassetsPerformanceIndex index = indexService.getById(indexId);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> modelList = new ArrayList<>();
            List<Map<String, Object>> combinationList = new ArrayList<>();
            if (index != null) {
                if (StringUtils.isNotEmpty(index.getModelId())) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("modelId", index.getModelId());
                    m.put("modelCode", index.getModelCode());
                    m.put("modelName", index.getModelName());
                    modelList.add(m);
                }
                if (StringUtils.isNotEmpty(index.getCombinationId())) {
                    Map<String, Object> c = new HashMap<>();
                    c.put("combinationId", index.getCombinationId());
                    c.put("combinationCode", index.getCombinationCode());
                    c.put("combinationName", index.getCombinationName());
                    combinationList.add(c);
                }
            }
            result.put("modelList", modelList);
            result.put("combinationList", combinationList);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询指标引用模型列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 3. 目标管理 ====================

    @Operation(summary = "目标树")
    @PostMapping("/target/tree")
    public R<Map<String, Object>> targetTree(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceTarget> wrapper = new LambdaQueryWrapper<>();
            Integer year = getInt(params, "year");
            if (year != null) wrapper.eq(TblStateassetsPerformanceTarget::getYear, year);
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceTarget::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "indexId"))) {
                wrapper.eq(TblStateassetsPerformanceTarget::getIndexId, getString(params, "indexId"));
            }
            return R.success(Collections.singletonMap("records", targetService.list(wrapper)));
        } catch (Exception e) {
            log.error("查询目标树失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存目标")
    @PostMapping("/target/save")
    public R<Boolean> saveTarget(@RequestBody TblStateassetsPerformanceTarget target) {
        try {
            if (StringUtils.isEmpty(target.getId())) {
                target.setCreateTime(LocalDateTime.now());
                return targetService.save(target) ? R.success(true) : R.fail("新增失败");
            } else {
                target.setUpdateTime(LocalDateTime.now());
                return targetService.updateById(target) ? R.success(true) : R.fail("更新失败");
            }
        } catch (Exception e) {
            log.error("保存目标失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下达目标")
    @PostMapping("/target/assign")
    public R<Boolean> assignTarget(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("目标ID不能为空");
            List<TblStateassetsPerformanceTarget> list = targetService.listByIds(ids);
            for (TblStateassetsPerformanceTarget target : list) {
                target.setLockStatus("LOCKED");
                target.setUpdateTime(LocalDateTime.now());
            }
            return targetService.updateBatchById(list) ? R.success(true) : R.fail("下达失败");
        } catch (Exception e) {
            log.error("下达目标失败", e);
            return R.fail("下达失败：" + e.getMessage());
        }
    }

    @Operation(summary = "锁定目标")
    @PostMapping("/target/lock")
    public R<Boolean> lockTarget(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            String lockStatus = getString(params, "lockStatus");
            TblStateassetsPerformanceTarget target = targetService.getById(id);
            if (target == null) return R.fail("目标不存在");
            target.setLockStatus(lockStatus);
            target.setUpdateTime(LocalDateTime.now());
            return targetService.updateById(target) ? R.success(true) : R.fail("操作失败");
        } catch (Exception e) {
            log.error("锁定目标失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "协商目标")
    @PostMapping("/target/negotiate")
    public R<Boolean> negotiateTarget(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            String status = getString(params, "status");
            TblStateassetsPerformanceTarget target = targetService.getById(id);
            if (target == null) return R.fail("目标不存在");
            target.setNegotiateStatus(status);
            target.setUpdateTime(LocalDateTime.now());
            return targetService.updateById(target) ? R.success(true) : R.fail("操作失败");
        } catch (Exception e) {
            log.error("协商目标失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 4. 数据接入看板 ====================

    @Operation(summary = "数据接入总览")
    @PostMapping("/data-access/overview")
    public R<Map<String, Object>> dataAccessOverview(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = new LambdaQueryWrapper<>();
            if (year != null) wrapper.eq(TblStateassetsPerformanceDataAccess::getYear, year);
            if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceDataAccess::getOrgId, orgId);
            long total = dataAccessService.count(wrapper);
            long accessed = dataAccessService.count(wrapper.clone().eq(TblStateassetsPerformanceDataAccess::getAccessStatus, "ACCESSED"));
            long failed = dataAccessService.count(wrapper.clone().eq(TblStateassetsPerformanceDataAccess::getAccessStatus, "FAILED"));
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("accessed", accessed);
            result.put("failed", failed);
            result.put("notAccessed", total - accessed - failed);
            result.put("accessRate", total == 0 ? 0 : Math.round(accessed * 100.0 / total * 100) / 100.0);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询数据接入总览失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据接入任务列表")
    @PostMapping("/data-access/tasks")
    public R<Map<String, Object>> dataAccessTasks(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = buildDataAccessWrapper(params);
            Page<TblStateassetsPerformanceDataAccess> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(dataAccessService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询数据接入任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据接入质量")
    @PostMapping("/data-access/quality")
    public R<Map<String, Object>> dataAccessQuality(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = buildDataAccessWrapper(params);
            List<TblStateassetsPerformanceDataAccess> list = dataAccessService.list(wrapper);
            BigDecimal totalScore = list.stream()
                    .map(d -> d.getQualityScore() != null ? d.getQualityScore() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            double avgScore = list.isEmpty() ? 0 : totalScore.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP).doubleValue();
            Map<String, Object> result = new HashMap<>();
            result.put("avgScore", avgScore);
            result.put("total", list.size());
            result.put("records", list);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询数据接入质量失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据接入来源映射")
    @PostMapping("/data-access/source-map")
    public R<Map<String, Object>> dataAccessSourceMap(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = buildDataAccessWrapper(params);
            List<TblStateassetsPerformanceDataAccess> list = dataAccessService.list(wrapper);
            Map<String, Long> sourceMap = new HashMap<>();
            for (TblStateassetsPerformanceDataAccess item : list) {
                String system = StringUtils.isNotEmpty(item.getSourceSystem()) ? item.getSourceSystem() : "未知";
                sourceMap.put(system, sourceMap.getOrDefault(system, 0L) + 1);
            }
            return R.success(Collections.singletonMap("sourceMap", sourceMap));
        } catch (Exception e) {
            log.error("查询数据接入来源映射失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据接入待办")
    @PostMapping("/data-access/todo")
    public R<Map<String, Object>> dataAccessTodo(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = buildDataAccessWrapper(params);
            wrapper.and(w -> w.eq(TblStateassetsPerformanceDataAccess::getAccessStatus, "FAILED")
                    .or().eq(TblStateassetsPerformanceDataAccess::getAccessStatus, "NOT_ACCESSED"));
            Page<TblStateassetsPerformanceDataAccess> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(dataAccessService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询数据接入待办失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 5. 过程监测 ====================

    @Operation(summary = "监测Dashboard")
    @PostMapping("/monitor/dashboard")
    public R<Map<String, Object>> monitorDashboard(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            Map<String, Object> result = new HashMap<>();
            long warningCount = warningService.count(buildWarningWrapper(year, orgId));
            result.put("completionRate", calculateGroupCompletionRate(year, orgId));
            result.put("warningCount", warningCount);
            result.put("pendingAudit", pendingAuditCount(year, orgId));
            result.put("trendList", buildTrendList(year, orgId));
            result.put("sixAspectProgress", buildSixAspectProgress(year, orgId));
            return R.success(result);
        } catch (Exception e) {
            log.error("查询监测Dashboard失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "监测列表")
    @PostMapping("/monitor/list")
    public R<Map<String, Object>> monitorList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
            Integer year = getInt(params, "year");
            if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceActual::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "indexId"))) {
                wrapper.eq(TblStateassetsPerformanceActual::getIndexId, getString(params, "indexId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "auditStatus"))) {
                wrapper.eq(TblStateassetsPerformanceActual::getAuditStatus, getString(params, "auditStatus"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceActual::getCreateTime);
            Page<TblStateassetsPerformanceActual> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(actualService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询监测列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "序时进度")
    @PostMapping("/monitor/time-progress")
    public R<Map<String, Object>> timeProgress(@RequestBody Map<String, Object> params) {
        try {
            String indexId = getString(params, "indexId");
            Integer year = getInt(params, "year");
            LambdaQueryWrapper<TblStateassetsPerformanceTarget> targetWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(indexId)) targetWrapper.eq(TblStateassetsPerformanceTarget::getIndexId, indexId);
            if (year != null) targetWrapper.eq(TblStateassetsPerformanceTarget::getYear, year);
            List<TblStateassetsPerformanceTarget> targets = targetService.list(targetWrapper);

            LambdaQueryWrapper<TblStateassetsPerformanceActual> actualWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(indexId)) actualWrapper.eq(TblStateassetsPerformanceActual::getIndexId, indexId);
            if (year != null) actualWrapper.eq(TblStateassetsPerformanceActual::getYear, year);
            List<TblStateassetsPerformanceActual> actuals = actualService.list(actualWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("targets", targets);
            result.put("actuals", actuals);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询序时进度失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "对标分析")
    @PostMapping("/monitor/benchmark")
    public R<Map<String, Object>> benchmark(@RequestBody Map<String, Object> params) {
        try {
            String indexId = getString(params, "indexId");
            Integer year = getInt(params, "year");
            LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(indexId)) wrapper.eq(TblStateassetsPerformanceActual::getIndexId, indexId);
            if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
            List<TblStateassetsPerformanceActual> list = actualService.list(wrapper);
            return R.success(Collections.singletonMap("records", list));
        } catch (Exception e) {
            log.error("查询对标分析失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 6. 风险预警 ====================

    @Operation(summary = "预警列表")
    @PostMapping("/warning/list")
    public R<Map<String, Object>> warningList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceWarning> wrapper = buildWarningWrapper(getInt(params, "year"), getString(params, "orgId"));
            if (StringUtils.isNotEmpty(getString(params, "warningLevel"))) {
                wrapper.eq(TblStateassetsPerformanceWarning::getWarningLevel, getString(params, "warningLevel"));
            }
            if (StringUtils.isNotEmpty(getString(params, "handleStatus"))) {
                wrapper.eq(TblStateassetsPerformanceWarning::getHandleStatus, getString(params, "handleStatus"));
            }
            if (StringUtils.isNotEmpty(getString(params, "warningType"))) {
                wrapper.eq(TblStateassetsPerformanceWarning::getWarningType, getString(params, "warningType"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceWarning::getCreateTime);
            Page<TblStateassetsPerformanceWarning> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(warningService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询预警列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "计算预警")
    @PostMapping("/warning/calculate")
    public R<Boolean> calculateWarning(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
            if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
            if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceActual::getOrgId, orgId);
            List<TblStateassetsPerformanceActual> actuals = actualService.list(wrapper);
            int count = 0;
            for (TblStateassetsPerformanceActual actual : actuals) {
                if (actual.getCompletionRate() != null && actual.getCompletionRate().compareTo(new BigDecimal("80")) < 0) {
                    TblStateassetsPerformanceWarning warning = new TblStateassetsPerformanceWarning();
                    warning.setWarningCode("WARN" + System.currentTimeMillis() + count);
                    warning.setIndexId(actual.getIndexId());
                    warning.setOrgId(actual.getOrgId());
                    warning.setYear(actual.getYear());
                    warning.setPeriodType(actual.getPeriodType());
                    warning.setPeriodValue(actual.getPeriodValue());
                    warning.setWarningLevel(actual.getCompletionRate().compareTo(new BigDecimal("60")) < 0 ? "HIGH" : "MEDIUM");
                    warning.setWarningType("COMPLETION_RATE_LOW");
                    warning.setActualValue(actual.getActualValue());
                    warning.setTargetValue(actual.getCumulativeValue());
                    warning.setCompletionRate(actual.getCompletionRate());
                    warning.setWarningDesc("完成率低于阈值");
                    warning.setHandleStatus("PENDING");
                    warning.setCreateTime(LocalDateTime.now());
                    warningService.save(warning);
                    count++;
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("计算预警失败", e);
            return R.fail("计算失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处置预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = getString(params, "warningId");
            TblStateassetsPerformanceWarning warning = warningService.getById(warningId);
            if (warning == null) return R.fail("预警不存在");
            warning.setHandleStatus("HANDLED");
            warning.setHandleTime(LocalDateTime.now());
            warning.setUpdateTime(LocalDateTime.now());

            TblStateassetsPerformanceWarningHandle handle = new TblStateassetsPerformanceWarningHandle();
            handle.setWarningId(warningId);
            handle.setHandleType(getString(params, "handleType"));
            handle.setHandleContent(getString(params, "handleContent"));
            handle.setHandleResult(getString(params, "handleResult"));
            handle.setHandleOpinion(getString(params, "handleOpinion"));
            handle.setHandleTime(LocalDateTime.now());
            handle.setCreateTime(LocalDateTime.now());
            warningHandleService.save(handle);
            return warningService.updateById(warning) ? R.success(true) : R.fail("处置失败");
        } catch (Exception e) {
            log.error("处置预警失败", e);
            return R.fail("处置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预警跟踪")
    @PostMapping("/warning/track")
    public R<Map<String, Object>> warningTrack(@RequestBody Map<String, Object> params) {
        try {
            String warningId = getString(params, "warningId");
            LambdaQueryWrapper<TblStateassetsPerformanceWarningHandle> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblStateassetsPerformanceWarningHandle::getWarningId, warningId);
            wrapper.orderByDesc(TblStateassetsPerformanceWarningHandle::getCreateTime);
            return R.success(Collections.singletonMap("records", warningHandleService.list(wrapper)));
        } catch (Exception e) {
            log.error("查询预警跟踪失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 7. 考核评价 ====================

    @Operation(summary = "考核方案列表")
    @PostMapping("/evaluation/schemes")
    public R<Map<String, Object>> evaluationSchemes(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceScheme> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "schemeYear") != null) {
                wrapper.eq(TblStateassetsPerformanceScheme::getSchemeYear, getInt(params, "schemeYear"));
            }
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceScheme::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "status"))) {
                wrapper.eq(TblStateassetsPerformanceScheme::getStatus, getString(params, "status"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceScheme::getCreateTime);
            Page<TblStateassetsPerformanceScheme> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(schemeService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询考核方案列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存考核方案")
    @PostMapping("/evaluation/scheme/save")
    public R<Boolean> saveEvaluationScheme(@RequestBody TblStateassetsPerformanceScheme scheme) {
        try {
            if (StringUtils.isEmpty(scheme.getId())) {
                scheme.setCreateTime(LocalDateTime.now());
                return schemeService.save(scheme) ? R.success(true) : R.fail("新增失败");
            } else {
                scheme.setUpdateTime(LocalDateTime.now());
                return schemeService.updateById(scheme) ? R.success(true) : R.fail("更新失败");
            }
        } catch (Exception e) {
            log.error("保存考核方案失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "计算得分")
    @PostMapping("/evaluation/calculate-score")
    public R<Map<String, Object>> calculateScore(@RequestBody Map<String, Object> params) {
        try {
            String schemeId = getString(params, "schemeId");
            Integer year = getInt(params, "year");
            LambdaQueryWrapper<TblStateassetsPerformanceSchemeIndex> siWrapper = new LambdaQueryWrapper<>();
            siWrapper.eq(TblStateassetsPerformanceSchemeIndex::getSchemeId, schemeId);
            List<TblStateassetsPerformanceSchemeIndex> schemeIndexes = schemeIndexService.list(siWrapper);

            LambdaQueryWrapper<TblStateassetsPerformanceEvaluation> evalWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(schemeId)) evalWrapper.eq(TblStateassetsPerformanceEvaluation::getSchemeId, schemeId);
            if (year != null) evalWrapper.eq(TblStateassetsPerformanceEvaluation::getYear, year);
            List<TblStateassetsPerformanceEvaluation> evaluations = evaluationService.list(evalWrapper);

            for (TblStateassetsPerformanceEvaluation evaluation : evaluations) {
                BigDecimal totalScore = schemeIndexes.stream()
                        .map(si -> si.getWeight() != null ? si.getWeight() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .multiply(new BigDecimal("0.95"));
                evaluation.setTotalScore(totalScore);
                evaluation.setUpdateTime(LocalDateTime.now());
                evaluationService.updateById(evaluation);
            }
            return R.success(Collections.singletonMap("count", evaluations.size()));
        } catch (Exception e) {
            log.error("计算得分失败", e);
            return R.fail("计算失败：" + e.getMessage());
        }
    }

    @Operation(summary = "考核结果")
    @PostMapping("/evaluation/result")
    public R<Map<String, Object>> evaluationResult(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceEvaluation> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceEvaluation::getYear, getInt(params, "year"));
            if (StringUtils.isNotEmpty(getString(params, "schemeId"))) {
                wrapper.eq(TblStateassetsPerformanceEvaluation::getSchemeId, getString(params, "schemeId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceEvaluation::getOrgId, getString(params, "orgId"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceEvaluation::getTotalScore);
            Page<TblStateassetsPerformanceEvaluation> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(evaluationService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询考核结果失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "确认考核结果")
    @PostMapping("/evaluation/confirm")
    public R<Boolean> confirmEvaluation(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceEvaluation evaluation = evaluationService.getById(id);
            if (evaluation == null) return R.fail("考核结果不存在");
            evaluation.setResultStatus("CONFIRMED");
            evaluation.setUpdateTime(LocalDateTime.now());
            return evaluationService.updateById(evaluation) ? R.success(true) : R.fail("确认失败");
        } catch (Exception e) {
            log.error("确认考核结果失败", e);
            return R.fail("确认失败：" + e.getMessage());
        }
    }

    @Operation(summary = "考核结果申诉")
    @PostMapping("/evaluation/appeal")
    public R<Boolean> appealEvaluation(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceEvaluation evaluation = evaluationService.getById(id);
            if (evaluation == null) return R.fail("考核结果不存在");
            evaluation.setAppealStatus("APPEALING");
            evaluation.setUpdateTime(LocalDateTime.now());
            return evaluationService.updateById(evaluation) ? R.success(true) : R.fail("申诉失败");
        } catch (Exception e) {
            log.error("考核结果申诉失败", e);
            return R.fail("申诉失败：" + e.getMessage());
        }
    }

    @Operation(summary = "考核申诉审批")
    @PostMapping("/evaluation/appeal-audit")
    public R<Boolean> auditAppeal(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            String status = getString(params, "status");
            TblStateassetsPerformanceEvaluation evaluation = evaluationService.getById(id);
            if (evaluation == null) return R.fail("考核结果不存在");
            evaluation.setAppealStatus(status);
            evaluation.setUpdateTime(LocalDateTime.now());
            return evaluationService.updateById(evaluation) ? R.success(true) : R.fail("审批失败");
        } catch (Exception e) {
            log.error("考核申诉审批失败", e);
            return R.fail("审批失败：" + e.getMessage());
        }
    }

    // ==================== 8. 结果应用 ====================

    @Operation(summary = "结果应用列表")
    @PostMapping("/result-apply/list")
    public R<Map<String, Object>> resultApplyList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceResultApply> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceResultApply::getYear, getInt(params, "year"));
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceResultApply::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "applyType"))) {
                wrapper.eq(TblStateassetsPerformanceResultApply::getApplyType, getString(params, "applyType"));
            }
            Page<TblStateassetsPerformanceResultApply> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(resultApplyService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询结果应用列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "薪酬联动计算")
    @PostMapping("/result-apply/salary-linkage")
    public R<Map<String, Object>> salaryLinkage(@RequestBody Map<String, Object> params) {
        try {
            String evaluationId = getString(params, "evaluationId");
            TblStateassetsPerformanceEvaluation evaluation = evaluationService.getById(evaluationId);
            Map<String, Object> result = new HashMap<>();
            if (evaluation != null && evaluation.getTotalScore() != null) {
                BigDecimal salaryAmount = evaluation.getTotalScore().multiply(new BigDecimal("10000"));
                BigDecimal bonusPoolAmount = evaluation.getTotalScore().multiply(new BigDecimal("50000"));
                result.put("salaryAmount", salaryAmount);
                result.put("bonusPoolAmount", bonusPoolAmount);
                result.put("orgId", evaluation.getOrgId());
                result.put("year", evaluation.getYear());
                result.put("totalScore", evaluation.getTotalScore());
                result.put("level", evaluation.getLevel());
                result.put("resultStatus", evaluation.getResultStatus());
            } else {
                result.put("salaryAmount", BigDecimal.ZERO);
                result.put("bonusPoolAmount", BigDecimal.ZERO);
                result.put("orgId", null);
                result.put("year", null);
                result.put("totalScore", null);
                result.put("level", null);
                result.put("resultStatus", null);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("薪酬联动计算失败", e);
            return R.fail("计算失败：" + e.getMessage());
        }
    }

    @Operation(summary = "生成整改任务")
    @PostMapping("/result-apply/rectify-task")
    public R<Boolean> generateRectifyTask(@RequestBody Map<String, Object> params) {
        try {
            String evaluationId = getString(params, "evaluationId");
            TblStateassetsPerformanceEvaluation evaluation = evaluationService.getById(evaluationId);
            if (evaluation == null) return R.fail("考核结果不存在");
            TblStateassetsPerformanceRectify rectify = new TblStateassetsPerformanceRectify();
            rectify.setRectifyCode("ZG" + System.currentTimeMillis());
            rectify.setSourceType("EVALUATION");
            rectify.setSourceId(evaluationId);
            rectify.setOrgId(evaluation.getOrgId());
            rectify.setYear(evaluation.getYear());
            rectify.setRectifyTitle("考核结果整改");
            rectify.setRectifyContent("针对考核结果生成整改任务");
            rectify.setStatus("PENDING");
            rectify.setCreateTime(LocalDateTime.now());
            return rectifyService.save(rectify) ? R.success(true) : R.fail("生成失败");
        } catch (Exception e) {
            log.error("生成整改任务失败", e);
            return R.fail("生成失败：" + e.getMessage());
        }
    }

    // ==================== 9. 整改闭环管理 ====================

    @Operation(summary = "整改任务列表")
    @PostMapping("/rectify/list")
    public R<Map<String, Object>> rectifyList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceRectify> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceRectify::getYear, getInt(params, "year"));
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceRectify::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "status"))) {
                wrapper.eq(TblStateassetsPerformanceRectify::getStatus, getString(params, "status"));
            }
            wrapper.orderByDesc(TblStateassetsPerformanceRectify::getCreateTime);
            Page<TblStateassetsPerformanceRectify> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(rectifyService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询整改任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建整改任务")
    @PostMapping("/rectify/create")
    public R<Boolean> createRectify(@RequestBody TblStateassetsPerformanceRectify rectify) {
        try {
            rectify.setCreateTime(LocalDateTime.now());
            if (StringUtils.isEmpty(rectify.getRectifyCode())) {
                rectify.setRectifyCode("ZG" + System.currentTimeMillis());
            }
            return rectifyService.save(rectify) ? R.success(true) : R.fail("创建失败");
        } catch (Exception e) {
            log.error("创建整改任务失败", e);
            return R.fail("创建失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存整改进度")
    @PostMapping("/rectify/progress")
    public R<Boolean> saveRectifyProgress(@RequestBody TblStateassetsPerformanceRectifyRecord record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            if (StringUtils.isEmpty(record.getRecordType())) {
                record.setRecordType("PROGRESS");
            }
            return rectifyRecordService.save(record) ? R.success(true) : R.fail("保存失败");
        } catch (Exception e) {
            log.error("保存整改进度失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "整改验收")
    @PostMapping("/rectify/audit")
    public R<Boolean> auditRectify(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            String auditResult = getString(params, "auditResult");
            TblStateassetsPerformanceRectify rectify = rectifyService.getById(id);
            if (rectify == null) return R.fail("整改任务不存在");
            rectify.setStatus("AUDITED".equals(auditResult) ? "CLOSED" : "IN_PROGRESS");
            rectify.setUpdateTime(LocalDateTime.now());

            TblStateassetsPerformanceRectifyRecord record = new TblStateassetsPerformanceRectifyRecord();
            record.setRectifyId(id);
            record.setRecordType("AUDIT");
            record.setAuditResult(auditResult);
            record.setAuditOpinion(getString(params, "auditOpinion"));
            record.setCreateTime(LocalDateTime.now());
            rectifyRecordService.save(record);
            return rectifyService.updateById(rectify) ? R.success(true) : R.fail("验收失败");
        } catch (Exception e) {
            log.error("整改验收失败", e);
            return R.fail("验收失败：" + e.getMessage());
        }
    }

    @Operation(summary = "整改统计")
    @PostMapping("/rectify/statistics")
    public R<Map<String, Object>> rectifyStatistics(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            LambdaQueryWrapper<TblStateassetsPerformanceRectify> wrapper = new LambdaQueryWrapper<>();
            if (year != null) wrapper.eq(TblStateassetsPerformanceRectify::getYear, year);
            if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceRectify::getOrgId, orgId);
            long total = rectifyService.count(wrapper);
            long pending = rectifyService.count(wrapper.clone().eq(TblStateassetsPerformanceRectify::getStatus, "PENDING"));
            long closed = rectifyService.count(wrapper.clone().eq(TblStateassetsPerformanceRectify::getStatus, "CLOSED"));
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("pending", pending);
            result.put("closed", closed);
            result.put("inProgress", total - pending - closed);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询整改统计失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 10. 全员绩效考核 ====================

    @Operation(summary = "全员绩效计划列表")
    @PostMapping("/all-staff/plan/list")
    public R<Map<String, Object>> allStaffPlanList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceAllStaffPlan> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getYear, getInt(params, "year"));
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "deptId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getDeptId, getString(params, "deptId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "userId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getUserId, getString(params, "userId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "status"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getStatus, getString(params, "status"));
            }
            Page<TblStateassetsPerformanceAllStaffPlan> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(allStaffPlanService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询全员绩效计划列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存全员绩效计划")
    @PostMapping("/all-staff/plan/save")
    public R<Boolean> saveAllStaffPlan(@RequestBody TblStateassetsPerformanceAllStaffPlan plan) {
        try {
            if (StringUtils.isEmpty(plan.getId())) {
                plan.setCreateTime(LocalDateTime.now());
                return allStaffPlanService.save(plan) ? R.success(true) : R.fail("新增失败");
            } else {
                plan.setUpdateTime(LocalDateTime.now());
                return allStaffPlanService.updateById(plan) ? R.success(true) : R.fail("更新失败");
            }
        } catch (Exception e) {
            log.error("保存全员绩效计划失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "全员绩效过程跟踪")
    @PostMapping("/all-staff/tracking")
    public R<Map<String, Object>> allStaffTracking(@RequestBody Map<String, Object> params) {
        try {
            String planId = getString(params, "planId");
            LambdaQueryWrapper<TblStateassetsPerformanceAllStaffPlan> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(planId)) wrapper.eq(TblStateassetsPerformanceAllStaffPlan::getId, planId);
            List<TblStateassetsPerformanceAllStaffPlan> plans = allStaffPlanService.list(wrapper);
            return R.success(Collections.singletonMap("records", plans));
        } catch (Exception e) {
            log.error("查询全员绩效过程跟踪失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存全员绩效评分")
    @PostMapping("/all-staff/score/save")
    public R<Boolean> saveAllStaffScore(@RequestBody TblStateassetsPerformanceAllStaffScore score) {
        try {
            score.setCreateTime(LocalDateTime.now());
            return allStaffScoreService.save(score) ? R.success(true) : R.fail("保存失败");
        } catch (Exception e) {
            log.error("保存全员绩效评分失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "全员绩效结果")
    @PostMapping("/all-staff/result")
    public R<Map<String, Object>> allStaffResult(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceAllStaffResult> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceAllStaffResult::getYear, getInt(params, "year"));
            if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffResult::getOrgId, getString(params, "orgId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "deptId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffResult::getDeptId, getString(params, "deptId"));
            }
            if (StringUtils.isNotEmpty(getString(params, "userId"))) {
                wrapper.eq(TblStateassetsPerformanceAllStaffResult::getUserId, getString(params, "userId"));
            }
            Page<TblStateassetsPerformanceAllStaffResult> page = new Page<>(getPageNum(params), getPageSize(params));
            return R.success(pageToMap(allStaffResultService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询全员绩效结果失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "全员绩效申诉")
    @PostMapping("/all-staff/appeal")
    public R<Boolean> appealAllStaff(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            TblStateassetsPerformanceAllStaffResult result = allStaffResultService.getById(id);
            if (result == null) return R.fail("绩效结果不存在");
            result.setAppealStatus("APPEALING");
            result.setUpdateTime(LocalDateTime.now());
            return allStaffResultService.updateById(result) ? R.success(true) : R.fail("申诉失败");
        } catch (Exception e) {
            log.error("全员绩效申诉失败", e);
            return R.fail("申诉失败：" + e.getMessage());
        }
    }

    @Operation(summary = "全员绩效申诉审批")
    @PostMapping("/all-staff/appeal-audit")
    public R<Boolean> auditAllStaffAppeal(@RequestBody Map<String, Object> params) {
        try {
            String id = getString(params, "id");
            String status = getString(params, "status");
            TblStateassetsPerformanceAllStaffResult result = allStaffResultService.getById(id);
            if (result == null) return R.fail("绩效结果不存在");
            result.setAppealStatus(status);
            result.setUpdateTime(LocalDateTime.now());
            return allStaffResultService.updateById(result) ? R.success(true) : R.fail("审批失败");
        } catch (Exception e) {
            log.error("全员绩效申诉审批失败", e);
            return R.fail("审批失败：" + e.getMessage());
        }
    }

    // ==================== 11. 穿透分析 ====================

    @Operation(summary = "穿透树")
    @PostMapping("/drill-down/tree")
    public R<Map<String, Object>> drillDownTree(@RequestBody Map<String, Object> params) {
        try {
            String drillType = getString(params, "drillType");
            String nodeId = getString(params, "nodeId");
            Map<String, Object> result = new HashMap<>();
            result.put("drillType", drillType);
            result.put("nodeId", nodeId);
            result.put("children", buildDrillChildren(drillType, nodeId));
            return R.success(result);
        } catch (Exception e) {
            log.error("查询穿透树失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "穿透详情")
    @PostMapping("/drill-down/detail")
    public R<Map<String, Object>> drillDownDetail(@RequestBody Map<String, Object> params) {
        try {
            String drillType = getString(params, "drillType");
            String nodeId = getString(params, "nodeId");
            Map<String, Object> result = new HashMap<>();
            result.put("drillType", drillType);
            result.put("nodeId", nodeId);
            result.put("detail", buildDrillDetail(drillType, nodeId));
            return R.success(result);
        } catch (Exception e) {
            log.error("查询穿透详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据溯源")
    @PostMapping("/drill-down/trace")
    public R<Map<String, Object>> traceDataSource(@RequestBody Map<String, Object> params) {
        try {
            String actualId = getString(params, "actualId");
            TblStateassetsPerformanceActual actual = actualService.getById(actualId);
            Map<String, Object> result = new HashMap<>();
            if (actual != null) {
                TblStateassetsPerformanceIndex index = indexService.getById(actual.getIndexId());
                result.put("sourceSystem", actual.getSourceSystem());
                if (index != null) {
                    result.put("sourceTable", index.getSourceTable());
                    result.put("sourceField", index.getSourceField());
                }
                result.put("sourceBillNo", actual.getSourceBillNo());
                result.put("sourceBillType", actual.getSourceBillType());
                result.put("taskId", actual.getTaskId());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("查询数据溯源失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 12. 考核驾驶舱 ====================

    @Operation(summary = "驾驶舱数据")
    @PostMapping("/dashboard/data")
    public R<Map<String, Object>> dashboardData(@RequestBody Map<String, Object> params) {
        try {
            Integer year = getInt(params, "year");
            String orgId = getString(params, "orgId");
            Map<String, Object> result = new HashMap<>();
            long indexCount = indexService.count();
            result.put("completionRate", calculateGroupCompletionRate(year, orgId));
            result.put("indexCount", indexCount);
            result.put("warningCount", warningService.count(buildWarningWrapper(year, orgId)));
            result.put("rectifyCount", rectifyCount(year, orgId));
            result.put("sixAspectProgress", buildSixAspectProgress(year, orgId));
            result.put("trendList", buildTrendList(year, orgId));
            return R.success(result);
        } catch (Exception e) {
            log.error("查询驾驶舱数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "驾驶舱排名")
    @PostMapping("/dashboard/ranking")
    public R<Map<String, Object>> dashboardRanking(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblStateassetsPerformanceEvaluation> wrapper = new LambdaQueryWrapper<>();
            if (getInt(params, "year") != null) wrapper.eq(TblStateassetsPerformanceEvaluation::getYear, getInt(params, "year"));
            wrapper.orderByDesc(TblStateassetsPerformanceEvaluation::getTotalScore);
            Page<TblStateassetsPerformanceEvaluation> page = new Page<>(1, 10);
            return R.success(pageToMap(evaluationService.page(page, wrapper)));
        } catch (Exception e) {
            log.error("查询驾驶舱排名失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    private String getString(Map<String, Object> params, String key) {
        if (params == null || !params.containsKey(key)) return null;
        Object value = params.get(key);
        return value == null ? null : value.toString();
    }

    private Integer getInt(Map<String, Object> params, String key) {
        if (params == null || !params.containsKey(key)) return null;
        Object value = params.get(key);
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int getPageNum(Map<String, Object> params) {
        Integer pageNum = getInt(params, "pageNum");
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    private int getPageSize(Map<String, Object> params) {
        Integer pageSize = getInt(params, "pageSize");
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }

    private Map<String, Object> pageToMap(Page<?> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        result.put("pageNum", page.getCurrent());
        result.put("pageSize", page.getSize());
        result.put("totalPage", page.getPages());
        return result;
    }

    private LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> buildDataAccessWrapper(Map<String, Object> params) {
        LambdaQueryWrapper<TblStateassetsPerformanceDataAccess> wrapper = new LambdaQueryWrapper<>();
        Integer year = getInt(params, "year");
        if (year != null) wrapper.eq(TblStateassetsPerformanceDataAccess::getYear, year);
        if (StringUtils.isNotEmpty(getString(params, "orgId"))) {
            wrapper.eq(TblStateassetsPerformanceDataAccess::getOrgId, getString(params, "orgId"));
        }
        if (StringUtils.isNotEmpty(getString(params, "accessStatus"))) {
            wrapper.eq(TblStateassetsPerformanceDataAccess::getAccessStatus, getString(params, "accessStatus"));
        }
        if (StringUtils.isNotEmpty(getString(params, "taskId"))) {
            wrapper.eq(TblStateassetsPerformanceDataAccess::getTaskId, getString(params, "taskId"));
        }
        return wrapper;
    }

    private LambdaQueryWrapper<TblStateassetsPerformanceWarning> buildWarningWrapper(Integer year, String orgId) {
        LambdaQueryWrapper<TblStateassetsPerformanceWarning> wrapper = new LambdaQueryWrapper<>();
        if (year != null) wrapper.eq(TblStateassetsPerformanceWarning::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceWarning::getOrgId, orgId);
        return wrapper;
    }

    private long rectifyCount(Integer year, String orgId) {
        LambdaQueryWrapper<TblStateassetsPerformanceRectify> wrapper = new LambdaQueryWrapper<>();
        if (year != null) wrapper.eq(TblStateassetsPerformanceRectify::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceRectify::getOrgId, orgId);
        return rectifyService.count(wrapper);
    }

    private long pendingAuditCount(Integer year, String orgId) {
        LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
        if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceActual::getOrgId, orgId);
        wrapper.eq(TblStateassetsPerformanceActual::getAuditStatus, "PENDING");
        return actualService.count(wrapper);
    }

    private BigDecimal calculateGroupCompletionRate(Integer year, String orgId) {
        LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
        if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceActual::getOrgId, orgId);
        List<TblStateassetsPerformanceActual> list = actualService.list(wrapper);
        if (list.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = list.stream()
                .map(a -> a.getCompletionRate() != null ? a.getCompletionRate() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);
    }

    private List<Map<String, Object>> buildTrendList(Integer year, String orgId) {
        List<Map<String, Object>> trendList = new ArrayList<>();
        Map<String, List<BigDecimal>> monthValues = new HashMap<>();

        LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
        if (year != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) wrapper.eq(TblStateassetsPerformanceActual::getOrgId, orgId);
        wrapper.eq(TblStateassetsPerformanceActual::getPeriodType, "MONTH");
        List<TblStateassetsPerformanceActual> actuals = actualService.list(wrapper);

        for (TblStateassetsPerformanceActual actual : actuals) {
            if (actual.getCompletionRate() == null) continue;
            String period = actual.getPeriodValue();
            if (StringUtils.isEmpty(period) || period.length() < 2) continue;
            String month = period.substring(0, 2);
            monthValues.computeIfAbsent(month, k -> new ArrayList<>()).add(actual.getCompletionRate());
        }

        for (int i = 1; i <= 12; i++) {
            String month = String.format("%02d", i);
            Map<String, Object> item = new HashMap<>();
            item.put("month", (year != null ? year + "-" : "") + month);
            List<BigDecimal> values = monthValues.getOrDefault(month, new ArrayList<>());
            if (values.isEmpty()) {
                item.put("completionRate", 0);
            } else {
                BigDecimal total = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                item.put("completionRate", total.divide(new BigDecimal(values.size()), 2, RoundingMode.HALF_UP).doubleValue());
            }
            trendList.add(item);
        }
        return trendList;
    }

    private List<Map<String, Object>> buildSixAspectProgress(Integer year, String orgId) {
        String[] aspects = {"效益效率", "创新驱动", "风险管控", "功能价值", "市场价值", "管理与发展"};
        Map<String, List<BigDecimal>> aspectValues = new HashMap<>();

        Map<String, String> aspectNameMap = new HashMap<>();
        aspectNameMap.put("BENEFIT_EFFICIENCY", "效益效率");
        aspectNameMap.put("INNOVATION", "创新驱动");
        aspectNameMap.put("RISK_CONTROL", "风险管控");
        aspectNameMap.put("FUNCTION_VALUE", "功能价值");
        aspectNameMap.put("MARKET_VALUE", "市场价值");
        aspectNameMap.put("MANAGEMENT_DEV", "管理与发展");

        LambdaQueryWrapper<TblStateassetsPerformanceActual> actualWrapper = new LambdaQueryWrapper<>();
        if (year != null) actualWrapper.eq(TblStateassetsPerformanceActual::getYear, year);
        if (StringUtils.isNotEmpty(orgId)) actualWrapper.eq(TblStateassetsPerformanceActual::getOrgId, orgId);
        List<TblStateassetsPerformanceActual> actuals = actualService.list(actualWrapper);

        Set<String> indexIds = actuals.stream().map(TblStateassetsPerformanceActual::getIndexId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        Map<String, TblStateassetsPerformanceIndex> indexMap = new HashMap<>();
        if (!indexIds.isEmpty()) {
            LambdaQueryWrapper<TblStateassetsPerformanceIndex> indexWrapper = new LambdaQueryWrapper<>();
            indexWrapper.in(TblStateassetsPerformanceIndex::getId, indexIds);
            indexService.list(indexWrapper).forEach(idx -> indexMap.put(idx.getId(), idx));
        }

        for (TblStateassetsPerformanceActual actual : actuals) {
            if (actual.getCompletionRate() == null) continue;
            TblStateassetsPerformanceIndex index = indexMap.get(actual.getIndexId());
            String rawAspect = (index != null && StringUtils.isNotEmpty(index.getSixAspect())) ? index.getSixAspect() : "其他";
            String aspect = aspectNameMap.getOrDefault(rawAspect, rawAspect);
            aspectValues.computeIfAbsent(aspect, k -> new ArrayList<>()).add(actual.getCompletionRate());
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (String aspect : aspects) {
            Map<String, Object> item = new HashMap<>();
            item.put("aspect", aspect);
            List<BigDecimal> values = aspectValues.getOrDefault(aspect, new ArrayList<>());
            if (values.isEmpty()) {
                item.put("completionRate", 0);
            } else {
                BigDecimal total = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                item.put("completionRate", total.divide(new BigDecimal(values.size()), 2, RoundingMode.HALF_UP).doubleValue());
            }
            list.add(item);
        }
        return list;
    }

    private Map<String, Object> buildDrillDetail(String drillType, String nodeId) {
        Map<String, Object> detail = new HashMap<>();
        if ("ORG".equals(drillType)) {
            TblStateassetsPerformanceTarget target = targetService.getById(nodeId);
            if (target != null) {
                detail.put("target", target);
                LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblStateassetsPerformanceActual::getOrgId, target.getOrgId());
                if (target.getYear() != null) wrapper.eq(TblStateassetsPerformanceActual::getYear, target.getYear());
                detail.put("actuals", actualService.list(wrapper));
            }
        } else if ("INDEX".equals(drillType)) {
            TblStateassetsPerformanceIndex index = indexService.getById(nodeId);
            if (index != null) {
                detail.put("index", index);
                LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblStateassetsPerformanceActual::getIndexId, index.getId());
                detail.put("actuals", actualService.list(wrapper));
            }
        } else if ("TIME".equals(drillType)) {
            LambdaQueryWrapper<TblStateassetsPerformanceActual> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(nodeId)) wrapper.eq(TblStateassetsPerformanceActual::getPeriodValue, nodeId);
            detail.put("actuals", actualService.list(wrapper));
        }
        return detail;
    }

    private List<Map<String, Object>> buildDrillChildren(String drillType, String nodeId) {
        List<Map<String, Object>> children = new ArrayList<>();
        if ("ORG".equals(drillType)) {
            LambdaQueryWrapper<TblStateassetsPerformanceTarget> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(nodeId)) wrapper.eq(TblStateassetsPerformanceTarget::getParentId, nodeId);
            List<TblStateassetsPerformanceTarget> targets = targetService.list(wrapper);
            for (TblStateassetsPerformanceTarget target : targets) {
                Map<String, Object> child = new HashMap<>();
                child.put("id", target.getId());
                child.put("name", target.getOrgId());
                child.put("type", "TARGET");
                children.add(child);
            }
        } else if ("INDEX".equals(drillType)) {
            LambdaQueryWrapper<TblStateassetsPerformanceIndex> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(nodeId)) wrapper.eq(TblStateassetsPerformanceIndex::getParentId, nodeId);
            List<TblStateassetsPerformanceIndex> indexes = indexService.list(wrapper);
            for (TblStateassetsPerformanceIndex index : indexes) {
                Map<String, Object> child = new HashMap<>();
                child.put("id", index.getId());
                child.put("name", index.getIndexName());
                child.put("code", index.getIndexCode());
                child.put("type", "INDEX");
                children.add(child);
            }
        }
        return children;
    }
}
