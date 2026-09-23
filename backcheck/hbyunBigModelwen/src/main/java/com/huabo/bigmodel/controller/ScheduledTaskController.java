package com.huabo.bigmodel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.ScheduledTask;
import com.huabo.bigmodel.entity.ScheduledTaskLog;
import com.huabo.bigmodel.mapper.ScheduledTaskLogMapper;
import com.huabo.bigmodel.mapper.ScheduledTaskMapper;
import com.huabo.bigmodel.schedule.ScheduledTaskRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI办公定时任务控制器（后端调度）
 * 前端 AI 办公页通过本接口管理任务；到点由调度线程直连 wenxinclaw 网关发送，
 * 浏览器关闭不影响执行。
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/scheduled-task")
@Tag(name = "AI办公定时任务", description = "后端调度的定时任务管理接口")
public class ScheduledTaskController {

    @Autowired
    private ScheduledTaskMapper taskMapper;

    @Autowired
    private ScheduledTaskLogMapper logMapper;

    @Autowired
    private ScheduledTaskRunner taskRunner;

    @Autowired
    private com.huabo.bigmodel.wenxinclaw.WenxinClawGatewayClient gatewayClient;

    @org.springframework.beans.factory.annotation.Value("${wenxinclaw.session-key:agent:main:main}")
    private String sessionKey;

    /**
     * 任务列表（含禁用的，按创建时间倒序；传 userId 时只看该用户自己的任务）
     */
    @GetMapping("/list")
    @Operation(summary = "获取定时任务列表（可按用户过滤）")
    public Result<List<ScheduledTask>> list(
            @RequestParam(required = false) String userId) {
        try {
            LambdaQueryWrapper<ScheduledTask> wrapper = new LambdaQueryWrapper<ScheduledTask>()
                    .orderByDesc(ScheduledTask::getCreateTime);
            if (userId != null && !userId.trim().isEmpty()) {
                wrapper.eq(ScheduledTask::getUserId, userId);
            }
            List<ScheduledTask> list = taskMapper.selectList(wrapper);
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取定时任务列表失败", e);
            return Result.error("获取任务列表失败: " + e.getMessage());
        }
    }

