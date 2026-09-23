package com.huabo.etl.service.impl;

import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.service.IkettleService;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class kettleServer implements IkettleService {
    public final Logger log = LoggerFactory.getLogger(kettleServer.class);


    private static String dbType;

    private static String db;

    private static String dbIp;

    private static String dbPort;

    private static String dbUser;

    private static String dbPassWord;

    private static String dbParam;

    @Value("${kettle.sqltype}")
    public void setDbType(String dbType) {
        kettleServer.dbType = dbType;
    }

    @Value("${kettle.db}")
    public void setDb(String db) {
        kettleServer.db = db;
    }

    @Value("${kettle.dbip}")
    public void setDbIp(String dbIp) {
        kettleServer.dbIp = dbIp;
    }

    @Value("${kettle.dbport}")
    public void setDbPort(String dbPort) {
        kettleServer.dbPort = dbPort;
    }

    @Value("${kettle.dbuser}")
    public void setDbUser(String dbUser) {
        kettleServer.dbUser = dbUser;
    }

    @Value("${kettle.dbpassword}")
    public void setDbPassWord(String dbPassWord) {
        kettleServer.dbPassWord = dbPassWord;
    }

    @Value("${kettle.dbparam}")
    public void setDbParam(String dbParam) {
        kettleServer.dbParam = dbParam;
    }

    /**
     * 执行文件资源库转换
     *
     * @param namedParams 命名参数
     * @param clParams    命令行参数
     */
    public void callTrans(KettleTrans kettleTrans, XRepository xrepository, Map<String, String> namedParams, String[] clParams) throws Exception {
        // 兼容mysql,oracle
        DatabaseMeta databaseMeta = new DatabaseMeta("kettle_trans_log", dbType, "Native(JDBC)",
                dbIp, db + dbParam, dbPort, dbUser, dbPassWord);
        String msg;
        KettleFileRepository repo = fileRepositoryCon(xrepository);
        TransMeta transMeta = null;
        try {
            transMeta = loadTrans(repo, kettleTrans.getTransPath(), kettleTrans.getTransName());
        } catch (Exception e) {
            throw e;
        }

        transMeta.addDatabase(databaseMeta);
        VariableSpace space = new Variables();
        TransLogTable jobLogTable = TransLogTable.getDefault(space, transMeta, null);
        jobLogTable.setTableName("kettle_trans_log");
        jobLogTable.setConnectionName("kettle_trans_log");
        transMeta.setTransLogTable(jobLogTable);
        //transMeta.getTransLogTable().setTableName(repInitialization.transLog);
        //转换
        Trans trans = new Trans(transMeta);
        //设置命名参数
        if (null != namedParams) {
            for (Iterator<Map.Entry<String, String>> it = namedParams.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                trans.setParameterValue(entry.getKey(), entry.getValue());
            }
        }
        trans.setLogLevel(getLogerLevel(kettleTrans.getTransLogLevel()));
        //执行
        trans.execute(clParams);
        trans.waitUntilFinished();

        KettleLogStore.discardLines(trans.getLogChannelId(), true);

        //记录日志
        String logChannelId = trans.getLogChannelId();
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
        log.info("[logTextlogText:" + logText + ":logTextlogText]");
        //抛出异常
        if (trans.getErrors() > 0) {
            msg = "There are errors during transformation exception!(转换过程中发生异常)";
            log.error(msg);
            throw new Exception(msg);
        }
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 执行文件资源库job
     *
     * @throws Exception
     */
    public boolean callJob(KettleJob kettleJob, XRepository xRepository, Map<String, String> variables, String[] clParams) throws Exception {
        String msg;
        DatabaseMeta databaseMeta = new DatabaseMeta("kettle_trans_log", dbType, "Native(JDBC)",
                dbIp, db + dbParam, dbPort, dbUser, dbPassWord);
        KettleFileRepository repo = fileRepositoryCon(xRepository);
        JobMeta jobMeta = null;
        try {
            jobMeta = loadJob(repo, kettleJob.getJobPath(), kettleJob.getJobName());
        } catch (Exception e) {
            throw e;
        }
        jobMeta.addDatabase(databaseMeta);
        VariableSpace space = new Variables();
        space.setVariable("test", "fromDbName");
        JobLogTable jobLogTable = JobLogTable.getDefault(space, jobMeta);
        jobLogTable.setTableName("kettle_job_log");
        jobLogTable.setConnectionName("kettle_job_log");
        jobMeta.setJobLogTable(jobLogTable);

        Job job = new Job(repo, jobMeta);
        //向Job 脚本传递参数，脚本中获取参数值：${参数名}
        if (null != variables) {
            for (Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                job.setVariable(entry.getKey(), entry.getValue());
            }
        }
        //设置日志级别
        job.setLogLevel(getLogerLevel(kettleJob.getJobLogLevel()));
        job.setArguments(clParams);
        job.start();

        job.waitUntilFinished();
        //记录日志
        String logChannelId = job.getLogChannelId();
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
        log.info(logText);
        if (job.getErrors() > 0) {
            msg = "There are errors during job exception!(执行job发生异常)";
            log.error(msg);
            throw new Exception(msg);
        }
        return true;
    }

    /**
     * 加载转换
     *
     * @param repo      kettle文件资源库
     * @param transPath 相对路径
     * @param transName 转换名称
     */
    public TransMeta loadTrans(KettleFileRepository repo, String transPath, String transName) throws Exception {
        String msg;
        RepositoryDirectoryInterface dir = repo.findDirectory(transPath);//根据指定的字符串路径找到目录
        if (null == dir) {
            msg = "kettle资源库转换路径不存在【" + repo.getRepositoryMeta().getBaseDirectory() + transPath + "】！";
            throw new Exception(msg);
        }
        TransMeta transMeta = null;
        try {
            transMeta = repo.loadTransformation(repo.getTransformationID(transName, dir), null);
        } catch (Exception e) {
            msg = "kettle资源库【" + dir.getPath() + "】不存在该转换【" + transName + "】！";
            throw new Exception(msg);
        }
        if (null == transMeta) {
            msg = "kettle资源库【" + dir.getPath() + "】不存在该转换【" + transName + "】！";
            throw new Exception(msg);
        }
        return transMeta;
    }

    /**
     * 加载job
     *
     * @param repo    kettle文件资源库
     * @param jobPath 相对路径
     * @param jobName job名称
     */
    public JobMeta loadJob(KettleFileRepository repo, String jobPath, String jobName) throws Exception {
        String msg;
        RepositoryDirectoryInterface dir = repo.findDirectory(jobPath);//根据指定的字符串路径找到目录
        if (null == dir) {
            msg = "kettle资源库Job路径不存在【" + repo.getRepositoryMeta().getBaseDirectory() + jobPath + "】！";
            throw new Exception(msg);
        }
        JobMeta jobMeta = null;
        try {
            jobMeta = repo.loadJob(repo.getJobId(jobName, dir), null);
        } catch (Exception e) {
            msg = "kettle资源库【" + dir.getPath() + "】不存在该Job【" + jobName + "】！";
            throw new Exception(msg);
        }
        if (null == jobMeta) {
            msg = "kettle资源库【" + dir.getPath() + "】不存在该Job【" + jobName + "】！";
            throw new Exception(msg);
        }
        return jobMeta;
    }

    /**
     * 调用trans文件 带参数的
     */
    public void callNativeTransWithParams(String[] params, String transName) throws Exception {
        // 初始化
        KettleEnvironment.init();
        EnvUtil.environmentInit();
        TransMeta transMeta = new TransMeta(transName);
        //转换
        Trans trans = new Trans(transMeta);
        //执行
        trans.execute(params);
        //等待结束
        trans.waitUntilFinished();
        //抛出异常
        if (trans.getErrors() > 0) {
            throw new Exception("There are errors during transformation exception!(传输过程中发生异常)");
        }
    }

    /**
     * 取得kettle的日志级别
     */
    private LogLevel getLogerLevel(String level) {
        LogLevel logLevel;
        if ("basic".equals(level)) {
            logLevel = LogLevel.BASIC;
        } else if ("detail".equals(level)) {
            logLevel = LogLevel.DETAILED;
        } else if ("error".equals(level)) {
            logLevel = LogLevel.ERROR;
        } else if ("debug".equals(level)) {
            logLevel = LogLevel.DEBUG;
        } else if ("minimal".equals(level)) {
            logLevel = LogLevel.MINIMAL;
        } else if ("rowlevel".equals(level)) {
            logLevel = LogLevel.ROWLEVEL;
        } else if ("nothing".endsWith(level)) {
            logLevel = LogLevel.NOTHING;
        } else {
            logLevel = null;
        }
        return logLevel;
    }

    /**
     * 配置kettle文件库资源库环境
     **/
    public KettleFileRepository fileRepositoryCon(XRepository xRepository) throws KettleException {
        String msg;
        //资源库元对象
        KettleFileRepositoryMeta fileRepositoryMeta = new KettleFileRepositoryMeta(String.valueOf(xRepository.getId()), xRepository.getRepoName(), xRepository.getRepoName(), xRepository.getBaseDir());
        // 文件形式的资源库
        KettleFileRepository repo = new KettleFileRepository();
        repo.init(fileRepositoryMeta);
        //连接到资源库
        repo.connect("", "");//默认的连接资源库的用户名和密码

        if (repo.isConnected()) {
            msg = "kettle文件库资源库【" + xRepository.getBaseDir() + "】连接成功";
            log.info(msg);
            return repo;
        } else {
            msg = "kettle文件库资源库【" + xRepository.getBaseDir() + "】连接失败";
            log.error(msg);
            throw new KettleException(msg);
        }
    }

    // 调用Transformation示例
    public void runTrans(String filename) {
        try {
            KettleEnvironment.init();
            TransMeta transMeta = new TransMeta(filename);
            Trans trans = new Trans(transMeta);
            trans.execute(null);// 执行转换
            trans.waitUntilFinished(); // 等待转换执行结束
            if (trans.getErrors() != 0) {
                System.out.println("Error");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
