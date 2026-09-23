package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetCollaborationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 协同预算Controller
 * 
 * @description 协同预算接口，支持多人协作、版本管理、评论讨论、任务分配、协作统计
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-协同预算"})
@RequestMapping(value = "/accountant/advanced/collaboration")
@Slf4j
public class BudgetCollaborationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetCollaborationService collaborationService;

    /**
     * 创建协作任务
     */
    @Operation(summary = "创建协作任务")
    @ApiOperation("创建协作任务")
    @PostMapping("/task/create")
    public MyJsonBean<Map<String, Object>> createTask(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> task = collaborationService.createTask(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(task);
        } catch (ServiceException ex) {
            log.error("创建协作任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建协作任务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分配任务
     */
    @Operation(summary = "分配任务")
    @ApiOperation("分配任务")
    @PostMapping("/task/assign")
    public MyJsonBean<Map<String, Object>> assignTask(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> assignment = collaborationService.assignTask(params);
            result.setCode(1);
            result.setMsg("分配成功");
            result.setData(assignment);
        } catch (ServiceException ex) {
            log.error("分配任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分配任务异常", e);
            result.setCode(0);
            result.setMsg("分配失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 添加评论
     */
    @Operation(summary = "添加评论")
    @ApiOperation("添加评论")
    @PostMapping("/comment/add")
    public MyJsonBean<Map<String, Object>> addComment(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> comment = collaborationService.addComment(params);
            result.setCode(1);
            result.setMsg("评论成功");
            result.setData(comment);
        } catch (ServiceException ex) {
            log.error("添加评论失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("添加评论异常", e);
            result.setCode(0);
            result.setMsg("评论失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 版本管理
     */
    @Operation(summary = "版本管理")
    @ApiOperation("版本管理")
    @PostMapping("/version/manage")
    public MyJsonBean<Map<String, Object>> manageVersion(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> version = collaborationService.manageVersion(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(version);
        } catch (ServiceException ex) {
            log.error("版本管理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("版本管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 协作统计
     */
    @Operation(summary = "协作统计")
    @ApiOperation("协作统计")
    @PostMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = collaborationService.getStatistics(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (ServiceException ex) {
            log.error("协作统计失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("协作统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/create/update/delete/archive/export/participants/activities/comments 端点已在 BudgetAdvancedFeaturesController 中实现
}

