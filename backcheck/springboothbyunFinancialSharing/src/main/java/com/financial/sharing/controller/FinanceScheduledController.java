package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceScheduledService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务定时任务控制器
 * 处理定时任务管理功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/scheduled/task")
@CrossOrigin
public class FinanceScheduledController {

    @Autowired
    private FinanceScheduledService financeScheduledService;

    /**
     * 获取定时任务列表
     */
    @GetMapping("/list")
    public MyJsonBean getScheduledTaskList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeScheduledService.getScheduledTaskList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取定时任务列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除定时任务
     */
    @GetMapping("/remove")
    public MyJsonBean deleteScheduledTask(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeScheduledService.deleteScheduledTask(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 修改定时任务状态
     */
    @PostMapping("/status")
    public MyJsonBean changeScheduledTaskStatus(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeScheduledService.changeScheduledTaskStatus(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("修改定时任务状态失败", e);
            return MyJsonBean.errorData("修改状态失败: " + e.getMessage());
        }
    }

    /**
     * 保存定时任务
     */
    @PostMapping("/add")
    public MyJsonBean saveScheduledTask(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeScheduledService.saveScheduledTask(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存定时任务失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 修改定时任务
     */
    @PostMapping("/modify")
    public MyJsonBean updateScheduledTask(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeScheduledService.updateScheduledTask(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("修改定时任务失败", e);
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 获取定时任务详情
     */
    @GetMapping("/detail")
    public MyJsonBean getScheduledTaskDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeScheduledService.getScheduledTaskDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取定时任务详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}