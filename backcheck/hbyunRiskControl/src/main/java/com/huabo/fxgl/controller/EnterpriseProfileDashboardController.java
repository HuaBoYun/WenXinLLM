package com.huabo.fxgl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.EnterpriseInfoQueryParam;
import com.huabo.fxgl.dto.FinancialRadarQueryParam;
import com.huabo.fxgl.entity.EnterpriseProfileInfo;
import com.huabo.fxgl.service.IEnterpriseProfileService;
import com.huabo.fxgl.vo.EnterpriseDetailInfoVO;
import com.huabo.fxgl.vo.EnterpriseHologramVO;
import com.huabo.fxgl.vo.EnterpriseInfoVO;
import com.huabo.fxgl.vo.FinancialRadarVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业画像大屏控制器
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/enterpriseProfile/dashboard")
@Tag(name="企业画像大屏",description="企业画像大屏")
public class EnterpriseProfileDashboardController {

    @Autowired
    private IEnterpriseProfileService enterpriseProfileService;

    /**
     * 获取企业基本信息
     */
    @PostMapping("/enterpriseInfo")
    @Operation(summary = "获取企业基本信息", description = "获取大屏顶部区域展示的企业基本信息")
    public String getEnterpriseInfo(@RequestBody EnterpriseInfoQueryParam param,
                                   @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取企业基本信息，企业ID: {}, token: {}", param.getEnterpriseId(), token != null ? "已提供" : "未提供");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(param.getEnterpriseId(), param.getEnterpriseName());
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }
            param.setEnterpriseId(resolvedId);

            // 获取企业信息
            EnterpriseInfoVO result = enterpriseProfileService.getEnterpriseInfo(param);
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("获取企业基本信息失败", e);
            return JsonBean.error("获取企业信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取财务雷达图数据
     */
    @PostMapping("/financialRadar")
    @Operation(summary = "获取财务雷达图数据", description = "获取左侧区域的财务雷达图和关键指标卡片数据")
    public String getFinancialRadarData(@RequestBody FinancialRadarQueryParam param,
                                       @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取财务雷达图数据，企业ID: {}, token: {}", param.getEnterpriseId(), token != null ? "已提供" : "未提供");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(param.getEnterpriseId(), param.getEnterpriseName());
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }
            param.setEnterpriseId(resolvedId);

            // 获取雷达图数据
            FinancialRadarVO result = enterpriseProfileService.getFinancialRadarData(param);
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("获取财务雷达图数据失败", e);
            return JsonBean.error("获取财务雷达图数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取企业选择列表
     */
    @PostMapping("/enterpriseList")
    @Operation(summary = "获取企业选择列表", description = "获取可选择的企业列表，用于大屏企业切换功能")
    public String getEnterpriseList(@RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取企业选择列表, token: {}", token != null ? "已提供" : "未提供");

            // 获取企业列表
            List<EnterpriseProfileInfo> enterpriseList = enterpriseProfileService.getEnterpriseList();
            return JsonBean.success("查询成功", enterpriseList);

        } catch (Exception e) {
            log.error("获取企业选择列表失败", e);
            return JsonBean.error("获取企业列表失败: " + e.getMessage());
        }
    }

    /**
     * 数据刷新
     */
    @PostMapping("/refresh")
    @Operation(summary = "数据刷新", description = "手动触发大屏数据刷新")
    public String refreshData(@RequestBody Map<String, String> param,
                             @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            String enterpriseId = param.get("enterpriseId");
            log.info("刷新企业数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            String enterpriseName = param.get("enterpriseName");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }

            // 刷新数据
            Boolean result = enterpriseProfileService.refreshEnterpriseData(resolvedId);
            
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("success", result);
            resultData.put("refreshTime", com.hbfk.util.DateUtil.getNowTime());
            
            return JsonBean.success("数据刷新成功", resultData);

        } catch (Exception e) {
            log.error("数据刷新失败", e);
            return JsonBean.error("数据刷新失败: " + e.getMessage());
        }
    }

    /**
     * 数据导出
     */
    @PostMapping("/export")
    @Operation(summary = "数据导出", description = "导出大屏展示的数据")
    public String exportData(@RequestBody Map<String, String> param,
                            @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            String enterpriseId = param.get("enterpriseId");
            String exportType = param.getOrDefault("exportType", "excel");

            log.info("导出企业数据，企业ID: {}, 导出类型: {}, token: {}",
                    enterpriseId, exportType, token != null ? "已提供" : "未提供");

            String enterpriseNameExport = param.get("enterpriseName");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseNameExport);
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }

