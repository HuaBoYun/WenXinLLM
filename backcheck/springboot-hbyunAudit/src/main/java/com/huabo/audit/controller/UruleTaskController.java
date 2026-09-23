package com.huabo.audit.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblUruleTask;
import com.huabo.audit.service.ScheduledTaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 规则引擎任务控制器
 * <p>提供URule规则引擎的任务执行和管理接口</p>
 *
 * @author hbyun
 */
@RestController
public class UruleTaskController {

    private static final Logger log = LoggerFactory.getLogger(UruleTaskController.class);

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public String TimeTask(){
        String url = "http://192.0.2.200:8787/rule/ceshi";
        // 使用getForObject方法获取远程接口返回的数据，返回值类型根据实际接口返回内容确定，这里假设返回字符串
        String result = restTemplate.getForObject(url, String.class);
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化为字符串（可选）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        System.out.println("访问成功"+formattedDateTime);
        return result;
    }

    @Resource
    private ScheduledTaskService scheduledTaskService;

    @OperationLog(
            success = "定时任务新增",
            busType = "整改追责",
            fail = "定时任务新增",
            operationType = OperationType.ADD,
            subType = "新增定时任务，定时执行Urule规则"
    )
    @PostMapping(value = "/task/add",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-新增")
    public JsonBean task_add(HttpServletRequest request, HttpServletResponse response, TblUruleTask tut,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return this.scheduledTaskService.add(tut);
    }

    @OperationLog(
            success = "定时任务修改",
            busType = "整改追责",
            fail = "定时任务修改",
            operationType = OperationType.UPDATE,
            subType = "修改定时任务，修改需要定时执行Urule的执行规则"
    )
    @PostMapping(value = "/task/modify",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-修改")
    public JsonBean task_modify(HttpServletRequest request, HttpServletResponse response,TblUruleTask tst
    ,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return this.scheduledTaskService.modify(tst);
    }

    @OperationLog(
            success = "定时任务删除",
            busType = "整改追责",
            fail = "定时任务删除",
            operationType = OperationType.UPDATE,
            subType = "删除指定需要定时执行的Urule规则【{{taskId}}】"
    )
    @GetMapping(value = "/task/remove",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-删除")
    public JsonBean task_remove(HttpServletRequest request, HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId) throws Exception {
        return this.scheduledTaskService.delete(taskId);
    }

    @OperationLog(
            success = "定时任务详情",
            busType = "整改追责",
            fail = "定时任务详情",
            operationType = OperationType.UPDATE,
            subType = "获取指定需要定时执行的Urule规则的详细信息"
    )
    @GetMapping(value = "/task/detail",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-获取详情")
    public JsonBean task_detail(HttpServletRequest request, HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId) throws Exception {
        return this.scheduledTaskService.getOne(taskId);
    }

    @OperationLog(
            success = "定时任务列表页",
            busType = "整改追责",
            fail = "定时任务列表页",
            operationType = OperationType.SELECT,
            subType = "获取定时任务列表页相关信息"
    )
    @GetMapping(value = "/task/list",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-分页列表")
    public JsonBean task_list(HttpServletRequest request, HttpServletResponse response,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name="pageNumber",description="当前页数",required=true)@RequestParam(value = "pageNumber",required = true) Integer pageNumber,
                              @Parameter(name="pageSize",description="每页数量",required=true)@RequestParam(value = "pageSize",required = true)Integer pageSize,
                              @Parameter(name="taskName",description="筛选条件-任务名称",required=false)@RequestParam(value = "taskName",required = false,defaultValue = "")String taskName
                              ) throws Exception {
        JsonBean getlist = scheduledTaskService.getlist(pageNumber, pageSize, taskName);
        return getlist;
    }

    @OperationLog(
            success = "定时任务列启用弃用",
            busType = "整改追责",
            fail = "定时任务列启用弃用",
            operationType = OperationType.UPDATE,
            subType = "修改定时任务启用弃用状态"
    )
    @PostMapping(value = "/task/status",produces = "application/json; charset=utf-8")
    @Operation(summary = "定时任务-启用弃用")
    public JsonBean task_status(HttpServletRequest request, HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId,
                                @Parameter(name="status",description="状态: 1启用, 0停用",required=true)@RequestParam(value = "status",required = true)Integer status
    ) throws Exception {
        return this.scheduledTaskService.task_status(taskId,status);
    }
}
