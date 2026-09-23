package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetSummaryQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetSummary;
import com.financial.sharing.budgetPlanning.service.BudgetSummaryService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 预算数据汇总Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Api(tags = "计划预算-预算数据汇总")
@RestController
@RequestMapping("/budgetPlanning/budgetSummary")
public class BudgetSummaryController {

    @Autowired
    private BudgetSummaryService summaryService;

    @ApiOperation("执行汇总")
    @PostMapping("/executeSummary")
    public MyJsonBean executeSummary(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            String version = params.get("version");
            String summaryType = params.get("summaryType");
            String summaryMethod = params.get("summaryMethod");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("预算模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("预算期间不能为空");
            }
            if (version == null || version.isEmpty()) {
                return MyJsonBean.errorData("预算版本不能为空");
            }
            if (summaryType == null || summaryType.isEmpty()) {
                return MyJsonBean.errorData("汇总类型不能为空");
            }
            if (summaryMethod == null || summaryMethod.isEmpty()) {
                return MyJsonBean.errorData("汇总方法不能为空");
            }
            
            summaryService.executeSummary(modelId, period, version, summaryType, summaryMethod);
            return MyJsonBean.ok("汇总执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("汇总执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询汇总记录列表(分页)")
    @PostMapping("/getSummaryList")
    public MyJsonBean getSummaryList(@RequestBody BudgetSummaryQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetSummary> pageInfo = summaryService.getSummaryList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询汇总记录")
    @PostMapping("/getSummaryById")
    public MyJsonBean getSummaryById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String summaryId = params.get("summaryId");
            if (summaryId == null || summaryId.isEmpty()) {
                return MyJsonBean.errorData("汇总ID不能为空");
            }
            
            TblBudgetSummary summary = summaryService.getSummaryById(summaryId);
            return MyJsonBean.ok("查询成功", summary);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据条件查询汇总记录")
    @PostMapping("/getSummaryByCondition")
    public MyJsonBean getSummaryByCondition(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            String version = params.get("version");
            String summaryType = params.get("summaryType");
            
            List<TblBudgetSummary> list = summaryService.getSummaryByCondition(modelId, period, version, summaryType);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除汇总记录")
    @PostMapping("/deleteSummary")
    public MyJsonBean deleteSummary(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String summaryId = params.get("summaryId");
            if (summaryId == null || summaryId.isEmpty()) {
                return MyJsonBean.errorData("汇总ID不能为空");
            }
            
            summaryService.deleteSummary(summaryId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除汇总记录")
    @PostMapping("/batchDeleteSummary")
    public MyJsonBean batchDeleteSummary(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> summaryIds = (List<String>) params.get("summaryIds");
            
            if (summaryIds == null || summaryIds.isEmpty()) {
                return MyJsonBean.errorData("汇总ID列表不能为空");
            }
            
            summaryService.batchDeleteSummary(summaryIds);
            return MyJsonBean.ok("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("重新执行汇总")
    @PostMapping("/reExecuteSummary")
    public MyJsonBean reExecuteSummary(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String summaryId = params.get("summaryId");
            if (summaryId == null || summaryId.isEmpty()) {
                return MyJsonBean.errorData("汇总ID不能为空");
            }

            summaryService.reExecuteSummary(summaryId);
            return MyJsonBean.ok("重新执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("重新执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询汇总统计信息")
    @PostMapping("/getSummaryStatistics")
    public MyJsonBean getSummaryStatistics() {
        // 未登录时返回零值统计, 避免前端卡片渲染异常 / 一直停在初始值
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", emptyStatistics());
        }
        try {
            Map<String, Object> statistics = summaryService.getSummaryStatistics();
            return MyJsonBean.ok("查询成功", statistics);
        } catch (Exception e) {
            // 登录态字段缺失（如 orgid 取不到）时同样降级为空数据, 前端友好
            if (isSessionExpired(e)) {
                return MyJsonBean.ok("查询成功", emptyStatistics());
            }
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
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
        empty.put("completedCount", 0);
        empty.put("processingCount", 0);
        empty.put("failedCount", 0);
        empty.put("totalDataCount", 0);
        return empty;
    }

    @ApiOperation("导出汇总数据")
    @PostMapping("/exportSummary")
    public void exportSummary(@RequestBody Map<String, String> params,
                              javax.servlet.http.HttpServletResponse response) {
        try {
            if (UserUtils.getUser() == null) {
                writePlain(response, 401, "用户未登录");
                return;
            }
            String summaryId = params.get("summaryId");
            if (summaryId == null || summaryId.isEmpty()) {
                writePlain(response, 400, "汇总ID不能为空");
                return;
            }
            summaryService.exportSummary(summaryId, response);
        } catch (Exception e) {
            e.printStackTrace();
            writePlain(response, 500, "导出失败: " + e.getMessage());
        }
    }

    /** 异常时以纯文本返回, 避免响应已被设置成 xlsx Content-Type 后写 JSON 出错 */
    private static void writePlain(javax.servlet.http.HttpServletResponse response, int status, String msg) {
        try {
            if (!response.isCommitted()) {
                response.reset();
                response.setStatus(status);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(msg);
            }
        } catch (java.io.IOException ignored) {
        }
    }
}