            // 这里实现数据导出逻辑
            // 目前返回导出文件的下载链接
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("downloadUrl", "/api/download/enterprise_profile_" + resolvedId + ".xlsx");
            resultData.put("fileName", "企业画像数据_" + com.hbfk.util.DateUtil.getNowTime("yyyyMMdd") + ".xlsx");
            
            return JsonBean.success("导出成功", resultData);

        } catch (Exception e) {
            log.error("数据导出失败", e);
            return JsonBean.error("数据导出失败: " + e.getMessage());
        }
    }

    /**
     * 趋势分析数据（预留接口）
     */
    @PostMapping("/trendAnalysis")
    @Operation(summary = "获取趋势分析数据", description = "获取中央区域的趋势分析图表数据")
    public String getTrendAnalysisData(@RequestBody Map<String, String> param,
                                      @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取趋势分析数据, token: {}", token != null ? "已提供" : "未提供");

            // 预留接口，后续实现
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "趋势分析功能开发中");
            
            return JsonBean.success("查询成功", resultData);

        } catch (Exception e) {
            log.error("获取趋势分析数据失败", e);
            return JsonBean.error("获取趋势分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 风险预警和审计面板数据（预留接口）
     */
    @PostMapping("/riskAuditPanel")
    @Operation(summary = "获取风险预警和审计面板数据", description = "获取右侧区域的风险预警面板和审计情况面板数据")
    public String getRiskAuditPanelData(@RequestBody Map<String, String> param,
                                       @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取风险预警和审计面板数据, token: {}", token != null ? "已提供" : "未提供");

            // 预留接口，后续实现
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "风险预警和审计面板功能开发中");
            
            return JsonBean.success("查询成功", resultData);

        } catch (Exception e) {
            log.error("获取风险预警和审计面板数据失败", e);
            return JsonBean.error("获取风险预警和审计面板数据失败: " + e.getMessage());
        }
    }

    /**
     * 组织架构与人员分析数据（预留接口）
     */
    @PostMapping("/organizationAnalysis")
    @Operation(summary = "获取组织架构与人员分析数据", description = "获取底部区域的组织架构和人员分析数据")
    public String getOrganizationAnalysisData(@RequestBody Map<String, String> param,
                                             @RequestHeader(value = "token", required = false) String token) {
        try {
            // 接收token但不验证，仅记录日志
            log.info("获取组织架构与人员分析数据, token: {}", token != null ? "已提供" : "未提供");

            // 预留接口，后续实现
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "组织架构与人员分析功能开发中");

            return JsonBean.success("查询成功", resultData);

        } catch (Exception e) {
            log.error("获取组织架构与人员分析数据失败", e);
            return JsonBean.error("获取组织架构与人员分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取指标预警数据
     */
    @PostMapping("/indicatorWarnings")
    @Operation(summary = "获取指标预警数据", description = "获取企业关键指标的预警信息，包括红绿灯状态")
    public String getIndicatorWarnings(@RequestBody String requestBody,
                                      @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = null;

            // 尝试解析请求参数，兼容字符串和JSON对象两种格式
            try {
                if (requestBody.startsWith("{")) {
                    // JSON对象格式
                    ObjectMapper objectMapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, String> param = objectMapper.readValue(requestBody, Map.class);
                    enterpriseId = param.get("enterpriseId");
                } else {
                    // 直接字符串格式，去除引号
                    enterpriseId = requestBody.replaceAll("^\"|\"$", "");
                }
            } catch (Exception parseException) {
                log.warn("参数解析失败，使用默认企业ID: {}", parseException.getMessage());
                enterpriseId = "ENT067"; // 使用默认企业ID
            }

            log.info("获取指标预警数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            // 检查企业是否存在
            if (enterpriseId != null && !enterpriseProfileService.checkEnterpriseExists(enterpriseId)) {
                return JsonBean.error("企业信息不存在");
            }

            // 获取指标预警数据
            List<Map<String, Object>> warningData = enterpriseProfileService.getIndicatorWarnings(enterpriseId);

            return JsonBean.success("查询成功", warningData);

        } catch (Exception e) {
            log.error("获取指标预警数据失败", e);
            return JsonBean.error("获取指标预警数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取企业全息画像数据
     */
    @PostMapping("/hologramData")
    @Operation(summary = "获取企业全息画像数据", description = "获取企业标签、关键指标、风险等级等全息画像信息")
    public String getEnterpriseHologramData(@RequestBody Map<String, Object> param,
                                           @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = (String) param.get("enterpriseId");
            String enterpriseName = (String) param.get("enterpriseName");
            log.info("获取企业全息画像数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }

            // 获取企业全息画像数据
            EnterpriseHologramVO hologramData = enterpriseProfileService.getEnterpriseHologramData(resolvedId);

            return JsonBean.success("查询成功", hologramData);

        } catch (Exception e) {
            log.error("获取企业全息画像数据失败", e);
            return JsonBean.error("获取企业全息画像数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取企业详细信息
     */
    @PostMapping("/detailInfo")
    @Operation(summary = "获取企业详细信息", description = "获取企业详细信息，包括经营信息、联系信息、财务概况等")
    public String getEnterpriseDetailInfo(@RequestBody Map<String, Object> param,
                                         @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = (String) param.get("enterpriseId");
            String enterpriseName = (String) param.get("enterpriseName");
            log.info("获取企业详细信息，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            // 解析企业ID（支持组织ID/企业编码/企业名称）
            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                return JsonBean.error("企业信息不存在");
            }

            // 获取企业详细信息
            EnterpriseDetailInfoVO detailInfo = enterpriseProfileService.getEnterpriseDetailInfo(resolvedId);

            return JsonBean.success("查询成功", detailInfo);

        } catch (Exception e) {
            log.error("获取企业详细信息失败", e);
            return JsonBean.error("获取企业详细信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取人员分析数据
     */
    @PostMapping("/personnelAnalysis")
    @Operation(summary = "获取人员分析数据", description = "获取企业人员分析数据，包括员工总数、学历分布等")
    public String getPersonnelAnalysisData(@RequestBody Map<String, Object> param,
                                            @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = (String) param.get("enterpriseId");
            String enterpriseName = (String) param.get("enterpriseName");
            log.info("获取人员分析数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                // 返回空数据而非错误，前端兜底显示“暂无数据”
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("totalEmployees", 0);
                emptyResult.put("averageAge", 0);
                emptyResult.put("turnoverRate", 0);
                emptyResult.put("educationDistribution", new ArrayList<>());
                emptyResult.put("ageDistribution", new ArrayList<>());
                return JsonBean.success("查询成功", emptyResult);
            }

            Map<String, Object> result = enterpriseProfileService.getPersonnelAnalysisData(resolvedId);
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("获取人员分析数据失败", e);
            return JsonBean.error("获取人员分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取审计数据
     */
    @PostMapping("/auditData")
    @Operation(summary = "获取审计数据", description = "获取企业审计记录和统计数据")
    public String getAuditData(@RequestBody Map<String, Object> param,
                                @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = (String) param.get("enterpriseId");
            String enterpriseName = (String) param.get("enterpriseName");
            log.info("获取审计数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                // 返回空数据而非错误
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("auditRecords", new ArrayList<>());
                Map<String, Object> emptyStats = new HashMap<>();
                emptyStats.put("totalAudits", 0);
                emptyStats.put("totalIssues", 0);
                emptyStats.put("resolvedIssues", 0);
                emptyResult.put("auditStats", emptyStats);
                return JsonBean.success("查询成功", emptyResult);
            }

            Map<String, Object> result = enterpriseProfileService.getAuditData(resolvedId);
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("获取审计数据失败", e);
            return JsonBean.error("获取审计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取法律案件数据
     */
    @PostMapping("/legalData")
    @Operation(summary = "获取法律案件数据", description = "获取企业法律案件记录和统计数据")
    public String getLegalData(@RequestBody Map<String, Object> param,
                                @RequestHeader(value = "token", required = false) String token) {
        try {
            String enterpriseId = (String) param.get("enterpriseId");
            String enterpriseName = (String) param.get("enterpriseName");
            log.info("获取法律案件数据，企业ID: {}, token: {}", enterpriseId, token != null ? "已提供" : "未提供");

            String resolvedId = enterpriseProfileService.resolveEnterpriseId(enterpriseId, enterpriseName);
            if (resolvedId == null) {
                // 返回空数据而非错误
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("legalCases", new ArrayList<>());
                Map<String, Object> emptyStats = new HashMap<>();
                emptyStats.put("totalCases", 0);
                emptyStats.put("activeCases", 0);
                emptyStats.put("totalAmount", 0);
                emptyResult.put("legalStats", emptyStats);
                return JsonBean.success("查询成功", emptyResult);
            }

            Map<String, Object> result = enterpriseProfileService.getLegalData(resolvedId);
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("获取法律案件数据失败", e);
            return JsonBean.error("获取法律案件数据失败: " + e.getMessage());
        }
    }
}
