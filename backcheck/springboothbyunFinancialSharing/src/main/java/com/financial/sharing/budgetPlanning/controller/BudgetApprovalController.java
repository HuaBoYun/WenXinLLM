package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetApprovalQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetApproval;
import com.financial.sharing.budgetPlanning.service.BudgetApprovalService;
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
 * 预算数据审批Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Api(tags = "计划预算-预算数据审批")
@RestController
@RequestMapping("/budgetPlanning/budgetApproval")
public class BudgetApprovalController {

    @Autowired
    private BudgetApprovalService approvalService;

    @ApiOperation("提交审批")
    @PostMapping("/submitApproval")
    public MyJsonBean submitApproval(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            String approverIds = params.get("approverIds");
            String comment = params.get("comment");
            
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            if (approverIds == null || approverIds.isEmpty()) {
                return MyJsonBean.errorData("审批人不能为空");
            }
            
            approvalService.submitApproval(dataId, approverIds, comment);
            return MyJsonBean.ok("提交审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("提交审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批通过")
    @PostMapping("/approveData")
    public MyJsonBean approveData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String approvalId = params.get("approvalId");
            String comment = params.get("comment");
            
            if (approvalId == null || approvalId.isEmpty()) {
                return MyJsonBean.errorData("审批ID不能为空");
            }
            
            approvalService.approveData(approvalId, comment);
            return MyJsonBean.ok("审批通过成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("审批通过失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批驳回")
    @PostMapping("/rejectData")
    public MyJsonBean rejectData(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String approvalId = params.get("approvalId");
            String comment = params.get("comment");
            
            if (approvalId == null || approvalId.isEmpty()) {
                return MyJsonBean.errorData("审批ID不能为空");
            }
            if (comment == null || comment.isEmpty()) {
                return MyJsonBean.errorData("驳回原因不能为空");
            }
            
            approvalService.rejectData(approvalId, comment);
            return MyJsonBean.ok("审批驳回成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("审批驳回失败: " + e.getMessage());
        }
    }

    @ApiOperation("撤销审批")
    @PostMapping("/cancelApproval")
    public MyJsonBean cancelApproval(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            String comment = params.get("comment");
            
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            
            approvalService.cancelApproval(dataId, comment);
            return MyJsonBean.ok("撤销审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("撤销审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询审批记录列表(分页)")
    @PostMapping("/getApprovalList")
    public MyJsonBean getApprovalList(@RequestBody BudgetApprovalQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetApproval> pageInfo = approvalService.getApprovalList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询审批历史")
    @PostMapping("/getApprovalHistory")
    public MyJsonBean getApprovalHistory(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            
            List<TblBudgetApproval> list = approvalService.getApprovalHistory(dataId);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询我的待审批列表(分页)")
    @PostMapping("/getMyPendingList")
    public MyJsonBean getMyPendingList(@RequestBody BudgetApprovalQueryParam param) {
        // 未登录时返回空分页结果，避免前端列表渲染异常
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", new PageInfo<>(java.util.Collections.emptyList()));
        }
        try {
            PageInfo<TblBudgetApproval> pageInfo = approvalService.getMyPendingList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            // 登录态字段缺失（如 orgid 取不到）时同样降级为空数据，前端友好
            if (isSessionExpired(e)) {
                return MyJsonBean.ok("查询成功", new PageInfo<>(java.util.Collections.emptyList()));
            }
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询我的已审批列表(分页)")
    @PostMapping("/getMyApprovedList")
    public MyJsonBean getMyApprovedList(@RequestBody BudgetApprovalQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetApproval> pageInfo = approvalService.getMyApprovedList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批通过")
    @PostMapping("/batchApprove")
    public MyJsonBean batchApprove(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> approvalIds = (List<String>) params.get("approvalIds");
            String comment = (String) params.get("comment");

            if (approvalIds == null || approvalIds.isEmpty()) {
                return MyJsonBean.errorData("审批ID列表不能为空");
            }

            approvalService.batchApprove(approvalIds, comment);
            return MyJsonBean.ok("批量审批通过成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("批量审批通过失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批驳回")
    @PostMapping("/batchReject")
    public MyJsonBean batchReject(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            @SuppressWarnings("unchecked")
            List<String> approvalIds = (List<String>) params.get("approvalIds");
            String comment = (String) params.get("comment");

            if (approvalIds == null || approvalIds.isEmpty()) {
                return MyJsonBean.errorData("审批ID列表不能为空");
            }
            if (comment == null || comment.isEmpty()) {
                return MyJsonBean.errorData("驳回原因不能为空");
            }

            approvalService.batchReject(approvalIds, comment);
            return MyJsonBean.ok("批量审批驳回成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("批量审批驳回失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询审批统计信息")
    @PostMapping("/getApprovalStatistics")
    public MyJsonBean getApprovalStatistics() {
        // 未登录时返回零值统计，避免前端统计卡片渲染异常
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", emptyStatistics());
        }
        try {
            Map<String, Object> statistics = approvalService.getApprovalStatistics();
            return MyJsonBean.ok("查询成功", statistics);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.ok("查询成功", emptyStatistics());
            }
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 识别 UserUtils.requireXxx() 抛出的"用户未登录或会话已失效"异常。
     * 用于 controller 把这种登录态缺失场景降级为空业务数据，提供前端友好响应。
     */
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
        empty.put("pendingCount", 0);
        empty.put("approvedCount", 0);
        empty.put("submitCount", 0);
        return empty;
    }
}
