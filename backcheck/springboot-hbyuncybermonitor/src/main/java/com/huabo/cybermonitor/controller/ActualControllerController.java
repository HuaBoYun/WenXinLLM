package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.service.IActualControllerService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.ActualControllerQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 实际控制人控制器
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="实际控制人管理",description="实际控制人管理")
@RestController
@RequestMapping("/v1/supervision/equity/controller")
public class ActualControllerController {

	private static final Logger log = LoggerFactory.getLogger(ActualControllerController.class);

    @Autowired
    private IActualControllerService actualControllerService;

    @Operation(summary = "分页查询实际控制人列表")
    @PostMapping("/list")
    public R<PageResult<ActualController>> getActualControllerList(@RequestBody ActualControllerQueryVO queryVO) {
        try {
            PageResult<ActualController> result = actualControllerService.getActualControllerList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询实际控制人列表失败", e);
            return R.fail("查询实际控制人列表失败");
        }
    }

    @Operation(summary = "根据ID获取实际控制人详情")
    @GetMapping("/detail/{controllerId}")
    public R<ActualController> getActualControllerById(@PathVariable String controllerId) {
        try {
            ActualController controller = actualControllerService.getActualControllerById(controllerId);
            return R.success(controller);
        } catch (Exception e) {
            log.error("获取实际控制人详情失败，controllerId: {}", controllerId, e);
            return R.fail("获取实际控制人详情失败");
        }
    }

    @Operation(summary = "新增实际控制人")
    @PostMapping("/add")
    public R<Boolean> addActualController(@RequestBody ActualController actualController) {
        try {
            boolean result = actualControllerService.addActualController(actualController);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增实际控制人失败", e);
            return R.fail("新增实际控制人失败");
        }
    }

    @Operation(summary = "更新实际控制人")
    @PostMapping("/update")
    public R<Boolean> updateActualController(@RequestBody ActualController actualController) {
        try {
            boolean result = actualControllerService.updateActualController(actualController);
            return R.success(result);
        } catch (Exception e) {
            log.error("更新实际控制人失败", e);
            return R.fail("更新实际控制人失败");
        }
    }

