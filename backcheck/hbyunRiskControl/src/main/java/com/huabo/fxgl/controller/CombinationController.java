package com.huabo.fxgl.controller;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.service.ICombinationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 指标组合分析控制器
 * 
 * @author AI Assistant
 * @date 2025-09-27
 * @description 提供指标组合分析的完整功能，包括组合管理、指标配置、执行引擎、结果分析等
 */
@Slf4j
@RestController
@RequestMapping("/model/combination")
@Tag(name="指标组合分析管理",description="指标组合分析管理")
public class CombinationController {

    @Autowired
    private ICombinationService combinationService;

    // =====================================================
    // 1. 组合管理接口
    // =====================================================

    @PostMapping("/list")
    @Operation(summary = "获取组合列表", description = "分页查询指标组合列表，支持多条件筛选")
    public String getCombinationList(@RequestParam(required = false) Integer pageNum,
                                   @RequestParam(required = false) Integer pageSize,
                                   @RequestParam(required = false) String combinationName,
                                   @RequestParam(required = false) String combinationCode,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(required = false) String tag,
                                   @RequestParam(required = false) String modelType,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(required = false) String createUser) {
        try {
            Map<String, Object> result = combinationService.getCombinationList(
                pageNum, pageSize, combinationName, combinationCode, category, tag, modelType, status, createUser);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取组合列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{combinationId}")
    @Operation(summary = "获取组合详情", description = "根据组合ID获取完整的组合配置信息")
    public String getCombinationDetail(@PathVariable String combinationId) {
        try {
            Map<String, Object> result = combinationService.getCombinationDetail(combinationId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取组合详情失败，combinationId: {}", combinationId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail-by-code/{combinationCode}")
    @Operation(summary = "根据组合编码获取组合详情", description = "根据组合编码获取完整的组合配置信息")
    public String getCombinationDetailByCode(@PathVariable String combinationCode) {
        try {
            Map<String, Object> result = combinationService.getCombinationDetailByCode(combinationCode);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据组合编码获取组合详情失败，combinationCode: {}", combinationCode, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存组合配置", description = "新增或更新指标组合配置")
    public String saveCombination(@RequestBody String combinationData) {
        try {
            String result = combinationService.saveCombination(combinationData);
            return JsonBean.success("保存成功", result);
        } catch (Exception e) {
            log.error("保存组合配置失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{combinationId}")
    @Operation(summary = "删除组合", description = "删除指定的指标组合")
    public String deleteCombination(@PathVariable String combinationId) {
        try {
            combinationService.deleteCombination(combinationId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除组合失败，combinationId: {}", combinationId, e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/copy/{combinationId}")
    @Operation(summary = "复制组合", description = "复制现有组合创建新的组合")
    public String copyCombination(@PathVariable String combinationId,
                                @RequestParam String newCombinationName,
                                @RequestParam String newCombinationCode) {
        try {
            String result = combinationService.copyCombination(combinationId, newCombinationName, newCombinationCode);
            return JsonBean.success("复制成功", result);
        } catch (Exception e) {
            log.error("复制组合失败，combinationId: {}", combinationId, e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 2. 指标管理接口
    // =====================================================

    @PostMapping("/indicators/available")
    @Operation(summary = "获取可用指标列表", description = "获取可以添加到组合中的指标列表")
    public String getAvailableIndicators(@RequestParam(required = false) Integer pageNum,
                                       @RequestParam(required = false) Integer pageSize,
                                       @RequestParam(required = false) String indicatorName,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) String source) {
        try {
            Map<String, Object> result = combinationService.getAvailableIndicators(
                pageNum, pageSize, indicatorName, category, source);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取可用指标列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/indicators/add")
    @Operation(summary = "添加指标到组合", description = "向指定组合中添加新的指标")
    public String addIndicatorToCombination(@RequestBody String indicatorData) {
        try {
            String result = combinationService.addIndicatorToCombination(indicatorData);
            return JsonBean.success("添加成功", result);
        } catch (Exception e) {
            log.error("添加指标到组合失败", e);
            return JsonBean.error("添加失败: " + e.getMessage());
        }
    }

    @PostMapping("/indicators/remove/{configId}")
    @Operation(summary = "移除组合中的指标", description = "从组合中移除指定的指标")
    public String removeIndicatorFromCombination(@PathVariable String configId) {
        try {
            combinationService.removeIndicatorFromCombination(configId);
            return JsonBean.success("移除成功");
        } catch (Exception e) {
            log.error("移除指标失败，configId: {}", configId, e);
            return JsonBean.error("移除失败: " + e.getMessage());
        }
    }

    @PostMapping("/indicators/update")
    @Operation(summary = "更新指标配置", description = "更新组合中指标的配置信息")
    public String updateIndicatorConfig(@RequestBody String configData) {
        try {
            combinationService.updateIndicatorConfig(configData);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新指标配置失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/indicators/validate")
    @Operation(summary = "验证指标SQL", description = "验证指标SQL语法和执行可行性")
    public String validateIndicatorSql(@RequestParam String sqlContent,
                                     @RequestParam(required = false) String parameterConfig,
                                     @RequestParam(required = false) String testParameters) {
        try {
            Map<String, Object> result = combinationService.validateIndicatorSql(
                sqlContent, parameterConfig, testParameters);
            return JsonBean.success("验证完成", result);
        } catch (Exception e) {
            log.error("验证指标SQL失败", e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 3. 执行引擎接口
    // =====================================================

    @PostMapping("/execute")
    @Operation(summary = "执行组合分析", description = "启动指标组合分析执行")
    public String executeCombination(HttpServletRequest request) {
        try {
            String combinationId = null;
            String executionName = null;
            String executionMode = null;
            String description = null;
            Boolean enableCache = true;
            String parameters = null;

            // 检查Content-Type，支持两种格式
            String contentType = request.getContentType();
            log.info("请求Content-Type: {}", contentType);

            if (contentType != null && contentType.contains("application/json")) {
                // JSON格式请求
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String requestBody = sb.toString();
                    log.info("JSON请求体: {}", requestBody);

                    Map<String, Object> requestData = JSON.parseObject(requestBody, Map.class);
                    combinationId = (String) requestData.get("combinationId");
                    executionName = (String) requestData.get("executionName");
                    executionMode = (String) requestData.get("executionMode");
                    description = (String) requestData.get("description");
                    enableCache = (Boolean) requestData.getOrDefault("enableCache", true);

                    // 处理parameters参数
                    Object parametersObj = requestData.get("parameters");
                    if (parametersObj != null) {
                        if (parametersObj instanceof String) {
                            parameters = (String) parametersObj;
                        } else {
                            parameters = JSON.toJSONString(parametersObj);
                        }
                    }
                } catch (Exception e) {
                    log.error("解析JSON请求失败", e);
                    return JsonBean.error("请求格式错误: " + e.getMessage());
                }
            } else {
                // Form格式请求
                combinationId = request.getParameter("combinationId");
                executionName = request.getParameter("executionName");
                executionMode = request.getParameter("executionMode");
                description = request.getParameter("description");
                String enableCacheStr = request.getParameter("enableCache");
                if (StringUtil.isNotEmpty(enableCacheStr)) {
                    enableCache = Boolean.parseBoolean(enableCacheStr);
                }
                parameters = request.getParameter("parameters");

                log.info("Form请求参数 - combinationId: {}, executionName: {}, executionMode: {}, parameters: {}",
                        combinationId, executionName, executionMode, parameters);
            }

            // 参数验证
            if (StringUtil.isEmpty(combinationId)) {
                return JsonBean.error("组合ID不能为空");
            }

            Map<String, Object> result = combinationService.executeCombination(
                combinationId, executionName, executionMode, parameters, enableCache);
            return JsonBean.success("执行启动成功", result);
        } catch (Exception e) {
            log.error("执行组合分析失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/executeIndicator")
    @Operation(summary = "执行单个指标", description = "执行单个指标并创建临时表供后续引用")
    public String executeIndicator(@RequestBody Map<String, Object> requestData) {
        try {
            // 从请求体中提取参数
            String configId = (String) requestData.get("configId");
            String dataSourceId = (String) requestData.get("dataSourceId");
            String sqlContent = (String) requestData.get("sqlContent");
            Map<String, Object> parameters = (Map<String, Object>) requestData.get("parameters");

            // 参数验证
            if (StringUtil.isEmpty(configId)) {
                return JsonBean.error("指标配置ID不能为空");
            }
            if (StringUtil.isEmpty(dataSourceId)) {
                return JsonBean.error("数据源ID不能为空");
            }
            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            Map<String, Object> result = combinationService.executeIndicatorWithTempTable(
                configId, dataSourceId, sqlContent, parameters);
            return JsonBean.success("指标执行成功", result);
        } catch (Exception e) {
            log.error("执行单个指标失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/status/{executionId}")
    @Operation(summary = "获取执行状态", description = "获取组合分析的执行状态和进度")
    public String getExecutionStatus(@PathVariable String executionId) {
        try {
            Map<String, Object> result = combinationService.getExecutionStatus(executionId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取执行状态失败，executionId: {}", executionId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/delete")
    @Operation(summary = "删除执行记录", description = "删除指定的执行记录及其相关结果数据")
    public String deleteExecutionRecord(@RequestBody Map<String, Object> request) {
        try {
            String executionId = (String) request.get("executionId");
            Boolean forceDelete = (Boolean) request.get("forceDelete");

            if (executionId == null || executionId.trim().isEmpty()) {
                return JsonBean.error("执行ID不能为空");
            }

            log.info("开始删除执行记录, executionId: {}, forceDelete: {}", executionId, forceDelete);

            // 调用Service删除执行记录
            if (forceDelete != null && forceDelete) {
                combinationService.deleteExecutionRecord(executionId, true);
            } else {
                combinationService.deleteExecutionRecord(executionId);
            }

            log.info("删除执行记录完成, executionId: {}", executionId);

            return JsonBean.success("删除成功");

        } catch (Exception e) {
            log.error("删除执行记录失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/fix-dirty-data")
    @Operation(summary = "修复脏数据", description = "修复执行历史中的脏数据，包括空名称、状态不一致等问题")
    public String fixDirtyData() {
        try {
            log.info("开始修复执行历史脏数据");

            Map<String, Object> result = combinationService.fixDirtyData();

            log.info("修复脏数据完成, 结果: {}", result);

            return JsonBean.success("修复完成", result);

        } catch (Exception e) {
            log.error("修复脏数据失败", e);
            return JsonBean.error("修复失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/result/{executionId}")
    @Operation(summary = "获取执行结果", description = "获取组合分析的完整执行结果")
    public String getExecutionResult(@PathVariable String executionId) {
        try {
            Map<String, Object> result = combinationService.getExecutionResult(executionId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取执行结果失败，executionId: {}", executionId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/cancel/{executionId}")
    @Operation(summary = "取消执行", description = "取消正在执行的组合分析")
    public String cancelExecution(@PathVariable String executionId) {
        try {
            combinationService.cancelExecution(executionId);
            return JsonBean.success("取消成功");
        } catch (Exception e) {
            log.error("取消执行失败，executionId: {}", executionId, e);
            return JsonBean.error("取消失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/history")
    @Operation(summary = "获取执行历史", description = "获取组合分析的执行历史记录")
    public String getExecutionHistory(@RequestBody String historyData) {
        try {
            log.info("获取执行历史，接收到的数据: {}", historyData);

            JSONObject data = JSON.parseObject(historyData);
            Integer pageNum = data.getInteger("pageNum");
            Integer pageSize = data.getInteger("pageSize");
            String combinationId = data.getString("combinationId");
            String status = data.getString("status");
            String executeUser = data.getString("executeUser");
            String executionName = data.getString("executionName");
            String startDate = data.getString("startDate");
            String endDate = data.getString("endDate");

            log.info("解析查询参数: pageNum={}, pageSize={}, combinationId={}, status={}, executeUser={}, executionName={}, startDate={}, endDate={}",
                     pageNum, pageSize, combinationId, status, executeUser, executionName, startDate, endDate);

            Map<String, Object> result = combinationService.getExecutionHistory(
                pageNum, pageSize, combinationId, status, executeUser, executionName, startDate, endDate);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取执行历史失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 4. 结果分析接口
    // =====================================================

    @PostMapping("/result/detail/{resultId}")
    @Operation(summary = "获取指标结果详情", description = "获取单个指标的详细执行结果")
    public String getIndicatorResultDetail(@PathVariable String resultId,
                                         @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        try {
            Map<String, Object> result = combinationService.getIndicatorResultDetail(resultId, pageNum, pageSize);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取指标结果详情失败，resultId: {}", resultId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/analysis/intersection")
    @Operation(summary = "执行交集分析", description = "对多个指标结果进行交集分析")
    public String executeIntersectionAnalysis(@RequestBody String analysisData) {
        try {
            Map<String, Object> result = combinationService.executeIntersectionAnalysis(analysisData);
            return JsonBean.success("分析完成", result);
        } catch (Exception e) {
            log.error("执行交集分析失败", e);
            return JsonBean.error("分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/result/export")
    @Operation(summary = "导出分析结果", description = "导出组合分析结果到Excel文件")
    public String exportAnalysisResult(@RequestBody String exportData) {
        try {
            Map<String, Object> result = combinationService.exportAnalysisResult(exportData);
            return JsonBean.success("导出成功", result);
        } catch (Exception e) {
            log.error("导出分析结果失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @GetMapping("/download/{exportId}")
    @Operation(summary = "下载导出文件", description = "根据导出ID下载生成的文件")
    public void downloadExportFile(@PathVariable String exportId, HttpServletResponse response) {
        try {
            combinationService.downloadExportFile(exportId, response);
        } catch (Exception e) {
            log.error("下载文件失败，exportId: {}", exportId, e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("下载失败: " + e.getMessage());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    // =====================================================
    // 5. 流程图接口
    // =====================================================

    @PostMapping("/flow/{combinationId}")
    @Operation(summary = "获取流程图配置", description = "获取组合的流程图配置信息")
    public String getCombinationFlow(@PathVariable String combinationId) {
        try {
            Map<String, Object> result = combinationService.getCombinationFlow(combinationId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取流程图配置失败，combinationId: {}", combinationId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/node/result/{indicatorCode}")
    @Operation(summary = "获取指标执行结果", description = "根据指标编码获取指标的执行结果")
    public String getNodeExecutionResult(@PathVariable String indicatorCode,
                                       @RequestBody String requestData) {
        try {
            // 解析请求数据
            JSONObject jsonParams = JSONObject.parseObject(requestData);

            String executionId = jsonParams.getString("executionId");
            String combinationId = jsonParams.getString("combinationId");
            Integer pageNum = jsonParams.getInteger("pageNum");
            Integer pageSize = jsonParams.getInteger("pageSize");

            // 设置默认值
            pageNum = pageNum != null ? pageNum : 1;
            pageSize = pageSize != null ? pageSize : 20;

            log.info("获取指标执行结果，indicatorCode: {}, combinationId: {}, executionId: {}, pageNum: {}, pageSize: {}",
                    indicatorCode, combinationId, executionId, pageNum, pageSize);

            // 🔥 新增：检查是否是特殊节点（汇聚节点等）
            if (isSpecialNode(indicatorCode)) {
                return handleSpecialNodeResult(indicatorCode, combinationId, executionId, pageNum, pageSize);
            }

            // 🔥 关键修复：使用指标编码而不是节点ID
            Map<String, Object> result = combinationService.getIndicatorExecutionResult(indicatorCode, combinationId, executionId, pageNum, pageSize);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取指标执行结果失败，indicatorCode: {}", indicatorCode, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 🔥 新增：检查是否是特殊节点
     */
    private boolean isSpecialNode(String nodeId) {
        return nodeId.contains("结果汇聚") || nodeId.contains("并行处理") ||
               "MERGE".equals(nodeId) || "merge".equals(nodeId) ||
               nodeId.startsWith("🔄");
    }

    /**
     * 🔥 新增：处理特殊节点结果
     */
    private String handleSpecialNodeResult(String nodeId, String combinationId, String executionId, Integer pageNum, Integer pageSize) {
        try {
            log.info("处理特殊节点结果，nodeId: {}, combinationId: {}", nodeId, combinationId);

            if (nodeId.contains("结果汇聚") || nodeId.contains("并行处理") || "MERGE".equals(nodeId)) {
                // 汇聚节点：返回所有并行指标的汇聚结果
                Map<String, Object> result = combinationService.getMergeNodeResult(combinationId, executionId, pageNum, pageSize);
                return JsonBean.success("查询成功", result);
            }

            // 其他特殊节点的处理逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("nodeType", "special");
            result.put("nodeId", nodeId);
            result.put("message", "特殊节点，暂无具体数据");

            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("处理特殊节点结果失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/node/detail/{nodeId}")
    @Operation(summary = "获取节点详细信息", description = "获取流程图节点的详细配置信息")
    public String getNodeDetail(@PathVariable String nodeId,
                              @RequestParam(required = false) String combinationId) {
        try {
            Map<String, Object> result = combinationService.getNodeDetail(nodeId, combinationId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取节点详细信息失败，nodeId: {}, combinationId: {}", nodeId, combinationId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/flow/save")
    @Operation(summary = "保存流程图配置", description = "保存组合的流程图配置")
    public String saveCombinationFlow(@RequestBody String flowData) {
        try {
            combinationService.saveCombinationFlow(flowData);
            return JsonBean.success("保存成功");
        } catch (Exception e) {
            log.error("保存流程图配置失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/flow/generate/{combinationId}")
    @Operation(summary = "自动生成流程图", description = "根据组合配置自动生成流程图")
    public String generateCombinationFlow(@PathVariable String combinationId) {
        try {
            Map<String, Object> result = combinationService.generateCombinationFlow(combinationId);
            return JsonBean.success("流程图生成成功", result);
        } catch (Exception e) {
            log.error("自动生成流程图失败，combinationId: {}", combinationId, e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 6. 统计分析接口
    // =====================================================

    @PostMapping("/statistics")
    @Operation(summary = "获取组合统计信息", description = "获取指标组合的统计分析信息")
    public String getCombinationStatistics() {
        try {
            Map<String, Object> result = combinationService.getCombinationStatistics();
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取组合统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 7. 模型文档接口
    // =====================================================

    @PostMapping("/modelDoc/{combinationId}")
    @Operation(summary = "获取组合关联的模型文档", description = "根据组合ID查询关联的模型文档ID")
    public String getModelDoc(@PathVariable String combinationId) {
        try {
            Map<String, Object> result = combinationService.getModelDoc(combinationId);
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取模型文档失败，combinationId: {}", combinationId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
