package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportTaskQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportTask;
import com.financial.sharing.enterpriseReport.service.ReportTaskService;
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
 * 报表任务Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-报表任务管理")
@RestController
@RequestMapping("/enterpriseReport/reportTask")
public class ReportTaskController {

    @Autowired
    private ReportTaskService reportTaskService;

    /**
     * 查询报表任务列表
     */
    @ApiOperation("查询报表任务列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody ReportTaskQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblReportTask> list = reportTaskService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询报表任务详情
     */
    @ApiOperation("查询报表任务详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }
            
            TblReportTask reportTask = reportTaskService.getDetail(taskId);
            return MyJsonBean.ok(reportTask);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存报表任务
     */
    @ApiOperation("保存报表任务")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblReportTask reportTask) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (reportTask.getTaskCode() == null || reportTask.getTaskCode().isEmpty()) {
                return MyJsonBean.errorData("任务编码不能为空");
            }
            if (reportTask.getTaskName() == null || reportTask.getTaskName().isEmpty()) {
                return MyJsonBean.errorData("任务名称不能为空");
            }
            if (reportTask.getGroupId() == null || reportTask.getGroupId().isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }
            
            boolean result = reportTaskService.saveReportTask(reportTask);
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
     * 删除报表任务
     */
    @ApiOperation("删除报表任务")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }
            
            boolean result = reportTaskService.deleteReportTask(taskId);
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
     * 发布报表任务
     */
    @ApiOperation("发布报表任务")
    @PostMapping("/publish")
    public MyJsonBean publish(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }

            boolean result = reportTaskService.publishTask(taskId);
            if (result) {
                return MyJsonBean.ok("发布成功");
            } else {
                return MyJsonBean.errorData("发布失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("发布失败: " + e.getMessage());
        }
    }

    /**
     * 撤回报表任务
     */
    @ApiOperation("撤回报表任务")
    @PostMapping("/withdraw")
    public MyJsonBean withdraw(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }

            boolean result = reportTaskService.withdrawTask(taskId);
            if (result) {
                return MyJsonBean.ok("撤回成功");
            } else {
                return MyJsonBean.errorData("撤回失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("撤回失败: " + e.getMessage());
        }
    }

    /**
     * 根据表单组ID查询报表任务列表
     */
    @ApiOperation("根据表单组ID查询报表任务列表")
    @PostMapping("/getListByGroupId")
    public MyJsonBean getListByGroupId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String groupId = params.get("groupId");
            if (groupId == null || groupId.isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }

            List<TblReportTask> list = reportTaskService.getListByGroupId(groupId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}


