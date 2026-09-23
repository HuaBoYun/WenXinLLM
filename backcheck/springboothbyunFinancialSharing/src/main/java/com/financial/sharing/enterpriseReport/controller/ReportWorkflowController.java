package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportWorkflowQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportWorkflow;
import com.financial.sharing.enterpriseReport.service.ReportWorkflowService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 报表工作流Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-报表工作流管理")
@RestController
@RequestMapping("/enterpriseReport/reportWorkflow")
public class ReportWorkflowController {

    @Autowired
    private ReportWorkflowService reportWorkflowService;

    /**
     * 查询报表工作流列表
     */
    @ApiOperation("查询报表工作流列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody ReportWorkflowQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblReportWorkflow> list = reportWorkflowService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询报表工作流详情
     */
    @ApiOperation("查询报表工作流详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String workflowId = params.get("workflowId");
            if (workflowId == null || workflowId.isEmpty()) {
                return MyJsonBean.errorData("流程ID不能为空");
            }
            
            TblReportWorkflow reportWorkflow = reportWorkflowService.getDetail(workflowId);
            return MyJsonBean.ok(reportWorkflow);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存报表工作流
     */
    @ApiOperation("保存报表工作流")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblReportWorkflow reportWorkflow) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (reportWorkflow.getWorkflowCode() == null || reportWorkflow.getWorkflowCode().isEmpty()) {
                return MyJsonBean.errorData("流程编码不能为空");
            }
            if (reportWorkflow.getWorkflowName() == null || reportWorkflow.getWorkflowName().isEmpty()) {
                return MyJsonBean.errorData("流程名称不能为空");
            }
            if (reportWorkflow.getTaskId() == null || reportWorkflow.getTaskId().isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }
            if (reportWorkflow.getWorkflowType() == null || reportWorkflow.getWorkflowType().isEmpty()) {
                return MyJsonBean.errorData("流程类型不能为空");
            }
            
            boolean result = reportWorkflowService.saveReportWorkflow(reportWorkflow);
            if (result) {
                return MyJsonBean.ok("保存成功");
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除报表工作流
     */
    @ApiOperation("删除报表工作流")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String workflowId = params.get("workflowId");
            if (workflowId == null || workflowId.isEmpty()) {
                return MyJsonBean.errorData("流程ID不能为空");
            }
            
            boolean result = reportWorkflowService.deleteReportWorkflow(workflowId);
            if (result) {
                return MyJsonBean.ok("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据任务ID查询报表工作流列表
     */
    @ApiOperation("根据任务ID查询报表工作流列表")
    @PostMapping("/getListByTaskId")
    public MyJsonBean getListByTaskId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }

            List<TblReportWorkflow> list = reportWorkflowService.getListByTaskId(taskId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新工作流状态
     */
    @ApiOperation("更新工作流状态")
    @PostMapping("/updateStatus")
    public MyJsonBean updateStatus(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String workflowId = params.get("workflowId");
            String status = params.get("status");

            if (workflowId == null || workflowId.isEmpty()) {
                return MyJsonBean.errorData("流程ID不能为空");
            }
            if (status == null || status.isEmpty()) {
                return MyJsonBean.errorData("状态不能为空");
            }

            boolean result = reportWorkflowService.updateWorkflowStatus(workflowId, status);
            if (result) {
                return MyJsonBean.ok("更新成功");
            } else {
                return MyJsonBean.errorData("更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }
}


