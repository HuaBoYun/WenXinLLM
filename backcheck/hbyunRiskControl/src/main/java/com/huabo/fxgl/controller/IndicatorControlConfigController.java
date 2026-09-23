package com.huabo.fxgl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblIndicatorControlConfig;
import com.huabo.fxgl.service.IDataModelService;
import com.huabo.fxgl.service.IIndicatorControlConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标管控配置控制器
 * <p>提供风险监测指标管控配置的查询、设置等接口</p>
 *
 * @author hbyun
 */
@Slf4j
@RestController
@RequestMapping("/indicatorControl")
@Tag(name = "indicatorControl", description = "indicator control config")
public class IndicatorControlConfigController {

    @Autowired
    private IIndicatorControlConfigService configService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private IDataModelService dataModelService;

    @Value("${indicator-control.external-api-key:}")
    private String externalApiKey;

    @PostMapping("/list")
    @Operation(summary = "指标管控配置列表查询")
    public String list(@RequestBody Map<String, Object> params) {
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 20).toString());
        IPage<TblIndicatorControlConfig> page = configService.getConfigPage(pageNum, pageSize);
        return JsonBean.success("ok", page);
    }

    @PostMapping("/save")
    @Operation(summary = "保存指标管控配置")
    public String save(@RequestBody TblIndicatorControlConfig config) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }
            configService.saveConfig(config, staff.getRealname());
            return JsonBean.success("ok");
        } catch (Exception e) {
            log.error("保存指标控制配置失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除指标管控配置")
    public String delete(@PathVariable String id) {
        configService.removeById(id);
        return JsonBean.success("ok");
    }

    @PostMapping("/getByRightId")
    @Operation(summary = "根据权限ID查询指标管控配置")
    public String getByRightId(@RequestParam String rightId, @RequestParam String operationType) {
        List<TblIndicatorControlConfig> configs = configService.getEnabledConfigs(rightId, operationType);
        Map<String, Object> result = new HashMap<>();
        result.put("configs", configs);
        result.put("needControl", !configs.isEmpty());
        return JsonBean.success("ok", result);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/validate")
    @Operation(summary = "指标校验执行")
    public String validate(@RequestBody Map<String, Object> requestBody) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }
            String modelId = (String) requestBody.get("modelId");
            Map<String, Object> params = (Map<String, Object>) requestBody.get("params");
            JsonBean result = dataModelService.executeDataModel(modelId, params, staff.getRealname());
            return result.toString();
        } catch (Exception e) {
            log.error("指标校验执行失败", e);
            return JsonBean.error("校验失败: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/external/check")
    @Operation(summary = "外部系统指标校验")
    public String externalCheck(@RequestBody Map<String, Object> requestBody,
                                @RequestHeader(value = "X-Api-Key", required = false) String apiKey) {
        try {
            // 1. 验证 API Key
            if (externalApiKey == null || externalApiKey.isEmpty()) {
                return JsonBean.error("外部系统校验未配置API密钥，请联系管理员");
            }
            if (apiKey == null || !apiKey.equals(externalApiKey)) {
                return JsonBean.error("API密钥验证失败");
            }

            // 2. 解析请求参数
            String pageKey = (String) requestBody.get("pageKey");
            String operationType = (String) requestBody.get("operationType");
            Map<String, Object> params = (Map<String, Object>) requestBody.get("params");

            if (pageKey == null || pageKey.isEmpty()) {
                return JsonBean.error("pageKey不能为空");
            }
            if (operationType == null || operationType.isEmpty()) {
                return JsonBean.error("operationType不能为空");
            }

            // 3. 查询外部系统配置
            List<TblIndicatorControlConfig> configs = configService.getEnabledConfigsByExternalKey(pageKey, operationType);
            if (configs.isEmpty()) {
                Map<String, Object> passResult = new HashMap<>();
                passResult.put("allowed", true);
                passResult.put("controlLevel", null);
                passResult.put("message", "无匹配的控制配置，放行");
                return JsonBean.success("ok", passResult);
            }

            // 4. 逐个执行模型校验（遇到第一个拦截即返回）
            for (TblIndicatorControlConfig config : configs) {
                String modelId = config.getModelId();
                JsonBean modelResult = dataModelService.executeDataModel(modelId, params != null ? params : new HashMap<>(), "EXTERNAL_SYSTEM");

                if (modelResult.getCode() != 1) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("allowed", false);
                    errorResult.put("controlLevel", config.getControlLevel());
                    errorResult.put("message", "模型执行失败: " + modelResult.getMsg());
                    return JsonBean.success("ok", errorResult);
                }

                // 解析模型执行结果
                Map<String, Object> executeResult = (Map<String, Object>) modelResult.getData();
                Object dataObj = executeResult.get("data");
                if (dataObj instanceof List && !((List<?>) dataObj).isEmpty()) {
                    Map<String, Object> firstRow = (Map<String, Object>) ((List<?>) dataObj).get(0);
                    String isControl = String.valueOf(firstRow.getOrDefault("IS_CONTROL", "false"));
                    String message = String.valueOf(firstRow.getOrDefault("MESSAGE", ""));

                    if ("true".equalsIgnoreCase(isControl) || "1".equals(isControl) || "Y".equalsIgnoreCase(isControl)) {
                        Map<String, Object> blockResult = new HashMap<>();
                        blockResult.put("allowed", false);
                        blockResult.put("controlLevel", config.getControlLevel());
                        blockResult.put("message", message.isEmpty() ? "指标控制校验未通过" : message);
                        return JsonBean.success("ok", blockResult);
                    }
                }

                // 阈值比较模式
                String thresholdStr = config.getThresholdValue();
                if (thresholdStr != null && !thresholdStr.isEmpty()) {
                    try {
                        double threshold = Double.parseDouble(thresholdStr);
                        Object dataObj2 = executeResult.get("data");
                        if (dataObj2 instanceof List && !((List<?>) dataObj2).isEmpty()) {
                            Map<String, Object> row = (Map<String, Object>) ((List<?>) dataObj2).get(0);
                            for (Object val : row.values()) {
                                try {
                                    double numVal = Double.parseDouble(String.valueOf(val));
                                    if (numVal >= threshold) {
                                        Map<String, Object> blockResult = new HashMap<>();
                                        blockResult.put("allowed", false);
                                        blockResult.put("controlLevel", config.getControlLevel());
                                        blockResult.put("message", "指标值 " + numVal + " 超过阈值 " + threshold);
                                        return JsonBean.success("ok", blockResult);
                                    }
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

            // 5. 所有模型都通过
            Map<String, Object> passResult = new HashMap<>();
            passResult.put("allowed", true);
            passResult.put("controlLevel", null);
            passResult.put("message", "校验通过");
            return JsonBean.success("ok", passResult);

        } catch (Exception e) {
            log.error("外部系统指标校验失败", e);
            return JsonBean.error("校验失败: " + e.getMessage());
        }
    }

}