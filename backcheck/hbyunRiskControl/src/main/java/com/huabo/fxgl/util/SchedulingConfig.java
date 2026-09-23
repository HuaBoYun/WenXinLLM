package com.huabo.fxgl.util;

import com.huabo.fxgl.service.TblRiskImprovementDetailsService;
import com.huabo.fxgl.service.TblRiskMonthlyEvaluationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 定时任务配置
 * @author Pactera
 *
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Resource
    private TblRiskMonthlyEvaluationService tblRiskMonthlyEvaluationService;

    //@Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
    //@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
    //@Scheduled(cron = “0 10 0 1 * ?”)//每月1号的0:10:00执行
    //@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
    @Scheduled(cron = "0 10 0 15 * ?") // 每月1号的0:10:00执行
    public void getToken() {
        try {
        	tblRiskMonthlyEvaluationService.isReportVersionT();
        } catch (Exception e) {
            log.error("定时任务出错",e);
        }
    }


}