    @Operation(summary = "删除实际控制人")
    @DeleteMapping("/delete/{controllerId}")
    public R<Boolean> deleteActualController(@PathVariable String controllerId) {
        try {
            boolean result = actualControllerService.deleteActualController(controllerId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除实际控制人失败，controllerId: {}", controllerId, e);
            return R.fail("删除实际控制人失败");
        }
    }

    @Operation(summary = "批量删除实际控制人")
    @PostMapping("/batchDelete")
    public R<Boolean> batchDeleteActualController(@RequestBody List<String> controllerIds) {
        try {
            boolean result = actualControllerService.batchDeleteActualController(controllerIds);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除实际控制人失败", e);
            return R.fail("批量删除实际控制人失败");
        }
    }

    @Operation(summary = "识别实际控制人")
    @GetMapping("/identify/{controlledEnterpriseId}")
    public R<List<ActualController>> identifyActualControllers(@PathVariable String controlledEnterpriseId) {
        try {
            List<ActualController> result = actualControllerService.identifyActualControllers(controlledEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("识别实际控制人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return R.fail("识别实际控制人失败");
        }
    }

    @Operation(summary = "查询最终控制人")
    @GetMapping("/ultimate/{controlledEnterpriseId}")
    public R<List<ActualController>> getUltimateControllers(@PathVariable String controlledEnterpriseId) {
        try {
            List<ActualController> result = actualControllerService.getUltimateControllers(controlledEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询最终控制人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return R.fail("查询最终控制人失败");
        }
    }

    @Operation(summary = "查询一致行动人")
    @GetMapping("/concerted/{controlledEnterpriseId}")
    public R<List<ActualController>> getConcertedActionControllers(@PathVariable String controlledEnterpriseId) {
        try {
            List<ActualController> result = actualControllerService.getConcertedActionControllers(controlledEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询一致行动人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return R.fail("查询一致行动人失败");
        }
    }

    @Operation(summary = "查询控制链路")
    @GetMapping("/chain")
    public R<List<ActualController>> getControlChain(@RequestParam String startEnterpriseId,
                                                    @RequestParam String endEnterpriseId) {
        try {
            List<ActualController> result = actualControllerService.getControlChain(startEnterpriseId, endEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询控制链路失败，startEnterpriseId: {}, endEnterpriseId: {}", startEnterpriseId, endEnterpriseId, e);
            return R.fail("查询控制链路失败");
        }
    }

    @Operation(summary = "查询控制层级结构")
    @GetMapping("/hierarchy/{controllerEnterpriseId}")
    public R<List<ActualController>> getControlHierarchy(@PathVariable String controllerEnterpriseId,
                                                        @RequestParam(defaultValue = "10") Integer maxLevel) {
        try {
            List<ActualController> result = actualControllerService.getControlHierarchy(controllerEnterpriseId, maxLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询控制层级结构失败，controllerEnterpriseId: {}", controllerEnterpriseId, e);
            return R.fail("查询控制层级结构失败");
        }
    }

    @Operation(summary = "控制稳定性分析")
    @GetMapping("/analysis/stability/{controllerId}")
    public R<Map<String, Object>> analyzeControlStability(@PathVariable String controllerId) {
        try {
            Map<String, Object> result = actualControllerService.analyzeControlStability(controllerId);
            return R.success(result);
        } catch (Exception e) {
            log.error("控制稳定性分析失败，controllerId: {}", controllerId, e);
            return R.fail("控制稳定性分析失败");
        }
    }

    @Operation(summary = "控制风险评估")
    @GetMapping("/assessment/risk/{controllerId}")
    public R<Map<String, Object>> assessControlRisk(@PathVariable String controllerId) {
        try {
            Map<String, Object> result = actualControllerService.assessControlRisk(controllerId);
            return R.success(result);
        } catch (Exception e) {
            log.error("控制风险评估失败，controllerId: {}", controllerId, e);
            return R.fail("控制风险评估失败");
        }
    }

    @Operation(summary = "按控制人类型统计")
    @GetMapping("/statistics/controllerType")
    public R<List<Map<String, Object>>> getControllerTypeStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControllerTypeStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制人类型统计失败", e);
            return R.fail("按控制人类型统计失败");
        }
    }

    @Operation(summary = "按控制人性质统计")
    @GetMapping("/statistics/controllerNature")
    public R<List<Map<String, Object>>> getControllerNatureStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControllerNatureStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制人性质统计失败", e);
            return R.fail("按控制人性质统计失败");
        }
    }

    @Operation(summary = "按控制方式统计")
    @GetMapping("/statistics/controlMethod")
    public R<List<Map<String, Object>>> getControlMethodStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlMethodStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制方式统计失败", e);
            return R.fail("按控制方式统计失败");
        }
    }

    @Operation(summary = "按控制状态统计")
    @GetMapping("/statistics/controlStatus")
    public R<List<Map<String, Object>>> getControlStatusStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlStatusStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制状态统计失败", e);
            return R.fail("按控制状态统计失败");
        }
    }

    @Operation(summary = "按控制稳定性统计")
    @GetMapping("/statistics/controlStability")
    public R<List<Map<String, Object>>> getControlStabilityStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlStabilityStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制稳定性统计失败", e);
            return R.fail("按控制稳定性统计失败");
        }
    }

    @Operation(summary = "按控制风险等级统计")
    @GetMapping("/statistics/controlRiskLevel")
    public R<List<Map<String, Object>>> getControlRiskLevelStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlRiskLevelStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按控制风险等级统计失败", e);
            return R.fail("按控制风险等级统计失败");
        }
    }

    @Operation(summary = "按监管关注度统计")
    @GetMapping("/statistics/regulatoryAttention")
    public R<List<Map<String, Object>>> getRegulatoryAttentionStatistics() {
        try {
            List<Map<String, Object>> result = actualControllerService.getRegulatoryAttentionStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按监管关注度统计失败", e);
            return R.fail("按监管关注度统计失败");
        }
    }

    @Operation(summary = "查询控制关系变化趋势")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> getControlRelationTrend(@RequestParam String startDate,
                                                               @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlRelationTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询控制关系变化趋势失败", e);
            return R.fail("查询控制关系变化趋势失败");
        }
    }

    @Operation(summary = "查询控制集中度分析")
    @GetMapping("/analysis/concentration")
    public R<List<Map<String, Object>>> getControlConcentrationAnalysis() {
        try {
            List<Map<String, Object>> result = actualControllerService.getControlConcentrationAnalysis();
            return R.success(result);
        } catch (Exception e) {
            log.error("查询控制集中度分析失败", e);
            return R.fail("查询控制集中度分析失败");
        }
    }

    @Operation(summary = "获取控制关系统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getControlStatisticsOverview() {
        try {
            Map<String, Object> result = actualControllerService.getControlStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制关系统计概览失败", e);
            return R.fail("获取控制关系统计概览失败");
        }
    }

    @Operation(summary = "批量更新控制状态")
    @PostMapping("/batchUpdateControlStatus")
    public R<Boolean> batchUpdateControlStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> controllerIds = (List<String>) params.get("controllerIds");
            String controlStatus = (String) params.get("controlStatus");
            boolean result = actualControllerService.batchUpdateControlStatus(controllerIds, controlStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新控制状态失败", e);
            return R.fail("批量更新控制状态失败");
        }
    }

    @Operation(summary = "批量更新确认状态")
    @PostMapping("/batchUpdateConfirmationStatus")
    public R<Boolean> batchUpdateConfirmationStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> controllerIds = (List<String>) params.get("controllerIds");
            String confirmationStatus = (String) params.get("confirmationStatus");
            String confirmedBy = (String) params.get("confirmedBy");
            boolean result = actualControllerService.batchUpdateConfirmationStatus(controllerIds, confirmationStatus, confirmedBy);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新确认状态失败", e);
            return R.fail("批量更新确认状态失败");
        }
    }

    @Operation(summary = "批量更新监管关注度")
    @PostMapping("/batchUpdateRegulatoryAttention")
    public R<Boolean> batchUpdateRegulatoryAttention(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> controllerIds = (List<String>) params.get("controllerIds");
            String regulatoryAttention = (String) params.get("regulatoryAttention");
            boolean result = actualControllerService.batchUpdateRegulatoryAttention(controllerIds, regulatoryAttention);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新监管关注度失败", e);
            return R.fail("批量更新监管关注度失败");
        }
    }

    @Operation(summary = "导出实际控制人列表")
    @PostMapping("/export")
    public R<List<Map<String, Object>>> exportActualControllerList(@RequestBody ActualControllerQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = actualControllerService.exportActualControllerList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出实际控制人列表失败", e);
            return R.fail("导出实际控制人列表失败");
        }
    }
}
