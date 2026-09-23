package com.huabo.etl.utils.quartz;

import com.huabo.etl.service.IKettleJobService;
import com.huabo.etl.service.IKettleTransService;
import com.huabo.etl.utils.SpringContextUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

/**
 * 作业定时任务执行器
 * 因为定时器的job类和kettle的job类名一样，因此这里采用继承{@code org.quartz。InterruptableJob}类
 *
 * @author lyf
 */
@Slf4j
@DisallowConcurrentExecution
public class ScriptQuartz implements InterruptableJob {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 此处无法使用常规注入方式注入bean
        IKettleTransService transService = SpringContextUtil.getBean(IKettleTransService.class);
        IKettleJobService jobService = SpringContextUtil.getBean(IKettleJobService.class);

        // 本次执行时间
        Date lastExecuteTime = jobExecutionContext.getFireTime();
        // 下一次任务时间
        Date nexExecuteTime = jobExecutionContext.getNextFireTime();
        // 运行状态
        boolean runStatus = true;
        // 获取传入过来的作业ID
        String runType = jobExecutionContext.getMergedJobDataMap().getString("runType");
        String scriptId = jobExecutionContext.getMergedJobDataMap().getString("id");
        try {
            log.info("执行定时任务：{} - {} - {}", lastExecuteTime, runType, scriptId);
            if (runType.equals("tran")) {
                transService.runTransRightNow(Long.valueOf(scriptId));
            } else if (runType.equals("job")) {
                jobService.runJobRightNow(Long.valueOf(scriptId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }
}