    /**
     * 新建任务（mode/time/datetime/intervalMinutes/taskName 必填性由前端校验）
     */
    @PostMapping("/save")
    @Operation(summary = "新建定时任务")
    public Result<ScheduledTask> save(@RequestBody ScheduledTask task) {
        try {
            if (task.getTaskName() == null || task.getTaskName().trim().isEmpty()) {
                return Result.error("任务描述不能为空");
            }
            task.setId(null);
            task.setTaskName(task.getTaskName().trim());
            task.setStatus(1);
            task.setDone(0);
            task.setLastFireTime(null);
            task.setLastFireDate(null);
            Date now = new Date();
            task.setCreateTime(now);
            task.setUpdateTime(now);
            taskMapper.insert(task);
            log.info("新建定时任务: id={}, mode={}, name={}", task.getId(), task.getMode(), task.getTaskName());
            return Result.success(task);
        } catch (Exception e) {
            log.error("新建定时任务失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新任务（启停/修改）；重新启用"执行一次"且时间已过的任务直接标记完成，避免立即补发
     */
    @PostMapping("/update")
    @Operation(summary = "更新定时任务（启停/修改）")
    public Result<ScheduledTask> update(@RequestBody ScheduledTask task) {
        try {
            if (task.getId() == null || task.getId().isEmpty()) {
                return Result.error("任务ID不能为空");
            }
            if (Integer.valueOf(1).equals(task.getStatus()) && "once".equals(task.getMode())
                    && task.getExecDatetime() != null && !task.getExecDatetime().after(new Date())) {
                task.setDone(1);
            }
            task.setUpdateTime(new Date());
            taskMapper.updateById(task);
            return Result.success(task);
        } catch (Exception e) {
            log.error("更新定时任务失败", e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除任务（同时删除其执行日志）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除定时任务")
    public Result<Void> delete(@PathVariable String id) {
        try {
            taskMapper.deleteById(id);
            logMapper.delete(new LambdaQueryWrapper<ScheduledTaskLog>()
                    .eq(ScheduledTaskLog::getTaskId, id));
            return Result.success();
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 执行日志（按触发时间倒序；可按任务或用户过滤——用户过滤=该用户全部任务的日志）
     */
    @GetMapping("/logs")
    @Operation(summary = "获取定时任务执行日志（可按任务/用户过滤）")
    public Result<List<ScheduledTaskLog>> logs(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        try {
            if (limit == null || limit < 1 || limit > 200) limit = 50;
            LambdaQueryWrapper<ScheduledTaskLog> wrapper = new LambdaQueryWrapper<ScheduledTaskLog>()
                    .orderByDesc(ScheduledTaskLog::getFireTime)
                    .last("LIMIT " + limit);
            if (taskId != null && !taskId.isEmpty()) {
                wrapper.eq(ScheduledTaskLog::getTaskId, taskId);
                return Result.success(logMapper.selectList(wrapper));
            }
            if (userId != null && !userId.trim().isEmpty()) {
                // 该用户的任务ID集合 → 日志按任务过滤（历史遗留无归属任务的日志不展示）
                List<String> taskIds = taskMapper.selectList(
                        new LambdaQueryWrapper<ScheduledTask>()
                                .eq(ScheduledTask::getUserId, userId)
                                .select(ScheduledTask::getId))
                        .stream().map(ScheduledTask::getId).collect(Collectors.toList());
                if (taskIds.isEmpty()) {
                    return Result.success(new java.util.ArrayList<>());
                }
                wrapper.in(ScheduledTaskLog::getTaskId, taskIds);
            }
            return Result.success(logMapper.selectList(wrapper));
        } catch (Exception e) {
            log.error("获取执行日志失败", e);
            return Result.error("获取日志失败: " + e.getMessage());
        }
    }

    /**
     * 单条执行日志详情（含 AI 完整回复，供前端点开查看完整结果）
     */
    @GetMapping("/logs/{id}")
    @Operation(summary = "获取单条执行日志详情（含完整回复）")
    public Result<ScheduledTaskLog> logDetail(@PathVariable String id) {
        try {
            ScheduledTaskLog logEntry = logMapper.selectById(id);
            if (logEntry == null) {
                return Result.error("日志不存在");
            }
            return Result.success(logEntry);
        } catch (Exception e) {
            log.error("获取日志详情失败", e);
            return Result.error("获取日志详情失败: " + e.getMessage());
        }
    }

    /**
     * 立即执行一次（测试/补发用；interval 任务会照常刷新计时）
     */
    @PostMapping("/{id}/fire-now")
    @Operation(summary = "立即执行一次定时任务")
    public Result<Void> fireNow(@PathVariable String id) {
        try {
            ScheduledTask task = taskMapper.selectById(id);
            if (task == null) return Result.error("任务不存在");
            Date now = new Date();
            taskRunner.fire(task, now,
                    (now.getYear() + 1900) + "-" + (now.getMonth() + 1) + "-" + now.getDate());
            return Result.success();
        } catch (Exception e) {
            log.error("手动触发定时任务失败", e);
            return Result.error("触发失败: " + e.getMessage());
        }
    }

    /**
     * 重置 wenxinclaw 网关会话（清空对话上下文）。
     * 用于会话上下文被污染的场景（如 AI 凭历史失败记录拒绝重试）；
     * 注意：会清空该会话的全部对话记忆（前端本地保存的历史记录不受影响）。
     */
    @PostMapping("/reset-gateway-session")
    @Operation(summary = "重置wenxinclaw网关会话（清空对话上下文）")
    public Result<Void> resetGatewaySession() {
        try {
            com.alibaba.fastjson.JSONObject params = new com.alibaba.fastjson.JSONObject();
            params.put("key", sessionKey);
            params.put("reason", "reset");
            com.alibaba.fastjson.JSONObject res = gatewayClient.rpcRequest("sessions.reset", params);
            if (res != null) {
                log.info("网关会话已重置: sessionKey={}", sessionKey);
                return Result.success();
            }
            return Result.error("重置失败（详见服务日志）");
        } catch (Exception e) {
            log.error("重置网关会话失败", e);
            return Result.error("重置失败: " + e.getMessage());
        }
    }
}
