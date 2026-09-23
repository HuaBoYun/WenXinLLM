package com.huabo.es.service.trans;

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.tran.DataStream;
import org.frameworkset.tran.ExportResultHandler;
import org.frameworkset.tran.config.ImportBuilder;
import org.frameworkset.tran.metrics.TaskMetrics;
import org.frameworkset.tran.plugin.db.input.DBInputConfig;
import org.frameworkset.tran.plugin.es.output.ElasticsearchOutputConfig;
import org.frameworkset.tran.schedule.CallInterceptor;
import org.frameworkset.tran.schedule.ImportIncreamentConfig;
import org.frameworkset.tran.schedule.TaskContext;
import org.frameworkset.tran.task.TaskCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName : OutErRuleDataTran
 * @Description : 外规数据导入
 * @Author : zhibo.cao
 * @Date: 2023-03-04 23:44:35
 */
@Component("outerRuleDataTran")
@Order(9)
public class OuterRuleDataTran implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(OuterRuleDataTran.class);
    @Value("${spring.elasticsearch.bboss.db.name}")
    private String dbName;

    @Override
    public void run(String... args) throws Exception {
        ImportBuilder importBuilder = ImportBuilder.newInstance();
        //清除测试表,导入的时候回重建表，测试的时候加上为了看测试效果，实际线上环境不要删表
        String outerrule = "outerrule";
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/ESRuleMapper.xml");
        if (clientUtil.existIndice(outerrule)) {
            clientUtil.dropIndice(outerrule);
            clientUtil.createIndiceMapping(outerrule, "createOuterRuleIndice");
        }

        // db相关配置
        DBInputConfig dbInputConfig = new DBInputConfig();
        dbInputConfig.setDbName(dbName);
        dbInputConfig.setSql("select i.OUTRULID,i.RULENAME,i.PUBLISHORG,i.PUBLISHDATE,i.RULENUMBER,i.MEMO,i.OUTRULETYPE,i.EFFECTIVELEVEL,i.TIMELINESS,i.TAKEEFFECTTIME,i.SUMMARYINFO,i.ENTERINGPERSON,i.ENTERINGTIME,i.STATUS,i.BODYINFO,i.RULECODE,i.CREATEORGID,i.APPLYAREA,i.ORGID,i.ISPYTHONFLAG" +
                " ,it.ATTID from TBL_OUTERRULE i left join TBL_OUTERRULE_ATT it on i.OUTRULID = it.OUTRULID" +
                " where i.OUTRULID > #[OUTRULID]");

        // es相关配置
        ElasticsearchOutputConfig elasticsearchOutputConfig = new ElasticsearchOutputConfig();
        elasticsearchOutputConfig.setTargetElasticsearch("default");
        elasticsearchOutputConfig.setIndex(outerrule).setEsIdField("outrulid");

        importBuilder.setInputConfig(dbInputConfig).setOutputConfig(elasticsearchOutputConfig)
                .setUseJavaName(true).setUseLowcase(true).setPrintTaskLog(true).setBatchSize(20)
                .setLastValueColumn("OUTRULID").setStatusDbname("outrullogtable").setLastValueStorePath("outrullogtable_import")
                .setLastValueType(ImportIncreamentConfig.NUMBER_TYPE).setFromFirst(false).setAsynFlushStatusInterval(10000)
                .setFixedRate(false).setDeyLay(1000L).setPeriod(5000L)
                .setParallel(true).setQueue(10).setThreadCount(6).setContinueOnError(true).setAsyn(false)
                .addFieldValue("table", "TBL_OUTERRULE");

        // 设置任务执行拦截器，可以添加多个，定时任务每次执行的拦截器
        importBuilder.addCallInterceptor(new CallInterceptor() {
            @Override
            public void preCall(TaskContext taskContext) {
            }

            @Override
            public void afterCall(TaskContext taskContext) {
                if (taskContext != null) logger.info(taskContext.getJobTaskMetrics().toString());
            }

            @Override
            public void throwException(TaskContext taskContext, Throwable e) {
                if (taskContext != null) logger.info(taskContext.getJobTaskMetrics().toString(), e);
            }
        });

        importBuilder.setExportResultHandler(new ExportResultHandler<String, String>() {
            @Override
            public void success(TaskCommand<String, String> taskCommand, String result) {
                TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                logger.info(taskMetrics.toString());
                logger.info(result);
            }

            @Override
            public void error(TaskCommand<String, String> taskCommand, String result) {
                TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                logger.info(taskMetrics.toString());
                logger.info(result);
            }

            @Override
            public void exception(TaskCommand<String, String> taskCommand, Throwable exception) {
                TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                logger.info(taskMetrics.toString());
            }

        });

        // 创建数据同步作业
        DataStream dataStream = importBuilder.builder();
        // 启动数据库表数据导入es作业
        dataStream.execute();
        logger.info("{} db2ESImport job started.", this.getClass().getName());
    }

}



