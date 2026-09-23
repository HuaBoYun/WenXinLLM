package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetAdjustmentQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustment;
import com.financial.sharing.budgetPlanning.service.BudgetAdjustmentService;
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
 * 预算调整Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Api(tags = "计划预算-预算调整管理")
@RestController
@RequestMapping("/budgetPlanning/budgetAdjustment")
public class BudgetAdjustmentController {

    @Autowired
    private BudgetAdjustmentService adjustmentService;

    @ApiOperation("创建调整单")
    @PostMapping("/createAdjustment")
    public MyJsonBean createAdjustment(@RequestBody TblBudgetAdjustment adjustment) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            adjustmentService.createAdjustment(adjustment);
            return MyJsonBean.ok("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改调整单")
    @PostMapping("/updateAdjustment")
    public MyJsonBean updateAdjustment(@RequestBody TblBudgetAdjustment adjustment) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            adjustmentService.updateAdjustment(adjustment);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询调整单列表(分页)")
    @PostMapping("/getAdjustmentList")
    public MyJsonBean getAdjustmentList(@RequestBody BudgetAdjustmentQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetAdjustment> pageInfo = adjustmentService.getAdjustmentList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询调整单")
    @PostMapping("/getAdjustmentById")
    public MyJsonBean getAdjustmentById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }
            
            TblBudgetAdjustment adjustment = adjustmentService.getAdjustmentById(adjustmentId);
            return MyJsonBean.ok("查询成功", adjustment);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询调整单(包含明细)")
    @PostMapping("/getAdjustmentWithDetails")
    public MyJsonBean getAdjustmentWithDetails(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }
            
            TblBudgetAdjustment adjustment = adjustmentService.getAdjustmentWithDetails(adjustmentId);
            return MyJsonBean.ok("查询成功", adjustment);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除调整单")
    @PostMapping("/deleteAdjustment")
    public MyJsonBean deleteAdjustment(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }
            
            adjustmentService.deleteAdjustment(adjustmentId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除调整单")
    @PostMapping("/batchDeleteAdjustment")
    public MyJsonBean batchDeleteAdjustment(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> adjustmentIds = (List<String>) params.get("adjustmentIds");
            
            if (adjustmentIds == null || adjustmentIds.isEmpty()) {
                return MyJsonBean.errorData("调整单ID列表不能为空");
            }
            
            adjustmentService.batchDeleteAdjustment(adjustmentIds);
            return MyJsonBean.ok("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("提交调整单")
    @PostMapping("/submitAdjustment")
    public MyJsonBean submitAdjustment(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }

            adjustmentService.submitAdjustment(adjustmentId);
            return MyJsonBean.ok("提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批调整单")
    @PostMapping("/approveAdjustment")
    public MyJsonBean approveAdjustment(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = (String) params.get("adjustmentId");
            Boolean approved = (Boolean) params.get("approved");
            String opinion = (String) params.get("opinion");

            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }
            if (approved == null) {
                return MyJsonBean.errorData("审批结果不能为空");
            }

            adjustmentService.approveAdjustment(adjustmentId, approved, opinion);
            return MyJsonBean.ok("审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行调整")
    @PostMapping("/executeAdjustment")
    public MyJsonBean executeAdjustment(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }

            adjustmentService.executeAdjustment(adjustmentId);
            return MyJsonBean.ok("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("撤销调整单")
    @PostMapping("/withdrawAdjustment")
    public MyJsonBean withdrawAdjustment(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String adjustmentId = params.get("adjustmentId");
            if (adjustmentId == null || adjustmentId.isEmpty()) {
                return MyJsonBean.errorData("调整单ID不能为空");
            }

            adjustmentService.withdrawAdjustment(adjustmentId);
            return MyJsonBean.ok("撤销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("撤销失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询调整单统计信息")
    @PostMapping("/getAdjustmentStatistics")
    public MyJsonBean getAdjustmentStatistics() {
        // 未登录时返回零值统计, 避免前端卡片渲染异常 / 一直停在初始值
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", emptyStatistics());
        }
        try {
            Map<String, Object> statistics = adjustmentService.getAdjustmentStatistics();
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
        empty.put("executedCount", 0);
        return empty;
    }
}

