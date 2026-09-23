package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRiskDTO;
import com.global.treasurer.dto.FinancingRiskQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;
import com.global.treasurer.service.FinancingRiskService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资风险监控管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Controller
@RequestMapping({"/rzgl/financing-risk", "/financial/rzgl/financing-risk", "/centralaudit/rzgl/financing-risk"})
@Api(tags = "融资风险监控管理")
public class FinancingRiskController {
    private static final Logger log = LoggerFactory.getLogger(FinancingRiskController.class);

    @Resource
    private FinancingRiskService financingRiskService;

    @Resource
    private UserProvider userProvider;

    /**
     * 1. 分页查询风险列表
     */
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询风险列表")
    public String getRiskList(FinancingRiskQueryDTO queryDTO,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblFinancingMonitoring> pageInfo = financingRiskService.getRiskList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询融资风险列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 2. 获取风险详情
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("获取风险详情")
    public String getRiskDetail(@PathVariable Long id,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingMonitoring risk = financingRiskService.getRiskById(id);
            if (risk == null) {
                return new JsonBean(0, "风险记录不存在", null).toJson();
            }
            return new JsonBean(1, "成功", risk).toJson();
        } catch (Exception e) {
            log.error("获取融资风险详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 3. 创建风险记录
     */
    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建风险记录")
    public String createRisk(@FlexibleRequestBody FinancingRiskDTO dto,
                            @RequestHeader(value = "token", required = false) String token,
                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingMonitoring risk = financingRiskService.createRisk(dto);
            return new JsonBean(1, "创建成功", risk).toJson();
        } catch (Exception e) {
            log.error("创建融资风险记录失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 4. 更新风险记录
     */
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新风险记录")
    public String updateRisk(@FlexibleRequestBody FinancingRiskDTO dto,
                            @RequestHeader(value = "token", required = false) String token,
                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (dto.getMonitoringId() == null) {
                return new JsonBean(0, "监控ID不能为空", null).toJson();
            }

            TblFinancingMonitoring risk = financingRiskService.updateRisk(dto);
            return new JsonBean(1, "更新成功", risk).toJson();
        } catch (Exception e) {
            log.error("更新融资风险记录失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 5. 风险评估
     */
    @PostMapping("/assessment")
    @ResponseBody
    @ApiOperation("风险评估")
    public String assessRisk(@RequestParam Long relatedFinancingId,
                             @RequestHeader(value = "token", required = false) String token,
                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (relatedFinancingId == null) {
                return new JsonBean(0, "关联融资ID不能为空", null).toJson();
            }

            Map<String, Object> assessment = financingRiskService.assessRisk(relatedFinancingId);
            return new JsonBean(1, "评估成功", assessment).toJson();
        } catch (Exception e) {
            log.error("风险评估失败", e);
            return new JsonBean(0, "评估失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 6. 风险预警
     */
    @PostMapping("/alert")
    @ResponseBody
    @ApiOperation("风险预警")
    public String alertRisk(@RequestParam Long relatedFinancingId,
                           @RequestHeader(value = "token", required = false) String token,
                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (relatedFinancingId == null) {
                return new JsonBean(0, "关联融资ID不能为空", null).toJson();
            }

            List<TblFinancingMonitoring> alerts = financingRiskService.alertRisk(relatedFinancingId);
            return new JsonBean(1, "预警成功", alerts).toJson();
        } catch (Exception e) {
            log.error("风险预警失败", e);
            return new JsonBean(0, "预警失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 7. 获取风险趋势统计
     */
    @GetMapping("/statistics/trend")
    @ResponseBody
    @ApiOperation("获取风险趋势统计")
    public String getRiskTrend(@RequestParam(required = false) Long companyId,
                              @RequestParam(defaultValue = "30") Integer days,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> trend = financingRiskService.getRiskTrend(companyId, days);
            return new JsonBean(1, "成功", trend).toJson();
        } catch (Exception e) {
            log.error("获取风险趋势统计失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 8. 处理风险
     */
    @PostMapping("/process")
    @ResponseBody
    @ApiOperation("处理风险")
    public String processRisk(@RequestParam Long monitoringId,
                             @RequestParam(required = false) String handlerName,
                             @RequestParam(required = false) String handleOpinion,
                             @RequestParam(required = false, defaultValue = "HANDLED") String alertStatus,
                             @RequestHeader(value = "token", required = false) String token,
                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (monitoringId == null) {
                return new JsonBean(0, "监控ID不能为空", null).toJson();
            }

            boolean result = financingRiskService.processRisk(monitoringId, handlerName, handleOpinion, alertStatus);
            if (result) {
                return new JsonBean(1, "处理成功", null).toJson();
            } else {
                return new JsonBean(0, "处理失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("处理融资风险失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除风险记录
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除风险记录")
    public String deleteRisk(@PathVariable Long id,
                            @RequestHeader(value = "token", required = false) String token,
                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingRiskService.deleteRisk(id);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除融资风险记录失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预警类型列表
     */
    @GetMapping("/alertTypes")
    @ResponseBody
    @ApiOperation("获取预警类型列表")
    public String getAlertTypes(@RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 返回预警类型列表
            List<Map<String, String>> alertTypes = new java.util.ArrayList<>();

            Map<String, String> alertType1 = new HashMap<>();
            alertType1.put("code", "INTEREST_RATE");
            alertType1.put("name", "利率风险");
            alertTypes.add(alertType1);

            Map<String, String> alertType2 = new HashMap<>();
            alertType2.put("code", "EXPIRY");
            alertType2.put("name", "到期风险");
            alertTypes.add(alertType2);

            Map<String, String> alertType3 = new HashMap<>();
            alertType3.put("code", "CREDIT");
            alertType3.put("name", "信用风险");
            alertTypes.add(alertType3);

            Map<String, String> alertType4 = new HashMap<>();
            alertType4.put("code", "LIQUIDITY");
            alertType4.put("name", "流动性风险");
            alertTypes.add(alertType4);

            Map<String, String> alertType5 = new HashMap<>();
            alertType5.put("code", "MARKET");
            alertType5.put("name", "市场风险");
            alertTypes.add(alertType5);

            Map<String, String> alertType6 = new HashMap<>();
            alertType6.put("code", "OPERATION");
            alertType6.put("name", "操作风险");
            alertTypes.add(alertType6);
            return new JsonBean(1, "成功", alertTypes).toJson();
        } catch (Exception e) {
            log.error("获取预警类型列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预警级别列表
     */
    @GetMapping("/alertLevels")
    @ResponseBody
    @ApiOperation("获取预警级别列表")
    public String getAlertLevels(@RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 返回预警级别列表
            List<Map<String, String>> alertLevels = new java.util.ArrayList<>();

            Map<String, String> alertLevel1 = new HashMap<>();
            alertLevel1.put("code", "HIGH");
            alertLevel1.put("name", "高风险");
            alertLevels.add(alertLevel1);

            Map<String, String> alertLevel2 = new HashMap<>();
            alertLevel2.put("code", "MEDIUM");
            alertLevel2.put("name", "中风险");
            alertLevels.add(alertLevel2);

            Map<String, String> alertLevel3 = new HashMap<>();
            alertLevel3.put("code", "LOW");
            alertLevel3.put("name", "低风险");
            alertLevels.add(alertLevel3);
            return new JsonBean(1, "成功", alertLevels).toJson();
        } catch (Exception e) {
            log.error("获取预警级别列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取风险统计汇总
     */
    @GetMapping("/statistics/summary")
    @ResponseBody
    @ApiOperation("获取风险统计汇总")
    public String getRiskSummary(@RequestParam(required = false) Long companyId,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 构建统计汇总数据
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalRisks", 150);
            summary.put("highRiskCount", 15);
            summary.put("mediumRiskCount", 45);
            summary.put("lowRiskCount", 90);
            summary.put("pendingCount", 30);
            summary.put("handledCount", 100);
            summary.put("closedCount", 20);

            return new JsonBean(1, "成功", summary).toJson();
        } catch (Exception e) {
            log.error("获取风险统计汇总失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
