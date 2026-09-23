package com.huabo.bigmodel.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.bigmodel.entity.ScheduledTask;
import com.huabo.bigmodel.entity.ScheduledTaskLog;
import com.huabo.bigmodel.mapper.ScheduledTaskLogMapper;
import com.huabo.bigmodel.mapper.ScheduledTaskMapper;
import com.huabo.bigmodel.wenxinclaw.WenxinClawGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AI办公定时任务调度线程
 *
 * 每 30 秒检查一次 ai_scheduled_task 中的启用任务（语义与前端版一致）：
 * - daily：当前 HH:mm 等于设定时间且今日未触发过
 * - once：到达设定时间点（触发后置 done=1）
 * - interval：距上次触发（或创建时间）超过设定分钟数
 * 到点后：先更新任务触发记录（防止重复触发），再异步直连 wenxinclaw 网关发送，
 * AI 回复摘要回写到 ai_scheduled_task_log。
 */
@Slf4j
@Component
public class ScheduledTaskRunner {

    @Autowired
    private ScheduledTaskMapper taskMapper;

    @Autowired
    private ScheduledTaskLogMapper logMapper;

    @Autowired
    private WenxinClawGatewayClient gatewayClient;

    @Autowired
    private ScheduledLoginService loginService;

    @Value("${scheduled-task.enabled:true}")
    private boolean enabled;

