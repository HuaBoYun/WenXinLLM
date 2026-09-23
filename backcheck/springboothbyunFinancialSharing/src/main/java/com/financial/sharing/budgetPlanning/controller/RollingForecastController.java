package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.RollingForecastQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecast;
import com.financial.sharing.budgetPlanning.service.RollingForecastService;
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
 * 滚动预测Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Api(tags = "计划预算-滚动预测管理")
@RestController
@RequestMapping("/budgetPlanning/rollingForecast")
public class RollingForecastController {

    @Autowired
    private RollingForecastService forecastService;

    @ApiOperation("创建预测任务")
    @PostMapping("/createForecast")
    public MyJsonBean createForecast(@RequestBody TblRollingForecast forecast) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            forecastService.createForecast(forecast);
            return MyJsonBean.ok("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改预测任务")
    @PostMapping("/updateForecast")
    public MyJsonBean updateForecast(@RequestBody TblRollingForecast forecast) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            forecastService.updateForecast(forecast);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询预测任务列表(分页)")
    @PostMapping("/getForecastList")
    public MyJsonBean getForecastList(@RequestBody RollingForecastQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblRollingForecast> pageInfo = forecastService.getForecastList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询预测任务")
    @PostMapping("/getForecastById")
    public MyJsonBean getForecastById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }
            
            TblRollingForecast forecast = forecastService.getForecastById(forecastId);
            return MyJsonBean.ok("查询成功", forecast);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询预测任务(包含数据明细)")
    @PostMapping("/getForecastWithData")
    public MyJsonBean getForecastWithData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }
            
            TblRollingForecast forecast = forecastService.getForecastWithData(forecastId);
            return MyJsonBean.ok("查询成功", forecast);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预测任务")
    @PostMapping("/deleteForecast")
    public MyJsonBean deleteForecast(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }
            
            forecastService.deleteForecast(forecastId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除预测任务")
    @PostMapping("/batchDeleteForecast")
    public MyJsonBean batchDeleteForecast(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> forecastIds = (List<String>) params.get("forecastIds");
            
            if (forecastIds == null || forecastIds.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID列表不能为空");
            }
            
            forecastService.batchDeleteForecast(forecastIds);
            return MyJsonBean.ok("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("提交预测任务")
    @PostMapping("/submitForecast")
    public MyJsonBean submitForecast(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }

            forecastService.submitForecast(forecastId);
            return MyJsonBean.ok("提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批预测任务")
    @PostMapping("/approveForecast")
    public MyJsonBean approveForecast(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = (String) params.get("forecastId");
            Boolean approved = (Boolean) params.get("approved");
            String opinion = (String) params.get("opinion");

            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }
            if (approved == null) {
                return MyJsonBean.errorData("审批结果不能为空");
            }

            forecastService.approveForecast(forecastId, approved, opinion);
            return MyJsonBean.ok("审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("撤销预测任务")
    @PostMapping("/withdrawForecast")
    public MyJsonBean withdrawForecast(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }

            forecastService.withdrawForecast(forecastId);
            return MyJsonBean.ok("撤销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("撤销失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询预测任务统计信息")
    @PostMapping("/getForecastStatistics")
    public MyJsonBean getForecastStatistics() {
        // 未登录时返回零值统计, 避免前端卡片渲染异常 / 一直停在初始值
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", emptyStatistics());
        }
        try {
            Map<String, Object> statistics = forecastService.getForecastStatistics();
            return MyJsonBean.ok("查询成功", statistics);
        } catch (Exception e) {
            // 登录态字段缺失(如 orgid 取不到)时同样降级为空数据, 前端友好
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
        empty.put("draftCount", 0);
        empty.put("submittedCount", 0);
        empty.put("approvedCount", 0);
        empty.put("rejectedCount", 0);
        return empty;
    }

    @ApiOperation("生成预测数据")
    @PostMapping("/generateForecastData")
    public MyJsonBean generateForecastData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String forecastId = params.get("forecastId");
            if (forecastId == null || forecastId.isEmpty()) {
                return MyJsonBean.errorData("预测任务ID不能为空");
            }

            forecastService.generateForecastData(forecastId);
            return MyJsonBean.ok("生成成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }
}
