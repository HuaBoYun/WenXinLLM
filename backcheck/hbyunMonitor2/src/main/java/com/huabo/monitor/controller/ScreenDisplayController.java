package com.huabo.monitor.controller;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.config.ServiceException;
import com.huabo.monitor.service.ScreenDisplayService;
import com.huabo.monitor.util.JsonBean;
import com.huabo.monitor.util.ResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 大屏展示数据 Controller
 */
@RestController
@Slf4j
@Tag(name = "内控-大屏展示", description = "内控-大屏展示")
@RequestMapping("/screen")
public class ScreenDisplayController {

    @Resource
    private ScreenDisplayService screenDisplayService;

    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "各部门评价项目数统计查询成功",
            busType = "内控-大屏展示",
            fail = "各部门评价项目数统计查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/departmentProjectCount")
    @Operation(summary = "各部门评价项目数统计")
    public JsonBean<Map<String, Object>> getDepartmentProjectCount(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDepartmentProjectCount();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("各部门评价项目数统计...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "控制有效性数据查询成功",
            busType = "内控-大屏展示",
            fail = "控制有效性数据查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/controlEffectiveness")
    @Operation(summary = "控制有效性数据查询")
    public JsonBean<Map<String, Object>> getControlEffectivenessData(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getControlEffectivenessData();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("控制有效性数据查询...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "各部门评价项目数占比统计查询成功",
            busType = "内控-大屏展示",
            fail = "各部门评价项目数占比统计查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/departmentProjectRatio")
    @Operation(summary = "各部门评价项目数占比统计")
    public JsonBean<Map<String, Object>> getDepartmentProjectRatio(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDepartmentProjectRatio();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("各部门评价项目数占比统计...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "缺陷类型分布统计查询成功",
            busType = "内控-大屏展示",
            fail = "缺陷类型分布统计查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/defectTypeDistribution")
    @Operation(summary = "缺陷类型分布统计")
    public JsonBean<Map<String, Object>> getDefectTypeDistribution(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDefectTypeDistribution();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("缺陷类型分布统计...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "各单位缺陷数量对比分析查询成功",
            busType = "内控-大屏展示",
            fail = "各单位缺陷数量对比分析查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/departmentDefectComparison")
    @Operation(summary = "各单位缺陷数量对比分析")
    public JsonBean<Map<String, Object>> getDepartmentDefectComparison(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDepartmentDefectComparison();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("各单位缺陷数量对比分析...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "本年缺陷项目趋势分析查询成功",
            busType = "内控-大屏展示",
            fail = "本年缺陷项目趋势分析查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/defectTrendAnalysis")
    @Operation(summary = "本年缺陷项目趋势分析")
    public JsonBean<Map<String, Object>> getDefectTrendAnalysis(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDefectTrendAnalysis();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("本年缺陷项目趋势分析...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    @OperationLog(
            success = "缺陷属性分布统计查询成功",
            busType = "内控-大屏展示",
            fail = "缺陷属性分布统计查询失败",
            operationType = OperationType.SELECT,
            subType = "内控-大屏展示"
    )
    @GetMapping("/statistics/defectPropertyDistribution")
    @Operation(summary = "缺陷属性分布统计")
    public JsonBean<Map<String, Object>> getDefectPropertyDistribution(@RequestHeader("token") String token) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            return screenDisplayService.getDefectPropertyDistribution();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("缺陷属性分布统计...接口异常", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }
}

