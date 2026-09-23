package com.huabo.es.service.trans;

import cn.hutool.core.collection.CollUtil;
import com.huabo.es.service.TblAccbookService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.frameworkset.tran.DataStream;
import org.frameworkset.tran.ExportResultHandler;
import org.frameworkset.tran.config.ImportBuilder;
import org.frameworkset.tran.metrics.TaskMetrics;
import org.frameworkset.tran.plugin.db.input.DBInputConfig;
import org.frameworkset.tran.plugin.es.output.ElasticsearchOutputConfig;
import org.frameworkset.tran.schedule.CallInterceptor;
import org.frameworkset.tran.schedule.TaskContext;
import org.frameworkset.tran.schedule.quartz.AbstractQuartzJobHandler;
import org.frameworkset.tran.task.TaskCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName : AccAssDataTran
 * @Description : 账簿数据导入
 * @Author : zhibo.cao
 * @Date: 2023-03-04 23:44:35
 */
@Component("accAssDataTran")
@RequiredArgsConstructor
public class AccAssDataTran extends AbstractQuartzJobHandler {
    private Logger logger = LoggerFactory.getLogger(AccAssDataTran.class);
    @Value("${spring.elasticsearch.bboss.db.name}")
    private String dbName;
    @NonNull
    private TblAccbookService tblAccbookService;
    private static String ACCINDEX = "acc";
    private static String ACCASS = "TBL_ACC_ASS";
    private static String ACCBAL = "TBL_ACC_BAL";
    private static String ACCBKPF = "TBL_ACC_BKPF";
    private static String ACCBSEG2019 = "TBL_ACC_BSEG2019";
    private static String ACCBSEG2020 = "TBL_ACC_BSEG2020";
    private static String ACCBSEG2018 = "TBL_ACC_BSEG2018";
    private static String ACCPERIOD = "TBL_ACC_PERIOD";
    private static String ACCREPORTMPROFIT = "TBL_ACC_REPORT_MPROFIT";
    private static String ACCREPORTYBAL = "TBL_ACC_REPORT_YBAL";
    private static String ACCREPORYPROFIT = "TBL_ACC_REPORT_YPROFIT";
    private static String ACCSUM = "TBL_ACC_SUM";
    private static String ACCOUNT = "TBL_ACCOUNT";
    private static String ACCSET = "TBL_ACCSET";
    private static String ASSBAL = "TBL_ASS_BAL";
    private static String ASBSEG = "TBL_ASS_BSEG";
    private static String ASSINFO = "TBL_ASS_INFO";
    private static String ASSSUM = "TBL_ASS_SUM";


    //    private static List<String> tables = CollUtil.newArrayList(ACCASS, ACCBAL, ACCBKPF, ACCBSEG2018,
//            ACCBSEG2019, ACCBSEG2020, ACCPERIOD, ACCREPORTMPROFIT, ACCREPORTYBAL, ACCREPORYPROFIT,
//            ACCSUM, ACCOUNT, ACCSET, ASSBAL, ASBSEG, ASSINFO, ASSSUM);
    private static List<String> tables = CollUtil.newArrayList(ACCBKPF, ACCSUM, ASSINFO, ASSBAL, ASSSUM,
            ACCOUNT, ACCBAL, ACCBSEG2018, ACCBSEG2019, ACCBSEG2020);

    @Override
    public void init() {
        List<String> acctids = tblAccbookService.queryDistinctByAcctid();
        if (CollUtil.isEmpty(acctids)) {
            return;
        }
        for (String acctid : acctids) {
            for (String table : tables) {
                Integer exists = tblAccbookService.checkTableExists(acctid, table);
                logger.info("是否存在表{}，结果{}", table, exists == 1);
                if (exists == 1) {
                    ImportBuilder importBuilder = ImportBuilder.newInstance();
                    // db相关配置
                    DBInputConfig dbInputConfig = new DBInputConfig();
                    dbInputConfig.setDbName(dbName);
                    dbInputConfig.setSql("SELECT * FROM " + acctid + "." + table);

                    // es相关配置
                    ElasticsearchOutputConfig elasticsearchOutputConfig = new ElasticsearchOutputConfig();
                    elasticsearchOutputConfig.setTargetElasticsearch("default");
                    elasticsearchOutputConfig.setIndex(ACCINDEX);

                    importBuilder.setInputConfig(dbInputConfig).setOutputConfig(elasticsearchOutputConfig)
                            .setUseJavaName(true).setUseLowcase(true).setPrintTaskLog(true).setBatchSize(20).setExternalTimer(true)
                            .setParallel(true).setQueue(10).setThreadCount(6).setContinueOnError(true).setAsyn(false)
                            .addFieldValue("table", table).addFieldValue("owner", acctid);


                    // 设置任务执行拦截器，可以添加多个，定时任务每次执行的拦截器
                    importBuilder.addCallInterceptor(new CallInterceptor() {
                        @Override
                        public void preCall(TaskContext taskContext) {
                        }

                        @Override
                        public void afterCall(TaskContext taskContext) {
                            if (taskContext != null)
                                logger.info(taskContext.getJobTaskMetrics().toString());
                        }

                        @Override
                        public void throwException(TaskContext taskContext, Throwable e) {
                            if (taskContext != null)
                                logger.info(taskContext.getJobTaskMetrics().toString(), e);
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
                            logger.info("插入es报错:{}", taskMetrics.toString());
                            logger.info("插入es报错:{}", result);
                        }

                        @Override
                        public void exception(TaskCommand<String, String> taskCommand, Throwable exception) {
                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                            logger.info("插入es异常:{}", taskMetrics.toString());
                        }
                    });

                    // 创建数据同步作业
                    DataStream dataStream = importBuilder.builder();
                    // 启动数据库表数据导入es作业
                    dataStream.execute();
                    logger.info("{} db2ESImport job started.", this.getClass().getName());
                }
            }
        }

    }

}