    /** 发送线程（单线程足够：任务频率低，串行避免并发会话混乱） */
    private final ExecutorService sender = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "scheduled-task-sender");
        t.setDaemon(true);
        return t;
    });

    /**
     * 启动时清扫"重启孤儿"：服务重启会杀掉在途发送线程，此类日志永远停在 sent——
     * 超过5分钟仍是 sent 的标记为 error 并提示可重新执行，避免用户无限等待
     */
    @PostConstruct
    public void markOrphanedSends() {
        try {
            Date threshold = new Date(System.currentTimeMillis() - 5 * 60 * 1000L);
            List<ScheduledTaskLog> orphans = logMapper.selectList(
                    new LambdaQueryWrapper<ScheduledTaskLog>()
                            .eq(ScheduledTaskLog::getStatus, "sent")
                            .lt(ScheduledTaskLog::getFireTime, threshold));
            for (ScheduledTaskLog orphan : orphans) {
                ScheduledTaskLog update = new ScheduledTaskLog();
                update.setId(orphan.getId());
                update.setStatus("error");
                update.setErrorMsg("服务重启导致本轮回复丢失，可重新执行获取结果");
                logMapper.updateById(update);
            }
            if (!orphans.isEmpty()) {
                log.warn("[定时任务] 已标记 {} 条重启前中断的发送记录", orphans.size());
            }
        } catch (Exception e) {
            log.warn("[定时任务] 清扫中断发送记录失败: {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "${scheduled-task.check-interval-ms:30000}")
    public void checkAndFire() {
        if (!enabled) return;
        try {
            List<ScheduledTask> tasks = taskMapper.selectList(
                    new LambdaQueryWrapper<ScheduledTask>().eq(ScheduledTask::getStatus, 1));
            if (tasks == null || tasks.isEmpty()) return;

            Date now = new Date();
            String todayStr = todayStr(now);
            String nowHHmm = hhmm(now);
            for (ScheduledTask task : tasks) {
                try {
                    if ("once".equals(task.getMode()) && Integer.valueOf(1).equals(task.getDone())) continue;
                    if (isDue(task, now, todayStr, nowHHmm)) {
                        fire(task, now, todayStr);
                    }
                } catch (Exception e) {
                    log.error("[定时任务] 任务判定/触发异常 taskId={}", task.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("[定时任务] 调度检查异常", e);
        }
    }

    private boolean isDue(ScheduledTask task, Date now, String todayStr, String nowHHmm) {
        String mode = task.getMode();
        if ("once".equals(mode)) {
            return task.getExecDatetime() != null && !now.before(task.getExecDatetime());
        }
        if ("daily".equals(mode)) {
            return nowHHmm.equals(task.getExecTime()) && !todayStr.equals(task.getLastFireDate());
        }
        if ("interval".equals(mode)) {
            int minutes = task.getIntervalMinutes() != null ? task.getIntervalMinutes() : 0;
            if (minutes <= 0) return false;
            Date base = task.getLastFireTime() != null ? task.getLastFireTime() : task.getCreateTime();
            if (base == null) return false;
            return now.getTime() - base.getTime() >= minutes * 60L * 1000L;
        }
        return false;
    }

    /**
     * 触发任务：先落触发记录（防重），再异步发送，回复/错误回写日志。
     * 供调度线程与"立即执行"接口共用。
     */
    public void fire(ScheduledTask task, Date now, String todayStr) {
        // 0. 触发前按创建人登录账号代登录换取新鲜 token（移植主平台登录流程、无密码校验），
        //    任务以创建人身份执行且凭证不过期；旧任务无账号或代登录失败时回退存量 token 快照
        try {
            String freshToken = loginService.loginForToken(task.getUserName());
            if (freshToken != null) {
                task.setUserToken(freshToken);
            } else if (task.getUserName() != null && !task.getUserName().trim().isEmpty()) {
                log.warn("[定时任务] 代登录未取得 token，回退任务存量 token taskId={}", task.getId());
            }
        } catch (Exception e) {
            log.warn("[定时任务] 代登录异常，回退任务存量 token taskId={}", task.getId(), e);
        }

        // 1. 先更新任务状态，防止下一轮（30秒后）重复触发；代登录成功时顺带持久化新 token，
        //    让 user_token 回退快照始终是最近一次的有效凭证
        ScheduledTask update = new ScheduledTask();
        update.setId(task.getId());
        update.setLastFireTime(now);
        update.setLastFireDate(todayStr);
        if ("once".equals(task.getMode())) update.setDone(1);
        if (task.getUserToken() != null && !task.getUserToken().isEmpty()) {
            update.setUserToken(task.getUserToken());
        }
        update.setUpdateTime(new Date());
        taskMapper.updateById(update);
        // 同步内存对象，避免同轮重复判断
        task.setLastFireTime(now);
        task.setLastFireDate(todayStr);
        if ("once".equals(task.getMode())) task.setDone(1);

        // 2. 写执行日志（初始 sent）
        ScheduledTaskLog taskLog = new ScheduledTaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setTaskDesc(task.getTaskName());
        taskLog.setFireTime(now);
        taskLog.setStatus("sent");
        taskLog.setCreateTime(new Date());
        logMapper.insert(taskLog);

        // 3. 异步发送（不阻塞调度线程；AI 回复可能耗时数分钟）
        sender.submit(() -> {
            WenxinClawGatewayClient.SendResult result;
            try {
                result = gatewayClient.sendMessage(buildMessage(task));
            } catch (Exception e) {
                log.error("[定时任务] 发送异常 taskId={}", task.getId(), e);
                result = new WenxinClawGatewayClient.SendResult();
                result.error = e.getMessage();
            }
            ScheduledTaskLog logUpdate = new ScheduledTaskLog();
            logUpdate.setId(taskLog.getId());
            if (result.success) {
                logUpdate.setStatus("responded");
                String full = result.responseText != null ? result.responseText : "";
                // 摘要用于列表展示（防御性截500字），全文走 CLOB 参数绑定存 RESPONSE_CONTENT
                logUpdate.setResponseExcerpt(full.length() > 500 ? full.substring(0, 500) : full);
                logUpdate.setResponseContent(full);
                log.info("[定时任务] 执行成功 taskId={} 回复{}字符", task.getId(), full.length());
            } else {
                logUpdate.setStatus("failed");
                logUpdate.setErrorMsg(result.error);
                log.warn("[定时任务] 执行失败 taskId={} 原因: {}", task.getId(), result.error);
            }
            try {
                logMapper.updateById(logUpdate);
            } catch (Exception updateEx) {
                // 历史教训：此处异常曾被线程池静默吞掉，日志永远停在 sent —— 必须显式记录并降级重试
                log.error("[定时任务] 执行结果回写失败 taskId={} logId={}", task.getId(), taskLog.getId(), updateEx);
                try {
                    ScheduledTaskLog fallback = new ScheduledTaskLog();
                    fallback.setId(taskLog.getId());
                    fallback.setStatus(result.success ? "responded" : "failed");
                    fallback.setResponseExcerpt("结果回写异常：" + updateEx.getMessage());
                    logMapper.updateById(fallback);
                } catch (Exception fallbackEx) {
                    log.error("[定时任务] 降级回写仍失败 logId={}", taskLog.getId(), fallbackEx);
                }
            }
        });
    }

    /** 构造发送文本（对齐前端：模块上下文 + 用户 token 前缀） */
    private String buildMessage(ScheduledTask task) {
        StringBuilder sb = new StringBuilder();
        if (task.getUserToken() != null && !task.getUserToken().isEmpty()) {
            sb.append("[USER_TOKEN: ").append(task.getUserToken()).append("]\n\n");
            // AI 会凭会话记忆中某次鉴权失败直接拒绝且不重试（实测：token 已刷新仍回"Token 未变（过期）"），
            // 必须显式要求它实调验证，不得引用历史结论
            sb.append("（注意：以上 token 是用户当前的登录凭证，每次触发都会刷新。")
                    .append("请直接使用它调用所需接口完成本次任务，")
                    .append("禁止根据对话中以往轮次的鉴权失败直接判定其过期；")
                    .append("若调用确被拒绝，请实际重试一次并在回复中给出具体的接口返回错误。）\n\n");
        } else {
            // 定时任务是一次性对话，用户无法回复 token；索要 token 只会产生无效轮次。
            // 明确告知 AI 凭证缺失的原因与应有的输出，便于用户在执行记录里直接定位是配置问题
            sb.append("（系统提示：本次任务未能获取到用户登录凭证（按创建人账号代登录失败或任务未配置登录账号）。")
                    .append("请勿向用户索要 token，也无需调用需要鉴权的接口；")
                    .append("请在回复开头输出 [NO_TOKEN]，并简要说明：定时任务代登录失败，")
                    .append("需要管理员检查 hbyunBigModelwen 的 scheduled-login 配置（token-key、Redis 连接）与任务的登录账号。）\n\n");
        }
        if (task.getModuleIdentification() != null && !task.getModuleIdentification().isEmpty()) {
            sb.append("[当前操作模块：").append(task.getModuleName() == null ? "" : task.getModuleName())
                    .append("（").append(task.getModuleIdentification()).append("）]\n\n");
        }
        sb.append(task.getTaskName() == null ? "" : task.getTaskName());
        return sb.toString();
    }

    private String todayStr(Date now) {
        // 与前端判定格式一致：yyyy-M-d（避免补零不一致导致 daily 重复触发）
        return (now.getYear() + 1900) + "-" + (now.getMonth() + 1) + "-" + now.getDate();
    }

    private String hhmm(Date now) {
        return String.format("%02d:%02d", now.getHours(), now.getMinutes());
    }

    @PreDestroy
    public void shutdown() {
        sender.shutdown();
    }
}
