package com.huabo.system.controller;

import com.huabo.system.service.DataGrowthTrendService;
import com.hbfk.util.JsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据增长趋势Controller
 * 用于系统画像和用户画像大屏数据接口
 */
@RestController
@RequestMapping("/dataGrowthTrend")
@Slf4j
@Tag(name = "DataGrowthTrendController", description = "数据增长趋势大屏接口")
public class DataGrowthTrendController {

    @Resource
    private DataGrowthTrendService dataGrowthTrendService;

    /**
     * 获取数据增长趋势
     * @return JsonBean
     */
    @RequestMapping(value = "/getList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取数据增长趋势")
    public JsonBean getDataGrowthTrend() {
        try {
            log.info("获取数据增长趋势");
            return dataGrowthTrendService.getDataGrowthTrend();
        } catch (Exception e) {
            log.error("获取数据增长趋势失败", e);
            return new JsonBean(0, "获取数据增长趋势失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取数据总量
     * @return JsonBean
     */
    @RequestMapping(value = "/getTotal", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取数据总量")
    public JsonBean getDataTotal() {
        try {
            log.info("获取数据总量");
            return dataGrowthTrendService.getDataTotal();
        } catch (Exception e) {
            log.error("获取数据总量失败", e);
            return new JsonBean(0, "获取数据总量失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取核心功能使用率
     * @return JsonBean
     */
    @RequestMapping(value = "/getCoreFunctionUsage", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取核心功能使用率")
    public JsonBean getCoreFunctionUsage() {
        try {
            log.info("获取核心功能使用率");
            return dataGrowthTrendService.getCoreFunctionUsage();
        } catch (Exception e) {
            log.error("获取核心功能使用率失败", e);
            return new JsonBean(0, "获取核心功能使用率失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取业务活跃度排名
     * @return JsonBean
     */
    @RequestMapping(value = "/getBusinessActivityRanking", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取业务活跃度排名")
    public JsonBean getBusinessActivityRanking() {
        try {
            log.info("获取业务活跃度排名");
            return dataGrowthTrendService.getBusinessActivityRanking();
        } catch (Exception e) {
            log.error("获取业务活跃度排名失败", e);
            return new JsonBean(0, "获取业务活跃度排名失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取新增用户数
     * @return JsonBean
     */
    @RequestMapping(value = "/getNewUserCount", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取新增用户数")
    public JsonBean getNewUserCount() {
        try {
            log.info("获取新增用户数");
            return dataGrowthTrendService.getNewUserCount();
        } catch (Exception e) {
            log.error("获取新增用户数失败", e);
            return new JsonBean(0, "获取新增用户数失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取用户活跃度
     * @return JsonBean
     */
    @RequestMapping(value = "/getUserActivity", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取用户活跃度")
    public JsonBean getUserActivity() {
        try {
            log.info("获取用户活跃度");
            return dataGrowthTrendService.getUserActivity();
        } catch (Exception e) {
            log.error("获取用户活跃度失败", e);
            return new JsonBean(0, "获取用户活跃度失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取风险内控审计维度
     * @return JsonBean
     */
    @RequestMapping(value = "/getRiskAuditDimension", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取风险内控审计维度")
    public JsonBean getRiskAuditDimension() {
        try {
            log.info("获取风险内控审计维度");
            return dataGrowthTrendService.getRiskAuditDimension();
        } catch (Exception e) {
            log.error("获取风险内控审计维度失败", e);
            return new JsonBean(0, "获取风险内控审计维度失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取内控测试缺陷程度
     * @return JsonBean
     */
    @RequestMapping(value = "/getInternalControlDefect", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取内控测试缺陷程度")
    public JsonBean getInternalControlDefect() {
        try {
            log.info("获取内控测试缺陷程度");
            return dataGrowthTrendService.getInternalControlDefect();
        } catch (Exception e) {
            log.error("获取内控测试缺陷程度失败", e);
            return new JsonBean(0, "获取内控测试缺陷程度失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取风险事件处理
     * @return JsonBean
     */
    @RequestMapping(value = "/getRiskEventHandling", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取风险事件处理")
    public JsonBean getRiskEventHandling() {
        try {
            log.info("获取风险事件处理");
            return dataGrowthTrendService.getRiskEventHandling();
        } catch (Exception e) {
            log.error("获取风险事件处理失败", e);
            return new JsonBean(0, "获取风险事件处理失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取审计问题数量
     * @return JsonBean
     */
    @RequestMapping(value = "/getAuditIssueCount", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取审计问题数量")
    public JsonBean getAuditIssueCount() {
        try {
            log.info("获取审计问题数量");
            return dataGrowthTrendService.getAuditIssueCount();
        } catch (Exception e) {
            log.error("获取审计问题数量失败", e);
            return new JsonBean(0, "获取审计问题数量失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取内控缺陷整改跟进
     * @return JsonBean
     */
    @RequestMapping(value = "/getInternalControlRectification", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取内控缺陷整改跟进")
    public JsonBean getInternalControlRectification() {
        try {
            log.info("获取内控缺陷整改跟进");
            return dataGrowthTrendService.getInternalControlRectification();
        } catch (Exception e) {
            log.error("获取内控缺陷整改跟进失败", e);
            return new JsonBean(0, "获取内控缺陷整改跟进失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取审计问题整改验证完成率
     * @return JsonBean
     */
    @RequestMapping(value = "/getAuditIssueVerificationRate", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取审计问题整改验证完成率")
    public JsonBean getAuditIssueVerificationRate() {
        try {
            log.info("获取审计问题整改验证完成率");
            return dataGrowthTrendService.getAuditIssueVerificationRate();
        } catch (Exception e) {
            log.error("获取审计问题整改验证完成率失败", e);
            return new JsonBean(0, "获取审计问题整改验证完成率失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取风险预警响应率
     * @return JsonBean
     */
    @RequestMapping(value = "/getRiskWarningResponse", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取风险预警响应率")
    public JsonBean getRiskWarningResponse() {
        try {
            log.info("获取风险预警响应率");
            return dataGrowthTrendService.getRiskWarningResponse();
        } catch (Exception e) {
            log.error("获取风险预警响应率失败", e);
            return new JsonBean(0, "获取风险预警响应率失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取基础用户信息
     * @return JsonBean
     */
    @RequestMapping(value = "/getBasicUserInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取基础用户信息")
    public JsonBean getBasicUserInfo() {
        try {
            log.info("获取基础用户信息");
            return dataGrowthTrendService.getBasicUserInfo();
        } catch (Exception e) {
            log.error("获取基础用户信息失败", e);
            return new JsonBean(0, "获取基础用户信息失败: " + e.getMessage(), null);
        }
    }
}
