package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.budgetPlanning.dto.BudgetExecutionQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetExecution;
import com.financial.sharing.budgetPlanning.service.BudgetExecutionService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 预算执行分析Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Slf4j
@Api(tags = "预算执行分析")
@RestController
@RequestMapping("/budgetPlanning/budgetExecution")
public class BudgetExecutionController {

    @Autowired
    private BudgetExecutionService budgetExecutionService;

    /**
     * 把异常压扁成可读字符串：找到根因 + 类名 + 消息。
     * 用于诊断 e.getMessage() 为 null 的情况（典型如 NPE）。
     */
    private static String diagMsg(Throwable e) {
        if (e == null) {
            return "未知错误";
        }
        Throwable root = e;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String cls = root.getClass().getSimpleName();
        String msg = root.getMessage();
        if (msg == null) {
            // 取第一行栈，方便定位
            StackTraceElement[] st = root.getStackTrace();
            String at = (st != null && st.length > 0) ? " at " + st[0].toString() : "";
            return "[" + cls + "]" + at;
        }
        return "[" + cls + "] " + msg;
    }

    /**
     * 查询预算执行列表
     */
    @ApiOperation("查询预算执行列表")
    @PostMapping("/getExecutionList")
    public MyJsonBean getExecutionList(@RequestBody BudgetExecutionQueryParam param) {
        try {
            // 获取当前用户组织ID（统一使用 ORG_ID 字段进行租户/组织隔离）
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            PageInfo<TblBudgetExecution> pageInfo = budgetExecutionService.getExecutionList(param);
            return MyJsonBean.successData(pageInfo);
        } catch (Exception e) {
            log.error("查询预算执行列表失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 根据ID查询预算执行
     */
    @ApiOperation("根据ID查询预算执行")
    @PostMapping("/getExecutionById")
    public MyJsonBean getExecutionById(@RequestBody Map<String, String> params) {
        try {
            String executionId = params.get("executionId");
            if (executionId == null || executionId.trim().isEmpty()) {
                return MyJsonBean.errorData("执行ID不能为空");
            }

            TblBudgetExecution execution = budgetExecutionService.getExecutionById(executionId);
            if (execution == null) {
                return MyJsonBean.errorData("执行记录不存在");
            }

            return MyJsonBean.successData(execution);
        } catch (Exception e) {
            log.error("查询预算执行失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算执行统计
     */
    @ApiOperation("查询预算执行统计")
    @PostMapping("/getExecutionStatistics")
    public MyJsonBean getExecutionStatistics(@RequestBody BudgetExecutionQueryParam param) {
        // 未登录时返回零值统计, 避免前端卡片渲染异常
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            Map<String, Object> statistics = budgetExecutionService.getExecutionStatistics(param);
            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            // 登录态字段缺失(orgid 取不到等)时降级为空统计, 让前端友好显示 0
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(emptyStatistics());
            }
            log.error("查询预算执行统计失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /** 识别 UserUtils.requireXxx() 抛出的"用户未登录或会话已失效"异常 */
    private static boolean isSessionExpired(Throwable e) {
        while (e != null) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("用户未登录或会话已失效")) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    private static Map<String, Object> emptyStatistics() {
        Map<String, Object> empty = new java.util.HashMap<>();
        empty.put("totalCount", 0);
        empty.put("normalCount", 0);
        empty.put("warningCount", 0);
        empty.put("exceededCount", 0);
        empty.put("totalBudget", 0);
        empty.put("totalAdjusted", 0);
        empty.put("totalActual", 0);
        empty.put("totalAvailable", 0);
        empty.put("avgExecutionRate", 0);
        return empty;
    }

    /**
     * 查询预算执行趋势
     */
    @ApiOperation("查询预算执行趋势")
    @PostMapping("/getExecutionTrend")
    public MyJsonBean getExecutionTrend(@RequestBody BudgetExecutionQueryParam param) {
        try {
            // 获取当前用户组织ID（统一使用 ORG_ID 字段进行租户/组织隔离）
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            List<Map<String, Object>> trend = budgetExecutionService.getExecutionTrend(param);
            return MyJsonBean.successData(trend);
        } catch (Exception e) {
            log.error("查询预算执行趋势失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算执行预警列表
     */
    @ApiOperation("查询预算执行预警列表")
    @PostMapping("/getExecutionWarnings")
    public MyJsonBean getExecutionWarnings(@RequestBody BudgetExecutionQueryParam param) {
        try {
            // 获取当前用户组织ID（统一使用 ORG_ID 字段进行租户/组织隔离）
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            PageInfo<TblBudgetExecution> pageInfo = budgetExecutionService.getExecutionWarnings(param);
            return MyJsonBean.successData(pageInfo);
        } catch (Exception e) {
            log.error("查询预算执行预警列表失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 刷新预算执行数据
     */
    @ApiOperation("刷新预算执行数据")
    @PostMapping("/refreshExecutionData")
    public MyJsonBean refreshExecutionData(@RequestBody BudgetExecutionQueryParam param) {
        try {
            // 获取当前用户组织ID（统一使用 ORG_ID 字段进行租户/组织隔离）
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            Map<String, Object> result = budgetExecutionService.refreshExecutionData(param);
            if ((Boolean) result.get("success")) {
                return MyJsonBean.successData(result);
            } else {
                return MyJsonBean.errorData((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("刷新预算执行数据失败", e);
            return MyJsonBean.errorData("刷新失败: " + diagMsg(e));
        }
    }
}

