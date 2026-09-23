package com.huabo.es.service;//package com.huabo.etl.service;
//
//import org.frameworkset.tran.DataStream;
//import org.frameworkset.tran.ExportResultHandler;
//import org.frameworkset.tran.config.ImportBuilder;
//import org.frameworkset.tran.metrics.TaskMetrics;
//import org.frameworkset.tran.plugin.db.input.DBInputConfig;
//import org.frameworkset.tran.plugin.es.output.ElasticsearchOutputConfig;
//import org.frameworkset.tran.schedule.CallInterceptor;
//import org.frameworkset.tran.schedule.ImportIncreamentConfig;
//import org.frameworkset.tran.schedule.TaskContext;
//import org.frameworkset.tran.task.TaskCommand;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName : AutoscheduleDataTran
// * @Description : TODO
// * @Author : zhibo.cao
// * @Date: 2023-03-03 10:40:56
// */
//@Component("autoscheduleDataTran")
//@Order(10)
//public class AutoscheduleDataTran implements CommandLineRunner {
//    private Logger logger = LoggerFactory.getLogger(AutoscheduleDataTran.class);
//    @Value("${spring.elasticsearch.bboss.db.name}")
//    public String dbName;
//    private ImportBuilder db2ESImportBuilder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("-----------------" + dbName);
//        if (db2ESImportBuilder == null) {
//            synchronized (this) {
//                if (db2ESImportBuilder == null) {
//                    ImportBuilder importBuilder = ImportBuilder.newInstance();
//
//                    // db相关配置
//                    DBInputConfig dbInputConfig = new DBInputConfig();
//                    dbInputConfig.setDbName(dbName);
//                    dbInputConfig.setSql("select * from sys_user where create_time > #[create_time]");
//
//                    // es相关配置
//                    ElasticsearchOutputConfig elasticsearchOutputConfig = new ElasticsearchOutputConfig();
//                    elasticsearchOutputConfig.setTargetElasticsearch("default");
//                    elasticsearchOutputConfig.setIndex("dbdemo").setEsIdField("user_id");
//
//                    importBuilder.setInputConfig(dbInputConfig)
//                            .setOutputConfig(elasticsearchOutputConfig).setUseJavaName(true).setPrintTaskLog(true).setBatchSize(100)
//                            .setLastValueColumn("create_time").setFromFirst(false).setStatusDbname("controllogtable").setLastValueStorePath("controllogtable_import")
//                            .setLastValueType(ImportIncreamentConfig.TIMESTAMP_TYPE).setAsynFlushStatusInterval(10000)
//                            .setFixedRate(false).setDeyLay(1000L).setPeriod(6000L)
//                            .setParallel(true).setQueue(10).setThreadCount(6).setContinueOnError(true).setAsyn(false);
//
//                    // 设置任务执行拦截器，可以添加多个，定时任务每次执行的拦截器
//                    importBuilder.addCallInterceptor(new CallInterceptor() {
//                        @Override
//                        public void preCall(TaskContext taskContext) {
//                        }
//
//                        @Override
//                        public void afterCall(TaskContext taskContext) {
//                            if (taskContext != null)
//                                logger.info(taskContext.getJobTaskMetrics().toString());
//                        }
//
//                        @Override
//                        public void throwException(TaskContext taskContext, Throwable e) {
//                            if (taskContext != null)
//                                logger.info(taskContext.getJobTaskMetrics().toString(), e);
//                        }
//                    });
//
//                    importBuilder.setExportResultHandler(new ExportResultHandler<String, String>() {
//                        @Override
//                        public void success(TaskCommand<String, String> taskCommand, String result) {
//                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
//                            logger.info(taskMetrics.toString());
//                            logger.info(result);
//                        }
//
//                        @Override
//                        public void error(TaskCommand<String, String> taskCommand, String result) {
//                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
//                            logger.info(taskMetrics.toString());
//                            logger.info(result);
//                        }
//
//                        @Override
//                        public void exception(TaskCommand<String, String> taskCommand, Throwable exception) {
//                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
//                            logger.info(taskMetrics.toString());
//                        }
//
//                    });
//
//                    // 创建数据同步作业
//                    DataStream dataStream = importBuilder.builder();
//                    // 启动数据库表数据导入es作业
//                    dataStream.execute();
//                    db2ESImportBuilder = importBuilder;
//                    logger.info("{} db2ESImport job started.", this.getClass().getName());
//                } else {
//                    logger.info("{} db2ESImport job has started.", this.getClass().getName());
//                }
//            }
//        } else {
//            logger.info("{} db2ESImport job has started.", this.getClass().getName());
//        }
//    }
//}
