package com.huabo.etl.service;

import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.domain.XRepository;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;

/**
 * @ClassName : Ikettle
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-02 11:20:27
 */
public interface IkettleService {


    /**
     * 执行文件资源库转换
     *
     * @param namedParams 命名参数
     * @param clParams    命令行参数
     */
    public void callTrans(KettleTrans kettleTrans, XRepository xrepository, Map<String, String> namedParams, String[] clParams) throws Exception;

    /**
     * 执行文件资源库job
     *
     * @throws Exception
     */
    public boolean callJob(KettleJob kettleJob, XRepository xRepository, Map<String, String> variables, String[] clParams) throws Exception;

    /**
     * 加载转换
     *
     * @param repo      kettle文件资源库
     * @param transPath 相对路径
     * @param transName 转换名称
     */
    public TransMeta loadTrans(KettleFileRepository repo, String transPath, String transName) throws Exception;

    /**
     * 加载job
     *
     * @param repo    kettle文件资源库
     * @param jobPath 相对路径
     * @param jobName job名称
     */
    public JobMeta loadJob(KettleFileRepository repo, String jobPath, String jobName) throws Exception;

    /**
     * 调用trans文件 带参数的
     */
    public void callNativeTransWithParams(String[] params, String transName) throws Exception;


    /**
     * 配置kettle文件库资源库环境
     **/
    public KettleFileRepository fileRepositoryCon(XRepository xRepository) throws KettleException;

    // 调用Transformation示例
    public void runTrans(String filename);

}
