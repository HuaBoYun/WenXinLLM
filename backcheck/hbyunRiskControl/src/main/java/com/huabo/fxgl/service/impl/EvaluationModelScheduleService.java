package com.huabo.fxgl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huabo.fxgl.entity.TblEvaluationModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 评估模型动态调度服务
 * 根据模型的SCHEDULE_CONFIG配置动态创建和管理定时任务
 * 
 * @author AI Assistant
 * @date 2025-11-11
 */
@Slf4j
@Service
public class EvaluationModelScheduleService {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private EvaluationModelServiceImpl evaluationModelService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 存储模型ID与定时任务的映射关系
     * Key: 模型ID (evalModelId)
     * Value: ScheduledFuture对象
     */
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    /**
     * 启动模型的定时任务
     * 
     * @param model 评估模型
     * @param currentUser 当前用户
     */
    public void scheduleModelTask(TblEvaluationModel model, String currentUser) {
        try {
            String modelId = model.getEvalModelId();
            String scheduleConfigJson = model.getScheduleConfig();

            log.info("准备启动模型定时任务，模型ID: {}, 模型名称: {}", modelId, model.getModelName());

            // 1. 先取消已存在的任务
            cancelModelTask(modelId);

            // 2. 解析定时任务配置
            if (scheduleConfigJson == null || scheduleConfigJson.trim().isEmpty()) {
                log.warn("模型定时任务配置为空，跳过调度，模型ID: {}", modelId);
                return;
            }

            Map<String, Object> scheduleConfig = objectMapper.readValue(scheduleConfigJson, Map.class);
            String cronExpression = (String) scheduleConfig.get("cronExpression");

            if (cronExpression == null || cronExpression.trim().isEmpty()) {
                log.warn("Cron表达式为空，跳过调度，模型ID: {}", modelId);
                return;
            }

            log.info("模型定时任务配置 - 模型ID: {}, Cron表达式: {}, 配置: {}", 
                    modelId, cronExpression, scheduleConfig);

            // 3. 创建定时任务
            // 🔧 修复：定时任务执行时，从数据库重新查询最新的模型信息，避免使用过期的模型对象
            Runnable task = () -> {
                try {
                    log.info("⏰ 定时任务触发 - 模型ID: {}", modelId);

                    // 🔧 关键修复：从数据库重新查询最新的模型信息
                    TblEvaluationModel latestModel = evaluationModelService.getById(modelId);
                    if (latestModel == null) {
                        log.warn("⚠️ 定时任务执行失败：模型不存在 - 模型ID: {}", modelId);
                        return;
                    }

                    // 检查模型状态是否仍然是RUNNING
                    if (!"RUNNING".equals(latestModel.getStatus())) {
                        log.warn("⚠️ 定时任务执行失败：模型状态不是RUNNING - 模型ID: {}, 当前状态: {}",
                                modelId, latestModel.getStatus());
                        return;
                    }

                    log.info("✅ 开始执行定时任务 - 模型ID: {}, 模型名称: {}", modelId, latestModel.getModelName());

                    // 🔧 关键：只执行当前模型，不执行其他模型
                    evaluationModelService.generateRiskWarningForModel(latestModel, "SYSTEM");

                    log.info("✅ 定时任务执行完成 - 模型ID: {}, 模型名称: {}", modelId, latestModel.getModelName());
                } catch (Exception e) {
                    log.error("❌ 定时任务执行失败 - 模型ID: {}, 错误: {}", modelId, e.getMessage(), e);
                }
            };

            // 4. 使用Cron表达式调度任务
            ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, new CronTrigger(cronExpression));
            scheduledTasks.put(modelId, scheduledFuture);

            log.info("模型定时任务启动成功 - 模型ID: {}, Cron表达式: {}", modelId, cronExpression);

        } catch (Exception e) {
            log.error("启动模型定时任务失败 - 模型ID: {}, 错误: {}", 
                    model.getEvalModelId(), e.getMessage(), e);
        }
    }

    /**
     * 取消模型的定时任务
     * 
     * @param modelId 模型ID
     */
    public void cancelModelTask(String modelId) {
        try {
            ScheduledFuture<?> scheduledFuture = scheduledTasks.get(modelId);
            if (scheduledFuture != null) {
                boolean cancelled = scheduledFuture.cancel(false);
                scheduledTasks.remove(modelId);
                log.info("取消模型定时任务 - 模型ID: {}, 取消结果: {}", modelId, cancelled);
            } else {
                log.debug("模型定时任务不存在，无需取消 - 模型ID: {}", modelId);
            }
        } catch (Exception e) {
            log.error("取消模型定时任务失败 - 模型ID: {}, 错误: {}", modelId, e.getMessage(), e);
        }
    }

    /**
     * 检查模型定时任务是否正在运行
     * 
     * @param modelId 模型ID
     * @return true-正在运行，false-未运行
     */
    public boolean isTaskRunning(String modelId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(modelId);
        return scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone();
    }

    /**
     * 获取当前运行中的任务数量
     * 
     * @return 运行中的任务数量
     */
    public int getRunningTaskCount() {
        return (int) scheduledTasks.values().stream()
                .filter(future -> !future.isCancelled() && !future.isDone())
                .count();
    }

    /**
     * 取消所有定时任务
     */
    public void cancelAllTasks() {
        log.info("准备取消所有模型定时任务，当前任务数: {}", scheduledTasks.size());
        
        scheduledTasks.forEach((modelId, future) -> {
            try {
                future.cancel(false);
                log.info("已取消模型定时任务 - 模型ID: {}", modelId);
            } catch (Exception e) {
                log.error("取消模型定时任务失败 - 模型ID: {}, 错误: {}", modelId, e.getMessage());
            }
        });
        
        scheduledTasks.clear();
        log.info("所有模型定时任务已取消");
    }
}

